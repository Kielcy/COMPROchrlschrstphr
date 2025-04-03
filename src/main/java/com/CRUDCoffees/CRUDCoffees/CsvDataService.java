package com.CRUDCoffees.CRUDCoffees;

import org.springframework.stereotype.Service;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Service for handling CSV operations related to Coffee data.
 * This service provides methods for saving coffee data to CSV files.
 */
@Service
public class CsvDataService {

    // Constant for the file path of the CSV file.
    private static final String CSV_FILE_PATH = "coffee_list.csv";

    /**
     * Saves the provided list of coffee objects to a CSV file in the root directory.
     * This method is called automatically whenever there are changes to the coffee list.
     *
     * @param coffeeList The list of coffee objects to save to CSV.
     * @return True if the save was successful, false otherwise.
     */
    public boolean saveToCsv(List<Coffee> coffeeList) {
        try {
            // OOP: Open the FileWriter in append mode to write to the CSV file.
            FileWriter csvWriter = new FileWriter(CSV_FILE_PATH);

            // OOP: Writing CSV header (column names)
            csvWriter.append("ID,Name,Type,Size,Price,Roast Level,Origin,IsDecaf,Stock,Flavor Notes,Brew Method\n");

            // OOP: Iterate over the coffee list and write each coffee's details to the CSV file.
            for (Coffee coffee : coffeeList) {
                // Writing each coffee's attributes to CSV, ensuring that fields are properly escaped
                csvWriter.append(String.valueOf(coffee.getId())).append(",");
                csvWriter.append(escapeCsvField(coffee.getName())).append(",");
                csvWriter.append(escapeCsvField(coffee.getType())).append(",");
                csvWriter.append(escapeCsvField(coffee.getSize())).append(",");
                csvWriter.append(String.valueOf(coffee.getPrice())).append(",");
                csvWriter.append(escapeCsvField(coffee.getRoastLevel())).append(",");
                csvWriter.append(escapeCsvField(coffee.getOrigin())).append(",");
                csvWriter.append(String.valueOf(coffee.isDecaf())).append(",");
                csvWriter.append(String.valueOf(coffee.getStock())).append(",");

                // OOP: Handle the list of flavor notes, converting it to a semicolon-separated string
                String flavorNotesStr = String.join(";", coffee.getFlavorNotes());
                csvWriter.append(escapeCsvField(flavorNotesStr)).append(",");

                // Writing the brew method to the CSV
                csvWriter.append(escapeCsvField(coffee.getBrewMethod())).append("\n");
            }

            // OOP: Flush and close the writer to ensure data is saved.
            csvWriter.flush();
            csvWriter.close();

            System.out.println("CSV file has been automatically updated at: " + CSV_FILE_PATH);
            return true; // Indicating successful save
        } catch (IOException e) {
            // OOP: Handle any errors during file writing
            System.err.println("Error saving coffee list to CSV: " + e.getMessage());
            e.printStackTrace();
            return false; // Indicating failure to save
        }
    }

    /**
     * Helper method to properly escape CSV fields that might contain commas, quotes, or newlines.
     * This ensures that the data is formatted correctly for CSV compliance.
     *
     * @param field The field to escape.
     * @return The properly escaped field as a String.
     */
    private String escapeCsvField(String field) {
        if (field == null) {
            return "";  // Handle null fields gracefully
        }

        // OOP: If the field contains commas, quotes, or newlines, wrap it in quotes and escape any quotes
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";  // Escape double quotes by replacing with two quotes
        }
        return field;  // Return field as is if no special characters
    }
}
