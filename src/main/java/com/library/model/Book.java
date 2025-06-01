package com.library.model;

import java.time.LocalDate;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String ISBN;
    private String category;
    private int publicationYear;
    private BookStatus status;
    private Student borrowedBy;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    
    public Book() {
        // Default constructor
    }
    
    public Book(String bookId, String title, String author, String ISBN, 
                String category, int publicationYear, BookStatus status) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.category = category;
        this.publicationYear = publicationYear;
        this.status = status;
    }
    
    // Getters and setters
    public String getBookId() {
        return bookId;
    }
    
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getISBN() {
        return ISBN;
    }
    
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public int getPublicationYear() {
        return publicationYear;
    }
    
    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }
    
    public BookStatus getStatus() {
        return status;
    }
    
    public void setStatus(BookStatus status) {
        this.status = status;
    }
    
    public Student getBorrowedBy() {
        return borrowedBy;
    }
    
    public void setBorrowedBy(Student borrowedBy) {
        this.borrowedBy = borrowedBy;
    }
    
    public LocalDate getBorrowDate() {
        return borrowDate;
    }
    
    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE;
    }
    
    // Methods for book operations
    public void borrowBook(Student student, int borrowDays) {
        if (isAvailable()) {
            this.borrowedBy = student;
            this.status = BookStatus.BORROWED;
            this.borrowDate = LocalDate.now();
            this.dueDate = LocalDate.now().plusDays(borrowDays);
        }
    }
    
    public void returnBook() {
        if (this.status == BookStatus.BORROWED) {
            this.borrowedBy = null;
            this.status = BookStatus.AVAILABLE;
            this.borrowDate = null;
            this.dueDate = null;
        }
    }
    
    public void reserveBook() {
        if (isAvailable()) {
            this.status = BookStatus.RESERVED;
        }
    }
    
    public boolean isOverdue() {
        if (this.status == BookStatus.BORROWED && this.dueDate != null) {
            return LocalDate.now().isAfter(this.dueDate);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Book{" +
                "bookId='" + bookId + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", status=" + status +
                '}';
    }
}