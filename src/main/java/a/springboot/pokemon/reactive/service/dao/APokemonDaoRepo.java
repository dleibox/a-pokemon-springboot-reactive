package a.springboot.pokemon.reactive.service.dao;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import a.springboot.pokemon.reactive.service.model.AbilityDetail;
import a.springboot.pokemon.reactive.service.model.PokemonDetail;
import a.springboot.pokemon.reactive.service.model.PokemonResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository("repo2")
public class APokemonDaoRepo implements APokemonDao { // extends ReactiveCrudRepository<PokemonResponse, ID>

	private static final Logger log = LoggerFactory.getLogger(APokemonDaoRepo.class);

	@Autowired
	@Qualifier("source2")
	private APokemonDao ds;

	@Autowired
	private DatabaseClient databaseClient;

	@Override
	public Flux<PokemonResponse> selectAllPokemons() {
		final String sql = "SELECT uuid,json FROM pokemon_response";
		Flux<PokemonResponse> p = databaseClient.execute(sql).map((r, md) -> {
			log.info("[ DB Response ] {}", r.get("json").toString().length());

			try {
				return new ObjectMapper().readValue(r.get("json").toString(), PokemonResponse.class);
			} catch (JsonMappingException e) {
//				e.printStackTrace();
				log.warn("[ WARN ] {}", e.toString());
			} catch (JsonProcessingException e) {
//				e.printStackTrace();
				log.warn("[ WARN ] {}", e.toString());
			}

			return (PokemonResponse) null;
		}).all();

		log.info("[ Repo Response ] {}", p);

		return p.switchIfEmpty(this.ds.selectAllPokemons().map(x -> {
			try {
				String json = new ObjectMapper().writeValueAsString(x);
				log.info("[ Source Response ] {}", json.length());
				databaseClient.execute("INSERT INTO pokemon_response (uuid,json) VALUES (:uuid,:json)")
						.bind("uuid", UUID.randomUUID()).bind("json", json).fetch().rowsUpdated().subscribe();
			} catch (DataAccessException e) {
//				e.printStackTrace();
				log.warn("[ WARN ] {}", e.toString());
			} catch (JsonProcessingException e) {
//				e.printStackTrace();
				log.warn("[ WARN ] {}", e.toString());
			}

			return x;
		}));
	}

	@Override
	public Mono<PokemonDetail> selectPokemonById(String key) {
		int iid;
		try {
			iid = Integer.parseInt(key);
		} catch (NumberFormatException e) {
			log.warn("[ WARN ] {}", e.toString());
			iid = 0;
		}
		final String sql = "SELECT uuid,id,name,json FROM pokemon WHERE id = :id OR name = :name";
//			o = databaseClient.execute(sql).bind("id", iid).bind("name", key).as(PokemonDetail.class).fetch().first();
		Mono<PokemonDetail> o = databaseClient.execute(sql).bind("id", iid).bind("name", key).fetch().first().map(x -> {
			log.info("[ DB Response ] {}", x.get("json").toString().length());
			try {
				return new ObjectMapper().readValue(x.get("json").toString(), PokemonDetail.class);
			} catch (JsonMappingException e) {
//					e.printStackTrace();
				log.warn("[ WARN ] {}", e.toString());
			} catch (JsonProcessingException e) {
//					e.printStackTrace();
				log.warn("[ WARN ] {}", e.toString());
			}
			return null;
		});

		log.info("[ Repo Response ] {}", o);

		return o.switchIfEmpty(this.ds.selectPokemonById(key).map(x -> {
			try {
				String json = new ObjectMapper().writeValueAsString(x);
				log.info("[ Source Response ] {}", json.length());
				databaseClient.execute("INSERT INTO pokemon (uuid,id,name,json) VALUES (:uuid,:id,:name,:json)")
						.bind("uuid", UUID.randomUUID()).bind("id", x.getId()).bind("name", x.getName())
						.bind("json", json).fetch().rowsUpdated().subscribe();
			} catch (DataAccessException e) {
//				e.printStackTrace();
				log.warn("[ WARN ] {}", e.toString());
			} catch (JsonProcessingException e) {
//				e.printStackTrace();
				log.warn("[ WARN ] {}", e.toString());
			}
			return x;
		}));
	}

	@Override
	public Mono<AbilityDetail> selectAbilityById(String key) {
		int iid;
		try {
			iid = Integer.parseInt(key);
		} catch (NumberFormatException e) {
			log.warn("[ WARN ] {}", e.toString());
			iid = 0;
		}
		final String sql = "SELECT uuid,id,name,json FROM ability WHERE id = :id OR name = :name";
//		o = databaseClient.execute(sql).bind("id", iid).bind("name", key).as(AbilityDetail.class).fetch().first();
		Mono<AbilityDetail> o = databaseClient.execute(sql).bind("id", iid).bind("name", key).fetch().first().map(x -> {
			log.info("[ DB Response ] {}", x.get("json").toString().length());
			try {
				return new ObjectMapper().readValue(x.get("json").toString(), AbilityDetail.class);
			} catch (JsonMappingException e) {
//				e.printStackTrace();
				log.warn("[ WARN ] {}", e.toString());
			} catch (JsonProcessingException e) {
//				e.printStackTrace();
				log.warn("[ WARN ] {}", e.toString());
			}
			return null;
		});

		log.info("[ Repo Response ] {}", o);

		return o.switchIfEmpty(this.ds.selectAbilityById(key).map(x -> {
			try {
				String json = new ObjectMapper().writeValueAsString(x);
				log.info("[ Source Response ] {}", json.length());
				databaseClient.execute("INSERT INTO ability (uuid,id,name,json) VALUES (:uuid,:id,:name,:json)")
						.bind("uuid", UUID.randomUUID()).bind("id", x.getId()).bind("name", x.getName())
						.bind("json", json).fetch().rowsUpdated().subscribe();
			} catch (DataAccessException e) {
//			e.printStackTrace();
				log.warn("[ WARN ] {}", e.toString());
			} catch (JsonProcessingException e) {
//			e.printStackTrace();
				log.warn("[ WARN ] {}", e.toString());
			}
			return x;
		}));
	}

}
