package com.library.service;

import com.library.model.Book;
import com.library.model.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserServiceTest {

    @Test
    public void testAddUser() {
        UserService userService = new UserService();
        User user = new User("1", "Adnan Al-Haddad");

        userService.addUser(user);
        assertNotNull(userService.getUser("1"));
    }

    @Test
    public void testBorrowBook() {
        UserService userService = new UserService();
        User user = new User("1", "Adnan Al-Haddad");
        Book book = new Book("1", "1984", "Omar Al-Farouqi");

        userService.addUser(user);
        userService.borrowBook("1", book);

        assertEquals(1, userService.getUser("1").getBorrowedBooks().size());
        assertEquals("1984", userService.getUser("1").getBorrowedBooks().get(0).getTitle());
    }

    @Test
    public void testReturnBook() {
        UserService userService = new UserService();
        User user = new User("1", "Adnan Al-Haddad");
        Book book = new Book("1", "1984", "Omar Al-Farouqi");

        userService.addUser(user);
        userService.borrowBook("1", book);
        userService.returnBook("1", book);

        assertTrue(userService.getUser("1").getBorrowedBooks().isEmpty());
    }
}
