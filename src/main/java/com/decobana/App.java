package com.decobana;

import com.decobana.db.DatabaseInitializer;
import com.decobana.ui.MainFrame;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception e) {
                e.printStackTrace();
            }
            DatabaseInitializer.initialize();
            new MainFrame().setVisible(true);
        });
    }
}