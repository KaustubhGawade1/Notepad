package undo;

import java.util.Stack;

public class UndoManager {

    private Stack<Command> undoStack;
    private Stack<Command> redoStack;

    public UndoManager() {

        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void executeCommand(
            Command command
    ) {

        command.execute();

        undoStack.push(command);

        redoStack.clear();
    }

    /**
     * Records a command that was already executed
     * (e.g., by the JTextArea itself).
     * Pushes to undo stack without calling execute().
     */
    public void recordCommand(
            Command command
    ) {

        undoStack.push(command);

        redoStack.clear();
    }

    public void undo() {

        if (undoStack.isEmpty()) {
            return;
        }

        Command command =
                undoStack.pop();

        command.undo();

        redoStack.push(command);
    }

    public void redo() {

        if (redoStack.isEmpty()) {
            return;
        }

        Command command =
                redoStack.pop();

        command.execute();

        undoStack.push(command);
    }

    public int getUndoSize() {
        return undoStack.size();
    }

    public int getRedoSize() {
        return redoStack.size();
    }

    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
}
