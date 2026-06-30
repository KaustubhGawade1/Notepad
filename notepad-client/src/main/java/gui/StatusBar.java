package gui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;

public class StatusBar extends JPanel {

    private final JLabel wordsLabel;
    private final JLabel charactersLabel;
    private final JLabel lineLabel;
    private final JLabel columnLabel;
    private final JLabel modifiedLabel;

    public StatusBar() {

        setLayout(new FlowLayout(FlowLayout.LEFT));

        wordsLabel =
                new JLabel("Words : 0");

        charactersLabel =
                new JLabel("Characters : 0");

        lineLabel =
                new JLabel("Line : 1");

        columnLabel =
                new JLabel("Column : 1");

        modifiedLabel =
                new JLabel("Modified : No");

        add(wordsLabel);
        add(new JLabel("|"));

        add(charactersLabel);
        add(new JLabel("|"));

        add(lineLabel);
        add(new JLabel("|"));

        add(columnLabel);
        add(new JLabel("|"));

        add(modifiedLabel);
    }

    public void updateStatus(
            JTextArea textArea,
            boolean modified
    ) {

        String text =
                textArea.getText();

        int characters =
                text.length();

        int words =
                text.trim().isEmpty()
                        ? 0
                        : text.trim()
                        .split("\\s+")
                        .length;

        int line = 1;
        int column = 1;

        try {

            int caret =
                    textArea.getCaretPosition();

            line =
                    textArea.getLineOfOffset(
                            caret
                    ) + 1;

            column =
                    caret
                            -
                            textArea.getLineStartOffset(
                                    line - 1
                            ) + 1;

        }
        catch (BadLocationException ignored) {

        }

        wordsLabel.setText(
                "Words : " + words
        );

        charactersLabel.setText(
                "Characters : " + characters
        );

        lineLabel.setText(
                "Line : " + line
        );

        columnLabel.setText(
                "Column : " + column
        );

        modifiedLabel.setText(
                "Modified : " +
                        (modified ? "Yes" : "No")
        );

    }

}
