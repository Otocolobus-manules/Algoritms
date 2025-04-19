package utils.string_search;

import java.util.HashMap;
import java.util.Map;

public class StringSearch {
    // Класс для хранения результата и времени выполнения
    public static class Result {
        public int position;
        public long executionTime;
        public int comparisons;

        public Result(int position, long executionTime, int comparisons) {
            this.position = position;
            this.executionTime = executionTime;
            this.comparisons = comparisons;
        }
    }

    // Интерфейс для алгоритмов поиска подстроки
    public interface StringSearchAlgorithm {
        Result search(String text, String pattern);
    }

    // Наивный алгоритм поиска подстроки
    public static class NaiveSearch implements StringSearchAlgorithm {
        @Override
        public Result search(String text, String pattern) {
            long startTime = System.nanoTime();
            int comparisons = 0;
            
            int n = text.length();
            int m = pattern.length();
            
            for (int i = 0; i <= n - m; i++) {
                int j;
                for (j = 0; j < m; j++) {
                    comparisons++;
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        break;
                    }
                }
                if (j == m) {
                    return new Result(i, System.nanoTime() - startTime, comparisons);
                }
            }
            
            return new Result(-1, System.nanoTime() - startTime, comparisons);
        }
    }

    // Алгоритм Бойера-Мура
    public static class BoyerMooreSearch implements StringSearchAlgorithm {
        @Override
        public Result search(String text, String pattern) {
            long startTime = System.nanoTime();
            int comparisons = 0;
            
            int n = text.length();
            int m = pattern.length();
            
            // Предварительная обработка для правила плохого символа
            Map<Character, Integer> badChar = new HashMap<>();
            for (int i = 0; i < m; i++) {
                badChar.put(pattern.charAt(i), i);
            }
            
            int s = 0; // сдвиг шаблона относительно текста
            while (s <= n - m) {
                int j = m - 1;
                
                while (j >= 0 && pattern.charAt(j) == text.charAt(s + j)) {
                    comparisons++;
                    j--;
                }
                
                if (j < 0) {
                    return new Result(s, System.nanoTime() - startTime, comparisons);
                } else {
                    comparisons++;
                    int shift = badChar.getOrDefault(text.charAt(s + j), -1);
                    s += Math.max(1, j - shift);
                }
            }
            
            return new Result(-1, System.nanoTime() - startTime, comparisons);
        }
    }

    // Алгоритм Рабина-Карпа
    public static class RabinKarpSearch implements StringSearchAlgorithm {
        private static final int PRIME = 101;
        
        @Override
        public Result search(String text, String pattern) {
            long startTime = System.nanoTime();
            int comparisons = 0;
            
            int n = text.length();
            int m = pattern.length();
            
            // Вычисление хеша для pattern и первого окна text
            long patternHash = 0;
            long textHash = 0;
            long h = 1;
            
            for (int i = 0; i < m - 1; i++) {
                h = (h * 256) % PRIME;
            }
            
            for (int i = 0; i < m; i++) {
                patternHash = (256 * patternHash + pattern.charAt(i)) % PRIME;
                textHash = (256 * textHash + text.charAt(i)) % PRIME;
            }
            
            for (int i = 0; i <= n - m; i++) {
                if (patternHash == textHash) {
                    int j;
                    for (j = 0; j < m; j++) {
                        comparisons++;
                        if (text.charAt(i + j) != pattern.charAt(j)) {
                            break;
                        }
                    }
                    if (j == m) {
                        return new Result(i, System.nanoTime() - startTime, comparisons);
                    }
                }
                
                if (i < n - m) {
                    textHash = (256 * (textHash - text.charAt(i) * h) + text.charAt(i + m)) % PRIME;
                    if (textHash < 0) {
                        textHash += PRIME;
                    }
                }
            }
            
            return new Result(-1, System.nanoTime() - startTime, comparisons);
        }
    }

    // Алгоритм Кнута-Морриса-Пратта
    public static class KMPSearch implements StringSearchAlgorithm {
        @Override
        public Result search(String text, String pattern) {
            long startTime = System.nanoTime();
            int comparisons = 0;
            
            int n = text.length();
            int m = pattern.length();
            
            // Создание массива lps (longest prefix suffix)
            int[] lps = new int[m];
            computeLPSArray(pattern, lps);
            
            int i = 0; // индекс для text
            int j = 0; // индекс для pattern
            
            while (i < n) {
                comparisons++;
                if (pattern.charAt(j) == text.charAt(i)) {
                    j++;
                    i++;
                }
                
                if (j == m) {
                    return new Result(i - j, System.nanoTime() - startTime, comparisons);
                } else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                    if (j != 0) {
                        j = lps[j - 1];
                    } else {
                        i++;
                    }
                }
            }
            
            return new Result(-1, System.nanoTime() - startTime, comparisons);
        }
        
        private void computeLPSArray(String pattern, int[] lps) {
            int len = 0;
            int i = 1;
            lps[0] = 0;
            
            while (i < pattern.length()) {
                if (pattern.charAt(i) == pattern.charAt(len)) {
                    len++;
                    lps[i] = len;
                    i++;
                } else {
                    if (len != 0) {
                        len = lps[len - 1];
                    } else {
                        lps[i] = len;
                        i++;
                    }
                }
            }
        }
    }
} 