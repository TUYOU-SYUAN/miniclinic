package tw.edu.fju.miniclinic.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Convert(converter = LocalDateConverter.class)
    @Column(name = "appointment_date", columnDefinition = "TEXT") // Explicitly define as TEXT
    private LocalDate appointmentDate;

    @Convert(converter = LocalTimeConverter.class) // Use the new converter
    @Column(name = "appointment_time", columnDefinition = "TEXT") // Explicitly define as TEXT
    private LocalTime appointmentTime;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id") // doctor_id in appointments table refers to id in doctors table
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id") // patient_id in appointments table refers to id in patients table
    private Patient patient;

    @Column(name = "status", length = 20) // Added status field
    private String status;

    // Constructors
    public Appointment() {
    }

    public Appointment(Integer id, LocalDate appointmentDate, LocalTime appointmentTime, Doctor doctor, Patient patient) {
        this.id = id;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.doctor = doctor;
        this.patient = patient;
    }

    public Appointment(Integer id, LocalDate appointmentDate, LocalTime appointmentTime, Doctor doctor, Patient patient, String status) {
        this.id = id;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.doctor = doctor;
        this.patient = patient;
        this.status = status;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}