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

    public void undo() {

        if (undoStack.isEmpty()) {

            System.out.println(
                    "Nothing to Undo"
            );

            return;
        }

        Command command =
                undoStack.pop();

        command.undo();

        redoStack.push(command);
    }

    public void redo() {

        if (redoStack.isEmpty()) {

            System.out.println(
                    "Nothing to Redo"
            );

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
}
