package JavaCompilerToolkit.Lexicographer.IO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Write to a file
 */
public class OutFile {
    private final File file;

    /**
     * Create or open a file to write to
     * @param location Location and filename of file to write to
     * @param create Create file if it does not exist
     * @throws IOException File not found or can not be written to
     */
    public OutFile(String location, boolean create) throws IOException {
        file = new File(location);
        if(create && !file.exists()){
                boolean ignored = file.createNewFile();
        }else if(!file.exists()){
            throw new IOException("File " + location + " does not exist.");
        }
        if(!file.canWrite()) throw new IOException("File " + location + " can not be written to.");
    }

    /**
     * Write to a file
     * @param file Java file object
     * Will create file if it does not exist
     * @throws IOException File not found or can not be written to
     */
    public OutFile(File file) throws IOException {
        this.file = file;
        if(!file.exists()){
            boolean ignored = file.createNewFile();
        }else if(!file.exists()){
            throw new IOException("File " + file.getName() + " does not exist.");
        }
        if(!file.canWrite()) throw new IOException("File " + file.getName() + " can not be written to.");
    }

    /**
     * Write text to a file
     * @param text Text to write
     * @throws IOException Problem writing text
     * @implNote Closes file when done. Do not writeText repeatedly, try to buffer string in memory and write it all at once.
     */
    public void writeText(String text) throws IOException{
        FileWriter writer = new FileWriter(file);
        writer.write(text);
        writer.close();
    }

    /**
     * Write raw data to file
     * @param data Data to write
     * @throws IOException Problem writing data
     * @implNote Closes file when done. Do not writeData repeatedly, try to buffer data in memory and write it all at once.
     */
    public void writeData(byte[] data) throws IOException {
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(data);
        stream.close();
    }
}
