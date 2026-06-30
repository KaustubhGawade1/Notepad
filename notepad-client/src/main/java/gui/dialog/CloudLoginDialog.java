package gui.dialog;

import service.DocumentApiClient;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CloudLoginDialog extends JDialog {

    private final DocumentApiClient apiClient;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel statusLabel;

    public CloudLoginDialog(Frame parent, DocumentApiClient apiClient) {
        super(parent, "Cloud Login", true);
        this.apiClient = apiClient;

        setLayout(new BorderLayout());
        setSize(360, 220);
        setLocationRelativeTo(parent);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        statusLabel = new JLabel(" ");
        formPanel.add(statusLabel);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");

        loginButton.addActionListener(e -> authenticate(false));
        registerButton.addActionListener(e -> authenticate(true));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void authenticate(boolean register) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Username and password are required.");
            return;
        }

        try {
            if (register) {
                apiClient.register(username, password);
                statusLabel.setText("Registered and logged in successfully.");
            } else {
                apiClient.login(username, password);
                statusLabel.setText("Logged in successfully.");
            }
            dispose();
        } catch (IOException | InterruptedException ex) {
            statusLabel.setText("Cloud auth failed: " + ex.getMessage());
        }
    }
}
