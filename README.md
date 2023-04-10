# 캠핑장 예약 가능 확인 프로그램

SpringBoot : SpringBoot 2.5.6 <br>

파싱하는 사이트 : 떠나요캠핑 , 땡큐캠핑<br>

Spring Schedule을 사용하여 예약가능한 캠핑 사이트 자리가 나왔을 경우 텔레그램으로 메시지를 발송해줍니다.<br>

AWS EC2에 올려 사용중이였으며, 날씨가 추워져 사용을 멈췄습니다.....<br>


TelegramService.java 파일의 Token 값은 텔레그램 봇 토큰입니다.<br>
해당 값을 본인이 생성한 봇의 토큰값으로 변경하면 됩니다.<br>

TelegramService.java 파일의 chatIdList 에는 본인의 텔레그램 ID를 넣어주시면 됩니다.<br>
해당ID는 봇이 있는 채팅방에 채팅을 치면 API를 통해 알수 있습니다.<br>
ex)
chatIdList.add("5633077612");       //나<br>
chatIdList.add("5592564880");       //혜니<br>



# 텔레그램 봇 생성 / 토큰발급방법 / 개인ID 확인 방법은 아래의 URL 에서 확인해주세요<br>
https://kkimsungchul.github.io/category/<br>
[Camping] 대상시스템 데이터 분석<br>
[Camping] 텔레그램 봇 생성<br>
[Camping] 땡큐캠핑 예약 시스템 분석<br>
[Camping] SpringBoot와 텔레그램 연동<br>