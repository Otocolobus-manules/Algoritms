package utils.huffman;

import java.util.*;

public class HuffmanCoding {
    // Узел дерева Хаффмана
    public static class Node implements Comparable<Node> {
        public char character;      // символ (для листовых узлов)
        public int frequency;       // частота
        public Node left, right;    // левый и правый потомки
        public String code;         // код Хаффмана для символа
        
        // Конструктор для листового узла
        public Node(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
            this.code = "";
        }
        
        // Конструктор для внутреннего узла
        public Node(Node left, Node right) {
            this.frequency = left.frequency + right.frequency;
            this.left = left;
            this.right = right;
            this.code = "";
        }
        
        @Override
        public int compareTo(Node other) {
            return this.frequency - other.frequency;
        }
    }
    
    // Класс для хранения результата кодирования
    public static class EncodingResult {
        public Map<Character, String> codeTable;  // таблица кодов
        public String encodedText;                // закодированный текст
        public long encodingTime;                 // время кодирования
        public int originalSize;                  // размер исходного текста (в битах)
        public int compressedSize;               // размер сжатого текста (в битах)
        
        public EncodingResult(Map<Character, String> codeTable, 
                            String encodedText,
                            long encodingTime,
                            int originalSize,
                            int compressedSize) {
            this.codeTable = codeTable;
            this.encodedText = encodedText;
            this.encodingTime = encodingTime;
            this.originalSize = originalSize;
            this.compressedSize = compressedSize;
        }
    }
    
    // Класс для хранения результата декодирования
    public static class DecodingResult {
        public String decodedText;    // декодированный текст
        public long decodingTime;     // время декодирования
        
        public DecodingResult(String decodedText, long decodingTime) {
            this.decodedText = decodedText;
            this.decodingTime = decodingTime;
        }
    }
    
    private Node root;                          // корень дерева Хаффмана
    private Map<Character, String> codeTable;   // таблица кодов
    
    // Построение дерева Хаффмана и создание таблицы кодов
    private void buildHuffmanTree(String text) {
        // Подсчет частот символов
        Map<Character, Integer> frequencies = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencies.put(c, frequencies.getOrDefault(c, 0) + 1);
        }
        
        // Создание очереди с приоритетами для узлов
        PriorityQueue<Node> queue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            queue.offer(new Node(entry.getKey(), entry.getValue()));
        }
        
        // Построение дерева
        while (queue.size() > 1) {
            Node left = queue.poll();
            Node right = queue.poll();
            queue.offer(new Node(left, right));
        }
        
        root = queue.poll();
        
        // Создание таблицы кодов
        codeTable = new HashMap<>();
        generateCodes(root, "");
    }
    
    // Рекурсивная генерация кодов для каждого символа
    private void generateCodes(Node node, String code) {
        if (node != null) {
            node.code = code;
            if (node.left == null && node.right == null) {
                codeTable.put(node.character, code);
            }
            generateCodes(node.left, code + "0");
            generateCodes(node.right, code + "1");
        }
    }
    
    // Кодирование текста
    public EncodingResult encode(String text) {
        long startTime = System.nanoTime();
        
        // Построение дерева и таблицы кодов
        buildHuffmanTree(text);
        
        // Кодирование текста
        StringBuilder encoded = new StringBuilder();
        for (char c : text.toCharArray()) {
            encoded.append(codeTable.get(c));
        }
        
        // Вычисление размеров
        int originalSize = text.length() * 8;  // ASCII символы по 8 бит
        int compressedSize = encoded.length();
        
        return new EncodingResult(
            new HashMap<>(codeTable),
            encoded.toString(),
            System.nanoTime() - startTime,
            originalSize,
            compressedSize
        );
    }
    
    // Декодирование текста
    public DecodingResult decode(String encodedText, Map<Character, String> codeTable) {
        long startTime = System.nanoTime();
        
        // Создание обратной таблицы кодов
        Map<String, Character> reverseTable = new HashMap<>();
        for (Map.Entry<Character, String> entry : codeTable.entrySet()) {
            reverseTable.put(entry.getValue(), entry.getKey());
        }
        
        // Декодирование
        StringBuilder decoded = new StringBuilder();
        StringBuilder currentCode = new StringBuilder();
        
        for (char bit : encodedText.toCharArray()) {
            currentCode.append(bit);
            String code = currentCode.toString();
            
            if (reverseTable.containsKey(code)) {
                decoded.append(reverseTable.get(code));
                currentCode.setLength(0);
            }
        }
        
        return new DecodingResult(
            decoded.toString(),
            System.nanoTime() - startTime
        );
    }
} 