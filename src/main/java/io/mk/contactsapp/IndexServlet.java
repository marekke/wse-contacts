package io.mk.contactsapp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import io.mk.contactsapp.dao.ContactDao;
import io.mk.contactsapp.entities.Contact;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(urlPatterns = "/")
public class IndexServlet extends HttpServlet {
    private String message;
    private ContactDao contactDao;

    public void init() {
        message = "Hello World!";
        contactDao = new ContactDao();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Contact> contacts;
        response.setContentType("text/html");

        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String msg = request.getParameter("msg");

        if (name != null && !name.isEmpty()) {
            contacts = contactDao.findByName(name);
        } else if (phone != null && !phone.isEmpty()) {
            contacts = contactDao.findByPhone(phone);
        } else {
            contacts = contactDao.selectAllContacts();
        }

        PrintWriter out = response.getWriter();
        out.println("<html><body>");

        if (msg != null && !msg.isEmpty()) {
            out.println("<h1 style='color: red'>" + msg + "</h1>");
        }

        out.println(searchFormHTML(name, phone));
        out.println(contactsTableHTML(contacts));
        out.println(createFormHTML());

        out.println("</body></html>");
    }

    private String createFormHTML() {
        return "<form method='POST' action='create'>"
                + "<input type='text' name='name' /> <br />"
                + "<input type='text' name='phone' /> <br />"
                + "<button type='submit'>Create</button>"
                + "</form>";
    }

    public void destroy() {
    }

    private String searchFormHTML(String name, String phone) {
        if (name == null)
            name = "";

        if (phone == null)
            phone = "";

        return "<form method='GET'>"
                + "<input type='text' name='name' value='"+ name + "' />"
                + "<input type='text' name='phone' value='" + phone + "' />"
                + "<button type='submit'>Search</button>"
                + "</form>";
    }

    private String contactsTableHTML(List<Contact> contacts) {
        StringBuilder result = new StringBuilder();
        result.append("<table>");
        result.append("<tr><th>ID</th><th>Name</th><th>Phone</th><th></th><tr>");

        for (Contact c : contacts) {
            result.append("<tr><td>").append(c.getId()).append("</td><td>").append(c.getName()).append("</td><td>").append(c.getPhone()).append("</td><td><a href='delete?id=").append(c.getId()).append("'>Delete</a></td><tr>");
        }

        result.append("</table>");
        return result.toString();
    }
}