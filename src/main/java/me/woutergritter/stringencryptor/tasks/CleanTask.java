package me.woutergritter.stringencryptor.tasks;

import java.io.File;

public class CleanTask {
    public static void clean(File outSrc) {
        if(outSrc.exists()) {
            deleteDirectory(outSrc);
        }
    }

    private static void deleteDirectory(File file) {
        File[] contents = file.listFiles();
        if(contents != null) {
            for(File other : contents) {
                deleteDirectory(other);
            }
        }

        file.delete();
    }
}
