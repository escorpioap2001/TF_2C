package es.uv.yeagapalpu.domain;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;

public class StationInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@NotNull(message = "El ID no puede ser nulo")
    private String id;

	@NotNull(message = "La direccion no puede ser nula")
    private String direction;

	@NotNull(message = "La latitud no puede estar vacía")
    private float latitude;
    
	@NotNull(message = "La longitud no puede estar vacía")
    private float longitude;
    
    
	public StationInfo() {}

	public StationInfo(String id, String direction, float latitude, float longitude, String token) {
		super();
		this.id = id;
		this.direction = direction;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "Station [id=" + id + ", direction=" + direction + ", latitude="
				+ latitude + ", longitude=" + longitude + "]";
	}

	
    
}
