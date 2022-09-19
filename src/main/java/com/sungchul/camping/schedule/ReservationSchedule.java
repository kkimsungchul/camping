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
     * 캠핑정보 파싱 스케줄러
     * 매분마다 시작
     * */
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void parsingSchedule(){
        log.info("---------------camping Reservation Schedule Start---------------");
        if(ReservationScheduleData.flag){
            reservationService.getReservationTrueList();
        }else{
            log.info("---------------ReservationScheduleData flag : false, Schedule not working---------------");
        }
        log.info("---------------camping Reservation Schedule End---------------");
    }

    /**
     * 파싱한 데이터를 삭제하는 컨트롤러
     * 매5분마다 시작
     * */
    @Scheduled(cron = "30 0/5 * * * *", zone = "Asia/Seoul")
    public void parsingDatClear(){
        log.info("---------------camping Parsing Data clear Start---------------");
        ReservationScheduleData.overlapHashSet.clear();
        log.info("---------------camping Parsing Data clear End---------------");
    }

}
