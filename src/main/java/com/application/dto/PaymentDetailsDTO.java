
package com.application.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for payment details and related bank transaction info.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailsDTO {

    // --- Core Payment Info ---
    private Float applicationFeeAmount;
    private String prePrintedReceiptNo;
    private Date applicationFeeDate;
    private Float concessionAmount; // Total concession amount to be displayed/recorded
    private Integer paymentModeId;


    // --- Bank Transaction Info (for Cheque/DD) ---
    private String chequeDdNo;
    private String ifscCode;
    private Date chequeDdDate;
    private Integer cityId;
    private Integer orgBankId;
    private Integer orgBankBranchId;
    private Integer organizationId;
    
    private int created_by;
    
}
