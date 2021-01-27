package ru.danya;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Enter absolute path to required folder:");
        Scanner scanner = new Scanner(System.in);

        List<Path> paths = null;
        try {
            Optional<List<Path>> filesOpt = FileOps.findTextFilesInFolder(scanner.next());
            if (filesOpt.isPresent()) {
                paths = filesOpt.get();
            } else {
                System.out.println("Directory is empty");
                return;
            }
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Enter absolute path to output file:");
        try {
            Path result = FileOps.mergeTextFiles(paths, scanner.next());

            System.out.println("Content of result file:");
            Files.readAllLines(result, StandardCharsets.UTF_8).forEach(System.out::println);
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
