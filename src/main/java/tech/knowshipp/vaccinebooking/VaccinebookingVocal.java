package tech.knowshipp.vaccinebooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import tech.knowshipp.vaccinebooking.dtos.UserSubscribe;

@SpringBootApplication
@EnableAsync
public class VaccinebookingVocal {
	
	public static void main(String[] args) {
		SpringApplication.run(VaccinebookingVocal.class, args);
	}

	@Bean
	public RestTemplate getRest() {
		return new RestTemplate();
	}
	
	@Bean("subscribed")
	public UserSubscribe subscribed() {
		UserSubscribe user = new UserSubscribe();
		user.setPinCode("201301");
		return user;
	}
}
