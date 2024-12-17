package com.library.service;

import com.library.exceptions.BookNotAvailableException;
import com.library.model.Book;
import org.junit.Test;

import static org.junit.Assert.*;

public class LibraryServiceTest {

    @Test
    public void testAddAndBorrowBook() throws BookNotAvailableException {
        LibraryService libraryService = new LibraryService();
        Book book = new Book("1", "1984", "Omar Al-Farouqi");

        libraryService.addBook(book);
        assertNotNull(libraryService.borrowBook("1"));
        assertFalse(book.isAvailable());
    }

    @Test
    public void testBorrowUnavailableBook() {
        LibraryService libraryService = new LibraryService();
        Book book = new Book("1", "1984", "Omar Al-Farouqi");

        libraryService.addBook(book);
        //assertDoesNotThrow(() -> libraryService.borrowBook("1"));
        assertThrows(BookNotAvailableException.class, () -> libraryService.borrowBook("1"));
    }

    @Test
    public void testReturnBook() throws BookNotAvailableException {
        LibraryService libraryService = new LibraryService();
        Book book = new Book("1", "1984", "Omar Al-Farouqi");

        libraryService.addBook(book);
        libraryService.borrowBook("1");
        libraryService.returnBook("1");
        assertTrue(book.isAvailable());
    }
}
