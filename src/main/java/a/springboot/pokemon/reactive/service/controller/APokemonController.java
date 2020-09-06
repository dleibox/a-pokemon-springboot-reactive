package a.springboot.pokemon.reactive.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import a.springboot.pokemon.reactive.service.model.AbilityDetail;
import a.springboot.pokemon.reactive.service.model.PokemonDetail;
import a.springboot.pokemon.reactive.service.model.PokemonResponse;
import a.springboot.pokemon.reactive.service.service.APokemonService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin
@RequestMapping("api/v2")
@RestController
public class APokemonController {

	private static final Logger log = LoggerFactory.getLogger(APokemonController.class);

	@Autowired
	private APokemonService service;

	@GetMapping("/pokemon")
	public ResponseEntity<Flux<PokemonResponse>> getAllPokemons() {
		log.info("[ getAllPokemons ]");
//		return this.service.getAllPokemons().map(x -> ResponseEntity.ok(x));
		return new ResponseEntity<>(this.service.getAllPokemons(), HttpStatus.OK);
	}

	@GetMapping(path = "/pokemon/{id}")
	public ResponseEntity<Mono<PokemonDetail>> getPokemonById(@PathVariable("id") String id) {
		log.info("[ getPokemonById ] {}", id);
		return ResponseEntity.ok(this.service.getPokemonById(id));
	}

	@GetMapping(path = "/ability/{id}")
	public ResponseEntity<Mono<AbilityDetail>> getAbilityById(@PathVariable("id") String id) {
		log.info("[ getAbilityById ] {}", id);
		return new ResponseEntity<>(this.service.getAbilityById(id), HttpStatus.OK);
	}

}
