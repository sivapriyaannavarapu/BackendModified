package com.application.repository;
 
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.application.entity.UserAppSold;
 
@Repository
public interface UserAppSoldRepository extends JpaRepository<UserAppSold, Long> {
    
    /**
     * Finds top performers by employee, but selects their associated zone name.
     */
	
	 List<UserAppSold> findByEntityId(Integer entityId);
    @Query(value = "SELECT z.zone_name, (CAST(SUM(s.sold) AS DECIMAL) / SUM(s.total_app_count)) * 100 AS performance_percentage " +
                   "FROM sce_application.sce_user_app_sold s " +
                   "JOIN sce_employee.sce_emp e ON s.emp_id = e.emp_id " +
                   "JOIN sce_campus.sce_cmps c ON e.cmps_id = c.cmps_id " +
                   "JOIN sce_locations.sce_zone z ON c.zone_id = z.zone_id " +
                   "WHERE s.entity_id = :entityId " +
                   "GROUP BY s.emp_id, z.zone_name " + // Group by employee, select zone name
                   "ORDER BY performance_percentage DESC", nativeQuery = true)
    List<Object[]> findTopPerformersByEntityType(@Param("entityId") Integer entityId);
 
    /**
     * Finds worst performers by employee, but selects their associated zone name.
     */
    @Query(value = "SELECT z.zone_name, (CAST((SUM(s.total_app_count) - SUM(s.sold)) AS DECIMAL) / SUM(s.total_app_count)) * 100 AS performance_percentage " +
                   "FROM sce_application.sce_user_app_sold s " +
                   "JOIN sce_employee.sce_emp e ON s.emp_id = e.emp_id " +
                   "JOIN sce_campus.sce_cmps c ON e.cmps_id = c.cmps_id " +
                   "JOIN sce_locations.sce_zone z ON c.zone_id = z.zone_id " +
                   "WHERE s.entity_id = :entityId " +
                   "GROUP BY s.emp_id, z.zone_name " + // Group by employee, select zone name
                   "ORDER BY performance_percentage DESC", nativeQuery = true)
    List<Object[]> findWorstPerformersByEntityType(@Param("entityId") Integer entityId);
}