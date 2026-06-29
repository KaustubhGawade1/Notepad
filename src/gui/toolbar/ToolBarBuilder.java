package gui.toolbar;

import controller.EditorController;
import gui.EditorPanel;
import gui.dialog.FindDialog;

import javax.swing.*;

public class ToolBarBuilder {

    private final EditorController controller;
    private final EditorPanel editorPanel;
    private final FindDialog findDialog;
  public ToolBarBuilder(
            EditorController controller,
            EditorPanel editorPanel,
            FindDialog findDialog
    ) {

        this.controller = controller;
        this.editorPanel = editorPanel;
        this.findDialog = findDialog;
    }

    public JToolBar build() {

        JToolBar toolBar =
                new JToolBar();

        JButton newButton =
                new JButton("New");

        JButton openButton =
                new JButton("Open");

        JButton saveButton =
                new JButton("Save");

        JButton undoButton =
                new JButton("Undo");

        JButton redoButton =
                new JButton("Redo");

        JButton findButton =
                new JButton("Find");

        toolBar.add(newButton);
        toolBar.add(openButton);
        toolBar.add(saveButton);

        toolBar.addSeparator();

        toolBar.add(undoButton);
        toolBar.add(redoButton);

        toolBar.addSeparator();

        toolBar.add(findButton);

        /*
         * Actions
         */

       // newButton.doClick();

        newButton.addActionListener(e -> {

            controller.clearDocument();

            editorPanel.clear();

        });

        openButton.addActionListener(e -> {

            JFileChooser chooser =
                    new JFileChooser();

            if (chooser.showOpenDialog(null)
                    == JFileChooser.APPROVE_OPTION) {

                controller.openDocument(
                        chooser.getSelectedFile()
                                .getAbsolutePath()
                );

                editorPanel.setText(
                        controller.getText()
                );

            }

        });

        saveButton.addActionListener(e -> {

            JFileChooser chooser =
                    new JFileChooser();

            if (chooser.showSaveDialog(null)
                    == JFileChooser.APPROVE_OPTION) {

                controller.saveDocument(

                        chooser.getSelectedFile()
                                .getAbsolutePath(),

                        editorPanel.getText()

                );

            }

        });

        undoButton.addActionListener(e -> {

            editorPanel.undo();
        });

        redoButton.addActionListener(e -> {

            editorPanel.redo();

        });

        findButton.addActionListener(e -> {

            findDialog.setVisible(true);

        });

        return toolBar;

    }

}
