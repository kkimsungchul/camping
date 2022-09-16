package com.sungchul.camping.telegram;


import com.sungchul.camping.schedule.ReservationScheduleData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.ServerError;
import java.util.Map;


@Slf4j
@AllArgsConstructor
@RestController
@Api(tags = {"텔레그램 메시지 발송 컨트롤러"})
public class TelegramController {

    TelegramService telegramService;

    @GetMapping("/telegram/message")
    public void telegramMessage(){
        telegramService.sendTelegramMessage("");
    }

    @GetMapping("/telegram/stop")
    @ApiOperation(
            httpMethod = "GET",
            value="스케줄러를 통한텔레그램 메시지 발송 및 파싱 정지" ,
            notes="스케줄러를 통한텔레그램 메시지 발송 및 파싱 정지")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 401, message = "권한없음", response = HttpClientErrorException.Forbidden.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.Forbidden.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class),
    })
    public void telegramStop(){
        ReservationScheduleData.flag=false;
    }

    @GetMapping("/telegram/start")
    @ApiOperation(
            httpMethod = "GET",
            value="스케줄러를 통한텔레그램 메시지 발송 및 파싱 시작" ,
            notes="스케줄러를 통한텔레그램 메시지 발송 및 파싱 시작")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 401, message = "권한없음", response = HttpClientErrorException.Forbidden.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.Forbidden.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class),
    })
    public void telegramStart(){
        ReservationScheduleData.flag=true;
    }
}
