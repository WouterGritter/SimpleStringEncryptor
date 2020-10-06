package me.woutergritter.stringencryptor;

import me.woutergritter.stringencryptor.tasks.CleanTask;
import me.woutergritter.stringencryptor.tasks.CopyDecryptClassTask;
import me.woutergritter.stringencryptor.tasks.CopyFolderTask;
import me.woutergritter.stringencryptor.tasks.EncryptTask;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Main {
    private static final File PROPS_FILE = new File("string-encryptor.properties");

    public static void main(String[] args) throws IOException {
        String key = "test";
        if(args.length > 0) {
            key = args[0];
        }
        Encrypt.setKey(key);

        Properties props = new Properties();
        props.load(new FileInputStream(PROPS_FILE));

        File inSrc = new File(props.getProperty("in"));
        File outSrc = new File(props.getProperty("out"));

        List<String> excludePackages = Arrays.asList(
                props.getProperty("exclude").split(",")
        );

        String decryptClassPackage = props.getProperty("decrypt-class-package");
        String keyProviderClass = props.getProperty("key-provider-class");

        System.out.println("Key: " + key);
        System.out.println("In path: " + inSrc.getAbsolutePath());
        System.out.println("Out path: " + outSrc.getAbsolutePath());

        System.out.println("Excluding:");
        excludePackages.forEach(System.out::println);

        System.out.println("Decrypt class package: " + decryptClassPackage);
        System.out.println("Key provider: " + keyProviderClass);

        CleanTask.clean(outSrc);

        CopyFolderTask.copyFolder(inSrc, outSrc);

        EncryptTask.encrypt(outSrc, outSrc, excludePackages, decryptClassPackage);

        CopyDecryptClassTask.copyDecryptClass(outSrc, decryptClassPackage, keyProviderClass);

        System.out.println("SUCCESS");
    }
}
