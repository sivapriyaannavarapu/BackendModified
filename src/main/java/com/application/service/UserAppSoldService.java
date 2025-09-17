package com.application.service;
 
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.application.dto.UserAppSoldDTO;
import com.application.dto.ZonePerformanceDTO;
import com.application.entity.UserAppSold;
import com.application.repository.UserAppSoldRepository;
 
@Service
public class UserAppSoldService {
 
    @Autowired
    private UserAppSoldRepository userAppSoldRepository;
 
    // Define constants for entity types to make the code more readable
    private static final int ENTITY_TYPE_ZONE = 2;
    private static final int ENTITY_TYPE_DGM = 3;
    private static final int ENTITY_TYPE_CAMPUS = 4;
 
    // This private helper method handles the conversion logic for any performance data
    private List<ZonePerformanceDTO> getPerformanceData(List<Object[]> rawData) {
        return rawData.stream()
                .limit(4) // Limit to the top/bottom 4
                .map(result -> {
                    ZonePerformanceDTO dto = new ZonePerformanceDTO();
                    
                    // result[0] is now the zone_name (String) from the query
                    dto.setZoneName((String) result[0]);
                    
                    // result[1] is still the performance percentage
                    dto.setPerformancePercentage(((BigDecimal) result[1]).doubleValue());
                    
                    return dto;
                })
                .collect(Collectors.toList());
    }
 
    // ... The rest of your service methods (getTopRatedZones, etc.) remain unchanged ...
    
    // --- Zones ---
    public List<ZonePerformanceDTO> getTopRatedZones() {
        List<Object[]> rawData = userAppSoldRepository.findTopPerformersByEntityType(ENTITY_TYPE_ZONE);
        return getPerformanceData(rawData);
    }
 
    public List<ZonePerformanceDTO> getDropRatedZones() {
        List<Object[]> rawData = userAppSoldRepository.findWorstPerformersByEntityType(ENTITY_TYPE_ZONE);
        return getPerformanceData(rawData);
    }
    // --- DGMs ---
    public List<ZonePerformanceDTO> getTopRatedDgms() {
        List<Object[]> rawData = userAppSoldRepository.findTopPerformersByEntityType(ENTITY_TYPE_DGM);
        return getPerformanceData(rawData);
    }
 
    public List<ZonePerformanceDTO> getDropRatedDgms() {
        List<Object[]> rawData = userAppSoldRepository.findWorstPerformersByEntityType(ENTITY_TYPE_DGM);
        return getPerformanceData(rawData);
    }
 
    // --- Campuses ---
    public List<ZonePerformanceDTO> getTopRatedCampus() {
        List<Object[]> rawData = userAppSoldRepository.findTopPerformersByEntityType(ENTITY_TYPE_CAMPUS);
        return getPerformanceData(rawData);
    }
    
    public List<ZonePerformanceDTO> getDropRatedCampus() {
        List<Object[]> rawData = userAppSoldRepository.findWorstPerformersByEntityType(ENTITY_TYPE_CAMPUS);
        return getPerformanceData(rawData);
    }
 
    // --- Original methods remain unchanged ---
    public List<UserAppSoldDTO> getAnalyticsByEntityId(Integer entityId) {
        List<UserAppSold> analyticsData = userAppSoldRepository.findByEntityId(entityId);
        return analyticsData.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
 
    private UserAppSoldDTO convertToDto(UserAppSold userAppSold) {
        UserAppSoldDTO dto = new UserAppSoldDTO();
        dto.setEmpId(userAppSold.getEmpId());
        dto.setEntityId(userAppSold.getEntityId());
        dto.setAcdcYearId(userAppSold.getAcdcYearId());
        dto.setTotalAppCount(userAppSold.getTotalAppCount());
        dto.setSold(userAppSold.getSold());
        return dto;
    }
}
 