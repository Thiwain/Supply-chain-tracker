package com.thiwain.servelets;

import com.thiwain.entity.Task;
import com.thiwain.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/test-db")
public class TestDbServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EntityManager em = JPAUtil.getEntityManager();

        em.getTransaction().begin();
        em.persist(new Task("Test Hibernate connection"));
        em.getTransaction().commit();

        List<Task> tasks = em.createQuery("SELECT t FROM Task t", Task.class).getResultList();
        em.close();

        resp.setContentType("text/plain");
        for (Task t : tasks) {
            resp.getWriter().println(t.getId() + " - " + t.getTitle() + " - done: " + t.isDone());
        }
    }
}