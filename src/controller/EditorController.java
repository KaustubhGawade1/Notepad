package controller;

import context.ApplicationContext;
import model.Document;
import service.FileManager;
import undo.UndoManager;

public class EditorController {

    private final Document document;
    private final FileManager fileManager;
    private final UndoManager undoManager;

    public EditorController(ApplicationContext context) {

        this.document =
                context.getBean(Document.class);

        this.fileManager =
                context.getBean(FileManager.class);

        this.undoManager =
                context.getBean(UndoManager.class);
    }

    /*
     * ------------------------
     * Document Operations
     * ------------------------
     */

    public void newDocument() {

        document.getBuffer().clear();

        document.setModified(false);

        document.setName("Untitled");
    }

    public void save(String fileName) {

        fileManager.save(
                document,
                fileName
        );
    }

    public void open(String fileName) {

        fileManager.open(
                document,
                fileName
        );
    }

    public String getText() {

        return document
                .getBuffer()
                .getText();
    }

    public void setText(String text) {

        document
                .getBuffer()
                .clear();

        document
                .getBuffer()
                .insert(
                        0,
                        text
                );

        document.setModified(true);
    }

    public void undo() {

        undoManager.undo();

        document.setModified(true);

    }

    public void redo() {

        undoManager.redo();

        document.setModified(true);

    }

    public Document getDocument() {
        return document;
    }
    public void saveCurrentDocument(String fileName, String text) {

        document.getBuffer().clear();

        document.getBuffer().insert(
                0,
                text
        );

        fileManager.save(
                document,
                fileName
        );
    }
    public void openDocument(String fileName) {

        fileManager.open(
                document,
                fileName
        );
    }
    public void clearDocument() {

        document.getBuffer().clear();

        document.setModified(false);

        document.setName("Untitled");
    }
}