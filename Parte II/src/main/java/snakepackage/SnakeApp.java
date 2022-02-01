package snakepackage;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import enums.GridSize;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author jd-
 *
 */
public class SnakeApp {

    private static SnakeApp app;
    public static final int MAX_THREADS = 8;
    Snake[] snakes = new Snake[MAX_THREADS];
    private static final Cell[] spawn = {
        new Cell(1, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2,
        3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, GridSize.GRID_HEIGHT - 2),
        new Cell(1, 3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2,
        GridSize.GRID_HEIGHT - 2)};
    private JFrame frame;
    private static Board board;
    int nr_selected = 0;

    public Object pivote = new Object();
   private boolean corriendo = false;
   private Snake winner= null;
   private Snake noob = null;


    Thread[] thread = new Thread[MAX_THREADS];

    public SnakeApp() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("The Snake Race");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(618, 640);
        frame.setSize(GridSize.GRID_WIDTH * GridSize.WIDTH_BOX + 17,
                GridSize.GRID_HEIGHT * GridSize.HEIGH_BOX + 40);
        frame.setLocation(dimension.width / 2 - frame.getWidth() / 2,
                dimension.height / 2 - frame.getHeight() / 2);
        board = new Board();


        frame.add(board, BorderLayout.CENTER);

        JPanel actionsBPabel = new JPanel();
        actionsBPabel.setLayout(new FlowLayout());
        actionsBPabel.add(new JButton("Action "));


        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        actionsBPabel.add(startButton);

        JButton stopButton = new JButton("Pause");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pauseGame();
            }
        });

        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resumeGame();
            }
        });

        actionsBPabel.add(stopButton);
        actionsBPabel.add(startButton);
        actionsBPabel.add(resumeButton);
        frame.add(actionsBPabel,BorderLayout.SOUTH);

    }

    public static void main(String[] args) {
        app = new SnakeApp();
        app.init();
    }

    private void init() {
        
        
        
        for (int i = 0; i != MAX_THREADS; i++) {
            
            snakes[i] = new Snake(i + 1, spawn[i], i + 1, pivote);
            snakes[i].addObserver(board);
            thread[i] = new Thread(snakes[i]);
            //thread[i].start();
        }

        frame.setVisible(true);
        boolean hayNoob = false;

        while (true) {
            //Se cambia el int para evitar el x++ que puede generar errores al uso de hilos
            //int x = 0;
            AtomicInteger x = new AtomicInteger(0);
            for (int i = 0; i != MAX_THREADS; i++) {
                if (snakes[i].isSnakeEnd() == true) {
                    //x++;
                    x.getAndIncrement();

                    if(!hayNoob){
                        this.noob = snakes[i];
                        System.out.println("El primer muerto :" +this.noob.getIdt());
                        hayNoob = true;
                    }
                }
            }
            if (x.get() == MAX_THREADS) {
                break;
            }
        }


        System.out.println("Thread (snake) status:");
        for (int i = 0; i != MAX_THREADS; i++) {
            System.out.println("["+i+"] :"+thread[i].getState());
        }
        

    }

    public static SnakeApp getApp() {
        return app;
    }

    private void startGame() {
        if (!corriendo) {
            for (int i = 0; i != MAX_THREADS; i++) {
                thread[i].start();
            }

            this.corriendo = true;
        }
    }

    private void pauseGame() {
        for (int i = 0; i != MAX_THREADS; i++) {
            snakes[i].pausar();
        }
        calcTheWinner();

        System.out.println("El ganador por ahora: "+ this.winner.getBody().size());
    }

    private void resumeGame(){
        for (int i = 0; i != MAX_THREADS; i++) {
            snakes[i].retornar();
        }
        synchronized (pivote) {
            pivote.notifyAll();
        }
    }

    private void calcTheWinner(){
        Snake winnerr = null;
        int a = -1;
        for (int i = 0; i != MAX_THREADS; i++) {
            if (snakes[i] != null && (snakes[i].getBody().size() > a)) {
                a = snakes[i].getBody().size();
                winnerr = snakes[i];
            }
        }
        this.setWinner(winnerr);
    }

    public Snake getWinner(){
        if (this.winner != null){
            return winner;
        }
        else{
            return null;
        }
    }

    public void setWinner(Snake winner) {
        this.winner = winner;
    }
}


