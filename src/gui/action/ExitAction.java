package gui.action;

import javax.swing.*;
import java.awt.*;

public class ExitAction {

    public void execute(
            Component parent
    ) {

        int option =
                JOptionPane.showConfirmDialog(

                        parent,

                        "Exit Application?",

                        "Exit",

                        JOptionPane.YES_NO_OPTION

                );

        if(option ==
                JOptionPane.YES_OPTION){

            System.exit(0);

        }

    }

}
