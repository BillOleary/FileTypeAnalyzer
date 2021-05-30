package analyzer;

import analyzer.FileTypeChecker.AnalysisStrategy.Analyzer;
import analyzer.FileTypeChecker.AnalysisStrategy.KMPAnalyzer;
import analyzer.FileTypeChecker.AnalysisStrategy.NaiveAnalyzer;
import analyzer.FileTypeChecker.AnalysisStrategy.RabinKarpAnalyzer;
import analyzer.FileTypeChecker.FileIO.DataReader;
import analyzer.FileTypeChecker.FileIO.FileRead;
import analyzer.FileTypeChecker.PatternMatcherContext;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

//
//
///*
// * Use of Strategy pattern to access two algorithms designs
// *   - naive solution
// *   - KMP algorithm
// * Use of the pattern makes the code easy to upgrade, less fragile.
// * To implement a new algorithm implement the interface "Analyzer".
// *
// */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {
            System.out.println("No Data Presented as Input Argument On The Command Line");
        } else {
            performCheck(args);
        }

    }//end main

    /*
     * arg[0] -> file path to test
     * arg[1] -> file Pattern to test
     */
    private static void performCheck(String[] args) throws InterruptedException {

        List<String> dataPatternsStringList;
        List<Path> myFileList;
        try {
            myFileList = Files.list(Paths.get(args[0])).collect(Collectors.toList());
//            System.out.println(myFileList);
            //Create a file reader
            DataReader fileRead = new FileRead();

            //List of data Patterns to check in the above data Files
            dataPatternsStringList = fileRead.readTextFile(args[1]);
//            System.out.println(dataPatternsStringList);
            //Convert the string in the dataPattens List to a DataPattern Object
            final List<DataPattern> dataPatterns = dataPatternsStringList.stream().
                    map(element -> {
                        String[] values = element.replaceAll("\"", "").split(";");
                        return new DataPattern(values[0], values[1], values[2]);
                    }).collect(Collectors.toList());

            //Creating a threaded system
            ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            //for each file in the list search for the pattern
            for (Path files : myFileList) {
                pool.submit(() -> {
                    StringBuffer fileAsUnicodeString = null;

                    try {
                        fileAsUnicodeString = fileRead.readBinaryFile(files);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    //Create a context
                    PatternMatcherContext pmContext = new PatternMatcherContext();
                    Analyzer analyzer;

                    switch ("RabinKarp") {
                        case "naive":
                        default:
                            analyzer = new NaiveAnalyzer();
                            break;

                        case "KMP":
                            analyzer = new KMPAnalyzer();
                            break;

                        case "RabinKarp" :
                            analyzer = new RabinKarpAnalyzer();
                            break;
                    }

                    //17 Feb 2021
                    //Pass the analysis type to be performed - decouple the client from the analysis.
                    //You can write your own analyser and as long as it is of PatternMatcherContext type
                    //you will be able to use the code below.
                    //reset the analyser by passing the code
                    //pmContext.setTheAnalyser(mySpecialAnalyserWhichIWrote)
                    pmContext.setTheAnalyzer(analyzer);

                    //Map DS stores the string as the key and the integer position of where in q
                    //the text the corresponding string is found.
                    Map<String, List<Integer>> theMatch = null;

                    //Loop through the Pattern List and search the files for the given Pattern
                    //Start looking from the back of the Pattern List - match the highest match
                    //and break.
//                    dataPatterns.forEach(element -> System.out.println(element.getFileType() + "\u2192" + element.getPattern()));
                    for (int index = dataPatterns.size() - 1; index >= 0; index--) {
                        //Start the search from the last pattern index
                        DataPattern currentPattern = dataPatterns.get(index);
//                        System.out.println(currentPattern.getFileType() + " " + currentPattern.getPattern());
                        theMatch =
                                pmContext.findTheMatch(fileAsUnicodeString,
                                        currentPattern.getPattern());
                        if (theMatch.get(currentPattern.getPattern()).size() > 0) {
                            System.out.println(files.getFileName() + ": " + currentPattern.getFileType());
                            break;
                        } else {
                            if (index == 0) {
                                System.out.println(files.getFileName() + ": Unknown file type");
                            }
                        }
                    }//end for
                }); //end Thread pool submit
            }//end for
            pool.awaitTermination(1, TimeUnit.SECONDS);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}//end class