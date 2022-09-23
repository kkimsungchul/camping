package com.sungchul.camping.telegram;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

@Service("telegramService")
public class TelegramService {


    /**
     * 텔레그램 메시지 발송
     * @param message 보낼 메시지
     * */
    public void sendTelegramMessage(String message){
        String Token = "5679884325:AAHGkslnv9lMvdI3jPeL2dORoIbUwh8drG0";
        ArrayList<String> chatIdList = new ArrayList<>();
        chatIdList.add("5633077612");       //나
        chatIdList.add("5592564880");       //혜니


        BufferedReader in = null;

        for(String chatId : chatIdList){
            try {
                //URLEncoder.encode 를 추가하지 않으면 서버에서 작동시 해당 데이터는 넘어가지 않음, 꼭 해서 넘기자
                URL obj = new URL("https://api.telegram.org/bot" + Token + "/sendmessage?chat_id=" + chatId + "&text=" + URLEncoder.encode(message,"UTF-8")); // 호출할 url

                HttpURLConnection con = (HttpURLConnection)obj.openConnection();
                con.setRequestMethod("GET");
                in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                String line;

                while((line = in.readLine()) != null) { // response를 차례대로 출력
                    System.out.println(line);
                }

            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if(in != null) try { in.close(); } catch(Exception e) { e.printStackTrace(); }
            }
        }
    }
}
