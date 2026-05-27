package tw.edu.fju.miniclinic.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import tw.edu.fju.miniclinic.model.Appointment;
import tw.edu.fju.miniclinic.model.AppointmentRepository; // Added
import tw.edu.fju.miniclinic.model.ClinicService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/appointments")
public class AppointmentApiController {

    private final ClinicService clinicService;
    private final AppointmentRepository appointmentRepository; // Added

    public AppointmentApiController(ClinicService clinicService, AppointmentRepository appointmentRepository) { // Modified constructor
        this.clinicService = clinicService;
        this.appointmentRepository = appointmentRepository; // Injected
    }

    @GetMapping("/count")
    public long getTotalAppointments() {
        return clinicService.getTotalAppointments();
    }

    @GetMapping
    public List<Appointment> getAppointments(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String doctorId) {

        if (date != null && !date.isBlank()) {
            return clinicService.getAppointmentsByDate(LocalDate.parse(date));
        }
        if (doctorId != null && !doctorId.isBlank()) {
            return clinicService.getAppointmentsByDoctorId(doctorId);
        }
        return List.of(); // Return empty list if no parameters are provided
    }

    @GetMapping("/by-department-count")
    public List<Map<String, Object>> getAppointmentsCountByDepartment() {
        return clinicService.getAppointmentsCountByDepartment();
    }

    @PutMapping("/{apptId}/status") // Corrected path to be relative to class's @RequestMapping
    public ResponseEntity<Appointment> updateStatus(
            @PathVariable Long apptId,
            @RequestBody Map<String, String> payload,
            HttpSession session) {

        String loggedInDoctorId = (String) session.getAttribute("loggedInDoctorId");

        Appointment appt = appointmentRepository.findById(apptId.intValue()).orElse(null); // Corrected to use appointmentRepository and intValue()
        if (appt == null) {
            return ResponseEntity.notFound().build();
        }

        // 只能修改自己的掛號
        if (!appt.getDoctor().getId().equals(loggedInDoctorId)) {
            return ResponseEntity.status(403).build();
        }

        String newStatus = payload.get("status");
        if (!List.of("BOOKED", "COMPLETED", "CANCELLED").contains(newStatus)) {
            return ResponseEntity.badRequest().build();
        }

        appt.setStatus(newStatus); // Uncommented as 'status' field now exists
        return ResponseEntity.ok(appointmentRepository.save(appt));
    }
}