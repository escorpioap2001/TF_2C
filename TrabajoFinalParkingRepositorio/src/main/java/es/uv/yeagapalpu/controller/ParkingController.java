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
		Optional<Parking> p = pr.findById(id);
		if(p.isPresent()) {
			return new ResponseEntity<Parking>(p.get(), HttpStatus.OK); 
		}
		return new ResponseEntity<Parking>(new Parking(), HttpStatus.NOT_FOUND);
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
		Parking p = pr.save(parking);
		if(p.getId() != null) {
			return new ResponseEntity<Parking>(p, HttpStatus.CREATED); 
		}
		return new ResponseEntity<Parking>(new Parking(), HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("parking/{id}")
	@Operation(summary="Actualizar parking", description="Edita un parking")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Parking editado", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Parking.class)) }), 
		    @ApiResponse(responseCode = "404", description = "Parking a editar no encontrado", 
		    content = @Content) })
    public ResponseEntity<?> updateParking(@PathVariable String id, @RequestBody Parking updatedParking) {
        Optional<Parking> existingParking = pr.findById(id);
        if (!existingParking.isPresent()) {
            return new ResponseEntity<String>("ID not found", HttpStatus.NOT_FOUND);
        }
        
        Parking parking = existingParking.get();
        
        parking.setDirection(updatedParking.getDirection());
        parking.setCapacidad(updatedParking.getCapacidad());
        parking.setLatitude(updatedParking.getLatitude());
        parking.setLongitude(updatedParking.getLongitude());
        
        Parking updated = pr.save(parking);
        
        return new ResponseEntity<Parking>(updated, HttpStatus.OK);
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
		Optional<Parking> p = pr.findById(id);
		if(!p.isPresent()) {
			return new ResponseEntity<String>("ID not found", HttpStatus.NOT_FOUND);
		}
		pr.delete(p.get());
		return new ResponseEntity<String>("Correctly eliminated", HttpStatus.OK);
	}
}