package tw.edu.fju.miniclinic.controller;

// This is a Data Transfer Object (DTO) to handle incoming request bodies
// It helps to decouple the API request format from the internal Doctor entity structure.
public class DoctorRequest {
    private String id; // Corresponds to "id" in the request body
    private String name;
    private String departmentName; // Corresponds to "departmentName" in the request body
    // private String specialty; // Not needed if Doctor entity doesn't have it

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}