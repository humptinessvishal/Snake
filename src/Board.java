import javax.swing.*;
import javax.swing.plaf.TreeUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Time;

public class Board extends JPanel implements ActionListener {
    int B_Height = 400;
    int B_Width = 400;
    int MAX_DOTS = 1600;
    int DOT_SIZE = 10;
    int DOTS;
    int x[] = new int[MAX_DOTS];
    int y[] = new int[MAX_DOTS];
    int apple_x;
    int apple_y;
    Image body, head, apple;

    Timer timer;
    int DELAY = 150;
    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = false;
    boolean inGame = true;

    Board(){
        setPreferredSize(new Dimension(B_Width,B_Height));
        setBackground(Color.darkGray);
        Game();
        loadImages();
        TAdapter tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);
    }
    //Initialize Game
    public void Game(){
        DOTS = 3;
        //Initialize Snake's Position
        x[0] = 300;
        y[0] = 100;
        for (int i =1;i<DOTS;i++){
            x[i] = x[0] + DOT_SIZE * i;
            y[i] = y[0];
        }
        //Initialize Apple's Position
        locateapple();
        timer = new Timer(DELAY, this);
        timer.start();
    }
    //Load  images from Resources Folder to Image Objects
    public void loadImages(){
        ImageIcon bodyIcon = new ImageIcon("src/resources/dot.png");
        body = bodyIcon.getImage();
        ImageIcon headIcon = new ImageIcon("src/resources/head.png");
        head = headIcon.getImage();
        ImageIcon appleIcon = new ImageIcon("src/resources/apple.png");
        apple = appleIcon.getImage();
    }
    //draw images at snakes and apple's position
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        doDrawing(g);
    }
    //draw image
    public void doDrawing(Graphics g){
        if(inGame){
            g.drawImage(apple, apple_x, apple_y,this);
            for(int i =0;i<DOTS;i++){
                if(i==0){
                    g.drawImage(head, x[0], y[0],this);
                }
                else{
                    g.drawImage(body, x[i],  y[i],this);
                }
            }
        }
        else{
            timer.stop();
            gameOver(g);
        }
    }
    //Randomize apple's Position
    public void locateapple(){
        apple_x = ((int)(Math.random()*39)) * DOT_SIZE;  // Math.random gives no. from 0 to 1
        apple_y = ((int)(Math.random()*39)) * DOT_SIZE;
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent){
        if(inGame){
            move();
            checkCollioson();
            checkApple();
        }
        repaint();
    }
    //Make Snake Move
    public void move(){
        for (int i = DOTS-1;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(leftDirection){
            x[0] = x[0] - DOT_SIZE;
        }
        if(rightDirection){
            x[0] = x[0] + DOT_SIZE;
        }
        if(upDirection){
            y[0] = y[0] - DOT_SIZE;
        }
        if(downDirection){
            y[0] = y[0] + DOT_SIZE;
        }
    }
    //Implement Controls For Snake
    private class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key = keyEvent.getKeyCode();
            if(key == KeyEvent.VK_LEFT && !rightDirection){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == KeyEvent.VK_RIGHT && !leftDirection){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == KeyEvent.VK_UP && !downDirection){
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            if(key == KeyEvent.VK_DOWN && !upDirection){
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
    //Make Snake Eat apple
    public void checkApple(){
        if(apple_x == x[0] && apple_y == y[0]){
            DOTS++;
            locateapple();
        }
    }
    //Collioson Of Snake
    public void checkCollioson(){
        //Collioson With Body
        for (int i=1;i<DOTS;i++){
            if(i>4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
        }
        //Collioson With Border
        if (x[0] < 0){ //left border
            inGame = false;
        }
        if(y[0] < 0){ //up border
            inGame = false;
        }
        if(x[0] > B_Width){ //right border
            inGame = false;
        }
        if(y[0] > B_Height){ //down border
            inGame = false;
        }
    }
    //Display Game Over Message
    public void gameOver(Graphics g){
        String msg = "Game Over";
        int Score = (DOTS - 3) * 10;
        String Score_msg = "Score : " + Integer.toString(Score);
        Font font = new Font("Helvetica", Font.BOLD, 50);
        FontMetrics fontMetrics = getFontMetrics(font);

        g.setColor(Color.red);
        g.setFont(font);
        g.drawString(msg, (B_Width - fontMetrics.stringWidth(msg)) / 2, B_Height / 4);
        g.drawString(Score_msg, (B_Width - fontMetrics.stringWidth(Score_msg)) / 2, 3 * (B_Height / 4));



    }
}
