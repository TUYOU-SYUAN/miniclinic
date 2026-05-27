package tw.edu.fju.miniclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.List;
import tw.edu.fju.miniclinic.model.Patient;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public Map<String, String> health() {
        return Map.of(
            "status", "ok",
            "service", "miniclinic"
        );
    }

    @GetMapping("/api/about")
    public Map<String, String> about() {
        return Map.of(
            "student_id", " 413570622 ",
            "student_name", " 涂佑瑄 ",
            "project", "MiniClinic",
            "version", "0.1.0",
            "chapter", "Ch09-A"
        );
    }

    @Autowired
    private tw.edu.fju.miniclinic.model.PatientRepository patientRepo;

    @GetMapping("/api/patients")
    public List<Patient> getAllPatients() { return patientRepo.findAll(); }
}
