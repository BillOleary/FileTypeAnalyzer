package analyzer.FileTypeChecker;

import analyzer.FileTypeChecker.AnalysisStrategy.Analyzer;

import java.util.List;
import java.util.Map;

public class PatternMatcherContext {

    Analyzer analyzer;

    public void setTheAnalyzer(Analyzer analyzer) {
        try {
            this.analyzer = analyzer;
        }
        catch (NullPointerException npXception) {
            System.out.println("Naughty Naughty! No Null values please!!!");
        }
    }

    /*
    * Pass the parameters to the relevant matcher to find the pattern match
     */
    public Map<String,List<Integer>> findTheMatch(StringBuffer t, String p) {
        return this.analyzer.search(t, p);
    }


}
