package me.woutergritter.stringencryptor.tasks;

import me.woutergritter.stringencryptor.Encrypt;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EncryptTask {
    public static void encrypt(File outSrc, File file, List<String> excludePackages, String decryptClassPackage) throws IOException {
        if(file.isDirectory()) {
            for(File other : file.listFiles()) {
                encrypt(outSrc, other, excludePackages, decryptClassPackage);
            }
        }

        if(!file.getName().endsWith(".java")) {
            return;
        }

        String filePath = file.getAbsolutePath();
        String fullClassName = filePath.substring(outSrc.getAbsolutePath().length() + 1, filePath.length() - 5)
                .replace(File.separator, ".");

        if(excludePackages.contains(fullClassName)) {
            return;
        }

        List<String> lines = new ArrayList<>();

        String _line;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while((_line = reader.readLine()) != null) {
            lines.add(_line);
        }
        reader.close();

        for(int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if(line.contains("case")) {
                continue;
            }

            List<String> strings = new ArrayList<>();

            boolean foundDoubleQuote = false;
            StringBuilder currentString = new StringBuilder();

            char lastChar = ' ';
            for(char c : line.toCharArray()) {
                if(c == '"' && lastChar != '\\') {
                    if(foundDoubleQuote) {
                        // We were in a string but now we aren't anymore
                        foundDoubleQuote = false;
                        strings.add(currentString.toString());
                        currentString = new StringBuilder();
                    }else{
                        // We weren't in a string but now we are
                        foundDoubleQuote = true;
                    }
                }else if(foundDoubleQuote) {
                    // Currently inside a string
                    currentString.append(c);
                }

                lastChar = c;
            }

            for(String str : strings) {
                if(str.contains("\\")) {
                    System.out.println("WARN: Found string containing '\\'. file: " + file.getName() + ", line: " + (i + 1));
                    System.out.println(str);
                }

                String unescapedString = str;
                unescapedString = unescapedString.replace("\\n", "\n");

                String encrypted = Encrypt.encrypt(unescapedString);
                String replacement = decryptClassPackage + ".Decrypt.decrypt(\"" + encrypted + "\")";

                line = line.replace('"' + str + '"', replacement);
            }

            lines.set(i, line);
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for(String line : lines) {
            writer.write(line + '\n');
        }
        writer.close();
    }
}
