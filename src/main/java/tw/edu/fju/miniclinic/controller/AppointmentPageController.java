package tw.edu.fju.miniclinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute; // Added
import org.springframework.web.bind.annotation.PostMapping; // Added
import org.springframework.web.bind.annotation.RequestMapping;
import tw.edu.fju.miniclinic.model.ClinicService;
import tw.edu.fju.miniclinic.model.DoctorRepository; // Keep this for showNewAppointmentForm
import tw.edu.fju.miniclinic.model.PatientRepository; // Added
import tw.edu.fju.miniclinic.model.AppointmentRepository; // Added
import tw.edu.fju.miniclinic.model.Patient; // Updated - no further change to the line itself
import tw.edu.fju.miniclinic.model.Doctor; // Added
import tw.edu.fju.miniclinic.model.Appointment; // Added
import jakarta.validation.Valid; // Added
import org.springframework.validation.BindingResult; // Added
import java.time.LocalTime; // Added
import java.util.List;
import org.slf4j.Logger; // Added for logging
import org.slf4j.LoggerFactory; // Added for logging

@Controller
@RequestMapping("/appointment")
public class AppointmentPageController {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository; // Added
    private final AppointmentRepository appointmentRepository; // Added
    private final ClinicService clinicService;

    private static final Logger logger = LoggerFactory.getLogger(AppointmentPageController.class); // Added logger

    public AppointmentPageController(DoctorRepository doctorRepository, PatientRepository patientRepository, AppointmentRepository appointmentRepository, ClinicService clinicService) { // Modified constructor
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.clinicService = clinicService;
    }

    @GetMapping("/new")
    public String showNewAppointmentForm(Model model) {
        model.addAttribute("form", new AppointmentForm()); // Provide an empty form object for Thymeleaf
        model.addAttribute("doctors", doctorRepository.findAll()); // Provide list of doctors for the dropdown
        return "appointment-new"; // Return the name of the Thymeleaf template
    }

    @PostMapping("/new") // Added
    public String submitAppointment(
            @Valid @ModelAttribute("form") AppointmentForm form,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("form", form);
            model.addAttribute("doctors", doctorRepository.findAll());
            // Need to add patients for the form if it uses patientId dropdown
            // For now, assuming patientId is just an input field.
            return "appointment-new";
        }

        logger.info("提交掛號表單資料: Patient ID = {}, Doctor ID = {}, Date = {}, Time = {}",
                    form.getPatientId(), form.getDoctorId(), form.getAppointmentDate(), form.getAppointmentTime());

        // 步驟 1：用表單的 ID，從資料庫查出真正的物件
        Patient patient = patientRepository.findById(form.getPatientId()).orElse(null);
        Doctor  doctor  = doctorRepository.findById(form.getDoctorId()).orElse(null);
        
        logger.info("查詢結果: Patient {} found, Doctor {} found",
                    patient != null ? patient.getId() : "not",
                    doctor != null ? doctor.getId() : "not");

        // 步驟 2：驗證——找不到就回表單顯示錯誤
        if (patient == null || doctor == null) {
            model.addAttribute("error", "查無此病患ID或醫師編號，請確認後重試");
            model.addAttribute("form", form);
            model.addAttribute("doctors", doctorRepository.findAll());
            return "appointment-new";   // ← 回到表單頁，不是跳轉
        }

        // 步驟 3：建立 Appointment Entity，設定關聯物件
        Appointment appt = new Appointment();
        appt.setPatient(patient);
        appt.setDoctor(doctor);
        appt.setAppointmentDate(form.getAppointmentDate()); // LocalDate can be directly set

        // Convert String appointmentTime ("AM", "PM", "EVENING") to LocalTime
        LocalTime appointmentTime;
        switch (form.getAppointmentTime()) {
            case "AM": appointmentTime = LocalTime.of(9, 0); break;
            case "PM": appointmentTime = LocalTime.of(14, 0); break;
            case "EVENING": appointmentTime = LocalTime.of(19, 0); break;
            default: appointmentTime = LocalTime.of(0, 0); // Fallback or error
        }
        appt.setAppointmentTime(appointmentTime);
        appt.setStatus("BOOKED"); // 設定新掛號的預設狀態為 BOOKED

        // 步驟 4：存入資料庫，JPA 自動填入 id
        Appointment saved = appointmentRepository.save(appt);

        // 步驟 5：把儲存後的物件交給結果頁面
        model.addAttribute("appointment", saved);
        return "appointment-result";
    }

    @GetMapping("/list") // Changed from /appointments to /appointment/list to align with @RequestMapping("/appointment")
    public String listAppointments(Model model) {
        List<tw.edu.fju.miniclinic.model.Appointment> appointments = clinicService.getAllAppointments();
        model.addAttribute("appointments", appointments);
        return "appointments"; // Refers to src/main/resources/templates/appointments.html
    }    
}