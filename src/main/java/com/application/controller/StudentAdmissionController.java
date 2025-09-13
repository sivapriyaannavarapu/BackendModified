//
//package com.application.controller;
//
//import com.application.dto.GenericDropdownDTO;
//import com.application.dto.StudentAdmissionDTO;
//import com.application.entity.SchoolDetails;
//import com.application.service.StudentAdmissionService;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/student-admissions-sale")
//@CrossOrigin(origins = "*")
//public class StudentAdmissionController {
//
//    @Autowired
//    private StudentAdmissionService studentAdmissionService;
//
//    // --- Endpoint for Form Submission ---
//    @PostMapping("/create")
//    public ResponseEntity<String> createAdmissionForm(@RequestBody StudentAdmissionDTO formDto) {
//        try {
//            studentAdmissionService.createNewAdmission(formDto);
//            return ResponseEntity.ok("Admission form created successfully!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body("Failed to save form: " + e.getMessage());
//        }
//    }
//
//    // --- Endpoints for Populating Dropdowns ---
//    @GetMapping("/admission-types")
//    public List<GenericDropdownDTO> getAdmissionTypes() {
//        return studentAdmissionService.getAllAdmissionTypes();
//    }
//
//    @GetMapping("/student-types")
//    public List<GenericDropdownDTO> getStudentTypes() {
//        return studentAdmissionService.getAllStudentTypes();
//    }
//
//    @GetMapping("/genders")
//    public List<GenericDropdownDTO> getGenders() {
//        return studentAdmissionService.getAllGenders();
//    }
//
//    @GetMapping("/campuses")
//    public List<GenericDropdownDTO> getCampuses() {
//        return studentAdmissionService.getAllCampuses();
//    }
//
//    @GetMapping("/courses")
//    public List<GenericDropdownDTO> getCourses() {
//        return studentAdmissionService.getAllCourses();
//    }
//
//    @GetMapping("/course-batches")
//    public List<GenericDropdownDTO> getCourseBatches() {
//        return studentAdmissionService.getAllCourseBatches();
//    }
//
//    @GetMapping("/states")
//    public List<GenericDropdownDTO> getStates() {
//        return studentAdmissionService.getAllStates();
//    }
//
//    @GetMapping("/districts/{stateId}")
//    public List<GenericDropdownDTO> getDistrictsByState(@PathVariable int stateId) {
//        return studentAdmissionService.getDistrictsByState(stateId);
//    }
//
//    @GetMapping("/school-types")
//    public List<GenericDropdownDTO> getSchoolTypes() {
//        return studentAdmissionService.getAllSchoolTypes();
//    }
//
//    @GetMapping("/quotas")
//    public List<GenericDropdownDTO> getQuotas() {
//        return studentAdmissionService.getAllQuotas();
//    }
//
//    @GetMapping("/relation-types")
//    public List<GenericDropdownDTO> getRelationTypes() {
//        return studentAdmissionService.getAllRelationTypes();
//    }
//
//    @GetMapping("/classes")
//    public List<GenericDropdownDTO> getClasses() {
//        return studentAdmissionService.getAllClasses();
//    }
//
//    // --- New Endpoints for Get By ID ---
//    @GetMapping("/student-type/{id}")
//    public ResponseEntity<GenericDropdownDTO> getStudentTypeById(@PathVariable int id) {
//        try {
//            GenericDropdownDTO studentType = studentAdmissionService.getStudentTypeById(id);
//            return ResponseEntity.ok(studentType);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    @GetMapping("/campus/{id}")
//    public ResponseEntity<GenericDropdownDTO> getCampusById(@PathVariable int id) {
//        try {
//            GenericDropdownDTO campus = studentAdmissionService.getCampusById(id);
//            return ResponseEntity.ok(campus);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    @GetMapping("/city/{id}")
//    public ResponseEntity<GenericDropdownDTO> getCityById(@PathVariable int id) {
//        try {
//            GenericDropdownDTO city = studentAdmissionService.getCityById(id);
//            return ResponseEntity.ok(city);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    @GetMapping("/orientation/{id}")
//    public ResponseEntity<GenericDropdownDTO> getOrientationById(@PathVariable int id) {
//        try {
//            GenericDropdownDTO orientation = studentAdmissionService.getOrientationById(id);
//            return ResponseEntity.ok(orientation);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    @GetMapping("/class/{id}")
//    public ResponseEntity<GenericDropdownDTO> getStudentClassById(@PathVariable int id) {
//        try {
//            GenericDropdownDTO studentClass = studentAdmissionService.getStudentClassById(id);
//            return ResponseEntity.ok(studentClass);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    @GetMapping("/course/{id}")
//    public ResponseEntity<GenericDropdownDTO> getCourseGroupById(@PathVariable int id) {
//        try {
//            GenericDropdownDTO courseGroup = studentAdmissionService.getCourseGroupById(id);
//            return ResponseEntity.ok(courseGroup);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    @GetMapping("/course-batch/{id}")
//    public ResponseEntity<GenericDropdownDTO> getCourseBatchById(@PathVariable int id) {
//        try {
//            GenericDropdownDTO courseBatch = studentAdmissionService.getCourseBatchById(id);
//            return ResponseEntity.ok(courseBatch);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    @GetMapping("/state/{id}")
//    public ResponseEntity<GenericDropdownDTO> getStateById(@PathVariable int id) {
//        try {
//            GenericDropdownDTO state = studentAdmissionService.getStateById(id);
//            return ResponseEntity.ok(state);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    @GetMapping("/district/{id}")
//    public ResponseEntity<GenericDropdownDTO> getDistrictById(@PathVariable int id) {
//        try {
//            GenericDropdownDTO district = studentAdmissionService.getDistrictById(id);
//            return ResponseEntity.ok(district);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    @GetMapping("/school-type/{id}")
//    public ResponseEntity<GenericDropdownDTO> getSchoolTypeById(@PathVariable int id) {
//        try {
//            GenericDropdownDTO schoolType = studentAdmissionService.getSchoolTypeById(id);
//            return ResponseEntity.ok(schoolType);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    @GetMapping("/school-details/{schoolName}")
//    public ResponseEntity<SchoolDetails> getSchoolDetailsByName(@PathVariable String schoolName) {
//        try {
//            SchoolDetails schoolDetails = studentAdmissionService.getSchoolDetailsByName(schoolName);
//            return ResponseEntity.ok(schoolDetails);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    @GetMapping("/quota/{id}")
//    public ResponseEntity<GenericDropdownDTO> getQuotaById(@PathVariable int id) {
//        try {
//            GenericDropdownDTO quota = studentAdmissionService.getQuotaById(id);
//            return ResponseEntity.ok(quota);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    @GetMapping("/relation-type/{id}")
//    public ResponseEntity<GenericDropdownDTO> getRelationTypeById(@PathVariable int id) {
//        try {
//            GenericDropdownDTO relationType = studentAdmissionService.getRelationTypeById(id);
//            return ResponseEntity.ok(relationType);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    @GetMapping("/gender/{id}")
//    public ResponseEntity<GenericDropdownDTO> getGenderById(@PathVariable int id) {
//        try {
//            GenericDropdownDTO gender = studentAdmissionService.getGenderById(id);
//            return ResponseEntity.ok(gender);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    @GetMapping("/concession-reason/{id}")
//    public ResponseEntity<GenericDropdownDTO> getConcessionReasonById(@PathVariable int id) {
//        try {
//            GenericDropdownDTO reason = studentAdmissionService.getConcessionReasonById(id);
//            return ResponseEntity.ok(reason);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    @GetMapping("/mandal/{id}")
//    public ResponseEntity<GenericDropdownDTO> getMandalById(@PathVariable int id) {
//        try {
//            GenericDropdownDTO mandal = studentAdmissionService.getMandalById(id);
//            return ResponseEntity.ok(mandal);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    @GetMapping("/organization/{id}")
//    public ResponseEntity<GenericDropdownDTO> getOrganizationById(@PathVariable int id) {
//        try {
//            GenericDropdownDTO organization = studentAdmissionService.getOrganizationById(id);
//            return ResponseEntity.ok(organization);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    @GetMapping("/bank/{id}")
//    public ResponseEntity<GenericDropdownDTO> getOrgBankById(@PathVariable int id) {
//        try {
//            GenericDropdownDTO bank = studentAdmissionService.getOrgBankById(id);
//            return ResponseEntity.ok(bank);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//}


package com.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.dto.ApplyCouponDTO;
import com.application.dto.BankDetailsDTO;
import com.application.dto.BatchDTO;
import com.application.dto.GenericDropdownDTO;
import com.application.dto.OrientationBatchDetailsDTO;
import com.application.dto.OrientationResponseDTO;
import com.application.dto.StudentAdmissionDTO;
import com.application.entity.StudyType;
import com.application.service.StudentAdmissionService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/student-admissions-sale")
@CrossOrigin(origins = "*")
public class StudentAdmissionController {

    @Autowired
    private StudentAdmissionService studentAdmissionService;

    // --- Endpoint for Form Submission ---
    @PostMapping("/create")
    public ResponseEntity<String> createAdmissionForm(@RequestBody StudentAdmissionDTO formDto) {
        try {
            studentAdmissionService.createNewAdmission(formDto);
            return ResponseEntity.ok("Admission form created successfully!");
        } catch (Exception e) {
            // Log the full stack trace for debugging purposes
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to save form: " + e.getMessage());
        }
    }

    // --- Endpoints for Populating Dropdowns ---
    @GetMapping("/admission-types")
    public List<GenericDropdownDTO> getAdmissionTypes() {
        return studentAdmissionService.getAllAdmissionTypes();
    }

    @GetMapping("/student-types")
    public List<GenericDropdownDTO> getStudentTypes() {
        return studentAdmissionService.getAllStudentTypes();
    }

    @GetMapping("/genders")
    public List<GenericDropdownDTO> getGenders() {
        return studentAdmissionService.getAllGenders();
    }

    @GetMapping("/campuses")
    public List<GenericDropdownDTO> getCampuses() {
        return studentAdmissionService.getAllCampuses();
    }
   
    @GetMapping("/orientations/{campusId}")
    public List<OrientationResponseDTO> getOrientationsByCampus(@PathVariable int campusId) {
        return studentAdmissionService.getOrientationsByCampus(campusId);
    }
    
 // ... inside the controller class

    @GetMapping("/districts/{stateId}")
    public List<GenericDropdownDTO> getDistrictsByState(@PathVariable int stateId) {
        return studentAdmissionService.getDistrictsByState(stateId);
    }

    @GetMapping("/mandals/{districtId}")
    public List<GenericDropdownDTO> getMandalsByDistrict(@PathVariable int districtId) {
        return studentAdmissionService.getMandalsByDistrict(districtId);
    }

    @GetMapping("/cities/{districtId}")
    public List<GenericDropdownDTO> getCitiesByDistrict(@PathVariable int districtId) {
        return studentAdmissionService.getCitiesByDistrict(districtId);
    }
    
 // ... inside the controller class
    @GetMapping("/organizations")
    public List<GenericDropdownDTO> getAllOrganizations() {
        return studentAdmissionService.getAllOrganizations();
    }

    /**
     * Endpoint to get banks for a selected organization.
     * Example URL: GET http://localhost:8080/api/student-admissions/banks/1
     */
    @GetMapping("/banks/{orgId}")
    public List<GenericDropdownDTO> getBanksByOrganization(@PathVariable int orgId) {
        return studentAdmissionService.getBanksByOrganization(orgId);
    }
   


    @GetMapping("/all/Studentclass")
    public List<GenericDropdownDTO> getAllStudentClass() {
        return studentAdmissionService.getAllStudentclass();
    }
    
    
    @GetMapping("/studtype{id}")
    public ResponseEntity<StudyType> getStudyTypeById(@PathVariable int id) {
        StudyType studyType = studentAdmissionService.getStudyTypeById(id);
 
        if (studyType != null) {
            return ResponseEntity.ok(studyType);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    /**
     * Endpoint to get branches for a selected organization and bank.
     * Example URL: GET http://localhost:8080/api/student-admissions/branches/1/101
     */
    @GetMapping("/branches/{orgId}/{bankId}")
    public List<GenericDropdownDTO> getBranchesByOrganizationAndBank(
            @PathVariable int orgId,
            @PathVariable int bankId) {
        return studentAdmissionService.getBranchesByOrganizationAndBank(orgId, bankId);
    }
    
    @GetMapping("/bank-details")
    public ResponseEntity<BankDetailsDTO> getBankDetails(
            @RequestParam int orgId,
            @RequestParam int bankId,
            @RequestParam int branchId) {
        try {
            BankDetailsDTO bankDetails = studentAdmissionService.getBankDetails(orgId, bankId, branchId);
            return ResponseEntity.ok(bankDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    

    @GetMapping("/quotas")
    public List<GenericDropdownDTO> getAllQuotas() {
        return studentAdmissionService.getAllQuotas();
    }
    
    @GetMapping("/AuthrizedBy/{campusId}")
    public ResponseEntity<List<GenericDropdownDTO>> getEmployeesByCampus(@PathVariable int campusId) {
        List<GenericDropdownDTO> employees = studentAdmissionService.getEmployeesByCampusId(campusId);
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employees);
    }
    @GetMapping("/authorizedBy/all")
    public List<GenericDropdownDTO> getAllEmployees() {
        return studentAdmissionService.getAllEmployees();
    }
    @GetMapping("/concessionReson/all")
    public List<GenericDropdownDTO> getAllConcessionReasons() {
        return studentAdmissionService.getAllConcessionReasons();
    }
    @GetMapping("/BloodGroup/all")
    public List<GenericDropdownDTO> getAllBloodGroups() {
        return studentAdmissionService.getAllBloodGroups();
    }
    @GetMapping("/PaymentModes/all")
    public List<GenericDropdownDTO> getAllPaymentModes() {
        return studentAdmissionService.getAllPaymentModes();
    }
    
    @PostMapping("/applyCoupon")
    public ResponseEntity<?> applyCoupon(@RequestBody ApplyCouponDTO dto) {
        try {
            double discountAmount = studentAdmissionService.applyCoupon(dto);
            return ResponseEntity.ok(discountAmount);
        } catch (EntityNotFoundException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
    @GetMapping("/classes-by-campus/{campusId}")
    public List<GenericDropdownDTO> getClassesByCampus(@PathVariable int campusId) {
        return studentAdmissionService.getClassNamesByCampusId(campusId);
    }
    
    @GetMapping("/study_typebycmpsId_and_classId")
    public ResponseEntity<List<GenericDropdownDTO>> getStudyTypes(
        @RequestParam("cmpsId") int cmpsId,
        @RequestParam("classId") int classId
    ) {
        List<GenericDropdownDTO> studyTypes = studentAdmissionService.getStudyTypesByCampusAndClass(cmpsId, classId);
        if (studyTypes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(studyTypes, HttpStatus.OK);
    }
    
    @GetMapping("/orientationbycmpsId_and_classId_and_studyType")
    public ResponseEntity<List<GenericDropdownDTO>> getOrientations(
        @RequestParam("cmpsId") int cmpsId,
        @RequestParam("classId") int classId,
        @RequestParam("studyTypeId") int studyTypeId
    ) {
        List<GenericDropdownDTO> orientations = studentAdmissionService.getOrientationsByCampusClassAndStudyType(cmpsId, classId, studyTypeId);
        if (orientations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orientations, HttpStatus.OK);
    }
    
    @GetMapping("/orientation-batchbycmpsId_and_classId_and_studyType_and_orientation")
    public ResponseEntity<List<GenericDropdownDTO>> getOrientationBatches(
        @RequestParam("cmpsId") int cmpsId,
        @RequestParam("classId") int classId,
        @RequestParam("studyTypeId") int studyTypeId,
        @RequestParam("orientationId") int orientationId
    ) {
        List<GenericDropdownDTO> batches = studentAdmissionService.getOrientationBatchesByAllCriteria(cmpsId, classId, studyTypeId, orientationId);
        if (batches.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(batches, HttpStatus.OK);
    }
    
    @GetMapping("/get_orientation_startDate_and_fee")
    public ResponseEntity<OrientationBatchDetailsDTO> getOrientationBatchDetails(
        @RequestParam("cmpsId") int cmpsId,
        @RequestParam("classId") int classId,
        @RequestParam("studyTypeId") int studyTypeId,
        @RequestParam("orientationId") int orientationId,
        @RequestParam("orientationBatchId") int orientationBatchId
    ) {
        return studentAdmissionService.getBatchDetails(cmpsId, classId, studyTypeId, orientationId, orientationBatchId)
            .map(ResponseEntity::ok)
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}