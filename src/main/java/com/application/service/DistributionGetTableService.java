package com.application.service;
 
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.application.dto.DistributionGetTableDTO;
import com.application.entity.Distribution;
import com.application.repository.DistributionRepository;
 
@Service
public class DistributionGetTableService {
 
    @Autowired
    private DistributionRepository distributionRepository;
 
    public List<DistributionGetTableDTO> getDistributionsByEmployeeId(int empId) {
        List<Distribution> distributions = distributionRepository.findByCreatedBy(empId);
 
        return distributions.stream().map(d -> new DistributionGetTableDTO(
            d.getAppDistributionId(),
            d.getAppStartNo(),
            d.getAppEndNo(),
            d.getTotalAppCount(),
            d.getAmount(),
            d.getIssuedByType().getAppIssuedId(),
            d.getIssuedByType().getTypeName(),
            d.getIssuedToType().getAppIssuedId(),
            d.getIssuedToType().getTypeName()
        )).toList();
    }
}