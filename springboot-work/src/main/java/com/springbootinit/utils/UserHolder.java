package com.springbootinit.utils;

import com.springbootinit.model.domain.UrUsers;

public class UserHolder {

    private static final ThreadLocal<UrUsers> userThreadLocal = new ThreadLocal<>();

    public static void setUser(UrUsers user) {
        userThreadLocal.set(user);
    }

    public static UrUsers getUser() {
        return userThreadLocal.get();
    }

    public static Long getUserId() {
        UrUsers user = userThreadLocal.get();
        return user != null ? user.getId() : null;
    }

    public static Integer getUserType() {
        UrUsers user = userThreadLocal.get();
        return user != null ? user.getUserType() : null;
    }

    public static void removeUser() {
        userThreadLocal.remove();
    }
}
