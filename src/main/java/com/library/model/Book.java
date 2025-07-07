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
    
    public Book() {
        
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
    
    
    
    public void reserveBook() {
        if (isAvailable()) {
            this.status = BookStatus.RESERVED;
        }
    }
    
    
    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE;
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