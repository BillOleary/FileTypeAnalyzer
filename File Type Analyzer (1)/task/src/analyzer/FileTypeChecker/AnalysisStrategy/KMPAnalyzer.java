package analyzer.FileTypeChecker.AnalysisStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KMPAnalyzer implements Analyzer {
    //Keep a list of the Matches for the given String
    Map<String, List<Integer>> sequenceOfMatches = new HashMap<>();

    @Override
    public Map<String, List<Integer>> search(StringBuffer text, String... pattern) {
        //System.out.println("String to be Tested \u2192 " + text.toString());
        for(String p : pattern) {
            List<Integer> listMatch;
            listMatch = kmp(text, p);
            sequenceOfMatches.put(p, listMatch);
        }
        return sequenceOfMatches;
    }

    private int[] lps(String p) {

        int pLength = p.length();
        int[] prefix = new int[pLength];
        int i = 1;
        int j = 0;
        for (; i < pLength; i++) {
            j = prefix[i - 1];
            while (j > 0 && p.charAt(i) != p.charAt(j)) {
                j = prefix[j - 1];
            }
            if (p.charAt(i) == p.charAt(j)) {
                j++;
            }
            prefix[i] = j;
        }
        return prefix;
    }

    private List<Integer> kmp(StringBuffer t, String p) {

        List<Integer> indexMatchList = new ArrayList<>();
        int[] failFunction;
        int j = 0;
        failFunction = lps(p);

        for (int i = 0; i < t.length(); i++) {
            while (j > 0 && t.charAt(i) != p.charAt(j)) {
                j = failFunction[j - 1];
            }
            if (t.charAt(i) == p.charAt(j)) {
                j++;
            }
            if (j == p.length()) {
                //add the index to the list
                indexMatchList.add(i - j + 1);
                //reset the lps index
                j = failFunction[j - 1];
                break;
            }
        }
        return indexMatchList;
    }
}
