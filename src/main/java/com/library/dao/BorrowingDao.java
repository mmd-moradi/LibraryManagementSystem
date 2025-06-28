package com.library.dao;

import com.library.model.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class BorrowingDao {
    private final FileStorage<Borrowing> storage;
    private List<Borrowing> borrowings;
    private final BookDao bookDao;
    private final UserDao userDao;
    
    public BorrowingDao() {
        this.storage = new FileStorage<>("borrowings.json", Borrowing.class);
        this.borrowings = storage.loadAll();
        
        // If borrowings list is null, initialize it
        if (this.borrowings == null) {
            this.borrowings = new ArrayList<>();
        }
        
        this.bookDao = new BookDao();
        this.userDao = new UserDao();
    }
    
    public void borrowBook(String bookId, String userId, LocalDate borrowDate, LocalDate dueDate) {
        // Check if book exists and is available
        Optional<Book> bookOpt = bookDao.findById(bookId);
        if (!bookOpt.isPresent()) {
            throw new IllegalArgumentException("Book with ID " + bookId + " not found");
        }
        
        Book book = bookOpt.get();
        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new IllegalArgumentException("Book with ID " + bookId + " is not available");
        }
        
        // Check if user exists
        Optional<User> userOpt = userDao.findById(userId);
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }
        
        // Create new borrowing
        Borrowing borrowing = new Borrowing();
        borrowing.setBorrowingId(generateBorrowingId());
        borrowing.setBookId(bookId);
        borrowing.setUserId(userId);
        borrowing.setBorrowDate(borrowDate);
        borrowing.setDueDate(dueDate);
        borrowing.setStatus("ACTIVE");
        
        // Update book status
        book.setStatus(BookStatus.BORROWED);
        bookDao.update(book);
        
        // Save borrowing
        borrowings.add(borrowing);
        storage.saveAll(borrowings);
    }
    
    public void returnBook(String bookId, LocalDate returnDate) {
        // Find active borrowing for this book
        Optional<Borrowing> borrowingOpt = borrowings.stream()
                .filter(b -> b.getBookId().equals(bookId) && b.getStatus().equals("ACTIVE"))
                .findFirst();
                
        if (!borrowingOpt.isPresent()) {
            throw new IllegalArgumentException("No active borrowing found for book ID " + bookId);
        }
        
        // Update borrowing
        Borrowing borrowing = borrowingOpt.get();
        borrowing.setReturnDate(returnDate);
        borrowing.setStatus("RETURNED");
        
        // Update book status
        Optional<Book> bookOpt = bookDao.findById(bookId);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            book.setStatus(BookStatus.AVAILABLE);
            bookDao.update(book);
        }
        
        // Save changes
        storage.saveAll(borrowings);
    }
    
    public List<Map<String, Object>> findActiveBorrowings() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Borrowing borrowing : borrowings) {
            if (borrowing.getStatus().equals("ACTIVE")) {
                Map<String, Object> item = new HashMap<>();
                item.put("borrowing_id", borrowing.getBorrowingId());
                item.put("book_id", borrowing.getBookId());
                item.put("user_id", borrowing.getUserId());
                
                // Add book and user details
                bookDao.findById(borrowing.getBookId()).ifPresent(book -> {
                    item.put("book_title", book.getTitle());
                });
                
                userDao.findById(borrowing.getUserId()).ifPresent(user -> {
                    item.put("user_name", user.getName());
                });
                
                item.put("borrow_date", borrowing.getBorrowDate());
                item.put("due_date", borrowing.getDueDate());
                
                result.add(item);
            }
        }
        
        return result;
    }
    
    public List<Map<String, Object>> findOverdueBooks() {
        LocalDate today = LocalDate.now();
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Borrowing borrowing : borrowings) {
            if (borrowing.getStatus().equals("ACTIVE") && borrowing.getDueDate().isBefore(today)) {
                Map<String, Object> item = new HashMap<>();
                item.put("borrowing_id", borrowing.getBorrowingId());
                item.put("book_id", borrowing.getBookId());
                item.put("user_id", borrowing.getUserId());
                
                // Add book and user details
                bookDao.findById(borrowing.getBookId()).ifPresent(book -> {
                    item.put("book_title", book.getTitle());
                    item.put("category", book.getCategory());
                });
                
                userDao.findById(borrowing.getUserId()).ifPresent(user -> {
                    item.put("user_name", user.getName());
                });
                
                item.put("borrow_date", borrowing.getBorrowDate());
                item.put("due_date", borrowing.getDueDate());
                
                // Calculate days overdue
                long daysOverdue = ChronoUnit.DAYS.between(borrowing.getDueDate(), today);
                item.put("days_overdue", daysOverdue);
                
                // Calculate late fee (R$0.50 per day)
                double lateFee = daysOverdue * 0.50;
                item.put("late_fee", lateFee);
                
                result.add(item);
            }
        }
        
        return result;
    }
    
    public List<Map<String, Object>> findBorrowingHistory(String userId) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Borrowing borrowing : borrowings) {
            if (borrowing.getUserId().equals(userId)) {
                Map<String, Object> item = new HashMap<>();
                item.put("borrowing_id", borrowing.getBorrowingId());
                item.put("book_id", borrowing.getBookId());
                item.put("status", borrowing.getStatus());
                
                // Add book details
                bookDao.findById(borrowing.getBookId()).ifPresent(book -> {
                    item.put("book_title", book.getTitle());
                });
                
                item.put("borrow_date", borrowing.getBorrowDate());
                item.put("due_date", borrowing.getDueDate());
                item.put("return_date", borrowing.getReturnDate());
                
                result.add(item);
            }
        }
        
        return result;
    }
    
    public List<Book> findBorrowedBooksByUser(String userId) {
        List<String> bookIds = borrowings.stream()
                .filter(b -> b.getUserId().equals(userId) && b.getStatus().equals("ACTIVE"))
                .map(Borrowing::getBookId)
                .collect(Collectors.toList());
                
        List<Book> books = new ArrayList<>();
        for (String bookId : bookIds) {
            bookDao.findById(bookId).ifPresent(books::add);
        }
        
        return books;
    }
    
    public Map<String, Integer> getPopularBooks(int limit, String period) {
        LocalDate startDate;
        LocalDate today = LocalDate.now();
        
        switch (period) {
            case "Últimos 30 dias":
                startDate = today.minusDays(30);
                break;
            case "Últimos 90 dias":
                startDate = today.minusDays(90);
                break;
            case "Último ano":
                startDate = today.minusYears(1);
                break;
            default:
                // All time
                startDate = LocalDate.of(1900, 1, 1);
        }
        
        // Count borrowings per book
        Map<String, Integer> bookCount = new HashMap<>();
        Map<String, String> bookTitles = new HashMap<>();
        
        for (Borrowing borrowing : borrowings) {
            if (!borrowing.getBorrowDate().isBefore(startDate)) {
                String bookId = borrowing.getBookId();
                bookCount.put(bookId, bookCount.getOrDefault(bookId, 0) + 1);
                
                // Get book title
                if (!bookTitles.containsKey(bookId)) {
                    bookDao.findById(bookId).ifPresent(book -> {
                        bookTitles.put(bookId, book.getTitle());
                    });
                }
            }
        }
        
        // Convert to book title -> count map
        Map<String, Integer> popularBooks = new HashMap<>();
        for (Map.Entry<String, Integer> entry : bookCount.entrySet()) {
            String title = bookTitles.getOrDefault(entry.getKey(), "Unknown Book");
            popularBooks.put(title, entry.getValue());
        }
        
        // Sort and limit results
        return popularBooks.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                    Map.Entry::getKey, 
                    Map.Entry::getValue, 
                    (e1, e2) -> e1, 
                    LinkedHashMap::new
                ));
    }
    
    public Map<String, Integer> getPopularCategories() {
        // Count borrowings per category
        Map<String, Integer> categoryCount = new HashMap<>();
        
        for (Borrowing borrowing : borrowings) {
            String bookId = borrowing.getBookId();
            bookDao.findById(bookId).ifPresent(book -> {
                String category = book.getCategory();
                if (category != null && !category.isEmpty()) {
                    categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
                }
            });
        }
        
        // Sort categories by count
        return categoryCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                    Map.Entry::getKey, 
                    Map.Entry::getValue, 
                    (e1, e2) -> e1, 
                    LinkedHashMap::new
                ));
    }
    
    private String generateBorrowingId() {
        int maxId = 0;
        
        for (Borrowing borrowing : borrowings) {
            String id = borrowing.getBorrowingId();
            if (id.startsWith("BR")) {
                try {
                    int idNumber = Integer.parseInt(id.substring(2));
                    maxId = Math.max(maxId, idNumber);
                } catch (NumberFormatException e) {
                    // Ignore if ID format is invalid
                }
            }
        }
        
        return "BR" + String.format("%03d", maxId + 1);
    }
    
    // Method to add sample borrowings
    public void insertSampleBorrowings() {
        // Only add sample data if there are no borrowings
        if (borrowings.isEmpty()) {
            List<Book> books;
            List<Student> students;
            
            books = bookDao.findAll();
            students = userDao.findAllStudents();
            
            if (books.isEmpty() || students.isEmpty()) {
                return;
            }
            
            LocalDate today = LocalDate.now();
            
            // Create sample borrowings
            if (books.size() > 0 && students.size() > 0) {
                try {
                    // Book 1 borrowed by student 1
                    borrowBook(
                        books.get(0).getBookId(),
                        students.get(0).getUserId(),
                        today.minusDays(20),
                        today.minusDays(6)
                    );
                    
                    // Book 2 borrowed by student 1
                    if (books.size() > 1) {
                        borrowBook(
                            books.get(1).getBookId(),
                            students.get(0).getUserId(),
                            today.minusDays(15),
                            today.minusDays(1)
                        );
                    }
                    
                    // Book 3 borrowed by student 1 (overdue)
                    if (books.size() > 2) {
                        borrowBook(
                            books.get(2).getBookId(),
                            students.get(0).getUserId(),
                            today.minusDays(25),
                            today.minusDays(11)
                        );
                    }
                } catch (IllegalArgumentException e) {
                    // Borrowing might already exist or book status has changed
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}