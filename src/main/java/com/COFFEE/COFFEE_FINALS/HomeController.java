package com.COFFEE.COFFEE_FINALS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class for handling coffee-related operations in the Spring Boot application.
 * This class provides methods for managing a list of coffee items, including
 * adding, updating, deleting, and searching for coffees.
 */
@Controller
public class HomeController {

    private List<Coffee> coffeeList = new ArrayList<>();

    @Autowired
    private CsvDataService csvDataService;

    /**
     * Constructor that initializes the coffee list with sample data.
     * Also saves the initial data to CSV.
     */
    @Autowired
    public HomeController(CsvDataService csvDataService) {
        this.csvDataService = csvDataService;

        // Initialize coffee list with sample data
        coffeeList.add(new Coffee(1, "Espresso", "Arabica", "Small", 150.00, "Dark", "Ethiopia", false, 10, Arrays.asList("Chocolate", "Nutty"), "Espresso"));
        coffeeList.add(new Coffee(2, "Latte", "Arabica", "Medium", 139.99, "Medium", "Brazil", false, 8, Arrays.asList("Creamy", "Sweet"), "Drip"));
        coffeeList.add(new Coffee(3, "Cappuccino", "Robusta", "Large", 200.00, "Medium", "Colombia", false, 12, Arrays.asList("Fruity", "Bold"), "French Press"));
        coffeeList.add(new Coffee(4, "Mocha", "Arabica", "Medium", 160, "Dark", "Guatemala", false, 6, Arrays.asList("Chocolate", "Smooth"), "Espresso"));
        coffeeList.add(new Coffee(5, "Americano", "Robusta", "Large", 145.99, "Light", "Kenya", false, 15, Arrays.asList("Citrus", "Balanced"), "Drip"));

        // Save initial data to CSV
        this.csvDataService.saveToCsv(coffeeList);
    }

    // Helper method to check if user is logged in
    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("user") != null;
    }
    
    /**
     * Handles GET requests to the root ("/") endpoint.
     * Retrieves the list of coffee objects and adds them to the model.
     * Additionally, filters the coffee list based on the search query if provided.
     *
     * @param search The search term entered by the user.
     * @param model  The model to which the filtered list of coffees is added.
     * @return The name of the Thymeleaf template to render ("index").
     */
    @GetMapping("/")
    public String getCoffees(
            @RequestParam(value = "search", required = false) String search, 
            Model model,
            HttpSession session) {
            
        // Check if user is authenticated
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        // Add user information to model
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
            
        List<Coffee> filteredCoffees = coffeeList;

        // Filter the coffee list based on the search term
        if (search != null && !search.isEmpty()) {
            filteredCoffees = coffeeList.stream()
                    .filter(coffee -> 
                        coffee.getName().toLowerCase().contains(search.toLowerCase()) ||
                        coffee.getType().toLowerCase().contains(search.toLowerCase()) ||
                        coffee.getOrigin().toLowerCase().contains(search.toLowerCase()) ||
                        coffee.getRoastLevel().toLowerCase().contains(search.toLowerCase()) ||
                        coffee.getBrewMethod().toLowerCase().contains(search.toLowerCase())
                    )
                    .collect(Collectors.toList());
        }

        // Add the filtered list and search term to the model
        model.addAttribute("coffees", filteredCoffees);
        model.addAttribute("search", search);
        return "index";
    }

    /**
     * Handles the deletion of a coffee item by its ID.
     * After deletion, the user is redirected to the root ("/") page.
     *
     * @param id The ID of the coffee item to delete.
     * @return A redirect to the root ("/") page after deletion.
     */
    @GetMapping("/delete")
    public String deleteCoffee(
            @RequestParam int id,
            HttpSession session) {
            
        // Check if user is authenticated
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        // Check if user has admin role
        User user = (User) session.getAttribute("user");
        if (!"ADMIN".equals(user.getRole())) {
            return "redirect:/";
        }
            
        coffeeList.removeIf(coffee -> coffee.getId() == id);

        // Automatically save changes to CSV
        csvDataService.saveToCsv(coffeeList);

        return "redirect:/";
    }

    /**
     * Handles GET requests to the "/add" endpoint.
     * Returns the view for adding a new coffee item with an empty CoffeeDTO.
     *
     * @param model The model to add the empty CoffeeDTO.
     * @return The name of the Thymeleaf template ("new") for adding a coffee.
     */
    @GetMapping("/add")
    public String addCoffeeForm(Model model, HttpSession session) {
        // Check if user is authenticated
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        // Check if user has admin role
        User user = (User) session.getAttribute("user");
        if (!"ADMIN".equals(user.getRole())) {
            return "redirect:/";
        }
        
        model.addAttribute("user", user);
        model.addAttribute("coffeeDTO", new CoffeeDTO());
        return "new";
    }

    /**
     * Handles POST requests to the "/save" endpoint.
     * Validates and adds a new coffee item to the coffee list.
     * After successful validation and saving, the user is redirected to the root ("/") page.
     * If validation fails, returns to the form with error messages.
     *
     * @param coffeeDTO The CoffeeDTO object with form data, with validation annotations.
     * @param result The binding result containing validation errors, if any.
     * @param model The model to add attributes to in case of validation errors.
     * @return The view to render based on validation results.
     */
    @PostMapping("/save")
    public String saveCoffee(@Valid @ModelAttribute CoffeeDTO coffeeDTO, BindingResult result, Model model) {
        // If there are validation errors, return to the form
        if (result.hasErrors()) {
            return "new";
        }

        // Create a new Coffee object and add it to the coffee list
        int newId = coffeeList.isEmpty() ? 1 : coffeeList.get(coffeeList.size() - 1).getId() + 1;
        coffeeList.add(coffeeDTO.toCoffee(newId));

        // Automatically save changes to CSV
        csvDataService.saveToCsv(coffeeList);

        return "redirect:/";
    }

    /**
     * Handles GET requests to the "/edit" endpoint.
     * Retrieves a coffee object by its ID and adds it to the model for editing.
     * If the coffee is found, it returns the "edit" view; otherwise, it redirects to the root ("/") page.
     *
     * @param id    The ID of the coffee item to edit.
     * @param model The model to which the coffee item is added for editing.
     * @return The name of the Thymeleaf template ("edit") if the coffee item is found, otherwise a redirect to the root ("/") page.
     */
    @GetMapping("/edit")
    public String editCoffee(@RequestParam int id, Model model) {
        for (Coffee coffee : coffeeList) {
            if (coffee.getId() == id) {
                model.addAttribute("coffeeDTO", CoffeeDTO.fromCoffee(coffee));
                return "edit";
            }
        }
        return "redirect:/";
    }

    /**
     * Handles POST requests to the "/update" endpoint.
     * Validates and updates the details of an existing coffee item.
     * After successful validation and updating, the user is redirected to the root ("/") page.
     * If validation fails, returns to the form with error messages.
     *
     * @param coffeeDTO The CoffeeDTO object with form data, with validation annotations.
     * @param result The binding result containing validation errors, if any.
     * @param model The model to add attributes to in case of validation errors.
     * @return The view to render based on validation results.
     */
    @PostMapping("/update")
    public String updateCoffee(@Valid @ModelAttribute CoffeeDTO coffeeDTO, BindingResult result, Model model) {
        // If there are validation errors, return to the form
        if (result.hasErrors()) {
            return "edit";
        }

        // Update the existing coffee item with the provided information
        for (Coffee coffee : coffeeList) {
            if (coffee.getId() == coffeeDTO.getId()) {
                coffee.setName(coffeeDTO.getName());
                coffee.setType(coffeeDTO.getType());
                coffee.setSize(coffeeDTO.getSize());
                coffee.setPrice(coffeeDTO.getPrice());
                coffee.setRoastLevel(coffeeDTO.getRoastLevel());
                coffee.setOrigin(coffeeDTO.getOrigin());
                coffee.setDecaf(coffeeDTO.isDecaf());
                coffee.setStock(coffeeDTO.getStock());
                coffee.setFlavorNotes(coffeeDTO.getFlavorNotes());
                coffee.setBrewMethod(coffeeDTO.getBrewMethod());
                break;
            }
        }

        // Automatically save changes to CSV
        csvDataService.saveToCsv(coffeeList);

        return "redirect:/";
    }
}