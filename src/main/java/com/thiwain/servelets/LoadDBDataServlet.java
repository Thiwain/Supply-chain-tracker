package com.thiwain.servelets;

import com.thiwain.entity.Country;
import com.thiwain.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/load-data")
public class LoadDBDataServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EntityManager em = JPAUtil.getEntityManager();
        resp.setContentType("text/plain");

        try {
            Long count = em.createQuery("SELECT COUNT(c) FROM Country c", Long.class)
                    .getSingleResult();

            if (count != null && count > 0) {
                resp.getWriter().println("Countries already exist (" + count + " rows). Skipping seed.");
                return;
            }

            Map<Integer, String> countries = buildCountryList();

            em.getTransaction().begin();
            for (Map.Entry<Integer, String> entry : countries.entrySet()) {
                Country country = new Country();
                country.setId(entry.getKey());
                country.setName(entry.getValue());
                em.persist(country);
            }
            em.getTransaction().commit();

            resp.getWriter().println("Seeded " + countries.size() + " countries.");

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            resp.getWriter().println("Error seeding countries: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private Map<Integer, String> buildCountryList() {
        Map<Integer, String> countries = new LinkedHashMap<>();
        countries.put(1, "United States");
        countries.put(2, "Canada");
        countries.put(3, "United Kingdom");
        countries.put(4, "Germany");
        countries.put(5, "France");
        countries.put(6, "Italy");
        countries.put(7, "Spain");
        countries.put(8, "Netherlands");
        countries.put(9, "Belgium");
        countries.put(10, "Switzerland");
        countries.put(11, "Sweden");
        countries.put(12, "Norway");
        countries.put(13, "Denmark");
        countries.put(14, "Ireland");
        countries.put(15, "Poland");
        countries.put(16, "Australia");
        countries.put(17, "New Zealand");
        countries.put(18, "China");
        countries.put(19, "Japan");
        countries.put(20, "South Korea");
        countries.put(21, "India");
        countries.put(22, "Singapore");
        countries.put(23, "Malaysia");
        countries.put(24, "Thailand");
        countries.put(25, "Indonesia");
        countries.put(26, "Philippines");
        countries.put(27, "Vietnam");
        countries.put(28, "United Arab Emirates");
        countries.put(29, "Saudi Arabia");
        countries.put(30, "Qatar");
        countries.put(31, "South Africa");
        countries.put(32, "Nigeria");
        countries.put(33, "Kenya");
        countries.put(34, "Egypt");
        countries.put(35, "Brazil");
        countries.put(36, "Mexico");
        countries.put(37, "Argentina");
        countries.put(38, "Chile");
        countries.put(39, "Colombia");
        countries.put(40, "Sri Lanka");
        return countries;
    }

    
}