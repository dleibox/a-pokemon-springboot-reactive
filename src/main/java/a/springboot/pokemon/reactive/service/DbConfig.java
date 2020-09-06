package a.springboot.pokemon.reactive.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;

/**
 * 
 * @author daniel
 *
 * https://blog.dema.in/flux2.html
 * 
 */

@Configuration
@EnableR2dbcRepositories
public class DbConfig extends AbstractR2dbcConfiguration {

	@Bean
	public ConnectionFactory connectionFactory() {
		return new H2ConnectionFactory(H2ConnectionConfiguration.builder().username("sa").password("")
				.url("mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE").build());
		
//		return new PostgresqlConnectionFactory(
//                PostgresqlConnectionConfiguration.builder()
//                        .host("localhost")
//                        .port(5432)
//                        .database("demodb")
//                        .username("demouser")
//                        .password("demopwd")
//                        .build()
//        );
	}
}
