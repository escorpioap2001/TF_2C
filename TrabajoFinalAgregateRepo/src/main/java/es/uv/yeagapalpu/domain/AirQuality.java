package es.uv.yeagapalpu.domain;

import java.io.Serializable;

public class AirQuality implements Serializable {
	
    private static final long serialVersionUID = 1L;

    private double nitricOxides;

    private double nitrogenDioxides;

    private double VOCs_NMHC;

    private double PM2_5;
    
    

    public AirQuality() {}
    
    

	public AirQuality(double nitricOxides, double nitrogenDioxides, double vOCs_NMHC, double pM2_5) {
		this.nitricOxides = nitricOxides;
		this.nitrogenDioxides = nitrogenDioxides;
		this.VOCs_NMHC = vOCs_NMHC;
		this.PM2_5 = pM2_5;
	}



	// Getters y Setters
    public double getNitricOxides() {
        return nitricOxides;
    }

    public void setNitricOxides(double nitricOxides) {
        this.nitricOxides = nitricOxides;
    }

    public double getNitrogenDioxides() {
        return nitrogenDioxides;
    }

    public void setNitrogenDioxides(double nitrogenDioxides) {
        this.nitrogenDioxides = nitrogenDioxides;
    }

    public double getVOCs_NMHC() {
        return VOCs_NMHC;
    }

    public void setVOCs_NMHC(double VOCs_NMHC) {
        this.VOCs_NMHC = VOCs_NMHC;
    }

    public double getPM2_5() {
        return PM2_5;
    }

    public void setPM2_5(double PM2_5) {
        this.PM2_5 = PM2_5;
    }



	@Override
	public String toString() {
		return "AirQuality [nitricOxides=" + nitricOxides + ", nitrogenDioxides=" + nitrogenDioxides + ", VOCs_NMHC="
				+ VOCs_NMHC + ", PM2_5=" + PM2_5 + "]";
	}	
    
}