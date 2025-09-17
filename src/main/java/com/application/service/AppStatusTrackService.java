package com.application.service;
 
import com.application.dto.AppStatusTrackDTO;
import com.application.dto.MetricCardDTO;
import com.application.repository.AppStatusTrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
 
@Service
public class AppStatusTrackService {
 
	@Autowired
	
	private AppStatusTrackRepository appStatusTrackRepository;
 
	/**
	 * Creates a list of dashboard metric cards with Year-over-Year (YoY) percentage change.
	 * The current year is determined dynamically.
	 * @return A list of MetricCardDTO objects for the dashboard.
	 */
	public List<MetricCardDTO> getDashboardCards() {
        
		// 1. Get the current and previous years dynamically
		int currentYear = LocalDate.now().getYear();
		int previousYear = currentYear - 1;
 
		// 2. Fetch aggregated data for both years
		AppStatusTrackDTO currentYearStats = appStatusTrackRepository
			.findAggregatedStatsByYear(currentYear)
			.orElse(new AppStatusTrackDTO());
 
		AppStatusTrackDTO previousYearStats = appStatusTrackRepository
			.findAggregatedStatsByYear(previousYear)
			.orElse(new AppStatusTrackDTO());
 
		// 3. Create metric cards with the calculated YoY percentage change
		List<MetricCardDTO> cards = new ArrayList<>();
 
		cards.add(new MetricCardDTO("Total Applications",
				Math.toIntExact(currentYearStats.getTotalApplications() != null ? currentYearStats.getTotalApplications() : 0L),
				calculatePercentageChange(currentYearStats.getTotalApplications(), previousYearStats.getTotalApplications()),
				"total_applications"));
 
		cards.add(new MetricCardDTO("Sold",
				Math.toIntExact(currentYearStats.getAppSold() != null ? currentYearStats.getAppSold() : 0L),
				calculatePercentageChange(currentYearStats.getAppSold(), previousYearStats.getAppSold()),
				"sold"));
 
		cards.add(new MetricCardDTO("Confirmed",
				Math.toIntExact(currentYearStats.getAppConfirmed() != null ? currentYearStats.getAppConfirmed() : 0L),
				calculatePercentageChange(currentYearStats.getAppConfirmed(), previousYearStats.getAppConfirmed()),
				"confirmed"));
 
		cards.add(new MetricCardDTO("Available",
				Math.toIntExact(currentYearStats.getAppAvailable() != null ? currentYearStats.getAppAvailable() : 0L),
				calculatePercentageChange(currentYearStats.getAppAvailable(), previousYearStats.getAppAvailable()),
				"available"));
 
		cards.add(new MetricCardDTO("Issued",
				Math.toIntExact(currentYearStats.getAppIssued() != null ? currentYearStats.getAppIssued() : 0L),
				calculatePercentageChange(currentYearStats.getAppIssued(), previousYearStats.getAppIssued()),
				"issued"));
 
		cards.add(new MetricCardDTO("Damaged",
				Math.toIntExact(currentYearStats.getAppDamaged() != null ? currentYearStats.getAppDamaged() : 0L),
				calculatePercentageChange(currentYearStats.getAppDamaged(), previousYearStats.getAppDamaged()),
				"damaged"));
 
		cards.add(new MetricCardDTO("Unavailable",
				Math.toIntExact(currentYearStats.getAppUnavailable() != null ? currentYearStats.getAppUnavailable() : 0L),
				calculatePercentageChange(currentYearStats.getAppUnavailable(), previousYearStats.getAppUnavailable()),
				"unavailable"));
 
		return cards;
	}
 
	/**
	 * Calculates the percentage change from a previous value to a current value.
	 * Handles nulls and division-by-zero scenarios.
	 * @param current The current year's value.
	 * @param previous The previous year's value.
	 * @return The rounded integer percentage change.
	 */
	private int calculatePercentageChange(Long current, Long previous) {
		long currentVal = (current != null) ? current : 0L;
		long previousVal = (previous != null) ? previous : 0L;
 
		if (previousVal == 0) {
			// If previous value was 0, any increase is shown as 100% growth
			return (currentVal > 0) ? 100 : 0;
		}
 
		double percentageChange = ((double) (currentVal - previousVal) / previousVal) * 100.0;
		return (int) Math.round(percentageChange);
	}
 
	/**
	 * Fetches aggregated application status stats filtered by issued type and employee.
	 * @param issuedTypeId The ID of the issued type to filter by.
	 * @param empId The ID of the employee to filter by.
	 * @return An AppStatusTrackDTO with the aggregated statistics.
	 */
	public AppStatusTrackDTO getAppStatusByIssuedTypeAndEmployee(Integer issuedTypeId, Integer empId) {
		Optional<AppStatusTrackDTO> statsOptional = appStatusTrackRepository
				.findAggregatedStatsByIssuedTypeAndEmployee(issuedTypeId, empId);
		return statsOptional.orElse(new AppStatusTrackDTO());
	}
}