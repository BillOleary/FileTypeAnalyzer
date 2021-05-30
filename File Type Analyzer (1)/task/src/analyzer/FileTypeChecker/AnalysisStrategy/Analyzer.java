package analyzer.FileTypeChecker.AnalysisStrategy;

import java.util.List;
import java.util.Map;

/*24 September 2020
* The Interface is the face for a strategy Pattern with
* a single search method with two parameters
* @param text - The Input String to be searched
* @param pattern - The pattern to search within the text above.
* The method must return a list of index positions if a match is
* found.
* If no matches are found return -1.
 */
public interface Analyzer {

    Map<String, List<Integer>> search(StringBuffer text, String... pattern);
}
