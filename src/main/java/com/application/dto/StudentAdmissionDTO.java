
package com.application.dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAdmissionDTO {

    // --- Academic & Personal Info ---
    private String studAdmsNo;
    private String studentName;
    private String surname;
    private String htNo;
    private String apaarNo;
    private LocalDate dateOfJoin;
    private Integer createdBy;
    private Long aadharCardNo;
    private Date dob;
    private Integer religionId;
    private Integer casteId;
    private Integer schoolTypeId;

    // --- Previous School & Orientation Info ---
    private String schoolName;
    private Integer preSchoolStateId;
    private Integer preSchoolDistrictId;
    private Integer preschoolTypeId;
    private String admissionReferredBy;
    private String scoreAppNo;
    private int marks;
    private Date orientationDate;
    private Date appSaleDate;
    private Float orientationFee;

    // --- Core Admission IDs ---
    private Integer genderId;
    private Integer appTypeId;
    private Integer studentTypeId;
    private Integer studyTypeId;
    private Integer orientationId; // Corrected from "Orientationid"
    private Integer sectionId;
    private Integer quotaId;
    private Integer statusId;
    private Integer classId;
    private Integer campusId;
    private Integer proId;
    private Integer orientationBatchId;
    private Integer bloodGroupId;
    // --- Parent Info ---
    private List<ParentDetailsDTO> parents;

    // --- Nested DTOs ---
    private AddressDetailsDTO addressDetails;
    private List<SiblingDTO> siblings;
    private StudentConcessionDetailsDTO studentConcessionDetails;
    private ProConcessionDTO proConcessionDetails;
    private PaymentDetailsDTO paymentDetails;
}
