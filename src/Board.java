
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author victor
 */
public class Board extends javax.swing.JPanel {

    public static final int NUM_ROWS = 20;
    public static final int NUM_COLS = 40;
    private Snake snake;
    private Snake snake2;
    private Food food;
    private Timer timer;
    private MyKeyAdapter keyAdapter;
    private boolean gameOver;
    private BufferedImage background;
    private ScoreBoard scoreBoard;

    public void setScoreBoard(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (snake.getDirection() != Direction.RIGHT) {
                        snake.setDirection(Direction.LEFT);
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (snake.getDirection() != Direction.LEFT) {
                        snake.setDirection(Direction.RIGHT);
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (snake.getDirection() != Direction.DOWN) {
                        snake.setDirection(Direction.UP);
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (snake.getDirection() != Direction.UP) {
                        snake.setDirection(Direction.DOWN);
                    }
                    break;
                case KeyEvent.VK_A:
                    if (snake2.getDirection() != Direction.RIGHT) {
                        snake2.setDirection(Direction.LEFT);
                    }
                    break;
                case KeyEvent.VK_D:
                    if (snake2.getDirection() != Direction.LEFT) {
                        snake2.setDirection(Direction.RIGHT);
                    }
                    break;
                case KeyEvent.VK_W:
                    if (snake.getDirection() != Direction.DOWN) {
                        snake2.setDirection(Direction.UP);
                    }
                    break;
                case KeyEvent.VK_S:
                    if (snake.getDirection() != Direction.UP) {
                        snake2.setDirection(Direction.DOWN);
                    }
                    break;

                case KeyEvent.VK_P:
                    if (timer.isRunning()) {
                        timer.stop();
                        scoreBoard.pause();
                               
                    } else {
                        timer.start();
                        scoreBoard.printScore();
                    }
                    break;
                default:
                    break;
            }
            repaint();
        }
    }

    /**
     * Creates new form Board
     */
    public Board() {
        initComponents();
        setFocusable(true);
        requestFocusInWindow();
        
    }

    public void initGame() {
        keyAdapter = new MyKeyAdapter();
        snake = new Snake(10);
        snake2 = new Snake(18);
        food = new Food(snake);
       
        setFocusable(true);
        gameOver = false;
        scoreBoard.setScoreP1(0);
        scoreBoard.setScoreP2(0);
        
      
        
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        run();
        removeKeyListener(keyAdapter);
        addKeyListener(keyAdapter);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        backGround(g);

        g.drawImage(background, 0, 0, this);

        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth() / NUM_COLS * NUM_COLS,
                getHeight() / NUM_ROWS * NUM_ROWS);
        if (snake != null) {
            food.paint(g, getSize().width / Board.NUM_COLS,
                    getSize().height / Board.NUM_ROWS);
            snake.paint(g, getSize().width / Board.NUM_COLS,
                    getSize().height / Board.NUM_ROWS);
            snake2.paintSnake2(g, getSize().width / Board.NUM_COLS,
                    getSize().height / Board.NUM_ROWS);

            if (gameOver) {
                printGameOver(g);
            }
        }
        Toolkit.getDefaultToolkit().sync(); // To avoid jumps when no pressed key
    }

    public void run() {

        timer = new Timer(Config.getInstance().getDeltaTime(),
                new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!snake.colide() && !snake2.colide()) {
                    if (snake.eats(food)) {
                        if (food instanceof SpecialFood) {
                            scoreBoard.incrementScoreP1(20);
                            
                        } else {
                            scoreBoard.incrementScoreP1(5);
                        }
                        snake.grow();

                        createFood();
                    } else {
                        snake.move();
                    }
                    if (snake2.eats(food)) {
                        if (food instanceof SpecialFood) {
                            scoreBoard.incrementScoreP2(20);
                            
                        } else {
                            scoreBoard.incrementScoreP2(5);
                        }

                        snake2.grow();
                        createFood();
                    } else {

                        snake2.move();
                    }

                } else {
                    gameOver();
                }

                repaint();

            }
        });
        timer.start();

    }

    public void createFood() {
        Random r = new Random();
        int num = r.nextInt(3);
        if (num == 0) {
            food = new SpecialFood(snake,this);
        } else {
            food = new Food(snake);
        }
    }

    private void gameOver() {

        Container c = getParent();
        if (timer != null) {
            timer.stop();
        }
        gameOver = true;
        repaint();
        System.out.println("Game Over");
    }

    private void printGameOver(Graphics g) {
        //G
        Util.drawSquare(g, 4, 3, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 4, 4, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 4, 5, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 4, 6, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 5, 3, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 6, 3, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 7, 3, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 8, 3, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 9, 3, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 9, 4, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 9, 5, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 9, 6, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 8, 6, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 7, 6, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 7, 5, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        // A
        Util.drawSquare(g, 4, 8, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 4, 9, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 4, 10, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 4, 11, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 5, 8, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 6, 8, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 7, 8, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 8, 8, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 9, 8, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 5, 11, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 6, 11, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 7, 11, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 8, 11, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 9, 11, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 7, 9, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 7, 10, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        //M
        Util.drawSquare(g, 4, 13, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 5, 13, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 6, 13, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 7, 13, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 8, 13, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 9, 13, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 5, 14, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 6, 15, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 5, 16, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 4, 17, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 5, 17, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 6, 17, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 7, 17, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 8, 17, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 9, 17, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        //E
        Util.drawSquare(g, 4, 19, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 5, 19, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 6, 19, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 7, 19, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 8, 19, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 9, 19, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 4, 20, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 4, 21, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 4, 22, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 7, 20, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 7, 21, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 7, 22, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 9, 20, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 9, 21, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 9, 22, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        //O
        Util.drawSquare(g, 11, 13, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 12, 13, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 13, 13, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 14, 13, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 15, 13, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 16, 13, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 11, 14, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 11, 15, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 11, 16, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 11, 17, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 12, 17, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 13, 17, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 14, 17, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 15, 17, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 16, 17, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 16, 14, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 16, 15, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 16, 16, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        //V
        Util.drawSquare(g, 11, 19, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 12, 19, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 13, 20, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 14, 20, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 15, 21, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 16, 21, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        //Util.drawSquare(g, 16, 22, Color.PINK, getWidth()/ NUM_COLS, getHeight() / NUM_ROWS);
        //Util.drawSquare(g, 15, 22, Color.PINK, getWidth()/ NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 14, 22, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 13, 22, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 12, 23, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 11, 23, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        //E
        Util.drawSquare(g, 11, 25, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 12, 25, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 13, 25, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 14, 25, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 15, 25, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 16, 25, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 11, 26, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 11, 27, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 11, 28, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 14, 26, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 14, 27, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 14, 28, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 16, 26, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 16, 27, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 16, 28, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        //R
        Util.drawSquare(g, 11, 30, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 12, 30, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 13, 30, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 14, 30, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 15, 30, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 16, 30, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 11, 31, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 11, 32, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 12, 33, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 13, 33, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);

        Util.drawSquare(g, 14, 31, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 14, 32, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 15, 32, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
        Util.drawSquare(g, 16, 33, Color.PINK, getWidth() / NUM_COLS, getHeight() / NUM_ROWS);
    }

    //Yo
    private void backGround(Graphics g) {
        Dimension tamanio = getSize();
        ImageIcon imagenFondo = new ImageIcon(getClass().
                getResource("/images/grass.jpg"));
        g.drawImage(imagenFondo.getImage(), 0, 0,
                tamanio.width, tamanio.height, null);
        setOpaque(false);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
