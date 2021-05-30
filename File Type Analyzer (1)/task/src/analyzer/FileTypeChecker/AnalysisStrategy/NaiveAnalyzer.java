package analyzer.FileTypeChecker.AnalysisStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveAnalyzer implements Analyzer {

    Map<String, List<Integer>> sequenceOfMatches = new HashMap<>();

    @Override
    public Map<String, List<Integer>> search(StringBuffer text, String... pattern) {
        for (String p : pattern) {
            List<Integer> listMatch;
            listMatch = naiveAlgorithm(text, p);
            sequenceOfMatches.put(p, listMatch);
        }
        return sequenceOfMatches;
    }

    public List<Integer> naiveAlgorithm(StringBuffer t, String p) {
        List<Integer> listOfMatches = new ArrayList<>();
        int textLength = t.length();
        int patternLength = p.length();

        for (int textIndex = 0; textIndex < textLength - patternLength + 1; textIndex++) {
            for (int patternIndex = 0; patternIndex < patternLength; patternIndex++ ) {
                if (!(t.charAt(patternIndex + textIndex) == p.charAt(patternIndex))) {
                    break;
                } else {
                    if (patternIndex == patternLength - 1) {
                        listOfMatches.add(textIndex);
                    }
                }
            }
        }
        return listOfMatches;
    }
}
