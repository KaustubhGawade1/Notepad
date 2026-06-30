package gui.toolbar;

import controller.EditorController;
import gui.EditorPanel;
import gui.StatusBar;
import gui.dialog.CloudDocumentsDialog;
import gui.dialog.CloudLoginDialog;
import gui.dialog.FindDialog;
import service.DocumentApiClient;

import javax.swing.*;
import java.awt.*;

public class ToolBarBuilder {

    private final EditorController controller;
    private final EditorPanel editorPanel;
    private final FindDialog findDialog;
    private final StatusBar statusBar;
    private final DocumentApiClient documentApiClient;
    private final Frame parentFrame;

    public ToolBarBuilder(
            EditorController controller,
            EditorPanel editorPanel,
            FindDialog findDialog,
            StatusBar statusBar,
            DocumentApiClient documentApiClient,
            Frame parentFrame
    ) {
        this.controller = controller;
        this.editorPanel = editorPanel;
        this.findDialog = findDialog;
        this.statusBar = statusBar;
        this.documentApiClient = documentApiClient;
        this.parentFrame = parentFrame;
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

        JButton saveAsButton =
            new JButton("Save As");

        JButton undoButton =
                new JButton("Undo");

        JButton redoButton =
                new JButton("Redo");

        JButton cloudLoginButton =
                new JButton("Cloud Login");

        JButton saveCloudButton =
                new JButton("Save Cloud");

        JButton openCloudButton =
                new JButton("Open Cloud");

        JButton findButton =
                new JButton("Find");

        toolBar.add(newButton);
        toolBar.add(openButton);
        toolBar.add(saveButton);
        toolBar.add(saveAsButton);
        toolBar.addSeparator();
        toolBar.add(cloudLoginButton);
        toolBar.add(saveCloudButton);
        toolBar.add(openCloudButton);

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

            if (chooser.showOpenDialog(parentFrame)
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
            String current = controller.getDocumentName();
            if (current == null || current.isBlank() || "Untitled".equals(current)) {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showSaveDialog(parentFrame) == JFileChooser.APPROVE_OPTION) {
                    controller.saveDocument(chooser.getSelectedFile().getAbsolutePath(), editorPanel.getText());
                }
            } else {
                controller.saveDocument(current, editorPanel.getText());
            }
        });

        saveAsButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(parentFrame) == JFileChooser.APPROVE_OPTION) {
                controller.saveDocument(chooser.getSelectedFile().getAbsolutePath(), editorPanel.getText());
            }
        });

        cloudLoginButton.addActionListener(e -> {
            CloudLoginDialog loginDialog = new CloudLoginDialog(parentFrame, documentApiClient);
            loginDialog.setVisible(true);
        });

        saveCloudButton.addActionListener(e -> {
            if (!documentApiClient.isAuthenticated()) {
                JOptionPane.showMessageDialog(parentFrame, "Please log in to the cloud first.", "Authentication Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String name = JOptionPane.showInputDialog(parentFrame, "Enter cloud document name:", controller.getDocumentName());
            if (name != null && !name.isBlank()) {
                try {
                    documentApiClient.saveDocument(name, editorPanel.getText());
                    JOptionPane.showMessageDialog(parentFrame, "Document saved to cloud.", "Cloud Save", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parentFrame, "Cloud save failed: " + ex.getMessage(), "Cloud Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        openCloudButton.addActionListener(e -> {
            if (!documentApiClient.isAuthenticated()) {
                JOptionPane.showMessageDialog(parentFrame, "Please log in to the cloud first.", "Authentication Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            CloudDocumentsDialog dialog = new CloudDocumentsDialog(parentFrame, documentApiClient, controller, editorPanel, statusBar);
            dialog.setVisible(true);
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
