package gui;

import controller.EditorController;
import java.util.*;
import javax.swing.*;
import java.awt.*;

public class NotepadFrame extends JFrame {
    private EditorPanel editorPanel;

    private final EditorController controller;
    private MenuBarBuilder menuBarBuilder;
    public NotepadFrame(EditorController controller) {

        this.controller = controller;

        initializeFrame();
    }
    public EditorPanel getEditorPanel(){

        return editorPanel;

    }
    public EditorController getController() {

        return controller;

    }
    private void initializeFrame() {

        setTitle("Notepad From Scratch");

        setSize(900,600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // 1. Create EditorPanel FIRST
        editorPanel =
                new EditorPanel(controller);

        System.out.println(
                "EditorPanel = " + editorPanel
        );

        // 2. Add it
        add(
                editorPanel,
                BorderLayout.CENTER
        );

        // 3. Now create MenuBarBuilder
        menuBarBuilder =
                new MenuBarBuilder(
                        controller,
                        editorPanel
                );

        // 4. Set menu
        setJMenuBar(
                menuBarBuilder.build()
        );

        setVisible(true);
    }
}
