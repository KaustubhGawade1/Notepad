package gui;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {

    private final JLabel wordsLabel;
    private final JLabel charactersLabel;
    private final JLabel modifiedLabel;

    public StatusBar() {

        setLayout(new FlowLayout(FlowLayout.LEFT));

        wordsLabel =
                new JLabel("Words : 0");

        charactersLabel =
                new JLabel("Characters : 0");

        modifiedLabel =
                new JLabel("Modified : No");

        add(wordsLabel);
        add(new JLabel("|"));

        add(charactersLabel);
        add(new JLabel("|"));

        add(modifiedLabel);
    }

    public void updateStatus(
            String text,
            boolean modified
    ) {

        int characters =
                text.length();

        int words =
                text.trim().isEmpty()
                        ? 0
                        : text.trim().split("\\s+").length;

        wordsLabel.setText(
                "Words : " + words
        );

        charactersLabel.setText(
                "Characters : " + characters
        );

        modifiedLabel.setText(
                "Modified : " +
                        (modified ? "Yes" : "No")
        );
    }

}
