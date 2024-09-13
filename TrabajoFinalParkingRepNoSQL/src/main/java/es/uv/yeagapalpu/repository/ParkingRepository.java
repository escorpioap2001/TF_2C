package es.uv.yeagapalpu.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import es.uv.yeagapalpu.domain.Parking;

public interface ParkingRepository extends MongoRepository<Parking, String> {
	@Query("{'id_parking' : ?0}")
	List<Parking> findByIdParking(String id_parking);
	
	@Query(value="{'id_parking' : ?0}", delete = true)
    void deleteById_parking(String id_parking);
	
	@Query("{'id_parking' : ?0}")
    List<Parking> findByIdByOrderByTimeStamp(String id_parking);
	
	@Query("{'id_parking' : ?0, 'timestamp' : { $gte: ?1, $lte: ?2 }}")
	List<Parking> findByIdParkingAndTimestampBetween(String id_parking, LocalDateTime start, LocalDateTime end);
}