
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class SpecialFood extends Food implements ActionListener {
    
    private int visibleMilliseconds;
    public static final int MAX_MILLISECONDS = 10000;
    public static final int MIN_MILLISECONDS = 3000;
    public Timer time;
    private Board board;
    
    public SpecialFood(Snake snake, Board board) {
        super(snake);
        visibleMilliseconds = (int) (Math.random()*(MAX_MILLISECONDS - MIN_MILLISECONDS) 
                                                + MIN_MILLISECONDS);
        time = new Timer(visibleMilliseconds, this);
        this.board = board;
       
        time.start();
    }
    
    public void paint(Graphics g, int width, int height) {
        Util.drawSquare(g, row, col, Color.magenta, width, height);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        
        time.stop();
    }
    
}
