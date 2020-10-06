package me.woutergritter.stringencryptor.tasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class CopyFolderTask {
    public static void copyFolder(File inSrc, File outSrc) throws IOException {
        try (Stream<Path> stream = Files.walk(inSrc.toPath())) {
            stream.forEach(source -> copy(source, outSrc.toPath().resolve(inSrc.toPath().relativize(source))));
        }
    }

    private static void copy(Path source, Path dest) {
        try {
            Files.copy(source, dest, REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
