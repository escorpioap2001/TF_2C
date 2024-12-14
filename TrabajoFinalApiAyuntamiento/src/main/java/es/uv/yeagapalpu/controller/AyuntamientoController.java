package es.uv.yeagapalpu.controller;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
import es.uv.yeagapalpu.domain.AggregatedData;
import es.uv.yeagapalpu.domain.Ayuntamiento;
import es.uv.yeagapalpu.domain.ParkingInfo;
import es.uv.yeagapalpu.domain.ParkingStatus;
import es.uv.yeagapalpu.domain.StationInfo;
import es.uv.yeagapalpu.domain.StationPolution;
import es.uv.yeagapalpu.service.AyuntamientoService;

@RestController
@RequestMapping("/api/ayuntamiento")
public class AyuntamientoController {
	
	@Autowired
    private RestTemplate template;
	
	@Autowired
	private AyuntamientoService as = new AyuntamientoService();
	
	@Value("${bicis.api.url}")
	private String apibicis;
	
	@Value("${polucion.api.url}")
	private String apipolucion;
	
	@Value("${repo.agregados.url}")
	private String repo;
	
	final private String servicio ="SERVICIO";
	
	final private String admin = "ADMIN";
	
	@GetMapping("aggregatedData")
	@Operation(summary="Obtener el agregado", description="Obtención de los últimos datos agregados")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Agregado obtenido", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Ayuntamiento.class)) }),
		    @ApiResponse(responseCode = "503", description = "Servicio no disponible", 
		    content = @Content) })
	public ResponseEntity<?> getLastAgregado(){
		ResponseEntity<Ayuntamiento> response;
		try {
			response = template.getForEntity(repo+"/agregado/reciente", Ayuntamiento.class); 
		}
		catch(ResourceAccessException e){
			return new ResponseEntity<Ayuntamiento>(new Ayuntamiento(), HttpStatus.SERVICE_UNAVAILABLE);
		}
		if(response.getStatusCode() == HttpStatus.OK) {
			return new ResponseEntity<Ayuntamiento>(response.getBody(), HttpStatus.OK);
		}
		return new ResponseEntity<Ayuntamiento>(new Ayuntamiento(), HttpStatus.SERVICE_UNAVAILABLE);	
	}
	
	@GetMapping("aparcamientoCercano")
	@Operation(summary="Obtener el aparcamiento cercano", description="Obtener el aparcamiento más cercano dependiendo de la latitud y longitud")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Aparcamiento obtenido", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Ayuntamiento.class)) }),
		    @ApiResponse(responseCode = "503", description = "Servicio no disponible", 
		    content = @Content) })
	public ResponseEntity<?> getNearestParking(@RequestParam(value = "lat", required = true) float latitud, 
											   @RequestParam(value = "lon", required = true) float longitud){
		ResponseEntity<ParkingInfo[]> response;
		List<ParkingInfo> parkings = new ArrayList<ParkingInfo>();
		try {
			response = template.getForEntity(apibicis + "/aparcamientos", ParkingInfo[].class);
			parkings = Arrays.stream(response.getBody()).collect(Collectors.toList());
			ParkingInfo p = this.as.ObtenerParkingCercano(parkings, latitud, longitud);
			
			if(p != null) {
				return new ResponseEntity<ParkingInfo>(p, HttpStatus.OK);
			}
			else {
				return new ResponseEntity<String>("No hay aparcamientos registrados", HttpStatus.OK);
			}
		}
		catch(ResourceAccessException e){
			return new ResponseEntity<Ayuntamiento>(new Ayuntamiento(), HttpStatus.SERVICE_UNAVAILABLE);
		}	
	}
	
	@GetMapping("aggregateData")
	@Operation(summary="Crear el agregado", description="Obtencer la información de las diferentes apis del ayuntamiento y crear un agregado de todo esto")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Agregado creado", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Ayuntamiento.class)) }),
		    @ApiResponse(responseCode = "400", description = "Agregado creado no válido", 
		    content = @Content),
		    @ApiResponse(responseCode = "403", description = "Usuario no válido", 
		    content = @Content),
		    @ApiResponse(responseCode = "404", description = "Aparcamiento o estación no encontrados", 
		    content = @Content),
		    @ApiResponse(responseCode = "503", description = "Servicio no disponible", 
		    content = @Content) })
	public ResponseEntity<?> getAgregado(@RequestHeader("x-auth-user-id") String user){
		if(!user.equals(servicio)) {
			return new ResponseEntity<String>("Usuario no permitido", HttpStatus.FORBIDDEN);
		}
		
		ResponseEntity<ParkingInfo[]> response1;
		List<ParkingInfo> parkings_loc = new ArrayList<ParkingInfo>();
		ResponseEntity<StationInfo[]> response2;
		List<StationInfo> stations_loc = new ArrayList<StationInfo>();
		ResponseEntity<ParkingStatus[]> response3;
		List<ParkingStatus> parkings_status = new ArrayList<ParkingStatus>();
		ResponseEntity<StationPolution[]> response4;
		List<StationPolution> stations_status = new ArrayList<StationPolution>();
		ResponseEntity<Ayuntamiento> response5;
		
		LocalDateTime fechaHoraActual = LocalDateTime.now();
		LocalDateTime fechaInicio = fechaHoraActual.minusYears(1);
		
		Ayuntamiento a = new Ayuntamiento();
		List<AggregatedData> agregados = new ArrayList<AggregatedData>();
		a.setTimeStamp(fechaHoraActual);
		
		try {
			response1 = template.getForEntity(apibicis+"/aparcamientos", ParkingInfo[].class);
			parkings_loc = Arrays.stream(response1.getBody()).collect(Collectors.toList());
			response2 = template.getForEntity(apipolucion+"/estaciones", StationInfo[].class);
			stations_loc = Arrays.stream(response2.getBody()).collect(Collectors.toList());
			
			for(ParkingInfo p : parkings_loc) {
				response3 = template.getForEntity(apibicis+ "/aparcamiento/" + p.getId() + "/status?from=" + fechaInicio + "&to=" + fechaHoraActual, ParkingStatus[].class);
				parkings_status = Arrays.stream(response3.getBody()).collect(Collectors.toList());
				AggregatedData ag = new AggregatedData();
				ag.setId(p.getId());
				ag.setAverageBikesAvailable(as.CalcularMediaBicis(parkings_status));
				StationInfo estacionCercana = as.ObtenerEstacionCercana(stations_loc, p.getLatitude(), p.getLongitude());
				response4 = template.getForEntity(apipolucion+ "/estacion/" + estacionCercana.getId() + "/status?from=" + fechaInicio + "&to=" + fechaHoraActual, StationPolution[].class);
				stations_status = Arrays.stream(response4.getBody()).collect(Collectors.toList());
				ag.setAirQuality(as.CalcularMediaContaminantes(stations_status));
				agregados.add(ag);
			}
			
			a.setAggregatedData(agregados);
			response5 = template.postForEntity(repo + "/agregado", a, Ayuntamiento.class);
			return new ResponseEntity<Ayuntamiento>(response5.getBody(), HttpStatus.OK);
			
		}
		catch (HttpClientErrorException e) {
	        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
	            return new ResponseEntity<>("No encontrado", HttpStatus.NOT_FOUND);
	        }
	        if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
	            return new ResponseEntity<>("No creado correctamente: " + e.getStatusCode(), HttpStatus.BAD_REQUEST);
	        }
	        return new ResponseEntity<>("Error en la solicitud: " + e.getStatusCode(), HttpStatus.SERVICE_UNAVAILABLE);
		} 
		catch (ResourceAccessException e) {
			return new ResponseEntity<>("Servicio no disponible", HttpStatus.SERVICE_UNAVAILABLE);
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
	public ResponseEntity<?> createParking(@RequestBody ParkingInfo parking, @RequestHeader("x-auth-user-id") String user) {
		if(!user.equals(admin)) {
			return new ResponseEntity<String>("Usuario no permitido", HttpStatus.FORBIDDEN);
		}
		
        ResponseEntity<ParkingInfo> response;
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-auth-user-id", user);
        HttpEntity<ParkingInfo> entity = new HttpEntity<>(parking, headers);

        try {
            response = template.postForEntity(apibicis + "/aparcamiento", entity, ParkingInfo.class);
            return new ResponseEntity<>(response.getBody(), HttpStatus.CREATED);
        }
        catch (HttpClientErrorException e) {
	        if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
	            return new ResponseEntity<>("No creado correctamente: " + e.getStatusCode(), HttpStatus.BAD_REQUEST);
	        }
	        return new ResponseEntity<>("Error en la solicitud: " + e.getStatusCode(), HttpStatus.SERVICE_UNAVAILABLE);
		} 
        catch (ResourceAccessException e) {
            return new ResponseEntity<>(new ParkingInfo(), HttpStatus.SERVICE_UNAVAILABLE);
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
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-auth-user-id", user);
        HttpEntity<StationInfo> entity = new HttpEntity<>(estacion, headers);

        try {
            response = template.postForEntity(apipolucion + "/estacion", entity, StationInfo.class);
            return new ResponseEntity<>(response.getBody(), HttpStatus.CREATED);
        }
        catch (HttpClientErrorException e) {
	        if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
	            return new ResponseEntity<>("No creado correctamente: " + e.getStatusCode(), HttpStatus.BAD_REQUEST);
	        }
	        return new ResponseEntity<>("Error en la solicitud: " + e.getStatusCode(), HttpStatus.SERVICE_UNAVAILABLE);
		}
        catch (ResourceAccessException e) {
            return new ResponseEntity<>(new StationInfo(), HttpStatus.SERVICE_UNAVAILABLE);
        }
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
		HttpHeaders headers = new HttpHeaders();
        headers.set("x-auth-user-id", user);
	    HttpEntity<ParkingInfo> request = new HttpEntity<ParkingInfo>(parking, headers);
	    
	    ResponseEntity<ParkingInfo> response;

	    try {
	        response = template.exchange(apibicis + "/aparcamiento/" + id, HttpMethod.PUT, request, ParkingInfo.class);
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
		HttpHeaders headers = new HttpHeaders();
        headers.set("x-auth-user-id", user);
	    HttpEntity<StationInfo> request = new HttpEntity<StationInfo>(estacion, headers);
	    
	    ResponseEntity<StationInfo> response;

	    try {
	        response = template.exchange(apipolucion + "/estacion/" + id, HttpMethod.PUT, request, StationInfo.class);
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
		
		HttpHeaders headers = new HttpHeaders();
        headers.set("x-auth-user-id", user);
        HttpEntity<StationInfo> entity = new HttpEntity<>(headers);
		
		try {
			template.exchange(
	                apibicis + "/aparcamiento/" + id,
	                HttpMethod.DELETE,
	                entity,
	                Void.class
	            );
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
		
		HttpHeaders headers = new HttpHeaders();
        headers.set("x-auth-user-id", user);
        HttpEntity<StationInfo> entity = new HttpEntity<>(headers);
		
		try {
			template.exchange(
					apipolucion + "/estacion/" + id,
	                HttpMethod.DELETE,
	                entity,
	                Void.class
	            );
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
