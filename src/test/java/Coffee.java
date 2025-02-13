import java.util.ArrayList;
import java.util.List;

public class Coffee {
    // Properties (Attributes)
    private String name;
    private String type;
    private String size;
    private double price; // Price in PHP
    private String roastLevel;
    private String origin;
    private boolean isDecaf;
    private int stock; // Using int for stock quantity
    private List<String> flavorNotes; // Using List for dynamic flavor notes
    private String brewMethod;

    // Constructor
    public Coffee(String name, String type, String size, double price, String roastLevel, String origin,
                  boolean isDecaf, int stock, List<String> flavorNotes, String brewMethod) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.price = price;
        this.roastLevel = roastLevel;
        this.origin = origin;
        this.isDecaf = isDecaf;
        this.stock = stock;
        this.flavorNotes = flavorNotes;
        this.brewMethod = brewMethod;
    }

    // Method to calculate price based on size
    public double calculatePrice(String size) {
        switch (size.toLowerCase()) {
            case "small":
                return price * 1.0;
            case "medium":
                return price * 1.5;
            case "large":
                return price * 2.0;
            default:
                return price;
        }
    }

    // Method to check if the coffee is in stock
    public boolean checkStock() {
        return stock > 0;
    }

    // Method to add a flavor note
    public void addFlavor(String note) {
        flavorNotes.add(note);
    }

    // Method to update stock quantity
    public void updateStock(int quantity) {
        stock += quantity;
    }

    // Method to describe the coffee
    public String describe() {
        return String.format("A %s roast coffee with %s notes.", roastLevel.toLowerCase(), String.join(", ", flavorNotes));
    }

    // Method to update decaf status
    public void setDecaf(boolean isDecaf) {
        this.isDecaf = isDecaf;
    }

    // Method to change roast level
    public void changeRoastLevel(String newRoastLevel) {
        this.roastLevel = newRoastLevel;
    }

    // Method to apply a discount to the price
    public void discount(double percentage) {
        price -= price * (percentage / 100);
    }

    // Method to display coffee details
    public void displayDetails() {
        System.out.println("Coffee Name: " + name);
        System.out.println("Type: " + type);
        System.out.println("Size: " + size);
        System.out.printf("Price (PHP): ₱%.2f%n", price);
        System.out.println("Roast Level: " + roastLevel);
        System.out.println("Origin: " + origin);
        System.out.println("Decaf: " + (isDecaf ? "Yes" : "No"));
        System.out.println("Stock: " + stock);
        System.out.println("Flavor Notes: " + String.join(", ", flavorNotes));
        System.out.println("Brew Method: " + brewMethod);
        System.out.println("-".repeat(30));
    }
}