package me.woutergritter.stringencryptor.tasks;

import me.woutergritter.stringencryptor.Main;

import java.io.*;

public class CopyDecryptClassTask {
    public static void copyDecryptClass(File outSrc, String decryptClassPackage, String keyProviderClass) throws IOException {
        File decryptClassPackageFile = new File(outSrc, decryptClassPackage.replace(".", "/"));
        File decryptClassFile = new File(decryptClassPackageFile, "Decrypt.java");

        BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/Decrypt.java")));
        BufferedWriter writer = new BufferedWriter(new FileWriter(decryptClassFile));

        String line;
        while((line = reader.readLine()) != null) {
            line = line.replace("%PACKAGE%", decryptClassPackage);
            line = line.replace("%KEY_PROVIDER_CLASS%", keyProviderClass);

            writer.write(line + '\n');
        }

        reader.close();
        writer.close();
    }
}
