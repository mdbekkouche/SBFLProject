package com.library.service;

import com.library.model.Book;
import com.library.exceptions.BookNotAvailableException;

import java.util.HashMap;
import java.util.Map;

public class LibraryService {
    private Map<String, Book> books;

    public LibraryService() {
        books = new HashMap<>();
    }

    public void addBook(Book book) {
        books.put(book.getId(), book);
    }

    public Book borrowBook(String bookId) throws BookNotAvailableException {
        Book book = books.get(bookId);
        if (book == null || !book.isAvailable()) {
            throw new BookNotAvailableException("Book is not available.");
        }
        book.setAvailable(false);
        return book;
    }

    public void returnBook(String bookId) {
        Book book = books.get(bookId);
        if (book != null) {
            book.setAvailable(true);
        }
    }
}
