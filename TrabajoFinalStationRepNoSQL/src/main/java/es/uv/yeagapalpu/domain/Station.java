package es.uv.yeagapalpu.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Document
public class Station implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id_station;
	private float nitricOxides;
    private float nitrogenDioxides;
    private float VOCs_NMHC;
    private float PM2_5;
    private LocalDateTime timestamp;
    
    
	public Station() {}

	public Station(String id_station, float nitricOxides, float nitrogenDioxides, float vOCs_NMHC, float pM2_5,
			LocalDateTime timestamp) {
		this.id_station = id_station;
		this.nitricOxides = nitricOxides;
		this.nitrogenDioxides = nitrogenDioxides;
		this.VOCs_NMHC = vOCs_NMHC;
		this.PM2_5 = pM2_5;
		this.timestamp = timestamp;
	}

	public String getId_station() {
		return id_station;
	}

	public void setId_station(String id_station) {
		this.id_station = id_station;
	}

	public float getNitricOxides() {
		return nitricOxides;
	}

	public void setNitricOxides(float nitricOxides) {
		this.nitricOxides = nitricOxides;
	}

	public float getNitrogenDioxides() {
		return nitrogenDioxides;
	}

	public void setNitrogenDioxides(float nitrogenDioxides) {
		this.nitrogenDioxides = nitrogenDioxides;
	}

	public float getVOCs_NMHC() {
		return VOCs_NMHC;
	}

	public void setVOCs_NMHC(float vOCs_NMHC) {
		VOCs_NMHC = vOCs_NMHC;
	}

	public float getPM2_5() {
		return PM2_5;
	}

	public void setPM2_5(float pM2_5) {
		PM2_5 = pM2_5;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
    
	
}