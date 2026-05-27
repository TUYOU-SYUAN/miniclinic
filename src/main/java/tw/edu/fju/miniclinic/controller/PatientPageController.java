package tw.edu.fju.miniclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import tw.edu.fju.miniclinic.model.Patient;
import java.util.List;

@Controller
public class PatientPageController {

    @Autowired
    private tw.edu.fju.miniclinic.model.PatientRepository patientRepo;

    @GetMapping("/patients")
    public String listPatients(Model model) {
        List<Patient> patients = patientRepo.findAll();
        model.addAttribute("patients", patients);
        return "patients"; // Refers to src/main/resources/templates/patients.html
    }
}