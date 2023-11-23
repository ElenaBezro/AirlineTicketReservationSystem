package flightManagement;

public class Flight {
    private int flightID;
    private String airline;
    private String origin;
    private  String destination;
    private String departureTime;
    private  String arrivalTime;
    private double price;
    private  int seatsAvailable;

    public Flight(int flightID, String airline, String origin, String destination, String departureTime, String arrivalTime, double price, int seatsAvailable) {
        this.flightID = flightID;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.seatsAvailable = seatsAvailable;
    }

    public Flight() {
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightID=" + flightID +
                ", airline='" + airline + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", price=" + price +
                ", seatsAvailable=" + seatsAvailable +
                '}';
    }
}
