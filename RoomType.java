import java.io.Serializable;

public enum RoomType implements Serializable{
    SINGLE("single bed, bathroom, TV, closet", 20),
    DOUBLE("double bed, bathroom, TV, closet", 35),
    DELUXE("king-size bed, bathtub, minibar, sitting area", 55);
    private final String description;
    private final int price;
    private RoomType(String description, int price) {
        this.description = description;
        this.price = price;
    }
    public String getDescription() {
        return description;
    }
    public int getPrice() {
        return price;
    }
}


