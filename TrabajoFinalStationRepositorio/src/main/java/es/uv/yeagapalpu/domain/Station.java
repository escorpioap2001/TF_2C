package es.uv.yeagapalpu.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "station")
public class Station implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", columnDefinition = "VARCHAR(255)", updatable = false, nullable = false)
    private String id;

    @Column(name = "direction")
    private String direction;

    @Column(name = "latitude")
    private float latitude;
    
    @Column(name = "longitude")
    private float longitude;
    
    
	public Station() {}

	public Station(String id, String direction, float latitude, float longitude) {
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
