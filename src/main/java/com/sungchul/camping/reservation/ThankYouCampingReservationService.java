package com.sungchul.camping.reservation;


import com.sungchul.camping.common.DateUtil;
import com.sungchul.camping.schedule.ReservationScheduleData;
import com.sungchul.camping.telegram.TelegramService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/*땡큐캠핑 파싱 서비스*/
@Slf4j
@AllArgsConstructor
@Service("thankYouCampingReservationService")
public class ThankYouCampingReservationService {

    TelegramService telegramService;


    /**
     * 캠핑808 예약 가능 여부 조회
     * @return parsingList , 예약 가능한 날짜 목록
     * */
    @Async
    public ArrayList<HashMap<String,String>> getCamping808ReservationTrueList(){
        ArrayList<HashMap<String,String>> parsingList = new ArrayList<>();
        DateUtil dateUtil = new DateUtil();
        //오늘, 토요일,일요일을 가져옴
        ArrayList<HashMap<String,String>> saturdays =  dateUtil.getSaturdays(2);
        //예약페이지 URL
        String reservationUrl = "http://r.camperstory.com/resMain.hbb?reserve_path=RP&campseq=3866";
        String parsingUrl = "http://r.camperstory.com/resStep.hbb";
        String [] largeSiteArr = {"14544" , "14545" , "14546"}; //A존 : 14544  , B존 : 14545 , C존 : 14546캠핑 사이트 고유 번호

        for(int i=0;i<largeSiteArr.length;i++){
            for(int j=0;j<saturdays.size();j++){
                Map<String,String> map = setReservationMap(saturdays.get(j));
                map.put("campseq","3866");//캠핑장 고유 번호
                map.put("campsiteseq",largeSiteArr[i]);//캠핑장 사이트 고유 번호
                parsingList.addAll(camping808ParsingData(map,parsingUrl));
            }

        }

        if(parsingList.size()>0){
            for(int i=0;i<parsingList.size();i++){
                String day = parsingList.get(i).get("day").substring(0,4) + "-" + parsingList.get(i).get("day").substring(4,6) + "-" + parsingList.get(i).get("day").substring(6) + " 일 ";

                //여러번 발송하는것을 막히위해,
                String message = "캠핑808 : "+ day +parsingList.get(i).get("site");
                if(!ReservationScheduleData.overlapHashSet.contains(message)){
                    telegramService.sendTelegramMessage(message);
                    telegramService.sendTelegramMessage(reservationUrl);
                    ReservationScheduleData.overlapHashSet.add(message);
                }

            }
        }
        return parsingList;
    }



    /**
     * 예약 가능 여부 조회
     * 테스트로 작성해노았으며, campseq , sampsiteseq 만 수정하면됨, 또한 호출하는 parsingDataTest 메소드에서 파싱할 데이터들의 기준을 정하면됨
     * @return parsingList , 예약 가능한 날짜 목록
     * */
    @Async
    public ArrayList<HashMap<String,String>> test(){
        ArrayList<HashMap<String,String>> parsingList = new ArrayList<>();
        DateUtil dateUtil = new DateUtil();
        //오늘, 토요일,일요일을 가져옴
        ArrayList<HashMap<String,String>> saturdays =  dateUtil.getSaturdays(2);
        //예약페이지 URL
        String reservationUrl = "http://r.camperstory.com/resMain.hbb?reserve_path=RP&campseq=3443";
        String parsingUrl = "http://r.camperstory.com/resStep.hbb";
        String [] largeSiteArr = {"16115","12769"}; //캠핑장 사이트 고유 번호

        for(int i=0;i<largeSiteArr.length;i++){
            for(int j=0;j<saturdays.size();j++){
                Map<String,String> map = setReservationMap(saturdays.get(j));
                map.put("campseq","3443");//캠핑장 고유 번호
                map.put("campsiteseq",largeSiteArr[i]);//캠핑장 사이트 고유 번호
                parsingList.addAll(parsingDataTest(map,parsingUrl));
            }
        }

        if(parsingList.size()>0){
            for(int i=0;i<parsingList.size();i++){
                String day = parsingList.get(i).get("day").substring(0,4) + "-" + parsingList.get(i).get("day").substring(4,6) + "-" + parsingList.get(i).get("day").substring(6) + " 일 ";
                System.out.println(day + " : " + parsingList.get(i).get("site"));
                //telegramService.sendTelegramMessage("테스트 : "+ day +parsingList.get(i).get("site"));
                //telegramService.sendTelegramMessage(reservationUrl);
            }
        }
        return parsingList;
    }

    //A1 , A9 , B6, B1
    //campseq A존 : 14544 , B존 : 14545 , C존 : 14546
    /**
     * URL 호출 전 데이터 정제 및 세팅 작업 및 파싱 URL호출
     * @param defaultMap , 요일에 대한 정보가 들어있음 (오늘 : today , 토요일 : saturday , 일요일 : sunday)
     * */
    public Map<String,String> setReservationMap(HashMap<String,String> defaultMap){
        Map<String,String> map = new HashMap<>();

        map.put("p_date",defaultMap.get("today"));
        map.put("res_dt",defaultMap.get("saturday"));
        map.put("res_edt",defaultMap.get("saturday"));
        map.put("res_edt2",defaultMap.get("sunday"));
        map.put("ser_res_days","1");
//        map.put("res_able_dt","20221206");
//        map.put("old_res_dt","20221102");
        map.put("campseq",defaultMap.get("campseq"));
        map.put("ser_yn","Y");
        map.put("pCnt","1");
        map.put("mCnt","2");
        map.put("tCnt","13");
        map.put("ViewImgPage","$1$");
        map.put("res_max_dt","6");
        map.put("res_tp","");
        map.put("sel_res_days","2");

        return map;
    }

    /**
     * 캠핑808 조건에 맞는 데이터 파싱 하는 메소드
     * @param map , 데이터 파싱에 필요한 데이터를 넣은 맵
     * @param parsingUrl , 파싱할 URL
     * */
    public ArrayList<HashMap<String,String>> camping808ParsingData(Map<String,String> map , String parsingUrl){
        ArrayList<HashMap<String,String>> resultList = new ArrayList<>();
        String parsingData = callUrl(map,parsingUrl);
        //System.out.println(elements.text());
        String []splitString = parsingData.split("\\)");
        for(int i=0;i<splitString.length;i++){
            if(camping808DataCheck(splitString[i])){
                HashMap<String,String> resultMap = new HashMap<>();
                resultMap.put("site",splitString[i].replaceAll("\\(",""));
                resultMap.put("day",map.get("res_dt"));
                resultList.add(resultMap);
            }
        }
        return resultList;
    }
    /**
     * 캠핑808 사이트의 파싱한 데이터가 요구조건이 맞는지 검증
     * @param parsingString , 파싱한 데이터 문자열
     * */
    public boolean camping808DataCheck(String parsingString){
        String []smallSiteArr = {"A-1" , "A-9" , "B-1" ,"B-6" ,"C-1"};
        for(int i=0;i<smallSiteArr.length;i++){
            if(parsingString.contains("가능") && parsingString.contains(smallSiteArr[i])){
                return true;
            }
        }
        return false;

    }


    /**
     * xxx캠핑사이트 조건에 맞는 데이터 파싱 하는 메소드
     * @param map , 데이터 파싱에 필요한 데이터를 넣은 맵
     * @param parsingUrl , 파싱할 URL
     * */
    public ArrayList<HashMap<String,String>> parsingDataTest(Map<String,String> map , String parsingUrl){
        ArrayList<HashMap<String,String>> resultList = new ArrayList<>();
        String parsingData = callUrl(map,parsingUrl);
        //System.out.println(elements.text());
        String []splitString = parsingData.split("\\)");
        for(int i=0;i<splitString.length;i++){
            HashMap<String,String> resultMap = new HashMap<>();
            resultMap.put("site",splitString[i].replaceAll("\\(",""));
            resultMap.put("day",map.get("res_dt"));
            resultList.add(resultMap);
        }
        return resultList;
    }







    /**
     * URL 에서 데이터 받아오기
     * @param map , 데이터 파싱에 필요한 데이터를 넣은 맵
     * @param parsingUrl , 파싱할 URL
     * */
    public String callUrl(Map<String,String> map , String parsingUrl){
        try{
            Document doc = Jsoup
                    .connect(parsingUrl)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .data(map)
                    .post();
            //System.out.println(doc);
            Elements elements = doc.select("#container > div > div.imply_l > div.section.select_site > div.resInfo_wp > div");
            return elements.text();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }



}
