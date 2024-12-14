package es.uv.yeagapalpu.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
		List<Station> s = sr.findByIdStation(id);
		if(s.size() > 0) {
			return new ResponseEntity<List<Station>>(s, HttpStatus.OK); 
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("station/{id}/last")
	@Operation(summary="Encontrar información más reciente de una estación", description="Encontrar información más reciente de una estación específica por ID")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Estación encontrada", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Station.class)) }), 
		    @ApiResponse(responseCode = "404", description = "Estación no encontrada", 
		    content = @Content) })
	public ResponseEntity<?> getLastStationById(@PathVariable String id) {
		List<Station> s = sr.findByIdByOrderByTimeStamp(id);
		if(s.size() > 0) {
			return new ResponseEntity<Station>(s.get(s.size() - 1), HttpStatus.OK); 
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("stations/{id}/between")
    @Operation(summary="Obtener estaciones por ID y rango de tiempo", description="Obtiene estaciones por ID y rango de tiempo")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Estaciones encontradas", 
            content = { @Content(mediaType = "application/json", 
              schema = @Schema(implementation = Station.class)) }),
            @ApiResponse(responseCode = "404", description = "Estaciones no encontradas", 
            content = @Content) })
    public ResponseEntity<?> getStationsByIdAndTimeRange(
            @PathVariable String id,
            @RequestParam("start") LocalDateTime start,
            @RequestParam("end") LocalDateTime end) {
        List<Station> stations = sr.findByIdStationAndTimestampBetween(id, start, end);
        if(stations.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Station>>(stations, HttpStatus.OK);
    }
	
	@PostMapping("station")
	@Operation(summary="Crear estación", description="Crea una estación")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Estación creada", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Station.class)) }),
		    @ApiResponse(responseCode = "400", description = "Estación enviada no válida", 
		    content = @Content)})
	public ResponseEntity<?> createStation(@RequestBody Station estacion) {
		estacion.setTimestamp(LocalDateTime.now());
		Station s = sr.insert(estacion);
		if(s.getId_station() != null) {
			return new ResponseEntity<Station>(s, HttpStatus.CREATED); 
		}
		return new ResponseEntity<Station>(new Station(), HttpStatus.BAD_REQUEST);
	}
	
	@DeleteMapping("stations/{id}")
	@Operation(summary="Eliminar estación", description="Elimina una estación por ID")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Estación eliminada", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Station.class)) }), 
		    @ApiResponse(responseCode = "404", description = "Estación a eliminar no encontrada", 
		    content = @Content) })
	public ResponseEntity<?> deleteStation(@PathVariable String id) {
		List<Station> s = sr.findByIdStation(id);
		if(s.isEmpty()) {
			return new ResponseEntity<String>("ID not found", HttpStatus.NOT_FOUND);
		}
		sr.deleteByIdStation(id);
		return new ResponseEntity<String>("Correctly eliminated", HttpStatus.OK);
	}
}