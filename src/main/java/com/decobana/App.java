package com.decobana;

import com.decobana.db.DatabaseInitializer;
import com.decobana.ui.MainFrame;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            DatabaseInitializer.initialize();
            new MainFrame().setVisible(true);
        });
    }
}