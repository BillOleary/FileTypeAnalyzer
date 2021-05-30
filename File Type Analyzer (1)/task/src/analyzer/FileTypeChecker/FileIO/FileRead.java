package analyzer.FileTypeChecker.FileIO;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


public class FileRead implements DataReader {

    byte[] dataArray;

    public FileRead() {
    }

    /*20 Feb 2021
     * synchronized the file reader as we have multiple threads calling
     * the reader to process.
     * The thread holding the monitor lock to the FileRead Object gets to
     * access the reader.
     */
    @Override
    public synchronized StringBuffer readBinaryFile(Path fileToRead) throws FileNotFoundException {
        dataArray = new byte[(int) fileToRead.toFile().length()];
        StringBuffer fileAsUnicodeString = new StringBuffer();
        try (FileInputStream fis = new FileInputStream(fileToRead.toFile())) {
            while(fis.read(dataArray) != -1) {
            }
            IntStream.range(0, dataArray.length).forEach(element ->
                    fileAsUnicodeString.append((char) dataArray[element]));
        } catch (IOException ioXception) {
            ioXception.printStackTrace();
        }
        return fileAsUnicodeString;
    }

    @Override
    public List<String> readTextFile(String name) throws FileNotFoundException {
        List<String> listOfDataPatterns = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(name))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                listOfDataPatterns.add(currentLine);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return listOfDataPatterns;
    }
}


