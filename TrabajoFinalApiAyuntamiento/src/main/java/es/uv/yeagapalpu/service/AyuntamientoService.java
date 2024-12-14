package es.uv.yeagapalpu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import es.uv.yeagapalpu.domain.AirQuality;
import es.uv.yeagapalpu.domain.ParkingInfo;
import es.uv.yeagapalpu.domain.ParkingStatus;
import es.uv.yeagapalpu.domain.StationInfo;
import es.uv.yeagapalpu.domain.StationPolution;

@Service
public class AyuntamientoService{
	
	public AyuntamientoService() {}
	
	private double calcularDistancia(float lat1, float lon1, float lat2, float lon2) {
        final int RADIO_TIERRA = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RADIO_TIERRA * c;
    }
	
	public ParkingInfo ObtenerParkingCercano(List<ParkingInfo> parkings, float latitud, float longitud) {
		if (parkings == null || parkings.isEmpty()) {
            return null;
        }

        ParkingInfo parkingCercano = null;
        double distanciaMinima = Double.MAX_VALUE;

        for (ParkingInfo parking : parkings) {
            double distancia = calcularDistancia(latitud, longitud, parking.getLatitude(), parking.getLongitude());
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                parkingCercano = parking;
            }
        }

        return parkingCercano;
	}
	
	public StationInfo ObtenerEstacionCercana(List<StationInfo> estaciones, float latitud, float longitud) {
		if (estaciones == null || estaciones.isEmpty()) {
            return null;
        }

        StationInfo estacionCercana = null;
        double distanciaMinima = Double.MAX_VALUE;

        for (StationInfo estacion : estaciones) {
            double distancia = calcularDistancia(latitud, longitud, estacion.getLatitude(), estacion.getLongitude());
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                estacionCercana = estacion;
            }
        }

        return estacionCercana;
	}
	
	public float CalcularMediaBicis(List<ParkingStatus> parkings) {
		if (parkings == null || parkings.isEmpty()) {
	        return 0.0f;
	    }

	    int totalBikes = 0;
	    int count = 0;

	    for (ParkingStatus parking : parkings) {
	        if (parking.getBikesAvailable() != null) {
	            totalBikes += parking.getBikesAvailable();
	            count++;
	        }
	    }

	    if (count == 0) {
	        return 0.0f;
	    }

	    return (float) totalBikes / count;
	}
	
	public AirQuality CalcularMediaContaminantes(List<StationPolution> estaciones) {
		if (estaciones == null || estaciones.isEmpty()) {
	        return null;
	    }

	    double totalNitricOxides = 0.0;
	    double totalNitrogenDioxides = 0.0;
	    double totalVOCs_NMHC = 0.0;
	    double totalPM2_5 = 0.0;

	    int count = 0;

	    for (StationPolution estacion : estaciones) {
	        totalNitricOxides += estacion.getNitricOxides();
	        totalNitrogenDioxides += estacion.getNitrogenDioxides();
	        totalVOCs_NMHC += estacion.getVOCs_NMHC();
	        totalPM2_5 += estacion.getPM2_5();
	        count++;
	    }

	    AirQuality media = new AirQuality();
	    media.setNitricOxides(totalNitricOxides / count);
	    media.setNitrogenDioxides(totalNitrogenDioxides / count);
	    media.setVOCs_NMHC(totalVOCs_NMHC / count);
	    media.setPM2_5(totalPM2_5 / count);

	    return media;
	}
}