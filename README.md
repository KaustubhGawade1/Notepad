# NotepadVibe

NotepadVibe is a custom-built Java Swing desktop notepad application showcasing software engineering design patterns, clean architecture, and decoupled components.

## Features

- **Text Editor**: A standard text editor interface with line, column, character, and word counting.
- **Custom Undo/Redo Engine**: Uses the Command Pattern (`InsertCommand`, `DeleteCommand`, `ReplaceCommand`) instead of Swing's built-in `UndoManager` to provide an extensible editing history.
- **IoC Container**: Includes a custom `ApplicationContext`, `BeanFactory`, and `BeanRegistry` for dependency injection and object lifecycle management.
- **Event-Driven Architecture**: An Observer Pattern implementation (`EventManager`, `EventListener`) broadcasts changes to the status bar, loggers, and word counters decoupled from the main UI.

## Architecture

1. **Controller Layer (`EditorController`)**: Mediates communication between the GUI (`EditorPanel`), the model (`Document`), and the services (`FileManager`, `UndoManager`).
2. **Context Layer (`ApplicationContext`)**: A lightweight Dependency Injection framework that wires components together at startup to ensure loose coupling.
3. **Undo System (`undo.UndoManager`)**: An application-level undo stack maintaining instances of `Command`. The `EditorPanel` records diffs to this system as the user types.

## How to Run

Compile and run `Main.java` located in `src/Main.java`. Requirements: Java 8 or above.

```bash
cd src
javac $(find . -name "*.java")
java Main
```
# Notepadvibe
