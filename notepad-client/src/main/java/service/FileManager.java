package service;

import model.Document;
import javax.swing.JOptionPane;
import java.io.*;

public class FileManager {

    public void save(Document document, String fileName) {

        try (FileWriter writer = new FileWriter(fileName)) {

            writer.write(
                    document.getBuffer().getText()
            );

            document.setName(fileName);
            document.setModified(false);

            JOptionPane.showMessageDialog(
                    null,
                    "File saved successfully.",
                    "Save",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error while saving file.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
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

            JOptionPane.showMessageDialog(
                    null,
                    "File opened successfully.",
                    "Open",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error while opening file.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}