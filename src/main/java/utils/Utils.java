package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Utils {
    public static void saveInFile(String thePath, String text)  {
        Path path = Paths.get(thePath);

        try {
            Files.createFile(path);
        } catch (Exception e) {
            File file = new File(path.toString());
            boolean bool = file.delete();

            if(bool) {
                try {
                    Files.createFile(path);
                }
                catch (IOException a) {
                    throw new RuntimeException("Não está sendo possível criar o arquivo.");
                }
            }
            else {
                throw new RuntimeException("O delete do arquivo falhou e o mesmo é existente.");
            }
        }

        try {
            Files.write(path, text.getBytes(), StandardOpenOption.WRITE);
        }
        catch (IOException e) {
            throw new RuntimeException("Não está sendo possível escrever no arquivo criado.");
        }
    }
}
