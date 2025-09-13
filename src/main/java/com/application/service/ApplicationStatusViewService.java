package com.application.service;
 
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.dto.ApplicationStatusViewDTO;
import com.application.entity.AppStatusTrackView;
import com.application.repository.AppStatusTrackViewRepository;
 
@Service
public class ApplicationStatusViewService {
 
    @Autowired
    private AppStatusTrackViewRepository appStatusTrackViewRepository;

    public List<AppStatusTrackView> getApplicationStatusByCampus(int cmpsId) {
        return appStatusTrackViewRepository.findByCmps_id(cmpsId);
    }
}