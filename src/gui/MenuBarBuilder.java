package gui;

import controller.EditorController;

import javax.swing.*;

public class MenuBarBuilder {

    private final EditorController controller;
  private EditorPanel editorPanel;
    public MenuBarBuilder(EditorController controller,
                          EditorPanel editorPanel) {

        this.controller = controller;
        this.editorPanel = editorPanel;

        System.out.println("Received EditorPanel = " + editorPanel);
        System.out.println("Stored EditorPanel = " + this.editorPanel);
    }

    public JMenuBar build() {

        JMenuBar menuBar =
                new JMenuBar();

        JMenu fileMenu =
                new JMenu("File");

        JMenu editMenu =
                new JMenu("Edit");

        JMenu helpMenu =
                new JMenu("Help");

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

            }
        });
        saveItem.addActionListener(e -> {

            JFileChooser chooser =
                    new JFileChooser();

            int result =
                    chooser.showSaveDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {

                controller.saveCurrentDocument(

                        chooser
                                .getSelectedFile()
                                .getAbsolutePath(),

                        editorPanel.getText()

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

            }

        });
        undoItem.addActionListener(e -> {

            controller.undo();

            editorPanel.setText(
                    controller.getText()
            );

        });

        redoItem.addActionListener(e -> {

            controller.redo();

            editorPanel.setText(
                    controller.getText()
            );

        });
        exitItem.addActionListener(e -> {

            System.exit(0);

        });

        return menuBar;
    }

}