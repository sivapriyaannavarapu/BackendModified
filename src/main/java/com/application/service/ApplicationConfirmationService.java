//package com.application.service;
//
//import java.time.Year;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.application.dto.ApplicationConfirmationDto;
//import com.application.dto.BatchDatesResponse;
//import com.application.dto.CampusAndZoneDTO;
//import com.application.dto.ConcessionDTO;
//import com.application.dto.EmployeeDetailsDTO;
//import com.application.dto.LanguageDTO;
//import com.application.dto.StudentDetailsDTO;
//import com.application.entity.AcademicYear;
//import com.application.entity.BloodGroup;
//import com.application.entity.CmpsCourseTrack;
//import com.application.entity.ConcessionReason;
//import com.application.entity.ConcessionType;
//import com.application.entity.CourseBatch;
//import com.application.entity.CourseTrack;
//import com.application.entity.Employee;
//import com.application.entity.ExamProgram;
//import com.application.entity.FoodType;
//import com.application.entity.Gender;
//import com.application.entity.Language;
//import com.application.entity.PaymentDetails;
//import com.application.entity.ProgramName;
//import com.application.entity.Section;
//import com.application.entity.Stream;
//import com.application.entity.StudentAcademicDetails;
//import com.application.entity.StudentConcessionType;
//import com.application.entity.StudentCourseDetails;
//import com.application.entity.StudentPersonalDetails;
//import com.application.repository.AcademicLanguageRepository;
//import com.application.repository.AcademicYearRepository;
//import com.application.repository.BloodGroupRepository;
//import com.application.repository.CmpsCourseTrackRepository;
//import com.application.repository.ConcessionReasonRepository;
//import com.application.repository.ConcessionTypeRepository;
//import com.application.repository.CourseBatchRepository;
//import com.application.repository.CourseGroupRepository;
//import com.application.repository.CourseTrackRepository;
//import com.application.repository.ExamProgramRepository;
//import com.application.repository.FoodTypeRepository;
//import com.application.repository.GenderRepository;
//import com.application.repository.LanguageRepository;
//import com.application.repository.ParentDetailsRepository;
//import com.application.repository.PaymentDetailsRepository;
//import com.application.repository.ProgramNameRepository;
//import com.application.repository.SectionRepository;
//import com.application.repository.StreamRepository;
//import com.application.repository.StudentAcademicDetailsRepository;
//import com.application.repository.StudentConcessionTypeRepository;
//import com.application.repository.StudentCourseDetailsRepository;
//import com.application.repository.StudentPersonalDetailsRepository;
//import com.application.repository.StudentRelationRepository;
//
//@Service
//public class ApplicationConfirmationService {
//
//    private final CourseGroupRepository courseGroupRepository;
//
//    @Autowired
//    private StudentAcademicDetailsRepository academicRepo;
//    @Autowired
//    private StudentPersonalDetailsRepository personalRepo;
//    @Autowired
//    private StudentConcessionTypeRepository concessionRepo;
//    @Autowired
//    private ConcessionTypeRepository concessionTypeRepo;
//    @Autowired
//    private ConcessionReasonRepository concessionReasonRepo;
//    @Autowired
//    private PaymentDetailsRepository paymentDetailsRepository;
//    @Autowired
//    private StudentCourseDetailsRepository studentCourseDetailsRepository; 
//    @Autowired
//    private GenderRepository genderRepository;
//    @Autowired
//    private ParentDetailsRepository parentDetailsRepo;
//    @Autowired
//    private StudentRelationRepository studentRelationRepo;
//
//    // Dropdown repositories
//    @Autowired
//    private AcademicYearRepository academicYearRepo;
//    @Autowired
//    private StreamRepository streamRepo;
//    @Autowired
//    private ProgramNameRepository programRepo;
//    @Autowired
//    private ExamProgramRepository examProgramRepo;
//    @Autowired
//    private CourseTrackRepository courseTrackRepo;
//    @Autowired
//    private CourseBatchRepository courseBatchRepo;
//    @Autowired
//    private SectionRepository sectionRepo;
//    @Autowired
//    private CmpsCourseTrackRepository cmpsCourseTrackRepo;
//    @Autowired
//    private ProgramNameRepository programNameRepository;
//    @Autowired
//    private AcademicLanguageRepository academicLanguageRepository;
//    @Autowired
//    private FoodTypeRepository foodTypeRepository;
//    @Autowired
//    private BloodGroupRepository bloodGroupRepository;
//    @Autowired
//    private LanguageRepository languageRepository;
//
//    ApplicationConfirmationService(CourseGroupRepository courseGroupRepository) {
//        this.courseGroupRepository = courseGroupRepository;
//    }
//
//    public List<AcademicYear> getJoinYears() {
//        return academicYearRepo.findAll();
//    }
//
//    public List<Stream> getStreams() {
//        return streamRepo.findAll();
//    }
//
//    public List<ProgramName> getPrograms() {
//        return programRepo.findAll();
//    }
//
//    public List<ExamProgram> getAllExamPrograms() {
//        return examProgramRepo.findAll();
//    }
//
//    public List<CourseTrack> getCourseTracks() {
//        return courseTrackRepo.findAll();
//    }
//
//    public List<CourseBatch> getCourseBatches() {
//        return courseBatchRepo.findAll();
//    }
//
//    public List<Section> getSections() {
//        return sectionRepo.findAll();
//    }
//
//    public List<ProgramName> getProgramsByStream(int streamId) {
//        return programNameRepository.findByStreamId(streamId);
//    }
//
//    public List<Stream> getStreamsByCourseTrackId(int courseTrackId) {
//        return streamRepo.findByCourseTrack_CourseTrackId(courseTrackId);
//    }
//
//    public List<CourseBatch> getCourseBatchesByCourseTrackId(int courseTrackId) {
//        return cmpsCourseTrackRepo.findCourseBatchesByCourseTrackId(courseTrackId);
//    }
//
//    public List<ConcessionReason> getConcessionReasons() {
//        return concessionReasonRepo.findAll();
//    }
//
//    public List<ExamProgram> getExamProgramsByProgramId(int programId) {
//        return examProgramRepo.findByProgramName_programId(programId);
//    }
//
//    public List<Language> getAllLanguages() {
//        return languageRepository.findAll();
//    }
//
//    public List<FoodType> getAllFoodTypes() {
//        return foodTypeRepository.findAll();
//    }
//
//    public List<BloodGroup> getAllBloodGroups() {
//        return bloodGroupRepository.findAll();
//    }
//
//    public Map<String, Object> getDropdownAcademicYears() {
//        int currentYear = Year.now().getValue();
//        int nextYear = currentYear + 1;
//
//        List<AcademicYear> years = academicYearRepo.findByYearIn(List.of(currentYear, nextYear));
//
//        AcademicYear defaultYear = years.stream().filter(y -> y.getYear() == nextYear).findFirst().orElse(null);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("default", defaultYear);
//        response.put("options", years);
//
//        return response;
//    }
//
//    // ---------------- Auto Populate Methods ---------------- //
//    public BatchDatesResponse getCourseBatchDetails(Integer batchId) {
//        CourseBatch batch = courseBatchRepo.findById(batchId)
//                .orElseThrow(() -> new RuntimeException("Invalid Batch ID: " + batchId));
//
//        return new BatchDatesResponse(batch.getStart_date(), batch.getEnd_date());
//    }
//
//    public CampusAndZoneDTO getCampusAndZoneByAdmissionNo(String admissionNo) {
//        return academicRepo.findByStudAdmsNo(admissionNo).map(studentDetails -> {
//            if (studentDetails.getCampus() != null && studentDetails.getCampus().getZone() != null) {
//                CampusAndZoneDTO dto = new CampusAndZoneDTO();
//                dto.setCampusId(studentDetails.getCampus().getCampusId());
//                dto.setCampusName(studentDetails.getCampus().getCampusName());
//                dto.setZoneId(studentDetails.getCampus().getZone().getZoneId());
//                dto.setZoneName(studentDetails.getCampus().getZone().getZoneName());
//                return dto;
//            }
//            return null;
//        }).orElse(null);
//    }
//
//    public Optional<Float> getCourseFeeByDetails(int cmpsId, int courseTrackId, int courseBatchId) {
//        Optional<CmpsCourseTrack> cmpsCourseTrack = cmpsCourseTrackRepo
//                .findByCmpsIdAndCourseTrack_CourseTrackIdAndCourseBatch_CourseBatchId(cmpsId, courseTrackId,
//                        courseBatchId);
//
//        return cmpsCourseTrack.map(CmpsCourseTrack::getCourse_fee);
//    }
//
//    public Optional<StudentDetailsDTO> getStudentDetailsByAdmissionNo(String admissionNo) {
//        Optional<StudentAcademicDetails> academicDetailsOptional = academicRepo.findByStudAdmsNo(admissionNo);
//
//        if (academicDetailsOptional.isEmpty()) {
//            return Optional.empty();
//        }
//
//        StudentAcademicDetails academicDetails = academicDetailsOptional.get();
//        int studAdmsId = academicDetails.getStud_adms_id();
//
//        Optional<StudentPersonalDetails> personalDetailsOptional = personalRepo.findByStudentAcademicDetails(academicDetails);
//        Optional<PaymentDetails> paymentDetailsOptional = paymentDetailsRepository.findByStudentAcademicDetails(academicDetails);
//        List<StudentConcessionType> concessions = concessionRepo.findByStudAdmsId(studAdmsId);
//
//        StudentDetailsDTO dto = new StudentDetailsDTO();
//
//        dto.setStudentName(academicDetails.getFirst_name());
//        dto.setSurname(academicDetails.getLast_name());
//        dto.setGender(academicDetails.getGender() != null ? academicDetails.getGender().getGenderName() : "N/A");
//
//        personalDetailsOptional.ifPresent(personal -> {
//        	 dto.setFathername(personal.getFather_name());
//             dto.setMothername(personal.getMother_name());
//        });
//
//        paymentDetailsOptional.ifPresent(payment -> {
//            dto.setApplicationFee(payment.getApp_fee());
//            dto.setConfirmationAmount(payment.getPaid_amount());
//        });
//
//        dto.setConcessionAmounts(concessions.stream().map(StudentConcessionType::getConc_amount)
//                .collect(Collectors.toList()));
//
//        return Optional.of(dto);
//    }
//
//    /**
//     * Save or update admission details along with concessions, personal, and course details.
//     */
//    @Transactional
//    public void saveOrUpdateAdmission(ApplicationConfirmationDto dto) {
//        StudentAcademicDetails academicDetails = academicRepo.findByStudAdmsNo(dto.getAdmissionNo())
//                .orElseThrow(() -> new RuntimeException("Invalid Admission No: " + dto.getAdmissionNo()));
//
//        // Update personal details first
//        updatePersonalDetails(dto, academicDetails);
//
//        // Update academic details
//        updateAcademicDetails(dto, academicDetails);
//
//        // Save or update concessions
//        saveOrUpdateConcessions(dto, academicDetails);
//
//        // Save or update course details
//        saveOrUpdateCourseDetails(dto, academicDetails);
//
//        academicRepo.save(academicDetails);
//    }
//
//    /**
//     * Save or update concessions for 1st, 2nd, and 3rd year without deletion
//     */
//    private void saveOrUpdateConcessions(ApplicationConfirmationDto dto, StudentAcademicDetails academicDetails) {
//        if (dto.getConcessions() == null || dto.getConcessions().isEmpty()) {
//            return;
//        }
//
//        for (ConcessionDTO conc : dto.getConcessions()) {
//            Optional<StudentConcessionType> existingConcession = findConcessionByStudentAndType(
//                    academicDetails.getStud_adms_id(), conc.getConcessionTypeId());
//
//            StudentConcessionType entity;
//
//            if (existingConcession.isPresent()) {
//                entity = existingConcession.get();
//                entity.setConc_amount(conc.getConcessionAmount().floatValue());
//
//                ConcessionReason currentReason = entity.getConcessionReason();
//                if (currentReason != null && currentReason.getConc_reason_id() != conc.getReasonId()) {
//                    ConcessionReason reason = concessionReasonRepo.findById(conc.getReasonId())
//                            .orElseThrow(() -> new RuntimeException("Invalid Concession Reason ID: " + conc.getReasonId()));
//                    entity.setConcessionReason(reason);
//                } else if (currentReason == null) {
//                    ConcessionReason reason = concessionReasonRepo.findById(conc.getReasonId())
//                            .orElseThrow(() -> new RuntimeException("Invalid Concession Reason ID: " + conc.getReasonId()));
//                    entity.setConcessionReason(reason);
//                }
//            } else {
//                entity = new StudentConcessionType();
//                entity.setStudAdmsId(academicDetails.getStud_adms_id());
//                AcademicYear year = academicYearRepo.findById(getAcademicYearIdForConcession(conc.getConcessionTypeId()))
//                        .orElseThrow(() -> new RuntimeException("Invalid Academic Year ID"));
//                entity.setAcademicYear(year);
//
//                entity.setConc_amount(conc.getConcessionAmount().floatValue());
//
//                ConcessionType concessionType = concessionTypeRepo.findById(conc.getConcessionTypeId())
//                        .orElseThrow(() -> new RuntimeException("Concession type not found for ID: " + conc.getConcessionTypeId()));
//
//                entity.setConcessionType(concessionType);
//
//                ConcessionReason reason = concessionReasonRepo.findById(conc.getReasonId())
//                        .orElseThrow(() -> new RuntimeException("Invalid Concession Reason ID: " + conc.getReasonId()));
//                entity.setConcessionReason(reason);
//            }
//
//            concessionRepo.save(entity);
//        }
//    }
//
//    /**
//     * Update academic details with form selections
//     */
//    private void updateAcademicDetails(ApplicationConfirmationDto dto, StudentAcademicDetails academicDetails) {
//        // Update personal info in StudentAcademicDetails
//        academicDetails.setFirst_name(dto.getFirstName());
//        academicDetails.setLast_name(dto.getLastName());
//
//        // Assuming you have a GenderRepository to fetch the Gender entity
//        if (dto.getGender() != null) {
//            Gender gender = genderRepository.findById(dto.getGender())
//                    .orElseThrow(() -> new RuntimeException("Invalid Gender ID: " + dto.getGender()));
//            academicDetails.setGender(gender);
//        }
//
//        // Update language selections by converting the DTO list to an int array
//     // Update language selections by converting the DTO list to an int array
//        if (dto.getLanguages() != null && !dto.getLanguages().isEmpty()) {
//            int[] languageIds = dto.getLanguages().stream()
//                    .mapToInt(LanguageDTO::getLangId) // This is where the ID is extracted
//                    .toArray();
//            academicDetails.setLang_id(languageIds);
//        }
//
//        if (dto.getApp_conf_date() != null) {
//            academicDetails.setApp_conf_date(dto.getApp_conf_date());
//        }
//    }
//
//    /**
//     * Update student personal details.
//     */
//    private void updatePersonalDetails(ApplicationConfirmationDto dto, StudentAcademicDetails academicDetails) {
//        Optional<StudentPersonalDetails> personalDetailsOptional = personalRepo.findByStudentAcademicDetails(academicDetails);
//        StudentPersonalDetails personalDetails = personalDetailsOptional.orElseGet(StudentPersonalDetails::new);
//        personalDetails.setStudentAcademicDetails(academicDetails);
//
//        // Assuming parentName in DTO is a single string like "Father Mother"
//        String parentName = dto.getParentName();
//        if (parentName != null) {
//            String[] names = parentName.split(" ", 2); // Split only once
//            personalDetails.setFather_name(names[0]);
//            if (names.length > 1) {
//                personalDetails.setMother_name(names[1]);
//            }
//        }
//
//        // Update food type
//        if (dto.getFoodType() != null) {
//            FoodType foodType = foodTypeRepository.findById(dto.getFoodType())
//                    .orElseThrow(() -> new RuntimeException("Invalid Food Type ID: " + dto.getFoodType()));
//            personalDetails.setFoodType(foodType);
//        }
//
//        // Update blood group
//        if (dto.getBloodGroup() != null) {
//            BloodGroup bloodGroup = bloodGroupRepository.findById(dto.getBloodGroup())
//                    .orElseThrow(() -> new RuntimeException("Invalid Blood Group ID: " + dto.getBloodGroup()));
//            personalDetails.setBloodGroup(bloodGroup);
//        }
//
//        personalRepo.save(personalDetails);
//    }
//
//    /**
//     * Save or update course details
//     */
//    private void saveOrUpdateCourseDetails(ApplicationConfirmationDto dto, StudentAcademicDetails academicDetails) {
//        Optional<StudentCourseDetails> existingCourseDetails = studentCourseDetailsRepository.findByStudentAcademicDetails(academicDetails);
//        StudentCourseDetails courseDetails = existingCourseDetails.orElseGet(StudentCourseDetails::new);
//
//        courseDetails.setStudentAcademicDetails(academicDetails);
//        courseDetails.setCourse_date(new Date());
//
//        if (dto.getStreamId() != null) {
//            courseDetails.setStream(streamRepo.findById(dto.getStreamId())
//                    .orElseThrow(() -> new RuntimeException("Invalid Stream ID: " + dto.getStreamId())));
//        }
//        if (dto.getProgramId() != null) {
//            courseDetails.setProgramName(programRepo.findById(dto.getProgramId())
//                    .orElseThrow(() -> new RuntimeException("Invalid Program ID: " + dto.getProgramId())));
//        }
//        if (dto.getExamProgramId() != null) {
//            courseDetails.setExamProgram(examProgramRepo.findById(dto.getExamProgramId())
//                    .orElseThrow(() -> new RuntimeException("Invalid Exam Program ID: " + dto.getExamProgramId())));
//        }
//        if (dto.getCourseTrackId() != null) {
//            courseDetails.setCourseTrack(courseTrackRepo.findById(dto.getCourseTrackId())
//                    .orElseThrow(() -> new RuntimeException("Invalid Course Track ID: " + dto.getCourseTrackId())));
//        }
//        if (dto.getBatchId() != null) {
//            courseDetails.setCourseBatch(courseBatchRepo.findById(dto.getBatchId())
//                    .orElseThrow(() -> new RuntimeException("Invalid Batch ID: " + dto.getBatchId())));
//        }
//        if (dto.getSectionId() != null) {
//            courseDetails.setSection(sectionRepo.findById(dto.getSectionId())
//                    .orElseThrow(() -> new RuntimeException("Invalid Section ID: " + dto.getSectionId())));
//        }
//
//        studentCourseDetailsRepository.save(courseDetails);
//    }
//
//    // Helper method to get academic year ID based on concession type ID
//    private Integer getAcademicYearIdForConcession(Integer concessionTypeId) {
//        return concessionTypeId;
//    }
//
//    // Alternative method if the repository query still has issues
//    private Optional<StudentConcessionType> findConcessionByStudentAndType(Integer studAdmsId, Integer concessionTypeId) {
//        return concessionRepo.findByStudAdmsId(studAdmsId).stream()
//                .filter(conc -> conc.getConcessionType() != null && Integer.valueOf(conc.getConcessionType().getConcTypeId()).equals(concessionTypeId))
//                .findFirst();
//    }
//
//    public Optional<EmployeeDetailsDTO> getEmployeeDetailsByAdmissionNo(String admissionNo) {
//        Optional<StudentAcademicDetails> academicDetailsOptional = academicRepo.findByStudAdmsNo(admissionNo);
//
//        if (academicDetailsOptional.isEmpty()) {
//            return Optional.empty();
//        }
//
//        StudentAcademicDetails academicDetails = academicDetailsOptional.get();
//
//        Employee employee = academicDetails.getEmployee();
//
//        if (employee == null) {
//            return Optional.empty();
//        }
//
//        EmployeeDetailsDTO dto = new EmployeeDetailsDTO();
//        dto.setEmployeeName(employee.getFirst_name() + " " + employee.getLast_name());
//        dto.setEmployeeMobileNo(employee.getPrimary_mobile_no());
//
//        if (employee.getCampus() != null) {
//            dto.setCampusName(employee.getCampus().getCampusName());
//        }
//
//        return Optional.of(dto);
//    }
//}

package com.application.service;

import java.util.stream.Collectors;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.dto.ApplicationConfirmationDto;
import com.application.dto.BatchDatesResponse;
import com.application.dto.CampusAndZoneDTO;
import com.application.dto.ConcessionDTO;
import com.application.dto.EmployeeDetailsDTO;
import com.application.dto.LanguageDTO;
import com.application.dto.StudentDetailsDTO;
import com.application.entity.AcademicYear;
import com.application.entity.BloodGroup;
import com.application.entity.CmpsCourseTrack;
import com.application.entity.ConcessionReason;
import com.application.entity.ConcessionType;
import com.application.entity.CourseBatch;
import com.application.entity.CourseTrack;
import com.application.entity.Employee;
import com.application.entity.ExamProgram;
import com.application.entity.FoodType;
import com.application.entity.Language;
import com.application.entity.ParentDetails;
import com.application.entity.PaymentDetails;
import com.application.entity.ProgramName;
import com.application.entity.Section;
import com.application.entity.Stream;
import com.application.entity.StudentAcademicDetails;
import com.application.entity.StudentConcessionType;
import com.application.entity.StudentPersonalDetails;
import com.application.entity.StudentCourseDetails;
import com.application.entity.Gender;
import com.application.entity.StudentRelation;
import com.application.repository.AcademicLanguageRepository;
import com.application.repository.AcademicYearRepository;
import com.application.repository.BloodGroupRepository;
import com.application.repository.CmpsCourseTrackRepository;
import com.application.repository.ConcessionReasonRepository;
import com.application.repository.ConcessionTypeRepository;
import com.application.repository.CourseBatchRepository;
import com.application.repository.CourseGroupRepository;
import com.application.repository.CourseTrackRepository;
import com.application.repository.ExamProgramRepository;
import com.application.repository.FoodTypeRepository;
import com.application.repository.LanguageRepository;
import com.application.repository.ParentDetailsRepository;
import com.application.repository.PaymentDetailsRepository;
import com.application.repository.ProgramNameRepository;
import com.application.repository.SectionRepository;
import com.application.repository.StreamRepository;
import com.application.repository.StudentAcademicDetailsRepository;
import com.application.repository.StudentConcessionTypeRepository;
import com.application.repository.StudentPersonalDetailsRepository;
import com.application.repository.StudentCourseDetailsRepository;
import com.application.repository.GenderRepository;
import com.application.repository.StudentRelationRepository;

@Service
public class ApplicationConfirmationService {

    private final CourseGroupRepository courseGroupRepository;

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
    private StudentCourseDetailsRepository studentCourseDetailsRepository; 
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
    private CourseTrackRepository courseTrackRepo;
    @Autowired
    private CourseBatchRepository courseBatchRepo;
    @Autowired
    private SectionRepository sectionRepo;
    @Autowired
    private CmpsCourseTrackRepository cmpsCourseTrackRepo;
    @Autowired
    private ProgramNameRepository programNameRepository;
    @Autowired
    private AcademicLanguageRepository academicLanguageRepository;
    @Autowired
    private FoodTypeRepository foodTypeRepository;
    @Autowired
    private BloodGroupRepository bloodGroupRepository;
    @Autowired
    private LanguageRepository languageRepository;

    ApplicationConfirmationService(CourseGroupRepository courseGroupRepository) {
        this.courseGroupRepository = courseGroupRepository;
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

    public List<CourseTrack> getCourseTracks() {
        return courseTrackRepo.findAll();
    }

    public List<CourseBatch> getCourseBatches() {
        return courseBatchRepo.findAll();
    }

    public List<Section> getSections() {
        return sectionRepo.findAll();
    }

    public List<ProgramName> getProgramsByStream(int streamId) {
        return programNameRepository.findByStreamId(streamId);
    }

    public List<Stream> getStreamsByCourseTrackId(int courseTrackId) {
        return streamRepo.findByCourseTrack_CourseTrackId(courseTrackId);
    }

    public List<CourseBatch> getCourseBatchesByCourseTrackId(int courseTrackId) {
        return cmpsCourseTrackRepo.findCourseBatchesByCourseTrackId(courseTrackId);
    }

    public List<ConcessionReason> getConcessionReasons() {
        return concessionReasonRepo.findAll();
    }

    public List<ExamProgram> getExamProgramsByProgramId(int programId) {
        return examProgramRepo.findByProgramName_programId(programId);
    }

    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    public List<FoodType> getAllFoodTypes() {
        return foodTypeRepository.findAll();
    }

    public List<BloodGroup> getAllBloodGroups() {
        return bloodGroupRepository.findAll();
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

    // ---------------- Auto Populate Methods ---------------- //
    public BatchDatesResponse getCourseBatchDetails(Integer batchId) {
        CourseBatch batch = courseBatchRepo.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Invalid Batch ID: " + batchId));

        return new BatchDatesResponse(batch.getStart_date(), batch.getEnd_date());
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

    public Optional<Float> getCourseFeeByDetails(int cmpsId, int courseTrackId, int courseBatchId) {
        Optional<CmpsCourseTrack> cmpsCourseTrack = cmpsCourseTrackRepo
                .findByCmpsIdAndCourseTrack_CourseTrackIdAndCourseBatch_CourseBatchId(cmpsId, courseTrackId,
                        courseBatchId);

        return cmpsCourseTrack.map(CmpsCourseTrack::getCourse_fee);
    }

    public Optional<StudentDetailsDTO> getStudentDetailsByAdmissionNo(String admissionNo) {
        Optional<StudentAcademicDetails> academicDetailsOptional = academicRepo.findByStudAdmsNo(admissionNo);

        if (academicDetailsOptional.isEmpty()) {
            return Optional.empty();
        }

        StudentAcademicDetails academicDetails = academicDetailsOptional.get();
        
        List<ParentDetails> parentDetailsList = parentDetailsRepo.findByStudentAcademicDetails(academicDetails);
        
        Optional<PaymentDetails> paymentDetailsOptional = paymentDetailsRepository.findByStudentAcademicDetails(academicDetails);
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

        paymentDetailsOptional.ifPresent(payment -> {
            dto.setApplicationFee(payment.getApp_fee());
            dto.setConfirmationAmount(payment.getPaid_amount());
        });

        dto.setConcessionAmounts(concessions.stream().map(StudentConcessionType::getConc_amount)
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

        // Update blood group
        if (dto.getBloodGroup() != null) {
            BloodGroup bloodGroup = bloodGroupRepository.findById(dto.getBloodGroup())
                    .orElseThrow(() -> new RuntimeException("Invalid Blood Group ID: " + dto.getBloodGroup()));
            personalDetails.setBloodGroup(bloodGroup);
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
        Optional<StudentCourseDetails> existingCourseDetails = studentCourseDetailsRepository.findByStudentAcademicDetails(academicDetails);
        StudentCourseDetails courseDetails = existingCourseDetails.orElseGet(StudentCourseDetails::new);

        courseDetails.setStudentAcademicDetails(academicDetails);
        courseDetails.setCourse_date(new Date());

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
        if (dto.getCourseTrackId() != null) {
            courseDetails.setCourseTrack(courseTrackRepo.findById(dto.getCourseTrackId())
                    .orElseThrow(() -> new RuntimeException("Invalid Course Track ID: " + dto.getCourseTrackId())));
        }
        if (dto.getBatchId() != null) {
            courseDetails.setCourseBatch(courseBatchRepo.findById(dto.getBatchId())
                    .orElseThrow(() -> new RuntimeException("Invalid Batch ID: " + dto.getBatchId())));
        }
        if (dto.getSectionId() != null) {
            courseDetails.setSection(sectionRepo.findById(dto.getSectionId())
                    .orElseThrow(() -> new RuntimeException("Invalid Section ID: " + dto.getSectionId())));
        }
        
        int cmpsId = academicDetails.getCampus().getCampusId();
        Optional<CmpsCourseTrack> cmpsCourseTrack = cmpsCourseTrackRepo
                .findByCmpsIdAndCourseTrack_CourseTrackIdAndCourseBatch_CourseBatchId(
                        cmpsId, dto.getCourseTrackId(), dto.getBatchId());

        cmpsCourseTrack.ifPresent(courseDetails::setCmpsCourseTrack);

        studentCourseDetailsRepository.save(courseDetails);
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