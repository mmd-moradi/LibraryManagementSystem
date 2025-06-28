package com.library.dao;

import com.library.model.Book;
import com.library.model.BookStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookDao implements Dao<Book> {
    private final FileStorage<Book> storage;
    private List<Book> books;

    public BookDao() {
        this.storage = new FileStorage<>("books.json", Book.class);
        this.books = storage.loadAll();
        
        // If books list is null, initialize it
        if (this.books == null) {
            this.books = new ArrayList<>();
        }
    }

    @Override
    public void insert(Book book) {
        // Ensure bookId is unique
        if (findById(book.getBookId()).isPresent()) {
            throw new IllegalArgumentException("Book with ID " + book.getBookId() + " already exists");
        }
        books.add(book);
        storage.saveAll(books);
    }

    @Override
    public Optional<Book> findById(String id) {
        return books.stream()
                .filter(book -> book.getBookId().equals(id))
                .findFirst();
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    @Override
    public void update(Book book) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBookId().equals(book.getBookId())) {
                books.set(i, book);
                storage.saveAll(books);
                return;
            }
        }
        throw new IllegalArgumentException("Book with ID " + book.getBookId() + " not found");
    }

    @Override
    public void delete(String id) {
        books.removeIf(book -> book.getBookId().equals(id));
        storage.saveAll(books);
    }

    public List<Book> findByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> findByAuthor(String author) {
        return books.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> findByCategory(String category) {
        return books.stream()
                .filter(book -> book.getCategory().toLowerCase().contains(category.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> findByStatus(BookStatus status) {
        return books.stream()
                .filter(book -> book.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Book> findAvailableBooks() {
        return findByStatus(BookStatus.AVAILABLE);
    }

    public String generateNewBookId() {
        int maxId = 0;
        
        for (Book book : books) {
            if (book.getBookId().startsWith("B")) {
                try {
                    int idNumber = Integer.parseInt(book.getBookId().substring(1));
                    maxId = Math.max(maxId, idNumber);
                } catch (NumberFormatException e) {
                    // Ignore if ID format is invalid
                }
            }
        }
        
        return "B" + String.format("%03d", maxId + 1);
    }

    // Method to add sample data for demonstration
    public void insertSampleData() {
        // Only add sample data if the books list is empty
        if (books.isEmpty()) {
            List<Book> sampleBooks = new ArrayList<>();
            sampleBooks.add(new Book("B001", "O Grande Gatsby", "F. Scott Fitzgerald", "978-0743273565", "Ficção", 1925, BookStatus.AVAILABLE));
            sampleBooks.add(new Book("B002", "O Sol é Para Todos", "Harper Lee", "978-0061120084", "Ficção", 1960, BookStatus.AVAILABLE));
            sampleBooks.add(new Book("B003", "1984", "George Orwell", "978-0451524935", "Distopia", 1949, BookStatus.BORROWED));
            sampleBooks.add(new Book("B004", "Orgulho e Preconceito", "Jane Austen", "978-0141439518", "Clássico", 1813, BookStatus.AVAILABLE));
            sampleBooks.add(new Book("B005", "O Hobbit", "J.R.R. Tolkien", "978-0547928227", "Fantasia", 1937, BookStatus.RESERVED));
            
            for (Book book : sampleBooks) {
                try {
                    insert(book);
                } catch (IllegalArgumentException e) {
                    // Book probably already exists, ignore
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}