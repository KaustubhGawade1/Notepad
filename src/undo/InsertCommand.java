package undo;

import buffer.TextBuffer;

public class InsertCommand implements Command {

    private TextBuffer buffer;
    private int index;
    private String text;

    public InsertCommand(
            TextBuffer buffer,
            int index,
            String text
    ) {
        this.buffer = buffer;
        this.index = index;
        this.text = text;
    }

    @Override
    public void execute() {

        buffer.insert(
                index,
                text
        );
    }

    @Override
    public void undo() {

        buffer.delete(
                index,
                index + text.length()
        );
    }
}
