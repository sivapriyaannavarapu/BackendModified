package com.application.dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Main DTO for capturing all data from the student admission form.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAdmissionDTO {

    // --- Academic & Personal Info ---
    private String studAdmsNo;
    private String studentName; // Maps to first_name
    private String surname;     // Maps to last_name
    private String htNo;
    private String apaarNo;
    private LocalDate dateOfJoin;
    private Integer createdBy;
    private Long aadharCardNo;
    private Date dob;
    private Integer religionId;
    private Integer casteId;

    // --- Previous School & Orientation Info ---
    private String schoolName;
    private Integer preSchoolStateId;
    private Integer preSchoolDistrictId;
    private Integer schoolTypeId;
    private String admissionReferredBy;
    private String scoreAppNo;
    private int marks;
//    private Integer orientationFee;
    private Date orientationDate;
    private Date appSaleDate;

    // --- Core Admission IDs ---
    private Integer genderId;
    private Integer appTypeId;
    private Integer studentTypeId;
    private Integer joinIntoId; // Orientation ID
    private Integer sectionId;
    private Integer quotaId;
    private Integer statusId;
    private Integer classId;
    private Integer campusId;
    private Integer proId; // PRO Employee ID
    private Integer courseBatchId;
//    private Integer foodTypeId;
//    private Integer streamId;
//    private Integer programId;

    // --- Parent Info (Now a flexible list) ---
    private List<ParentDetailsDTO> parents;

    // --- Nested DTOs for Complex Sections ---
    private AddressDetailsDTO addressDetails;
    private List<SiblingDTO> siblings;
    private StudentConcessionDetailsDTO studentConcessionDetails;
    private ProConcessionDTO proConcessionDetails;
    private PaymentDetailsDTO paymentDetails;
}