package es.uv.yeagapalpu.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Ayuntamiento implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDateTime timeStamp;

    private List<AggregatedData> aggregatedData;
    
    
    public Ayuntamiento() {}

    public Ayuntamiento(LocalDateTime timeStamp, List<AggregatedData> aggregatedData) {
		this.timeStamp = timeStamp;
		this.aggregatedData = aggregatedData;
	}

	public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<AggregatedData> getAggregatedData() {
        return aggregatedData;
    }

    public void setAggregatedData(List<AggregatedData> aggregatedData) {
        this.aggregatedData = aggregatedData;
    }

	@Override
	public String toString() {
		return "Ayuntamiento [timeStamp=" + timeStamp + ", aggregatedData=" + aggregatedData.toString() + "]";
	}
}