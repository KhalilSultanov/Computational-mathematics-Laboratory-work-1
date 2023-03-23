package org.example;

import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
public class Main {
    public static void main(String[] args) throws IOException {
        boolean test = true;
        String num = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Scanner sc = new Scanner(System.in);
        while(test){
            try{
                System.out.println("""
                        Select the type of data reading:\s
                        1. Entering data from the console.\s
                        2. Reading data from a file.\s
                        3. Generation of random values.""");
                num =  in.readLine();
                if ((num.equals("1")) || (num.equals("2")) || (num.equals("3"))) {
                    test = false;
                }
            }catch (IOException w) {
                System.out.println("Input error");
                try {
                    in.close();
                } catch (IOException r) {
                    System.out.println("Error");
                }
            }
        }
        switch (num) {
            case ("1") -> {
                try {
                    float[][] M;
                    float[] B;
                    int n;
                    System.out.println("Number of unknowns:");
                    n = Integer.parseInt(in.readLine());
                    M = new float[n][n];
                    B = new float[n];
                    System.out.println("Accuracy:");
                    float eps = Float.parseFloat(in.readLine());
                    for (int i = 0; i < n; i++) {
                        System.out.println("Enter " + (i + 1) + "-th line of equation:");
                        StringTokenizer inputMatrix = new StringTokenizer(in.readLine());
                        for (int j = 0; j < n + 1; j++)
                            if (j >= n) {
                                B[i] = Float.parseFloat(inputMatrix.nextToken().replace(',', '.'));
                            } else {
                                M[i][j] = Float.parseFloat(inputMatrix.nextToken().replace(',', '.'));
                            }
                    }
                    SeidelMethod gaussSeidel = new SeidelMethod(eps);
                    gaussSeidel.matrixSolvingWithGaussSeidelMethod(M, B);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    try {
                        in.close();
                    } catch (IOException r) {
                        System.out.println(r.getMessage());
                    }
                    System.out.println("Incorrect input");
                }
            }
            case ("2") -> {
                try {
                    float[][] M;
                    float[] B;
                    int n;
                    System.out.println("Write file name:");
                    String file_name = sc.nextLine();
                    System.out.println("I'm reading data from a file...");
                    BufferedReader reader = new BufferedReader(new FileReader(file_name));
                    n = Integer.parseInt(reader.readLine());
                    M = new float[n][n];
                    B = new float[n];
                    for (int i = 0; i < n; i++) {
                        StringTokenizer inputMatrix = new StringTokenizer(reader.readLine());
                        while (inputMatrix.hasMoreTokens())
                            for (int j = 0; j < n + 1 && inputMatrix.hasMoreTokens(); j++) {
                                if (j == n) {
                                    B[i] = Float.parseFloat(inputMatrix.nextToken().replace(',', '.'));
                                } else {
                                    M[i][j] = Float.parseFloat(inputMatrix.nextToken().replace(',', '.'));
                                }
                            }
                    }
                    System.out.println("Accuracy:");
                    float eps = Float.parseFloat(in.readLine());
                    SeidelMethod gaussSeidel = new SeidelMethod(eps);
                    gaussSeidel.matrixSolvingWithGaussSeidelMethod(M, B);
                } catch (FileNotFoundException e) {
                    System.out.println("File not found!");
                    try {
                        in.close();
                    } catch (IOException b) {
                        System.out.println("Error!");
                    }
                } catch (IOException e) {
                    System.out.println("Error!");
                }
            }
            case ("3") -> {
                try {
                    float[][] M;
                    float[] B;
                    int n;
                    System.out.println("You have chosen random generation, enter the number of unknowns:");
                    float sum;
                    n = Integer.parseInt(in.readLine());
                    M = new float[n][n];
                    B = new float[n];
                    Random r = new Random();
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n + 1; j++) {
                            if (j == n) {
                                B[i] = r.nextFloat(-50, 50);
                            } else {
                                M[i][j] = r.nextFloat(-50, 50);
                            }
                        }
                    }
                    for (int i = 0; i < n; i++) {
                        sum = 0;
                        for (int j = 0; j < n; j++) {
                            if (i != j) {
                                sum += Math.abs(M[i][j]);
                            }
                        }
                        if (Math.abs(M[i][i]) <= sum) {
                            while (Math.abs(M[i][i]) <= sum) {
                                M[i][i] = r.nextFloat(sum, (float) (sum * 1.2));
                            }
                        }
                    }
                    int n3 = M.length;
                    System.out.println("Your matrix:");
                    for (int i = 0; i < n3; i++) {
                        for (int j = 0; j < n3 + 1; j++)
                            if (j == n) {
                                System.out.print(B[i] + " ");
                            } else {
                                System.out.print(M[i][j] + " ");
                            }
                        System.out.println();
                    }
                    System.out.println(M.length);
                    System.out.println("Enter the margin of accuracy:");
                    float eps;
                    eps = Float.parseFloat(in.readLine());
                    SeidelMethod gaussSeidel = new SeidelMethod(eps);
                    gaussSeidel.matrixSolvingWithGaussSeidelMethod(M, B);
                } catch (RuntimeException | IOException e) {
                    System.out.println("Error!");
                    in.close();
                }
            }
            default -> System.out.println("Unexpected value");
        }
    }
}