package se.yrgo.user_service.rest;

import se.yrgo.user_service.domain.User;

import java.util.*;

public class UserList {
    private List<User> users;

    public UserList() {}

    public UserList(List<User> users) {
        this.users = users;
    }

    public List<User> getUserList() {
        return users;
    }

    public void setUserList(List<User> users) {
        this.users = users;
    }
}
