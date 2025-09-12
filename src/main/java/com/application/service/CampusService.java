package com.application.service;
 
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.dto.AppNumberRangeDTO;
import com.application.dto.DgmToCampusFormDTO;
import com.application.dto.GenericDropdownDTO;
import com.application.entity.BalanceTrack;
import com.application.entity.Campaign;
import com.application.entity.Campus;
import com.application.entity.CampusProView;
import com.application.entity.Distribution;
import com.application.entity.District;
import com.application.entity.Employee;
import com.application.repository.AcademicYearRepository;
import com.application.repository.AppIssuedTypeRepository;
import com.application.repository.BalanceTrackRepository;
import com.application.repository.CampaignRepository;
import com.application.repository.CampusProViewRepository;
import com.application.repository.CampusRepository;
import com.application.repository.CityRepository;
import com.application.repository.DistributionRepository;
import com.application.repository.DistrictRepository;
import com.application.repository.EmployeeRepository;
import com.application.repository.StateRepository;

import lombok.NonNull;
 
@Service
public class CampusService {
 
    @Autowired private AcademicYearRepository academicYearRepository;
    @Autowired private StateRepository stateRepository;
    @Autowired private DistrictRepository districtRepository;
    @Autowired private CityRepository cityRepository;
    @Autowired private CampusRepository campusRepository;
    @Autowired private CampaignRepository campaignRepository;
    @Autowired private AppIssuedTypeRepository appIssuedTypeRepository;
    @Autowired private DistributionRepository distributionRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private BalanceTrackRepository balanceTrackRepository;
    @Autowired private CampusProViewRepository campusProViewRepository;
 
    // --- Dropdown Logic ---
    public List<GenericDropdownDTO> getAllAcademicYears() {
        return academicYearRepository.findAll().stream()
                .map(y -> new GenericDropdownDTO(y.getAcdcYearId(), y.getAcademicYear())).collect(Collectors.toList());
    }
    public List<GenericDropdownDTO> getAllStates() {
        return stateRepository.findAll().stream()
                .map(s -> new GenericDropdownDTO(s.getStateId(), s.getStateName())).collect(Collectors.toList());
    }
    
    public List<District> getAllDistricts()
    {
    	return districtRepository.findAll();
    }
    
    public List<GenericDropdownDTO> getDistrictsByStateId(int stateId) {
        return districtRepository.findByStateStateId(stateId).stream()
                .map(d -> new GenericDropdownDTO(d.getDistrictId(), d.getDistrictName())).collect(Collectors.toList());
    }
    public List<GenericDropdownDTO> getCitiesByDistrictId(int districtId) {
        return cityRepository.findByDistrictDistrictId(districtId).stream()
                .map(c -> new GenericDropdownDTO(c.getCityId(), c.getCityName())).collect(Collectors.toList());
    }
    public List<GenericDropdownDTO> getCampusesByCityId(int cityId) {
        return campusRepository.findByCityCityId(cityId).stream()
                .map(c -> new GenericDropdownDTO(c.getCampusId(), c.getCampusName())).collect(Collectors.toList());
    }
    public List<GenericDropdownDTO> getAllCampaignAreas() {
        return campaignRepository.findAll().stream()
                .map(c -> new GenericDropdownDTO(c.getCampaignId(), c.getAreaName())).collect(Collectors.toList());
    }
    public List<GenericDropdownDTO> getProsByCampusId(int campusId) {
        List<Integer> employeeIds = campusProViewRepository.findByCampusId(campusId).stream()
                                                  .map(CampusProView::getEmp_id)
                                                  .collect(Collectors.toList());
        if (employeeIds.isEmpty()) {
            return List.of();
        }
        return employeeRepository.findAllById(employeeIds).stream()
                .map(employee -> new GenericDropdownDTO(employee.getEmp_id(), employee.getFirst_name() + " " + employee.getLast_name()))
                .collect(Collectors.toList());
    }
    public List<GenericDropdownDTO> getAllIssuedToTypes() {
        return appIssuedTypeRepository.findAll().stream()
                .map(t -> new GenericDropdownDTO(t.getAppIssuedId(), t.getTypeName())).collect(Collectors.toList());
    }
    public List<AppNumberRangeDTO> getAvailableAppNumberRanges(int employeeId, int academicYearId) {
        return balanceTrackRepository.findAppNumberRanges(academicYearId, employeeId).stream()
                .map(r -> new AppNumberRangeDTO(r.getAppBalanceTrkId(), r.getAppFrom(), r.getAppTo())).collect(Collectors.toList());
    }
    public String getMobileNumberByEmpId(int empId) {
        return employeeRepository.findMobileNoByEmpId(empId);
    }
    
    private int getDgmUserTypeId() {
        
        return 3;
    }
    
    public List<GenericDropdownDTO> getCampusByCampaignId(int campaignId) {
        List<Campus> campuses = campusRepository.findCampusByCampaignId(campaignId);
        return campuses.stream().map(c -> new GenericDropdownDTO(c.getCampusId(), c.getCampusName())).collect(Collectors.toList());
    }
    
    public List<GenericDropdownDTO> getCampaignsByCityId(int cityId) {
        List<Campaign> campaigns = campaignRepository.findByCity_CityId(cityId);
        return campaigns.stream()
            .map(c -> new GenericDropdownDTO(c.getCampaignId(), c.getAreaName()))
            .collect(Collectors.toList());
    }

    @Transactional
    public void submitDgmToCampusForm(DgmToCampusFormDTO formDto) {
        int dgmUserId = formDto.getUserId();
        int proEmployeeId = formDto.getProEmployeeId();
        int dgmUserTypeId = getDgmUserTypeId();
        int appNoFrom = Integer.parseInt(formDto.getApplicationNoFrom());
        int appNoTo = Integer.parseInt(formDto.getApplicationNoTo());
 
        BalanceTrack dgmBalance = balanceTrackRepository.findById(formDto.getSelectedBalanceTrackId())
                .orElseThrow(() -> new RuntimeException("The selected application range for the DGM was not found."));
       
        if (appNoFrom < dgmBalance.getAppFrom() || appNoTo > dgmBalance.getAppTo()) {
            throw new IllegalStateException("The submitted application number range is outside the DGM's available range.");
        }
       
        dgmBalance.setAppAvblCnt(dgmBalance.getAppAvblCnt() - formDto.getRange());
        dgmBalance.setAppFrom(appNoTo + 1);
        balanceTrackRepository.save(dgmBalance);
 
        Optional<BalanceTrack> proBalanceOpt = balanceTrackRepository.findBalanceTrack(formDto.getAcademicYearId(), proEmployeeId);
        BalanceTrack proBalance;
        if (proBalanceOpt.isPresent()) {
            proBalance = proBalanceOpt.get();
            proBalance.setAppAvblCnt(proBalance.getAppAvblCnt() + formDto.getRange());
            proBalance.setAppTo(appNoTo);
        } else {
            proBalance = new BalanceTrack();
            Employee proEmployee = employeeRepository.findById(proEmployeeId)
                    .orElseThrow(() -> new RuntimeException("Receiver PRO employee not found for ID: " + proEmployeeId));
            proBalance.setEmployee(proEmployee);
            proBalance.setAcademicYear(academicYearRepository.findById(formDto.getAcademicYearId()).orElse(null));
            proBalance.setAppFrom(appNoFrom);
            proBalance.setAppTo(appNoTo);
            proBalance.setAppAvblCnt(formDto.getRange());
            proBalance.setIssuedByType(appIssuedTypeRepository.findById(formDto.getIssuedToId()).orElse(null));
            proBalance.setIsActive(1);
            proBalance.setCreatedBy(dgmUserId);
        }
        balanceTrackRepository.save(proBalance);
 
        Distribution distribution = new Distribution();
       
        academicYearRepository.findById(formDto.getAcademicYearId()).ifPresent(distribution::setAcademicYear);
        districtRepository.findById(formDto.getDistrictId()).ifPresent(distribution::setDistrict);
        cityRepository.findById(formDto.getCityId()).ifPresent(distribution::setCity);
        campusRepository.findById(formDto.getCampusId()).ifPresent(distribution::setCampus);
        appIssuedTypeRepository.findById(dgmUserTypeId).ifPresent(distribution::setIssuedByType);
        appIssuedTypeRepository.findById(formDto.getIssuedToId()).ifPresent(distribution::setIssuedToType);
       
        Campus selectedCampus = campusRepository.findById(formDto.getCampusId()).orElse(null);
        distribution.setCampus(selectedCampus);
 
        if (selectedCampus != null) {
            distribution.setZone(selectedCampus.getZone());
        }
 
        if (distribution.getDistrict() != null) {
            distribution.setState(distribution.getDistrict().getState());
        }
       
        distribution.setAppStartNo(appNoFrom);
        distribution.setAppEndNo(appNoTo);
        distribution.setTotalAppCount(formDto.getRange());
        distribution.setIssueDate(LocalDate.now());
        distribution.setIsActive(1);
        distribution.setCreated_by(dgmUserId);
        distribution.setIssued_to_emp_id(proEmployeeId);
       
        distributionRepository.save(distribution);
    }
 
    @Transactional
    public void updateDgmToCampusForm(@NonNull Integer distributionId, @NonNull DgmToCampusFormDTO formDto) {
        // Step 1: Find the existing distribution record and its original values.
        Distribution existingDistribution = distributionRepository.findById(distributionId)
                .orElseThrow(() -> new RuntimeException("Original distribution record not found for ID: " + distributionId));
 
        int oldRange = existingDistribution.getTotalAppCount();
        int oldIssuedToEmpId = existingDistribution.getIssued_to_emp_id();
        int newAppFrom = Integer.parseInt(formDto.getApplicationNoFrom());
        int newAppTo = Integer.parseInt(formDto.getApplicationNoTo());
        int newRange = formDto.getRange();
        int dgmUserId = formDto.getUserId();
        int newProEmployeeId = formDto.getProEmployeeId();
        int academicYearId = formDto.getAcademicYearId();
 
        // Step 2: Adjust the DGM's (issuer) balance by the net difference.
        // This is a more direct and reliable approach than a full revert and re-apply.
        Optional<BalanceTrack> dgmBalanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, dgmUserId);
        if (dgmBalanceOpt.isPresent()) {
            BalanceTrack dgmBalance = dgmBalanceOpt.get();
            int rangeDifference = newRange - oldRange;
            dgmBalance.setAppAvblCnt(dgmBalance.getAppAvblCnt() - rangeDifference);
            // Assuming sequential range usage, update the appFrom for the issuer.
            dgmBalance.setAppFrom(newAppTo + 1);
            balanceTrackRepository.save(dgmBalance);
        } else {
            throw new RuntimeException("DGM's balance track not found.");
        }
 
        // Step 3: Adjust the balances of the old and new PROs (receivers).
        if (oldIssuedToEmpId != newProEmployeeId) {
            // A new PRO is receiving the applications, so adjust both balances.
            // Revert the old PRO's balance.
            Optional<BalanceTrack> oldProBalanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, oldIssuedToEmpId);
            if (oldProBalanceOpt.isPresent()) {
                BalanceTrack oldProBalance = oldProBalanceOpt.get();
                oldProBalance.setAppAvblCnt(oldProBalance.getAppAvblCnt() - oldRange);
                // This is the crucial fix: correctly adjust the appTo for the old record.
                oldProBalance.setAppTo(newAppFrom - 1);
                balanceTrackRepository.save(oldProBalance);
            } else {
                throw new RuntimeException("Original PRO's balance track not found.");
            }
 
            // Add the new range to the new PRO's balance.
            Optional<BalanceTrack> newProBalanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, newProEmployeeId);
            BalanceTrack newProBalance;
            if (newProBalanceOpt.isPresent()) {
                newProBalance = newProBalanceOpt.get();
                newProBalance.setAppAvblCnt(newProBalance.getAppAvblCnt() + newRange);
                newProBalance.setAppTo(newAppTo);
            } else {
                newProBalance = new BalanceTrack();
                newProBalance.setEmployee(employeeRepository.findById(newProEmployeeId).orElseThrow(() -> new RuntimeException("New PRO employee not found.")));
                newProBalance.setAcademicYear(academicYearRepository.findById(academicYearId).orElseThrow(() -> new RuntimeException("Academic year not found.")));
                newProBalance.setAppFrom(newAppFrom);
                newProBalance.setAppTo(newAppTo);
                newProBalance.setAppAvblCnt(newRange);
                newProBalance.setIssuedByType(appIssuedTypeRepository.findById(formDto.getIssuedToId()).orElseThrow(() -> new RuntimeException("Issued to type not found.")));
                newProBalance.setIsActive(1);
                newProBalance.setCreatedBy(dgmUserId);
            }
            balanceTrackRepository.save(newProBalance);
        } else {
            // The PRO is the same, so just adjust their balance by the net change in range.
            Optional<BalanceTrack> proBalanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, newProEmployeeId);
            if (proBalanceOpt.isPresent()) {
                BalanceTrack proBalance = proBalanceOpt.get();
                int rangeDifference = newRange - oldRange;
                proBalance.setAppAvblCnt(proBalance.getAppAvblCnt() + rangeDifference);
                proBalance.setAppTo(newAppTo);
                balanceTrackRepository.save(proBalance);
            } else {
                throw new RuntimeException("PRO's balance track not found.");
            }
        }
 
        // Step 4: Update the Distribution Record itself.
        existingDistribution.setAcademicYear(academicYearRepository.findById(academicYearId).orElse(null));
        existingDistribution.setDistrict(districtRepository.findById(formDto.getDistrictId()).orElse(null));
        existingDistribution.setCity(cityRepository.findById(formDto.getCityId()).orElse(null));
        existingDistribution.setCampus(campusRepository.findById(formDto.getCampusId()).orElse(null));
 
        if (existingDistribution.getDistrict() != null) {
            existingDistribution.setState(existingDistribution.getDistrict().getState());
        }
 
        existingDistribution.setAppStartNo(newAppFrom);
        existingDistribution.setAppEndNo(newAppTo);
        existingDistribution.setTotalAppCount(newRange);
        existingDistribution.setIssueDate(LocalDate.now());
        existingDistribution.setIssued_to_emp_id(newProEmployeeId);
        existingDistribution.setCreated_by(dgmUserId);
 
        distributionRepository.save(existingDistribution);
    }}