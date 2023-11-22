import db.DatabaseConnector;
import properties.PropertiesDB;


public class Main {
    public static void main(String[] args) {
        DatabaseConnector.getInstance().connect();
        DatabaseConnector.getInstance().insertFlight(7, "Delta Airlines", "Samara", "Berlin", "2023-11-20 08:00:00", "2023-11-20 12:00:00", 1500.00, 150);
    }
}
