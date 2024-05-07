package org.example.sikulix.utils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

    public static byte[] readFileToBytes(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public static byte[] readFileToBytes(String filePath) throws IOException {
        return readFileToBytes(new File(filePath));
    }

    public static void writeBytesToFile(byte[] bytes, String filePath) throws IOException {
        Files.write(Path.of(filePath), bytes);
    }
}