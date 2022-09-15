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


    /**
     *
     *
     * */
    @GetMapping("/reservation")
    public void reservation(){
        reservationService.getReservationList();
    }
}
