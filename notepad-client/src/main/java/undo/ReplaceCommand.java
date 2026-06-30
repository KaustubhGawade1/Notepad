package undo;

import buffer.TextBuffer;

public class ReplaceCommand
        implements Command {

    private TextBuffer buffer;

    private int start;
    private int end;

    private String oldText;
    private String newText;

    public ReplaceCommand(
            TextBuffer buffer,
            int start,
            int end,
            String newText
    ) {

        this.buffer = buffer;

        this.start = start;
        this.end = end;

        this.newText = newText;

        this.oldText =
                buffer.getText()
                        .substring(
                                start,
                                end
                        );
    }

    @Override
    public void execute() {

        buffer.replace(
                start,
                end,
                newText
        );
    }

    @Override
    public void undo() {

        buffer.replace(
                start,
                start + newText.length(),
                oldText
        );
    }
}