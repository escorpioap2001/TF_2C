package es.uv.yeagapalpu.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Document
public class Parking implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id_parking;
	private String operation;
	private Integer bikesAvailable;
    private Integer freeParkingSpots;
    private LocalDateTime timestamp;
    
	public Parking() {}

	public Parking(String id_parking, String operation, Integer bikesAvailable, Integer freeParkingSpots,
			LocalDateTime timestamp) {
		this.id_parking = id_parking;
		this.operation = operation;
		this.bikesAvailable = bikesAvailable;
		this.freeParkingSpots = freeParkingSpots;
		this.timestamp = timestamp;
	}

	public String getId_parking() {
		return id_parking;
	}

	public void setId_parking(String id_parking) {
		this.id_parking = id_parking;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Integer getBikesAvailable() {
		return bikesAvailable;
	}

	public void setBikesAvailable(Integer bikesAvailable) {
		this.bikesAvailable = bikesAvailable;
	}

	public Integer getFreeParkingSpots() {
		return freeParkingSpots;
	}

	public void setFreeParkingSpots(Integer freeParkingSpots) {
		this.freeParkingSpots = freeParkingSpots;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
	
    
    
    
}