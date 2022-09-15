package com.sungchul.camping.reservation;

import com.sungchul.camping.common.DateUtil;
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


@Slf4j
@AllArgsConstructor
@Service("reservationService")
public class ReservationService {



    public void getReservationList(){
        //https://java119.tistory.com/52
//        DateUtil dateUtil = new DateUtil();
//        System.out.println(dateUtil.getDayOfWeek("2022-09-01"));
//        System.out.println(dateUtil.getDayOfWeek("2022-09-02"));
//        System.out.println(dateUtil.getDayOfWeek("2022-09-03"));
//        System.out.println("###");
//        System.out.println(dateUtil.dayEqual("2022-09-03"));
//        System.out.println(dateUtil.dayEqual("2022-09-20"));

        DateUtil dateUtil = new DateUtil();
        String month = dateUtil.getMonth();
        String url = "https://booking.ddnayo.com/booking-calendar-api/calendar/accommodation/13676/reservation-calendar?month="+month+"&calendarTypeCode=PRICE_CALENDAR&channelCode=0030";
        String method = "GET";
        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
        Map<String,Object> map = getReservation(method,url,multiValueMap);
        parsingReservable(map);


    }




    //isReservable 예약가능 여부 , true 가능 , false 불가능

    public ArrayList<HashMap<String,Object>> parsingReservable(Map<String,Object> map){
        ArrayList <HashMap<String,Object>> resultList = new ArrayList<>();
        ArrayList <HashMap<String,Object>> tempList = new ArrayList<>();
        HashMap<String,ArrayList<HashMap<String,Object>>> tempMap = new HashMap<>();

        DateUtil dateUtil = new DateUtil();
        HashMap<String,Object> dataMap = (HashMap<String,Object>)map.get("data");
        ArrayList<HashMap<String,Object>> rowDtos = (ArrayList<HashMap<String,Object>>)dataMap.get("rowDtos");


        //
        for(int i=0;i<rowDtos.size();i++){
            if(dateUtil.dayEqual(rowDtos.get(i).get("startDate").toString())){
                tempList.add(rowDtos.get(i));
                tempMap.put(rowDtos.get(i).get("startDate").toString(),(ArrayList<HashMap<String,Object>>)rowDtos.get(i).get("columnDtos"));
            }
        }
        System.out.println(tempMap);

        for(int i=0;i<tempList.size();i++){
            System.out.println("### tempList : " + tempList.get(i));
        }
        return resultList;
    }



    /**
     * 현재 예약 상황 목록 데이터 가져오기
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
