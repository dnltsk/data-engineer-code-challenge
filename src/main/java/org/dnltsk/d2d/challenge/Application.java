package org.dnltsk.d2d.challenge;

import lombok.extern.slf4j.Slf4j;
import org.dnltsk.d2d.challenge.write.DbInitiator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.sql.SQLException;

@SpringBootApplication
@Slf4j
public class Application {

	@Autowired
	private DbInitiator dbInitiator;

	@PostConstruct
	public void initDatabase(){
		try {
			dbInitiator.resetDb();
		} catch (SQLException e) {
			log.error("Cannot reset database -> Exiting Application!");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
