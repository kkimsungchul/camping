package com.sungchul.camping.reservation;

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

import java.util.HashMap;
import java.util.Map;


@Slf4j
@AllArgsConstructor
@Service("reservationService")
public class ReservationService {



    public void getReservationList(String method , String url,MultiValueMap<String,String> multiValueMap){

        ResponseEntity<String> responseEntity = callApi(method,url,multiValueMap);
        Map<String,Object> map = jsonToMap(responseEntity.getBody());
        System.out.println(map);
    }


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

    public Map<String,Object> jsonToMap(String body){
        JsonParser jsonParser = JsonParserFactory.getJsonParser();
        return jsonParser.parseMap(body);
    }

}
