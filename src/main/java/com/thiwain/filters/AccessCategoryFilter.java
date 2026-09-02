package com.thiwain.filters;

import com.thiwain.entity.User;
import com.thiwain.util.SessionUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AccessCategoryFilter implements Filter {

    private String requiredAccess;

    @Override
    public void init(FilterConfig filterConfig) {
        this.requiredAccess = filterConfig.getInitParameter("requiredAccess");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        User user = SessionUtil.getLoggedInUser(req);

        if (user == null) {
            // Shouldn't normally happen if AuthenticationFilter already ran first,
            // but guard against direct/misconfigured access anyway.
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=account_disabled");
            return;
        }

        if (user.hasAccess(requiredAccess)) {
            chain.doFilter(request, response);
        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            req.setAttribute("errorMessage", "You do not have permission to access this page.");
            req.getRequestDispatcher("/access-denied.jsp").forward(req, resp);
        }
    }
}