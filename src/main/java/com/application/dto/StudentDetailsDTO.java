// StudentDetailsDTO.java
package com.application.dto;

import java.util.List;
import lombok.Data;

@Data
public class StudentDetailsDTO {
    private String studentName;
    private String surname;
    private String fathername;
    private String mothername;
    private String gender;
    private float applicationFee;
    private float confirmationAmount; 
    private List<Float> concessionAmounts;
}