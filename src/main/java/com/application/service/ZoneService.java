package com.application.service;
 
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.dto.ApplicationStartEndDto;
import com.application.dto.DistributionRequestDTO;
import com.application.entity.AcademicYear;
import com.application.entity.BalanceTrack;
import com.application.entity.City;
import com.application.entity.Distribution;
import com.application.entity.Employee;
import com.application.entity.State;
import com.application.entity.StateApp;
import com.application.entity.ZonalAccountant;
import com.application.entity.Zone;
import com.application.repository.AcademicYearRepository;
import com.application.repository.AppIssuedTypeRepository;
import com.application.repository.BalanceTrackRepository;
import com.application.repository.CityRepository;
import com.application.repository.DistributionRepository;
import com.application.repository.EmployeeRepository;
import com.application.repository.StateAppRepository;
import com.application.repository.StateRepository;
import com.application.repository.ZonalAccountantRepository;
import com.application.repository.ZoneRepository;

import lombok.NonNull;
 
@Service
public class ZoneService {
 
    // --- Repositories ---
    @Autowired
    private AcademicYearRepository academicYearRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private ZoneRepository zoneRepository;
    @Autowired
    private AppIssuedTypeRepository appIssuedTypeRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private StateAppRepository stateAppRepository;
    @Autowired
    private BalanceTrackRepository balanceTrackRepository;
    @Autowired
    private DistributionRepository distributionRepository;
    @Autowired
    private ZonalAccountantRepository zonalAccountantRepository;
 
    // --- Methods for Dropdowns ---
    public List<AcademicYear> getAllAcademicYears() {
        return academicYearRepository.findAll();
    }
 
    public List<State> getAllStates() {
        return stateRepository.findAll();
    }
 
    public List<City> getCitiesByState(int stateId) {
        return cityRepository.findByDistrictStateStateId(stateId);
    }
 
    public List<Zone> getZonesByCity(int cityId) {
        return zoneRepository.findByCityCityId(cityId);
    }
 
    public List<Employee> getIssuableToEmployees() {
        return employeeRepository.findAll();
    }
 
    public Integer getAppEndNoForUser(int stateId, int userId) {
        return stateAppRepository.findAppEndNoByStateAndUser(stateId, userId);
    }
 
    public List<Employee> getEmployeesByZone(int zoneId) {
        List<ZonalAccountant> zonalAccountants = zonalAccountantRepository.findByZoneZoneId(zoneId);
        return zonalAccountants.stream()
                              .map(ZonalAccountant::getEmployee)
                              .collect(Collectors.toList());
    }
 
    // --- Method for Auto-Populating 'Application No From' ---
    public String getNextApplicationNumber(int academicYearId, int stateId, int userId) {
        Integer lastAppNumber = distributionRepository.findMaxAppEndNo(stateId, userId, academicYearId);
 
        if (lastAppNumber != null) {
            return String.valueOf(lastAppNumber + 1);
        } else {
            Optional<StateApp> stateAppOpt = stateAppRepository.findStartNumber(stateId, userId, academicYearId);
            if (stateAppOpt.isPresent()) {
                return String.valueOf(stateAppOpt.get().getApp_start_no());
            } else {
                return "ERROR_NO_RANGE_ASSIGNED";
            }
        }
    }
    
    public ApplicationStartEndDto getAppNumberRanges(int academicYearId, int stateId, int createdBy) {
        return stateAppRepository.findAppNumberRanges(academicYearId, stateId, createdBy)
                .orElseThrow(() -> new RuntimeException("Application number ranges not found for the specified criteria."));
    }
   
    @Transactional
    public void saveDistribution(@NonNull DistributionRequestDTO request) {
        // Log the request for debugging
        System.out.println("--- RECEIVED DATA IN SERVICE ---");
        System.out.println("Request: " + request.toString());
        System.out.println("CreatedBy ID: " + request.getCreatedBy());
        System.out.println("IssuedToEmpId: " + request.getIssuedToEmpId());
 
        // Validate input fields
        if (request.getCreatedBy() <= 0) {
            throw new IllegalArgumentException("Invalid issuer employee ID: " + request.getCreatedBy() + ". ID must be positive.");
        }
        if (request.getIssuedToEmpId() <= 0) {
            throw new IllegalArgumentException("Invalid receiver employee ID: " + request.getIssuedToEmpId() + ". ID must be positive.");
        }
        if (!employeeRepository.existsById(request.getCreatedBy())) {
            throw new IllegalArgumentException("Issuer employee not found for ID: " + request.getCreatedBy() + ". Please ensure the employee exists in the database.");
        }
        if (!employeeRepository.existsById(request.getIssuedToEmpId())) {
            throw new IllegalArgumentException("Receiver employee not found for ID: " + request.getIssuedToEmpId() + ". Please ensure the employee exists in the database.");
        }
 
        // Part 1: Populate and save the main distribution record
        Distribution newDistribution = new Distribution();
        newDistribution.setAcademicYear(academicYearRepository.findById(request.getAcademicYearId())
            .orElseThrow(() -> new RuntimeException("Academic year not found for ID: " + request.getAcademicYearId())));
        newDistribution.setState(stateRepository.findById(request.getStateId())
            .orElseThrow(() -> new RuntimeException("State not found for ID: " + request.getStateId())));
        newDistribution.setZone(zoneRepository.findById(request.getZoneId())
            .orElseThrow(() -> new RuntimeException("Zone not found for ID: " + request.getZoneId())));
        newDistribution.setIssuedByType(appIssuedTypeRepository.findById(request.getIssuedByTypeId())
            .orElseThrow(() -> new RuntimeException("Issued by type not found for ID: " + request.getIssuedByTypeId())));
        newDistribution.setIssuedToType(appIssuedTypeRepository.findById(request.getIssuedToTypeId())
            .orElseThrow(() -> new RuntimeException("Issued to type not found for ID: " + request.getIssuedToTypeId())));
 
        City selectedCity = cityRepository.findById(request.getCityId())
            .orElseThrow(() -> new RuntimeException("City not found for ID: " + request.getCityId()));
        newDistribution.setCity(selectedCity);
        newDistribution.setDistrict(selectedCity.getDistrict());
 
        newDistribution.setIssued_to_emp_id(request.getIssuedToEmpId());
        newDistribution.setCreated_by(request.getCreatedBy());
        newDistribution.setAppStartNo(request.getAppStartNo());
        newDistribution.setAppEndNo(request.getAppEndNo());
        newDistribution.setTotalAppCount(request.getRange());
        newDistribution.setIsActive(1);
        newDistribution.setIssueDate(request.getIssueDate() != null ? request.getIssueDate() : LocalDate.now());
 
        distributionRepository.save(newDistribution);
 
        // Part 2: Find or Create and then Update Issuer's Balance
        Optional<BalanceTrack> issuerBalanceOpt = balanceTrackRepository.findBalanceTrack(request.getAcademicYearId(), request.getCreatedBy());
        BalanceTrack issuerBalance;
 
        if (issuerBalanceOpt.isPresent()) {
            issuerBalance = issuerBalanceOpt.get();
        } else {
            StateApp stateApp = stateAppRepository.findStartNumber(request.getStateId(), request.getCreatedBy(), request.getAcademicYearId())
                .orElseThrow(() -> new RuntimeException("Issuer is not configured in StateApp for ID: " + request.getCreatedBy()));
 
            issuerBalance = new BalanceTrack();
            issuerBalance.setEmployee(employeeRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("Issuer employee not found for ID: " + request.getCreatedBy())));
            issuerBalance.setAcademicYear(stateApp.getAcademicYear());
            issuerBalance.setAppFrom(stateApp.getApp_start_no());
            issuerBalance.setAppTo(stateApp.getApp_end_no());
            issuerBalance.setAppAvblCnt(stateApp.getTotal_no_of_app());
            issuerBalance.setIsActive(1);
            issuerBalance.setCreatedBy(request.getCreatedBy());
            issuerBalance.setIssuedByType(appIssuedTypeRepository.findById(request.getIssuedByTypeId())
                .orElseThrow(() -> new RuntimeException("Issued by type not found for ID: " + request.getIssuedByTypeId())));
        }
 
        int rangeToDistribute = request.getRange();
        if (issuerBalance.getAppAvblCnt() < rangeToDistribute) {
            throw new RuntimeException("Not enough applications in balance. Available: " + issuerBalance.getAppAvblCnt() + ", Tried to distribute: " + rangeToDistribute);
        }
 
        issuerBalance.setAppAvblCnt(issuerBalance.getAppAvblCnt() - rangeToDistribute);
        issuerBalance.setAppFrom(request.getAppEndNo() + 1);
        balanceTrackRepository.save(issuerBalance);
 
        // Part 3: Create or Update the receiver's balance
        Optional<BalanceTrack> existingReceiverBalanceOpt = balanceTrackRepository.findBalanceTrack(request.getAcademicYearId(), request.getIssuedToEmpId());
        BalanceTrack receiverBalance;
        if (existingReceiverBalanceOpt.isPresent()) {
            receiverBalance = existingReceiverBalanceOpt.get();
            receiverBalance.setAppAvblCnt(receiverBalance.getAppAvblCnt() + request.getRange());
            receiverBalance.setAppTo(request.getAppEndNo());
        } else {
            receiverBalance = new BalanceTrack();
            Employee receiverEmployee = employeeRepository.findById(request.getIssuedToEmpId())
                .orElseThrow(() -> new RuntimeException("Receiver employee not found for ID: " + request.getIssuedToEmpId()));
            receiverBalance.setEmployee(receiverEmployee);
            receiverBalance.setAcademicYear(academicYearRepository.findById(request.getAcademicYearId())
                .orElseThrow(() -> new RuntimeException("Academic year not found for ID: " + request.getAcademicYearId())));
            receiverBalance.setAppFrom(request.getAppStartNo());
            receiverBalance.setAppTo(request.getAppEndNo());
            receiverBalance.setAppAvblCnt(request.getRange());
            receiverBalance.setIssuedByType(appIssuedTypeRepository.findById(request.getIssuedToTypeId())
                .orElseThrow(() -> new RuntimeException("Issued to type not found for ID: " + request.getIssuedToTypeId())));
            receiverBalance.setIsActive(1);
            receiverBalance.setCreatedBy(request.getCreatedBy());
        }
        balanceTrackRepository.save(receiverBalance);
    }
    
    @Transactional
    public void updateDistribution(int distributionId, @NonNull DistributionRequestDTO request) {
        // Step 1: Find the existing distribution record to be updated.
        Distribution existingDistribution = distributionRepository.findById(distributionId)
                .orElseThrow(() -> new RuntimeException("Distribution record not found for ID: " + distributionId));
 
        // Get old values before updating
        int oldRange = existingDistribution.getTotalAppCount();
        int oldIssuedToEmpId = existingDistribution.getIssued_to_emp_id();
        int newRange = request.getRange();
        int academicYearId = existingDistribution.getAcademicYear().getAcdcYearId();
 
        // Step 2: Handle balance updates for old recipient
        Optional<BalanceTrack> oldReceiverBalanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, oldIssuedToEmpId);
        if (oldReceiverBalanceOpt.isPresent()) {
            BalanceTrack oldReceiverBalance = oldReceiverBalanceOpt.get();
            oldReceiverBalance.setAppAvblCnt(oldReceiverBalance.getAppAvblCnt() - oldRange);
            // This is the crucial fix: the appTo for the old record must be adjusted.
            oldReceiverBalance.setAppTo(request.getAppStartNo() -1);
            balanceTrackRepository.save(oldReceiverBalance);
        } else {
             throw new RuntimeException("Old balance track not found for employee ID: " + oldIssuedToEmpId);
        }
        
        // Step 3: Handle balance updates for the new recipient
        Optional<BalanceTrack> newReceiverBalanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, request.getIssuedToEmpId());
        BalanceTrack newReceiverBalance;
        if (newReceiverBalanceOpt.isPresent()) {
            newReceiverBalance = newReceiverBalanceOpt.get();
            newReceiverBalance.setAppAvblCnt(newReceiverBalance.getAppAvblCnt() + newRange);
            newReceiverBalance.setAppTo(request.getAppEndNo());
        } else {
            newReceiverBalance = new BalanceTrack();
            newReceiverBalance.setEmployee(employeeRepository.findById(request.getIssuedToEmpId()).orElseThrow(() -> new RuntimeException("New receiver employee not found.")));
            newReceiverBalance.setAcademicYear(academicYearRepository.findById(request.getAcademicYearId()).orElseThrow(() -> new RuntimeException("Academic year not found.")));
            newReceiverBalance.setAppFrom(request.getAppStartNo());
            newReceiverBalance.setAppTo(request.getAppEndNo());
            newReceiverBalance.setAppAvblCnt(newRange);
            newReceiverBalance.setIssuedByType(appIssuedTypeRepository.findById(request.getIssuedToTypeId()).orElseThrow(() -> new RuntimeException("Issued by type not found.")));
            newReceiverBalance.setIsActive(1);
            newReceiverBalance.setCreatedBy(request.getCreatedBy());
        }
        balanceTrackRepository.save(newReceiverBalance);
 
        // Step 4: Update the main distribution record.
        existingDistribution.setAcademicYear(academicYearRepository.findById(request.getAcademicYearId())
            .orElseThrow(() -> new RuntimeException("Academic year not found for ID: " + request.getAcademicYearId())));
        existingDistribution.setState(stateRepository.findById(request.getStateId())
            .orElseThrow(() -> new RuntimeException("State not found for ID: " + request.getStateId())));
        existingDistribution.setZone(zoneRepository.findById(request.getZoneId())
            .orElseThrow(() -> new RuntimeException("Zone not found for ID: " + request.getZoneId())));
        existingDistribution.setIssuedByType(appIssuedTypeRepository.findById(request.getIssuedByTypeId())
            .orElseThrow(() -> new RuntimeException("Issued by type not found for ID: " + request.getIssuedByTypeId())));
        existingDistribution.setIssuedToType(appIssuedTypeRepository.findById(request.getIssuedToTypeId())
            .orElseThrow(() -> new RuntimeException("Issued to type not found for ID: " + request.getIssuedToTypeId())));
 
        City selectedCity = cityRepository.findById(request.getCityId())
            .orElseThrow(() -> new RuntimeException("City not found for ID: " + request.getCityId()));
        existingDistribution.setCity(selectedCity);
        existingDistribution.setDistrict(selectedCity.getDistrict());
 
        existingDistribution.setIssued_to_emp_id(request.getIssuedToEmpId());
        existingDistribution.setCreated_by(request.getCreatedBy());
        existingDistribution.setAppStartNo(request.getAppStartNo());
        existingDistribution.setAppEndNo(request.getAppEndNo());
        existingDistribution.setTotalAppCount(newRange);
        existingDistribution.setIsActive(1);
        existingDistribution.setIssueDate(request.getIssueDate() != null ? request.getIssueDate() : LocalDate.now());
 
        distributionRepository.save(existingDistribution);
    }
 
    // Helper method to adjust BalanceTrack records
    private void adjustBalanceTrack(int academicYearId, int employeeId, int changeInCount) {
        Optional<BalanceTrack> balanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, employeeId);
        if (balanceOpt.isPresent()) {
            BalanceTrack balance = balanceOpt.get();
            balance.setAppAvblCnt(balance.getAppAvblCnt() + changeInCount);
            // This logic might need to be more complex based on business rules.
            // For a simple update, we assume appTo is updated elsewhere.
            balanceTrackRepository.save(balance);
        } else {
            // This case should ideally not happen for an update operation if the old record was valid.
            throw new RuntimeException("Balance track not found for employee ID: " + employeeId);
        }
    }
}