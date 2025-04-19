package utils.knapsack;

import java.util.*;

public class KnapsackProblem {
    // Класс для представления предмета
    public static class Item {
        public int id;
        public int weight;
        public int value;
        public double ratio;  // value/weight ratio для жадного алгоритма

        public Item(int id, int weight, int value) {
            this.id = id;
            this.weight = weight;
            this.value = value;
            this.ratio = (double) value / weight;
        }

        @Override
        public String toString() {
            return String.format("Item(id=%d, weight=%d, value=%d, ratio=%.2f)", 
                               id, weight, value, ratio);
        }
    }

    // Класс для хранения результата и времени выполнения
    public static class Result {
        public List<Item> selectedItems;
        public int totalValue;
        public int totalWeight;
        public long executionTime;

        public Result(List<Item> selectedItems, int totalValue, int totalWeight, long executionTime) {
            this.selectedItems = selectedItems;
            this.totalValue = totalValue;
            this.totalWeight = totalWeight;
            this.executionTime = executionTime;
        }
    }

    // Решение методом полного перебора
    public static class BruteForceKnapsack {
        private List<Item> bestSelection;
        private int bestValue;
        private int maxWeight;
        private List<Item> items;

        public Result solve(List<Item> items, int maxWeight) {
            long startTime = System.nanoTime();
            
            this.items = new ArrayList<>(items);
            this.maxWeight = maxWeight;
            this.bestValue = 0;
            this.bestSelection = new ArrayList<>();

            // Начинаем перебор с пустого набора
            tryAllCombinations(new ArrayList<>(), 0, 0, 0);

            int totalWeight = bestSelection.stream()
                                         .mapToInt(item -> item.weight)
                                         .sum();

            return new Result(bestSelection, bestValue, totalWeight, 
                            System.nanoTime() - startTime);
        }

        private void tryAllCombinations(List<Item> currentSelection, 
                                      int currentWeight, 
                                      int currentValue,
                                      int index) {
            // Если текущий набор лучше предыдущего лучшего, обновляем результат
            if (currentValue > bestValue && currentWeight <= maxWeight) {
                bestValue = currentValue;
                bestSelection = new ArrayList<>(currentSelection);
            }

            // Перебираем все оставшиеся предметы
            for (int i = index; i < items.size(); i++) {
                Item item = items.get(i);
                
                // Если добавление предмета не превысит максимальный вес
                if (currentWeight + item.weight <= maxWeight) {
                    currentSelection.add(item);
                    
                    // Рекурсивно пробуем добавить следующие предметы
                    tryAllCombinations(currentSelection, 
                                     currentWeight + item.weight,
                                     currentValue + item.value,
                                     i + 1);
                    
                    // Убираем предмет для следующей итерации
                    currentSelection.remove(currentSelection.size() - 1);
                }
            }
        }
    }

    // Решение жадным алгоритмом
    public static class GreedyKnapsack {
        public Result solve(List<Item> items, int maxWeight) {
            long startTime = System.nanoTime();
            
            // Создаем копию списка для сортировки
            List<Item> sortedItems = new ArrayList<>(items);
            
            // Сортируем предметы по убыванию отношения стоимость/вес
            sortedItems.sort((a, b) -> Double.compare(b.ratio, a.ratio));
            
            List<Item> selectedItems = new ArrayList<>();
            int currentWeight = 0;
            int totalValue = 0;
            
            // Жадно выбираем предметы
            for (Item item : sortedItems) {
                if (currentWeight + item.weight <= maxWeight) {
                    selectedItems.add(item);
                    currentWeight += item.weight;
                    totalValue += item.value;
                }
            }
            
            return new Result(selectedItems, totalValue, currentWeight, 
                            System.nanoTime() - startTime);
        }
    }
} 