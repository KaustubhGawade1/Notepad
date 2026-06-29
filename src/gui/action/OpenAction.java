package gui.action;

import controller.EditorController;
import gui.EditorPanel;

import javax.swing.*;
import java.awt.*;

public class OpenAction {

    private final EditorController controller;
    private final EditorPanel editorPanel;

    public OpenAction(
            EditorController controller,
            EditorPanel editorPanel
    ) {

        this.controller = controller;
        this.editorPanel = editorPanel;

    }

    public void execute(
            Component parent
    ) {

        JFileChooser chooser =
                new JFileChooser();

        int result =
                chooser.showOpenDialog(parent);

        if(result ==
                JFileChooser.APPROVE_OPTION){

            controller.openDocument(

                    chooser
                            .getSelectedFile()
                            .getAbsolutePath()

            );

            editorPanel.setText(

                    controller.getText()

            );

        }

    }

}