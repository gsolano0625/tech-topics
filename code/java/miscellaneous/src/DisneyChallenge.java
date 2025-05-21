import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
// Uncomment below classes to send network request if needed.
// import java.net.HttpURLConnection;
// import java.net.URL;


/*

The first line nunmer of test cases
the number of accounts
accounts

Repeat the test cases

input:

2
6
22 12345678 1234 1234 1234 1234
21 12345678 1234 1234 1234 1234
23 12345678 1234 1234 1234 1234
23 12345678 1234 1234 1234 1234
22 12345678 1234 1234 1234 1234
21 12345678 1234 1234 1234 1234

5
31 12345678 1234 1234 1234 1234
30 12345678 1234 1234 1234 1234
29 12345678 1234 1234 1234 1234
31 12345678 1234 1234 1234 1234
35 12345678 1234 1234 1234 1234

output:

21 12345678 1234 1234 1234 1234 2
22 12345678 1234 1234 1234 1234 2
23 12345678 1234 1234 1234 1234 2

29 12345678 1234 1234 1234 1234 1
30 12345678 1234 1234 1234 1234 1
31 12345678 1234 1234 1234 1234 2
35 12345678 1234 1234 1234 1234 1


 */


class Main {
  public static void main(String[] args ) throws Exception {
    StringBuilder inputData = new StringBuilder();
    String thisLine = null;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    while ((thisLine = br.readLine()) != null) {
      inputData.append(thisLine + "\n");
    }
    // Output the solution to the console
    System.out.println(codeHere(inputData));
  }

  public static String codeHere(StringBuilder inputData) {
    // Use this function to write your solution;
    StringTokenizer tokenizer = new StringTokenizer(inputData.toString(), "\n");
    StringBuilder outputData = new StringBuilder();
    int numTest = tokenizer.hasMoreTokens() ? Integer.parseInt(tokenizer.nextToken()) : 0;
    int testIdx = 0;

    // Loop for number of test
    while (tokenizer.hasMoreTokens() && testIdx < numTest ) {
      String nextLine = tokenizer.nextToken();

      // Empty line
      if (nextLine.equals("")) {
        System.out.println("testIdx: " + testIdx);
        outputData.append("\n");
        continue;
      }

      int numAccounts = Integer.parseInt(nextLine);
      String[] accStrings = new String[numAccounts];
      //List<String> accounts = new ArrayList<>();
      for (int idx = 0; idx < numAccounts; idx++) {
        if (tokenizer.hasMoreTokens()) {
          String acc = tokenizer.nextToken();

          // Debug
          //System.out.println("Account: " + acc);

          //accounts.add(acc);
          accStrings[idx] = acc;

          // Debug
          //System.out.println("accStrings[" + idx + "]: " + accStrings[idx]);
        }
        else {
          System.out.println("It doesn't get here");
          // Maybe throw an exception the input is invalid
        }
      }

      // Sort the array
      Arrays.sort(accStrings);
      //Arrays.sort(accounts);
      String currentAcc = null;
      int accCount = 0;
      for (String acc: accStrings) {
        //System.out.println("acc: " + acc);

        if (currentAcc == null || !acc.equals(currentAcc)) {
          // System.out.println("accCount: " + accCount);
          if (accCount > 0) {
            outputData.append(currentAcc + " " + String.valueOf(accCount) + "\n");
          }
          currentAcc = acc;
          accCount = 1;
        }
        else {
          accCount += 1;
        }
      }

      if (currentAcc != null && accCount > 0) {
        outputData.append(currentAcc + " " + String.valueOf(accCount) + "\n");
        outputData.append("\n");
      }

      testIdx += 1;
    }

    return outputData.toString();
  }
}