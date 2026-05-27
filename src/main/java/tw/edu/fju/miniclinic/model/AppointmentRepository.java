package tw.edu.fju.miniclinic.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    List<Appointment> findByAppointmentDate(LocalDate date);

    List<Appointment> findByDoctor_Id(String doctorId);

    @Query("SELECT d.department.name AS departmentName, COUNT(a.id) AS appointmentCount " +
           "FROM Appointment a JOIN a.doctor d GROUP BY d.department.name")
    List<Map<String, Object>> countAppointmentsByDepartment();

    List<Appointment> findByDoctorAndAppointmentDate(Doctor doctor, LocalDate appointmentDate);  // 更正屬性名稱
}