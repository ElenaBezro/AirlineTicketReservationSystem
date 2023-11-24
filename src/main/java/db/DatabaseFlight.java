package db;

import flightManagement.Flight;
import properties.PropertiesDB;

import java.sql.*;

public class DatabaseFlight {
    private PropertiesDB properties;
    private static DatabaseFlight instance = null;

    private DatabaseFlight() {
        properties = new PropertiesDB();
    }

    public static DatabaseFlight getInstance() {
        if (instance == null) {
            instance = new DatabaseFlight();
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
            //Statement statement = conn.createStatement();
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

    public Flight selectFlight(int id) {
        String url = properties.getUrl();
        String user = properties.getUser();
        String password = properties.getPassword();
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM Flight WHERE flightID = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);

                try (ResultSet result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        Flight flight = new Flight();
                        flight.setFlightID(result.getInt("flightID"));
                        flight.setAirline(result.getString("airline"));
                        flight.setOrigin(result.getString("origin"));
                        flight.setDestination(result.getString("destination"));
                        flight.setDepartureTime(result.getString("departureTime"));
                        flight.setArrivalTime(result.getString("arrivalTime"));
                        flight.setPrice(result.getDouble("price"));
                        flight.setSeatsAvailable(result.getInt("seatsAvailable"));
                        return flight;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getSeatsAvailable(Connection conn, int id) {
        try {
            String sql = "SELECT seatsAvailable FROM Flight WHERE flightID = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);

                try (ResultSet result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        return result.getInt("seatsAvailable");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updateFlightDepartureTime(int id, String departureTime) {
        String url = properties.getUrl();
        String user = properties.getUser();
        String password = properties.getPassword();
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "UPDATE Flight SET departureTime = ? WHERE flightID = ?;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, departureTime);
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

    public void updateFlightColumn(Connection conn, int id, String columnName, String value) throws SQLException {

        try {
            String sql = "UPDATE Flight SET " + columnName + " = ? WHERE flightID = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

                if (columnName.equals("seatsAvailable")) {
                    preparedStatement.setInt(1, Integer.parseInt(value));
                } else {
                    preparedStatement.setString(1, columnName);
                }
                preparedStatement.setInt(2, id);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Record updated successfully!");
                } else {
                    throw new SQLException("Failed to update the record.");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to update the record.");
        }
    }

    public void updateAvailableSeats(Connection conn, int flightID, int numberOfPassengers) throws SQLException {

        try {
            String sql = "UPDATE Flight SET seatsAvailable = seatsAvailable - ? WHERE flightID = ? AND seatsAvailable >= ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

                preparedStatement.setInt(1, numberOfPassengers);
                preparedStatement.setInt(2, flightID);
                preparedStatement.setInt(3, numberOfPassengers);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Flight record updated successfully!");
                } else {
                    throw new SQLException("Failed to update the Flight record.");
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public void deleteFlight(Connection connection, int id) throws SQLException {
        try {
            connection.setAutoCommit(false);

            String parentColumnName = "flightID";
            int bookingAffectedRows = DatabaseBooking.getInstance().deleteBookingCascade(connection, id, parentColumnName);

            String sql = "DELETE FROM Flight WHERE flightID = ?;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    connection.commit();

                    if (bookingAffectedRows > 0) {
                        System.out.println("Booking Record deleted successfully!");
                    }

                    System.out.println("Flight Record deleted successfully!");
                } else {
                    System.out.println("Failed to delete the Flight record.");
                    connection.rollback();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        }
    }
}
