package gui;

import controller.EditorController;
import undo.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class EditorPanel extends JPanel {

    private boolean programmaticUpdate = false;
    private JTextArea textArea;
    private final StatusBar statusBar;
    private final EditorController controller;

    private String lastText = "";

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
                        onTextChanged();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        onTextChanged();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        onTextChanged();
                    }

                    private void onTextChanged() {

                        if (programmaticUpdate) {
                            return;
                        }

                        String currentText =
                                textArea.getText();

                        int caretPos =
                                textArea.getCaretPosition();

                        recordEdit(
                                lastText,
                                currentText,
                                caretPos
                        );

                        lastText = currentText;

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

    private void recordEdit(
            String oldText,
            String newText,
            int caretPos
    ) {

        UndoManager undoManager =
                controller.getUndoManager();

        if (newText.length() > oldText.length()) {

            int insertLen =
                    newText.length() - oldText.length();

            int insertIndex =
                    caretPos - insertLen;

            if (insertIndex < 0) {
                insertIndex = 0;
            }

            String insertedText =
                    newText.substring(
                            insertIndex,
                            insertIndex + insertLen
                    );

            Command cmd = new InsertCommand(
                    controller.getDocument().getBuffer(),
                    insertIndex,
                    insertedText
            );

            undoManager.recordCommand(cmd);

        } else if (newText.length() < oldText.length()) {

            int deleteLen =
                    oldText.length() - newText.length();

            int deleteStart = caretPos;

            Command cmd = new DeleteCommand(
                    controller.getDocument().getBuffer(),
                    deleteStart,
                    deleteStart + deleteLen
            );

            undoManager.recordCommand(cmd);

        } else {

            // Same length = replace (e.g., paste over selection)

            int diffStart = 0;

            while (diffStart < oldText.length()
                    && oldText.charAt(diffStart) == newText.charAt(diffStart)) {
                diffStart++;
            }

            if (diffStart < oldText.length()) {

                int diffEnd = oldText.length();

                while (diffEnd > diffStart
                        && oldText.charAt(diffEnd - 1) == newText.charAt(diffEnd - 1)) {
                    diffEnd--;
                }

                Command cmd = new ReplaceCommand(
                        controller.getDocument().getBuffer(),
                        diffStart,
                        diffEnd,
                        newText.substring(diffStart, diffEnd)
                );

                undoManager.recordCommand(cmd);
            }
        }
    }

    public void clear() {

        programmaticUpdate = true;
        textArea.setText("");
        lastText = "";
        programmaticUpdate = false;
    }

    public String getText() {

        return textArea.getText();
    }

    public void undo() {

        UndoManager undoManager =
                controller.getUndoManager();

        if (undoManager.getUndoSize() > 0) {

            undoManager.undo();

            programmaticUpdate = true;

            String text =
                    controller.getDocument()
                            .getBuffer()
                            .getText();

            textArea.setText(text);
            lastText = text;

            programmaticUpdate = false;

            statusBar.updateStatus(
                    textArea,
                    controller.isModified()
            );
        }
    }

    public void redo() {

        UndoManager undoManager =
                controller.getUndoManager();

        if (undoManager.getRedoSize() > 0) {

            undoManager.redo();

            programmaticUpdate = true;

            String text =
                    controller.getDocument()
                            .getBuffer()
                            .getText();

            textArea.setText(text);
            lastText = text;

            programmaticUpdate = false;

            statusBar.updateStatus(
                    textArea,
                    controller.isModified()
            );
        }
    }

    public JTextArea getTextArea() {

        return textArea;
    }

    public void setText(String text) {

        programmaticUpdate = true;
        textArea.setText(text);
        lastText = text;
        programmaticUpdate = false;
    }
}