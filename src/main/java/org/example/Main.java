package org.example;

import java.io.*;
import java.util.Random;
import java.util.StringTokenizer;
public class Main {
    public static void main(String[] args) throws IOException {
        boolean test = true;
        String num = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while(test){
            try{
                System.out.println("""
                        Выберете тип считывания данных:\s
                        1. Ввод данных с консоли.\s
                        2. Чтение данных из файла.\s
                        3. Генерация случайных значений.""");
                num =  in.readLine();
                if ((num.equals("1")) || (num.equals("2")) || (num.equals("3"))) {
                    test = false;
                }
            }catch (IOException w) {
                System.out.println("Ошибка ввода");
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
                    System.out.println("Число неизвестных:");
                    n = Integer.parseInt(in.readLine());
                    M = new float[n][n];
                    B = new float[n];
                    System.out.println("Погрешность:");
                    float eps = Float.parseFloat(in.readLine());
                    for (int i = 0; i < n; i++) {
                        System.out.println("Введите " + (i + 1) + "-ую строку уравнения:");
                        StringTokenizer inputMatrix = new StringTokenizer(in.readLine());
                        for (int j = 0; j < n + 1; j++)
                            if (j >= n) {
                                B[i] = Float.parseFloat(inputMatrix.nextToken().replace(',', '.'));
                            } else {
                                M[i][j] = Float.parseFloat(inputMatrix.nextToken().replace(',', '.'));
                            }
                    }
                    SeidelMethod gaussSeidel = new SeidelMethod(eps);
                    gaussSeidel.solveMatrix(M, B);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    try {
                        in.close();
                    } catch (IOException r) {
                        System.out.println(r.getMessage());
                    }
                    System.out.println("Некорректный ввод");
                }
            }
            case ("2") -> {
                try {
                    float[][] M;
                    float[] B;
                    int n;
                    System.out.println("Считываю данные из файла...");
                    BufferedReader reader = new BufferedReader(new FileReader("test.txt"));
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
                    System.out.println("Погрешность:");
                    float eps = Float.parseFloat(in.readLine());
                    SeidelMethod gaussSeidel = new SeidelMethod(eps);
                    gaussSeidel.solveMatrix(M, B);
                } catch (FileNotFoundException e) {
                    System.out.println("Файл не найден!");
                    try {
                        in.close();
                    } catch (IOException b) {
                        System.out.println("Ошибка!");
                    }
                } catch (IOException e) {
                    System.out.println("Ошибка!");
                }
            }
            case ("3") -> {
                try {
                    float[][] M;
                    float[] B;
                    int n;
                    System.out.println("Вы выбрали cлучайную генерацию, введите кол-во неизвестных:");
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
                    System.out.println("Ваша матрица:");
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
                    System.out.println("Введите допустимую погрешность:");
                    float eps;
                    eps = Float.parseFloat(in.readLine());
                    SeidelMethod gaussSeidel = new SeidelMethod(eps);
                    gaussSeidel.solveMatrix(M, B);
                } catch (RuntimeException | IOException e) {
                    System.out.println("Error!");
                    in.close();
                }
            }
            default -> System.out.println("Unexpected value");
        }
    }
}