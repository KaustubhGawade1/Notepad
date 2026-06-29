package gui.menu;

import controller.EditorController;
import gui.EditorPanel;
import gui.StatusBar;
import gui.dialog.FindDialog;
import javax.swing.*;

public class MenuBarBuilder {

    private final StatusBar statusBar;
    private final EditorController controller;
    private final EditorPanel editorPanel;
    private final FindDialog findDialog;

    public MenuBarBuilder(
            EditorController controller,
            EditorPanel editorPanel,
            StatusBar statusBar,
            FindDialog findDialog
    ) {

        this.controller = controller;
        this.editorPanel = editorPanel;
        this.statusBar = statusBar;
        this.findDialog = findDialog;
    }

    public JMenuBar build() {

        JMenuBar menuBar =
                new JMenuBar();

        JMenu fileMenu =
                new JMenu("File");

        JMenu editMenu =
                new JMenu("Edit");

        JMenu searchMenu =
                new JMenu("Search");

        JMenu helpMenu =
                new JMenu("Help");

        JMenuItem findItem =
                new JMenuItem("Find");

        JMenuItem newItem =
                new JMenuItem("New");

        JMenuItem openItem =
                new JMenuItem("Open");

        JMenuItem saveItem =
                new JMenuItem("Save");

        JMenuItem exitItem =
                new JMenuItem("Exit");

        JMenuItem undoItem =
                new JMenuItem("Undo");

        JMenuItem redoItem =
                new JMenuItem("Redo");

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        editMenu.add(undoItem);
        editMenu.add(redoItem);

        searchMenu.add(findItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(searchMenu);
        menuBar.add(helpMenu);

        /*
         * Actions
         */

        newItem.addActionListener(e -> {

            int option =
                    JOptionPane.showConfirmDialog(
                            null,
                            "Discard current document?",
                            "New Document",
                            JOptionPane.YES_NO_OPTION
                    );

            if (option == JOptionPane.YES_OPTION) {

                controller.clearDocument();
                controller.markSaved();
                editorPanel.clear();
            }
        });

        saveItem.addActionListener(e -> {

            JFileChooser chooser =
                    new JFileChooser();

            int result =
                    chooser.showSaveDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {

                controller.saveDocument(
                        chooser.getSelectedFile()
                                .getAbsolutePath(),

                        editorPanel.getText()
                );

                controller.markSaved();

                statusBar.updateStatus(
                        editorPanel.getTextArea(),
                        false
                );
            }
        });

        openItem.addActionListener(e -> {

            JFileChooser chooser =
                    new JFileChooser();

            int result =
                    chooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {

                controller.openDocument(
                        chooser
                                .getSelectedFile()
                                .getAbsolutePath()
                );

                controller.markSaved();

                editorPanel.setText(
                        controller.getText()
                );
            }
        });

        undoItem.addActionListener(e -> {

            editorPanel.undo();
        });

        redoItem.addActionListener(e -> {

            editorPanel.redo();
        });

        findItem.addActionListener(e -> {

            findDialog.setVisible(true);
        });

        exitItem.addActionListener(e -> {

            System.exit(0);
        });

        return menuBar;
    }
}