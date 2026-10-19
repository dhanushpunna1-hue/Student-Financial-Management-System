package com.sfms;

import com.sfms.ui.StudentLoginFrame;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> new StudentLoginFrame().setVisible(true));
    }
}
