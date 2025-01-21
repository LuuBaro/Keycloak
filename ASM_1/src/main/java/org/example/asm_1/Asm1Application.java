package org.example.asm_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Asm1Application {

    public static void main(String[] args) {
        SpringApplication.run(Asm1Application.class, args);
    }

}
