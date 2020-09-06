package a.springboot.pokemon.reactive.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import a.springboot.pokemon.reactive.service.dao.APokemonDao;
import a.springboot.pokemon.reactive.service.exception.AException;
import a.springboot.pokemon.reactive.service.model.AbilityDetail;
import a.springboot.pokemon.reactive.service.model.PokemonDetail;
import a.springboot.pokemon.reactive.service.model.PokemonResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class APokemonService {

	@Autowired
	@Qualifier("repo2")
	private APokemonDao repository;

	public Flux<PokemonResponse> getAllPokemons() {
		return this.repository.selectAllPokemons();
	}

	public Mono<PokemonDetail> getPokemonById(String id) {
		return this.repository.selectPokemonById(id).switchIfEmpty(Mono.error(new AException("NOT_FOUND")));
	}

	public Mono<AbilityDetail> getAbilityById(String id) {
		return this.repository.selectAbilityById(id).switchIfEmpty(Mono.error(new AException("NOT_FOUND")));
	}
}
