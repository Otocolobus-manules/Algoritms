package utils.permutation_algoritms;

import java.io.*;
import java.util.*;
import java.util.function.Supplier;

public class Permutation {
    private static final Map<String, Supplier<PermutationAlgorithm>> algorithms = Map.of(
        "narayana", NarayanaMethod::new,
        "johnson-trotter", JohnsonTrotterMethod::new,
        "inversion-vector", InversionVectorMethod::new
    );

    // Интерфейс для алгоритмов генерации перестановок
    interface PermutationAlgorithm {
        Result generate(String[] array, String filePath);
    }

    // Алгоритм Нарайаны
    private static class NarayanaMethod implements PermutationAlgorithm {
        @Override
        public Result generate(String[] array, String filePath) {
            ArrayList<String> permutation = new ArrayList<>(Arrays.asList(array));
            long startTime = System.nanoTime();
            Collections.sort(permutation);

            try (BufferedWriter writer = (filePath != null) ? new BufferedWriter(new FileWriter(filePath)) : null) {
                writeToFile(writer, permutation);

                while (true) {
                    int i = permutation.size() - 2;
                    while (i >= 0 && permutation.get(i).compareTo(permutation.get(i + 1)) >= 0) {
                        i--;
                    }

                    if (i < 0) {
                        double elapsedTime = (System.nanoTime() - startTime) / 1_000_000.0;
                        return new Result(elapsedTime);
                    }

                    int j = permutation.size() - 1;
                    while (permutation.get(j).compareTo(permutation.get(i)) <= 0) {
                        j--;
                    }

                    Collections.swap(permutation, i, j);
                    reverseList(permutation, i + 1);

                    writeToFile(writer, permutation);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new Result(-1);
            }
        }

        // Метод реверса части списка
        private static void reverseList(List<String> list, int start) {
            int end = list.size() - 1;
            while (start < end) {
                Collections.swap(list, start, end);
                start++;
                end--;
            }
        }
    }

    // Алгоритм Джонсона-Троттера
    private static class JohnsonTrotterMethod implements PermutationAlgorithm {
        @Override
        public Result generate(String[] array, String filePath) {
            long startTime = System.nanoTime();
            int n = array.length;
            String[] perm = array.clone();
            int[] dir = new int[n];

            Arrays.fill(dir, -1); // Все числа двигаются влево

            try (BufferedWriter writer = (filePath != null) ? new BufferedWriter(new FileWriter(filePath)) : null) {
                writeToFile(writer, perm);

                while (true) {
                    int largestMovableIndex = -1;
                    String largestMovableValue = null;

                    // Найти наибольшее перемещаемое число
                    for (int i = 0; i < n; i++) {
                        int nextIndex = i + dir[i];
                        if (nextIndex >= 0 && nextIndex < n && perm[i].compareTo(perm[nextIndex]) > 0) {
                            if (largestMovableValue == null || perm[i].compareTo(largestMovableValue) > 0) {
                                largestMovableValue = perm[i];
                                largestMovableIndex = i;
                            }
                        }
                    }

                    if (largestMovableIndex == -1) {
                        double elapsedTime = (System.nanoTime() - startTime) / 1_000_000.0;
                        return new Result(elapsedTime);
                    }

                    int swapIndex = largestMovableIndex + dir[largestMovableIndex];
                    swap(perm, largestMovableIndex, swapIndex);
                    swap(dir, largestMovableIndex, swapIndex);

                    // Если число достигло границы или сосед больше его — поменять направление
                    for (int i = 0; i < n; i++) {
                        if (perm[i].compareTo(largestMovableValue) > 0) {
                            dir[i] = (dir[i] == -1) ? 1 : -1;
                        }
                    }

                    writeToFile(writer, perm);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new Result(-1);
            }
        }

        // Метод обмена значений в массиве
        private static void swap(String[] array, int i, int j) {
            String temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

        // Метод обмена значений в массиве для направления
        private static void swap(int[] dir, int i, int j) {
            int temp = dir[i];
            dir[i] = dir[j];
            dir[j] = temp;
        }
    }

    // Алгоритм инверсии вектора
    private static class InversionVectorMethod implements PermutationAlgorithm {
        @Override
        public Result generate(String[] array, String filePath) {
            long startTime = System.nanoTime();
            int n = array.length;
            int[] inversionVector = new int[n];
            String[] permutation = new String[n];

            try (BufferedWriter writer = (filePath != null) ? new BufferedWriter(new FileWriter(filePath)) : null) {
                while (true) {
                    buildPermutationFromInversionVector(permutation, inversionVector, array);
                    writeToFile(writer, permutation);

                    if (!nextInversionVector(inversionVector)) {
                        double elapsedTime = (System.nanoTime() - startTime) / 1_000_000.0;
                        return new Result(elapsedTime);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new Result(-1);
            }
        }

        // Построение перестановки из вектора инверсий
        private void buildPermutationFromInversionVector(String[] permutation, int[] inversionVector, String[] array) {
            int n = inversionVector.length;
            List<String> numbers = new ArrayList<>(Arrays.asList(array));

            for (int i = 0; i < n; i++) {
                permutation[i] = numbers.remove(inversionVector[i]);
            }
        }

        // Генерация следующего вектора инверсий
        private boolean nextInversionVector(int[] inversionVector) {
            int n = inversionVector.length;
            for (int i = n - 1; i >= 0; i--) {
                if (inversionVector[i] < n - 1 - i) {
                    inversionVector[i]++;
                    Arrays.fill(inversionVector, i + 1, n, 0);
                    return true;
                }
            }
            return false;
        }
    }

    // Метод выбора алгоритма
    public static Result runPermutationMethod(String method, String[] array, String filePath) {
        PermutationAlgorithm algorithm = algorithms.getOrDefault(method.toLowerCase(),
            () -> { throw new IllegalArgumentException("Неизвестный метод: " + method); }).get();
        return algorithm.generate(array, filePath);
    }

    // Вспомогательный метод для записи в файл
    private static void writeToFile(BufferedWriter writer, String[] array) throws IOException {
        if (writer != null) {
            writer.write(Arrays.toString(array));
            writer.newLine();
        }
    }

    private static void writeToFile(BufferedWriter writer, List<String> list) throws IOException {
        if (writer != null) {
            writer.write(list.toString());
            writer.newLine();
        }
    }

    // Класс для хранения результата
    public static class Result {
        public double time;

        public Result(double time) {
            this.time = time;
        }

        public double getTime() {
            return time;
        }

        @Override
        public String toString() {
            return "Elapsed time: " + time + " ms";
        }
    }
}
