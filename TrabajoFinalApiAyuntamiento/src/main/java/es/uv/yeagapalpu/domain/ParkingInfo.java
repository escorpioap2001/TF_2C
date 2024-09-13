package es.uv.yeagapalpu.domain;

import java.io.Serializable;

public class ParkingInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String id;
    
    private String direction;
	
	private int capacidad;

    private float latitude;
    
    private float longitude;
    
    
	public ParkingInfo() {}

	

	public ParkingInfo(String id, String direction, int capacidad, float latitude, float longitude) {
		this.id = id;
		this.direction = direction;
		this.capacidad = capacidad;
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
	
	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
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
		return "ParkingInfo [id=" + id + ", direction=" + direction + ", capacidad=" + capacidad + ", latitude="
				+ latitude + ", longitude=" + longitude + "]";
	}
	
    
}
