package com.library.model;

public class Student extends User {
    private String studentId;
    private String department;
    
    public Student() {
    }
    
    public Student(String userId, String name, String email, String phoneNumber, 
                  String address, String studentId, String department) {
        super(userId, name, email, phoneNumber, address);
        this.studentId = studentId;
        this.department = department;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    @Override
    public String getUserType() {
        return "Student";
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "name='" + getName() + '\'' +
                ", studentId='" + studentId + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}