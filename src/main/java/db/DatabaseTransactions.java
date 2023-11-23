package db;

import properties.PropertiesDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseTransactions {
    private PropertiesDB properties = new PropertiesDB();

    public void testTransaktion(int customerID, String newName) throws SQLException {
        String url = properties.getUrl();
        String user = properties.getUser();
        String password = properties.getPassword();
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            try {
                connection.setAutoCommit(false);

                String firstSQL = "UPDATE Customers SET name = ? WHERE customerID = ?;";
                PreparedStatement firstPrS = connection.prepareStatement(firstSQL);
                firstPrS.setString(1, newName);
                firstPrS.setInt(2, customerID);
                firstPrS.executeUpdate();

                String secondSQL = "UPDATE Customers SET name = ? WHERE customerID = ?;";
                PreparedStatement secondPrS = connection.prepareStatement(secondSQL);
                secondPrS.setString(1, newName);
                secondPrS.setInt(2, customerID);
                secondPrS.executeUpdate();

                connection.commit();
            } catch (Exception e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
