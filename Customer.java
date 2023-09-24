import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Customer implements Serializable {
    private final String name;
    private final String email;
    private final ArrayList<History> allBookings;
    private final String FILENAME;
    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
        allBookings = new ArrayList<>();
        FILENAME = name + "_Bookings.txt";
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getFILENAME() {
        return FILENAME;
    }
    // Adding booking to all other bookings of that customer
    public void addBooking(Room room, LocalDate startDate, LocalDate endDate, double roomBill) {
        History history = new History(room, this, startDate, endDate, roomBill);
        allBookings.add(history);
    }
    // Displaying all bookings of customer
    public void displayAllBookings(){
        if(allBookings.isEmpty()) {
            System.out.println("There have been no bookings for " + name + " customer");
        }
        for (History booking : allBookings) {
            System.out.println(booking.customerHistory());
        }
    }
    // Writing booking history of this customer in his/her file
    public void writingInFIle(FileWriter writer) throws IOException {
        for (History history : allBookings) {
            writer.write(history.customerHistory() + "\n");
            writer.flush();
        }
    }
    public String toString(){
        return "Name: " + name + ", email: "+ email;
    }
}
