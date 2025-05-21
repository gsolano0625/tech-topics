import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Demo {
  public static void main(String[] args) {
//    List numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
//    List<Integer> numbers = Random.ints(1, 1, 1000).boxed().collect(Collectors.toList());

    Long result = LongStream.rangeClosed(1, 1000000000L).parallel().filter(number -> number % 3 != 0 && !String.valueOf(number).contains("3")).sum();
    System.out.println("Sum of numbers: " + result);

//    Long start = 0L;
//    long result = 0L;
//    for (int j = 1; j <= 1000L; j++) {
//      List<Long> numbers = new ArrayList<>();
//      Long i = 1 + start;
//      for (;i <= 1000000L; i++) {
//        numbers.add(i);
//      }
//      start = i - 1;
//      result = addNumbers(result, numbers);
//    }
//
////    System.out.println("Hello, world!");
//
//    System.out.println("Sum of numbers: " + result);


  }

  public int numPairs(int k, int[] items) {
    int pairs = 0;
    Map<Integer, Integer> map = new HashMap<>();

    for (int num : items) {
        map.put(num, map.getOrDefault(num, 0) + 1);

        pairs += map.get(num) % 2 == 0 ? 1 : 0;
    }

    return pairs;
  }


  int numPairsReduce(int k, int[] items) {
    Map<Integer, Integer> map = new HashMap<>();

    return Arrays.stream(items)
        .boxed()
        // .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
        .reduce(0, (acc, num) -> {
          map.put(num, map.getOrDefault(num, 0) + 1);
          return acc + (map.get(num) % 2 == 0 ? 1 : 0);
        });
  }

  public static Long addNumbers(Long start, List<Long> numbers) {
    long sum = start;

//    return numbers.stream().forEach((number) -> {
//      return sum += number
//    });

    for (Long number : numbers) {
      if (number % 3 != 0 && !String.valueOf(number).contains("3")) {
        sum += number;
      }
    }

    return sum;
  }
}
