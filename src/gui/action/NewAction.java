package gui.action;

import controller.EditorController;
import gui.EditorPanel;

import javax.swing.*;
import java.awt.*;

public class NewAction {

    private final EditorController controller;
    private final EditorPanel editorPanel;

    public NewAction(
            EditorController controller,
            EditorPanel editorPanel
    ) {

        this.controller = controller;
        this.editorPanel = editorPanel;

    }

    public void execute(
            Component parent
    ) {

        int option =
                JOptionPane.showConfirmDialog(

                        parent,

                        "Discard current document?",

                        "New Document",

                        JOptionPane.YES_NO_OPTION

                );

        if(option ==
                JOptionPane.YES_OPTION){

            controller.clearDocument();

            editorPanel.clear();

        }

    }

}
