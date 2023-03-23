package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SeidelMethod {
    static boolean input_data_cheking = true;
    final float accuracy;
    float[] answer;
    float[] p;
    int iter = 0;

    public SeidelMethod(float expectedly) {
        accuracy = expectedly;
    }

    public static boolean dominationChecking(float[][] M, int n) {
        int i, j, k = 1;
        float sum;
        for (i = 0; i < n; i++) {
            sum = 0;
            for (j = 0; j < n; j++) {
                sum += Math.abs(M[i][j]);
            }
            sum -= Math.abs(M[i][i]);
            if (sum > Math.abs(M[i][i])) {
                k = 0;
                break;
            }
        }
        if (k == 0) {
            k = 1;
            for (i = 0; i < n; i++) {
                sum = 0;
                for (j = M.length - 1; j >= 0; j--) {
                    sum += Math.abs(M[i][M.length - 1 - j]);
                }
                sum -= Math.abs(M[i][M.length - 1 - i]);
                if (sum > Math.abs(M[i][M.length - 1 - i])) {
                    k = 0;
                    break;
                }
            }
            if (k == 1) {
                float[] temp;
                for (int l = 0; l < M.length / 2; l++) {
                    temp = M[l];
                    M[l] = M[M.length - l - 1];
                    M[M.length - l - 1] = temp;
                }
            }
        }
        return (k == 1);
    }

    private boolean tryToRewriteString(float[][] M, float[] B) {
        for (int i = 0; i < M.length; i++) {
            float LocalMax = Math.abs(M[i][0]);
            int CountMax;
            for (int j = i; j < M.length; j++) {
                if (LocalMax < Math.abs(M[j][i])) {
                    LocalMax = Math.max(LocalMax, Math.abs(M[j][i]));
                    CountMax = j;
                    float[] t = M[i];
                    float b = B[i];
                    M[i] = M[CountMax];
                    M[CountMax] = t;
                    B[i] = B[CountMax];
                    B[CountMax] = b;
                }
            }
        }
        return dominationChecking(M, M.length);
    }

    boolean resultCheck(float[] answer, float[] p, int n, float accuracy) {
        float norm = 0;
        for (int i = 0; i < n; i++) {
            norm += (answer[i] - p[i]) * (answer[i] - p[i]);
        }
        return (Math.sqrt(norm) <= accuracy);
    }

    float roundingAnswers(float answer, float accuracy) {
        int i = 0;
        float newEps = accuracy;
        while (newEps < 1) {
            i++;
            newEps *= 10;
        }
        int rounding = (int) Math.pow(10.0, i);
        answer = (int) (answer * rounding + 0.5) / (float) (rounding);
        return answer;
    }

    public void matrixSolvingWithGaussSeidelMethod(float[][] array_m, float[] array_b) throws IOException {
        p = new float[array_m.length];
        answer = new float[array_m.length];
        for (int i = 0; i < array_m.length; i++) {
            answer[i] = 1;
        }
        if (dominationChecking(array_m, array_m.length)) {
            while (!resultCheck(answer, p, array_m.length, accuracy)) {
                System.arraycopy(answer, 0, p, 0, array_m.length);
                for (int j = 0; j < array_m.length; j++) {
                    float variable = 0;
                    for (int i = 0; i < array_m.length; i++) {
                        if (i != j) {
                            variable += (array_m[j][i]) * answer[i];
                        }
                    }
                    answer[j] = (array_b[j] - variable) / array_m[j][j];
                }
                iter++;
                if (iter >= 1000) {
                    System.out.println("The solution is not received after 1000 iterations.");
                    System.exit(0);
                    break;
                }
            }
            System.out.println("System solution:");
            System.out.println("Iterations: " + iter);
            for (int i = 0; i < array_m.length; i++) {
                System.out.println("x" + i + " = " + roundingAnswers(answer[i], accuracy));
            }
        } else {
            String number = "";
            BufferedReader input_data_num = new BufferedReader(new InputStreamReader(System.in));
            while (input_data_cheking) {
                System.out.println("""
                        There is no diagonal predominance:\s
                        1. Rearrange the rows.\s
                        2. End the program.""");
                number = input_data_num.readLine();
                if (number.equals("1") || number.equals("2")) {
                    input_data_cheking = false;
                }
            }
            if (number.equals("1")) {
                if (tryToRewriteString(array_m, array_b)) {
                    matrixSolvingWithGaussSeidelMethod(array_m, array_b);
                } else {
                    System.out.println("It was not possible to achieve diagonal dominance.");
                }
            } else if (number.equals("2")) {
                System.out.println("Cancel.");
            } else {
                System.out.println(" ");
            }
        }
    }
}

