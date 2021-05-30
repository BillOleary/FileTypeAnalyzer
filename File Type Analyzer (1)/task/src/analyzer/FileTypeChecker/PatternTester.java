package analyzer.FileTypeChecker;

public interface PatternTester {
    //
    //Given a string s, and a pattern p, find the pattern p within the string
    //|s| >= |p|
    //
    void patternMatcher(String s, String p);
}
