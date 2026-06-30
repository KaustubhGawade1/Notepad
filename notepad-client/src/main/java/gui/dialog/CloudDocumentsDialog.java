package gui.dialog;

import controller.EditorController;
import gui.EditorPanel;
import gui.StatusBar;
import model.CloudDocument;
import service.DocumentApiClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class CloudDocumentsDialog extends JDialog {

    private final DocumentApiClient apiClient;
    private final EditorController controller;
    private final EditorPanel editorPanel;
    private final StatusBar statusBar;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public CloudDocumentsDialog(Frame parent,
                                DocumentApiClient apiClient,
                                EditorController controller,
                                EditorPanel editorPanel,
                                StatusBar statusBar) {
        super(parent, "Cloud Documents", true);
        this.apiClient = apiClient;
        this.controller = controller;
        this.editorPanel = editorPanel;
        this.statusBar = statusBar;

        setSize(760, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Created", "Updated"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh");
        JButton openButton = new JButton("Open");
        JButton closeButton = new JButton("Close");

        refreshButton.addActionListener(e -> loadDocuments());
        openButton.addActionListener(e -> openSelectedDocument());
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(refreshButton);
        buttonPanel.add(openButton);
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);
        loadDocuments();
    }

    private void loadDocuments() {
        tableModel.setRowCount(0);
        try {
            List<CloudDocument> documents = apiClient.listDocuments();
            for (CloudDocument document : documents) {
                tableModel.addRow(new Object[]{document.id(), document.name(), document.createdAt(), document.updatedAt()});
            }
        } catch (IOException | InterruptedException ex) {
            JOptionPane.showMessageDialog(this, "Could not load cloud documents: " + ex.getMessage(), "Cloud Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openSelectedDocument() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            JOptionPane.showMessageDialog(this, "Please select a cloud document to open.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long documentId = (Long) tableModel.getValueAt(selected, 0);
        try {
            CloudDocument document = apiClient.getDocument(documentId);
            controller.setText(document.content());
            editorPanel.setText(document.content());
            controller.markSaved();
            statusBar.updateStatus(editorPanel.getTextArea(), false);
            dispose();
        } catch (IOException | InterruptedException ex) {
            JOptionPane.showMessageDialog(this, "Could not open cloud document: " + ex.getMessage(), "Cloud Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
