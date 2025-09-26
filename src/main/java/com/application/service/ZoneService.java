//package com.application.service;
// 
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.application.dto.ApplicationStartEndDto;
//import com.application.dto.DistributionRequestDTO;
//import com.application.entity.AcademicYear;
//import com.application.entity.BalanceTrack;
//import com.application.entity.City;
//import com.application.entity.Distribution;
//import com.application.entity.Employee;
//import com.application.entity.State;
//import com.application.entity.StateApp;
//import com.application.entity.ZonalAccountant;
//import com.application.entity.Zone;
//import com.application.repository.AcademicYearRepository;
//import com.application.repository.AppIssuedTypeRepository;
//import com.application.repository.BalanceTrackRepository;
//import com.application.repository.CityRepository;
//import com.application.repository.DistributionRepository;
//import com.application.repository.EmployeeRepository;
//import com.application.repository.StateAppRepository;
//import com.application.repository.StateRepository;
//import com.application.repository.ZonalAccountantRepository;
//import com.application.repository.ZoneRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import lombok.NonNull;
// 
//@Service
//public class ZoneService {
// 
//    // --- Repositories ---
//    @Autowired
//    private AcademicYearRepository academicYearRepository;
//    @Autowired
//    private StateRepository stateRepository;
//    @Autowired
//    private CityRepository cityRepository;
//    @Autowired
//    private ZoneRepository zoneRepository;
//    @Autowired
//    private AppIssuedTypeRepository appIssuedTypeRepository;
//    @Autowired
//    private EmployeeRepository employeeRepository;
//    @Autowired
//    private StateAppRepository stateAppRepository;
//    @Autowired
//    private BalanceTrackRepository balanceTrackRepository;
//    @Autowired
//    private DistributionRepository distributionRepository;
//    @Autowired
//    private ZonalAccountantRepository zonalAccountantRepository;
// 
//    // --- Methods for Dropdowns ---
//    public List<AcademicYear> getAllAcademicYears() {
//        return academicYearRepository.findAll();
//    }
// 
//    public List<State> getAllStates() {
//        return stateRepository.findAll();
//    }
// 
//    public List<City> getCitiesByState(int stateId) {
//        return cityRepository.findByDistrictStateStateId(stateId);
//    }
// 
//    public List<Zone> getZonesByCity(int cityId) {
//        return zoneRepository.findByCityCityId(cityId);
//    }
// 
//    public List<Employee> getIssuableToEmployees() {
//        return employeeRepository.findAll();
//    }
// 
//    public Integer getAppEndNoForUser(int stateId, int userId) {
//        return stateAppRepository.findAppEndNoByStateAndUser(stateId, userId);
//    }
// 
//    public List<Employee> getEmployeesByZone(int zoneId) {
//        List<ZonalAccountant> zonalAccountants = zonalAccountantRepository.findByZoneZoneId(zoneId);
//        return zonalAccountants.stream()
//                              .map(ZonalAccountant::getEmployee)
//                              .collect(Collectors.toList());
//    }
// 
//    // --- Method for Auto-Populating 'Application No From' ---
//    public String getNextApplicationNumber(int academicYearId, int stateId, int userId) {
//        Integer lastAppNumber = distributionRepository.findMaxAppEndNo(stateId, userId, academicYearId);
// 
//        if (lastAppNumber != null) {
//            return String.valueOf(lastAppNumber + 1);
//        } else {
//            Optional<StateApp> stateAppOpt = stateAppRepository.findStartNumber(stateId, userId, academicYearId);
//            if (stateAppOpt.isPresent()) {
//                return String.valueOf(stateAppOpt.get().getApp_start_no());
//            } else {
//                return "ERROR_NO_RANGE_ASSIGNED";
//            }
//        }
//    }
//    
//    public ApplicationStartEndDto getAppNumberRanges(int academicYearId, int stateId, int createdBy) {
//        return stateAppRepository.findAppNumberRanges(academicYearId, stateId, createdBy)
//                .orElseThrow(() -> new RuntimeException("Application number ranges not found for the specified criteria."));
//    }
//   
//    @Transactional
//    public void saveDistribution(@NonNull DistributionRequestDTO request) {
//        // Log the request for debugging
//        System.out.println("--- RECEIVED DATA IN SERVICE ---");
//        System.out.println("Request: " + request.toString());
//        System.out.println("CreatedBy ID: " + request.getCreatedBy());
//        System.out.println("IssuedToEmpId: " + request.getIssuedToEmpId());
// 
//        // Validate input fields
//        if (request.getCreatedBy() <= 0) {
//            throw new IllegalArgumentException("Invalid issuer employee ID: " + request.getCreatedBy() + ". ID must be positive.");
//        }
//        if (request.getIssuedToEmpId() <= 0) {
//            throw new IllegalArgumentException("Invalid receiver employee ID: " + request.getIssuedToEmpId() + ". ID must be positive.");
//        }
//        if (!employeeRepository.existsById(request.getCreatedBy())) {
//            throw new IllegalArgumentException("Issuer employee not found for ID: " + request.getCreatedBy() + ". Please ensure the employee exists in the database.");
//        }
//        if (!employeeRepository.existsById(request.getIssuedToEmpId())) {
//            throw new IllegalArgumentException("Receiver employee not found for ID: " + request.getIssuedToEmpId() + ". Please ensure the employee exists in the database.");
//        }
// 
//        // Part 1: Populate and save the main distribution record
//        Distribution newDistribution = new Distribution();
//        newDistribution.setAcademicYear(academicYearRepository.findById(request.getAcademicYearId())
//            .orElseThrow(() -> new RuntimeException("Academic year not found for ID: " + request.getAcademicYearId())));
//        newDistribution.setState(stateRepository.findById(request.getStateId())
//            .orElseThrow(() -> new RuntimeException("State not found for ID: " + request.getStateId())));
//        newDistribution.setZone(zoneRepository.findById(request.getZoneId())
//            .orElseThrow(() -> new RuntimeException("Zone not found for ID: " + request.getZoneId())));
//        newDistribution.setIssuedByType(appIssuedTypeRepository.findById(request.getIssuedByTypeId())
//            .orElseThrow(() -> new RuntimeException("Issued by type not found for ID: " + request.getIssuedByTypeId())));
//        newDistribution.setIssuedToType(appIssuedTypeRepository.findById(request.getIssuedToTypeId())
//            .orElseThrow(() -> new RuntimeException("Issued to type not found for ID: " + request.getIssuedToTypeId())));
// 
//        City selectedCity = cityRepository.findById(request.getCityId())
//            .orElseThrow(() -> new RuntimeException("City not found for ID: " + request.getCityId()));
//        newDistribution.setCity(selectedCity);
//        newDistribution.setDistrict(selectedCity.getDistrict());
// 
//        newDistribution.setIssued_to_emp_id(request.getIssuedToEmpId());
//        newDistribution.setCreated_by(request.getCreatedBy());
//        newDistribution.setAppStartNo(request.getAppStartNo());
//        newDistribution.setAppEndNo(request.getAppEndNo());
//        newDistribution.setTotalAppCount(request.getRange());
//        newDistribution.setIsActive(1);
//        newDistribution.setIssueDate(request.getIssueDate() != null ? request.getIssueDate() : LocalDate.now());
// 
//        distributionRepository.save(newDistribution);
// 
//        // Part 2: Find or Create and then Update Issuer's Balance
//        Optional<BalanceTrack> issuerBalanceOpt = balanceTrackRepository.findBalanceTrack(request.getAcademicYearId(), request.getCreatedBy());
//        BalanceTrack issuerBalance;
// 
//        if (issuerBalanceOpt.isPresent()) {
//            issuerBalance = issuerBalanceOpt.get();
//        } else {
//            StateApp stateApp = stateAppRepository.findStartNumber(request.getStateId(), request.getCreatedBy(), request.getAcademicYearId())
//                .orElseThrow(() -> new RuntimeException("Issuer is not configured in StateApp for ID: " + request.getCreatedBy()));
// 
//            issuerBalance = new BalanceTrack();
//            issuerBalance.setEmployee(employeeRepository.findById(request.getCreatedBy())
//                .orElseThrow(() -> new RuntimeException("Issuer employee not found for ID: " + request.getCreatedBy())));
//            issuerBalance.setAcademicYear(stateApp.getAcademicYear());
//            issuerBalance.setAppFrom(stateApp.getApp_start_no());
//            issuerBalance.setAppTo(stateApp.getApp_end_no());
//            issuerBalance.setAppAvblCnt(stateApp.getTotal_no_of_app());
//            issuerBalance.setIsActive(1);
//            issuerBalance.setCreatedBy(request.getCreatedBy());
//            issuerBalance.setIssuedByType(appIssuedTypeRepository.findById(request.getIssuedByTypeId())
//                .orElseThrow(() -> new RuntimeException("Issued by type not found for ID: " + request.getIssuedByTypeId())));
//        }
// 
//        int rangeToDistribute = request.getRange();
//        if (issuerBalance.getAppAvblCnt() < rangeToDistribute) {
//            throw new RuntimeException("Not enough applications in balance. Available: " + issuerBalance.getAppAvblCnt() + ", Tried to distribute: " + rangeToDistribute);
//        }
// 
//        issuerBalance.setAppAvblCnt(issuerBalance.getAppAvblCnt() - rangeToDistribute);
//        issuerBalance.setAppFrom(request.getAppEndNo() + 1);
//        balanceTrackRepository.save(issuerBalance);
// 
//        // Part 3: Create or Update the receiver's balance
//        Optional<BalanceTrack> existingReceiverBalanceOpt = balanceTrackRepository.findBalanceTrack(request.getAcademicYearId(), request.getIssuedToEmpId());
//        BalanceTrack receiverBalance;
//        if (existingReceiverBalanceOpt.isPresent()) {
//            receiverBalance = existingReceiverBalanceOpt.get();
//            receiverBalance.setAppAvblCnt(receiverBalance.getAppAvblCnt() + request.getRange());
//            receiverBalance.setAppTo(request.getAppEndNo());
//        } else {
//            receiverBalance = new BalanceTrack();
//            Employee receiverEmployee = employeeRepository.findById(request.getIssuedToEmpId())
//                .orElseThrow(() -> new RuntimeException("Receiver employee not found for ID: " + request.getIssuedToEmpId()));
//            receiverBalance.setEmployee(receiverEmployee);
//            receiverBalance.setAcademicYear(academicYearRepository.findById(request.getAcademicYearId())
//                .orElseThrow(() -> new RuntimeException("Academic year not found for ID: " + request.getAcademicYearId())));
//            receiverBalance.setAppFrom(request.getAppStartNo());
//            receiverBalance.setAppTo(request.getAppEndNo());
//            receiverBalance.setAppAvblCnt(request.getRange());
//            receiverBalance.setIssuedByType(appIssuedTypeRepository.findById(request.getIssuedToTypeId())
//                .orElseThrow(() -> new RuntimeException("Issued to type not found for ID: " + request.getIssuedToTypeId())));
//            receiverBalance.setIsActive(1);
//            receiverBalance.setCreatedBy(request.getCreatedBy());
//        }
//        balanceTrackRepository.save(receiverBalance);
//    }
//    
////    @Transactional
////    public void updateDistribution(int distributionId, @NonNull DistributionRequestDTO request) {
////        // Step 1: Find the existing distribution record to be updated.
////        Distribution existingDistribution = distributionRepository.findById(distributionId)
////                .orElseThrow(() -> new RuntimeException("Distribution record not found for ID: " + distributionId));
//// 
////        // Get old values before updating
////        int oldRange = existingDistribution.getTotalAppCount();
////        int oldIssuedToEmpId = existingDistribution.getIssued_to_emp_id();
////        int newRange = request.getRange();
////        int academicYearId = existingDistribution.getAcademicYear().getAcdcYearId();
//// 
////        // Step 2: Handle balance updates for old recipient
////        Optional<BalanceTrack> oldReceiverBalanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, oldIssuedToEmpId);
////        if (oldReceiverBalanceOpt.isPresent()) {
////            BalanceTrack oldReceiverBalance = oldReceiverBalanceOpt.get();
////            oldReceiverBalance.setAppAvblCnt(oldReceiverBalance.getAppAvblCnt() - oldRange);
////            // This is the crucial fix: the appTo for the old record must be adjusted.
////            oldReceiverBalance.setAppTo(request.getAppStartNo() -1);
////            balanceTrackRepository.save(oldReceiverBalance);
////        } else {
////             throw new RuntimeException("Old balance track not found for employee ID: " + oldIssuedToEmpId);
////        }
////        
////        // Step 3: Handle balance updates for the new recipient
////        Optional<BalanceTrack> newReceiverBalanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, request.getIssuedToEmpId());
////        BalanceTrack newReceiverBalance;
////        if (newReceiverBalanceOpt.isPresent()) {
////            newReceiverBalance = newReceiverBalanceOpt.get();
////            newReceiverBalance.setAppAvblCnt(newReceiverBalance.getAppAvblCnt() + newRange);
////            newReceiverBalance.setAppTo(request.getAppEndNo());
////        } else {
////            newReceiverBalance = new BalanceTrack();
////            newReceiverBalance.setEmployee(employeeRepository.findById(request.getIssuedToEmpId()).orElseThrow(() -> new RuntimeException("New receiver employee not found.")));
////            newReceiverBalance.setAcademicYear(academicYearRepository.findById(request.getAcademicYearId()).orElseThrow(() -> new RuntimeException("Academic year not found.")));
////            newReceiverBalance.setAppFrom(request.getAppStartNo());
////            newReceiverBalance.setAppTo(request.getAppEndNo());
////            newReceiverBalance.setAppAvblCnt(newRange);
////            newReceiverBalance.setIssuedByType(appIssuedTypeRepository.findById(request.getIssuedToTypeId()).orElseThrow(() -> new RuntimeException("Issued by type not found.")));
////            newReceiverBalance.setIsActive(1);
////            newReceiverBalance.setCreatedBy(request.getCreatedBy());
////        }
////        balanceTrackRepository.save(newReceiverBalance);
//// 
////        // Step 4: Update the main distribution record.
////        existingDistribution.setAcademicYear(academicYearRepository.findById(request.getAcademicYearId())
////            .orElseThrow(() -> new RuntimeException("Academic year not found for ID: " + request.getAcademicYearId())));
////        existingDistribution.setState(stateRepository.findById(request.getStateId())
////            .orElseThrow(() -> new RuntimeException("State not found for ID: " + request.getStateId())));
////        existingDistribution.setZone(zoneRepository.findById(request.getZoneId())
////            .orElseThrow(() -> new RuntimeException("Zone not found for ID: " + request.getZoneId())));
////        existingDistribution.setIssuedByType(appIssuedTypeRepository.findById(request.getIssuedByTypeId())
////            .orElseThrow(() -> new RuntimeException("Issued by type not found for ID: " + request.getIssuedByTypeId())));
////        existingDistribution.setIssuedToType(appIssuedTypeRepository.findById(request.getIssuedToTypeId())
////            .orElseThrow(() -> new RuntimeException("Issued to type not found for ID: " + request.getIssuedToTypeId())));
//// 
////        City selectedCity = cityRepository.findById(request.getCityId())
////            .orElseThrow(() -> new RuntimeException("City not found for ID: " + request.getCityId()));
////        existingDistribution.setCity(selectedCity);
////        existingDistribution.setDistrict(selectedCity.getDistrict());
//// 
////        existingDistribution.setIssued_to_emp_id(request.getIssuedToEmpId());
////        existingDistribution.setCreated_by(request.getCreatedBy());
////        existingDistribution.setAppStartNo(request.getAppStartNo());
////        existingDistribution.setAppEndNo(request.getAppEndNo());
////        existingDistribution.setTotalAppCount(newRange);
////        existingDistribution.setIsActive(1);
////        existingDistribution.setIssueDate(request.getIssueDate() != null ? request.getIssueDate() : LocalDate.now());
//// 
////        distributionRepository.save(existingDistribution);
////    }
//    
//    private void handleNewRecipientBalance(int academicYearId, DistributionRequestDTO request, int range) {
//        
//        // Attempt to find the existing BalanceTrack record for the recipient
//        Optional<BalanceTrack> newReceiverBalanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, request.getIssuedToEmpId());
//        BalanceTrack newReceiverBalance;
//        
//        // --- 1. Find or Create Balance Record and Update Count ---
//        if (newReceiverBalanceOpt.isPresent()) {
//            newReceiverBalance = newReceiverBalanceOpt.get();
//            // Increment the available count by the range being added
//            newReceiverBalance.setAppAvblCnt(newReceiverBalance.getAppAvblCnt() + range);
//        } else {
//            // Creation of new balance record (for new employee)
//            newReceiverBalance = new BalanceTrack();
//            newReceiverBalance.setEmployee(employeeRepository.findById(request.getIssuedToEmpId()).orElseThrow(() -> new RuntimeException("New receiver employee not found.")));
//            newReceiverBalance.setAcademicYear(academicYearRepository.findById(request.getAcademicYearId()).orElseThrow(() -> new RuntimeException("Academic year not found.")));
//            
//            // These will be overridden below, but set as initial values for safety
//            newReceiverBalance.setAppFrom(request.getAppStartNo());
//            newReceiverBalance.setAppTo(request.getAppEndNo());
//            
//            newReceiverBalance.setAppAvblCnt(range);
//            newReceiverBalance.setIssuedByType(appIssuedTypeRepository.findById(request.getIssuedToTypeId()).orElseThrow(() -> new RuntimeException("Issued by type not found.")));
//            newReceiverBalance.setIsActive(1);
//            newReceiverBalance.setCreatedBy(request.getCreatedBy());
//        }
//
//        // --- 2. CRUCIAL FIX: Update AppFrom and AppTo using Distribution Records ---
//        
//        // Find the true maximum application end number across ALL distributions for this employee
//        Optional<Integer> maxAppEndNo = distributionRepository
//            .findMaxAppEndNoByIssuedToEmpIdAndAcademicYearId(request.getIssuedToEmpId(), academicYearId);
//
//        if (maxAppEndNo.isPresent()) {
//            newReceiverBalance.setAppTo(maxAppEndNo.get());
//        }
//        
//        // Find the true minimum application start number across ALL distributions for this employee
//        Optional<Integer> minAppStartNo = distributionRepository
//            .findMinAppStartNoByIssuedToEmpIdAndAcademicYearId(request.getIssuedToEmpId(), academicYearId);
//
//        if (minAppStartNo.isPresent()) {
//            newReceiverBalance.setAppFrom(minAppStartNo.get());
//        }
//
//        // --- 3. Save the updated BalanceTrack ---
//        balanceTrackRepository.save(newReceiverBalance);
//    }
//    private Distribution createNewDistributionRecord(DistributionRequestDTO request, Distribution sourceDistribution) {
//        // Logic to create and populate the new Distribution record for User 2
//        Distribution newDistribution = new Distribution();
//        // Copy data from request and sourceDistribution (as a template)
//        newDistribution.setAcademicYear(sourceDistribution.getAcademicYear());
//        newDistribution.setState(stateRepository.findById(request.getStateId()).orElseThrow(() -> new RuntimeException("State not found.")));
//        newDistribution.setZone(zoneRepository.findById(request.getZoneId()).orElseThrow(() -> new RuntimeException("Zone not found.")));
//        newDistribution.setIssuedByType(appIssuedTypeRepository.findById(request.getIssuedByTypeId()).orElseThrow(() -> new RuntimeException("Issued by type not found.")));
//        newDistribution.setIssuedToType(appIssuedTypeRepository.findById(request.getIssuedToTypeId()).orElseThrow(() -> new RuntimeException("Issued to type not found.")));
//
//        City selectedCity = cityRepository.findById(request.getCityId()).orElseThrow(() -> new RuntimeException("City not found."));
//        newDistribution.setCity(selectedCity);
//        newDistribution.setDistrict(selectedCity.getDistrict());
//
//        newDistribution.setIssued_to_emp_id(request.getIssuedToEmpId());
//        newDistribution.setCreated_by(request.getCreatedBy());
//        newDistribution.setAppStartNo(request.getAppStartNo());
//        newDistribution.setAppEndNo(request.getAppEndNo());
//        newDistribution.setTotalAppCount(request.getRange());
//        newDistribution.setIsActive(1);
//        newDistribution.setIssueDate(request.getIssueDate() != null ? request.getIssueDate() : LocalDate.now());
//        
//        return newDistribution;
//    }
// 
//    @Transactional
//    public void updateDistribution(int distributionId, @NonNull DistributionRequestDTO request) {
//
//        // Step 1: Find the existing distribution record (User 1's old record)
//        Distribution existingDistribution = distributionRepository.findById(distributionId)
//                .orElseThrow(() -> new RuntimeException("Distribution record not found for ID: " + distributionId));
//
//        // Get old values
//        int oldIssuedToEmpId = existingDistribution.getIssued_to_emp_id();
//        int oldRange = existingDistribution.getTotalAppCount();
//        int newIssuedToEmpId = request.getIssuedToEmpId();
//        int newRange = request.getRange();
//        int academicYearId = existingDistribution.getAcademicYear().getAcdcYearId();
//        
//        // --- Determine the action type based on implicit business rules ---
//        boolean isRecipientChanging = oldIssuedToEmpId != newIssuedToEmpId;
//        boolean isRangeShrinking = newRange < oldRange;
//        
//        // A transfer is implicitly defined as:
//        // 1. The recipient is changing AND
//        // 2. The requested range is a strict subset of the existing distribution's range.
//        boolean isSplitTransfer = isRecipientChanging && 
//                                  isRangeShrinking &&
//                                  request.getAppStartNo() >= existingDistribution.getAppStartNo() &&
//                                  request.getAppEndNo() <= existingDistribution.getAppEndNo();
//                                  
//        // If the recipient is the same, it's always an Update/Adjustment, never a Transfer/Split.
//        if (!isRecipientChanging && newRange != oldRange) {
//            // If the same recipient changes the range, the current logic (B) is highly flawed 
//            // as it fully reverses and then re-adds the new range, which is risky.
//            // A better approach would be to calculate the net change (newRange - oldRange).
//            // For now, we'll let it proceed to the original (B) logic.
//            isSplitTransfer = false;
//        }
//        // --------------------------------------------------------------------------------------------------
//        
//        
//        // --------------------------------------------------------------------------------------------------
//        // A) HANDLE COMPLEX SPLIT/TRANSFER LOGIC
//        // (User 1 shrinks existing record, User 2 gets new record)
//        // --------------------------------------------------------------------------------------------------
//        if (isSplitTransfer) {
//            
//            // Validation for the transferred range
//            if (newRange != (request.getAppEndNo() - request.getAppStartNo() + 1)) {
//                throw new IllegalArgumentException("Transfer range calculation does not match start and end numbers.");
//            }
//
//            // 1. Adjust User 1's (Source) Balance
//            BalanceTrack user1Balance = balanceTrackRepository.findBalanceTrack(academicYearId, oldIssuedToEmpId)
//                    .orElseThrow(() -> new RuntimeException("Balance track not found for source employee ID: " + oldIssuedToEmpId));
//            
//            // Decrement available count by the amount being transferred (newRange)
//            user1Balance.setAppAvblCnt(user1Balance.getAppAvblCnt() - newRange);
//            
//            // 2. Update/Shrink User 1's Distribution Record (SourceDistribution)
//            if (request.getAppStartNo() == existingDistribution.getAppStartNo()) {
//                // Transfer is from the START (e.g., 800-879, transfer 800-809). Shrink by increasing start number.
//                existingDistribution.setAppStartNo(request.getAppEndNo() + 1);
//                
//            } else if (request.getAppEndNo() == existingDistribution.getAppEndNo()) {
//                // Transfer is from the END (e.g., 800-879, transfer 870-879). Shrink by decreasing end number.
//                existingDistribution.setAppEndNo(request.getAppStartNo() - 1);
//
//            } else {
//                // Transfer is from the MIDDLE (e.g., 800-879, transfer 830-839). This requires complex logic 
//                // to split the source into two records. For simplicity, we'll throw an exception.
//                throw new UnsupportedOperationException("Transferring from the middle of a range is not supported by this API.");
//            }
//            
//            // Finalize User 1's shrunken record
//            existingDistribution.setTotalAppCount(existingDistribution.getAppEndNo() - existingDistribution.getAppStartNo() + 1);
//            distributionRepository.save(existingDistribution); 
//
//            // 3. Save User 1's balance (AppTo/AppFrom fix is required here but is complex)
//            // For safety, we only save the AppAvblCnt change.
//            balanceTrackRepository.save(user1Balance); 
//
//            // 4. Handle User 2 (New Recipient) Balance
//            handleNewRecipientBalance(academicYearId, request, newRange);
//
//            // 5. Create User 2's New Distribution Record
//            Distribution user2Distribution = createNewDistributionRecord(request, existingDistribution); 
//            distributionRepository.save(user2Distribution);
//            
//            // **The method ends here for a transfer/split.**
//            return; 
//        }
//
//        // --------------------------------------------------------------------------------------------------
//        // B) STANDARD UPDATE / ENTIRE RANGE REASSIGNMENT LOGIC
//        // (This executes if it's not a split/transfer)
//        // --------------------------------------------------------------------------------------------------
//
//        // Step 2: Handle balance updates for old recipient (Reverting the original distribution)
//        Optional<BalanceTrack> oldReceiverBalanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, oldIssuedToEmpId);
//        if (oldReceiverBalanceOpt.isPresent()) {
//            BalanceTrack oldReceiverBalance = oldReceiverBalanceOpt.get();
//            oldReceiverBalance.setAppAvblCnt(oldReceiverBalance.getAppAvblCnt() - oldRange);
//            oldReceiverBalance.setAppTo(request.getAppStartNo() -1); 
//            balanceTrackRepository.save(oldReceiverBalance);
//        } else {
//             throw new RuntimeException("Old balance track not found for employee ID: " + oldIssuedToEmpId);
//        }
//        
//        // Step 3: Handle balance updates for the new recipient (Gaining the entire new range)
//        handleNewRecipientBalance(academicYearId, request, newRange);
//
//        // Step 4: Update the main distribution record (Reassignment/Details Change)
//        // This overwrites the existing record's details entirely.
//        // ... (All entity fetching logic remains the same as your original Step 4)
//        
//        // Set core fields for the updated record
//        existingDistribution.setIssued_to_emp_id(request.getIssuedToEmpId());
//        existingDistribution.setAppStartNo(request.getAppStartNo());
//        existingDistribution.setAppEndNo(request.getAppEndNo());
//        existingDistribution.setTotalAppCount(newRange);
//        // ... (rest of the fields)
//
//        distributionRepository.save(existingDistribution);
//    }
//
//    // NOTE: The helper methods (handleNewRecipientBalance and createNewDistributionRecord) 
//    // remain the same as defined in the previous response.
//    // Helper method to adjust BalanceTrack records
//    private void adjustBalanceTrack(int academicYearId, int employeeId, int changeInCount) {
//        Optional<BalanceTrack> balanceOpt = balanceTrackRepository.findBalanceTrack(academicYearId, employeeId);
//        if (balanceOpt.isPresent()) {
//            BalanceTrack balance = balanceOpt.get();
//            balance.setAppAvblCnt(balance.getAppAvblCnt() + changeInCount);
//            // This logic might need to be more complex based on business rules.
//            // For a simple update, we assume appTo is updated elsewhere.
//            balanceTrackRepository.save(balance);
//        } else {
//            // This case should ideally not happen for an update operation if the old record was valid.
//            throw new RuntimeException("Balance track not found for employee ID: " + employeeId);
//        }
//    }
//}
package com.application.service;

import com.application.dto.ApplicationStartEndDto;
import com.application.dto.DistributionRequestDTO;
import com.application.entity.*;
import com.application.repository.*;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ZoneService {

    private final AcademicYearRepository academicYearRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final ZoneRepository zoneRepository;
    private final AppIssuedTypeRepository appIssuedTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final StateAppRepository stateAppRepository;
    private final BalanceTrackRepository balanceTrackRepository;
    private final DistributionRepository distributionRepository;
    private final ZonalAccountantRepository zonalAccountantRepository;

    @Autowired
    public ZoneService(AcademicYearRepository academicYearRepository, StateRepository stateRepository,
                       CityRepository cityRepository, ZoneRepository zoneRepository,
                       AppIssuedTypeRepository appIssuedTypeRepository, EmployeeRepository employeeRepository,
                       StateAppRepository stateAppRepository, BalanceTrackRepository balanceTrackRepository,
                       DistributionRepository distributionRepository, ZonalAccountantRepository zonalAccountantRepository) {
        this.academicYearRepository = academicYearRepository;
        this.stateRepository = stateRepository;
        this.cityRepository = cityRepository;
        this.zoneRepository = zoneRepository;
        this.appIssuedTypeRepository = appIssuedTypeRepository;
        this.employeeRepository = employeeRepository;
        this.stateAppRepository = stateAppRepository;
        this.balanceTrackRepository = balanceTrackRepository;
        this.distributionRepository = distributionRepository;
        this.zonalAccountantRepository = zonalAccountantRepository;
    }

    // --- Dropdown/Helper Methods ---
    public List<AcademicYear> getAllAcademicYears() { return academicYearRepository.findAll(); }
    public List<State> getAllStates() { return stateRepository.findAll(); }
    public List<City> getCitiesByState(int stateId) { return cityRepository.findByDistrictStateStateId(stateId); }
    public List<Zone> getZonesByCity(int cityId) { return zoneRepository.findByCityCityId(cityId); }
    public List<Employee> getIssuableToEmployees() { return employeeRepository.findAll(); }
    public Integer getAppEndNoForUser(int stateId, int userId) { return stateAppRepository.findAppEndNoByStateAndUser(stateId, userId); }
    public List<Employee> getEmployeesByZone(int zoneId) {
        return zonalAccountantRepository.findByZoneZoneId(zoneId).stream()
            .map(ZonalAccountant::getEmployee).collect(Collectors.toList());
    }
    public String getNextApplicationNumber(int academicYearId, int stateId, int userId) {
        Integer lastAppNumber = distributionRepository.findMaxAppEndNo(stateId, userId, academicYearId);
        if (lastAppNumber != null) return String.valueOf(lastAppNumber + 1);
        return stateAppRepository.findStartNumber(stateId, userId, academicYearId)
            .map(sa -> String.valueOf(sa.getApp_start_no())).orElse("ERROR_NO_RANGE_ASSIGNED");
    }
    public ApplicationStartEndDto getAppNumberRanges(int academicYearId, int stateId, int createdBy) {
        return stateAppRepository.findAppNumberRanges(academicYearId, stateId, createdBy)
                .orElseThrow(() -> new RuntimeException("App ranges not found."));
    }
    
    // --- CORE METHODS ---

    @Transactional
    public void saveDistribution(@NonNull DistributionRequestDTO request) {
        validateEmployeeExists(request.getCreatedBy(), "Issuer");
        validateEmployeeExists(request.getIssuedToEmpId(), "Receiver");

        // --- "Revoke on Overlap" Logic ---
        List<Distribution> overlappingDists = distributionRepository.findOverlappingDistributions(
            request.getAcademicYearId(), request.getAppStartNo(), request.getAppEndNo());

        if (!overlappingDists.isEmpty()) {
            handleOverlappingDistributions(overlappingDists, request);
        }
        // --- End of Overlap Logic ---

        Distribution newDistribution = new Distribution();
        mapDtoToDistribution(newDistribution, request);
        distributionRepository.save(newDistribution);

        recalculateBalanceForEmployee(request.getCreatedBy(), request.getAcademicYearId(), request.getStateId(), request.getIssuedByTypeId(), request.getCreatedBy());
        recalculateBalanceForEmployee(request.getIssuedToEmpId(), request.getAcademicYearId(), request.getStateId(), request.getIssuedToTypeId(), request.getCreatedBy());
    }
    
    @Transactional
    public void updateDistribution(int distributionId, @NonNull DistributionRequestDTO request) {
        validateEmployeeExists(request.getCreatedBy(), "Issuer");
        validateEmployeeExists(request.getIssuedToEmpId(), "New Receiver");

        Distribution existingDistribution = distributionRepository.findById(distributionId)
                .orElseThrow(() -> new RuntimeException("Distribution record not found: " + distributionId));

        int oldReceiverId = existingDistribution.getIssued_to_emp_id();
        boolean isRecipientChanging = oldReceiverId != request.getIssuedToEmpId();
        boolean isRangeShrinking = request.getRange() < existingDistribution.getTotalAppCount();
        
        // --- Condition for a Split/Transfer ---
        if (isRecipientChanging && isRangeShrinking &&
            request.getAppStartNo() >= existingDistribution.getAppStartNo() &&
            request.getAppEndNo() <= existingDistribution.getAppEndNo()) {
            
            // --- HANDLE SPLIT LOGIC ---
            existingDistribution.setTotalAppCount(existingDistribution.getTotalAppCount() - request.getRange());
            existingDistribution.setAppStartNo(request.getAppEndNo() + 1); // This assumes splitting from the start
            distributionRepository.save(existingDistribution);

            Distribution newSplitDistribution = new Distribution();
            mapDtoToDistribution(newSplitDistribution, request);
            distributionRepository.save(newSplitDistribution);

        } else {
            // --- HANDLE SIMPLE UPDATE/REASSIGNMENT ---
            mapDtoToDistribution(existingDistribution, request);
            distributionRepository.save(existingDistribution);
        }

        // --- Recalculate balances for all affected parties ---
        int academicYearId = existingDistribution.getAcademicYear().getAcdcYearId();
        int stateId = existingDistribution.getState().getStateId();
        
        recalculateBalanceForEmployee(request.getCreatedBy(), academicYearId, stateId, request.getIssuedByTypeId(), request.getCreatedBy());
        recalculateBalanceForEmployee(request.getIssuedToEmpId(), academicYearId, stateId, request.getIssuedToTypeId(), request.getCreatedBy());
        
        if (isRecipientChanging) {
            balanceTrackRepository.findBalanceTrack(academicYearId, oldReceiverId).ifPresent(oldBalance -> {
                int oldReceiverTypeId = oldBalance.getIssuedByType().getAppIssuedId(); 
                recalculateBalanceForEmployee(oldReceiverId, academicYearId, stateId, oldReceiverTypeId, request.getCreatedBy());
            });
        }
    }

    // --- PRIVATE HELPER METHODS ---

    private void handleOverlappingDistributions(List<Distribution> overlappingDists, DistributionRequestDTO request) {
        for (Distribution oldDist : overlappingDists) {
            int oldReceiverId = oldDist.getIssued_to_emp_id();
            
            // Do nothing if the overlap is with the same person we are issuing to.
            if (oldReceiverId == request.getIssuedToEmpId()) continue;

            // Case 1: The new range completely takes over the old one.
            if (request.getAppStartNo() <= oldDist.getAppStartNo() && request.getAppEndNo() >= oldDist.getAppEndNo()) {
                distributionRepository.delete(oldDist);
            } 
            // Case 2: The new range takes the end part of the old one.
            else if (request.getAppStartNo() > oldDist.getAppStartNo() && request.getAppEndNo() >= oldDist.getAppEndNo()) {
                oldDist.setAppEndNo(request.getAppStartNo() - 1);
                oldDist.setTotalAppCount(oldDist.getAppEndNo() - oldDist.getAppStartNo() + 1);
                distributionRepository.save(oldDist);
            }
            // Case 3: The new range takes the start part of the old one.
            else if (request.getAppStartNo() <= oldDist.getAppStartNo() && request.getAppEndNo() < oldDist.getAppEndNo()) {
                oldDist.setAppStartNo(request.getAppEndNo() + 1);
                oldDist.setTotalAppCount(oldDist.getAppEndNo() - oldDist.getAppStartNo() + 1);
                distributionRepository.save(oldDist);
            }
            // Case 4: The new range splits an old block in the middle. (This creates two records from one)
            else if (request.getAppStartNo() > oldDist.getAppStartNo() && request.getAppEndNo() < oldDist.getAppEndNo()) {
                // Create a new record for the part *after* the split
                Distribution afterSplit = new Distribution();
                mapDtoToDistribution(afterSplit, createDtoFromDistribution(oldDist)); // copy existing data
                afterSplit.setAppStartNo(request.getAppEndNo() + 1);
                afterSplit.setTotalAppCount(afterSplit.getAppEndNo() - afterSplit.getAppStartNo() + 1);
                distributionRepository.save(afterSplit);
                
                // Update the original record to be the part *before* the split
                oldDist.setAppEndNo(request.getAppStartNo() - 1);
                oldDist.setTotalAppCount(oldDist.getAppEndNo() - oldDist.getAppStartNo() + 1);
                distributionRepository.save(oldDist);
            }

            // After adjusting the old distribution, recalculate balance for the employee who lost applications
            recalculateBalanceForEmployee(oldReceiverId, request.getAcademicYearId(), oldDist.getState().getStateId(), oldDist.getIssuedToType().getAppIssuedId(), request.getCreatedBy());
        }
    }

    private void recalculateBalanceForEmployee(int employeeId, int academicYearId, int stateId, int typeId, int createdBy) {
        BalanceTrack balance = balanceTrackRepository.findBalanceTrack(academicYearId, employeeId)
                .orElseGet(() -> createNewBalanceTrack(employeeId, academicYearId, typeId, createdBy));

        Optional<StateApp> initialState = stateAppRepository.findStartNumber(stateId, employeeId, academicYearId);

        if (initialState.isPresent()) {
            StateApp stateApp = initialState.get();
            int totalDistributed = distributionRepository.sumTotalAppCountByCreatedBy(employeeId, academicYearId).orElse(0);
            balance.setAppFrom(stateApp.getApp_start_no());
            balance.setAppTo(stateApp.getApp_end_no());
            balance.setAppAvblCnt(stateApp.getTotal_no_of_app() - totalDistributed);
        } else {
            Integer totalReceived = distributionRepository.sumTotalAppCountByIssuedToEmpId(employeeId, academicYearId).orElse(0);
            if (totalReceived > 0) {
                Integer minAppNo = distributionRepository.findMinAppStartNoByIssuedToEmpIdAndAcademicYearId(employeeId, academicYearId).orElse(0);
                Integer maxAppNo = distributionRepository.findMaxAppEndNoByIssuedToEmpIdAndAcademicYearId(employeeId, academicYearId).orElse(0);
                balance.setAppFrom(minAppNo);
                balance.setAppTo(maxAppNo);
            } else {
                balance.setAppFrom(0);
                balance.setAppTo(0);
            }
            balance.setAppAvblCnt(totalReceived);
        }
        balanceTrackRepository.save(balance);
    }
    
    private void mapDtoToDistribution(Distribution d, DistributionRequestDTO req) {
        d.setAcademicYear(academicYearRepository.findById(req.getAcademicYearId()).orElseThrow());
        d.setState(stateRepository.findById(req.getStateId()).orElseThrow());
        d.setZone(zoneRepository.findById(req.getZoneId()).orElseThrow());
        d.setIssuedByType(appIssuedTypeRepository.findById(req.getIssuedByTypeId()).orElseThrow());
        d.setIssuedToType(appIssuedTypeRepository.findById(req.getIssuedToTypeId()).orElseThrow());
        City city = cityRepository.findById(req.getCityId()).orElseThrow();
        d.setCity(city);
        d.setDistrict(city.getDistrict());
        d.setIssued_to_emp_id(req.getIssuedToEmpId());
        d.setCreated_by(req.getCreatedBy());
        d.setAppStartNo(req.getAppStartNo());
        d.setAppEndNo(req.getAppEndNo());
        d.setTotalAppCount(req.getRange());
        d.setIsActive(1);
        d.setIssueDate(req.getIssueDate() != null ? req.getIssueDate() : LocalDate.now());
    }

    private DistributionRequestDTO createDtoFromDistribution(Distribution dist) {
        DistributionRequestDTO dto = new DistributionRequestDTO();
        dto.setAcademicYearId(dist.getAcademicYear().getAcdcYearId());
        dto.setStateId(dist.getState().getStateId());
        dto.setCityId(dist.getCity().getCityId());
        dto.setZoneId(dist.getZone().getZoneId());
        dto.setIssuedByTypeId(dist.getIssuedByType().getAppIssuedId());
        dto.setIssuedToTypeId(dist.getIssuedToType().getAppIssuedId());
        dto.setIssuedToEmpId(dist.getIssued_to_emp_id());
        dto.setAppStartNo(dist.getAppStartNo());
        dto.setAppEndNo(dist.getAppEndNo());
        dto.setRange(dist.getTotalAppCount());
        dto.setIssueDate(dist.getIssueDate());
        dto.setCreatedBy(dist.getCreated_by());
        return dto;
    }

    private BalanceTrack createNewBalanceTrack(int employeeId, int academicYearId, int typeId, int createdBy) {
        BalanceTrack nb = new BalanceTrack();
        nb.setEmployee(employeeRepository.findById(employeeId).orElseThrow());
        nb.setAcademicYear(academicYearRepository.findById(academicYearId).orElseThrow());
        nb.setIssuedByType(appIssuedTypeRepository.findById(typeId).orElseThrow());
        nb.setAppAvblCnt(0);
        nb.setAppFrom(0);
        nb.setAppTo(0);
        nb.setIsActive(1);
        nb.setCreatedBy(createdBy); // **FIXED**
        return nb;
    }

    private void validateEmployeeExists(int employeeId, String role) {
        if (employeeId <= 0 || !employeeRepository.existsById(employeeId)) {
            throw new IllegalArgumentException(role + " employee not found or invalid ID: " + employeeId);
        }
    }
}