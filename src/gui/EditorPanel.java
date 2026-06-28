package gui;

import controller.EditorController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class EditorPanel extends JPanel {

    private JTextArea textArea;

    private final EditorController controller;

    public EditorPanel(EditorController controller) {
        this.controller = controller;
        initializeComponents();
    }

    private void initializeComponents() {

        setLayout(new BorderLayout());

        textArea = new JTextArea();

        // Listen for every text change
//        textArea.getDocument().addDocumentListener(
//                new DocumentListener() {
//
//                    @Override
//                    public void insertUpdate(DocumentEvent e) {
//                        sync();
//                    }
//
//                    @Override
//                    public void removeUpdate(DocumentEvent e) {
//                        sync();
//                    }
//
//                    @Override
//                    public void changedUpdate(DocumentEvent e) {
//                        sync();
//                    }
//
//                    private void sync() {
//
//                        controller.updateText(
//                                textArea.getText()
//                        );
//                    }
//                });

        textArea.setFont(
                new Font(
                        "Consolas",
                        Font.PLAIN,
                        16
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(textArea);

        add(
                scrollPane,
                BorderLayout.CENTER
        );
    }
    public void setText(
            String text
    ){

        textArea.setText(
                text
        );

    }
    public void clear() {

        textArea.setText("");

    }
    public String getText(){

        return textArea.getText();

    }

    public JTextArea getTextArea() {
        return textArea;
    }

}
