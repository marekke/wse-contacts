package io.mk.contactsapp;

import io.mk.contactsapp.dao.ContactDao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/delete")
public class DeleteServlet extends HttpServlet {
    private ContactDao contactDao;

    public void init() {
        contactDao = new ContactDao();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        contactDao.delete(Integer.parseInt(id));
        response.sendRedirect(request.getContextPath() + "/");
    }
}
