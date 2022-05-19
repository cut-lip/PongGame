import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

// Window frame class
public class GameFrame extends JFrame
{
    // Canvas on which we paint
    GamePanel panel;
    String s = "Hello";

    GameFrame()
    {
        panel = new GamePanel();
        panel.setBackground(Color.orange);
        this.add(panel);
        this.setTitle("Pong Game");
        this.setResizable(false);
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Fits Frame around game panel
        this.pack();
        this.setVisible(true);
        // Window appears in middle of screen
        this.setLocationRelativeTo(null);
    }
}
