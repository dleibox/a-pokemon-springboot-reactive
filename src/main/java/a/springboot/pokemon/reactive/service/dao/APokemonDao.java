package a.springboot.pokemon.reactive.service.dao;

import a.springboot.pokemon.reactive.service.model.AbilityDetail;
import a.springboot.pokemon.reactive.service.model.PokemonDetail;
import a.springboot.pokemon.reactive.service.model.PokemonResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface APokemonDao {
	Flux<PokemonResponse> selectAllPokemons();

	Mono<PokemonDetail> selectPokemonById(String id);

	Mono<AbilityDetail> selectAbilityById(String id);
}
