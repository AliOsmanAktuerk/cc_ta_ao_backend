package de.aktuerk.brothers.cuc_ta_allaince_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CucTaAllainceManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CucTaAllainceManagerApplication.class, args);
    }

}
