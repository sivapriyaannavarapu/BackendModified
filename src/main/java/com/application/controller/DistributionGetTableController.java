package com.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.dto.DistributionGetTableDTO;
import com.application.service.DistributionGetTableService;

@RestController
@RequestMapping("/distribution/table")
public class DistributionGetTableController {
	
	
	@Autowired
	private DistributionGetTableService distributionGetTableService;
	
	@GetMapping("/getdistributiondata/{empId}")
    public List<DistributionGetTableDTO> getDistributionsByEmployee(@PathVariable int empId) {
        return distributionGetTableService.getDistributionsByEmployeeId(empId);
    }
 
}
