package com.kodyfikator.Kodyfikator;

import com.kodyfikator.Kodyfikator.service.DataService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ DataService.class })
public class KodyfikatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(KodyfikatorApplication.class, args);
	}

}
