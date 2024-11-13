import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Random;
import java.util.Collections;

public class DataProcessing {

    public static void main(String[] args) {
        List<Integer> largeDataset = new Random().ints(1_000_000, 1, 100)
                .boxed().collect(Collectors.toList());

        System.out.println("Collection API Processing");
        CollectionAPIProcessing collectionAPIProcessing = new CollectionAPIProcessing();
        collectionAPIProcessing.processCollection();

        System.out.println("\nStream API Processing");
        StreamAPIProcessing streamAPIProcessing = new StreamAPIProcessing();
        streamAPIProcessing.processStream();

        System.out.println("\nCollection API Processing Large Dataset");
        System.out.println("filtering size: " + collectionAPIProcessing.processLargeDataset(largeDataset).size());

        System.out.println("\nStream API Processing Large Dataset");
        System.out.println("filtering size: " + streamAPIProcessing.processLargeDataset(largeDataset).size());

        System.out.println("\nSorting using Collection API");
        System.out.println("Sorting list size: " + collectionAPIProcessing.sortList(largeDataset).size());

        System.out.println("\nSorting using Stream API");
        System.out.println("Sorting list size: " + streamAPIProcessing.sortList(largeDataset).size());

        System.out.println("\nSumming using Collection API");
        System.out.println("Sum: " + collectionAPIProcessing.sumList(largeDataset));

        System.out.println("\nSumming using Stream API");
        System.out.println("Sum: " + streamAPIProcessing.sumList(largeDataset));

        System.out.println("\nParallel Sorting using Stream API");
        System.out.println("Sorting list size: " + streamAPIProcessing.getParallelSortedList(largeDataset).size());
    }
}

class CollectionAPIProcessing {

    public void processCollection() {

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> evenNumbers = new ArrayList<>();

        for (Integer num : numbers) {
            if (num % 2 == 0) {
                evenNumbers.add(num);
            }
        }

        System.out.println(evenNumbers); // Output: [2, 4, 6, 8, 10]
    }

    List<Integer> processLargeDataset(List<Integer> largeDataset) {
        List<Integer> filteredList = new ArrayList<>();
        for (Integer num : largeDataset) {
            if (num > 50) {
                filteredList.add(num);
            }
        }

        return filteredList;
    }

    List<Integer> sortList(List<Integer> largeDataset) {
        List<Integer> sortedList = new ArrayList<>(largeDataset);
        Collections.sort(sortedList);
        return sortedList;
    }

    Integer sumList(List<Integer> largeDataset) {
        int sum = 0;
        for (Integer num : largeDataset) {
            sum += num;
        }

        return sum;
    }
}

class StreamAPIProcessing {

    public void processStream() {

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        List<Integer> evenNumbers = numbers.stream()
                .filter(num -> num % 2 == 0)
                .collect(Collectors.toList());

        System.out.println(evenNumbers); // Output: [2, 4, 6, 8, 10]
    }

    List<Integer> processLargeDataset(List<Integer> largeDataset) {
        return largeDataset.stream()
                .filter(num -> num > 50)
                .collect(Collectors.toList());
    }

    List<Integer> sortList(List<Integer> largeDataset) {
        return largeDataset.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    Integer sumList(List<Integer> largeDataset) {
        return largeDataset.stream()
                .reduce(0, Integer::sum);
    }

    List<Integer> getParallelSortedList(List<Integer> largeDataset) {
        return largeDataset.parallelStream()
                .filter(num -> num > 50)
                .sorted()
                .collect(Collectors.toList());
    }
}
