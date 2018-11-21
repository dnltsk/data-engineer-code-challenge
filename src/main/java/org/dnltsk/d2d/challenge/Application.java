package org.dnltsk.d2d.challenge;

import org.dnltsk.d2d.challenge.write.DbInitiator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {

	@Autowired
	private DbInitiator dbInitiator;

	@PostConstruct
	public void initDatabase(){
		dbInitiator.resetDb();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
