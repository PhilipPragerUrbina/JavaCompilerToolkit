package Lexicographer.IO;

import java.io.*;
import java.util.Scanner;

/**
 * Read a file
 */
public class InFile {
    private final File file;

    /**
     * Open a file to read from
     * @param location Location and filename of file to read from
     * @throws IOException File not found or can not be read
     */
    public InFile(String location) throws IOException {
        file = new File(location);
        if(!file.exists()){
            throw new IOException("File " + location + " does not exist.");
        } else if(!file.canRead()) throw new IOException("File " + location + " can not be read.");
    }

    /**
     * Create file to read from
     * @param file Java file object
     * @throws IOException File not found or can not be read
     */
    public InFile(File file) throws IOException {
        this.file = file;
        if(!file.exists()){
            throw new IOException("File " + file.getName() + " does not exist.");
        } else if(!file.canRead()) throw new IOException("File " + file.getName() + " can not be read.");
    }

    /**
     * Get file extension of file
     * @return File extension(without .) or null if not found
     */
    public String getFileExtension(){
        int index = file.getName().lastIndexOf('.');
        return index > 0 ? file.getName().substring(index+1) : null;
    }


    /**
     * Read text file
     * @return Contents of file
     * @throws IOException Problem reading file
     */
    public String readText() throws IOException {
        StringBuilder builder = new StringBuilder();
        Scanner reader = new Scanner(file);
        while (reader.hasNextLine()) {
            builder.append(reader.nextLine()).append('\n');
        }
        reader.close();
        return builder.toString();
    }
}
