package gui.dialog;

import gui.EditorPanel;

import javax.swing.*;
import java.awt.*;



public class FindDialog extends JDialog {

    private final JTextField searchField;
    private final JButton findButton;

    private final JButton replaceButton;

    private final JButton replaceAllButton;

    private final JButton closeButton;

    private final JTextField replaceField;
    private final EditorPanel editorPanel;

    private int lastIndex = -1;

    public FindDialog(
            JFrame parent,
            EditorPanel editorPanel
    ) {

        super(parent, "Find / Replace", false);
        this.editorPanel = editorPanel;

        setLayout(
                new GridLayout(
                        4,
                        2,
                        5,
                        5
                )
        );
        searchField =
                new JTextField();

        replaceField =
                new JTextField();

        findButton =
                new JButton("Find Next");

        replaceButton =
                new JButton("Replace");

        replaceAllButton =
                new JButton("Replace All");

        closeButton =
                new JButton("Close");
        add(new JLabel("Find"));

        add(searchField);

        add(new JLabel("Replace With"));

        add(replaceField);

        add(findButton);

        add(replaceButton);

        add(replaceAllButton);

        add(closeButton);

        findButton.addActionListener(e -> findNext());
        replaceButton.addActionListener(
                e -> replace()
        );

        replaceAllButton.addActionListener(
                e -> replaceAll()
        );

        closeButton.addActionListener(
                e -> dispose()
        );
        setSize(
                420,
                200
        );
        setLocationRelativeTo(parent);

    }

    private void findNext() {

        String text =
                editorPanel.getText();

        String search =
                searchField.getText();

        if(search.isEmpty()){

            return;

        }

        int index =
                text.indexOf(
                        search,
                        lastIndex + 1
                );

        if(index == -1){

            JOptionPane.showMessageDialog(

                    this,

                    "Text not found."

            );

            lastIndex = -1;

            return;

        }

        JTextArea area =
                editorPanel.getTextArea();

        area.requestFocus();

        area.requestFocusInWindow();

        area.setCaretPosition(index);

        area.select(
                index,
                index + search.length()
        );

        lastIndex = index;

    }
    private void replace() {

        JTextArea area =
                editorPanel.getTextArea();

        String selectedText =
                area.getSelectedText();

        if (selectedText == null ||
                !selectedText.equals(searchField.getText())) {

            findNext();

            return;

        }

        area.replaceSelection(
                replaceField.getText()
        );

        lastIndex =
                area.getSelectionStart() - 1;

        findNext();

    }
    private void replaceAll() {

        String search =
                searchField.getText();

        if (search.isEmpty()) {

            return;

        }

        String replace =
                replaceField.getText();

        String updatedText =
                editorPanel.getText()
                        .replace(
                                search,
                                replace
                        );

        editorPanel.setText(
                updatedText
        );

        lastIndex = -1;

        JOptionPane.showMessageDialog(

                this,

                "All occurrences replaced."

        );

    }

}
