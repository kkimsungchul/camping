package com.sungchul.camping.reservation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Slf4j
@AllArgsConstructor
@Service("reservationService")
public class ReservationService {



    public void getReservationList(){
        String month = "202209";
        String url = "https://booking.ddnayo.com/booking-calendar-api/calendar/accommodation/13676/reservation-calendar?month="+month+"&calendarTypeCode=PRICE_CALENDAR&channelCode=0030";
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000); //타임아웃 설정 5초
        factory.setReadTimeout(5000);//타임아웃 설정 5초
        RestTemplate restTemplate = new RestTemplate(factory);
        HttpHeaders header = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(header);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url.toString(), HttpMethod.GET, entity,String.class);
        /* log.info("### resList : {}" ,resList);*/
        System.out.println("### responseEntity : " + responseEntity.getBody());



    }
}
