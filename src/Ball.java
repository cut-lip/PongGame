import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Ball extends Rectangle
{
    Random random;
    int xVelocity;
    int yVelocity;

    // Speed it up mudz
    int initialSpeed = 2;

    Ball(int x, int y, int width, int height)
    {
        super(x, y, width, height);
        random = new Random();

        int randomXDir = random.nextInt(2);
        if (randomXDir == 0)
        {
            randomXDir--;
        }
        setXDirection(randomXDir * initialSpeed);

        int randomYDir = random.nextInt(2);
        if (randomYDir == 0)
        {
            randomYDir--;
        }
        setYDirection(randomYDir * initialSpeed);
    }

    public void setXDirection(int randomXDir)
    {
        xVelocity = randomXDir;
    }

    public void setYDirection(int randomYDir)
    {
        yVelocity = randomYDir;
    }

    public void move()
    {
        x += xVelocity;
        y += yVelocity;
    }

    public void draw(Graphics g)
    {
        g.setColor(Color.pink);
        g.fillOval(x, y, height, width);
    }
}
