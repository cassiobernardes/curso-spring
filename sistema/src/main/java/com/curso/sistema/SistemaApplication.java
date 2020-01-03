package com.curso.sistema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SistemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaApplication.class, args);

        System.out.println(new BCryptPasswordEncoder().encode("bernardes"));
        System.out.println(new BCryptPasswordEncoder().encode("joao"));
    }

}
