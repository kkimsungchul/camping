package com.sungchul.camping.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DateUtil {
    /*yyyymmdd 로 현재 날짜 리턴*/
    public String getDate() {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String nowString = now.format(dateTimeFormatter);
        return nowString;
    }

    /*yyyy 로 현재 연도 리턴*/
    public String getYear() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy");
        String nowString = now.format(dateTimeFormatter);
        return nowString;
    }
    /*HHmmss 로 현재 시간 리턴*/
    public String getTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HHmmss");
        String nowString = now.format(dateTimeFormatter);
        return nowString;
    }
    /*지정한 형식으로 출력*/
    public String getTime(String strformat){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(strformat);
        String nowString = now.format(dateTimeFormatter);
        return nowString;
    }

    public String getMonth(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM");
        String nowString = now.format(dateTimeFormatter);
        return nowString;
    }

    public String addMonth(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM");
        String nowString = now.plusMonths(1).format(dateTimeFormatter);
        return nowString;
    }


    /**
     * 입력받은 날짜의 요일을 구함
     * @param day yyyy-MM-dd 형식으로 입력
     * @return result , 1부터 시작하며 1은 월요일 7은 일요일
     * */
    public int  getDayOfWeek(String day){
        String splitString[] = day.split("-");
        LocalDate now = LocalDate.of(
                Integer.parseInt(splitString[0])
                ,Integer.parseInt(splitString[1])
                ,Integer.parseInt(splitString[2]));

        return now.getDayOfWeek().getValue();
    }

    /**
     * 현재 날짜가 입력받은 날짜보다 이전인지 구함
     * @param day yyyy-MM-dd 형식으로 입력
     * @return Boolean , true 이전임 , false 지났음
     * */
    public boolean dayEqual(String day){
        String splitString[] = day.split("-");
        LocalDate getDate = LocalDate.of(
                Integer.parseInt(splitString[0])
                ,Integer.parseInt(splitString[1])
                ,Integer.parseInt(splitString[2]));
        LocalDate now = LocalDate.now();
        return now.isBefore(getDate);

    }


}
