import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Hotel implements Serializable {
    private final List<Customer> customers;
    private final List<Room> rooms;
    public Hotel(){
        customers = new ArrayList<>();
        rooms = new ArrayList<>();
    }
    // Checking if customer has already been registered, and adding him/her otherwise ( returning false if operation fails )
    public boolean addCustomer(String name, String email){
        if (findCustomerByEmail(email) != null) {
            System.out.println("User with " + email + " email address has already been registered");
            return false;
        }
        System.out.println(name + " is successfully registered!");
        customers.add(new Customer(name, email));
        return true;
    }
    // Adding room to hotel by provided type ( returning false if operation fails )
    public boolean addRoom(String strType) {
        RoomType type = typeRecognizing(strType);
        if (type == null) {
            return false;
        }
        Room room = new Room(type);
        rooms.add(room);
        System.out.println(type + " room is successfully added to hotel");
        return true;
    }
    // performing booking operation if there is room of provided type available during provided period
    public void bookRoom(RoomType roomType, Customer customer, LocalDate startDate, LocalDate endDate) {
        for (Room room : rooms) {
            if (room.getRoomType().equals(roomType) && room.isAvailableDuringPeriod(startDate, endDate)) {
                History history = room.addBooking(customer, startDate, endDate);
                customer.addBooking(room, startDate, endDate, history.getRoomBill());
                history.successfulBooking();
                return;
            }
        }
        System.out.println("There are no " + roomType + " rooms available during this period");
    }
    // Finding customer registered in hotel by email (This works right, cause two different customers can't have same email address)
    public Customer findCustomerByEmail (String email) {
        for (Customer customer : customers) {
            if (customer.getEmail().equals(email)) {
                return customer;
            }
        }
        return null;
    }
    // If provided string contains valid room type, converting it to RoomType and returning
    public RoomType typeRecognizing(String strType) {
        switch (strType.toLowerCase()) {
            case "single" -> {
                return RoomType.SINGLE;
            }
            case "double" -> {
                return RoomType.DOUBLE;
            }
            case "deluxe" -> {
                return RoomType.DELUXE;
            }
        }
        return null;
    }
    // checking whether hotel has rooms or not
    public boolean roomsDoNotExist() {
        return rooms.isEmpty();
    }
    // checking whether hotel has customers or not
    public boolean customersDoNotExist() {
        return customers.isEmpty();
    }
    // Displaying all rooms of hotel
    public void displayRooms() {
        rooms.forEach(System.out::println);
    }
    // Displaying all customers of hotel if there are any
    public void displayCustomers(){
        customers.forEach(System.out::println);
    }
    // Checking if room with provided ID exists and returning it
    public Room findRoomByID (String ID) {
        try {
            int intID = Integer.parseInt(ID);
            for (Room room : rooms) {
                if (room.getRoomID() == intID) {
                    return room;
                }
            }
        } catch (NumberFormatException ignored){}
        System.out.println("Invalid ID provided");
        return null;
    }
    // Writing every room's booking history in "Hotel_Bookings.txt" file
    public void writeHotelBookingHistoryInFIle(){
        try (FileWriter writer = new FileWriter("Hotel_Bookings.txt")) {
            for (Room room : rooms) {
                room.writingInFIle(writer);
            }
            System.out.println("Hotel's booking history is successfully saved in \"Hotel_Bookings.txt\" file");
        } catch (IOException e) {
            System.out.println("Something goes wrong while saving hotel booking history in file");
        }
    }
    // Writing booking history of provided customer in his/her file
    public void writeCustomerBookingHistoryInFIle(Customer customer) {
        try (FileWriter writer = new FileWriter(customer.getFILENAME())) {
            customer.writingInFIle(writer);
            System.out.println(customer.getName() + "'s booking history is successfully saved in " + customer.getFILENAME() + " file");
        } catch (IOException e) {
            System.out.println("Something goes wrong while saving customer's booking history in file");
        }
    }
}
