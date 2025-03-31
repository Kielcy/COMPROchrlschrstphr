package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class for managing coffee data and handling HTTP requests.
 * This class provides a list of available coffee options and allows users
 * to search for specific coffees using a search bar.
 */
@Controller
public class HomeController {
    private List<Coffee> coffeeList = new ArrayList<>();

    /**
     * Initializes the coffee list with sample data.
     */
    public HomeController() {
        coffeeList.add(new Coffee(1, "Espresso", "Arabica", "Small", 3.50, "Dark", "Ethiopia", false, 10, Arrays.asList("Chocolate", "Nutty"), "Espresso"));
        coffeeList.add(new Coffee(2, "Latte", "Arabica", "Medium", 4.50, "Medium", "Brazil", false, 8, Arrays.asList("Creamy", "Sweet"), "Drip"));
        coffeeList.add(new Coffee(3, "Cappuccino", "Robusta", "Large", 5.00, "Medium", "Colombia", false, 12, Arrays.asList("Fruity", "Bold"), "French Press"));
        coffeeList.add(new Coffee(4, "Mocha", "Arabica", "Medium", 4.75, "Dark", "Guatemala", false, 6, Arrays.asList("Chocolate", "Smooth"), "Espresso"));
        coffeeList.add(new Coffee(5, "Americano", "Robusta", "Large", 3.25, "Light", "Kenya", false, 15, Arrays.asList("Citrus", "Balanced"), "Drip"));
    }

    /**
     * Handles the root ("/") request and displays the list of available coffees.
     * If a search parameter is provided, it filters the coffee list based on the search query.
     *
     * @param search The search keyword entered by the user (optional).
     * @param model  The Spring Model object used to pass attributes to the view.
     * @return The name of the HTML template ("index") to be rendered.
     */
    @GetMapping("/")
    public String getCoffees(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Coffee> filteredCoffees = coffeeList;

        // Filter coffee list based on search input
        if (search != null && !search.isEmpty()) {
            filteredCoffees = coffeeList.stream()
                    .filter(coffee -> coffee.getName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Add filtered coffee list and search term to the model
        model.addAttribute("coffees", filteredCoffees);
        model.addAttribute("search", search);

        return "index"; // Return the name of the Thymeleaf template
    }
}
