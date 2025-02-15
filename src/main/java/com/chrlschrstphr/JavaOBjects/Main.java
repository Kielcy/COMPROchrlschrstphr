package JavaOBjects;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create two Coffee objects with prices in PHP
        Coffee espresso = new Coffee(
                "Espresso",
                "Arabica",
                "Small",
                196.00, // Price in PHP
                "Dark",
                "Italy",
                false,
                10,
                new ArrayList<>(List.of("Chocolate", "Nutty")),
                "Espresso Machine"
        );

        Coffee latte = new Coffee(
                "Latte",
                "Robusta",S
                "Medium",
                252.00, // Price in PHP
                "Medium",
                "Brazil",
                true,
                5,
                new ArrayList<>(List.of("Vanilla", "Caramel")),
                "Steamed Milk"
        );

        // Call methods for the first object (espresso)
        System.out.println("=== Espresso Details ===");
        espresso.displayDetails();
        System.out.println("Calculated Price for Large Size (PHP): ₱" + espresso.calculatePrice("Large"));
        System.out.println("Is in Stock? " + (espresso.checkStock() ? "Yes" : "No"));
        espresso.addFlavor("Citrus");
        espresso.updateStock(-2); // Reduce stock by 2
        espresso.setDecaf(true);
        espresso.changeRoastLevel("Medium");
        espresso.discount(10); // Apply 10% discount
        System.out.println("Updated Description: " + espresso.describe());
        espresso.displayDetails();

        // Call methods for the second object latte
        System.out.println("=== Latte Details ===");
        latte.displayDetails();
        System.out.println("Calculated Price for Small Size (PHP): ₱" + latte.calculatePrice("Small"));
        System.out.println("Is in Stock? " + (latte.checkStock() ? "Yes" : "No"));
        latte.addFlavor("Hazelnut");
        latte.updateStock(10); // Add 10 to stock
        latte.setDecaf(false);
        latte.changeRoastLevel("Dark");
        latte.discount(20); // Apply 20% discount
        System.out.println("Updated Description: " + latte.describe());
        latte.displayDetails();
    }
}