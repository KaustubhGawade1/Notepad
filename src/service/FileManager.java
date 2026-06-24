package service;

import model.Document;
import java.io.*;

public class FileManager {

    public void save(Document document, String fileName) {

        try (FileWriter writer = new FileWriter(fileName)) {

            writer.write(
                    // 1st doubt how it works
                    document.getBuffer().getText()
            );

            document.setName(fileName);
            document.setModified(false);

            System.out.println("File saved successfully.");

        } catch (IOException e) {

            System.out.println("Error while saving file.");
        }
    }

    public void open(Document document, String fileName) {

        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(fileName))) {

            StringBuilder content =
                    new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {

                content.append(line)
                        .append("\n");
            }

            document.getBuffer().clear();

            document.getBuffer().insert(
                    0,
                    content.toString()
            );

            document.setName(fileName);
            document.setModified(false);

            System.out.println(
                    "File opened successfully."
            );

        } catch (IOException e) {

            System.out.println(
                    "Error while opening file."
            );
        }
    }
}