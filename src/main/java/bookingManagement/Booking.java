package bookingManagement;

public class Booking {
    private int bookingID;
    private int customerID;
    private int flightID;
    private String bookingDate;
    private int numberOfPassengers;
    private  String status;

    public Booking(int bookingID, int customerID, int flightID, String bookingDate, int numberOfPassengers, String status) {
        this.bookingID = bookingID;
        this.customerID = customerID;
        this.flightID = flightID;
        this.bookingDate = bookingDate;
        this.numberOfPassengers = numberOfPassengers;
        this.status = status;
    }

    public Booking() {
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingID=" + bookingID +
                ", customerID=" + customerID +
                ", flightID=" + flightID +
                ", bookingDate='" + bookingDate + '\'' +
                ", numberOfPassengers=" + numberOfPassengers +
                ", status='" + status + '\'' +
                '}';
    }
}
