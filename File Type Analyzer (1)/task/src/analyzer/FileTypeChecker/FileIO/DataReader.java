package analyzer.FileTypeChecker.FileIO;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;

public interface DataReader {
    StringBuffer readBinaryFile(Path name) throws FileNotFoundException;
    List<String> readTextFile(String name) throws FileNotFoundException;
}
