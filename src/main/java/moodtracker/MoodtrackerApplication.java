package moodtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoodtrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoodtrackerApplication.class, args);
	}

}
