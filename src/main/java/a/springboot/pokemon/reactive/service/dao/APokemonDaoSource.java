package a.springboot.pokemon.reactive.service.dao;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import a.springboot.pokemon.reactive.service.exception.AException;
import a.springboot.pokemon.reactive.service.model.AbilityDetail;
import a.springboot.pokemon.reactive.service.model.PokemonDetail;
import a.springboot.pokemon.reactive.service.model.PokemonResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 * @author daniel
 * 
 *         https://www.baeldung.com/spring-5-webclient
 *         https://www.callicoder.com/spring-5-reactive-webclient-webtestclient-examples/
 *         https://dzone.com/articles/spring-boot-webclient-and-its-testing
 */

@Repository("source2")
public class APokemonDaoSource implements APokemonDao {

	private static final Logger log = LoggerFactory.getLogger(APokemonDaoSource.class);

	private WebClient webClient;

	public APokemonDaoSource(@Value("${service.base-url}") String baseURL) {
		log.info("[ DaoSource ] {}", baseURL);
		this.webClient = WebClient.builder().baseUrl(baseURL)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//		        .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.github.v3+json")
				.defaultHeader(HttpHeaders.USER_AGENT, "Spring 5 WebClient")
				.defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
				.filter(ExchangeFilterFunctions.basicAuthentication("username", "token")).filter(logFilter())
				.filters(fltr -> {
					fltr.add(logRequest());
					fltr.add(logResponse());
				}).defaultCookie("cookieKey", "cookieValue").build();
	}

	private ExchangeFilterFunction logFilter() {
		return (clientRequest, next) -> {
			log.info("[ PokeApi Request ] {} {}", clientRequest.method(), clientRequest.url());
			clientRequest.headers().forEach(
					(name, values) -> values.forEach(value -> log.info("[ PokeApi Request ] {}={}", name, value)));
			return next.exchange(clientRequest);
		};
	}

	private ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(req -> {
			log.info("[ PokeApi Request ]");
			req.headers().forEach((name, values) -> values.forEach(value -> {
				log.info("[ PokeApi Request] {}={}", name, value);
			}));
			return Mono.just(req);
		});
	}

	private ExchangeFilterFunction logResponse() {
		return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
			log.info("[ PokeApi Response ]");
			log.info("[ PokeApi Response ] {}", clientResponse.statusCode());
			log.info("[ PokeApi Response ] {}", clientResponse.bodyToFlux(String.class));
			return Mono.just(clientResponse);
		});
	}

	@Override
	public Flux<PokemonResponse> selectAllPokemons() { // Object.class
		return this.webClient.get().uri(ub -> ub.path("/pokemon").queryParam("limit", 1000).build())
//				.header("Authorization",
//						"Basic " + Base64Utils.encodeToString(("username" + ":" + "token").getBytes("UTF_8")))
				.retrieve().bodyToFlux(PokemonResponse.class); // .blockFirst();
	}

	// must set static here! otherwise error
//	private static class HashMapStr extends HashMap<String, String> {
//	}

//	public HashMap<String, String> selectAllPokemons() {
//		return this.webClient.get().uri("/pokemon").retrieve().bodyToFlux(HashMapStr.class).blockFirst();
//	}

//	public Mono<HashMap> selectAllPokemons() {
//		return this.webClient.get().url("").exchange().flatMap(res -> res.bodyToMono(HashMap.class));
//	}
//
//	public Mono<ResponseEntity<HashMap>> selectAllPokemons() {
//		return this.webClient.get().url("").exchange().flatMap(res -> res.toEntity(HashMap.class));
//	}

	@Override
	public Mono<PokemonDetail> selectPokemonById(String id) {
		return this.webClient.get().uri(ub -> ub.path("/pokemon/" + id).build()).retrieve()
				.onStatus(HttpStatus::is4xxClientError,
						clientResponse -> Mono.error(new AException("A Custom Client Exception: " + clientResponse.statusCode())) // MyCustomClientException
				).onStatus(HttpStatus::is5xxServerError,
						clientResponse -> Mono.error(new AException("A Custom Server Exception")) // MyCustomServerException
				).bodyToMono(PokemonDetail.class); // .block();
	}

	@Override
	public Mono<AbilityDetail> selectAbilityById(String id) {
		return this.webClient.get().uri(ub -> ub.path("/ability/" + id).build()).retrieve()
				.bodyToMono(AbilityDetail.class); // .block();
	}

}
