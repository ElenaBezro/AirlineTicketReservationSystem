package db;

import properties.PropertiesDB;

import java.sql.*;

public class DatabaseConnector {
    private PropertiesDB properties;
    private static DatabaseConnector instance = null;

    private DatabaseConnector() {
            properties = new PropertiesDB();
    }

    public static DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    public void connect() {
        String url = properties.getUrl();
        String user = properties.getUser();
        String password = properties.getPassword();
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertFlight(int flightID, String airline,
                             String origin, String destination,
                             String departureTime, String arrivalTime,
                             double price, int seatsAvailable) {
        String url = properties.getUrl();
        String user = properties.getUser();
        String password = properties.getPassword();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Statement statement = conn.createStatement();
            String sql = "INSERT INTO Flight (flightID, airline, origin, destination, departureTime, arrivalTime, price, seatsAvailable)\n" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, flightID);
            preparedStatement.setString(2, airline);
            preparedStatement.setString(3, origin);
            preparedStatement.setString(4, destination);
            preparedStatement.setString(5, departureTime);
            preparedStatement.setString(6, arrivalTime);
            preparedStatement.setDouble(7, price);
            preparedStatement.setInt(8, seatsAvailable);

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
}
