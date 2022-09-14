package com.sungchul.camping.reservation;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class ReservationController {


    ReservationService reservationService;


    @GetMapping("/reservation")
    public void reservation(){
        String month = "202209";
        String url = "https://booking.ddnayo.com/booking-calendar-api/calendar/accommodation/13676/reservation-calendar?month="+month+"&calendarTypeCode=PRICE_CALENDAR&channelCode=0030";
        String method = "GET";
        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
        reservationService.getReservationList(method,url,multiValueMap);
    }
}
