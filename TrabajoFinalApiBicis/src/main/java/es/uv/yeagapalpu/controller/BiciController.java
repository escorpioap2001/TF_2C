package es.uv.yeagapalpu.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import es.uv.yeagapalpu.domain.ParkingInfo;
import es.uv.yeagapalpu.domain.ParkingStatus;

@RestController
@RequestMapping("/api/aparcamientos")
public class BiciController {
	
	@Autowired
    private RestTemplate template;
	
	@Value("${repoParking.sql.url}")
	private String apisql;
	
	@Value("${repoParking.nosql.url}")
	private String apinosql;
	
	final private String admin = "ADMIN";
	
	@GetMapping("aparcamientos")
	@Operation(summary="Obtener aparcamientos", description="Obtener todas laos aparcamientos de las bases de datos")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Aparcamientos obtenidos", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = ParkingInfo.class)) }),
		    @ApiResponse(responseCode = "503", description = "Servicio no disponible", 
		    content = @Content) })
	public ResponseEntity<?> getParkings() {
		ResponseEntity<ParkingInfo[]> response;
		List<ParkingInfo> parkings = new ArrayList<ParkingInfo>();
		try {
			response = template.getForEntity(apisql+"/parkings", ParkingInfo[].class); 
		}
		catch(ResourceAccessException e){
			return new ResponseEntity<List<ParkingInfo>>(parkings, HttpStatus.SERVICE_UNAVAILABLE);
		}
		parkings = Arrays.stream(response.getBody()).collect(Collectors.toList());
		if(response.getStatusCode() == HttpStatus.OK) {
			return new ResponseEntity<List<ParkingInfo>>(parkings, HttpStatus.OK);
		}
		return new ResponseEntity<List<ParkingInfo>>(parkings, HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	@GetMapping("aparcamiento/{id}/status")
	@Operation(summary="Obtener estado de aparcamiento más reciente", description="Obtener estado de aparcamiento más reciente por ID")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Aparcamientos obtenidos", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = ParkingStatus.class)) }),
		    @ApiResponse(responseCode = "404", description = "Aparcamiento no encontrado", 
		    content = @Content),
		    @ApiResponse(responseCode = "503", description = "Servicio no disponible", 
		    content = @Content)})
	public ResponseEntity<?> getStatusById(@PathVariable String id, @RequestParam(value = "from", required = false) LocalDateTime from, @RequestParam(value = "to", required = false) LocalDateTime to) {
		if(from != null && to != null) {
			ResponseEntity<ParkingStatus[]> response;
			List<ParkingStatus> parkings = new ArrayList<ParkingStatus>();
			try {
				response = template.getForEntity(apinosql+"/parkings/" + id + "/between?start=" + from + "&end=" + to, ParkingStatus[].class);
				for(ParkingStatus p : Arrays.stream(response.getBody()).collect(Collectors.toList())) {
					parkings.add(p);
				}
				return new ResponseEntity<List<ParkingStatus>>(parkings, HttpStatus.OK);
			}
			catch (HttpClientErrorException e) {
		        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
		            return new ResponseEntity<>("No encontrado", HttpStatus.NOT_FOUND);
		        }
		        return new ResponseEntity<>("Error en la solicitud: " + e.getStatusCode(), HttpStatus.SERVICE_UNAVAILABLE);
			} 
			catch (ResourceAccessException e) {
				return new ResponseEntity<>("Servicio no disponible", HttpStatus.SERVICE_UNAVAILABLE);
			}
			
		}
		else {
			ResponseEntity<ParkingStatus> response;
			try {
				response = template.getForEntity(apinosql+"/parking/" + id + "/last", ParkingStatus.class);
				return new ResponseEntity<ParkingStatus>(response.getBody(), HttpStatus.OK);
			}
			catch (HttpClientErrorException e) {
		        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
		            return new ResponseEntity<>("No encontrado", HttpStatus.NOT_FOUND);
		        }
		        return new ResponseEntity<>("Error en la solicitud: " + e.getStatusCode(), HttpStatus.SERVICE_UNAVAILABLE);
			} 
			catch(ResourceAccessException e){
				return new ResponseEntity<ParkingStatus>(new ParkingStatus(), HttpStatus.SERVICE_UNAVAILABLE);
			}
		}
	}
	
	@PostMapping("aparcamiento")
	@Operation(summary="Crear aparcamiento", description="Crea un aparcamiento")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Aparcamiento creado", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = ParkingInfo.class)) }),
		    @ApiResponse(responseCode = "400", description = "Parking enviado no válido", 
		    content = @Content),
		    @ApiResponse(responseCode = "403", description = "Usuario no válido", 
		    content = @Content),
		    @ApiResponse(responseCode = "503", description = "Servicio no disponible", 
		    content = @Content)})
	public ResponseEntity<?> createParking(@RequestBody @Valid ParkingInfo parking, @RequestHeader("x-auth-user-id") String user) {
		if(!user.equals(admin)) {
			return new ResponseEntity<String>("Usuario no permitido", HttpStatus.FORBIDDEN);
		}
		
		ResponseEntity<ParkingInfo> response;

        try {
            response = template.postForEntity(apisql + "/parking", parking, ParkingInfo.class);
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(new ParkingInfo(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        if (response.getBody().getId() != null) {
            return new ResponseEntity<>(response.getBody(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ParkingInfo(), HttpStatus.BAD_REQUEST);
        }
    }
	
	@PostMapping("evento/{id}")
	@Operation(summary="Crear aparcamiento", description="Crea un aparcamiento por un evento")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Aparcamiento creado", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = ParkingStatus.class)) }),
		    
		    @ApiResponse(responseCode = "400", description = "Aparcamiento enviado no válido", 
		    content = @Content),
		    @ApiResponse(responseCode = "403", description = "Usuario no válido", 
		    content = @Content),
		    @ApiResponse(responseCode = "404", description = "Aparcamiento no enconrado", 
		    content = @Content),
		    @ApiResponse(responseCode = "503", description = "Servicio no disponible", 
		    content = @Content)})
	public ResponseEntity<?> createIDParking(@PathVariable String id, @RequestBody ParkingStatus parking, @RequestHeader("x-auth-user-id") String user) {
		if(!user.equals(id)) {
			return new ResponseEntity<String>("Usuario no permitido", HttpStatus.FORBIDDEN);
		}
		if(id != null) {
			parking.setId_parking(id);
	        ResponseEntity<ParkingInfo> response1;
	        ResponseEntity<ParkingStatus> response2;
	        
	        try {
	        	response1 = template.getForEntity(apisql + "/parkings/id/" + id, ParkingInfo.class);
	        	if(response1.getBody().getId() != null) {
	        		response2 = template.postForEntity(apinosql + "/parking", parking, ParkingStatus.class);
		            return new ResponseEntity<>(response2.getBody(), HttpStatus.CREATED);
	        	}
	        }
	        catch (HttpClientErrorException e) {
		        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
		            return new ResponseEntity<>("No encontrado", HttpStatus.NOT_FOUND);
		        }
		        return new ResponseEntity<>("Error en la solicitud: " + e.getStatusCode(), HttpStatus.SERVICE_UNAVAILABLE);
			}
			catch (ResourceAccessException e) {
				return new ResponseEntity<>(new ParkingStatus(), HttpStatus.SERVICE_UNAVAILABLE);
			}
		}
		return new ResponseEntity<>(new ParkingStatus(), HttpStatus.BAD_REQUEST);
    }
	
	@PutMapping("aparcamiento/{id}")
	@Operation(summary="Actualizar aparcamiento", description="Modifica la información de un aparcamiento")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Aparcamiento modificado", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = ParkingInfo.class)) }),
		    @ApiResponse(responseCode = "403", description = "Usuario no válido", 
		    content = @Content),
		    @ApiResponse(responseCode = "404", description = "Aparcamiento no encontrado", 
		    content = @Content),
		    @ApiResponse(responseCode = "503", description = "Servicio no disponible", 
		    content = @Content)})
	public ResponseEntity<?> updateParking(@PathVariable String id, @RequestBody @Valid ParkingInfo parking, @RequestHeader("x-auth-user-id") String user) {
		if(!user.equals(admin)) {
			return new ResponseEntity<String>("Usuario no permitido", HttpStatus.FORBIDDEN);
		}
		parking.setId(id); // Actualizar el ID de la estación
	    HttpEntity<ParkingInfo> request = new HttpEntity<ParkingInfo>(parking);
	    
	    ResponseEntity<ParkingInfo> response;

	    try {
	        response = template.exchange(apisql + "/parking/" + id, HttpMethod.PUT, request, ParkingInfo.class);
	        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
	    }
	    catch (HttpClientErrorException e) {
	        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
	            return new ResponseEntity<>("No encontrado", HttpStatus.NOT_FOUND);
	        }
	        return new ResponseEntity<>("Error en la solicitud: " + e.getStatusCode(), HttpStatus.SERVICE_UNAVAILABLE);
		} 
	    catch (ResourceAccessException e) {
	        return new ResponseEntity<>(new ParkingInfo(), HttpStatus.SERVICE_UNAVAILABLE);
	    }
	}
	
	
	
	@DeleteMapping("aparcamiento/{id}")
	@Operation(summary="Eliminar aparcamiento", description="Eliminar aparcamiento por ID de la base de datos")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Aparcamiento eliminado correctamente", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = ParkingInfo.class)) }),
		    @ApiResponse(responseCode = "403", description = "Usuario no válido", 
		    content = @Content),
		    @ApiResponse(responseCode = "404", description = "Aparcamiento no encontrado", 
		    content = @Content),
		    @ApiResponse(responseCode = "503", description = "Servicio no disponible", 
		    content = @Content) })
	public ResponseEntity<?> deleteParking(@PathVariable String id, @RequestHeader("x-auth-user-id") String user) {
		if(!user.equals(admin)) {
			return new ResponseEntity<String>("Usuario no permitido", HttpStatus.FORBIDDEN);
		}
		
		try {
			template.delete(apisql+"/parking/"+id);
			return new ResponseEntity<String>("Deleted", HttpStatus.OK);
		}
		catch (HttpClientErrorException e) {
	        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
	            return new ResponseEntity<>("No encontrado", HttpStatus.NOT_FOUND);
	        }
	        return new ResponseEntity<>("Error en la solicitud: " + e.getStatusCode(), HttpStatus.SERVICE_UNAVAILABLE);
		} 
		catch(ResourceAccessException e){
			return new ResponseEntity<String>("Not available", HttpStatus.SERVICE_UNAVAILABLE);
		}
	}
}
