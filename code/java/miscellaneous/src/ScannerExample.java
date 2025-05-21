import java.util.List;
import java.util.stream.Collectors;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.stream.Stream;

public class ScannerExample {

  public static void main(String... args) {
    String wordsAndNumbers = """
                Longing rusted furnace
                daybreak 17 benign 
                9 homecoming 1 
                freight car
                """;

    try (Scanner scanner = new Scanner(wordsAndNumbers)) {
      //Stream<MatchResult> list = scanner.findAll("benign");
      Stream<MatchResult> list = scanner.findAll(".*\\d+.*");
      List<String> result = list.map(MatchResult::group).collect(Collectors.toList());
      System.out.println(result);
      //scanner.findAll("benign").map(MatchResult::group).forEach(System.out::println);
    }
  }
}
