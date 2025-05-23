package com.example.FinalsCRUD.controllers;

import com.example.FinalsCRUD.models.Coffee;
import com.example.FinalsCRUD.models.CoffeeDTO;
import com.example.FinalsCRUD.services.CsvDataService;
import com.example.FinalsCRUD.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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
    
    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Constructor that initializes the coffee list with sample data.
     * Also saves the initial data to CSV.
     */
    @Autowired
    public HomeController(CsvDataService csvDataService, FileStorageService fileStorageService) {
        this.csvDataService = csvDataService;
        this.fileStorageService = fileStorageService;

        // Initialize coffee list with sample data
        coffeeList.add(new Coffee(1, "Espresso", "Arabica", "Small", 3.50, "Dark", "Ethiopia", false, 10, Arrays.asList("Chocolate", "Nutty"), "Espresso", "default-coffee.jpg"));
        coffeeList.add(new Coffee(2, "Latte", "Arabica", "Medium", 4.50, "Medium", "Brazil", false, 8, Arrays.asList("Creamy", "Sweet"), "Drip", "default-coffee.jpg"));
        coffeeList.add(new Coffee(3, "Cappuccino", "Robusta", "Large", 5.00, "Medium", "Colombia", false, 12, Arrays.asList("Fruity", "Bold"), "French Press", "default-coffee.jpg"));
        coffeeList.add(new Coffee(4, "Mocha", "Arabica", "Medium", 4.75, "Dark", "Guatemala", false, 6, Arrays.asList("Chocolate", "Smooth"), "Espresso", "default-coffee.jpg"));
        coffeeList.add(new Coffee(5, "Americano", "Robusta", "Large", 3.25, "Light", "Kenya", false, 15, Arrays.asList("Citrus", "Balanced"), "Drip", "default-coffee.jpg"));

        // Save initial data to CSV
        this.csvDataService.saveToCsv(coffeeList);
    }

    /**
     * Handles GET requests to the root ("/") endpoint.
     * Displays the main dashboard with statistics and navigation options.
     *
     * @param model The model to which dashboard data is added
     * @return The name of the Thymeleaf template to render ("dashboard")
     */
    @GetMapping("/")
    public String dashboard(Model model) {
        // Calculate coffee statistics for the dashboard
        model.addAttribute("totalCoffees", coffeeList.size());
        
        // Count unique coffee types
        Set<String> types = coffeeList.stream()
                .map(Coffee::getType)
                .collect(Collectors.toSet());
        model.addAttribute("totalTypes", types.size());
        
        // Count unique origins
        Set<String> origins = coffeeList.stream()
                .map(Coffee::getOrigin)
                .collect(Collectors.toSet());
        model.addAttribute("totalOrigins", origins.size());
        
        // Calculate average price
        double avgPrice = coffeeList.stream()
                .mapToDouble(Coffee::getPrice)
                .average()
                .orElse(0.0);
        model.addAttribute("averagePrice", avgPrice);
        
        return "dashboard";
    }

    /**
     * Handles GET requests to the table view endpoint.
     * Retrieves the list of coffee objects and adds them to the model.
     * Additionally, filters the coffee list based on the search query if provided.
     *
     * @param search The search term entered by the user.
     * @param model  The model to which the filtered list of coffees is added.
     * @return The name of the Thymeleaf template to render ("index").
     */
    @GetMapping("/table")
    public String getTableView(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Coffee> filteredCoffees = coffeeList;

        // Filter the coffee list based on the search term
        if (search != null && !search.isEmpty()) {
            filteredCoffees = coffeeList.stream()
                    .filter(coffee -> coffee.getName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Add the filtered list and search term to the model
        model.addAttribute("coffees", filteredCoffees);
        model.addAttribute("search", search);
        return "index";
    }
    
    /**
     * Handles GET requests to the "/catalog" endpoint.
     * Shows the coffee products in a card layout.
     * 
     * @param model The model for the view
     * @return The catalog view template
     */
    @GetMapping("/catalog")
    public String showCatalog(Model model) {
        model.addAttribute("coffees", coffeeList);
        return "catalog";
    }

    /**
     * Handles GET requests to the "/coffee" endpoint.
     * Shows detailed information for a single coffee product.
     * 
     * @param id The ID of the coffee to view
     * @param model The model for the view
     * @return The coffee view template or redirect to catalog if coffee not found
     */
    @GetMapping("/coffee")
    public String viewCoffee(@RequestParam int id, Model model) {
        for (Coffee coffee : coffeeList) {
            if (coffee.getId() == id) {
                model.addAttribute("coffee", coffee);
                return "coffee-view";
            }
        }
        return "redirect:/catalog";
    }

    /**
     * Handles the deletion of a coffee item by its ID.
     * After deletion, the user is redirected to the table view ("/table") page.
     *
     * @param id The ID of the coffee item to delete.
     * @return A redirect to the table view ("/table") page after deletion.
     */
    @PostMapping("/delete")
    public String deleteCoffee(@RequestParam int id) {
        coffeeList.removeIf(coffee -> coffee.getId() == id);

        // Automatically save changes to CSV
        csvDataService.saveToCsv(coffeeList);

        return "redirect:/table";
    }

    /**
     * Handles GET requests to the "/add" endpoint.
     * Returns the view for adding a new coffee item.
     *
     * @return The name of the Thymeleaf template ("new") for adding a coffee.
     */
    @GetMapping("/add")
    public String addCoffee(Model model) {
        // Add an empty CoffeeDTO to the model
        model.addAttribute("coffeeDTO", new CoffeeDTO());
        return "new";
    }

    /**
     * Handles POST requests to the "/save" endpoint.
     * Adds a new coffee item to the coffee list after validating the input.
     * If validation fails, returns to the form with error messages.
     * After successful saving, the user is redirected to the table view ("/table") page.
     *
     * @param coffeeDTO The DTO containing coffee form data with validation annotations
     * @param result The binding result containing validation errors
     * @return The "new" view if validation fails, or a redirect to the table view ("/table") page after saving
     */
    @PostMapping("/save")
    public String saveCoffee(@Valid @ModelAttribute("coffeeDTO") CoffeeDTO coffeeDTO, BindingResult result) {
        // Check for validation errors
        if (result.hasErrors()) {
            return "new";
        }

        // Handle file upload
        MultipartFile imageFile = coffeeDTO.getImageFile();
        String imagePath = "default-coffee.jpg"; // Default image if none uploaded
        
        if (imageFile != null && !imageFile.isEmpty()) {
            imagePath = fileStorageService.storeFile(imageFile);
        }
        
        coffeeDTO.setImagePath(imagePath);

        // Create a new Coffee object and add it to the coffee list
        int newId = coffeeList.isEmpty() ? 1 : coffeeList.get(coffeeList.size() - 1).getId() + 1;
        coffeeDTO.setId(newId);
        coffeeList.add(coffeeDTO.toCoffee());

        // Automatically save changes to CSV
        csvDataService.saveToCsv(coffeeList);

        return "redirect:/table";
    }

    /**
     * Handles GET requests to the "/edit" endpoint.
     * Retrieves a coffee object by its ID and adds it to the model for editing.
     * If the coffee is found, it returns the "edit" view; otherwise, it redirects to the table view ("/table") page.
     *
     * @param id    The ID of the coffee item to edit.
     * @param model The model to which the coffee item is added for editing.
     * @return The name of the Thymeleaf template ("edit") if the coffee item is found, otherwise a redirect to the table view ("/table") page.
     */
    @GetMapping("/edit")
    public String editCoffee(@RequestParam int id, Model model) {
        for (Coffee coffee : coffeeList) {
            if (coffee.getId() == id) {
                // Create a CoffeeDTO from the existing Coffee and add it to the model
                CoffeeDTO coffeeDTO = new CoffeeDTO(
                    coffee.getId(),
                    coffee.getName(),
                    coffee.getType(),
                    coffee.getSize(),
                    coffee.getPrice(),
                    coffee.getRoastLevel(),
                    coffee.getOrigin(),
                    coffee.isDecaf(),
                    coffee.getStock(),
                    coffee.getFlavorNotes(),
                    coffee.getBrewMethod(),
                    coffee.getImagePath()
                );
                model.addAttribute("coffeeDTO", coffeeDTO);
                return "edit";
            }
        }
        return "redirect:/table";
    }

    /**
     * Handles POST requests to the "/update" endpoint.
     * Updates the details of an existing coffee item with the provided information after validation.
     * If validation fails, returns to the form with error messages.
     * After successful updating, the user is redirected to the table view ("/table") page.
     *
     * @param coffeeDTO The DTO containing coffee form data with validation annotations
     * @param result The binding result containing validation errors
     * @return The "edit" view if validation fails, or a redirect to the table view ("/table") page after updating
     */
    @PostMapping("/update")
    public String updateCoffee(@Valid @ModelAttribute("coffeeDTO") CoffeeDTO coffeeDTO, BindingResult result) {
        // Check for validation errors
        if (result.hasErrors()) {
            return "edit";
        }

        // Handle file upload
        MultipartFile imageFile = coffeeDTO.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = fileStorageService.storeFile(imageFile);
            coffeeDTO.setImagePath(imagePath);
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
                
                // Only update the image path if a new image was uploaded
                if (coffeeDTO.getImagePath() != null) {
                    coffee.setImagePath(coffeeDTO.getImagePath());
                }
                
                break;
            }
        }

        // Automatically save changes to CSV
        csvDataService.saveToCsv(coffeeList);

        return "redirect:/table";
    }
}