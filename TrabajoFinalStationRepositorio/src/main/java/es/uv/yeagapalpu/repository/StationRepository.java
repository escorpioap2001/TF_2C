package es.uv.yeagapalpu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.uv.yeagapalpu.domain.Station;

@Repository
public interface StationRepository extends JpaRepository<Station, String> {
	
}