import model.Document;
import service.FileManager;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Document document = new Document();

        FileManager fileManager = new FileManager();

        while (true) {

            System.out.println("\n===== NOTEPAD =====");
            System.out.println("1. New Document");
            System.out.println("2. Write Content");
            System.out.println("3. View Content");
            System.out.println("4. Save File");
            System.out.println("5. Open File");
            System.out.println("6. Exit");

            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    document = new Document();

                    System.out.println("New document created.");
                    break;

                case 2:

                    System.out.println("Enter content:");

                    String content = sc.nextLine();

                    document.setContent(content);

                    System.out.println("Content updated.");
                    break;

                case 3:

                    System.out.println("\n----- CONTENT -----");
                    System.out.println(document.getContent());
                    System.out.println("-------------------");

                    break;


                case 4:

                    System.out.print("Enter file name: ");

                    String saveFile = sc.nextLine();

                    fileManager.save(document, saveFile);

                    break;

                case 5:

                    System.out.print("Enter file name: ");

                    String openFile = sc.nextLine();

                    fileManager.open(document, openFile);

                    break;

                case 6:

                    System.out.println("Exiting...");
                    return;

                default:

                    System.out.println("Invalid choice.");
            }
        }
    }
}