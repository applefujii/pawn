package com.apple.pawn.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.DefaultLwjgl3Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Robot;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PawnLwjgl3Input extends DefaultLwjgl3Input {
    public PawnLwjgl3Input(Lwjgl3Window window) {
        super(window);
    }

    @Override
    public void getTextInput (TextInputListener listener, String title, String text, String hint, OnscreenKeyboardType type) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                JPanel panel = new JPanel(new FlowLayout());

                JPanel textPanel = new JPanel() {
                    public boolean isOptimizedDrawingEnabled () {
                        return false;
                    };
                };

                textPanel.setLayout(new OverlayLayout(textPanel));
                panel.add(textPanel);

                final JTextField textField = new JTextField(20);
                textField.setText(text);
                textField.setAlignmentX(0.0f);
                textField.addFocusListener(new FocusListener(){
                    @Override
                    public void focusGained(FocusEvent fe){
                        try {
                            textField.getInputContext().setCompositionEnabled(true);
                        } catch(UnsupportedOperationException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void focusLost(FocusEvent fe){
//                        kanjiEnd(textField.getText());
                    }
                });
                textPanel.add(textField);

                final JLabel placeholderLabel = new JLabel(hint);
                placeholderLabel.setForeground(Color.GRAY);
                placeholderLabel.setAlignmentX(0.0f);
                textPanel.add(placeholderLabel, 0);

                textField.getDocument().addDocumentListener(new DocumentListener() {

                    @Override
                    public void removeUpdate (DocumentEvent arg0) {
                        this.updated();
                    }

                    @Override
                    public void insertUpdate (DocumentEvent arg0) {
                        this.updated();
                    }

                    @Override
                    public void changedUpdate (DocumentEvent arg0) {
                        this.updated();
                    }

                    private void updated () {
                        placeholderLabel.setVisible(textField.getText().length() == 0);
                    }
                });

                JOptionPane pane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, null,
                        null);

                pane.setInitialValue(null);
                pane.setComponentOrientation(JOptionPane.getRootFrame().getComponentOrientation());

                Border border = textField.getBorder();
                placeholderLabel.setBorder(new EmptyBorder(border.getBorderInsets(textField)));

                JDialog dialog = pane.createDialog(null, title);
                pane.selectInitialValue();

                dialog.addWindowFocusListener(new WindowFocusListener() {

                    @Override
                    public void windowLostFocus (WindowEvent arg0) {
                    }

                    @Override
                    public void windowGainedFocus (WindowEvent arg0) {
                        textField.requestFocusInWindow();
                    }
                });

                dialog.setModal(true);
                dialog.setAlwaysOnTop(true);
                dialog.setVisible(true);
                dialog.dispose();

                Object selectedValue = pane.getValue();

                if ((selectedValue instanceof Integer)
                        && (Integer) selectedValue == JOptionPane.OK_OPTION) {
                    kanjiEnd(textField.getText());
                    listener.input(textField.getText());
                } else {
                    kanjiEnd(textField.getText());
                    listener.canceled();
                }

            }
        });
    }

    private static void kanjiEnd(String s) {
        if(s == null) return;
        if (s.length()>0) {
            if (s.charAt(s.length()-1) <= 0x127) {
                return;
            }
        }

        String osName = System.getProperty("os.name").toLowerCase();
//        Gdx.app.debug("info", osName);

        try {
            Robot rb = new Robot();
            if(osName.startsWith("windows")) {
                rb.keyPress(KeyEvent.VK_ALT);
                rb.keyPress(244);
                rb.keyRelease(244);
                rb.keyRelease(KeyEvent.VK_ALT);
            }
            if(osName.startsWith("linux")) {
                rb.keyPress(KeyEvent.VK_CONTROL);
                rb.keyPress(KeyEvent.VK_SPACE);
                rb.keyRelease(KeyEvent.VK_SPACE);
                rb.keyRelease(KeyEvent.VK_CONTROL);
            }
            if(osName.startsWith("mac")) {
                rb.keyPress(102);
                rb.keyRelease(102);
            }
        }catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
