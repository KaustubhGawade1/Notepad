package gui.menu;

import controller.EditorController;
import gui.EditorPanel;
import gui.StatusBar;
import gui.dialog.CloudDocumentsDialog;
import gui.dialog.CloudLoginDialog;
import gui.dialog.FindDialog;
import service.DocumentApiClient;
import javax.swing.*;
import java.awt.*;

public class MenuBarBuilder {

    private final StatusBar statusBar;
    private final EditorController controller;
    private final EditorPanel editorPanel;
    private final FindDialog findDialog;
    private final DocumentApiClient documentApiClient;
    private final Frame parentFrame;

    public MenuBarBuilder(
            EditorController controller,
            EditorPanel editorPanel,
            StatusBar statusBar,
            FindDialog findDialog,
            DocumentApiClient documentApiClient,
            Frame parentFrame
    ) {

        this.controller = controller;
        this.editorPanel = editorPanel;
        this.statusBar = statusBar;
        this.findDialog = findDialog;
        this.documentApiClient = documentApiClient;
        this.parentFrame = parentFrame;
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

        JMenuItem saveAsItem =
                new JMenuItem("Save As");

        JMenuItem exitItem =
                new JMenuItem("Exit");

        JMenuItem undoItem =
                new JMenuItem("Undo");

        JMenuItem redoItem =
                new JMenuItem("Redo");

        JMenuItem loginCloudItem =
                new JMenuItem("Login to Cloud");

        JMenuItem saveCloudItem =
                new JMenuItem("Save to Cloud");

        JMenuItem openCloudItem =
                new JMenuItem("Open from Cloud");

        saveCloudItem.setEnabled(documentApiClient.isAuthenticated());
        openCloudItem.setEnabled(documentApiClient.isAuthenticated());

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(loginCloudItem);
        fileMenu.add(saveCloudItem);
        fileMenu.add(openCloudItem);
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
                        String current = controller.getDocumentName();
                        if (current == null || current.isBlank() || "Untitled".equals(current)) {
                                JFileChooser chooser = new JFileChooser();
                                int result = chooser.showSaveDialog(parentFrame);
                                if (result == JFileChooser.APPROVE_OPTION) {
                                        controller.saveDocument(chooser.getSelectedFile().getAbsolutePath(), editorPanel.getText());
                                        controller.markSaved();
                                        statusBar.updateStatus(editorPanel.getTextArea(), false);
                                }
                        } else {
                                controller.saveDocument(current, editorPanel.getText());
                                controller.markSaved();
                                statusBar.updateStatus(editorPanel.getTextArea(), false);
                        }
                });

                saveAsItem.addActionListener(e -> {
                        JFileChooser chooser = new JFileChooser();
                        int result = chooser.showSaveDialog(parentFrame);
                        if (result == JFileChooser.APPROVE_OPTION) {
                                controller.saveDocument(chooser.getSelectedFile().getAbsolutePath(), editorPanel.getText());
                                controller.markSaved();
                                statusBar.updateStatus(editorPanel.getTextArea(), false);
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

        loginCloudItem.addActionListener(e -> {
            CloudLoginDialog loginDialog = new CloudLoginDialog(parentFrame, documentApiClient);
            loginDialog.setVisible(true);
            saveCloudItem.setEnabled(documentApiClient.isAuthenticated());
            openCloudItem.setEnabled(documentApiClient.isAuthenticated());
        });

        saveCloudItem.addActionListener(e -> {
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

        openCloudItem.addActionListener(e -> {
            if (!documentApiClient.isAuthenticated()) {
                JOptionPane.showMessageDialog(parentFrame, "Please log in to the cloud first.", "Authentication Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            CloudDocumentsDialog dialog = new CloudDocumentsDialog(parentFrame, documentApiClient, controller, editorPanel, statusBar);
            dialog.setVisible(true);
        });

        exitItem.addActionListener(e -> {

            System.exit(0);
        });

        return menuBar;
    }
}