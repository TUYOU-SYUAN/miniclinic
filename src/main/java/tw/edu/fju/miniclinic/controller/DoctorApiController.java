package tw.edu.fju.miniclinic.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.edu.fju.miniclinic.model.Department;
import tw.edu.fju.miniclinic.model.DepartmentRepository;
import tw.edu.fju.miniclinic.model.Doctor;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import java.util.List;
import java.util.Optional;

@RestController
public class DoctorApiController {

    private final DoctorRepository doctorRepo;
    private final DepartmentRepository departmentRepo; // Inject DepartmentRepository

    public DoctorApiController(DoctorRepository doctorRepo, DepartmentRepository departmentRepo) {
        this.doctorRepo = doctorRepo;
        this.departmentRepo = departmentRepo;
    }

    @GetMapping("/api/doctors")
    public List<Doctor> getDoctors(
            @RequestParam(required = false) String department) {
        if (department == null || department.isBlank()) {
            return doctorRepo.findAll();
        }
        return doctorRepo.findByDepartment_Name(department); // Corrected method name
    }

    @GetMapping("/api/doctors/{doctorId}")
    public ResponseEntity<Doctor> getDoctor(@PathVariable String doctorId) {
        Optional<Doctor> doctor = doctorRepo.findById(doctorId);
        return doctor
            .map(d -> ResponseEntity.ok(d))       // 有 → 200 OK + 資料
            .orElse(ResponseEntity.notFound().build());  // 沒有 → 404
    }

    //@GetMapping("/api/departments")
    //public List<String> getDepartments() {
    //    return doctorRepo.findAllDepartments();
    //}
    
    @PostMapping("/api/doctors")
    public ResponseEntity<Doctor> createDoctor(@RequestBody DoctorRequest doctorRequest) { // Use a DTO for request
        Optional<Department> department = departmentRepo.findByName(doctorRequest.getDepartmentName());
        if (department.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Or a more specific error
        }
        Doctor newDoctor = new Doctor(doctorRequest.getId(), doctorRequest.getName(), department.get());
        Doctor savedDoctor = doctorRepo.save(newDoctor);
        return ResponseEntity.status(201).body(savedDoctor);
    }

    @PutMapping("/api/doctors/{doctorId}")
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable String doctorId,
            @RequestBody DoctorRequest doctorRequest) { // Use a DTO for request

        // Fetch the Department entity based on the name provided in the request
        Optional<Department> department = departmentRepo.findByName(doctorRequest.getDepartmentName());
        if (department.isEmpty()) {
            // If department not found, return a bad request or not found error
            // Depending on your business logic, you might want to create the department
            // or return a more specific error message.
            return ResponseEntity.badRequest().build(); // Example: 400 Bad Request
        }

        return doctorRepo.findById(doctorId)
            .map(existing -> {
                existing.setName(doctorRequest.getName());
                existing.setDepartment(department.get()); // Set the actual Department object
                return ResponseEntity.ok(doctorRepo.save(existing));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/doctors/{doctorId}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable String doctorId) {
        if (!doctorRepo.existsById(doctorId)) {
            return ResponseEntity.notFound().build();
        }
        doctorRepo.deleteById(doctorId);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}