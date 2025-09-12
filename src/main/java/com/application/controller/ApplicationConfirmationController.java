package com.application.controller;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.application.dto.ApplicationConfirmationDto;
import com.application.dto.BatchDTO;
import com.application.dto.BatchDetailsDTO;
import com.application.dto.CampusAndZoneDTO;
import com.application.dto.EmployeeDetailsDTO;
import com.application.dto.ExamProgramDTO;
import com.application.dto.OrientationDTO;
import com.application.dto.ProgramDTO;
import com.application.dto.SectionDTO;
import com.application.dto.StreamDTO;
import com.application.dto.StudentDetailsDTO;
import com.application.entity.AcademicYear;
import com.application.entity.ConcessionReason;
import com.application.entity.ExamProgram;
import com.application.entity.FoodType;
import com.application.entity.Language;
import com.application.entity.Orientation;
import com.application.entity.OrientationBatch;
import com.application.entity.ProgramName;
import com.application.entity.Section;
import com.application.entity.Stream;
import com.application.service.ApplicationConfirmationService;

@RestController
@RequestMapping("/api/application-confirmation")
@CrossOrigin(origins = "*") 
public class ApplicationConfirmationController {
    
    @Autowired
    private ApplicationConfirmationService service;
    
    @PostMapping("/save")
    public ResponseEntity<String> saveAdmission(@RequestBody ApplicationConfirmationDto dto) {
        try {
            service.saveOrUpdateAdmission(dto);
            return ResponseEntity.ok("Admission details saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/join-years")
    public ResponseEntity<List<AcademicYear>> getJoinYears() {
        try {
            return ResponseEntity.ok(service.getJoinYears());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
//    @GetMapping("/batch/{batchId}/dates")
//    public ResponseEntity<BatchDatesResponse> getBatchDates(@PathVariable Integer batchId) {
//        BatchDatesResponse response = service.getCourseBatchDetails(batchId);
//        return ResponseEntity.ok(response);
//    }

    // 2. Get Course Fee by CourseTrackId
    @GetMapping("/{admissionNo}/campus-zone")
    public ResponseEntity<CampusAndZoneDTO> getCampusAndZoneByAdmissionNo(@PathVariable String admissionNo) {
        CampusAndZoneDTO campusAndZone = service.getCampusAndZoneByAdmissionNo(admissionNo);

        if (campusAndZone != null) {
            return ResponseEntity.ok(campusAndZone);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/course-fee/campus/{cmpsId}/track/{courseTrackId}/batch/{courseBatchId}")
    public ResponseEntity<Float> getCourseFee(
            @PathVariable int cmpsId,
            @PathVariable int courseTrackId,
            @PathVariable int courseBatchId) {

        Optional<Float> courseFee = service.getCourseFeeByDetails(cmpsId, courseTrackId, courseBatchId);

        return courseFee.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/details/{admissionNo}")
    public ResponseEntity<StudentDetailsDTO> getStudentDetailsByAdmission(@PathVariable String admissionNo) {
        return service.getStudentDetailsByAdmissionNo(admissionNo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    
    @GetMapping("/streams")
    public ResponseEntity<List<Stream>> getStreams() {
        try {
            return ResponseEntity.ok(service.getStreams());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/programs")
    public ResponseEntity<List<ProgramName>> getPrograms() {
        try {
            return ResponseEntity.ok(service.getPrograms());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/examprograms")
    public List<ExamProgram> getAllExamPrograms() {
        return service.getAllExamPrograms();
    }
    
    @GetMapping("/course-tracks")
    public ResponseEntity<List<Orientation>> getCourseTracks() {
        try {
            return ResponseEntity.ok(service.getCourseTracks());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/course-batches")
    public ResponseEntity<List<OrientationBatch>> getCourseBatches() {
        try {
            return ResponseEntity.ok(service.getCourseBatches());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/sections")
    public ResponseEntity<List<Section>> getSections() {
        try {
            return ResponseEntity.ok(service.getSections());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/concession-reasons")
    public ResponseEntity<List<ConcessionReason>> getConcessionReasons() {
        try {
            return ResponseEntity.ok(service.getConcessionReasons());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/employee-details")
    public ResponseEntity<EmployeeDetailsDTO> getEmployeeDetails(@RequestParam String admissionNo) {
        Optional<EmployeeDetailsDTO> employeeDetails = service.getEmployeeDetailsByAdmissionNo(admissionNo);
        return employeeDetails.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                              .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
//    @GetMapping("/getProgramsByStream")
//    public List<ProgramName> getProgramsByStream(@RequestParam int streamId) {
//        return service.getProgramsByStream(streamId);
//    }
    
//    @GetMapping("/by-course-track/id/{courseTrackId}")
//    public List<Stream> getStreamsByCourseTrackId(@PathVariable int courseTrackId) {
//        return service.getStreamsByCourseTrackId(courseTrackId);
//    }
    
//    @GetMapping("/getbatches/{courseTrackId}")
//    public List<CourseBatch> getCourseBatchesByCourseTrackId(@PathVariable int courseTrackId) {
//        return service.getCourseBatchesByCourseTrackId(courseTrackId);
//    }
    
    @GetMapping("/examPrograms/{programId}")
    public List<ExamProgramDTO> getExamProgramsByProgram(@PathVariable int programId) {
        ProgramName programName = new ProgramName();  
        programName.setProgramId(programId);
        
        return service.getExamProgramsByProgramId(programId);
    }
    
    @GetMapping("/dropdownforjoinyear")
    public Map<String, Object> getAcademicYears() {
        return service.getDropdownAcademicYears();
    }
    
    @GetMapping("/getalllanguages")
    public List<Language> getAllLanguages()
    {
    	return service.getAllLanguages();
    }
    
    @GetMapping("/getallfoodtypes")
    public List<FoodType> getAllFoodTypes()
    {
    	return service.getAllFoodTypes();
    }
    
    @GetMapping("/campus/{campusId}/getorientation")
    public List<OrientationDTO> getOrientations(@PathVariable int campusId) {
        return service.getOrientationsByCampusId(campusId);
    }

    @GetMapping("/orientation/{orientationId}/getstreams")
    public List<StreamDTO> getStreams(@PathVariable int orientationId) {
        return service.getStreamsByOrientationId(orientationId);
    }
    
    @GetMapping("/orientation/{orientationId}/programs")
    public List<ProgramDTO> getProgramsByOrientationId(@PathVariable int orientationId) {
        return service.getProgramsByOrientationId(orientationId);
    }

    // Endpoint to get exam programs by a specific program ID
    @GetMapping("/program/{programId}/exam-programs")
    public List<ExamProgramDTO> getExamProgramsByProgramId(@PathVariable int programId) {
        return service.getExamProgramsByProgramId(programId);
    }
    
    @GetMapping("/orientation/{orientationId}/batches")
    public List<BatchDTO> getBatchesByOrientationId(@PathVariable int orientationId) {
        return service.getBatchesByOrientationId(orientationId);
    }

    // Endpoint to get detailed information about a single batch by its ID
    @GetMapping("/{batchId}/details")
    public ResponseEntity<List<BatchDetailsDTO>> getBatchDetailsByBatchId(@PathVariable int batchId) {
        List<BatchDetailsDTO> batchDetails = service.getBatchDetailsByBatchId(batchId);

        if (batchDetails.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(batchDetails);
    }


    // Endpoint to get sections associated with a batch name
    @GetMapping("/by-id/{batchId}/sections")
    public List<SectionDTO> getSectionsByBatchId(@PathVariable int batchId) {
        return service.getSectionsByBatchId(batchId);
    }
    
    @GetMapping("/getprogram/{streamId}")
    public ResponseEntity<List<ProgramDTO>> getProgramsByStreamId(@PathVariable int streamId) {
        
        List<ProgramDTO> programs = service.getProgramsByStreamId(streamId);

        if (programs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(programs);
    }


}