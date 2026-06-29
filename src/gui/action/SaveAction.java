package gui.action;

import controller.EditorController;
import gui.EditorPanel;

import javax.swing.*;
import java.awt.*;

public class SaveAction {

    private final EditorController controller;
    private final EditorPanel editorPanel;

    public SaveAction(
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
                chooser.showSaveDialog(parent);

        if(result ==
                JFileChooser.APPROVE_OPTION){

            controller.saveDocument(

                    chooser
                            .getSelectedFile()
                            .getAbsolutePath(),

                    editorPanel.getText()

            );

        }

    }

}
