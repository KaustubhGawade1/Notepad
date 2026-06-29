package gui.menu;

import controller.EditorController;
import gui.EditorPanel;
import gui.StatusBar;
import gui.dialog.FindDialog;
import javax.swing.*;

public class MenuBarBuilder {
    private final StatusBar statusBar;
    private final EditorController controller;
  private EditorPanel editorPanel;
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

        menuBar.add(searchMenu);

        JMenuItem findItem =
                new JMenuItem("Find");

        searchMenu.add(findItem);

        JMenuItem newItem =
                new JMenuItem("New");

        JMenuItem openItem =
                new JMenuItem("Open");
        JMenu helpMenu =
                new JMenu("Help");

        menuBar.add(helpMenu);
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

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        editMenu.add(undoItem);
        editMenu.add(redoItem);
        /*
         * Actions
         */

//        newItem.addActionListener(e -> {
//
//            controller.newDocument();
//
//        });

//        exitItem.addActionListener(e -> {
//
//            System.exit(0);
//
//        });
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

                editorPanel.clear();

                statusBar.updateStatus(
                        "",
                        false
                );

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

                statusBar.updateStatus(
                        editorPanel.getText(),
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

                editorPanel.setText(
                        controller.getText()
                );
                statusBar.updateStatus(
                        controller.getText(),
                        false
                );
            }

        });
        undoItem.addActionListener(e -> {

            editorPanel.undo();

        });

        redoItem.addActionListener(e -> {

            editorPanel.redo();

        });
        exitItem.addActionListener(e -> {

            System.exit(0);

        });

        return menuBar;
    }

}