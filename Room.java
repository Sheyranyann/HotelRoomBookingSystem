import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Room implements Serializable{
    private static int I = 0;
    private final int roomID;
    private final RoomType roomType;
    private final ArrayList<History> allBookings;
    private final ArrayList<History> upcomingBookings;
    public Room(RoomType roomType) {
        this.roomType = roomType;
        roomID = ++I;
        allBookings = new ArrayList<>();
        upcomingBookings = new ArrayList<>();
    }
    // Adding booking to all bookings of room
    public History addBooking(Customer customer, LocalDate startDate, LocalDate endDate) {
        History history = new History(this, customer, startDate, endDate, calculateBillPerDay(startDate, endDate));
        allBookings.add(history);
        upcomingBookings.add(history);
        return history;
    }
    // Displaying all information about room's bookings
    public void displayAllBookings() {
        if (allBookings.isEmpty()) {
            System.out.println("There are no bookings of N" + roomID + " room");
        }
        for (History history : allBookings) {
            System.out.println(history.hotelHistory());
        }
    }
    public void displayUpcomingBookings() {
        if (upcomingBookings.isEmpty()) {
            System.out.println("There are no upcoming bookings of N" + roomID + " room");
        }
        for (History history : upcomingBookings) {
            System.out.println(history.hotelHistory());
        }
    }
    public RoomType getRoomType() {
        return roomType;
    }
    public int getRoomID() {
        return roomID;
    }
    // Checking if room will be available during provided period
    public boolean isAvailableDuringPeriod(LocalDate startDate, LocalDate endDate) {
        upcomingBookings.removeIf(booking -> ChronoUnit.DAYS.between(booking.getEndDate(), LocalDate.now()) >= 0);
        for (History booking : upcomingBookings) {
            if (!(ChronoUnit.DAYS.between(booking.getEndDate(), startDate) >= 0 || ChronoUnit.DAYS.between(endDate, booking.getStartDate()) >= 0)) {
                return false;
            }
        }
        return true;
    }
    // Calculating bill for provided period of time without taxes and service fees
    private double calculateBillPerDay(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate) * roomType.getPrice();
    }
    // Writing booking history of this room in hotel's booking history
    public void writingInFIle(FileWriter writer) throws IOException {
        for (History history : allBookings) {
            writer.write(history.hotelHistory()+"\n");
            writer.flush();
        }
    }
    public String toString() {
        return "Room N" + roomID
                + " - type: " + roomType
                + ", Description: " + roomType.getDescription()
                + ", Price per day: " + roomType.getPrice() + "$";
    }
}
