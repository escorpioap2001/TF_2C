package es.uv.yeagapalpu.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import es.uv.yeagapalpu.repository.StationRepository;
import es.uv.yeagapalpu.domain.Station;

@RestController
@RequestMapping("/api/v1")
public class StationController {

	@Autowired
	private StationRepository sr;
	
	@GetMapping("stations")
	@Operation(summary="Obtener estaciones", description="Obtener todas las estaciones de la base de datos")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Estaciones obtenidas", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Station.class)) })})
	public ResponseEntity<?> getStations() {
		
		List<Station> stations;
		stations = (List<Station>) sr.findAll();
		
		return new ResponseEntity<List<Station>>(stations, HttpStatus.OK);
	}
	
	@GetMapping("stations/id/{id}")
	@Operation(summary="Encontrar estación", description="Encuentra una estación por ID")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Estación encontrada por ID", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Station.class)) }), 
		    @ApiResponse(responseCode = "404", description = "Estación no encontrada", 
		    content = @Content) })
	public ResponseEntity<?> getStationById(@PathVariable String id) {
		Optional<Station> s = sr.findById(id);
		if(s.isPresent()) {
			return new ResponseEntity<Station>(s.get(), HttpStatus.OK); 
		}
		return new ResponseEntity<Station>(new Station(), HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("station")
	@Operation(summary="Crear estación", description="Crea una estación")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Estación creada", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Station.class)) }),
		    @ApiResponse(responseCode = "400", description = "Estación enviada no válida", 
		    content = @Content)})
	public ResponseEntity<?> createStation(@RequestBody Station station) {
		Station s = sr.save(station);
		if(s.getId() != null) {
			return new ResponseEntity<Station>(s, HttpStatus.CREATED); 
		}
		return new ResponseEntity<Station>(new Station(), HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("station/{id}")
	@Operation(summary="Actualizar estación", description="Edita una estación")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Estación editada", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Station.class)) }), 
		    @ApiResponse(responseCode = "404", description = "Estación a editar no encontrada", 
		    content = @Content) })
    public ResponseEntity<?> updateStation(@PathVariable String id, @RequestBody Station updatedStation) {
        Optional<Station> existingStation = sr.findById(id);
        if (!existingStation.isPresent()) {
            return new ResponseEntity<String>("ID not found", HttpStatus.NOT_FOUND);
        }
        
        Station station = existingStation.get();
        
        station.setDirection(updatedStation.getDirection());
        station.setLatitude(updatedStation.getLatitude());
        station.setLongitude(updatedStation.getLongitude());
        
        Station updated = sr.save(station);
        
        return new ResponseEntity<Station>(updated, HttpStatus.OK);
    }
	
	@DeleteMapping("station/{id}")
	@Operation(summary="Eliminar estación", description="Elimina una estación por ID")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Estación eliminada", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Station.class)) }), 
		    @ApiResponse(responseCode = "404", description = "Estación a eliminar no encontrada", 
		    content = @Content) })
	public ResponseEntity<?> deleteStation(@PathVariable String id) {
		Optional<Station> s = sr.findById(id);
		if(!s.isPresent()) {
			return new ResponseEntity<String>("ID not found", HttpStatus.NOT_FOUND);
		}
		sr.delete(s.get());
		return new ResponseEntity<String>("Correctly eliminated", HttpStatus.OK);
	}
}