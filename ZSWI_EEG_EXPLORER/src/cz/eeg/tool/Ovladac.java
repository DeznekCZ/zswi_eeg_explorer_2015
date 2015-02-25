package cz.eeg.tool;

import java.awt.KeyEventDispatcher;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFileChooser;

import cz.eeg.Aplikace;

public class Ovladac implements KeyEventDispatcher {
    public boolean dispatchKeyEvent(KeyEvent e) {
        if(Aplikace.OKNO.isActive()
        		&& e.getID() == KeyEvent.KEY_PRESSED 
        		&& e.getKeyCode() == KeyEvent.VK_ENTER) {
            Aplikace.EDITOR.otevrit(true);
        }
        return false;
    }
}
