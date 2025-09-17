package com.application.dto;
 
import lombok.Data;
 
@Data
public class ZonePerformanceDTO {
    // Renamed 'entityId' to 'zoneName' and changed the type to String
    private String zoneName;
    private Double performancePercentage;
}