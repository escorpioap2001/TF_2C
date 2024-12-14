package es.uv.yeagapalpu.domain;

import java.io.Serializable;

public class AggregatedData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id_parking;

    private double average_bikes_available;

    private AirQuality airQuality;
    
    
    public AggregatedData() {}

    public AggregatedData(String id_parking, double average_bikes_available, AirQuality airQuality) {
		this.id_parking = id_parking;
		this.average_bikes_available = average_bikes_available;
		this.airQuality = airQuality;
	}

	public String getId_parking() {
        return id_parking;
    }

    public void setId(String id_parking) {
        this.id_parking = id_parking;
    }

    public double getAverageBikesAvailable() {
        return average_bikes_available;
    }

    public void setAverageBikesAvailable(double average_bikes_available) {
        this.average_bikes_available = average_bikes_available;
    }

    public AirQuality getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(AirQuality airQuality) {
        this.airQuality = airQuality;
    }

	@Override
	public String toString() {
		return "AggregatedData [id_parking=" + id_parking + ", average_bikes_available=" + average_bikes_available
				+ ", airQuality=" + airQuality.toString() + "]";
	}
    
    
}