package db;

import bookingManagement.Booking;
import properties.PropertiesDB;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseBooking {
    private PropertiesDB properties;
    private static DatabaseBooking instance = null;

    private DatabaseBooking() {
        properties = new PropertiesDB();
    }

    public static DatabaseBooking getInstance() {
        if (instance == null) {
            instance = new DatabaseBooking();
        }
        return instance;
    }

    //Create a transactional method for booking a flight. This should include
    //inserting a record into the Bookings table and updating the SeatsAvailable
    //in the Flights table.

    public void bookingFlight(Connection conn, int bookingID, int customerID,
                              int flightID,
                              int numberOfPassengers, String status) {

        try {
            //TODO:  instead og checking availableSeats,
            // Use a single SQL UPDATE statement with a WHERE clause to check and update
            //    String sqlUpdate = "UPDATE Flight SET " + columnName + " = " + columnName + " - ? WHERE flightID = ? AND " + columnName + " >= ?";
            //    try (PreparedStatement preparedStatement = conn.prepareStatement(sqlUpdate)) {
            //        preparedStatement.setInt(1, numberOfPassengers);
            //        preparedStatement.setInt(2, flightID);
            //        preparedStatement.setInt(3, numberOfPassengers);
            int availableSeats = DatabaseFlight.getInstance().getSeatsAvailable(conn, flightID);
            if (availableSeats < numberOfPassengers) {
                throw new SQLException("Not enough seats");
            }
            conn.setAutoCommit(false);
            try {

                String bookingDate = LocalDate.now().toString();
                insertBookingInTransaction(conn, bookingID, customerID, flightID, bookingDate, numberOfPassengers, status);

                String newSeatsAvaliable = String.valueOf(availableSeats - numberOfPassengers);
                String columnName = "seatsAvailable";
                DatabaseFlight.getInstance().updateFlightInTransaction(conn, flightID, columnName, newSeatsAvaliable);

                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertBookingInTransaction(Connection conn, int bookingID, int customerID,
                                           int flightID, String bookingDate,
                                           int numberOfPassengers, String status) throws SQLException {

        try {
            String sql = "INSERT INTO Bookings (bookingID, customerID, flightID, bookingDate, numberOfPassengers, status)\n" +
                    "VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, bookingID);
            preparedStatement.setInt(2, customerID);
            preparedStatement.setInt(3, flightID);
            preparedStatement.setString(4, bookingDate);
            preparedStatement.setInt(5, numberOfPassengers);
            preparedStatement.setString(6, status);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Record is ready to commit!");
            } else {
                throw new SQLException("Failed to insert the Booking record.");
            }

        } catch (SQLException e) {
            throw new SQLException("Failed to insert the Booking record.");
        }
    }

    public void insertBooking(int bookingID, int customerID,
                              int flightID, String bookingDate,
                              int numberOfPassengers, String status) {
        String url = properties.getUrl();
        String user = properties.getUser();
        String password = properties.getPassword();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO Bookings (bookingID, customerID, flightID, bookingDate, numberOfPassengers, status)\n" +
                    "VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, bookingID);
            preparedStatement.setInt(2, customerID);
            preparedStatement.setInt(3, flightID);
            preparedStatement.setString(4, bookingDate);
            preparedStatement.setInt(5, numberOfPassengers);
            preparedStatement.setString(6, status);
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

    public Booking selectBooking(int id) {
        String url = properties.getUrl();
        String user = properties.getUser();
        String password = properties.getPassword();
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM Bookings WHERE bookingID = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);

                try (ResultSet result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        Booking booking = new Booking();
                        booking.setBookingID(result.getInt("bookingID"));
                        booking.setCustomerID(result.getInt("customerID"));
                        booking.setFlightID(result.getInt("flightID"));
                        booking.setBookingDate(result.getString("bookingDate"));
                        booking.setNumberOfPassengers(result.getInt("numberOfPassengers"));
                        booking.setStatus(result.getString("status"));
                        return booking;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateBooking(int id, String status) {
        String url = properties.getUrl();
        String user = properties.getUser();
        String password = properties.getPassword();
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "UPDATE Bookings SET status = ? WHERE bookingID = ?;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, status);
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

    public void deleteBooking(int id) {
        String url = properties.getUrl();
        String user = properties.getUser();
        String password = properties.getPassword();
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "DELETE FROM Bookings WHERE bookingID = ?;";
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

    public int deleteBookingCascade(Connection connection, int parentID, String parentColumnName) throws SQLException {
        String sql = "DELETE FROM Bookings WHERE " + parentColumnName + " = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, parentID);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Booking record is ready to delete!");
            } else {
                System.out.println("There is no Booking with " + parentColumnName + " = " + parentID + ".");
            }
            return affectedRows;
        } catch (SQLException e) {
            throw new SQLException("Failed to delete the Booking record.");
        }
    }
}
