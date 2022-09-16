package com.sungchul.camping.schedule;


import com.sungchul.camping.reservation.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReservationSchedule {

    @Autowired
    ReservationService reservationService;

    /**
     * 주식 파싱 스케줄러
     * 월-금요일까지 매일 오후 8시에 시작
     * */
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void parsingSchedule(){
        log.info("---------------------------------------camping Reservation Schedule Start------------------------------------------------");
        if(ReservationScheduleData.flag){
            reservationService.getReservationTrueList();
        }else{
            log.info("---------------------------------------ReservationScheduleData flag : false, Schedule not working------------------------------------------------");
        }
        log.info("---------------------------------------camping Reservation Schedule End------------------------------------------------");

    }

}
