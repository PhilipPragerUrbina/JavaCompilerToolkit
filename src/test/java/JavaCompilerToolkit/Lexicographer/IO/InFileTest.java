package JavaCompilerToolkit.Lexicographer.IO;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class InFileTest {
    @TempDir(cleanup = CleanupMode.ON_SUCCESS) File temporary_directory;

    @BeforeEach
    void setUp() throws IOException{
        File readable = new File(temporary_directory, "readable.txt");
        readable.setReadable(true);
        FileWriter writer = new FileWriter(readable);
        writer.write("foo \n bar \n");
        writer.close();
        File not_readable = new File(temporary_directory, "not_readable.txt");
        not_readable.setReadable(false);
    }

    @Test
    void TestInFile(){
        assertThrows(IOException.class, () -> {
                InFile not_exist =   new InFile(temporary_directory.getAbsolutePath() + "/not_exist.txt" );
        });

        assertThrows(IOException.class, () -> {
            InFile not_readable =   new InFile(temporary_directory.getAbsolutePath() + "/not_readable.txt" );
        });

       assertDoesNotThrow(() -> {
           InFile readable =   new InFile(temporary_directory.getAbsolutePath() + "/readable.txt" );
           assertEquals("foo \n bar \n", readable.readText());
           assertEquals("txt",readable.getFileExtension());
       });

    }
}