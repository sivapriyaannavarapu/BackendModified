package com.application.service;
 
import com.application.dto.AppStatusTrackDTO;
import com.application.dto.MetricCardDTO;
import com.application.repository.AppStatusTrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
 
@Service
public class AppStatusTrackService {
 
    @Autowired
    private AppStatusTrackRepository appStatusTrackRepository;
 
    /**
     * Gets the overall dashboard cards with year-over-year percentage change.
     */
    public List<MetricCardDTO> getDashboardCards() {
        Optional<AppStatusTrackDTO> currentStatsOptional = appStatusTrackRepository.findLatestAggregatedStats();
        AppStatusTrackDTO currentStats = currentStatsOptional.orElse(new AppStatusTrackDTO());
        return transformToMetricCards(currentStats);
    }
    
    /**
     * Gets dashboard cards for a single employee with year-over-year percentage change.
     */
    public List<MetricCardDTO> getDashboardCardsByEmployee(Integer empId) {
        Optional<AppStatusTrackDTO> statsOptional = appStatusTrackRepository.findAggregatedStatsByEmployee(empId);
        AppStatusTrackDTO employeeStats = statsOptional.orElse(new AppStatusTrackDTO());
        return transformToMetricCards(employeeStats);
    }
 
    /**
     * PRIVATE HELPER: Converts stats DTO into metric cards with year-over-year percentage change.
     */
    private List<MetricCardDTO> transformToMetricCards(AppStatusTrackDTO stats) {
        List<MetricCardDTO> cards = new ArrayList<>();
 
        // Total Applications
        long totalThisYear = (stats.getTotalApplications() != null) ? stats.getTotalApplications() : 0L;
        long totalLastYear = (stats.getTotalApplicationsLastYear() != null) ? stats.getTotalApplicationsLastYear() : 0L;
        cards.add(new MetricCardDTO("Total Applications", (int) totalThisYear,
                      calculatePercentageChange(totalThisYear, totalLastYear), "total_applications"));
 
        // Sold
        long soldThisYear = (stats.getAppSold() != null) ? stats.getAppSold() : 0L;
        long soldLastYear = (stats.getAppSoldLastYear() != null) ? stats.getAppSoldLastYear() : 0L;
        cards.add(new MetricCardDTO("Sold", (int) soldThisYear,
                      calculatePercentageChange(soldThisYear, soldLastYear), "sold"));
 
        // Confirmed
        long confirmedThisYear = (stats.getAppConfirmed() != null) ? stats.getAppConfirmed() : 0L;
        long confirmedLastYear = (stats.getAppConfirmedLastYear() != null) ? stats.getAppConfirmedLastYear() : 0L;
        cards.add(new MetricCardDTO("Confirmed", (int) confirmedThisYear,
                      calculatePercentageChange(confirmedThisYear, confirmedLastYear), "confirmed"));
 
        // Available
        long availableThisYear = (stats.getAppAvailable() != null) ? stats.getAppAvailable() : 0L;
        long availableLastYear = (stats.getAppAvailableLastYear() != null) ? stats.getAppAvailableLastYear() : 0L;
        cards.add(new MetricCardDTO("Available", (int) availableThisYear,
                      calculatePercentageChange(availableThisYear, availableLastYear), "available"));
 
        // Issued
        long issuedThisYear = (stats.getAppIssued() != null) ? stats.getAppIssued() : 0L;
        long issuedLastYear = (stats.getAppIssuedLastYear() != null) ? stats.getAppIssuedLastYear() : 0L;
        cards.add(new MetricCardDTO("Issued", (int) issuedThisYear,
                      calculatePercentageChange(issuedThisYear, issuedLastYear), "issued"));
 
        // Damaged
        long damagedThisYear = (stats.getAppDamaged() != null) ? stats.getAppDamaged() : 0L;
        long damagedLastYear = (stats.getAppDamagedLastYear() != null) ? stats.getAppDamagedLastYear() : 0L;
        cards.add(new MetricCardDTO("Damaged", (int) damagedThisYear,
                      calculatePercentageChange(damagedThisYear, damagedLastYear), "damaged"));
 
        // Unavailable
        long unavailableThisYear = (stats.getAppUnavailable() != null) ? stats.getAppUnavailable() : 0L;
        long unavailableLastYear = (stats.getAppUnavailableLastYear() != null) ? stats.getAppUnavailableLastYear() : 0L;
        cards.add(new MetricCardDTO("Unavailable", (int) unavailableThisYear,
                      calculatePercentageChange(unavailableThisYear, unavailableLastYear), "unavailable"));
 
        return cards;
    }
 
    /**
     * HELPER METHOD: Calculates percentage change between a current and previous value.
     */
    private int calculatePercentageChange(long currentValue, long previousValue) {
        if (previousValue == 0) {
            // If previous was 0, any increase is considered 100% growth.
            return currentValue > 0 ? 100 : 0;
        }
        // Formula: ((current - previous) / previous) * 100
        return (int) (((double) (currentValue - previousValue) / previousValue) * 100);
    }
}
 