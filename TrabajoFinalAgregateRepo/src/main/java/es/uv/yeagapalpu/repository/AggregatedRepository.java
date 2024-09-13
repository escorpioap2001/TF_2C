package es.uv.yeagapalpu.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import es.uv.yeagapalpu.domain.Ayuntamiento;

public interface AggregatedRepository extends MongoRepository<Ayuntamiento, String> {
	@Query("{}")
    List<Ayuntamiento> findAllByOrderByTimeStamp();
}