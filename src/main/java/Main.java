import bookingManagement.Booking;
import customerManagement.Customer;
import db.DatabaseBooking;
import db.DatabaseCustomer;
import db.DatabaseFlight;
import flightManagement.Flight;

import java.sql.Connection;
import java.sql.DriverManager;


public class Main {
    public static void main(String[] args) {

        DatabaseFlight.getInstance().connect();
        //DatabaseFlight.getInstance().insertFlight(7, "Delta Airlines", "Samara", "Berlin", "2023-11-20 08:00:00", "2023-11-20 12:00:00", 1500.00, 150);
        Flight flight = DatabaseFlight.getInstance().selectFlight(7);
        System.out.println(flight);
        DatabaseFlight.getInstance().updateFlightDepartureTime(7, "2023-11-23 22:00:00");
        Flight changedFlight = DatabaseFlight.getInstance().selectFlight(7);
        System.out.println(changedFlight);
        DatabaseFlight.getInstance().deleteFlight(2);

        System.out.println("*******");
        DatabaseCustomer.getInstance().insertCustomer(7, "Elena Bezro", "elena@elena", "030-40-40");
        Customer customer = DatabaseCustomer.getInstance().selectCustomer(7);
        System.out.println(customer);
        DatabaseCustomer.getInstance().updateCustomerName(7, "Maxim");
        Customer changedCustomer = DatabaseCustomer.getInstance().selectCustomer(7);
        System.out.println(changedCustomer);
        //DatabaseCustomer.getInstance().deleteCustomer(2);

        System.out.println("*******");
        DatabaseBooking.getInstance().insertBooking(7, 1, 1, "2023-11-22", 2, "Pending");
        Booking booking = DatabaseBooking.getInstance().selectBooking(7);
        System.out.println(booking);
        DatabaseBooking.getInstance().updateBooking(7, "Maxim");
        Booking changedBooking = DatabaseBooking.getInstance().selectBooking(7);
        System.out.println(changedBooking);
        //DatabaseCustomer.getInstance().deleteCustomer(2);
    }
}
