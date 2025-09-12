package com.application.service;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.dto.ApplicationConfirmationDto;
import com.application.dto.BatchDTO;
import com.application.dto.BatchDetailsDTO;
import com.application.dto.CampusAndZoneDTO;
import com.application.dto.ConcessionDTO;
import com.application.dto.EmployeeDetailsDTO;
import com.application.dto.ExamProgramDTO;
import com.application.dto.LanguageDTO;
import com.application.dto.OrientationDTO;
import com.application.dto.ProgramDTO;
import com.application.dto.SectionDTO;
import com.application.dto.StreamDTO;
import com.application.dto.StudentDetailsDTO;
import com.application.entity.AcademicYear;
import com.application.entity.CmpsOrientation;
import com.application.entity.CmpsOrientationBatchFeeView;
import com.application.entity.CmpsOrientationProgramView;
import com.application.entity.CmpsOrientationStreamView;
import com.application.entity.ConcessionReason;
import com.application.entity.ConcessionType;
import com.application.entity.Employee;
import com.application.entity.ExamProgram;
import com.application.entity.FoodType;
import com.application.entity.Gender;
import com.application.entity.Language;
import com.application.entity.Orientation;
import com.application.entity.OrientationBatch;
import com.application.entity.ParentDetails;
import com.application.entity.ProgramName;
import com.application.entity.Section;
import com.application.entity.StateApp;
import com.application.entity.Stream;
import com.application.entity.StudentAcademicDetails;
import com.application.entity.StudentConcessionType;
import com.application.entity.StudentOrientationDetails;
import com.application.entity.StudentPersonalDetails;
import com.application.entity.StudentRelation;
import com.application.repository.AcademicLanguageRepository;
import com.application.repository.AcademicYearRepository;
import com.application.repository.CmpsOrientationBatchFeeViewRepository;
import com.application.repository.CmpsOrientationProgramViewRepository;
import com.application.repository.CmpsOrientationRepository;
import com.application.repository.CmpsOrientationStreamViewRepository;
import com.application.repository.ConcessionReasonRepository;
import com.application.repository.ConcessionTypeRepository;
import com.application.repository.ExamProgramRepository;
import com.application.repository.FoodTypeRepository;
import com.application.repository.GenderRepository;
import com.application.repository.LanguageRepository;
import com.application.repository.OrientationBatchRepository;
import com.application.repository.OrientationGroupRepository;
import com.application.repository.OrientationRepository;
import com.application.repository.ParentDetailsRepository;
import com.application.repository.PaymentDetailsRepository;
import com.application.repository.ProgramNameRepository;
import com.application.repository.SectionRepository;
import com.application.repository.StateAppRepository;
import com.application.repository.StreamRepository;
import com.application.repository.StudentAcademicDetailsRepository;
import com.application.repository.StudentConcessionTypeRepository;
import com.application.repository.StudentOrientationDetailsRepository;
import com.application.repository.StudentPersonalDetailsRepository;
import com.application.repository.StudentRelationRepository;

@Service
public class ApplicationConfirmationService {

    private final OrientationGroupRepository orientationGroupRepository;

    @Autowired
    private StudentAcademicDetailsRepository academicRepo;
    @Autowired
    private StudentPersonalDetailsRepository personalRepo;
    @Autowired
    private StudentConcessionTypeRepository concessionRepo;
    @Autowired
    private ConcessionTypeRepository concessionTypeRepo;
    @Autowired
    private ConcessionReasonRepository concessionReasonRepo;
    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;
    @Autowired
    private StudentOrientationDetailsRepository studentOrientationDetailsRepository; 
    @Autowired
    private GenderRepository genderRepository;
    @Autowired
    private ParentDetailsRepository parentDetailsRepo;
    @Autowired
    private StudentRelationRepository studentRelationRepo;

    // Dropdown repositories
    @Autowired
    private AcademicYearRepository academicYearRepo;
    @Autowired
    private StreamRepository streamRepo;
    @Autowired
    private ProgramNameRepository programRepo;
    @Autowired
    private ExamProgramRepository examProgramRepo;
    @Autowired
    private OrientationRepository orientationRepository;
    @Autowired
    private OrientationBatchRepository orientationBatchRepository;
    @Autowired
    private SectionRepository sectionRepo;
    @Autowired
    private CmpsOrientationRepository cmpsOrientationRepository;
    @Autowired
    private ProgramNameRepository programNameRepository;
    @Autowired
    private AcademicLanguageRepository academicLanguageRepository;
    @Autowired
    private FoodTypeRepository foodTypeRepository;
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private StateAppRepository stateAppRepository;
    @Autowired
    private CmpsOrientationStreamViewRepository cmpsOrientationStreamViewRepository;
    @Autowired
    private CmpsOrientationProgramViewRepository cmpsOrientationProgramViewRepository;
    @Autowired
    private CmpsOrientationBatchFeeViewRepository cmpsOrientationBatchFeeViewRepository;

    ApplicationConfirmationService(OrientationGroupRepository orientationGroupRepository) {
        this.orientationGroupRepository = orientationGroupRepository;
    }

    public List<AcademicYear> getJoinYears() {
        return academicYearRepo.findAll();
    }

    public List<Stream> getStreams() {
        return streamRepo.findAll();
    }

    public List<ProgramName> getPrograms() {
        return programRepo.findAll();
    }

    public List<ExamProgram> getAllExamPrograms() {
        return examProgramRepo.findAll();
    }

    public List<Orientation> getCourseTracks() {
        return orientationRepository.findAll();
    }

    public List<OrientationBatch> getCourseBatches() {
        return orientationBatchRepository.findAll();
    }

    public List<Section> getSections() {
        return sectionRepo.findAll();
    }

    public List<ConcessionReason> getConcessionReasons() {
        return concessionReasonRepo.findAll();
    }

    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    public List<FoodType> getAllFoodTypes() {
        return foodTypeRepository.findAll();
    }

    public List<ProgramDTO> getProgramsByStreamId(int streamId) {
        // Step 1: Get orientation IDs based on the stream ID
        List<CmpsOrientationStreamView> streamViews = cmpsOrientationStreamViewRepository.findByStreamId(streamId);
        
        List<Integer> orientationIds = streamViews.stream()
                .map(CmpsOrientationStreamView::getOrientationId)
                .distinct() // Add distinct to avoid redundant lookups in the next step
                .collect(Collectors.toList());

        if (orientationIds.isEmpty()) {
            return new ArrayList<>(); // Return an empty list if no orientation is found
        }
        
        // Step 2: Get program details for each orientation ID
        List<CmpsOrientationProgramView> programViews = new ArrayList<>();
        for (Integer orientationId : orientationIds) {
            programViews.addAll(cmpsOrientationProgramViewRepository.findByOrientationId(orientationId));
        }

        // Step 3: Extract unique program IDs and names into DTOs
        return programViews.stream()
                .map(view -> new ProgramDTO(view.getProgramId(), view.getProgramName()))
                .distinct()
                .collect(Collectors.toList());
    }

    public Map<String, Object> getDropdownAcademicYears() {
        int currentYear = Year.now().getValue();
        int nextYear = currentYear + 1;

        List<AcademicYear> years = academicYearRepo.findByYearIn(List.of(currentYear, nextYear));

        AcademicYear defaultYear = years.stream().filter(y -> y.getYear() == nextYear).findFirst().orElse(null);

        Map<String, Object> response = new HashMap<>();
        response.put("default", defaultYear);
        response.put("options", years);

        return response;
    }
    
    public List<OrientationDTO> getOrientationsByCampusId(int campusId) {
        List<CmpsOrientationStreamView> allData = cmpsOrientationStreamViewRepository.findByCmpsId(campusId);

        return allData.stream()
                      .map(data -> new OrientationDTO(data.getOrientationId(), data.getOrientationName()))
                      .distinct()
                      .collect(Collectors.toList());
    }


    public List<StreamDTO> getStreamsByOrientationId(int orientationId) {
        List<CmpsOrientationStreamView> allData = cmpsOrientationStreamViewRepository.findAll();

        return allData.stream()
                      .filter(data -> data.getOrientationId() == orientationId) // ✅ fixed
                      .map(data -> new StreamDTO(data.getStreamId(), data.getStreamName()))
                      .distinct()
                      .collect(Collectors.toList());
    }

    
    
    public List<ProgramDTO> getProgramsByOrientationId(int orientationId) {
        List<CmpsOrientationProgramView> result = cmpsOrientationProgramViewRepository.findByOrientationId(orientationId);
        
        return result.stream()
                     .map(data -> new ProgramDTO(data.getProgramId(), data.getProgramName()))
                     .distinct()
                     .collect(Collectors.toList());
    }

    // Method to get unique exam program IDs and names based on a program ID
    public List<ExamProgramDTO> getExamProgramsByProgramId(int programId) {
        List<CmpsOrientationProgramView> result = cmpsOrientationProgramViewRepository.findByProgramId(programId);

        return result.stream()
                     .map(data -> new ExamProgramDTO(data.getExamProgramId(), data.getExamProgramName()))
                     .distinct()
                     .collect(Collectors.toList());
    }

    
    public List<BatchDTO> getBatchesByOrientationId(int orientationId) {
        return cmpsOrientationBatchFeeViewRepository.findByOrientationId(orientationId).stream()
                         .filter(Objects::nonNull) // Add this line to filter out null elements
                         .map(data -> new BatchDTO(data.getOrientationBatchId(), data.getOrientationBatchName()))
                         .distinct()
                         .collect(Collectors.toList());
    }

    // Get batch details (dates, fee) by batch ID
 public List<BatchDetailsDTO> getBatchDetails(int orientationId, int orientationBatchId) {
        
        List<CmpsOrientationBatchFeeView> batchDetailsList = 
            cmpsOrientationBatchFeeViewRepository.findByOrientationIdAndOrientationBatchId(orientationId, orientationBatchId);
        
        if (batchDetailsList.isEmpty()) {
            return Collections.emptyList();
        }

        return batchDetailsList.stream()
                .filter(Objects::nonNull)
                .map(data -> new BatchDetailsDTO(
                        data.getOrientationStartDate(),
                        data.getOrientationEndDate(),
                        data.getOrientationFee()
                ))
                .collect(Collectors.toList());
    }


    // Get unique section IDs and names by batch name
    public List<SectionDTO> getSectionsByBatchId(int orientationBatchId) {
        return cmpsOrientationBatchFeeViewRepository.findByOrientationBatchId(orientationBatchId).stream()
                .map(data -> new SectionDTO(data.getSectionId(), data.getSectionName()))
                .distinct()
                .collect(Collectors.toList());
    }


    public CampusAndZoneDTO getCampusAndZoneByAdmissionNo(String admissionNo) {
        return academicRepo.findByStudAdmsNo(admissionNo).map(studentDetails -> {
            if (studentDetails.getCampus() != null && studentDetails.getCampus().getZone() != null) {
                CampusAndZoneDTO dto = new CampusAndZoneDTO();
                dto.setCampusId(studentDetails.getCampus().getCampusId());
                dto.setCampusName(studentDetails.getCampus().getCampusName());
                dto.setZoneId(studentDetails.getCampus().getZone().getZoneId());
                dto.setZoneName(studentDetails.getCampus().getZone().getZoneName());
                return dto;
            }
            return null;
        }).orElse(null);
    }
    
    // NOTE: This method had errors and was corrected to use the correct repository and field names.
    public Optional<Float> getCourseFeeByDetails(int cmpsId, int orientationId, int orientationBatchId) {
        Optional<CmpsOrientation> cmpsOrientation = cmpsOrientationRepository
                .findByCmpsIdAndOrientation_OrientationIdAndOrientationBatch_OrientationBatchId(cmpsId, orientationId, orientationBatchId);

        return cmpsOrientation.map(CmpsOrientation::getOrientation_fee);
    }

    public Optional<StudentDetailsDTO> getStudentDetailsByAdmissionNo(String admissionNo) {
        Optional<StudentAcademicDetails> academicDetailsOptional = academicRepo.findByStudAdmsNo(admissionNo);

        if (academicDetailsOptional.isEmpty()) {
            return Optional.empty();
        }

        StudentAcademicDetails academicDetails = academicDetailsOptional.get();
        List<ParentDetails> parentDetailsList = parentDetailsRepo.findByStudentAcademicDetails(academicDetails);

        int admissionNumber;
        List<StateApp> matchingStateApps = List.of();
        try {
            admissionNumber = Integer.parseInt(admissionNo);
            matchingStateApps = stateAppRepository.findAllByAdmissionNoBetweenRange(admissionNumber);
        } catch (NumberFormatException e) {
            // log error if needed
        }

        List<StudentConcessionType> concessions = concessionRepo.findByStudAdmsId(academicDetails.getStud_adms_id());

        StudentDetailsDTO dto = new StudentDetailsDTO();

        dto.setStudentName(academicDetails.getFirst_name());
        dto.setSurname(academicDetails.getLast_name());
        dto.setGender(academicDetails.getGender() != null ? academicDetails.getGender().getGenderName() : "N/A");

        parentDetailsList.forEach(parent -> {
            if (parent.getStudentRelation() != null) {
                if ("Father".equalsIgnoreCase(parent.getStudentRelation().getStudent_relation_type())) {
                    dto.setFathername(parent.getName());
                } else if ("Mother".equalsIgnoreCase(parent.getStudentRelation().getStudent_relation_type())) {
                    dto.setMothername(parent.getName());
                }
            }
        });

        if (!matchingStateApps.isEmpty()) {
            StateApp stateApp = matchingStateApps.get(0); // pick the first match
            dto.setApplicationFee(Float.parseFloat(stateApp.getApp_fee()));
            dto.setConfirmationAmount(stateApp.getAmount());
        } else {
            dto.setApplicationFee(0);
            dto.setConfirmationAmount(0);
        }

        dto.setConcessionAmounts(concessions.stream()
                .map(StudentConcessionType::getConc_amount)
                .collect(Collectors.toList()));

        return Optional.of(dto);
    }


    /**
     * Save or update admission details along with concessions, personal, and course details.
     */
    @Transactional
    public void saveOrUpdateAdmission(ApplicationConfirmationDto dto) {
        StudentAcademicDetails academicDetails = academicRepo.findByStudAdmsNo(dto.getAdmissionNo())
                .orElseThrow(() -> new RuntimeException("Invalid Admission No: " + dto.getAdmissionNo()));

        // Update personal details with food and blood group
        updatePersonalDetails(dto, academicDetails);
        
        // Save or update parent information
        saveOrUpdateParentDetails(dto, academicDetails);

        // Update academic details
        updateAcademicDetails(dto, academicDetails);

        // Save or update concessions
        saveOrUpdateConcessions(dto, academicDetails);

        // Save or update course details
        saveOrUpdateCourseDetails(dto, academicDetails);

        academicRepo.save(academicDetails);
    }

    /**
     * Save or update concessions for 1st, 2nd, and 3rd year without deletion
     */
    private void saveOrUpdateConcessions(ApplicationConfirmationDto dto, StudentAcademicDetails academicDetails) {
        if (dto.getConcessions() == null || dto.getConcessions().isEmpty()) {
            return;
        }

        for (ConcessionDTO conc : dto.getConcessions()) {
            Optional<StudentConcessionType> existingConcession = findConcessionByStudentAndType(
                    academicDetails.getStud_adms_id(), conc.getConcessionTypeId());

            StudentConcessionType entity;

            if (existingConcession.isPresent()) {
                entity = existingConcession.get();
                entity.setConc_amount(conc.getConcessionAmount().floatValue());

                ConcessionReason currentReason = entity.getConcessionReason();
                if (currentReason != null && currentReason.getConc_reason_id() != conc.getReasonId()) {
                    ConcessionReason reason = concessionReasonRepo.findById(conc.getReasonId())
                            .orElseThrow(() -> new RuntimeException("Invalid Concession Reason ID: " + conc.getReasonId()));
                    entity.setConcessionReason(reason);
                } else if (currentReason == null) {
                    ConcessionReason reason = concessionReasonRepo.findById(conc.getReasonId())
                            .orElseThrow(() -> new RuntimeException("Invalid Concession Reason ID: " + conc.getReasonId()));
                    entity.setConcessionReason(reason);
                }
            } else {
                entity = new StudentConcessionType();
                entity.setStudAdmsId(academicDetails.getStud_adms_id());
                AcademicYear year = academicYearRepo.findById(getAcademicYearIdForConcession(conc.getConcessionTypeId()))
                        .orElseThrow(() -> new RuntimeException("Invalid Academic Year ID"));
                entity.setAcademicYear(year);

                entity.setConc_amount(conc.getConcessionAmount().floatValue());

                ConcessionType concessionType = concessionTypeRepo.findById(conc.getConcessionTypeId())
                        .orElseThrow(() -> new RuntimeException("Concession type not found for ID: " + conc.getConcessionTypeId()));

                entity.setConcessionType(concessionType);

                ConcessionReason reason = concessionReasonRepo.findById(conc.getReasonId())
                        .orElseThrow(() -> new RuntimeException("Invalid Concession Reason ID: " + conc.getReasonId()));
                entity.setConcessionReason(reason);
            }

            concessionRepo.save(entity);
        }
    }

    /**
     * Update academic details with form selections
     */
    private void updateAcademicDetails(ApplicationConfirmationDto dto, StudentAcademicDetails academicDetails) {
        // Update personal info in StudentAcademicDetails
        academicDetails.setFirst_name(dto.getFirstName());
        academicDetails.setLast_name(dto.getLastName());

        // Assuming you have a GenderRepository to fetch the Gender entity
        if (dto.getGender() != null) {
            Gender gender = genderRepository.findById(dto.getGender())
                    .orElseThrow(() -> new RuntimeException("Invalid Gender ID: " + dto.getGender()));
            academicDetails.setGender(gender);
        }

        // Update language selections by converting the DTO list to an int array
        if (dto.getLanguages() != null && !dto.getLanguages().isEmpty()) {
            int[] languageIds = dto.getLanguages().stream()
                    .mapToInt(LanguageDTO::getLangId)
                    .toArray();
            academicDetails.setLang_id(languageIds);
        }

        if (dto.getApp_conf_date() != null) {
            academicDetails.setApp_conf_date(dto.getApp_conf_date());
        }
    }

    /**
     * Update student personal details (only food and blood group).
     */
    private void updatePersonalDetails(ApplicationConfirmationDto dto, StudentAcademicDetails academicDetails) {
        Optional<StudentPersonalDetails> personalDetailsOptional = personalRepo.findByStudentAcademicDetails(academicDetails);
        StudentPersonalDetails personalDetails = personalDetailsOptional.orElseGet(StudentPersonalDetails::new);
        personalDetails.setStudentAcademicDetails(academicDetails);

        // Update food type
        if (dto.getFoodType() != null) {
            FoodType foodType = foodTypeRepository.findById(dto.getFoodType())
                    .orElseThrow(() -> new RuntimeException("Invalid Food Type ID: " + dto.getFoodType()));
            personalDetails.setFoodType(foodType);
        }
        
        personalRepo.save(personalDetails);
    }
    
    /**
     * Save or update parent details in the ParentDetails table.
     */
    private void saveOrUpdateParentDetails(ApplicationConfirmationDto dto, StudentAcademicDetails academicDetails) {
        StudentRelation fatherRelation = studentRelationRepo.findByStudentRelationType("father")
                .orElseThrow(() -> new RuntimeException("Student relation 'Father' not found."));

        StudentRelation motherRelation = studentRelationRepo.findByStudentRelationType("mother")
                .orElseThrow(() -> new RuntimeException("Student relation 'Mother' not found."));
                
        String parentName = dto.getParentName();
        String fatherName = null;
        String motherName = null;
        if (parentName != null) {
            String[] names = parentName.split(" ", 2);
            fatherName = names[0];
            if (names.length > 1) {
                motherName = names[1];
            }
        }
        
        List<ParentDetails> existingParents = parentDetailsRepo.findByStudentAcademicDetails(academicDetails);

        // Update or create Father's details
        if (fatherName != null) {
            ParentDetails father = existingParents.stream()
                .filter(p -> p.getStudentRelation().getStudent_relation_id() == fatherRelation.getStudent_relation_id())
                .findFirst()
                .orElseGet(ParentDetails::new);
            
            father.setName(fatherName);
            father.setStudentRelation(fatherRelation);
            father.setStudentAcademicDetails(academicDetails);
            parentDetailsRepo.save(father);
        }
        
        // Update or create Mother's details
        if (motherName != null) {
            ParentDetails mother = existingParents.stream()
                .filter(p -> p.getStudentRelation().getStudent_relation_id() == motherRelation.getStudent_relation_id())
                .findFirst()
                .orElseGet(ParentDetails::new);
                
            mother.setName(motherName);
            mother.setStudentRelation(motherRelation);
            mother.setStudentAcademicDetails(academicDetails);
            parentDetailsRepo.save(mother);
        }
    }
    
    /**
     * Save or update course details
     */
    private void saveOrUpdateCourseDetails(ApplicationConfirmationDto dto, StudentAcademicDetails academicDetails) {
        // Find existing StudentOrientationDetails by the associated StudentAcademicDetails
        Optional<StudentOrientationDetails> existingDetails = studentOrientationDetailsRepository.findByStudentAcademicDetails(academicDetails);
        StudentOrientationDetails courseDetails;

        if (existingDetails.isPresent()) {
            // If the record exists, retrieve it for an update
            courseDetails = existingDetails.get();
        } else {
            // If no record exists, create a new one
            courseDetails = new StudentOrientationDetails();
            courseDetails.setStudentAcademicDetails(academicDetails);
        }

        // Set the current date for the course entry
        courseDetails.setOrientation_date(new Date());

        // Update fields based on DTO values, handling null checks
        if (dto.getStreamId() != null) {
            courseDetails.setStream(streamRepo.findById(dto.getStreamId())
                    .orElseThrow(() -> new RuntimeException("Invalid Stream ID: " + dto.getStreamId())));
        }
        
        if (dto.getProgramId() != null) {
            courseDetails.setProgramName(programRepo.findById(dto.getProgramId())
                    .orElseThrow(() -> new RuntimeException("Invalid Program ID: " + dto.getProgramId())));
        }
        
        if (dto.getExamProgramId() != null) {
            courseDetails.setExamProgram(examProgramRepo.findById(dto.getExamProgramId())
                    .orElseThrow(() -> new RuntimeException("Invalid Exam Program ID: " + dto.getExamProgramId())));
        }
        
        if (dto.getOrientationId() != null) {
            courseDetails.setOrientation(orientationRepository.findById(dto.getOrientationId())
                    .orElseThrow(() -> new RuntimeException("Invalid Orientation ID: " + dto.getOrientationId())));
        }
        
        // Corrected code: use dto.getBatchId() and setCourseBatch()
        if (dto.getBatchId() != null) {
            courseDetails.setOrientationBatch(orientationBatchRepository.findById(dto.getBatchId())
                    .orElseThrow(() -> new RuntimeException("Invalid Batch ID: " + dto.getBatchId())));
        }
        
        if (dto.getSectionId() != null) {
            courseDetails.setSection(sectionRepo.findById(dto.getSectionId())
                    .orElseThrow(() -> new RuntimeException("Invalid Section ID: " + dto.getSectionId())));
        }
        
        // Find and set the CmpsOrientation entity if all required fields are present
        if (dto.getOrientationId() != null && dto.getBatchId() != null && academicDetails.getCampus() != null) {
            int cmpsId = academicDetails.getCampus().getCampusId();
            Optional<CmpsOrientation> cmpsOrientation = cmpsOrientationRepository
                    .findByCmpsIdAndOrientation_OrientationIdAndOrientationBatch_OrientationBatchId(
                            cmpsId, dto.getOrientationId(), dto.getBatchId());

            cmpsOrientation.ifPresent(courseDetails::setCmpsOrientation);
        }

        // Save the entity. This will either insert a new record or update the existing one.
        studentOrientationDetailsRepository.save(courseDetails);
    }

    // Helper method to get academic year ID based on concession type ID
    private Integer getAcademicYearIdForConcession(Integer concessionTypeId) {
        return concessionTypeId;
    }

    // Alternative method if the repository query still has issues
    private Optional<StudentConcessionType> findConcessionByStudentAndType(Integer studAdmsId, Integer concessionTypeId) {
        return concessionRepo.findByStudAdmsId(studAdmsId).stream()
                .filter(conc -> conc.getConcessionType() != null && Integer.valueOf(conc.getConcessionType().getConcTypeId()).equals(concessionTypeId))
                .findFirst();
    }

    public Optional<EmployeeDetailsDTO> getEmployeeDetailsByAdmissionNo(String admissionNo) {
        Optional<StudentAcademicDetails> academicDetailsOptional = academicRepo.findByStudAdmsNo(admissionNo);

        if (academicDetailsOptional.isEmpty()) {
            return Optional.empty();
        }

        StudentAcademicDetails academicDetails = academicDetailsOptional.get();

        Employee employee = academicDetails.getEmployee();

        if (employee == null) {
            return Optional.empty();
        }

        EmployeeDetailsDTO dto = new EmployeeDetailsDTO();
        dto.setEmployeeName(employee.getFirst_name() + " " + employee.getLast_name());
        dto.setEmployeeMobileNo(employee.getPrimary_mobile_no());

        if (employee.getCampus() != null) {
            dto.setCampusName(employee.getCampus().getCampusName());
        }

        return Optional.of(dto);
    }
}