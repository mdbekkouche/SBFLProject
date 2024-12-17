package com.library.service;

import com.library.model.Book;
import com.library.model.User;

import java.util.HashMap;
import java.util.Map;



public class UserService {
    private Map<String, User> users;

    public UserService() {
        users = new HashMap<>();
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public void borrowBook(String userId, Book book) {
        User user = users.get(userId);
        if (user != null) {
            user.borrowBook(book);
        }
    }

    public void returnBook(String userId, Book book) {
        User user = users.get(userId);
        if (user != null) {
            user.returnBook(book);
        }
    }
}
