import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

// Implements runnable so that it can run on a thread
public class GamePanel extends JPanel implements Runnable
{
    // Modifiers static final because, if we had multiple instances of
    // GamePanel class, they would all share this variable instead of
    // having their own, final if no need to change (what about dif. sized displays?)

    // 5x9 ratio like common ping pong table
    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int)(GAME_WIDTH * (5.0/9.0));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;

    //Declare but don't initialize yet
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;

    GamePanel()
    {
        newPaddles();
        newBall();
        score = new Score(GAME_WIDTH, GAME_HEIGHT);
        // Allows panel to read key strokes
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();

    }

    public void newBall()
    {
        random = new Random();
        ball = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), random.nextInt(GAME_HEIGHT - BALL_DIAMETER) - (BALL_DIAMETER / 2),
                BALL_DIAMETER, BALL_DIAMETER);
    }

    public void newPaddles()
    {
        paddle1 = new Paddle(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        paddle2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);
    }

    public void paint(Graphics g)
    {
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g)
    {
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
    }

    public void move()
    {
        //add move methods here to improve sluggishness etc
        paddle1.move();
        paddle2.move();
        ball.move();
    }

    public void checkCollision()
    {
        // Check paddle collisions
        if (paddle1.y <= 0)
        {
            paddle1.y = 0;
        }
        if (paddle1.y >= GAME_HEIGHT - PADDLE_HEIGHT)
        {
            paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;
        }
        if (paddle2.y <= 0)
        {
            paddle2.y = 0;
        }
        if (paddle2.y >= GAME_HEIGHT - PADDLE_HEIGHT)
        {
            paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;
        }

        // Bounce ball off top and bottom
        if (ball.y <= 0)
            ball.setYDirection(-ball.yVelocity);
        if (ball.y >= GAME_HEIGHT - BALL_DIAMETER)
            ball.setYDirection(-ball.yVelocity);

        // Bounce ball off paddles (can use Rectangles.intersects() )
        if (ball.intersects(paddle1))
        {
            ball.xVelocity = -ball.xVelocity;
            // Optional for more difficulty
            ball.xVelocity++;
            if (ball.yVelocity > 0)
                ball.yVelocity++;
            else
                ball.yVelocity--;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if (ball.intersects(paddle2))
        {
            ball.xVelocity = (-ball.xVelocity);
            ball.xVelocity--;
            if (ball.yVelocity > 0)
                ball.yVelocity++;
            else
                ball.yVelocity--;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        // Give a player a point if they get the ball past their rivals paddle
        if (ball.x <= 0)
        {
            score.player2++;
            newPaddles();
            newBall();
            System.out.println("Player 2: " + score.player2);
        }
        if (ball.x >= GAME_WIDTH - BALL_DIAMETER)
        {
            score.player1++;
            newPaddles();
            newBall();
            System.out.println("Player 1: " + score.player1);
        }
    }

    public void run()
    {
        // Create basic game loop (60 fps or something he said??) copied from minecraft
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        //could also create boolean flag, 'running'
        while (true)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if (delta >= 1)
            {
                // Move all components
                move();
                checkCollision();
                repaint();
                delta--;

            }
        }
    }

    // Create inner action listener class
    public class AL extends KeyAdapter
    {
        public void keyPressed(KeyEvent e)
        {
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }

        public void keyReleased(KeyEvent e)
        {
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }
}
