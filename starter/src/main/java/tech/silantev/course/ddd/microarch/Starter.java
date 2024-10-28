package tech.silantev.course.ddd.microarch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// сканирование работает без указания пакетов, т.к. Starter лежит в самом корне относительно других модулей
@SpringBootApplication
public class Starter {
    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }
}
