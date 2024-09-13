package es.uv.yeagapalpu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.uv.yeagapalpu.domain.Parking;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, String> {
	
}