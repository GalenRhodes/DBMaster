package com.projectgalen.dbmaster;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

public class Main extends WindowAdapter {

    private static final ResourceBundle msgs = ResourceBundle.getBundle("com.projectgalen.dbmaster.messages");
    private static       Main           application;

    protected JFrame  mainFrame;
    protected boolean running = true;

    private Main(@NotNull String... args) {
        JLabel label = new JLabel("");
        label.setPreferredSize(new Dimension(1024, 1000));
        mainFrame = new JFrame();
        mainFrame.setTitle(msgs.getString("main.frame.title"));
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.setContentPane(label);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        mainFrame.addWindowListener(this);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if(confirmClose() == JOptionPane.YES_OPTION) System.exit(0);
        super.windowClosing(e);
    }

    private int confirmClose() {
        return JOptionPane.showConfirmDialog(mainFrame,
                                             msgs.getString("dlg.close.confirm.msg"),
                                             msgs.getString("dlg.close.confirm.title"),
                                             JOptionPane.YES_NO_OPTION,
                                             JOptionPane.QUESTION_MESSAGE);
    }

    public static Main getApplication() {
        return application;
    }

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeLater(() -> application = new Main(args));
        }
        catch(Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
