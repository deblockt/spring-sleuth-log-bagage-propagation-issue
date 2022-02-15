# Reproduction project for spring sleuth issue

This project is to reproduction baggage propagation issue using spring sleuth with spring-boot 2.6.3.

## dependencies

- spring-boot: 2.6.3
- r2dbc: 0.8.8.RELEASE
- spring-cloud: 2021.0.0
- spring-cloud-starter-sleuth: 3.1.0

## how to reproduce

run `docker-composer up -d` to start a postgres database 

start the app using `mvn spring-boot:run`

ok case: `curl http://localhost:8080/non-transactional`
You can see on logs 
```
external_id=  2022-02-15 11:36:32.596  INFO [,264d6538dcdc798d,264d6538dcdc798d] 22626 --- [ctor-http-nio-3] com.example.demo.DemoController          : Before Mono.defer on non transactional controller
external_id=external_id_value  2022-02-15 11:36:32.602  INFO [,264d6538dcdc798d,264d6538dcdc798d] 22626 --- [ctor-http-nio-3] com.example.demo.DemoController          : on Mono.defer
external_id=external_id_value 2022-02-15 11:36:32.883  INFO [,264d6538dcdc798d,264d6538dcdc798d] 22626 --- [actor-tcp-nio-1] com.example.demo.DemoController          : doOnNext.transactionalService 1
```

ko case: `curl http://localhost:8080/transactional`
```
external_id=  2022-02-15 11:42:31.265  INFO [,03068d207a0a71f4,535dbc5dbe9d11a4] 22797 --- [actor-tcp-nio-1] com.example.demo.DemoController          : Before Mono.defer on the transactional controller
external_id=  2022-02-15 11:42:31.266  INFO [,03068d207a0a71f4,535dbc5dbe9d11a4] 22797 --- [actor-tcp-nio-1] com.example.demo.DemoController          : on Mono.defer
external_id=  2022-02-15 11:42:31.269  INFO [,03068d207a0a71f4,03068d207a0a71f4] 22797 --- [actor-tcp-nio-1] com.example.demo.DemoController          : doOnNext.nontransactionalService 1
```

In the two cases the expected behaviour is to have the external_id value on logs `on Mono.defer` and `doOnNext....transactionalService`.
The difference is the location on `@Transactional` annotation. on `non-transactional` the annotation is on the service, on `transactional` the annotation is on the controller.