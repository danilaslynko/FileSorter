package ru.danya;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileOps {

    public static Optional<List<Path>> findTextFilesInFolder(String path) throws IOException {
        Path parentDir = Paths.get(path);

        if (!Files.exists(parentDir) || !Files.isDirectory(parentDir)) {
            // по-хорошему, надо бы выбросить исключение DirectoryDoesntExistException
            // но пока ограничился FileNotFoundException
            throw new FileNotFoundException("Directory " + path + " does not exist");
        }

        // чтобы не возвращать null, вернем Optional
        // не будем городить велосипеды и воспользуемся NIO и Stream API
        return Optional.of(
                Files.walk(parentDir)
                        .filter(Files::isRegularFile)
                        .sorted((path1, path2) ->
                                        path1.getFileName().toString()
                                                .compareToIgnoreCase(
                                                        path2.getFileName().toString()))
                        .collect(Collectors.toList()));
    }

    public static Path mergeTextFiles(List<Path> filesToMerge, String pathForResultingFile)
            throws IOException {
        Path outputPath = Paths.get(pathForResultingFile);
        // стрим, в который засунем все считанные строчки
        Stream<String> totalLines = Stream.empty();

        if (!Files.exists(outputPath)) {
            Files.createDirectories(outputPath.getParent());
            Files.createFile(outputPath);
        }

        for (Path path : filesToMerge) {
            if (Files.exists(path) && Files.isReadable(path)) {
                // считывем строчки из файла в стрим и вставляем его в конец общего стрима
                totalLines = Stream.concat(totalLines, Files.lines(path));
            }
            else {
                throw new FileNotFoundException("File " + path + " does not exist");
            }
        }

        Files.write(outputPath, totalLines.collect(Collectors.toList()), StandardCharsets.UTF_8);
        return outputPath;
    }
}
