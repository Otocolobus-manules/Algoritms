package utils.shopping;

import java.util.*;

public class ShoppingOptimizer {
    // Класс для представления товара
    public static class Item {
        public String name;        // название товара
        public double price;       // цена за единицу
        public int required;       // требуемое количество
        public int purchased;      // купленное количество
        
        public Item(String name, double price, int required) {
            this.name = name;
            this.price = price;
            this.required = required;
            this.purchased = 0;
        }
        
        public double getTotalCost() {
            return price * purchased;
        }
        
        @Override
        public String toString() {
            return String.format("%s (цена: %.2f руб., требуется: %d, куплено: %d)",
                               name, price, required, purchased);
        }
    }
    
    // Класс для хранения результата оптимизации
    public static class OptimizationResult {
        public List<Item> items;           // список товаров с результатами
        public double totalSpent;          // потраченная сумма
        public double remainingBudget;     // оставшийся бюджет
        public int uniqueItemsPurchased;   // количество различных купленных товаров
        public long executionTime;         // время выполнения в наносекундах
        
        public OptimizationResult(List<Item> items, double totalSpent, 
                                double remainingBudget, int uniqueItemsPurchased,
                                long executionTime) {
            this.items = items;
            this.totalSpent = totalSpent;
            this.remainingBudget = remainingBudget;
            this.uniqueItemsPurchased = uniqueItemsPurchased;
            this.executionTime = executionTime;
        }
    }
    
    /**
     * Оптимизирует покупки для максимизации количества различных наименований
     * @param items список товаров с ценами и требуемыми количествами
     * @param budget доступный бюджет
     * @return результат оптимизации
     */
    public OptimizationResult optimize(List<Item> items, double budget) {
        long startTime = System.nanoTime();
        
        // Сортируем товары по возрастанию цены за единицу
        items.sort(Comparator.comparingDouble(item -> item.price));
        
        double totalSpent = 0;
        int uniqueItemsPurchased = 0;
        
        // Первый проход: пытаемся купить хотя бы по одной единице каждого товара
        for (Item item : items) {
            if (totalSpent + item.price <= budget) {
                item.purchased = 1;
                totalSpent += item.price;
                uniqueItemsPurchased++;
            }
        }
        
        // Второй проход: распределяем оставшийся бюджет
        boolean canBuyMore;
        do {
            canBuyMore = false;
            for (Item item : items) {
                if (item.purchased > 0 && item.purchased < item.required) {
                    if (totalSpent + item.price <= budget) {
                        item.purchased++;
                        totalSpent += item.price;
                        canBuyMore = true;
                    }
                }
            }
        } while (canBuyMore);
        
        return new OptimizationResult(
            new ArrayList<>(items),
            totalSpent,
            budget - totalSpent,
            uniqueItemsPurchased,
            System.nanoTime() - startTime
        );
    }
} 