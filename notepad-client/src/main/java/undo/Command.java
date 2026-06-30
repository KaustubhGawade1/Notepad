package undo;
public interface Command {

    void execute();

    void undo();
}