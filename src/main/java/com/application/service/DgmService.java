package com.application.service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.dto.AppNumberRangeDTO;
import com.application.dto.EmployeeApplicationsDTO;
import com.application.dto.FormSubmissionDTO;
import com.application.dto.GenericDropdownDTO;
import com.application.entity.BalanceTrack;
import com.application.entity.Campus;
import com.application.entity.Distribution;
import com.application.entity.Employee;
import com.application.entity.Zone;
import com.application.repository.AcademicYearRepository;
import com.application.repository.AppIssuedTypeRepository;
import com.application.repository.BalanceTrackRepository;
import com.application.repository.CampusRepository;
import com.application.repository.CityRepository;
import com.application.repository.DistributionRepository;
import com.application.repository.EmployeeRepository;
import com.application.repository.ZoneRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
 
@Service
@RequiredArgsConstructor
public class DgmService {
    private final AcademicYearRepository academicYearRepository;
    private final CityRepository cityRepository;
    private final ZoneRepository zoneRepository;
    private final CampusRepository campusRepository;
    private final AppIssuedTypeRepository appIssuedTypeRepository;
    private final DistributionRepository distributionRepository;
    private final EmployeeRepository employeeRepository;
    private final BalanceTrackRepository balanceTrackRepository;
 
    public List<GenericDropdownDTO> getAllAcademicYears() {
        return academicYearRepository.findAll().stream()
                .map(year -> new GenericDropdownDTO(year.getAcdcYearId(), year.getAcademicYear()))
                .collect(Collectors.toList());
    }
 
    public List<GenericDropdownDTO> getAllCities() {
        return cityRepository.findAll().stream()
                .map(city -> new GenericDropdownDTO(city.getCityId(), city.getCityName()))
                .collect(Collectors.toList());
    }
    
    public List<Zone> findAllZones(){
        return zoneRepository.findAll();
    }
    
    public List<Campus> fetchAllCampuses(){
        return campusRepository.findAll();
    }
 
    public List<GenericDropdownDTO> getZonesByCityId(int cityId) {
        return zoneRepository.findByCityCityId(cityId).stream()
                .map(zone -> new GenericDropdownDTO(zone.getZoneId(), zone.getZoneName()))
                .collect(Collectors.toList());
    }
 
    public List<GenericDropdownDTO> getCampusesByZoneId(int zoneId) {
        return campusRepository.findByZoneZoneId(zoneId).stream()
                .map(campus -> new GenericDropdownDTO(campus.getCampusId(), campus.getCampusName()))
                .collect(Collectors.toList());
    }
 
    public List<GenericDropdownDTO> getAllIssuedToTypes() {
        return appIssuedTypeRepository.findAll().stream()
                .map(type -> new GenericDropdownDTO(type.getAppIssuedId(), type.getTypeName()))
                .collect(Collectors.toList());
    }
 
    public List<AppNumberRangeDTO> getAvailableAppNumberRanges(int academicYearId, int employeeId) {
        return balanceTrackRepository.findAppNumberRanges(academicYearId, employeeId).stream()
                .map(range -> new AppNumberRangeDTO(range.getAppBalanceTrkId(), range.getAppFrom(), range.getAppTo()))
                .collect(Collectors.toList());
    }
 
    public String getMobileNumberByEmpId(int empId) {
        return employeeRepository.findMobileNoByEmpId(empId);
    }
    
    private int getIssuedTypeByUserId(int userId) {
        // TODO: Implement logic to find the issuer's type ID based on their role
        // For now, hardcoding '2' for Zonal Officer as an example
        return 2;
    }
    
    @Transactional
    public void submitForm(FormSubmissionDTO formDto) {
        int issuerUserId = formDto.getUserId(); // This is the Zonal Officer
        int receiverEmpId = formDto.getDgmEmployeeId(); // This is the DGM
        int issuedById = getIssuedTypeByUserId(issuerUserId); // Get issuer's role ID
        int appNoFrom = Integer.parseInt(formDto.getApplicationNoFrom());
        int appNoTo = Integer.parseInt(formDto.getApplicationNoTo());
 
        // --- Part 1: Update the Zonal Officer's (Issuer's) Balance ---
        BalanceTrack issuerBalance = balanceTrackRepository.findById(formDto.getSelectedBalanceTrackId())
                .orElseThrow(() -> new RuntimeException("The selected application range for the issuer was not found."));
 
        // Validate the submitted range is within the issuer's selected balance track
        if (appNoFrom < issuerBalance.getAppFrom() || appNoTo > issuerBalance.getAppTo()) {
            throw new IllegalStateException("The submitted application number range is outside the issuer's available range.");
        }
        
        // This simple logic assumes ranges are used sequentially from the start.
        // A more complex system could split the BalanceTrack record into two if a middle chunk is taken.
        issuerBalance.setAppAvblCnt(issuerBalance.getAppAvblCnt() - formDto.getRange());
        issuerBalance.setAppFrom(appNoTo + 1);
        balanceTrackRepository.save(issuerBalance);
 
        // --- Part 2: Create or Update the DGM's (Receiver's) Balance ---
        Optional<BalanceTrack> existingReceiverBalanceOpt = balanceTrackRepository.findBalanceTrack(formDto.getAcademicYearId(), receiverEmpId);
        BalanceTrack receiverBalance;
        if (existingReceiverBalanceOpt.isPresent()) {
            // If the DGM's balance exists, UPDATE it
            receiverBalance = existingReceiverBalanceOpt.get();
            receiverBalance.setAppAvblCnt(receiverBalance.getAppAvblCnt() + formDto.getRange());
            receiverBalance.setAppTo(appNoTo);
        } else {
            // If the DGM's balance does NOT exist, CREATE it
            receiverBalance = new BalanceTrack();
            Employee receiverEmployee = employeeRepository.findById(receiverEmpId)
                    .orElseThrow(() -> new RuntimeException("Receiver DGM employee not found for ID: " + receiverEmpId));
            
            receiverBalance.setEmployee(receiverEmployee);
            receiverBalance.setAcademicYear(academicYearRepository.findById(formDto.getAcademicYearId()).orElse(null));
            receiverBalance.setAppFrom(appNoFrom);
            receiverBalance.setAppTo(appNoTo);
            receiverBalance.setAppAvblCnt(formDto.getRange());
            receiverBalance.setIssuedByType(appIssuedTypeRepository.findById(formDto.getIssuedToId()).orElse(null));
            receiverBalance.setIsActive(1);
            receiverBalance.setCreatedBy(issuerUserId);
        }
        balanceTrackRepository.save(receiverBalance);
 
        // --- Part 3: Save the Distribution Record ---
        Distribution distribution = new Distribution();
        
        academicYearRepository.findById(formDto.getAcademicYearId()).ifPresent(distribution::setAcademicYear);
        zoneRepository.findById(formDto.getZoneId()).ifPresent(distribution::setZone);
        campusRepository.findById(formDto.getCampusId()).ifPresent(distribution::setCampus);
        cityRepository.findById(formDto.getCityId()).ifPresent(city -> {
            distribution.setCity(city);
            if (city.getDistrict() != null) {
                distribution.setDistrict(city.getDistrict());
                if (city.getDistrict().getState() != null) {
                    distribution.setState(city.getDistrict().getState());
                }
            }
        });
        
        appIssuedTypeRepository.findById(issuedById).ifPresent(distribution::setIssuedByType);
        appIssuedTypeRepository.findById(formDto.getIssuedToId()).ifPresent(distribution::setIssuedToType);
        
        distribution.setAppStartNo(appNoFrom);
        distribution.setAppEndNo(appNoTo);
        distribution.setTotalAppCount(formDto.getRange());
        distribution.setIssueDate(LocalDate.now());
        distribution.setIsActive(1);
        distribution.setCreated_by(issuerUserId);
        distribution.setIssued_to_emp_id(receiverEmpId);
        
        distributionRepository.save(distribution);
    }
    
 // ---------------------- NEW METHOD ----------------------
    public EmployeeApplicationsDTO getEmployeeAvailableApplications(int academicYearId, int employeeId) {

        List<BalanceTrack> balances = balanceTrackRepository.findAppNumberRanges(academicYearId, employeeId);

        if (balances.isEmpty()) {
            throw new RuntimeException("No available applications found for employeeId: " + employeeId);
        }

        List<Integer> availableApplications = new ArrayList<>();

        for (BalanceTrack balance : balances) {
            int from = balance.getAppFrom();
            int to = balance.getAppTo();

            for (int i = from; i <= to; i++) {
                availableApplications.add(i);
            }
        }

        // ✅ Pass all 3 args because @AllArgsConstructor expects them
        return new EmployeeApplicationsDTO(employeeId, availableApplications, availableApplications.size());
    }


    
    //---------------------- NEW METHOD FOR UPDATING A RECORD ----------------------
    @Transactional
    public void updateForm(@NonNull Integer distributionId, @NonNull FormSubmissionDTO formDto) {
        Distribution existingDistribution = distributionRepository.findById(distributionId)
                .orElseThrow(() -> new RuntimeException("Distribution record not found with ID: " + distributionId));
 
        int oldRange = existingDistribution.getTotalAppCount();
        int oldIssuedToEmpId = existingDistribution.getIssued_to_emp_id();
        
        int newAppFrom = Integer.parseInt(formDto.getApplicationNoFrom());
        int newAppTo = Integer.parseInt(formDto.getApplicationNoTo());
        int newRange = formDto.getRange();
        
        int issuerUserId = formDto.getUserId();
        int newReceiverEmpId = formDto.getDgmEmployeeId();
        int academicYearId = formDto.getAcademicYearId();
 
        // Step 1: Handle balance updates for the old recipient.
        // This is a crucial fix to avoid data inconsistency.
        // We first find the old balance track and deduct the original range.
        Optional<BalanceTrack> oldReceiverBalanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, oldIssuedToEmpId);
        if (oldReceiverBalanceOpt.isPresent()) {
            BalanceTrack oldReceiverBalance = oldReceiverBalanceOpt.get();
            oldReceiverBalance.setAppAvblCnt(oldReceiverBalance.getAppAvblCnt() - oldRange);
            // We do not need to update appTo here, as it will be handled by the update of the new record if there is a change.
            balanceTrackRepository.save(oldReceiverBalance);
        } else {
            throw new RuntimeException("Old receiver's balance track not found for update.");
        }
 
        // Step 2: Update the new receiver's balance.
        Optional<BalanceTrack> newReceiverBalanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, newReceiverEmpId);
        BalanceTrack newReceiverBalance;
        if (newReceiverBalanceOpt.isPresent()) {
            newReceiverBalance = newReceiverBalanceOpt.get();
            newReceiverBalance.setAppAvblCnt(newReceiverBalance.getAppAvblCnt() + newRange);
            newReceiverBalance.setAppTo(newAppTo);
        } else {
            newReceiverBalance = new BalanceTrack();
            newReceiverBalance.setEmployee(employeeRepository.findById(newReceiverEmpId).orElseThrow(() -> new RuntimeException("New receiver employee not found.")));
            newReceiverBalance.setAcademicYear(academicYearRepository.findById(academicYearId).orElseThrow(() -> new RuntimeException("Academic year not found.")));
            newReceiverBalance.setAppFrom(newAppFrom);
            newReceiverBalance.setAppTo(newAppTo);
            newReceiverBalance.setAppAvblCnt(newRange);
            newReceiverBalance.setIssuedByType(appIssuedTypeRepository.findById(formDto.getIssuedToId()).orElseThrow(() -> new RuntimeException("Issued to type not found.")));
            newReceiverBalance.setIsActive(1);
            newReceiverBalance.setCreatedBy(issuerUserId);
        }
        balanceTrackRepository.save(newReceiverBalance);
 
        // Step 3: Update the main distribution record with the new values.
        existingDistribution.setAcademicYear(academicYearRepository.findById(academicYearId).orElse(null));
        existingDistribution.setZone(zoneRepository.findById(formDto.getZoneId()).orElse(null));
        existingDistribution.setCampus(campusRepository.findById(formDto.getCampusId()).orElse(null));
        existingDistribution.setCity(cityRepository.findById(formDto.getCityId()).orElse(null));
        
        // This logic implicitly updates the state and district from the city object
        cityRepository.findById(formDto.getCityId()).ifPresent(city -> {
             existingDistribution.setCity(city);
             if (city.getDistrict() != null) {
                 existingDistribution.setDistrict(city.getDistrict());
                 if (city.getDistrict().getState() != null) {
                     existingDistribution.setState(city.getDistrict().getState());
                 }
             }
         });
        
        existingDistribution.setAppStartNo(newAppFrom);
        existingDistribution.setAppEndNo(newAppTo);
        existingDistribution.setTotalAppCount(newRange);
        existingDistribution.setIssueDate(LocalDate.now());
        existingDistribution.setCreated_by(issuerUserId);
        existingDistribution.setIssued_to_emp_id(newReceiverEmpId);
        
        distributionRepository.save(existingDistribution);
    }
}