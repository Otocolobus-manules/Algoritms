package utils.greedy_algorithms;

import java.util.*;

public class Lab7Tasks {
    // Класс для представления заказа
    public static class Order {
        public int id;
        public int deadline;
        public int cost;

        public Order(int id, int deadline, int cost) {
            this.id = id;
            this.deadline = deadline;
            this.cost = cost;
        }

        @Override
        public String toString() {
            return String.format("Order(id=%d, deadline=%d, cost=%d)", id, deadline, cost);
        }
    }

    // Класс для представления ребенка
    public static class Child {
        public int id;
        public int age;

        public Child(int id, int age) {
            this.id = id;
            this.age = age;
        }

        @Override
        public String toString() {
            return String.format("Child(id=%d, age=%d)", id, age);
        }
    }

    // Класс для хранения результата и времени выполнения
    public static class Result<T> {
        public T result;
        public long executionTime;

        public Result(T result, long executionTime) {
            this.result = result;
            this.executionTime = executionTime;
        }
    }

    // Задача 1: Выбор заказов с максимальной суммарной стоимостью
    public static class MaxProfitScheduling {
        public Result<List<Order>> findMaxProfitSchedule(List<Order> orders) {
            long startTime = System.nanoTime();
            
            // Сортируем заказы по убыванию стоимости
            orders.sort((a, b) -> Integer.compare(b.cost, a.cost));
            
            int maxDeadline = orders.stream()
                                  .mapToInt(o -> o.deadline)
                                  .max()
                                  .orElse(0);
            
            // Массив для хранения расписания
            int[] schedule = new int[maxDeadline];
            Arrays.fill(schedule, -1);
            
            List<Order> selectedOrders = new ArrayList<>();
            
            // Для каждого заказа пытаемся найти для него место в расписании
            for (Order order : orders) {
                // Ищем самый поздний свободный слот до дедлайна
                for (int j = order.deadline - 1; j >= 0; j--) {
                    if (schedule[j] == -1) {
                        schedule[j] = order.id;
                        selectedOrders.add(order);
                        break;
                    }
                }
            }
            
            return new Result<>(selectedOrders, System.nanoTime() - startTime);
        }
    }

    // Задача 2: Группировка детей по возрасту
    public static class ChildrenGrouping {
        public Result<List<List<Child>>> groupChildren(List<Child> children) {
            long startTime = System.nanoTime();
            
            // Сортируем детей по возрасту
            children.sort(Comparator.comparingInt(a -> a.age));
            
            List<List<Child>> groups = new ArrayList<>();
            if (children.isEmpty()) {
                return new Result<>(groups, System.nanoTime() - startTime);
            }
            
            List<Child> currentGroup = new ArrayList<>();
            currentGroup.add(children.get(0));
            
            // Проходим по всем детям и формируем группы
            for (int i = 1; i < children.size(); i++) {
                Child currentChild = children.get(i);
                Child firstInGroup = currentGroup.get(0);
                
                // Если разница в возрасте больше 2 лет, создаем новую группу
                if (currentChild.age - firstInGroup.age > 2) {
                    groups.add(new ArrayList<>(currentGroup));
                    currentGroup.clear();
                }
                currentGroup.add(currentChild);
            }
            
            // Добавляем последнюю группу
            if (!currentGroup.isEmpty()) {
                groups.add(currentGroup);
            }
            
            return new Result<>(groups, System.nanoTime() - startTime);
        }
    }
} 