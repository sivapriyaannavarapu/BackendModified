package com.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.dto.MetricCardDTO;
import com.application.service.AppStatusTrackService;

@RestController
@RequestMapping("/api/dashboard/CO")
public class AppStatusTrackController {

	@Autowired
	private AppStatusTrackService appStatusTrackService;

	@GetMapping("/cards")
    public List<MetricCardDTO> getDashboardCards() {
        return appStatusTrackService.getDashboardCards();
    }

    @GetMapping("/cards/employee/{empId}")
    public List<MetricCardDTO> getDashboardCardsByEmployee(@PathVariable Integer empId) {
        return appStatusTrackService.getDashboardCardsByEmployee(empId);
    }
}