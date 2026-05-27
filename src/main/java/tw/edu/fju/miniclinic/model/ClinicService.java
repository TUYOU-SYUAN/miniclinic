package tw.edu.fju.miniclinic.model;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ClinicService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public ClinicService(DoctorRepository doctorRepository, PatientRepository patientRepository, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Retrieves the total number of doctors.
     * @return Total doctor count.
     */
    public long getTotalDoctors() {
        return doctorRepository.count();
    }

    /**
     * Retrieves the total number of patients.
     * @return Total patient count.
     */
    public long getTotalPatients() {
        return patientRepository.count();
    }

    /**
     * Retrieves the total number of appointments.
     * @return Total appointment count.
     */
    public long getTotalAppointments() {
        return appointmentRepository.count();
    }

    /**
     * Retrieves a list of all appointments.
     * @return List of all appointments.
     */
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    /**
     * Retrieves a list of appointments for a specific date.
     * @param date The date to filter appointments by.
     * @return List of appointments.
     */
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date);
    }

    public List<Appointment> getAppointmentsByDoctorId(String doctorId) {
        return appointmentRepository.findByDoctor_Id(doctorId);
    }

    public List<Map<String, Object>> getAppointmentsCountByDepartment() {
        return appointmentRepository.countAppointmentsByDepartment();
    }
}