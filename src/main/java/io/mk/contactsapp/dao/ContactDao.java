package io.mk.contactsapp.dao;

import io.mk.contactsapp.entities.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDao {

    private String jdbcURL = "jdbc:mysql://localhost:3306/contacts?useSSL=false";
    private String jdbcUsername = "user";
    private String jdbcPassword = "user";

    private static final String SELECT_ALL_CONTACTS = "select * from contact;";
    private static final String SELECT_CONTACTS_BY_NAME = "select * from contact where name like ?;";
    private static final String SELECT_CONTACTS_BY_PHONE = "select * from contact where phone like ?;";
    private static final String SELECT_IF_EXISTS = "select * from contact where name = ? or phone = ?;";
    private static final String DELETE_BY_ID = "delete from contact where id = ?;";
    private static final String INSERT = "insert into contact (name, phone) values (?, ?);";

    public ContactDao() {
    }

    private Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }

    public List<Contact> selectAllContacts() {

        List<Contact> contacts = new ArrayList<>();
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CONTACTS);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                contacts.add(new Contact(id, name, phone));
            }
        } catch (SQLException e) {
        }

        return contacts;
    }

    public void delete(int id) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
        }
    }

    public List<Contact> findByName(String name) {
        List<Contact> contacts = new ArrayList<>();
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CONTACTS_BY_NAME);
            preparedStatement.setString(1, "%" + name + "%");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone")));
            }
        } catch (SQLException e) {

        }

        return contacts;
    }

    public List<Contact> findByPhone(String phone) {
        List<Contact> contacts = new ArrayList<>();
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CONTACTS_BY_PHONE);
            preparedStatement.setString(1, "%" + phone + "%");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone")));
            }
        } catch (SQLException e) {

        }

        return contacts;
    }

    public boolean create(String name, String phone) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_IF_EXISTS);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phone);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return false;
            }

            preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phone);
            preparedStatement.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }
}
