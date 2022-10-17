package com.sungchul.camping.reservation;


import com.sungchul.camping.common.DateUtil;
import com.sungchul.camping.telegram.TelegramService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Service("camping808ReservationService")
public class Camping808ReservationService {

    TelegramService telegramService;


    public void aa(){
        DateUtil dateUtil = new DateUtil();
        ArrayList<HashMap<String,String>> saturdays =  dateUtil.getSaturdays();
        //예약페이지 URL
        String url = "http://r.camperstory.com/resMain.hbb?reserve_path=RP&campseq=3866";

        for(int i=0;i<saturdays.size();i++){
            getReservation(saturdays.get(i));


        }

    }

    //A1 , A9 , B6, B1
    //campseq A존 : 14544 , B존 : 14545 , C존 : 14546
    public void getReservation(HashMap<String,String> weekMap){
        Map<String,String> map = new HashMap<>();
        String [] siteArr = {"14544" , "14545"}; //14546 C존은 넣지 않음

        map.put("p_date",weekMap.get("today"));
        map.put("res_dt",weekMap.get("saturday"));
        map.put("res_edt",weekMap.get("saturday"));
        map.put("res_edt2",weekMap.get("sunday"));
        map.put("ser_res_days","1");
//        map.put("res_able_dt","20221206");
//        map.put("old_res_dt","20221102");
        map.put("campseq","3866");
        map.put("ser_yn","Y");
        map.put("pCnt","1");
        map.put("mCnt","2");
        map.put("tCnt","13");
        map.put("ViewImgPage","$1$");
        map.put("res_max_dt","6");
        map.put("res_tp","");
        map.put("sel_res_days","2");
        for(int i=0; i<siteArr.length;i++){
            map.put("campsiteseq",siteArr[0]);
            parsingData(map);
        }

    }

    public HashMap<String,String> parsingData(Map<String,String> map){
        HashMap<String,String> resultMap = new HashMap<>();
        String url = "http://r.camperstory.com/resStep.hbb";

        try{
            Document doc = Jsoup
                    .connect(url)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .data(map)
                    .post();
            System.out.println(doc);
        }catch (Exception e){

        }

        return resultMap;
    }




}
