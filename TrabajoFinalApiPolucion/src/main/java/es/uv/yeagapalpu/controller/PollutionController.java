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
import es.uv.yeagapalpu.domain.StationInfo;
import es.uv.yeagapalpu.domain.StationPolution;

@RestController
@RequestMapping("/api/estaciones")
public class PollutionController {
	
	@Autowired
    private RestTemplate template;
	
	@Value("${repoStation.sql.url}")
	private String apisql;
	
	@Value("${repoStation.nosql.url}")
	private String apinosql;
	
	final private String admin = "ADMIN";
	
	@GetMapping("estaciones")
	@Operation(summary="Obtener estaciones", description="Obtener todas las estaciones de las bases de datos")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Estaciones obtenidas", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = StationInfo.class)) }),
		    @ApiResponse(responseCode = "503", description = "Servicio no disponible", 
		    content = @Content) })
	public ResponseEntity<?> getStations() {
		ResponseEntity<StationInfo[]> response;
		List<StationInfo> stationsdto = new ArrayList<StationInfo>();
		try {
			response = template.getForEntity(apisql+"/stations", StationInfo[].class); 
		}
		catch(ResourceAccessException e){
			return new ResponseEntity<List<StationInfo>>(stationsdto, HttpStatus.SERVICE_UNAVAILABLE);
		}
		stationsdto = Arrays.stream(response.getBody()).collect(Collectors.toList());
		if(response.getStatusCode() == HttpStatus.OK) {
			return new ResponseEntity<List<StationInfo>>(stationsdto, HttpStatus.OK);
		}
		return new ResponseEntity<List<StationInfo>>(stationsdto, HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	@GetMapping("estacion/{id}/status")
	@Operation(summary="Obtener estado de estación más reciente", description="Obtener estado de estación más reciente por ID")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Estaciones obtenidas", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = StationInfo.class)) }),
		    @ApiResponse(responseCode = "404", description = "Estación no encontrada", 
		    content = @Content),
		    @ApiResponse(responseCode = "503", description = "Servicio no disponible", 
		    content = @Content)})
	public ResponseEntity<?> getStatusById(@PathVariable String id, @RequestParam(value = "from", required = false) LocalDateTime from, @RequestParam(value = "to", required = false) LocalDateTime to) {
		if(from != null && to != null) {
			ResponseEntity<StationPolution[]> response;
			List<StationPolution> stations = new ArrayList<StationPolution>();
			try {
				response = template.getForEntity(apinosql+"/stations/" + id + "/between?start=" + from + "&end=" + to, StationPolution[].class);
				for(StationPolution s : Arrays.stream(response.getBody()).collect(Collectors.toList())) {
					stations.add(s);
				}
				return new ResponseEntity<List<StationPolution>>(stations, HttpStatus.OK);
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
			ResponseEntity<StationPolution> response;
			try {
				response = template.getForEntity(apinosql+"/station/" + id + "/last", StationPolution.class);
				return new ResponseEntity<StationPolution>(response.getBody(), HttpStatus.OK);
			}
			catch (HttpClientErrorException e) {
		        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
		            return new ResponseEntity<>("No encontrado", HttpStatus.NOT_FOUND);
		        }
		        return new ResponseEntity<>("Error en la solicitud: " + e.getStatusCode(), HttpStatus.SERVICE_UNAVAILABLE);
			} 
			catch(ResourceAccessException e){
				return new ResponseEntity<StationPolution>(new StationPolution(), HttpStatus.SERVICE_UNAVAILABLE);
			}
		}
	}
	
	@PostMapping("estacion")
	@Operation(summary="Crear estación", description="Crea una estación")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Estación creada", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = StationInfo.class)) }),
		    @ApiResponse(responseCode = "400", description = "Estación enviada no válida", 
		    content = @Content),
		    @ApiResponse(responseCode = "403", description = "Usuario no válido", 
		    content = @Content),
		    @ApiResponse(responseCode = "503", description = "Servicio no disponible", 
		    content = @Content)})
	public ResponseEntity<?> createStation(@RequestBody @Valid StationInfo estacion, @RequestHeader("x-auth-user-id") String user) {
		if(!user.equals(admin)) {
			return new ResponseEntity<String>("Usuario no permitido", HttpStatus.FORBIDDEN);
		}
		
		ResponseEntity<StationInfo> response;

        try {
            response = template.postForEntity(apisql + "/station", estacion, StationInfo.class);
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(new StationInfo(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        if (response.getBody().getId() != null) {
            return new ResponseEntity<>(response.getBody(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new StationPolution(), HttpStatus.BAD_REQUEST);
        }
    }
	
	@PostMapping("estacion/{id}")
	@Operation(summary="Crear estación", description="Crea una estación")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Estación creada", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = StationPolution.class)) }),
		    @ApiResponse(responseCode = "400", description = "Estación enviada no válida", 
		    content = @Content),
		    @ApiResponse(responseCode = "403", description = "Usuario no válido", 
		    content = @Content),
		    @ApiResponse(responseCode = "404", description = "Estación enviada no encontrada", 
		    content = @Content),
		    @ApiResponse(responseCode = "503", description = "Servicio no disponible", 
		    content = @Content)})
	public ResponseEntity<?> createIDStation(@PathVariable String id, @RequestBody StationPolution estacion, @RequestHeader("x-auth-user-id") String user) {
		if(!user.equals(id)) {
			return new ResponseEntity<String>("Usuario no permitido", HttpStatus.FORBIDDEN);
		}
		
		if(id != null) {
			estacion.setId_station(id);
	        ResponseEntity<StationInfo> response1;
	        ResponseEntity<StationPolution> response2;
	        
	        try {
	        	response1 = template.getForEntity(apisql + "/stations/id/" + id, StationInfo.class);
	        	if(response1.getBody().getId() != null) {
	        		response2 = template.postForEntity(apinosql + "/station", estacion, StationPolution.class);
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
				return new ResponseEntity<>(new StationPolution(), HttpStatus.SERVICE_UNAVAILABLE);
			}
		}
		return new ResponseEntity<>(new StationPolution(), HttpStatus.BAD_REQUEST);
    }
	
	@PutMapping("estacion/{id}")
	@Operation(summary="Actualizar estación", description="Modifica la información de una estación")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Estación modificada", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = StationInfo.class)) }),
		    @ApiResponse(responseCode = "403", description = "Usuario no válido", 
		    content = @Content),
		    @ApiResponse(responseCode = "404", description = "Estación no encontrada", 
		    content = @Content),
		    @ApiResponse(responseCode = "503", description = "Servicio no disponible", 
		    content = @Content)})
	public ResponseEntity<?> updateStation(@PathVariable String id, @RequestBody @Valid StationInfo estacion, @RequestHeader("x-auth-user-id") String user) {
		if(!user.equals(admin)) {
			return new ResponseEntity<String>("Usuario no permitido", HttpStatus.FORBIDDEN);
		}
		
		estacion.setId(id); // Actualizar el ID de la estación
	    HttpEntity<StationInfo> request = new HttpEntity<StationInfo>(estacion);
	    
	    ResponseEntity<StationInfo> response;

	    try {
	        response = template.exchange(apisql + "/station/" + id, HttpMethod.PUT, request, StationInfo.class);
	        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
	    }
	    catch (HttpClientErrorException e) {
	        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
	            return new ResponseEntity<>("No encontrado", HttpStatus.NOT_FOUND);
	        }
	        return new ResponseEntity<>("Error en la solicitud: " + e.getStatusCode(), HttpStatus.SERVICE_UNAVAILABLE);
		} 
	    catch (ResourceAccessException e) {
	        return new ResponseEntity<>(new StationInfo(), HttpStatus.SERVICE_UNAVAILABLE);
	    }
	}
	
	
	
	@DeleteMapping("estacion/{id}")
	@Operation(summary="Eliminar estación", description="Eliminar estación por ID de la base de datos")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Estación eliminada correctamente", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = StationInfo.class)) }),
		    @ApiResponse(responseCode = "403", description = "Usuario no válido", 
		    content = @Content),
		    @ApiResponse(responseCode = "404", description = "Estación no encontrada", 
		    content = @Content),
		    @ApiResponse(responseCode = "503", description = "Servicio no disponible", 
		    content = @Content) })
	public ResponseEntity<?> deleteStation(@PathVariable String id, @RequestHeader("x-auth-user-id") String user) {
		if(!user.equals(admin)) {
			return new ResponseEntity<String>("Usuario no permitido", HttpStatus.FORBIDDEN);
		}
		
		try {
			template.delete(apisql+"/station/"+id);
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
