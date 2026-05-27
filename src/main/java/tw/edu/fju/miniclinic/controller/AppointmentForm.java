package tw.edu.fju.miniclinic.controller;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank; // Added
import jakarta.validation.constraints.NotNull; // Added
import jakarta.validation.constraints.Pattern; // Added

// This DTO is used to bind data from the appointment creation form (appointment-new.html)
public class AppointmentForm {
    @NotBlank(message = "請輸入病患編號") // Added specific message for NotBlank on String ID
    @Pattern(regexp = "TEST\\d{5}", message = "病患編號格式為 TESTxxxxx") // Added pattern validation
    private String patientId; // Changed from Integer to String
    @NotBlank(message = "請選擇醫師") // Added validation
    private String doctorId;

    @NotNull(message = "請選擇日期") // Added validation
    private LocalDate appointmentDate;

    @NotBlank(message = "請選擇時段") // Added validation
    private String appointmentTime; // "AM", "PM", "EVENING"

    // Getters and Setters
    public String getPatientId() { // Changed return type to String
        return patientId;
    }
    public void setPatientId(String patientId) { // Changed parameter type to String
        this.patientId = patientId;
    }
    public String getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }
    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    public String getAppointmentTime() {
        return appointmentTime;
    }
    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}