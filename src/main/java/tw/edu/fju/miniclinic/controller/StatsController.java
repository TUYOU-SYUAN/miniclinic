package tw.edu.fju.miniclinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tw.edu.fju.miniclinic.model.ClinicService;

@Controller
public class StatsController {

    private final ClinicService clinicService;

    public StatsController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    /**
     * GET /stats
     * Displays clinic statistics: total doctors, patients, appointments, and appointments by department.
     * @param model The model to pass data to the view.
     * @return The name of the Thymeleaf template.
     */
    @GetMapping("/stats")
    public String getClinicStats(Model model) {
        model.addAttribute("totalDoctors", clinicService.getTotalDoctors());
        model.addAttribute("totalPatients", clinicService.getTotalPatients());
        model.addAttribute("totalAppointments", clinicService.getTotalAppointments());
        model.addAttribute("appointmentsByDepartment", clinicService.getAppointmentsCountByDepartment());
        return "stats"; // Refers to src/main/resources/templates/stats.html
    }
}