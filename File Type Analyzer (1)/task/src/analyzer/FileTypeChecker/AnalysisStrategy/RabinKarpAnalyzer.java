package analyzer.FileTypeChecker.AnalysisStrategy;

import java.util.*;

public class RabinKarpAnalyzer implements Analyzer{

    @Override
    public Map<String, List<Integer>> search(StringBuffer text, String... pattern) {
        Map<String, List<Integer>> mapOfMatches = new HashMap<>();
        for (String patternValue : pattern) {
            mapOfMatches.put(patternValue, rabinKarp(text, patternValue));
//            System.out.println(mapOfMatches);
        }
        return mapOfMatches;
    }

    /*
    *   Use of Polynomial Hash Function
    *   h(s) -> ( s0 * p^(m-1) + s1 * p ^ (m - 2) + ......+ s(m-1) * p ^ 0 ) % m
    *   Calculate the first hash(0)
    *   Horners Rule
    *   h(0) = (s0 * p + s1) * p + s2) * p + ..... + ))) * p + s(m - 1) % m
    *
    *   Alternative (to prevent Overflow)
    *   (a + b) % m = (a % m + b % m + m ) % m
    *
    *
    *
     */

    public List<Integer> rabinKarp(StringBuffer text, String pattern) {

        int a = 53;                     //range of characters
        long m = 1_000_000_000 + 9;     //Big Prime to reduce hash collision

        long pow = 1;
        long currentHash = 0;
        long patternHash = 0;
        int patternLength = pattern.length();
        int textLength = text.length();
        ArrayList<Integer> matches = new ArrayList<>();

//        System.out.print(textLength + " " + patternLength);
        if (patternLength > textLength) {
            return matches;
        }

        //Find the Hash for the pattern and the first k chars in the text
        for (int index = patternLength - 1; index >= 0; index--) {
//            System.out.println("\t" + text.charAt(index) + " = " + pattern.charAt(index));
            currentHash = (currentHash + (text.charAt(index) - ' ' + 1) * pow % m + m) % m;
            patternHash = (patternHash + (pattern.charAt(index) - ' ' + 1) * pow % m + m) % m;
//            System.out.println("Pattern Hash h(" + pattern.substring(index, patternLength) + ") = " +
//                    patternHash);

            //Stop the calculation at the last index
            if (index > 0) {
                pow = (pow * a) % m;
            }
        }

        for (int textIndex = patternLength; textIndex < textLength - 1; textIndex++) {
            //Test if the currentHash matches the patternHash
            if (currentHash == patternHash) {
                //Do a character by character test
                for (int patIndex = 0; patIndex < patternLength; patIndex++) {
//                    System.out.println("\tTesting Chars \u2192 " +
//                            text.charAt(patIndex + textIndex - 1) +
//                            " = " +
//                            pattern.charAt(patIndex));

                    if (text.charAt(textIndex - patIndex - 1) != pattern.charAt(patternLength - patIndex - 1)) {
                        break;
                    } else {
                        if (patIndex == patternLength - 1) {
                            matches.add(textIndex);
                        }
                    }
                }
            }

            //Calculate the new Hash values using polynomial rolling hash
            int firstCharToRemoveIndex = textIndex - patternLength;

            currentHash = ((currentHash - (text.charAt(firstCharToRemoveIndex) - ' ' + 1) * pow % m + m) * a +
                                            text.charAt(textIndex) - ' ' + 1) % m;
//            System.out.println("H(" + text.substring(firstCharToRemoveIndex + 1, textIndex + 1) + ") = " + currentHash + " " +
//                    "H(" + pattern + ") = " + patternHash);
        }
        return matches;
    }

}
