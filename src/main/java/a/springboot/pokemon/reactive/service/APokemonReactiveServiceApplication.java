package a.springboot.pokemon.reactive.service;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.core.DatabaseClient;

@SpringBootApplication
public class APokemonReactiveServiceApplication {

	private static final Logger log = LoggerFactory.getLogger(APokemonReactiveServiceApplication.class);

	public static void main(String[] args) {
		log.info("-----------------------------------------");
		log.info("[ App ] main() starting !", Arrays.toString(args));
		log.info("-----------------------------------------");

		SpringApplication.run(APokemonReactiveServiceApplication.class, args);

		log.info("-----------------------------------------");
		log.info("[ App ] main() end !", Arrays.toString(args));
		log.info("-----------------------------------------");
	}

	public APokemonReactiveServiceApplication(AppContact contact) {
		log.info("[ App ] [ APokemonReactiveServiceApplication ] constructor");
		log.info("[ Contact ] {}", contact.getName());
		log.info("[ Contact ] {}", contact.getEmail());
		log.info("[ Contact ] {}", contact.getUrl());
	}

	@Bean
	public CommandLineRunner initApp(AppConfig config) {
		return (args) -> {
			log.info("[ AppConfig ] {}", config.info().getTitle());
			log.info("[ AppConfig ] {}", config.info().getVersion());
			log.info("[ AppConfig ] {}", config.info().getDescription());
		};
	}

	@Bean
	CommandLineRunner initDb(DatabaseClient databaseClient) {
		return args -> {
			log.info("[ DB initialization ]");
			log.info("-------------------------------");

			databaseClient.execute("\r\n\r\n\tDROP TABLE IF EXISTS pokemon_response;\r\n" + 
					"CREATE TABLE pokemon_response (\r\n" + 
					"	`uuid` UUID NOT NULL PRIMARY KEY,\r\n" + 
					"	`json` VARCHAR(100000) NOT NULL,\r\n" + 
					"	`created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\r\n" + 
					"	`updated` timestamp\r\n" + 
					");\r\n" + 
					"\r\n" + 
					"DROP TABLE IF EXISTS pokemon;\r\n" + 
					"CREATE TABLE pokemon (\r\n" + 
					"	`uuid` UUID NOT NULL PRIMARY KEY,\r\n" + 
					"	`id` INTEGER NOT NULL UNIQUE,\r\n" + 
					"	`name` VARCHAR(50) NOT NULL UNIQUE,\r\n" + 
					"	`json` VARCHAR(300000) NOT NULL,\r\n" + 
					"	`created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\r\n" + 
					"	`updated` timestamp\r\n" + 
					");\r\n" + 
					"\r\n" + 
					"DROP TABLE IF EXISTS ability;\r\n" + 
					"CREATE TABLE ability (\r\n" + 
					"	`uuid` UUID NOT NULL PRIMARY KEY,\r\n" + 
					"	`id` INTEGER NOT NULL UNIQUE,\r\n" + 
					"	`name` VARCHAR(50) NOT NULL UNIQUE,\r\n" + 
					"	`json` VARCHAR(100000) NOT NULL,\r\n" + 
					"	`created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\r\n" + 
					"	`updated` timestamp\r\n" + 
					");").fetch().all().log().subscribe();
		};
	}
}
