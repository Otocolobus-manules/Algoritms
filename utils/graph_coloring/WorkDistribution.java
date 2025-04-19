package utils.graph_coloring;

import java.util.*;

public class WorkDistribution {
    // Класс для представления результата и времени выполнения
    public static class Result<T> {
        public T result;
        public long executionTime;
        public int numColors;  // количество использованных цветов/механизмов

        public Result(T result, int numColors, long executionTime) {
            this.result = result;
            this.numColors = numColors;
            this.executionTime = executionTime;
        }
    }

    // Задача 1: Распределение работ между механизмами
    public static class WorkAssignment {
        private final boolean[][] compatibility;  // матрица совместимости работ
        private final int numWorks;              // количество работ
        
        public WorkAssignment(boolean[][] compatibility) {
            this.compatibility = compatibility;
            this.numWorks = compatibility.length;
        }

        public Result<int[]> solve() {
            long startTime = System.nanoTime();
            
            // Массив для хранения назначенных механизмов (цветов)
            int[] assignments = new int[numWorks];
            Arrays.fill(assignments, -1);
            
            // Список степеней вершин (количество конфликтов для каждой работы)
            List<WorkDegree> workDegrees = new ArrayList<>();
            for (int i = 0; i < numWorks; i++) {
                int degree = 0;
                for (int j = 0; j < numWorks; j++) {
                    if (!compatibility[i][j]) {
                        degree++;
                    }
                }
                workDegrees.add(new WorkDegree(i, degree));
            }
            
            // Сортируем работы по убыванию степени
            workDegrees.sort((a, b) -> Integer.compare(b.degree, a.degree));
            
            int maxColor = 0;
            
            // Назначаем механизмы (цвета) для каждой работы
            for (WorkDegree work : workDegrees) {
                // Множество уже использованных цветов для смежных работ
                Set<Integer> usedColors = new HashSet<>();
                
                // Собираем использованные цвета
                for (int j = 0; j < numWorks; j++) {
                    if (!compatibility[work.id][j] && assignments[j] != -1) {
                        usedColors.add(assignments[j]);
                    }
                }
                
                // Находим минимальный доступный цвет
                int color = 0;
                while (usedColors.contains(color)) {
                    color++;
                }
                
                assignments[work.id] = color;
                maxColor = Math.max(maxColor, color);
            }
            
            return new Result<>(assignments, maxColor + 1, System.nanoTime() - startTime);
        }
        
        private static class WorkDegree {
            int id;
            int degree;
            
            WorkDegree(int id, int degree) {
                this.id = id;
                this.degree = degree;
            }
        }
    }

    // Задача 2: Размещение грузов по контейнерам
    public static class ContainerPacking {
        private final List<Integer> weights;  // веса грузов
        private final int containerCapacity;  // вместимость контейнера
        
        public ContainerPacking(List<Integer> weights, int containerCapacity) {
            // Преобразуем все элементы в Integer
            this.weights = new ArrayList<>();
            for (Object w : weights) {
                if (w instanceof Number) {
                    this.weights.add(((Number) w).intValue());
                }
            }
            this.containerCapacity = containerCapacity;
        }
        
        public Result<List<List<Integer>>> solve() {
            long startTime = System.nanoTime();
            
            // Сортируем грузы по убыванию веса
            List<Integer> sortedWeights = new ArrayList<>(weights);
            sortedWeights.sort(Collections.reverseOrder());
            
            // Список контейнеров, каждый контейнер содержит список индексов грузов
            List<List<Integer>> containers = new ArrayList<>();
            containers.add(new ArrayList<>());  // Создаем первый контейнер
            
            // Текущая загрузка каждого контейнера
            List<Integer> containerLoads = new ArrayList<>();
            containerLoads.add(0);
            
            // Распределяем грузы по контейнерам
            for (int i = 0; i < sortedWeights.size(); i++) {
                int weight = sortedWeights.get(i);
                boolean packed = false;
                
                // Пытаемся добавить груз в существующий контейнер
                for (int j = 0; j < containers.size(); j++) {
                    if (containerLoads.get(j) + weight <= containerCapacity) {
                        containers.get(j).add(i);
                        containerLoads.set(j, containerLoads.get(j) + weight);
                        packed = true;
                        break;
                    }
                }
                
                // Если не удалось добавить в существующие контейнеры, создаем новый
                if (!packed) {
                    List<Integer> newContainer = new ArrayList<>();
                    newContainer.add(i);
                    containers.add(newContainer);
                    containerLoads.add(weight);
                }
            }
            
            return new Result<>(containers, containers.size(), System.nanoTime() - startTime);
        }
    }
} 