import java.util.Arrays;
import java.util.Scanner;

public class Matrix {
    private double[][] array;
    private int rowsCount;
    private int colsCount;

    private void init(int rc, int cc) {
        rowsCount = rc;
        colsCount = cc;
        array = new double[rc][cc];
    }

    public void input() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите размер матрицы в формате <n m>, где n -кол-во строк, m - кол-во столбцов");
        int n = sc.nextInt(), m = sc.nextInt();
        if (n <= 0 || m <= 0) {
            System.out.println("Нельзя создать матрицу такой размерности!!! Перезапуск ввода!");
            input();
            return;
        }
        init(n, m);
        System.out.println("Как вы хотите ввести матрицу? Введите 1 или 2.");
        System.out.println("1. По строкам\n2. По элементам");
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Введите разделитель:");
                sc.nextLine();
                String separator = sc.nextLine();
                String nelza = "-0123456789?.*\\";
                if (separator.length() == 0) {
                    System.out.println("Содержит недопустимые символы, перезапуск ввода!");
                    input();
                    return;
                }
                for (int i = 0; i < nelza.length(); ++i) {
                    if (separator.contains(nelza.charAt(i) + "")) {
                        System.out.println("Содержит недопустимые символы, перезапуск ввода!");
                        input();
                        return;
                    }
                }
                for (int i = 0; i < n; ++i) {
                    System.out.println("Введите " + (i + 1) + " строчку.");
                    String[] line = sc.nextLine().split(separator);
                    line = Arrays.stream(line).filter(x -> x.matches("-?[0-9]+[.]?[0-9]*")).toArray(String[]::new);
                    if (line.length != m) {
                        System.out.println("Введена неправильная строка, перезапуск ввода!");
                        input();
                        return;
                    }
                    for (int j = 0; j < colsCount; ++j) {
                        array[i][j] = Double.parseDouble(line[j]);
                    }
                }
                break;
            case 2:
                sc.nextLine();
                for (int i = 0; i < n; ++i) {
                    for (int j = 0; j < m; ++j) {
                        System.out.println("Введите элемент " + (i + 1) + " строки " + (j + 1) + " столбца.");
                        String tempStr = sc.nextLine();
                        if (tempStr.matches("-?\\d+(\\.\\d+)?(/-?\\d+(\\.\\d+)?)?")) {
                            if (tempStr.contains("/")) {
                                String[] dec = tempStr.split("/");
                                if (Double.parseDouble(dec[1]) == 0) {
                                    System.out.println("На 0 делить нельзя!");
                                    System.out.println("Введена неправильная строка, перезапуск ввода!");
                                    input();
                                    return;
                                }
                                array[i][j] = round(Double.parseDouble(dec[0]) / Double.parseDouble(dec[1]));
                            } else {
                                array[i][j] = Double.parseDouble(tempStr);
                            }
                        } else {
                            System.out.println("Введена неправильная строка, перезапуск ввода!");
                            input();
                            return;
                        }
                    }
                }
                break;
        }
    }

    public void printMatrix() {
        System.out.println("Матрица " + rowsCount + " x " + colsCount);
        for (int i = 0; i < rowsCount; ++i) {
            for (int j = 0; j < colsCount; ++j) {
                String symbol = " ";
                if (array[i][j] < 0) {
                    symbol = "";
                }
                if (j != colsCount - 1) {
                    System.out.printf(symbol + "%.5f\t", array[i][j]);
                } else {
                    System.out.printf(symbol + "%.5f", array[i][j]);
                }
            }
            System.out.println();
        }
    }

    public Matrix multiplicationMatrix(Matrix other) {
        if (this.colsCount == other.getRowsCount()) {
            Matrix newM = new Matrix();
            newM.init(this.rowsCount, other.colsCount);
            for (int i = 0; i < this.rowsCount; ++i) {
                for (int j = 0; j < other.colsCount; ++j) {
                    double temp = 0;
                    for (int k = 0; k < other.rowsCount; ++k) {
                        temp += this.array[i][k] * other.array[k][j];
                    }
                    newM.array[i][j] = temp;
                }
            }
            return newM;
        }
        if (this.rowsCount == other.colsCount) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Нельзя умножить эту матрицу на матрицу B. Однако, можно умножить матрицу B на эту." +
                    "\nХотите ли вы выполнить умножение?" +
                    "\nВведите да, yes, д, y для согласия. Любое другое для отказа");
            String choice = sc.nextLine().toLowerCase();
            switch (choice) {
                case "да", "yes", "д", "y" -> {
                    Matrix newM = new Matrix();
                    newM.init(other.rowsCount, this.colsCount);
                    for (int i = 0; i < other.rowsCount; ++i) {
                        for (int j = 0; j < this.colsCount; ++j) {
                            double temp = 0;
                            for (int k = 0; k < this.rowsCount; ++k) {
                                temp += other.array[i][k] * this.array[k][j];
                            }
                            newM.array[i][j] = temp;
                        }
                    }
                    return newM;
                }
            }
        }
        return null;
    }

    public Matrix sumMatrix(Matrix other) {
        if (this.colsCount != other.getColsCount() || this.rowsCount != other.getRowsCount()) {
            System.out.println("Нельзя сложить данные матрицы!");
            return null;
        }
        
        Matrix newM = new Matrix();
        newM.init(rowsCount, colsCount);
        for (int i = 0; i < rowsCount; ++i) {
            for (int j = 0; j < colsCount; ++j) {
                newM.array[i][j] = this.array[i][j] + other.array[i][j];
            }
        }
        return newM;
    }

    public int rang() {
        double[][] tempArr = gauss(array, 0);
        int rang = rowsCount;
        for (int i = 0; i < rowsCount; ++i) {
            if (checkZeroRow(tempArr[i], 0)) {
                --rang;
            }
        }
        return rang;
    }

    public Matrix solve(Matrix freeCoef) {
        double[][] mergedArr = mergeMatrix(freeCoef);
        if (rang() == rowsCount && rowsCount == colsCount) {
            System.out.println("Матрица имеет решение!");
            System.out.println("Как вы хотите ее решить?\n1. Обратный ход.\n2. Без обратного хода");
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            mergedArr = gauss(mergedArr, 1);
            switch (choice) {
                case 1:
                    mergedArr = reversedGauss(mergedArr, 1);
                    break;
                case 2:
                    mergedArr = withoutRevGauss(mergedArr);
                    break;
            }
            Matrix ansMatrix = new Matrix();
            ansMatrix.init(rowsCount, 1);
            for (int i = 0; i < rowsCount; ++i) {
                ansMatrix.array[i][0] = round(mergedArr[i][colsCount] / mergedArr[i][i]);
            }
            return ansMatrix;
        } else {
           mergedArr = gauss(mergedArr, 1);
           for (int i = 0; i < rowsCount; ++i) {
               if (checkZeroRow(mergedArr[i], 1) && mergedArr[i][colsCount] != 0) {
                   System.out.println("Нет решений!");
                   return null;
               }
           }
            System.out.println("Бесконечное количество решений!");
           return null;
        }
    }

    private static double[][] gauss(double[][] someArray, int haveFreeCoef) {
        int row = 0;
        int someArrayRowsCount = someArray.length;
        int someArrayColsCount = someArray[0].length;
        double[][] tempArr = cloneArray(someArray);
        while (row < Math.min(someArrayColsCount - haveFreeCoef, someArrayRowsCount)) {
            if (tempArr[row][row] != 0) {
                for (int i = row + 1; i < someArrayRowsCount; ++i) {
                    double coef = -tempArr[i][row] / tempArr[row][row];
                    for (int j = row; j < someArrayColsCount; ++j) {
                        tempArr[i][j] = round(tempArr[row][j] * coef + tempArr[i][j]);
                    }
                }
            } else {
                boolean foundNoneZero = false;
                for (int j = row; j < someArrayColsCount - haveFreeCoef; ++j) {
                    for (int i = row; i < someArrayRowsCount; ++i) {
                        if (tempArr[i][j] != 0) {
                            swapRows(tempArr, row, i);
                            swapCols(tempArr, row, j);
                            foundNoneZero = true;
                        }
                    }
                }
                if (!foundNoneZero) return tempArr;
                --row;
            }
            ++row;
        }
        return tempArr;
    }

    private static double[][] reversedGauss(double[][] someArray, int haveFreeCoef) {
        int row = someArray.length - 1, col = someArray[0].length - 1 - haveFreeCoef;
        double[][] tempArr = cloneArray(someArray);
        while (row >= 0 && col >= 0) {
            if (tempArr[row][col] != 0) {
                for (int i = row - 1; i >= 0; --i) {
                    double coef = -tempArr[i][col] / tempArr[row][col];
                    for (int j = someArray[0].length - 1; j >= 0; --j) {
                        tempArr[i][j] = round(tempArr[i][j] + tempArr[row][j] * coef);
                    }
                }
            } else {
                boolean foundNoneZero = false;
                for (int j = row; j >= 0; --j) {
                    for (int i = row; i >= 0; --i) {
                        if (tempArr[i][j] != 0) {
                            swapRows(tempArr, row, i);
                            swapCols(tempArr, row, j);
                            foundNoneZero = true;
                        }
                    }
                }
                if (!foundNoneZero) return tempArr;
                ++row;
                ++col;
            }
            --row;
            --col;
        }
        return tempArr;
    }
    
    private static double[][] withoutRevGauss(double[][] someArray) {
        int row = someArray.length - 1;
        double[][] tempArr = cloneArray(someArray);
        for (int i = row; i > 0; --i) {
            double coef = tempArr[i][row + 1] / tempArr[i][i];
            for (int j = i - 1; j >= 0; --j) {
                tempArr[j][row + 1] -= tempArr[j][i] * coef;
            }
            tempArr[i][row + 1] = round(tempArr[i][row + 1]);
        }
        return tempArr;
    }

    private static void swapCols(double[][] arr, int p1, int p2) {
        double temp = 0;
        for (int i = 0; i < arr.length; ++i) {
            temp = arr[i][p1];
            arr[i][p1] = arr[i][p2];
            arr[i][p2] = temp;
        }
    }

    private static void swapRows(double[][] arr, int p1, int p2) {
        double[] temp = arr[p1];
        arr[p1] = arr[p2];
        arr[p2] = temp;
    }

    private static boolean checkZeroRow(double[] arr, int haveFreeCoef) {
        for (int i = 0; i < arr.length - haveFreeCoef; ++i) {
            if (arr[i] != 0) return false;
        }
        return true;
    }

    public static double[][] cloneArray(double[][] arr) {
        double[][] ans = new double[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; ++i) {
            ans[i] = arr[i].clone();
        }
        return ans;
    }

    private static double round(double a) {
        double errRate = 0.000001;
        if (Math.abs(a - Math.round(a)) < errRate) {
            return Math.round(a);
        }
        return a;
    }
    
    private double[][] mergeMatrix(Matrix other) {
        double[][] newM = new double[rowsCount][colsCount + other.getColsCount()];
        for (int i = 0; i < this.rowsCount; ++i) {
            for (int j = 0; j < this.colsCount; ++j) {
                newM[i][j] = this.array[i][j];
            }
        }
        for (int i = 0; i < rowsCount; ++i) {
            for (int j = colsCount; j < colsCount + other.getColsCount(); ++j) {
                newM[i][j] = other.array[i][j - colsCount];
            }
        }
        return newM;
    }

    public String toString() {
        String temp = "";
        temp += "Матрица " + rowsCount + " x " + colsCount + "\n";
        for (int i = 0; i < rowsCount; ++i) {
            for (int j = 0; j < colsCount; ++j) {
                String symbol = " ";
                if (array[i][j] < 0) {
                    symbol = "";
                }
                if (j != colsCount - 1) {
                    temp += String.format(symbol + "%.5f\t", array[i][j]);
                } else {
                    temp += String.format(symbol + "%.5f", array[i][j]);
                }
            }
            temp += "\n";
        }
        return temp;
    }

    public int getRowsCount() {
        return rowsCount;
    }

    public int getColsCount() {
        return colsCount;
    }
}
