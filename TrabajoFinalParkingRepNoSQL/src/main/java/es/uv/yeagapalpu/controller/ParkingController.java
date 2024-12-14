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
import es.uv.yeagapalpu.repository.ParkingRepository;
import es.uv.yeagapalpu.domain.Parking;

@RestController
@RequestMapping("/api/v1")
public class ParkingController {

	@Autowired
	private ParkingRepository pr;
	
	@GetMapping("parkings")
	@Operation(summary="Obtener parkings", description="Obtener todos los parkings de la base de datos")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Parkings obtenidos", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Parking.class)) })})
	public ResponseEntity<?> getParkings() {
		
		List<Parking> parkings;
		parkings = (List<Parking>) pr.findAll();
		
		return new ResponseEntity<List<Parking>>(parkings, HttpStatus.OK);
	}
	
	@GetMapping("parkings/id/{id}")
	@Operation(summary="Encontrar parking", description="Encuentra un parking por ID")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Parking encontrado por ID", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Parking.class)) }), 
		    @ApiResponse(responseCode = "404", description = "Parking no encontrado", 
		    content = @Content) })
	public ResponseEntity<?> getParkingById(@PathVariable String id) {
		List<Parking> p = pr.findByIdParking(id);
		if(p.size() > 0) {
			return new ResponseEntity<List<Parking>>(p, HttpStatus.OK); 
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("parking/{id}/last")
	@Operation(summary="Encontrar información más reciente de un aparcamiento", description="Encontrar información más reciente de un aparcamiento específico por ID")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Aparcamiento encontrado", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Parking.class)) }), 
		    @ApiResponse(responseCode = "404", description = "Aparcamiento no encontrado", 
		    content = @Content) })
	public ResponseEntity<?> getLastParkingById(@PathVariable String id) {
		List<Parking> p = pr.findByIdByOrderByTimeStamp(id);
		if(p.size() > 0) {
			return new ResponseEntity<Parking>(p.get(p.size() - 1), HttpStatus.OK); 
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("parkings/{id}/between")
    @Operation(summary="Obtener aparcamientos por ID y rango de tiempo", description="Obtiene aparcamientos por ID y rango de tiempo")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Aparcamientos encontrados", 
            content = { @Content(mediaType = "application/json", 
              schema = @Schema(implementation = Parking.class)) }),
            @ApiResponse(responseCode = "404", description = "Aparcamientos no encontrados", 
            content = @Content) })
    public ResponseEntity<?> getStationsByIdAndTimeRange(
            @PathVariable String id,
            @RequestParam("start") LocalDateTime start,
            @RequestParam("end") LocalDateTime end) {
        List<Parking> parkings = pr.findByIdParkingAndTimestampBetween(id, start, end);
        if(parkings.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Parking>>(parkings, HttpStatus.OK);
    }
	
	@PostMapping("parking")
	@Operation(summary="Crear parking", description="Crea un parking")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Parking creado", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Parking.class)) }),
		    @ApiResponse(responseCode = "400", description = "Parking enviado no válido", 
		    content = @Content)})
	public ResponseEntity<?> createParking(@RequestBody Parking parking) {
		parking.setTimestamp(LocalDateTime.now());
		Parking p = pr.insert(parking);
		if(p.getId_parking() != null) {
			return new ResponseEntity<Parking>(p, HttpStatus.CREATED); 
		}
		return new ResponseEntity<Parking>(new Parking(), HttpStatus.BAD_REQUEST);
	}
	
	@DeleteMapping("parking/{id}")
	@Operation(summary="Eliminar parking", description="Elimina un parking por ID")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Parking eliminado", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Parking.class)) }), 
		    @ApiResponse(responseCode = "404", description = "Parking a eliminar no encontrado", 
		    content = @Content) })
	public ResponseEntity<?> deleteParking(@PathVariable String id) {
		List<Parking> p = pr.findByIdParking(id);
		if(p.isEmpty()) {
			return new ResponseEntity<String>("ID not found", HttpStatus.NOT_FOUND);
		}
		pr.deleteById_parking(id);
		return new ResponseEntity<String>("Correctly eliminated", HttpStatus.OK);
	}
}