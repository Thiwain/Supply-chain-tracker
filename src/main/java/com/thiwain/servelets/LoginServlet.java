package com.thiwain.servelets;

import com.thiwain.entity.User;
import com.thiwain.util.JPAUtil;
import com.thiwain.util.SessionUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/do-login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        EntityManager em = JPAUtil.getEntityManager();

        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();

            if (!Boolean.TRUE.equals(user.getIsActive())) {
                resp.sendRedirect(req.getContextPath() + "/login.jsp?error=disabled");
                return;
            }

            if (BCrypt.checkpw(password, user.getPasswordHash())) {
                user.setLastLogin(LocalDateTime.now());
                em.getTransaction().begin();
                em.merge(user);
                em.getTransaction().commit();

                SessionUtil.setLoggedInUser(req, user);
                resp.sendRedirect(req.getContextPath() + "/");
            } else {
                resp.sendRedirect(req.getContextPath() + "/login.jsp?error=1");
            }

        } catch (NoResultException e) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=1");
        } finally {
            em.close();
        }
    }
}