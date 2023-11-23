package db;

import customerManagement.Customer;
import flightManagement.Flight;
import properties.PropertiesDB;

import java.sql.*;

public class DatabaseCustomer {
    private PropertiesDB properties;
    private static DatabaseCustomer instance = null;

    private DatabaseCustomer() {
        properties = new PropertiesDB();
    }

    public static DatabaseCustomer getInstance() {
        if (instance == null) {
            instance = new DatabaseCustomer();
        }
        return instance;
    }

    public void insertCustomer(int customerID, String name,
                             String email, String phone) {
        String url = properties.getUrl();
        String user = properties.getUser();
        String password = properties.getPassword();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            //Statement statement = conn.createStatement();
            String sql = "INSERT INTO Customers (customerID, name, email, phone)\n" +
                    "VALUES (?, ?, ?, ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, customerID);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phone);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Record inserted successfully!");
            } else {
                System.out.println("Failed to insert the record.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Customer selectCustomer(int id) {
        String url = properties.getUrl();
        String user = properties.getUser();
        String password = properties.getPassword();
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM Customers WHERE customerID = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);

                try (ResultSet result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        Customer customer = new Customer();
                        customer.setCustomerID(result.getInt("customerID"));
                        customer.setName(result.getString("name"));
                        customer.setEmail(result.getString("email"));
                        customer.setPhone(result.getString("phone"));
                        return customer;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateCustomerName(int id, String name) {
        String url = properties.getUrl();
        String user = properties.getUser();
        String password = properties.getPassword();
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "UPDATE Customers SET name = ? WHERE customerID = ?;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, id);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Record updated successfully!");
                } else {
                    System.out.println("Failed to update the record.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCustomer(int id) {
        String url = properties.getUrl();
        String user = properties.getUser();
        String password = properties.getPassword();
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "DELETE FROM Customers WHERE customerID = ?;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Record deleted successfully!");
                } else {
                    System.out.println("Failed to delete the record.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
