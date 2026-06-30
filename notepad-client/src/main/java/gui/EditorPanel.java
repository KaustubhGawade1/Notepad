package gui;

import controller.EditorController;
import undo.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import javax.swing.text.BadLocationException;

public class EditorPanel extends JPanel {

    private boolean programmaticUpdate = false;
    private JTextArea textArea;
    private final StatusBar statusBar;
    private final EditorController controller;

    public EditorPanel(
            EditorController controller,
            StatusBar statusBar
    ) {

        this.controller = controller;
        this.statusBar = statusBar;

        initializeComponents();
    }

    private void initializeComponents() {

        setLayout(new BorderLayout());

        textArea = new JTextArea();

        textArea.getDocument().addDocumentListener(
                new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        if (programmaticUpdate) return;
                        
                        try {
                            String insertedText = e.getDocument()
                                    .getText(e.getOffset(), e.getLength());
                            
                            Command cmd = new InsertCommand(
                                    controller.getDocument().getBuffer(),
                                    e.getOffset(),
                                    insertedText
                            );
                            
                            controller.getUndoManager().executeCommand(cmd);
                            
                            onTextChanged();
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        if (programmaticUpdate) return;
                        
                        Command cmd = new DeleteCommand(
                                controller.getDocument().getBuffer(),
                                e.getOffset(),
                                e.getOffset() + e.getLength()
                        );
                        
                        controller.getUndoManager().executeCommand(cmd);
                        
                        onTextChanged();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        // Not used for plain text modifications
                    }

                    private void onTextChanged() {
                        controller.markModified();

                        statusBar.updateStatus(
                                textArea,
                                controller.isModified()
                        );
                    }
                });

        textArea.addCaretListener(e -> {

            statusBar.updateStatus(
                    textArea,
                    controller.isModified()
            );
        });

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

    public void clear() {
        programmaticUpdate = true;
        textArea.setText("");
        programmaticUpdate = false;
    }

    public String getText() {
        return textArea.getText();
    }

    public void undo() {
        UndoManager undoManager = controller.getUndoManager();

        if (undoManager.getUndoSize() > 0) {
            undoManager.undo();

            programmaticUpdate = true;
            String text = controller.getDocument().getBuffer().getText();
            textArea.setText(text);
            programmaticUpdate = false;

            statusBar.updateStatus(textArea, controller.isModified());
        }
    }

    public void redo() {
        UndoManager undoManager = controller.getUndoManager();

        if (undoManager.getRedoSize() > 0) {
            undoManager.redo();

            programmaticUpdate = true;
            String text = controller.getDocument().getBuffer().getText();
            textArea.setText(text);
            programmaticUpdate = false;

            statusBar.updateStatus(textArea, controller.isModified());
        }
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setText(String text) {
        programmaticUpdate = true;
        textArea.setText(text);
        programmaticUpdate = false;
    }
}