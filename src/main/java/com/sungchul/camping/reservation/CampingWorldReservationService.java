package com.sungchul.camping.reservation;

import com.sungchul.camping.common.DateUtil;
import com.sungchul.camping.schedule.ReservationScheduleData;
import com.sungchul.camping.telegram.TelegramService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*충주호 캠핑월드 파싱 서비스*/

@Slf4j
@AllArgsConstructor
@Service("campingWorldReservationService")
public class CampingWorldReservationService {

    TelegramService telegramService;


    /**
     * 예약가능 여부 조회
     * @param week 1 월요일 2화요일 3 수요일 4 목요일 5 금요일 6 토요일 7 일요일
     * @return ArrayList<HashMap<String,Object>>
     * */
    //dayOfWeek 7 일요일 6
    //isReservable 예약가능 여부 , true 가능 , false 불가능
    public ArrayList<HashMap<String,Object>> getReservationTrueList(String week){
        String url = "https://booking.ddnayo.com/booking-calendar-status?accommodationId=13676";
        ArrayList<HashMap<String,Object>> reservationList= getReservationList();
        ArrayList<HashMap<String,Object>> resultList = new ArrayList<>();
        for(int i=0;i<reservationList.size();i++){
            ArrayList<HashMap<String,Object>> templist1 = (ArrayList<HashMap<String,Object>>)reservationList.get(i).get("columnDtos");

            //detailDtos
            for(int j=0;j<templist1.size();j++){
                ArrayList<HashMap<String,Object>> templist2 = (ArrayList<HashMap<String,Object>>)templist1.get(j).get("detailDtos");
                //reservationList.get(i).get("holidayName");  //공휴일이면 해당 필드에 값이 있음
                //reservationList.get(i).get("dayTypeName");  //주중 , 주말 , 공휴일, 공휴일 전날
                //토요일일 경우
                if(null!= templist1.get(j).get("dayOfWeek") &&templist1.get(j).get("dayOfWeek").toString().equals(week)){
                    for(int k=0;k<templist2.size();k++){
                        if(null!=templist2.get(k).get("isReservable") && (boolean)templist2.get(k).get("isReservable")==true && !templist2.get(k).get("roomName").toString().equalsIgnoreCase("일반오토")){
                            resultList.add(templist2.get(k));
                            String message = templist2.get(k).get("date").toString() + " 일 " + templist2.get(k).get("roomName").toString();
                            //여러번 발송하는것을 막히위해,
                            if(!ReservationScheduleData.overlapHashSet.contains(message)){
                                telegramService.sendTelegramMessage(message + " 예약 가능 " + templist2.get(k).get("salePrice").toString()+"원");
                                telegramService.sendTelegramMessage(url);



                                ReservationScheduleData.overlapHashSet.add(message);
                            }
                            //roomName
                            //salePrice
                        }
                    }
                }
            }
        }
        return resultList;
    }


    /**
     * 오늘 기준으로 해당 캠핑장의 모든 객실 예약상황 데이터를 가져옴
     * @return ArrayList<HashMap<String,Object>>
     * */
    public ArrayList<HashMap<String,Object>> getReservationList(){
        DateUtil dateUtil = new DateUtil();
        ArrayList<HashMap<String,Object>> reservationList = new ArrayList<>();
        //이번달
        String month = dateUtil.getMonth();
        String url = "https://booking.ddnayo.com/booking-calendar-api/calendar/accommodation/13676/reservation-calendar?month="+month+"&calendarTypeCode=PRICE_CALENDAR&channelCode=0030";
        String method = "GET";
        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
        Map<String,Object> nextMonthReservationMap = getReservation(method,url,multiValueMap);

        //다음달
        String month2 = dateUtil.addMonth();
        String url2 = "https://booking.ddnayo.com/booking-calendar-api/calendar/accommodation/13676/reservation-calendar?month="+month2+"&calendarTypeCode=PRICE_CALENDAR&channelCode=0030";
        MultiValueMap<String,String> multiValueMap2 = new LinkedMultiValueMap<>();
        Map<String,Object> thisMonthReservationMap2 = getReservation(method,url2,multiValueMap2);


        //이번달 다음달 합쳐서 리스트로 만듬
        reservationList.addAll(parsingReservableBody(nextMonthReservationMap));
        reservationList.addAll(parsingReservableBody(thisMonthReservationMap2));

//        for(int i=0;i<reservationList.size();i++){
//            System.out.println("### reservationList" + reservationList.get(i));
//        }

        return reservationList;
    }





    /**
     * 현재 예약 상황 데이터를 getReservation 메소드에서 파싱
     * @param Map<String,Object> map 메소드 호출방식
     * @return ArrayList<HashMap<String,Object>>
     * */
    public ArrayList<HashMap<String,Object>> parsingReservableBody(Map<String,Object> map){
        ArrayList <HashMap<String,Object>> resultList = new ArrayList<>();
        ArrayList <HashMap<String,Object>> tempList = new ArrayList<>();
        HashMap<String,ArrayList<HashMap<String,Object>>> tempMap = new HashMap<>();

        DateUtil dateUtil = new DateUtil();
        HashMap<String,Object> dataMap = (HashMap<String,Object>)map.get("data");
        ArrayList<HashMap<String,Object>> rowDtos = (ArrayList<HashMap<String,Object>>)dataMap.get("rowDtos");


        //
        for(int i=0;i<rowDtos.size();i++){
            if(dateUtil.dayEqual(rowDtos.get(i).get("startDate").toString())){
                resultList.add(rowDtos.get(i));
                tempMap.put(rowDtos.get(i).get("startDate").toString(),(ArrayList<HashMap<String,Object>>)rowDtos.get(i).get("columnDtos"));
            }
        }

        return resultList;
    }



    /**
     * 현재 예약 상황 목록 데이터 가져오기 - API호출
     * @param method 메소드 호출방식
     * @param url 호출할 url
     * @param multiValueMap 보낼 데이터
     * */
    public Map<String,Object> getReservation(String method , String url,MultiValueMap<String,String> multiValueMap){
        ResponseEntity<String> responseEntity = callApi(method,url,multiValueMap);
        Map<String,Object> map = jsonToMap(responseEntity.getBody());
        return map;
    }


    /**
     * API 호출
     * @param method 메소드 호출방식
     * @param url 호출할 url
     * @param multiValueMap 보낼 데이터
     * @return ResponseEntity<String>
     * */
    public ResponseEntity<String> callApi(String method , String url , MultiValueMap<String,String> multiValueMap ){
        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(multiValueMap);
        ResponseEntity<String> responseEntity;
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000); //타임아웃 설정 5초
        factory.setReadTimeout(5000);//타임아웃 설정 5초
        RestTemplate restTemplate = new RestTemplate(factory);
        HttpHeaders header = new HttpHeaders();

        if(method.equalsIgnoreCase("GET")){
            return responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity,String.class);
        }else if(method.equalsIgnoreCase("POST")){
            return responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity,String.class);
        }else{
            return null;
        }
    }

    /**
     * JSON 을 Map 으로 변환
     * @param body Json타입의 데이터
     * @return Map<String,Object>
     * */
    public Map<String,Object> jsonToMap(String body){
        JsonParser jsonParser = JsonParserFactory.getJsonParser();
        return jsonParser.parseMap(body);
    }

}
