package gui;

import controller.EditorController;
import gui.menu.MenuBarBuilder;
import gui.toolbar.ToolBarBuilder;
import gui.dialog.FindDialog;
import service.DocumentApiClient;
import javax.swing.*;
import java.awt.*;

public class NotepadFrame extends JFrame {
    private EditorPanel editorPanel;
    private ToolBarBuilder toolBarBuilder;
    private StatusBar statusBar;
    private final EditorController controller;
    private MenuBarBuilder menuBarBuilder;
    private FindDialog findDialog;
    private DocumentApiClient documentApiClient;
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
    private void initializeShortcuts() {

        JRootPane root = getRootPane();

        InputMap inputMap =
                root.getInputMap(
                        JComponent.WHEN_IN_FOCUSED_WINDOW
                );

        ActionMap actionMap =
                root.getActionMap();

        /*
         * Ctrl + N
         */

        inputMap.put(

                KeyStroke.getKeyStroke(
                        "control N"
                ),

                "new"

        );

        actionMap.put(

                "new",

                new AbstractAction() {

                    @Override
                    public void actionPerformed(
                            java.awt.event.ActionEvent e
                    ) {

                        controller.clearDocument();

                        editorPanel.clear();

                    }

                }

        );

        /*
         * Ctrl + O
         */

        inputMap.put(

                KeyStroke.getKeyStroke(
                        "control O"
                ),

                "open"

        );

        actionMap.put(

                "open",

                new AbstractAction() {

                    @Override
                    public void actionPerformed(
                            java.awt.event.ActionEvent e
                    ) {

                        JFileChooser chooser =
                                new JFileChooser();

                        if (chooser.showOpenDialog(
                                NotepadFrame.this
                        ) == JFileChooser.APPROVE_OPTION) {

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

        );

        /*
         * Ctrl + S
         */

        inputMap.put(

                KeyStroke.getKeyStroke(
                        "control S"
                ),

                "save"

        );

        actionMap.put(

                "save",

                new AbstractAction() {

                    @Override
                    public void actionPerformed(
                            java.awt.event.ActionEvent e
                    ) {

                        JFileChooser chooser =
                                new JFileChooser();

                        if (chooser.showSaveDialog(
                                NotepadFrame.this
                        ) == JFileChooser.APPROVE_OPTION) {

                            controller.saveDocument(

                                    chooser
                                            .getSelectedFile()
                                            .getAbsolutePath(),

                                    editorPanel.getText()

                            );

                        }

                    }

                }

        );

        /*
         * Ctrl + Z
         */

        inputMap.put(

                KeyStroke.getKeyStroke(
                        "control Z"
                ),

                "undo"

        );

        actionMap.put(

                "undo",

                new AbstractAction() {

                    @Override
                    public void actionPerformed(
                            java.awt.event.ActionEvent e
                    ) {

                        editorPanel.undo();

                    }

                }

        );

        /*
         * Ctrl + Y
         */

        inputMap.put(

                KeyStroke.getKeyStroke(
                        "control Y"
                ),

                "redo"

        );

        actionMap.put(

                "redo",

                new AbstractAction() {

                    @Override
                    public void actionPerformed(
                            java.awt.event.ActionEvent e
                    ) {

                        editorPanel.redo();

                    }

                }

        );

        /*
         * Ctrl + F
         */

        inputMap.put(

                KeyStroke.getKeyStroke(
                        "control F"
                ),

                "find"

        );

        actionMap.put(

                "find",

                new AbstractAction() {

                    @Override
                    public void actionPerformed(
                            java.awt.event.ActionEvent e
                    ) {

                        findDialog.setVisible(true);

                    }

                }

        );

    }
    private void initializeFrame() {

        setTitle("Notepad From Scratch");

        setSize(900,600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // 1. Create EditorPanel FIRST
        statusBar =
                new StatusBar();

        add(
                statusBar,
                BorderLayout.SOUTH
        );

        editorPanel =
                new EditorPanel(
                        controller,
                        statusBar
                );

        add(
                editorPanel,
                BorderLayout.CENTER
        );



        findDialog =
                new FindDialog(
                        this,
                        editorPanel
                );

        documentApiClient =
                new DocumentApiClient();

        menuBarBuilder =
                new MenuBarBuilder(
                        controller,
                        editorPanel,
                        statusBar,
                        findDialog,
                        documentApiClient,
                        this
                );

        setJMenuBar(
                menuBarBuilder.build()
        );

        toolBarBuilder =
                new ToolBarBuilder(
                        controller,
                        editorPanel,
                        findDialog,
                        statusBar,
                        documentApiClient,
                        this
                );

        add(
                toolBarBuilder.build(),
                BorderLayout.NORTH
        );

        initializeShortcuts();

        setVisible(true);
    }
}
