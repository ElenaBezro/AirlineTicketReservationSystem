import bookingManagement.Booking;
import customerManagement.Customer;
import db.DatabaseBooking;
import db.DatabaseCustomer;
import db.DatabaseFlight;
import flightManagement.Flight;
import properties.PropertiesDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Main {
    private static final PropertiesDB properties = new PropertiesDB();

    public static void bankTransaction(String url, String user, String password, double amount, int firstAccID, int secondAccID) {

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            connection.setAutoCommit(false);

            String sqlFrom1Withdraw = "UPDATE bank_accounts SET balance = balance - ? WHERE id = ?;";
            String sqlTo2Deposit = "UPDATE bank_accounts SET balance = balance + ? WHERE id = ?;";

            try (PreparedStatement transferFrom1Withdraw = connection.prepareStatement(sqlFrom1Withdraw);
                 PreparedStatement transferTo2Deposit = connection.prepareStatement(sqlTo2Deposit)) {

                transferFrom1Withdraw.setDouble(1, amount);
                transferFrom1Withdraw.setInt(2, firstAccID);

                transferTo2Deposit.setDouble(1, amount);
                transferTo2Deposit.setInt(2, secondAccID);

                int affectedRows1 = transferFrom1Withdraw.executeUpdate();
                int affectedRows2 = transferTo2Deposit.executeUpdate();

                if (affectedRows1 > 0 && affectedRows2 > 0) {
                    connection.commit();
                } else {
                    connection.rollback();
                }
            }
            catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
        e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Connection connection = null;
        String url = properties.getUrl();
        String user = properties.getUser();
        String password = properties.getPassword();
        try {
            connection = DriverManager.getConnection(url, user, password);
            //Delete Flight using transaction
            DatabaseFlight.getInstance().deleteFlight(connection, 1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null || !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        DatabaseFlight.getInstance().connect();
        DatabaseFlight.getInstance().insertFlight(7, "Delta Airlines", "Samara", "Berlin", "2023-11-20 08:00:00", "2023-11-20 12:00:00", 1500.00, 150);
        Flight flight = DatabaseFlight.getInstance().selectFlight(7);
        System.out.println(flight);
        DatabaseFlight.getInstance().updateFlightDepartureTime(7, "2023-11-23 22:00:00");
        Flight changedFlight = DatabaseFlight.getInstance().selectFlight(7);
        System.out.println(changedFlight);

        System.out.println("*******");
        DatabaseCustomer.getInstance().insertCustomer(7, "Elena Bezro", "elena@elena", "030-40-40");
        Customer customer = DatabaseCustomer.getInstance().selectCustomer(7);
        System.out.println(customer);
        DatabaseCustomer.getInstance().updateCustomerName(7, "Maxim");
        Customer changedCustomer = DatabaseCustomer.getInstance().selectCustomer(7);
        System.out.println(changedCustomer);
        //TODO: Delete Customer using transaction
        //DatabaseCustomer.getInstance().deleteCustomer(2);

        System.out.println("*******");
        DatabaseBooking.getInstance().insertBooking(7, 1, 1, "2023-11-22", 2, "Pending");
        Booking booking = DatabaseBooking.getInstance().selectBooking(7);
        System.out.println(booking);
        DatabaseBooking.getInstance().updateBooking(7, "Maxim");
        Booking changedBooking = DatabaseBooking.getInstance().selectBooking(7);
        System.out.println(changedBooking);
        DatabaseBooking.getInstance().deleteBooking(2);

        System.out.println("Faruk task");
        //Exercise 1:
        //Create a Java program using JDBC that performs multiple money transfers between multiple bank accounts within a single transaction.
        //Implement a rollback mechanism to ensure data consistency in case of an error during any part of the transaction.
        //
        //To set up the database with tables and initial data, you can use the following SQL statements as an example:
        //
        //-- Create a bank_accounts table
        //CREATE TABLE bank_accounts (
        //    account_id INT AUTO_INCREMENT PRIMARY KEY,
        //    balance DECIMAL(10, 2)
        //);
        //
        //-- Insert initial account data
        //INSERT INTO bank_accounts (account_id, balance) VALUES
        //    (0, 1000.00),
        //    (0, 1500.00),
        //    (0, 800.00),
        //    (0, 2000.00);
        bankTransaction(url, user, password, 500.00, 1,2);

    }
}
