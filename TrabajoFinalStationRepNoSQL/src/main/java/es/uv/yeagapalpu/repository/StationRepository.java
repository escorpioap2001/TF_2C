package es.uv.yeagapalpu.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import es.uv.yeagapalpu.domain.Station;

public interface StationRepository extends MongoRepository<Station, String> {
	@Query("{'id_station' : ?0}")
	List<Station> findByIdStation(String id_station);
	
	@Query(value="{'id_station' : ?0}", delete = true)
    void deleteByIdStation(String id_station);
	
	@Query("{'id_station' : ?0}")
    List<Station> findByIdByOrderByTimeStamp(String id_station);
	
	@Query("{'id_station' : ?0, 'timestamp' : { $gte: ?1, $lte: ?2 }}")
	List<Station> findByIdStationAndTimestampBetween(String id_station, LocalDateTime start, LocalDateTime end);
}