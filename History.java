import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class History implements Serializable{
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Room room;
    private final Customer customer;
    private final double roomBill;
    private final double taxes;
    private final double serviceFees;

    public History(Room room, Customer customer, LocalDate startDate, LocalDate endDate, double roomBill) {
        this.room = room;
        this.customer = customer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomBill = roomBill;
        DecimalFormat df = new DecimalFormat("#.##");
        taxes = Double.parseDouble(df.format(roomBill * 0.2));
        serviceFees = Double.parseDouble(df.format(roomBill * 1.2 * 0.1));
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public double getTotalPrice() {
        return roomBill + taxes + serviceFees;
    }
    public double getRoomBill() {
        return roomBill;
    }
    // for writing in the "Hotel_Bookings.txt" file
    public String hotelHistory() {
        return "Customer's name: " + customer.getName()
                + " Customer's email: " + customer.getEmail()
                + " " + customerHistory();
    }
    // for writing in customer's personal history file
    public String customerHistory() {
        return "N" + room.getRoomID() + " room - "
                + "  Period: [" + startDate + ", " + endDate + "] "
                + "Total price: " + getTotalPrice() + "$  (room cost: " + roomBill + "$, taxes: " + taxes + "$, service fees: " + serviceFees + "$)";
    }
    // Announcement for successful booking
    public void successfulBooking() {
        System.out.println(room.getRoomType() + " room N" + room.getRoomID() + " is successfully booked for " + customer.getName()
                + " for [" + startDate + ", " + endDate + "] period"
                + " Total price: " + getTotalPrice() + "$  (room cost: " + roomBill + "$, taxes: " + taxes + "$, service fees: " + serviceFees + "$)");
    }

}

