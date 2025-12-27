package ca.jrvs.apps.grep;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class JavaGrepImp implements JavaGrep{

    final Logger logger = LoggerFactory.getLogger(JavaGrepImp.class);

    private String rootPath;
    private String regex;
    private String outFile;

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootpath outfile");
        }

        BasicConfigurator.configure();

        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try {
            javaGrepImp.process();
        } catch (Exception e) {
            javaGrepImp.logger.error("ERROR: unable to process", e);
        }
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getOutFile() {
        return outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    @Override
    public void process() throws IOException {
        List<String> matchedLines = new ArrayList<>();
        for(File file : listFiles(this.rootPath)){
            for(String line : readLines(file)){
                if (containsPattern(line)){
                    matchedLines.add(String.valueOf(file) + ": " + line);
                }
            }

        }
        this.logger.trace("Result written to: {}", outFile);
        writeToFile(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> files = new ArrayList<>();
        File root = new File(rootDir);
        // Check for rootDir validity
        if (!root.exists()){
            this.logger.error("ERROR: No such root directory: {}", rootDir);
        } else if (!root.isDirectory() || root.listFiles() == null) {
            return files;
        }

        // Traverse recursively
        for(File file : root.listFiles()) {
            if (file.isFile()){
                files.add(file);
            } else if (file.isDirectory()) {
                files.addAll(listFiles(file.getAbsolutePath()));
            }
        }
        return files;
    }

    @Override
    public List<String> readLines(File inputFile) throws IllegalArgumentException {
        // Check input file
        if (inputFile == null || !inputFile.isFile()) {
            throw new IllegalArgumentException("Invalid input file: " + inputFile);
        }

        // Read the file
        List<String> lines = new ArrayList<>();

        try (
                // Open a file with FileReader
                FileReader fr = new FileReader(inputFile);
                // Open a BufferedRender to read data chunks by chunks
                BufferedReader br = new BufferedReader(fr)
        ) {
            // Traverse the file
            String line = br.readLine();
            while (line != null){
                lines.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
        return lines;
    }

    @Override
    public boolean containsPattern(String line) {
        if (line == null) {
            return false;
        }

        return Pattern.compile(regex).matcher(line).find();
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        try (
                // Open a FileOutputStream to write bytes in a file
                FileOutputStream fos = new FileOutputStream(outFile);
                // Open an OutputStreamWriter to convert characters into bytes
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                // Open a BufferedWriter to write data in chunks
                BufferedWriter bw = new BufferedWriter(osw)
        ) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
        catch (IOException e) {
            throw new IOException("Failed to write output file: " + outFile, e);
        }
    }
}
