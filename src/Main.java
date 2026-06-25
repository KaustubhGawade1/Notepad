import context.ApplicationContext;
import event.*;
import event.listeners.*;
import model.Document;
import service.FileManager;
import undo.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

       // Document document = new Document();
        ApplicationContext context =
                new ApplicationContext();

        Document document =
                context.getBean(Document.class);

        UndoManager undoManager =
                context.getBean(UndoManager.class);

        FileManager fileManager =
                context.getBean(FileManager.class);

        EventManager eventManager =
                context.getBean(EventManager.class);
        eventManager.subscribe(
                TextChangedEvent.class,
                new LoggingListener()
        );

        eventManager.subscribe(
                TextChangedEvent.class,
                new WordCountListener()
        );

        eventManager.subscribe(
                TextChangedEvent.class,
                new StatusListener()
        );

        eventManager.subscribe(
                SaveEvent.class,
                new StatusListener()
        );
        while (true) {

            String marker =
                    document.isModified()
                            ? "*"
                            : "";

            System.out.println(
                    "\n===== NOTEPAD ["
                            + document.getName()
                            + marker
                            + "] ====="
            );

            System.out.println(
                    "Undo History : "
                            + undoManager.getUndoSize()
            );

            System.out.println(
                    "Redo History : "
                            + undoManager.getRedoSize()
            );

            System.out.println("1. New Document");
            System.out.println("2. Append Content");
            System.out.println("3. View Content");
            System.out.println("4. Save File");
            System.out.println("5. Open File");
            System.out.println("6. Character Count");
            System.out.println("7. Word Count");
            System.out.println("8. Insert At Index");
            System.out.println("9. Delete Range");
            System.out.println("10. Replace Range");
            System.out.println("11. Find Word");
            System.out.println("12. Find All Occurrences");
            System.out.println("13. Undo");
            System.out.println("14. Redo");
            System.out.println("15. Exit");

            System.out.print("\nEnter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1: {

                    if (canProceed(sc, document)) {

                        document = new Document();

                        System.out.println(
                                "New document created."
                        );
                    }

                    break;
                }
                case 2: {

                    System.out.println(
                            "Enter content (END to stop)"
                    );

                    StringBuilder content =
                            new StringBuilder();

                    while (true) {

                        String line =
                                sc.nextLine();

                        if (line.equalsIgnoreCase(
                                "END"
                        )) {
                            break;
                        }

                        content.append(line)
                                .append("\n");
                    }



                    Command appendCommand =
                            new InsertCommand(
                                    document.getBuffer(),
                                    document.getBuffer().length(),
                                    content.toString()
                            );

                    undoManager.executeCommand(
                            appendCommand
                    );



                    document.setModified(true);

                    System.out.println(
                            "Content appended."
                    );

                    break;
                }
                case 3: {

                    System.out.println(
                            "\n----- CONTENT -----"
                    );

                    System.out.print(
                            document
                                    .getBuffer()
                                    .getText()
                    );

                    System.out.println(
                            "-------------------"
                    );

                    break;
                }
                case 4: {

                    System.out.print(
                            "Enter file name: "
                    );

                    String saveFile =
                            sc.nextLine();

                    fileManager.save(
                            document,
                            saveFile
                    );
                    eventManager.notify(
                            new SaveEvent(
                                    saveFile
                            )
                    );

                    break;
                }
                case 5: {
                    if (canProceed(sc, document)) {

                        System.out.print(
                                "Enter file name: "
                        );

                        String openFile =
                                sc.nextLine();

                        fileManager.open(
                                document,
                                openFile
                        );
                    }

                    break;
                }
                case 6: {
                    System.out.println(
                            "Characters : "
                                    + document
                                    .getBuffer()
                                    .charCount()
                    );

                    break;
                }
                case 7: {

                    System.out.println(
                            "Words : "
                                    + document
                                    .getBuffer()
                                    .wordCount()
                    );

                    break;
                }
                case 8: {

                    System.out.print(
                            "Enter Index: "
                    );

                    int insertIndex =
                            sc.nextInt();

                    sc.nextLine();

                    System.out.print(
                            "Enter Text: "
                    );

                    String insertText =
                            sc.nextLine();



                    Command insertCommand =
                            new InsertCommand(
                                    document.getBuffer(),
                                    insertIndex,
                                    insertText
                            );

                    undoManager.executeCommand(
                            insertCommand
                    );

                    document.setModified(true);

                    System.out.println(
                            "Text inserted."
                    );

                    break;
                }

                case 9: {

                    System.out.print("Start Index: ");
                    int start = sc.nextInt();

                    System.out.print("End Index: ");
                    int end = sc.nextInt();

                    sc.nextLine();


                    Command deleteCommand =
                            new DeleteCommand(
                                    document.getBuffer(),
                                    start,
                                    end
                            );

                    undoManager.executeCommand(
                            deleteCommand
                    );


                    document.setModified(true);

                    System.out.println(
                            "Text deleted."
                    );

                    break;
                }
                case 10: {

                    System.out.print("Start Index: ");
                    int replaceStart = sc.nextInt();

                    System.out.print("End Index: ");
                    int replaceEnd = sc.nextInt();

                    sc.nextLine();

                    System.out.print(
                            "Replacement Text: "
                    );

                    String replacement =
                            sc.nextLine();


                    Command replaceCommand =
                            new ReplaceCommand(
                                    document.getBuffer(),
                                    replaceStart,
                                    replaceEnd,
                                    replacement
                            );

                    undoManager.executeCommand(
                            replaceCommand
                    );


                    document.setModified(true);

                    System.out.println(
                            "Text replaced."
                    );

                    break;
                }
                case 11: {

                    System.out.print(
                            "Enter Word: "
                    );

                    String word =
                            sc.nextLine();

                    int index =
                            document.getBuffer()
                                    .find(word);

                    if (index == -1) {

                        System.out.println(
                                "Word not found."
                        );

                    } else {

                        System.out.println(
                                "Found at index: "
                                        + index
                        );
                    }

                    break;
                }
                case 12: {

                    System.out.print(
                            "Enter Word: "
                    );

                    String searchWord =
                            sc.nextLine();

                    System.out.println(
                            "Found at indexes: "
                                    + document
                                    .getBuffer()
                                    .findAll(searchWord)
                    );

                    break;
                }
                case 13: {

                    undoManager.undo();

                    System.out.println(
                            "Undo successful."
                    );

                    break;
                }
                case 14: {

                    undoManager.redo();

                    System.out.println(
                            "Redo successful."
                    );

                    break;
                }
                case 15: {
                    if (canProceed(
                            sc,
                            document
                    )) {

                        System.out.println(
                                "Exiting..."
                        );

                        return;
                    }

                    break;
                }
                default: {
                    System.out.println(
                            "Invalid choice."
                    );
                }
            }
        }
    }

    private static boolean canProceed(
            Scanner sc,
            Document document
    ) {

        if (document.isModified()) {

            System.out.print(
                    "Unsaved changes exist. Discard? (Y/N): "
            );

            String response =
                    sc.nextLine();

            return response.equalsIgnoreCase(
                    "Y"
            );
        }

        return true;
    }
}