import itertools

# Пример товаров и их цен
goods = [
    ("Ручка", 20),  # Название товара и его цена
    ("Тетрадь", 50),
    ("Карандаш", 10),
    ("Линейка", 30)
]

# Сумма, которую студент может потратить
budget = 100

# Функция для генерации всех возможных комбинаций
def generate_combinations(goods, budget):
    # Получаем только цены товаров
    prices = [price for _, price in goods]
    
    # Максимальное количество товаров, которые можно купить (определяется бюджетом)
    max_items = [budget // price for price in prices]
    
    # Сгенерируем все возможные комбинации товаров и их количеств
    all_combinations = []
    
    for item_counts in itertools.product(*(range(m+1) for m in max_items)):
        total_cost = sum(item_counts[i] * prices[i] for i in range(len(item_counts)))
        
        if total_cost <= budget:
            combination = [(goods[i][0], item_counts[i]) for i in range(len(item_counts))]
            all_combinations.append((combination, total_cost))
    
    return all_combinations

# Генерация всех возможных комбинаций
combinations = generate_combinations(goods, budget)

# Поиск комбинации с максимальным количеством товаров
max_count_combination = max(combinations, key=lambda x: sum(item[1] for item in x[0]))

# Вывод
print("Максимальная комбинация товаров:")
for item, count in max_count_combination[0]:
    if count > 0:
        print(f"{item}: {count} шт.")

print(f"Общая стоимость: {max_count_combination[1]} руб.")
