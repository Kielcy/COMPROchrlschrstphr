package com.Kielcy;

import java.util.Scanner;

// Matrix class to encapsulate matrix operations
class Matrix {
    private int[][] data;
    private int rows;
    private int cols;

    // Constructor to initialize the matrix with rows and columns
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        data = new int[rows][cols];
    }

    // Method to input the matrix values
    public void inputMatrix() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter matrix elements:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = input.nextInt();
            }
        }
    }

    // Method to display the matrix
    public void displayMatrix() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Method to add two matrices
    public Matrix add(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            System.out.println("Matrices have different sizes, cannot add.");
            return null;
        }

        Matrix result = new Matrix(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.data[i][j] = this.data[i][j] + other.data[i][j];
            }
        }
        return result;
    }

    // Method to subtract two matrices
    public Matrix subtract(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            System.out.println("Matrices have different sizes, cannot subtract.");
            return null;
        }

        Matrix result = new Matrix(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.data[i][j] = this.data[i][j] - other.data[i][j];
            }
        }
        return result;
    }
}

public class Prelims {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Get matrix dimensions
        System.out.print("Enter number of rows: ");
        int rows = input.nextInt();
        System.out.print("Enter number of columns: ");
        int cols = input.nextInt();

        // Create two matrix objects
        Matrix matrix1 = new Matrix(rows, cols);
        Matrix matrix2 = new Matrix(rows, cols);

        // Input matrices
        System.out.println("Enter elements of Matrix A:");
        matrix1.inputMatrix();
        System.out.println("Enter elements of Matrix B:");
        matrix2.inputMatrix();

        // Choose operation
        System.out.println("Select operation:");
        System.out.println("1. Addition");
        System.out.println("2. Subtraction");
        int choice = input.nextInt();

        Matrix result = null;

        // Perform the selected operation
        if (choice == 1) {
            result = matrix1.add(matrix2);
            if (result != null) {
                System.out.println("Result of Addition:");
                result.displayMatrix();
            }
        } else if (choice == 2) {
            result = matrix1.subtract(matrix2);
            if (result != null) {
                System.out.println("Result of Subtraction:");
                result.displayMatrix();
            }
        } else {
            System.out.println("Invalid choice.");
        }

        input.close();
    }
}


