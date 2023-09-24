import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class HotelRoomBookingSystem implements Serializable {
    private static final Scanner scanner = new Scanner(System.in);
    private static Hotel hotel;

    public static void main(String[] args) {
        loadDataBase();
        System.out.println("Welcome to hotel's official WebSite");
        boolean exit = false;
        while (!exit) {
            System.out.println("Do you want to enter as");
            System.out.println("  1. System operator");
            System.out.println("  2. Customer");
            System.out.println("  3. Exit the website");
            System.out.print("Enter your choice: ");
            String response = scanner.nextLine();
            switch (response.trim()) {
                case "1" -> SystemOperator();
                case "2" -> Customer();
                case "3" -> {
                    System.out.println("Exiting web site...");
                    exit = true;
                }
                default -> System.out.println("Not valid choice. Please try again");
            }
        }
        saveDataBase();
    }

    // This method provides operations you can perform with System operator permissions
    private static void SystemOperator() {
        System.out.println("You entered as a System Operator");
        while (true) {
            System.out.println("Choose the operation you want to perform");
            System.out.println("  1. Add a customer");
            System.out.println("  2. Add a room");
            System.out.println("  3. Book a room for the customer");
            System.out.println("  4. Display all registered users");
            System.out.println("  5. Display all rooms");
            System.out.println("  6. Check bookings of provided room");
            System.out.println("  7. Save history of hotel in \"Hotel_Bookings.txt\" file");
            System.out.println("  8. Exit System Operator mode");
            System.out.print("Enter your choice: ");
            String response = scanner.nextLine();
            switch (response.trim()) {

                case "1" -> registerUser();

                case "2" -> {
                    String strType;
                    String choice;
                    boolean exit = false;
                    while(!exit) {
                        System.out.print("Enter the room type you want to add - (SINGLE, DOUBLE, DELUXE): ");
                        strType = scanner.nextLine();
                        if(hotel.addRoom(strType)) break;
                        System.out.println("Invalid type for room registration");
                        while (true) {
                            System.out.println("Do you want to");
                            System.out.println("  1. Enter other room type you want to add");
                            System.out.println("  2. Perform other operation");
                            System.out.print("Enter your choice: ");
                            choice = scanner.nextLine();
                            if (choice.equals("1")) {
                                break;
                            }
                            if (choice.equals("2")) {
                                exit = true;
                                break;
                            }
                            System.out.println("Not valid choice. Please try again.");
                        }
                    }
                }

                case "3" -> {
                    String choice;
                    String email;
                    Customer customer = null;
                    boolean exit = false;
                    while(!exit) {
                        System.out.print("Enter the email of customer you want to book room for: ");
                        email = scanner.nextLine();
                        customer = hotel.findCustomerByEmail(email);
                        if (customer != null) break;
                        System.out.println("This customer is not registered");
                        while (true) {
                            System.out.println("Do you want to");
                            System.out.println("  1. Enter other email");
                            System.out.println("  2. Perform other operation");
                            System.out.print("Enter your choice: ");
                            choice = scanner.nextLine();
                            if (choice.equals("1")) {
                                break;
                            }
                            if (choice.equals("2")) {
                                exit = true;
                                break;
                            }
                            System.out.println("Not valid choice. Please try again.");
                        }
                    }
                    if (exit) break;
                    bookRoom(customer);
                }
                case "4" -> {
                    if (hotel.customersDoNotExist()) {
                        System.out.println("There are no customers registered");
                    } else {
                        hotel.displayCustomers();
                    }
                }
                case "5" -> {
                    if(hotel.roomsDoNotExist()) {
                        System.out.println("There are no rooms in hotel");
                    } else {
                        hotel.displayRooms();
                    }
                }

                case "6" -> {
                    String choice;
                    boolean exit = true;
                    System.out.println("You can check bookings of certain room by its ID");
                    while (true) {
                        if(hotel.roomsDoNotExist()) {
                            System.out.println("There are no rooms in hotel");
                            break;
                        }
                        exit = false;
                        System.out.println("Do you want to check ID's of all rooms? [Y/N]");
                        System.out.print("Your choice: ");
                        choice = scanner.nextLine();
                        if (choice.equals("Y")) {
                            hotel.displayRooms();
                            break;
                        }
                        if (choice.equals("N")) {
                            break;
                        }
                        System.out.println("Not valid choice. Please try again.");
                    }
                    while (!exit) {
                        System.out.print("Enter room ID: ");
                        choice = scanner.nextLine();
                        Room room = hotel.findRoomByID(choice);
                        if (room != null) {
                            while(true) {
                                System.out.println("Do you want to display all bookings or upcoming bookings");
                                System.out.println("  1. All bookings");
                                System.out.println("  2. Upcoming bookings");
                                System.out.print("Enter your choice: ");
                                choice = scanner.nextLine();
                                if (choice.equals("1")) {
                                    room.displayAllBookings();
                                    break;
                                }
                                if (choice.equals("2")) {
                                    room.displayUpcomingBookings();
                                    break;
                                }
                                System.out.println("Not valid choice. Please try again!");
                            }
                            break;
                        }
                        while (true) {
                            System.out.println("Do you want to");
                            System.out.println("  1. Enter other ID");
                            System.out.println("  2. Perform other operation");
                            System.out.print("Enter your choice: ");
                            choice = scanner.nextLine();
                            if (choice.equals("1")) {
                                break;
                            }
                            if (choice.equals("2")) {
                                exit = true;
                                break;
                            }
                            System.out.println("Not valid choice. Please try again.");
                        }
                    }
                }
                case "7" -> hotel.writeHotelBookingHistoryInFIle();
                case "8" -> {
                    return;
                }
                default -> System.out.println("Not valid choice. Please try again");
            }
        }
    }

    // This method provides operations that can be performed with Customer permissions (When customer hasn't entered his/her account yet)
    private static void Customer() {
        System.out.println("You entered as a customer");
        while (true) {
            System.out.println("Choose the operation you want to perform");
            System.out.println("  1. Sign in");
            System.out.println("  2. Log in");
            System.out.println("  3. Exit Customer mode");
            System.out.print("Enter your choice: ");
            String response = scanner.nextLine();
            switch (response.trim()) {
                case "1" -> registerUser();
                case "2" -> {
                    Customer customer;
                    String name;
                    String email;
                    boolean exit = false;
                    while (!exit) {
                        System.out.print("Enter your email: ");
                        email = scanner.nextLine();
                        customer = hotel.findCustomerByEmail(email);
                        if (customer != null) {
                            boolean innerExit = false;
                            while (!innerExit) {
                                System.out.print("Enter your name: ");
                                name = scanner.nextLine();
                                if (name.equals(customer.getName())) {
                                    System.out.println("You entered your account");
                                    customerOperations(customer);
                                    break;
                                }
                                System.out.println("Not correct name for this user");
                                while (true) {
                                    System.out.println("Do you want to");
                                    System.out.println("  1. Enter other name");
                                    System.out.println("  2. Perform other operation");
                                    System.out.print("Enter your choice: ");
                                    String choice = scanner.nextLine();
                                    if (choice.equals("1")) {
                                        break;
                                    }
                                    if (choice.equals("2")) {
                                        innerExit = true;
                                        break;
                                    }
                                }
                            }
                           break;
                        }
                        System.out.println("No such user registered");
                        while (true) {
                            System.out.println("Do you want to sign in [Y/N]");
                            System.out.print("Enter your choice: ");
                            String choice = scanner.nextLine();
                            if (choice.equals("Y")) {
                                registerUser();
                            }
                            if (choice.equals("Y") || choice.equals("N")) {
                                exit = true;
                                break;
                            }
                            System.out.println("Invalid choice");
                        }
                    }
                }
                case "3" -> {
                    return;
                }
                default -> System.out.println("Not valid choice. Please try again");
            }
        }
    }
    // This method provides operations that customer can perform after entering his/her account
    private static void customerOperations(Customer customer) {
        while (true) {
            System.out.println("Choose the operation you want to perform");
            System.out.println("  1. Book a room");
            System.out.println("  2. View booking history");
            System.out.println("  3. Save history of bookings in " + customer.getFILENAME() + " file");
            System.out.println("  4. Log out");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> bookRoom(customer);
                case "2" -> customer.displayAllBookings();
                case "3" -> hotel.writeCustomerBookingHistoryInFIle(customer);
                case "4" -> {
                    return;
                }
                default -> System.out.println("Not valid choice. Please try again");
            }
        }
    }
    // This method is used by System operator while adding customer, and by Customer during his/her registration
    private static void registerUser() {
        boolean exit = false;
        String email;
        String name;
        while (!exit) {
            while (true) {
                System.out.print("Enter name: ");
                name = scanner.nextLine();
                if (name.length()!=0) {
                    break;
                }
                System.out.println("Invalid input for name. Please try again");
            }
            while(true) {
                System.out.print("Enter email address: ");
                email = scanner.nextLine();
                if (email.length()!=0){
                    break;
                }
                System.out.println("Invalid input for email address. Please try again");
            }
            if (hotel.addCustomer(name, email)) {
                break;
            }
            while (true) {
                System.out.println("Do you want to");
                System.out.println("  1. Enter other name and email");
                System.out.println("  2. Perform other operation");
                System.out.print("Enter your choice: ");
                String choice = scanner.nextLine();
                if (choice.equals("1")) {
                    break;
                }
                if (choice.equals("2")) {
                    exit = true;
                    break;
                }
                System.out.println("Not valid choice. Please try again");
            }
        }
    }

    // This method is used for booking room for the specific customer
    private static void bookRoom(Customer customer) {
        boolean exit = false;
        String choice;
        String strType;
        RoomType roomType = null;
        while(!exit) {
            System.out.print("Enter a room type you want to book for " + customer.getName() + " - (SINGLE, DOUBLE, DELUXE): ");
            strType = scanner.nextLine();
            roomType = hotel.typeRecognizing(strType);
            if (roomType != null) {
                break;
            }
            System.out.println("This type is not valid room type");
            while (true) {
                System.out.println("Do you want to");
                System.out.println("  1. Enter other room type");
                System.out.println("  2. Perform other operation");
                System.out.print("Enter your choice: ");
                choice = scanner.nextLine();
                if (choice.equals("1")) {
                    break;
                }
                if (choice.equals("2")) {
                    exit = true;
                    break;
                }
                System.out.println("Not valid choice. Please try again");
            }

        }
        if (exit) return;
        while(!exit) {
            try {
                System.out.println("Enter date of start of the reservation (format: year-month-day format)");
                String strStartDate = scanner.nextLine();
                LocalDate startDate = LocalDate.parse(strStartDate);
                System.out.println("Enter date of end of the reservation (format: year-month-day format)");
                String strEndDate = scanner.nextLine();
                LocalDate endDate = LocalDate.parse(strEndDate);
                if (ChronoUnit.DAYS.between(startDate, endDate) > 0 && ChronoUnit.DAYS.between(LocalDate.now() , startDate) >= 0) {
                    hotel.bookRoom(roomType, customer, startDate, endDate);
                    return;
                }
            } catch (DateTimeParseException ignored) {}
            while (true) {
                System.out.println("Invalid input for date");
                System.out.println("Do you want to cancel booking process [Y/N]");
                System.out.print("Enter your choice: ");
                choice = scanner.nextLine();
                if (choice.equals("Y")) {
                    exit = true;
                    break;
                }
                if (choice.equals("N")) {
                    break;
                }
                System.out.println("Not valid choice. Please try again");
            }
        }
    }
    // This method loads the state of the system from the file system
    public static void loadDataBase(){
        try (ObjectInputStream objIn = new ObjectInputStream(new FileInputStream("Hotel_Database.txt"))) {
            hotel = (Hotel) objIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            hotel = new Hotel();
        }
    }
    // This method saves the state of the system in the file system
    public static void saveDataBase() {
        try (ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream("Hotel_Database.txt"))) {
            objOut.writeObject(hotel);
        } catch (IOException e) {
            System.err.println("Program failed while saving database: " + e.getMessage());
        }
    }
}