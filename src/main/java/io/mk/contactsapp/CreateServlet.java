package io.mk.contactsapp;

import io.mk.contactsapp.dao.ContactDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/create")
public class CreateServlet extends HttpServlet {
    private ContactDao contactDao;

    public void init() {
        contactDao = new ContactDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");

        if ((name == null || name.isEmpty()) || (phone == null || phone.isEmpty())) {
            response.sendRedirect(request.getContextPath() + "/?msg=" + "Name and phone fields are required.");
            return;
        }

        if (contactDao.create(name, phone)) {
            response.sendRedirect(request.getContextPath() + "/");
        } else {
            response.sendRedirect(request.getContextPath() + "/?msg=" + "Incorrect data was sent");
        }
    }
}
