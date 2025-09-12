package com.application.service;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.dto.AppStatusDetailsDTO;
import com.application.dto.ApplicationDamagedDto;
import com.application.dto.CampusDto;
import com.application.dto.EmployeeDto;
import com.application.entity.AppStatus;
import com.application.entity.AppStatusTrackView;
import com.application.entity.ApplicationStatus;
import com.application.entity.Campus;
import com.application.entity.CampusProView;
import com.application.entity.Dgm;
import com.application.entity.Employee;
import com.application.entity.Status;
import com.application.entity.Zone;
import com.application.repository.AppStatusRepository;
import com.application.repository.AppStatusTrackViewRepository;
import com.application.repository.ApplicationStatusRepository;
import com.application.repository.CampusProViewRepository;
import com.application.repository.CampusRepository;
import com.application.repository.DgmRepository;
import com.application.repository.EmployeeRepository;
import com.application.repository.StatusRepository;
import com.application.repository.ZoneRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
 
@Service
public class ApplicationDamagedService {
 
    @PersistenceContext
    private EntityManager entityManager;
 
    @Autowired
    private AppStatusRepository appStatusRepository;
 
    @Autowired
    private StatusRepository statusRepository;
 
    @Autowired
    private EmployeeRepository employeeRepository;
 
    @Autowired
    private CampusRepository campusRepository;
 
    @Autowired
    private CampusProViewRepository campusProViewRepository;
 
    @Autowired
    private AppStatusTrackViewRepository appStatusTrackViewRepository;
 
    @Autowired
    public ApplicationStatusRepository applicationStatusRepository;
 
    @Autowired
    public DgmRepository dgmRepository;
 
    @Autowired
    public ZoneRepository zoneRepository;
 
    // New method for autopopulation
    public Optional<AppStatusTrackView> getDetailsByApplicationNo(int num) {
        return appStatusTrackViewRepository.findById(num);
    }
 
    public List<Employee> getAllZoneEmployees() {
        return employeeRepository.findAll();
    }
 
    public List<Campus> getAllCampuses() {
        return campusRepository.findAll();
    }
 
    public List<ApplicationStatus> getAllStatus() {
        return applicationStatusRepository.findAll();
    }
 
    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }
    
    public List<Campus> getCampusesByDgmId(int dgmId) {
        // Find all Dgm entries for the given DGM ID.
        // A DGM might be linked to multiple campuses, so find all entries
        List<Dgm> dgmEntries = dgmRepository.findByDgmId(dgmId);

        if (dgmEntries.isEmpty()) {
            // Handle case where DGM ID is not found
            return List.of(); 
        }

        // Use a Stream to map each Dgm entry to its Campus object
        return dgmEntries.stream()
                         .map(Dgm::getCampus)
                         .collect(Collectors.toList());
    }
    
    public Optional<AppStatusDetailsDTO> getAppStatusDetails(int appNo) {
        // Fetch the AppStatus entity by the application number
        Optional<AppStatus> appStatusOptional = appStatusRepository.findByApplicationNumber(appNo);

        // If no AppStatus is found, return an empty Optional
        if (appStatusOptional.isEmpty()) {
            return Optional.empty();
        }

        AppStatus appStatus = appStatusOptional.get();

        // Create a new DTO and populate it with details from related entities
        AppStatusDetailsDTO dto = new AppStatusDetailsDTO();

        // Populate PRO details
        if (appStatus.getEmployee() != null) {
            dto.setProId(appStatus.getEmployee().getEmp_id());
            dto.setProName(appStatus.getEmployee().getFirst_name() + " " + appStatus.getEmployee().getLast_name());
        }

        // Populate Zone details
        if (appStatus.getZone() != null) {
            dto.setZoneId(appStatus.getZone().getZoneId());
            dto.setZoneName(appStatus.getZone().getZoneName());
        }

        // Populate DGM details
        if (appStatus.getEmployee2() != null) {
            dto.setDgmEmpId(appStatus.getEmployee2().getEmp_id());
            dto.setDgmEmpName(appStatus.getEmployee2().getFirst_name() + " " + appStatus.getEmployee2().getLast_name());
        }

        // Populate Campus details
        if (appStatus.getCampus() != null) {
            dto.setCampusId(appStatus.getCampus().getCampusId());
            dto.setCampusName(appStatus.getCampus().getCampusName());
        }

        // Populate Status details
        if (appStatus.getStatus() != null) {
            dto.setStatus(appStatus.getStatus().getStatus_type());
        }
        
        dto.setReason(appStatus.getReason());

        return Optional.of(dto);
    }
 
    public List<CampusDto> getCampusDtosByZoneId(int zoneId) {
        // Find the Zone entity by its ID
        Optional<Zone> zoneOptional = zoneRepository.findById(zoneId);

        // If the Zone is found, get the Campuses as DTOs associated with it.
        // If not, return an empty list.
        return zoneOptional.map(zone -> campusRepository.findByZoneAsDto(zone))
                           .orElse(List.of());
    }
    
    @Transactional
    public AppStatus saveOrUpdateApplicationStatus(ApplicationDamagedDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO cannot be null");
        }
 
        Optional<AppStatus> existingAppStatusOpt = dto.getApplicationNo() != null
            ? appStatusRepository.findByAppNo(dto.getApplicationNo())
            : Optional.empty();
        AppStatus appStatus = existingAppStatusOpt.orElse(new AppStatus());
 
        Status status = statusRepository.findById(dto.getStatusId())
                .orElseThrow(() -> new EntityNotFoundException("Status not found with ID: " + dto.getStatusId()));
 
        Campus campus = campusRepository.findById(dto.getCampusId())
                .orElseThrow(() -> new EntityNotFoundException("Campus not found with ID: " + dto.getCampusId()));
 
        Employee proEmployee = employeeRepository.findById(dto.getProId())
                .orElseThrow(() -> new EntityNotFoundException("PRO Employee not found with ID: " + dto.getProId()));
 
        Zone zone = zoneRepository.findById(dto.getZoneId())
                .orElseThrow(() -> new EntityNotFoundException("Zone not found with ID: " + dto.getDgmEmpId()));
 
        Employee dgmEmployee = employeeRepository.findById(dto.getDgmEmpId())
                .orElseThrow(() -> new EntityNotFoundException("DGM Employee not found with ID: " + dto.getDgmEmpId()));
 
        appStatus.setApp_no(dto.getApplicationNo());
        appStatus.setReason(dto.getReason());
        appStatus.setStatus(status);
        appStatus.setCampus(campus);
        appStatus.setEmployee(proEmployee);
        appStatus.setZone(zone);
        appStatus.setEmployee2(dgmEmployee);
 
        if (dto.getStatusId() == 1) {
            appStatus.setIs_active(1);
        } else {
            appStatus.setIs_active(0);
        }
 
        appStatus.setCreated_by(2);
 
        return appStatusRepository.save(appStatus);
    }
 
    public List<EmployeeDto> getDgmNamesByZoneId(int zoneId) {
        List<Dgm> dgmList = dgmRepository.findByZoneId(zoneId);
        List<EmployeeDto> dgmEmployees = new ArrayList<>();
 
        for (Dgm dgm : dgmList) {
            if (dgm.getEmployee() != null) {
                EmployeeDto dto = new EmployeeDto();
                dto.setEmpId(dgm.getEmployee().getEmp_id());
                dto.setName(dgm.getEmployee().getFirst_name() + " " + dgm.getEmployee().getLast_name());
                dgmEmployees.add(dto);
            }
        }
        return dgmEmployees;
    }
 
    @Transactional(readOnly = true)
    public List<EmployeeDto> getEmployeeNamesByCampusId(int campusId) {
        entityManager.clear();
        List<CampusProView> views = campusProViewRepository.findByCampusId(campusId);
        System.out.println("Fetched CampusProView records: " + views.size());
        views.forEach(v -> System.out.println("cmps_id: " + v.getCmps_id() + ", emp_id: " + v.getEmp_id() + ", is_active: " + v.getIs_active()));
        return views.stream()
                .map(view -> {
                    int empId = view.getEmp_id();
                    Employee employee = employeeRepository.findById(empId).orElse(null);
                    if (employee != null && employee.getEmp_id() == empId && employee.getFirst_name() != null && employee.getLast_name() != null) {
                        EmployeeDto dto = new EmployeeDto();
                        dto.setEmpId(empId);
                        dto.setName(employee.getFirst_name() + " " + employee.getLast_name());
                        System.out.println("Debug - emp_id: " + empId + ", name: " + dto.getName() + ", hashCode: " + System.identityHashCode(employee));
                        return dto;
                    }
                    System.out.println("Debug - emp_id: " + empId + " not found or invalid");
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}