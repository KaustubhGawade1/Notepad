package undo;

import buffer.TextBuffer;

public class DeleteCommand implements Command {

    private TextBuffer buffer;

    private int start;
    private int end;

    private String deletedText;

    public DeleteCommand(
            TextBuffer buffer,
            int start,
            int end
    ) {
        this.buffer = buffer;
        this.start = start;
        this.end = end;

        this.deletedText =
                buffer.getText()
                        .substring(
                                start,
                                end
                        );
    }

    @Override
    public void execute() {

        buffer.delete(
                start,
                end
        );
    }

    @Override
    public void undo() {

        buffer.insert(
                start,
                deletedText
        );
    }
}
