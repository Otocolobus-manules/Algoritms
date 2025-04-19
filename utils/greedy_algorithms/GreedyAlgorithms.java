package utils.greedy_algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GreedyAlgorithms {
    // Класс для хранения результата и времени выполнения
    public static class Result {
        public List<?> result;
        public long executionTime;

        public Result(List<?> result, long executionTime) {
            this.result = result;
            this.executionTime = executionTime;
        }
    }

    // Класс для представления отрезка
    public static class Segment {
        public int start;
        public int end;

        public Segment(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "[" + start + ", " + end + "]";
        }
    }

    // Класс для представления заявки
    public static class Request {
        public int startTime;
        public int endTime;

        public Request(int startTime, int endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        @Override
        public String toString() {
            return "[" + startTime + ", " + endTime + "]";
        }
    }

    // Задача 1: Максимальное подмножество непересекающихся отрезков
    public static class MaxNonOverlappingSegments {
        public Result findMaxNonOverlapping(List<Segment> segments) {
            long startTime = System.nanoTime();
            
            if (segments.isEmpty()) {
                return new Result(new ArrayList<>(), System.nanoTime() - startTime);
            }

            // Сортируем отрезки по правому концу
            Collections.sort(segments, Comparator.comparingInt(s -> s.end));

            List<Segment> result = new ArrayList<>();
            result.add(segments.get(0));
            int lastEnd = segments.get(0).end;

            for (int i = 1; i < segments.size(); i++) {
                if (segments.get(i).start >= lastEnd) {
                    result.add(segments.get(i));
                    lastEnd = segments.get(i).end;
                }
            }

            return new Result(result, System.nanoTime() - startTime);
        }
    }

    // Задача 2: Максимальное количество допустимых заявок
    public static class MaxRequests {
        public Result findMaxRequests(List<Request> requests) {
            long startTime = System.nanoTime();
            
            if (requests.isEmpty()) {
                return new Result(new ArrayList<>(), System.nanoTime() - startTime);
            }

            // Сортируем заявки по времени окончания
            Collections.sort(requests, Comparator.comparingInt(r -> r.endTime));

            List<Request> result = new ArrayList<>();
            result.add(requests.get(0));
            int lastEndTime = requests.get(0).endTime;

            for (int i = 1; i < requests.size(); i++) {
                if (requests.get(i).startTime >= lastEndTime) {
                    result.add(requests.get(i));
                    lastEndTime = requests.get(i).endTime;
                }
            }

            return new Result(result, System.nanoTime() - startTime);
        }
    }

    // Задача 3: Минимальное покрытие интервала отрезками
    public static class MinCoverage {
        public Result findMinCoverage(List<Segment> segments, int L, int R) {
            long startTime = System.nanoTime();
            
            if (segments.isEmpty()) {
                return new Result(new ArrayList<>(), System.nanoTime() - startTime);
            }

            // Сортируем отрезки по левому концу
            Collections.sort(segments, Comparator.comparingInt(s -> s.start));

            List<Segment> result = new ArrayList<>();
            int currentPosition = L;
            int bestEnd = L;
            Segment bestSegment = null;

            for (Segment segment : segments) {
                if (segment.start <= currentPosition) {
                    if (segment.end > bestEnd) {
                        bestEnd = segment.end;
                        bestSegment = segment;
                    }
                } else {
                    if (bestSegment != null) {
                        result.add(bestSegment);
                        currentPosition = bestEnd;
                        bestSegment = null;
                        
                        if (currentPosition >= R) {
                            break;
                        }
                        
                        if (segment.start <= currentPosition) {
                            bestEnd = segment.end;
                            bestSegment = segment;
                        }
                    }
                }
            }

            if (bestSegment != null && bestEnd > currentPosition) {
                result.add(bestSegment);
                currentPosition = bestEnd;
            }

            if (currentPosition < R) {
                return new Result(new ArrayList<>(), System.nanoTime() - startTime);
            }

            return new Result(result, System.nanoTime() - startTime);
        }
    }
} 