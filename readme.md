para ejecutar `./gradlew bootRun`



para kill un purto y ejecutar

lsof -i :8080

COMMAND   PID            USER   FD   TYPE             DEVICE SIZE/OFF NODE NAME
java    55836 yeffreyespinoza   64u  IPv6 0x84a114d066641e97      0t0  TCP *:http-alt (LISTEN)
yeffreyespinoza@yeffreys-MacBook-Air maquinarias % kill -9 55836


## configuara base de datos

en application.properties para indicar la conexcoin
en build.gradle dependencias

como poner los tests
