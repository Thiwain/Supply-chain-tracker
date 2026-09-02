package com.thiwain.util;

import com.thiwain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    public static final String SESSION_USER_KEY = "loggedInUser";

    public static User getLoggedInUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false); // false = don't create a new one just to check
        if (session == null) return null;
        return (User) session.getAttribute(SESSION_USER_KEY);
    }

    public static boolean isLoggedIn(HttpServletRequest req) {
        return getLoggedInUser(req) != null;
    }

    public static void setLoggedInUser(HttpServletRequest req, User user) {
        req.getSession(true).setAttribute(SESSION_USER_KEY, user);
    }

    public static void clearSession(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();
    }
}