package com.example.FinalsCRUD;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Data Transfer Object for Coffee form validation.
 * This class represents the form data for creating or updating coffee entries
 * and contains validation annotations for server-side validation.
 */
public class CoffeeDTO {
    private int id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "Size is required")
    private String size;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private Double price;

    @NotBlank(message = "Roast level is required")
    private String roastLevel;

    @NotBlank(message = "Origin is required")
    private String origin;

    private boolean isDecaf;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    private List<String> flavorNotes;

    @NotBlank(message = "Brew method is required")
    private String brewMethod;
    
    private String imagePath;
    
    private MultipartFile imageFile;

    // Default constructor
    public CoffeeDTO() {
    }

    // Constructor with all fields
    public CoffeeDTO(int id, String name, String type, String size, Double price, String roastLevel, String origin, 
                  boolean isDecaf, Integer stock, List<String> flavorNotes, String brewMethod, String imagePath) {
        this.id = id;
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
        this.imagePath = imagePath;
    }
    
    // Constructor without imagePath for backward compatibility
    public CoffeeDTO(int id, String name, String type, String size, Double price, String roastLevel, String origin, 
                  boolean isDecaf, Integer stock, List<String> flavorNotes, String brewMethod) {
        this(id, name, type, size, price, roastLevel, origin, isDecaf, stock, flavorNotes, brewMethod, null);
    }

    // Convert DTO to Coffee entity
    public Coffee toCoffee() {
        return new Coffee(id, name, type, size, price, roastLevel, origin, isDecaf, stock, flavorNotes, brewMethod, imagePath);
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getRoastLevel() {
        return roastLevel;
    }

    public void setRoastLevel(String roastLevel) {
        this.roastLevel = roastLevel;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public boolean isDecaf() {
        return isDecaf;
    }

    public void setDecaf(boolean decaf) {
        isDecaf = decaf;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public List<String> getFlavorNotes() {
        return flavorNotes;
    }

    public void setFlavorNotes(List<String> flavorNotes) {
        this.flavorNotes = flavorNotes;
    }

    public String getBrewMethod() {
        return brewMethod;
    }

    public void setBrewMethod(String brewMethod) {
        this.brewMethod = brewMethod;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public MultipartFile getImageFile() {
        return imageFile;
    }
    
    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
} 