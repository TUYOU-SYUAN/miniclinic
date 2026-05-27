package tw.edu.fju.miniclinic.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {

    List<Doctor> findByDepartment_Name(String departmentName);

    // If you uncommented the findAllDepartments in DoctorApiController, you'd need a custom query here.
}