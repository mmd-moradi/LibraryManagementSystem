package com.library.dao;

import com.library.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserDao implements Dao<User> {
    private final FileStorage<User> storage;
    private List<User> users;

    public UserDao() {
        this.storage = new FileStorage<>("users.json", User.class);
        this.users = storage.loadAll();
        
        // If users list is null, initialize it
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
    }

    @Override
    public void insert(User user) {
        // Ensure userId is unique
        if (findById(user.getUserId()).isPresent()) {
            throw new IllegalArgumentException("User with ID " + user.getUserId() + " already exists");
        }
        
        // Check username uniqueness if account exists
        if (user.getAccount() != null) {
            String username = user.getAccount().getUsername();
            boolean usernameExists = users.stream()
                    .filter(u -> u.getAccount() != null)
                    .anyMatch(u -> u.getAccount().getUsername().equals(username));
                    
            if (usernameExists) {
                throw new IllegalArgumentException("Username " + username + " already exists");
            }
        }
        
        users.add(user);
        storage.saveAll(users);
    }

    @Override
    public Optional<User> findById(String id) {
        return users.stream()
                .filter(user -> user.getUserId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public void update(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(user.getUserId())) {
                // Check username uniqueness if username changed
                if (user.getAccount() != null) {
                    String newUsername = user.getAccount().getUsername();
                    boolean usernameExists = users.stream()
                            .filter(u -> !u.getUserId().equals(user.getUserId()) && u.getAccount() != null)
                            .anyMatch(u -> u.getAccount().getUsername().equals(newUsername));
                            
                    if (usernameExists) {
                        throw new IllegalArgumentException("Username " + newUsername + " already exists");
                    }
                }
                
                users.set(i, user);
                storage.saveAll(users);
                return;
            }
        }
        throw new IllegalArgumentException("User with ID " + user.getUserId() + " not found");
    }

    @Override
    public void delete(String id) {
        users.removeIf(user -> user.getUserId().equals(id));
        storage.saveAll(users);
    }

    public List<User> findByType(String userType) {
        return users.stream()
                .filter(user -> user.getUserType().equals(userType))
                .collect(Collectors.toList());
    }

    public List<Student> findAllStudents() {
        return users.stream()
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .collect(Collectors.toList());
    }

    public List<Employee> findAllEmployees() {
        return users.stream()
                .filter(user -> user instanceof Employee)
                .map(user -> (Employee) user)
                .collect(Collectors.toList());
    }

    public User findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getAccount() != null && 
                       user.getAccount().getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public String generateNewUserId() {
        int maxId = 0;
        
        for (User user : users) {
            if (user.getUserId().startsWith("U")) {
                try {
                    int idNumber = Integer.parseInt(user.getUserId().substring(1));
                    maxId = Math.max(maxId, idNumber);
                } catch (NumberFormatException e) {
                    // Ignore if ID format is invalid
                }
            }
        }
        
        return "U" + String.format("%03d", maxId + 1);
    }

    public String generateNewStudentId() {
        int maxId = 0;
        
        for (User user : users) {
            if (user instanceof Student) {
                Student student = (Student) user;
                if (student.getStudentId().startsWith("S")) {
                    try {
                        int idNumber = Integer.parseInt(student.getStudentId().substring(1));
                        maxId = Math.max(maxId, idNumber);
                    } catch (NumberFormatException e) {
                        // Ignore if ID format is invalid
                    }
                }
            }
        }
        
        return "S" + String.format("%03d", maxId + 1);
    }

    public String generateNewEmployeeId() {
        int maxId = 0;
        
        for (User user : users) {
            if (user instanceof Employee) {
                Employee employee = (Employee) user;
                if (employee.getEmployeeId().startsWith("E")) {
                    try {
                        int idNumber = Integer.parseInt(employee.getEmployeeId().substring(1));
                        maxId = Math.max(maxId, idNumber);
                    } catch (NumberFormatException e) {
                        // Ignore if ID format is invalid
                    }
                }
            }
        }
        
        return "E" + String.format("%03d", maxId + 1);
    }
    
    // Method to add sample data for demonstration
    public void insertSampleData() {
        // Only add sample data if the users list is empty
        if (users.isEmpty()) {
            // Create a student with an account
            Student student = new Student();
            student.setUserId("U001");
            student.setStudentId("S001");
            student.setName("João Silva");
            student.setEmail("joao@exemplo.com");
            student.setPhoneNumber("(11) 98765-4321");
            student.setAddress("Av. Paulista, 123 - São Paulo, SP");
            student.setDepartment("Ciência da Computação");
            
            Account studentAccount = new Account();
            studentAccount.setAccountId("A001");
            studentAccount.setUsername("joao");
            studentAccount.setPassword("senha123"); 
            studentAccount.setStatus(AccountStatus.ACTIVE);
            studentAccount.setLastLogin(LocalDateTime.now().minusDays(2));
            student.setAccount(studentAccount);
            
            // Create an employee (librarian) with an account
            Employee employee = new Employee();
            employee.setUserId("U002");
            employee.setEmployeeId("E001");
            employee.setName("Maria Oliveira");
            employee.setEmail("maria@exemplo.com");
            employee.setPhoneNumber("(11) 91234-5678");
            employee.setAddress("Rua Augusta, 456 - São Paulo, SP");
            employee.setPosition("Bibliotecário");
            employee.setSalary(3500.00);
            employee.setDateHired(LocalDate.now().minusYears(2));
            
            Account employeeAccount = new Account();
            employeeAccount.setAccountId("A002");
            employeeAccount.setUsername("admin");
            employeeAccount.setPassword("admin123");
            employeeAccount.setStatus(AccountStatus.ACTIVE);
            employeeAccount.setLastLogin(LocalDateTime.now().minusDays(1));
            employee.setAccount(employeeAccount);
            
            // Insert in the database
            try {
                insert(student);
                insert(employee);
            } catch (IllegalArgumentException e) {
                // User probably already exists
                System.out.println(e.getMessage());
            }
        }
    }
}