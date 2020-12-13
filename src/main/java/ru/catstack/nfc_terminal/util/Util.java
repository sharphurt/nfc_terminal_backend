package ru.catstack.nfc_terminal.util;

import org.apache.commons.io.FileUtils;
import ru.catstack.nfc_terminal.exception.ResourceNotFoundException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.Scanner;

public class Util {
    public static String imageToBase64(String imagePath) {
        try {
            var fileContent = FileUtils.readFileToByteArray(new File(imagePath));
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readFile(String filePath) {
        String text;
        try {
            var scanner = new Scanner(new File(filePath));
            text = scanner.useDelimiter("\\A").next();
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new ResourceNotFoundException("File", "path", filePath);
        }
        return text;
    }
}
