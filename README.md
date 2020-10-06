# How to use
## Step 1
Place the compiled `SimpleStringEncryptor.jar` into your project directory.

## Step 2
Copy the `string-encryptor.properties` file from this repository into your project directory.\
Change the `in` and `out` parameters:
- `in` should be your source directory. In a maven project this would be `src/main/java`.
- `out` will be the directory containing your modified source code with encrypted strings. Ex. `src/main/java_encrypted_strings`.

## Step 3
This step is for maven only. If you're using something else, please translate the code to work with your environment.

Create a file called `encrypt-strings.sh` containing the following code containing the following:
```shell script
# Execute the SimpleStringEncryptor program with a specific key
java -jar SimpleStringEncryptor.jar $1

# Move the original source folder to a temporary one
mv src/main/java src/main/java_original

# Move the encrypted strings source code to the maven source code folder
mv src/main/java_encrypted_strings src/main/java

# Build the project
mvn clean package

# Delete the java folder, this currently contains the modified source code with encrypted strings
rm -R src/main/java

# Copy the original source folder back to its original location
mv src/main/java_original src/main/java
```
