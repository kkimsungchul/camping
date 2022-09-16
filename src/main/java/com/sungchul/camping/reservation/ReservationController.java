package com.sungchul.camping.reservation;


import com.sungchul.camping.common.ResponseAPI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.rmi.ServerError;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@Api(tags = {"캠핑장 예약 확인 컨트롤러"})
public class ReservationController {


    ReservationService reservationService;


//    @GetMapping("/reservation")
//    @ApiOperation(
//            httpMethod = "GET",
//            value="예약목록 가져오는지 테스트" ,
//            notes="예약목록 가져오는지 테스트")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "성공", response = Map.class),
//            @ApiResponse(code = 401, message = "권한없음", response = HttpClientErrorException.Forbidden.class),
//            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.Forbidden.class),
//            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class),
//    })
//    public ResponseEntity<ResponseAPI> reservation() throws Exception{
//        ResponseAPI responseAPI  = new ResponseAPI();
//        responseAPI.setData(reservationService.getReservationTrueList());
//        return new ResponseEntity<>(responseAPI,HttpStatus.OK);
//    }




}
