package com.sungchul.camping.reservation;


import com.sungchul.camping.common.ResponseAPI;
import com.sungchul.camping.schedule.ReservationScheduleData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.rmi.ServerError;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/reservation")
@Api(tags = {"캠핑장 예약 확인 컨트롤러"})
public class ReservationController {


    CampingWorldReservationService campingWorldReservationService;

    Camping808ReservationService camping808ReservationService;

    @GetMapping("/campingWorld")
    @ApiOperation(
            httpMethod = "GET",
            value="충주 캠핑월드 예약목록 가져오는지 테스트" ,
            notes="충주 캠핑월드 예약목록 가져오는지 테스트")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 401, message = "권한없음", response = HttpClientErrorException.Forbidden.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.Forbidden.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class),
    })
    public ResponseEntity<ResponseAPI> campingWorldReservation(String week) throws Exception{
        ReservationScheduleData.overlapHashSet.clear();
        ResponseAPI responseAPI  = new ResponseAPI();
        responseAPI.setData(campingWorldReservationService.getReservationTrueList(week));
        return new ResponseEntity<>(responseAPI,HttpStatus.OK);
    }

    @GetMapping("/camping808")
    @ApiOperation(
            httpMethod = "GET",
            value="캠핑808 예약목록 가져오는지 테스트" ,
            notes="캠핑808 예약목록 가져오는지 테스트")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 401, message = "권한없음", response = HttpClientErrorException.Forbidden.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.Forbidden.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class),
    })
    public ResponseEntity<ResponseAPI> camping808Reservation(String week) throws Exception{

        camping808ReservationService.aa();
        ResponseAPI responseAPI  = new ResponseAPI();
        return new ResponseEntity<>(responseAPI,HttpStatus.OK);
    }




}
