package com.application.service;

import com.application.dto.*;
import com.application.entity.*;
import com.application.repository.*;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentAdmissionService {

    private static final Logger logger = LoggerFactory.getLogger(StudentAdmissionService.class);

    // region Repositories
    @Autowired private StudentAcademicDetailsRepository academicDetailsRepo;
    @Autowired private StudentPersonalDetailsRepository personalDetailsRepo;
    @Autowired private StudentAddressRepository addressRepo;
    @Autowired private SiblingRepository siblingRepo;
    @Autowired private AdmissionTypeRepository admissionTypeRepo;
    @Autowired private StudentTypeRepository studentTypeRepo;
    @Autowired private StudyTypeRepository studyTypeRepo;
    @Autowired private GenderRepository genderRepo;
    @Autowired private CampusRepository campusRepo;
    @Autowired private StateRepository stateRepo;
    @Autowired private DistrictRepository districtRepo;
    @Autowired private CampusSchoolTypeRepository schoolTypeRepo;
    @Autowired private QuotaRepository quotaRepo;
    @Autowired private StudentRelationRepository relationTypeRepo;
    @Autowired private StudentClassRepository classRepo;
    @Autowired private OrientationRepository orientationRepo;
    @Autowired private MandalRepository mandalRepo;
    @Autowired private CityRepository cityRepo;
    @Autowired private AcademicYearRepository academicYearRepository;
    @Autowired private SectionRepository sectionRepo;
    @Autowired private StatusRepository statusRepo;
    @Autowired private EmployeeRepository employeeRepo;
    @Autowired private PaymentDetailsRepository paymentDetailsRepo;
    @Autowired private PaymentModeRepository paymentModeRepo;
    @Autowired private StudentConcessionTypeRepository studentConcessionTypeRepo;
    @Autowired private ConcessionTypeRepository concessionTypeRepo;
    @Autowired private ConcessionReasonRepository concessionReasonRepo;
    @Autowired private StudentApplicationTransactionRepository appTransactionRepo;
    @Autowired private OrganizationRepository orgRepo;
    @Autowired private OrgBankRepository orgBankRepo;
    @Autowired private OrgBankBranchRepository orgBankBranchRepo;
    @Autowired private SchoolDetailsRepository schoolDetailsRepo;
    @Autowired private ReligionRepository religionRepo;
    @Autowired private CasteRepository casteRepo;
    @Autowired private ParentDetailsRepository parentDetailsRepo;
    @Autowired private ProConcessionRepository proConcessionRepo;
    @Autowired private StudentOrientationDetailsRepository studentOrientationDetailsRepo;
    @Autowired private OrientationBatchRepository orientationBatchRepo;
    @Autowired private CmpsOrientationRepository cmpsOrientationRepo;
    @Autowired private CmpsOrientationStreamViewRepository cmpsOrientationStreamViewRepo;
    @Autowired private CmpsOrientationProgramViewRepository cmpsOrientationProgramViewRepo;
    @Autowired private CmpsOrientationBatchFeeViewRepository cmpsOrientationBatchFeeViewRepo;
    @Autowired private BloodGroupRepository bloodGroupRepo;
    @Autowired private StreamRepository streamRepo;
    @Autowired private ProgramNameRepository programNameRepo;
    @Autowired private ExamProgramRepository examProgramRepo;
    // endregion

    // region Dropdown and GetById Methods
    
    public List<GenericDropdownDTO> getAllReligions() {
        return religionRepo.findAll().stream()
                .map(r -> new GenericDropdownDTO(r.getReligion_id(), r.getReligion_type()))
                .collect(Collectors.toList());
    }

    public List<GenericDropdownDTO> getAllCastes() {
        return casteRepo.findAll().stream()
                .map(c -> new GenericDropdownDTO(c.getCaste_id(), c.getCaste_type()))
                .collect(Collectors.toList());
    }
    
    public List<GenericDropdownDTO> getAllAdmissionTypes() {
        return admissionTypeRepo.findAll().stream()
                .map(t -> new GenericDropdownDTO(t.getAdms_type_id(), t.getAdms_type_name()))
                .collect(Collectors.toList());
    }

    public List<GenericDropdownDTO> getAllStudentTypes() {
        return studentTypeRepo.findAll().stream()
                .map(t -> new GenericDropdownDTO(t.getStud_type_id(), t.getStud_type())).collect(Collectors.toList());
    }

    public List<GenericDropdownDTO> getAllGenders() {
        return genderRepo.findAll().stream().map(g -> new GenericDropdownDTO(g.getGender_id(), g.getGenderName()))
                .collect(Collectors.toList());
    }

    public List<GenericDropdownDTO> getAllCampuses() {
        return campusRepo.findAll().stream().map(c -> new GenericDropdownDTO(c.getCampusId(), c.getCampusName()))
                .collect(Collectors.toList());
    }
    
//    public List<GenericDropdownDTO> getOrientationsByCampus(int campusId) {
//        return cmpsOrientationStreamViewRepo.findByCmpsId(campusId).stream()
//            .map(view -> new GenericDropdownDTO(view.getOrientationId(), view.getOrientationName()))
//            .distinct()
//            .collect(Collectors.toList());
//    }
//    
//    public List<GenericDropdownDTO> getStreamsByCampusAndOrientation(int campusId, int orientationId) {
//        return cmpsOrientationStreamViewRepo.findByCmpsIdAndOrientationId(campusId, orientationId).stream()
//            .map(view -> new GenericDropdownDTO(view.getStreamId(), view.getStreamName()))
//            .distinct()
//            .collect(Collectors.toList());
//    }
//
//    public List<GenericDropdownDTO> getProgramsByCampusAndOrientation(int campusId, int orientationId) {
//        return cmpsOrientationProgramViewRepo.findByCmpsIdAndOrientationId(campusId, orientationId).stream()
//            .map(view -> new GenericDropdownDTO(view.getProgramId(), view.getProgramName()))
//            .distinct()
//            .collect(Collectors.toList());
//    }
//    
//    public List<GenericDropdownDTO> getBatchesByCampusAndOrientation(int campusId, int orientationId) {
//        return cmpsOrientationBatchFeeViewRepo.findByCmpsIdAndOrientationId(campusId, orientationId).stream()
//            .map(view -> new GenericDropdownDTO(view.getOrientationBatchId(), view.getOrientationBatchName()))
//            .distinct()
//            .collect(Collectors.toList());
//    }

    public CourseFeeDTO getOrientationFee(int campusId, int orientationId) {
        List<CmpsOrientation> orientations = cmpsOrientationRepo
                .findByCmpsIdAndOrientationOrientationId(campusId, orientationId);

        if (orientations.isEmpty()) {
            throw new EntityNotFoundException(
                "Fee not found for Campus ID: " + campusId + " and Orientation ID: " + orientationId
            );
        }
        float fee = orientations.get(0).getOrientation_fee();
        return new CourseFeeDTO(fee);
    }
    
    // ... other standard dropdown and getById methods
    
    // endregion

    @Transactional
    public StudentAcademicDetails createNewAdmission(StudentAdmissionDTO formData) {
        
        // --- 1. Save Academic Details ---
        StudentAcademicDetails academicDetails = new StudentAcademicDetails();
        academicYearRepository.findById(26).ifPresent(academicDetails::setAcademicYear);
        academicDetails.setStudAdmsNo(formData.getStudAdmsNo());
        academicDetails.setFirst_name(formData.getStudentName());
        academicDetails.setLast_name(formData.getSurname());
        academicDetails.setHt_no(formData.getHtNo());
        academicDetails.setApaar_no(formData.getApaarNo());
        academicDetails.setAdms_date(LocalDate.now());
        academicDetails.setDoj(formData.getDateOfJoin());
        academicDetails.setApp_sale_date(formData.getAppSaleDate());
        academicDetails.setPre_school_name(formData.getSchoolName());
        if (formData.getPreSchoolStateId() != null) stateRepo.findById(formData.getPreSchoolStateId()).ifPresent(academicDetails::setState);
        if (formData.getPreSchoolDistrictId() != null) districtRepo.findById(formData.getPreSchoolDistrictId()).ifPresent(academicDetails::setDistrict);
        if (formData.getPreschoolTypeId() != null) {
             schoolTypeRepo.findById(formData.getPreschoolTypeId()).ifPresent(academicDetails::setCampusSchoolType);
        }
        academicDetails.setAdmission_referred_by(formData.getAdmissionReferredBy());
        academicDetails.setScore_app_no(formData.getScoreAppNo());
        academicDetails.setScore_marks(formData.getMarks());
        academicDetails.setAdditional_orientation_fee(formData.getOrientationFee() != null ? formData.getOrientationFee().intValue() : 0);
        if (formData.getGenderId() != null) genderRepo.findById(formData.getGenderId()).ifPresent(academicDetails::setGender);
        if (formData.getAppTypeId() != null) admissionTypeRepo.findById(formData.getAppTypeId()).ifPresent(academicDetails::setAdmissionType);
        if (formData.getStudentTypeId() != null) studentTypeRepo.findById(formData.getStudentTypeId()).ifPresent(academicDetails::setStudentType);
        if (formData.getStudyTypeId() != null) studyTypeRepo.findById(formData.getStudyTypeId()).ifPresent(academicDetails::setStudyType);
        if (formData.getQuotaId() != null) quotaRepo.findById(formData.getQuotaId()).ifPresent(academicDetails::setQuota);
        if (formData.getStatusId() != null) statusRepo.findById(formData.getStatusId()).ifPresent(academicDetails::setStatus);
        StudentClass studentClass = classRepo.findById(formData.getClassId()).orElseThrow(() -> new EntityNotFoundException("Invalid Class ID"));
        academicDetails.setStudentClass(studentClass);
        Campus campus = campusRepo.findById(formData.getCampusId()).orElseThrow(() -> new EntityNotFoundException("Invalid Campus ID"));
        academicDetails.setCampus(campus);
        Employee pro = employeeRepo.findById(formData.getProId()).orElseThrow(() -> new EntityNotFoundException("Invalid PRO ID"));
        academicDetails.setEmployee(pro);
        academicDetails.setCreated_by(formData.getCreatedBy());
        StudentAcademicDetails savedAcademicDetails = academicDetailsRepo.save(academicDetails);

        // --- 2. Save Personal Details ---
        StudentPersonalDetails personalDetails = new StudentPersonalDetails();
        personalDetails.setStudentAcademicDetails(savedAcademicDetails);
        personalDetails.setStud_aadhaar_no(formData.getAadharCardNo());
        personalDetails.setDob(formData.getDob());
        personalDetails.setReligion_id(formData.getReligionId());
        personalDetails.setCaste_id(formData.getCasteId());
        personalDetails.setCreated_by(formData.getCreatedBy());
        if(formData.getBloodGroupId() != null){
            bloodGroupRepo.findById(formData.getBloodGroupId()).ifPresent(personalDetails::setBloodGroup);
        }
        personalDetailsRepo.save(personalDetails);
        
        // --- 3. Save Student Orientation Details ---
        StudentOrientationDetails orientationDetails = new StudentOrientationDetails();
        orientationDetails.setStudentAcademicDetails(savedAcademicDetails);
        orientationDetails.setOrientation_date(formData.getOrientationDate());
        if (formData.getOrientationId() != null) orientationRepo.findById(formData.getOrientationId()).ifPresent(orientationDetails::setOrientation);
        if (formData.getOrientationBatchId() != null) orientationBatchRepo.findById(formData.getOrientationBatchId()).ifPresent(orientationDetails::setOrientationBatch);
//        if (formData.getStreamId() != null) streamRepo.findById(formData.getStreamId()).ifPresent(orientationDetails::setStream);
//        if (formData.getProgramId() != null) programNameRepo.findById(formData.getProgramId()).ifPresent(orientationDetails::setProgramName);
//        if (formData.getExamProgramId() != null) examProgramRepo.findById(formData.getExamProgramId()).ifPresent(orientationDetails::setExamProgram);
//        studentOrientationDetailsRepo.save(orientationDetails);

        // --- 4. Save Parent Details ---
        if (formData.getParents() != null && !formData.getParents().isEmpty()) {
            for (ParentDetailsDTO parentDto : formData.getParents()) {
                ParentDetails parent = new ParentDetails();
                parent.setStudentAcademicDetails(savedAcademicDetails);
                parent.setName(parentDto.getName());
                parent.setOccupation(parentDto.getOccupation());
                parent.setMobileNo(parentDto.getMobileNo());
                parent.setEmail(parentDto.getEmail());
                parent.setCreated_by(formData.getCreatedBy());
                if (parentDto.getRelationTypeId() != null) {
                    relationTypeRepo.findById(parentDto.getRelationTypeId())
                                    .ifPresent(parent::setStudentRelation);
                }
                parentDetailsRepo.save(parent);
            }
        }
        
        // --- 5. Save Address Details ---
        if (formData.getAddressDetails() != null) {
            AddressDetailsDTO addressDTO = formData.getAddressDetails();
            StudentAddress address = new StudentAddress();
            address.setStudentAcademicDetails(savedAcademicDetails);
            address.setHouse_no(addressDTO.getDoorNo());
            address.setStreet(addressDTO.getStreet());
            address.setLandmark(addressDTO.getLandmark());
            address.setArea(addressDTO.getArea());
            if (addressDTO.getPincode() != null) address.setPostalCode(addressDTO.getPincode());
            if (addressDTO.getCityId() != null) cityRepo.findById(addressDTO.getCityId()).ifPresent(address::setCity);
            if (addressDTO.getMandalId() != null) mandalRepo.findById(addressDTO.getMandalId()).ifPresent(address::setMandal);
            if (addressDTO.getDistrictId() != null) districtRepo.findById(addressDTO.getDistrictId()).ifPresent(address::setDistrict);
            if (addressDTO.getStateId() != null) stateRepo.findById(addressDTO.getStateId()).ifPresent(address::setState);
            address.setCreated_by(formData.getCreatedBy());
            addressRepo.save(address);
        }

        // --- 6. Save Sibling Details ---
        if (formData.getSiblings() != null && !formData.getSiblings().isEmpty()) {
            for (SiblingDTO siblingDto : formData.getSiblings()) {
                Sibling sibling = new Sibling();
                sibling.setStudentAcademicDetails(savedAcademicDetails);
                sibling.setSibling_name(siblingDto.getFullName());
                sibling.setSibling_school(siblingDto.getSchoolName());
                if (siblingDto.getClassId() != null) classRepo.findById(siblingDto.getClassId()).ifPresent(sibling::setStudentClass);
                if (siblingDto.getRelationTypeId() != null) relationTypeRepo.findById(siblingDto.getRelationTypeId()).ifPresent(sibling::setStudentRelation);
                if (siblingDto.getGenderId() != null) genderRepo.findById(siblingDto.getGenderId()).ifPresent(sibling::setGender);
                sibling.setCreated_by(formData.getCreatedBy());
                siblingRepo.save(sibling);
            }
        }

        // --- 7. Save Concession Details ---
        if (formData.getStudentConcessionDetails() != null) {
            saveStudentConcession(formData.getStudentConcessionDetails(), savedAcademicDetails);
        }
        if (formData.getProConcessionDetails() != null) {
            saveProConcession(formData.getProConcessionDetails(), savedAcademicDetails, formData.getCreatedBy());
        }

        // --- 8. Save Payment Details ---
        if (formData.getPaymentDetails() != null) {
            savePaymentDetails(formData.getPaymentDetails(), savedAcademicDetails, null);
        }
        
        return savedAcademicDetails;
    }

    // region Helper Methods
    
    @Transactional
    private void savePaymentDetails(PaymentDetailsDTO paymentDetailsDTO, StudentAcademicDetails academicDetails, StudentConcessionType savedConcession) {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setStudentAcademicDetails(academicDetails);
        paymentDetails.setApp_fee(paymentDetailsDTO.getApplicationFeeAmount() != null ? paymentDetailsDTO.getApplicationFeeAmount() : 0.0f);
        paymentDetails.setPaid_amount(paymentDetailsDTO.getApplicationFeeAmount() != null ? paymentDetailsDTO.getApplicationFeeAmount() : 0.0f);
        paymentDetails.setPre_print_receipt_no(paymentDetailsDTO.getPrePrintedReceiptNo());
        paymentDetails.setApplication_fee_pay_date(paymentDetailsDTO.getApplicationFeeDate());
        paymentDetails.setConc_amount(paymentDetailsDTO.getConcessionAmount());
        paymentDetails.setAcedemicYear(academicDetails.getAcademicYear());
        paymentDetails.setStudentClass(academicDetails.getStudentClass());
        paymentDetails.setStatus(academicDetails.getStatus());
        paymentDetails.setStudentConcessionType(savedConcession);
        paymentDetails.setCreated_by(academicDetails.getCreated_by());

        if (paymentDetailsDTO.getPaymentModeId() != null) {
            paymentModeRepo.findById(paymentDetailsDTO.getPaymentModeId()).ifPresent(paymentDetails::setPaymenMode);
        }

        PaymentDetails savedPaymentDetails = paymentDetailsRepo.save(paymentDetails);

        PaymentMode paymentMode = savedPaymentDetails.getPaymenMode();
        if (paymentMode != null && ("DD".equals(paymentMode.getPayment_type()) || "Cheque".equals(paymentMode.getPayment_type()))) {
            StudentApplicationTransaction transaction = new StudentApplicationTransaction();
            transaction.setPaymnetDetails(savedPaymentDetails);
            transaction.setNumber(paymentDetailsDTO.getChequeDdNo());
            transaction.setIfsc_code(paymentDetailsDTO.getIfscCode());
            transaction.setDate(paymentDetailsDTO.getChequeDdDate());
            transaction.setCreated_by(academicDetails.getCreated_by());
            transaction.setPaymentMode(paymentMode);
            transaction.setApplication_fee_pay_date(paymentDetailsDTO.getApplicationFeeDate());
            
            if (paymentDetailsDTO.getCityId() != null) cityRepo.findById(paymentDetailsDTO.getCityId()).ifPresent(transaction::setCity);
            if (paymentDetailsDTO.getOrgBankId() != null) orgBankRepo.findById(paymentDetailsDTO.getOrgBankId()).ifPresent(transaction::setOrgBank);
            if (paymentDetailsDTO.getOrgBankBranchId() != null) orgBankBranchRepo.findById(paymentDetailsDTO.getOrgBankBranchId()).ifPresent(transaction::setOrgBankBranch);
            if (paymentDetailsDTO.getOrganizationId() != null) transaction.setOrg_id(paymentDetailsDTO.getOrganizationId());

            appTransactionRepo.save(transaction);
        }
    }

    @Transactional
    private void saveStudentConcession(StudentConcessionDetailsDTO dto, StudentAcademicDetails academicDetails) {
        if (dto.getConcessions() == null || dto.getConcessions().isEmpty()) {
            return;
        }
        for (ConcessionEntryDTO entry : dto.getConcessions()) {
            if (entry.getAmount() != null && entry.getAmount() > 0) {
                StudentConcessionType concession = new StudentConcessionType();
                setCommonConcessionDetails(concession, dto, academicDetails);
                concession.setConc_amount(entry.getAmount());
                concessionTypeRepo.findById(entry.getConcTypeId())
                        .ifPresent(concession::setConcessionType);
                studentConcessionTypeRepo.save(concession);
            }
        }
    }

    private void setCommonConcessionDetails(StudentConcessionType concession, StudentConcessionDetailsDTO dto, StudentAcademicDetails academicDetails) {
        concession.setStudAdmsId(academicDetails.getStud_adms_id());
        concession.setAcademicYear(academicDetails.getAcademicYear());
        concession.setConc_issued_by(dto.getConcessionIssuedBy());
        concession.setConc_authorised_by(dto.getConcessionAuthorisedBy());
        concession.setComments(dto.getDescription());
        concession.setCreated_by(academicDetails.getCreated_by());
        concession.setCreated_Date(LocalDateTime.now());
        if (dto.getConcessionReasonId() != null) {
            concessionReasonRepo.findById(dto.getConcessionReasonId()).ifPresent(concession::setConcessionReason);
        }
    }

    @Transactional
    private ProConcession saveProConcession(ProConcessionDTO dto, StudentAcademicDetails academicDetails, Integer createdBy) {
        ProConcession proConcession = new ProConcession();
        proConcession.setAdm_no(academicDetails.getStudAdmsNo());
        proConcession.setConc_amount(dto.getConcessionAmount());
        proConcession.setReason(dto.getReason());
        proConcession.setCreated_by(createdBy);

        if (dto.getProEmployeeId() != null) {
            employeeRepo.findById(dto.getProEmployeeId())
                .ifPresent(proConcession::setEmployee);
        } else {
             logger.warn("PRO Concession is being saved without an associated PRO Employee ID for admission number: {}", academicDetails.getStudAdmsNo());
        }

        return proConcessionRepo.save(proConcession);
    }
    // endregion
}