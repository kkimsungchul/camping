package com.sungchul.camping.schedule;


import com.sungchul.camping.reservation.ReservationService;
import com.sungchul.camping.telegram.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HealthCheckSchedule {

    @Autowired
    TelegramService telegramService;;

    /**
     * 캠핑정보 파싱 스케줄러
     * 매분마다 시작
     * */
    @Scheduled(cron = "0 0 0/6 * * *", zone = "Asia/Seoul")
    public void healthCheckSchedule(){
        log.info("---------------------------------------Health CheckS Schedule Start------------------------------------------------");
        telegramService.sendTelegramMessage("System Health Check - OK");
        log.info("---------------------------------------Health CheckS Schedule End------------------------------------------------");
    }

}
