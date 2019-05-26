package map;

import AI.Computer;
import checker.Checker;
import damas.GameController;
import damas.GameGUI;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author hasalp
 */
public class Board extends JPanel {

    private int _tile_size = 5;
    public static final int[] BOARD_DIMENSIONS = {8, 8};
    private boolean inDrag = false;
    private Checker lastChecker;
    private int teamTurn = 1;
    private boolean isMandatory = false;
    public boolean isAgainstComputer = false;

    public Board() {
        GameController.initGame();
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e); //To change body of generated methods, choose Tools | Templates.
                inDrag = false;
                //if (lastChecker != null && teamTurn == 1){
                if (lastChecker != null && lastChecker.getTeam() == teamTurn) {
                    int x_coordinate = (int) Math.floor(e.getX() / _tile_size);
                    int y_coordinate = (int) Math.floor(e.getY() / _tile_size);
                    System.out.println("Released: " + x_coordinate + " : " + y_coordinate);
                    //check is out of bound
                    if ((x_coordinate < map.Board.BOARD_DIMENSIONS[0] && x_coordinate >= 0) && (y_coordinate < map.Board.BOARD_DIMENSIONS[1] && y_coordinate >= 0)) {
                        //is cell empty
                        if (GameController.getChecker(x_coordinate, y_coordinate) == null) {
                            if (GameController.move(lastChecker, x_coordinate, y_coordinate)) {
                                //swap turn
                                swapTurn();
                            }
                        }
                        refresh();
                    }
                }
                lastChecker = null;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e); //To change body of generated methods, choose Tools | Templates.
                System.out.println("Pressed");
                inDrag = true;
                int x = e.getX();
                int y = e.getY();
                int x_coordinate = (int) Math.floor(x / _tile_size);
                int y_coordinate = (int) Math.floor(y / _tile_size);
                System.out.println("Pressed: " + x_coordinate + " : " + y_coordinate);
                Checker clickedChecker = GameController.getChecker(x_coordinate, y_coordinate);
                if (clickedChecker != null) {
                    if (!isMandatory) {
                        lastChecker = clickedChecker;
                        System.out.println("Checker found on: " + x_coordinate + " : " + y_coordinate);
                    }
                } else {
                    lastChecker = null;
                }
            }

        });
    }
    
    private void computerRandomPlay(){
        //Mandatory Jump Found
        GameController.shuffleCheckers();
        if(GameController.mandatoryJumpScan(2).size()>0){
            ArrayList<Checker> checkers = GameController.mandatoryJumpScan(2);
            for(Checker checker : checkers){
                for(Point pos : AI.Computer.getAttackMovements(new Point(checker.getX(), checker.getY()))){
                    System.out.println("Computer Played: "+checker.getX()+"x"+checker.getY()+" to "+pos.x+"x"+pos.y);
                    if (GameController.move(checker, pos.x, pos.y)) {
                        //swap turn
                        System.out.println("Checker moved to "+pos.x+"x"+pos.y);
                        swapTurn();
                        return;
                    }
                }
            }
        }
        //mandatory jump not found
        for(Checker checker : damas.GameController.getCheckers()){
            if(checker.getTeam() == 2 && checker.isAlive()){
                for(Point pos : AI.Computer.getMovements(new Point(checker.getX(), checker.getY()))){
                    System.out.println("Checker: " + checker.getX()+"x"+checker.getY()+" want to move to: "+ pos.x+"x"+pos.y);
                    if (GameController.getChecker(pos.x, pos.y) == null) {
                        if (GameController.move(checker, pos.x, pos.y)) {
                            //swap turn
                            swapTurn();
                            return;
                        }
                    }
                }
            }
        }
    }

    private void swapTurn() {
        teamTurn = (teamTurn == 1) ? 2 : 1;
        GameGUI.teamText.setText((teamTurn == 1) ? "Team 1" : "Team 2");
        GameGUI.teamText.setForeground((teamTurn == 1) ? Color.RED : Color.BLUE);
        if(isAgainstComputer && teamTurn == 2){
            computerRandomPlay();
        }
    }

    private void refresh() {
        revalidate();
        repaint();
        System.out.println("Refresh Worked");
    }

    public void setTileSize(int _tile_size) {
        this._tile_size = _tile_size;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        super.revalidate();
        //get size again
        int width = this.getSize().width;
        int height = this.getSize().height;
        int tile_size = (int) (width > height ? Math.floor(height / 8) : Math.floor(width / 8));
        setTileSize(tile_size);

        System.out.println("Repainted");
        //draw squares
        for (int i = 0; i < BOARD_DIMENSIONS[0]; i++) {
            for (int j = 0; j < BOARD_DIMENSIONS[1]; j++) {
                Color color = ((i + j) % 2 == 0) ? Color.YELLOW : Color.ORANGE;
                graphics.setColor(color);
                graphics.fillRect(i * _tile_size, j * _tile_size, _tile_size, _tile_size);
                graphics.setColor(Color.BLACK);
                graphics.drawRect(i * _tile_size, j * _tile_size, _tile_size, _tile_size);
            }
        }
        for (Checker checker : GameController.getCheckers()){
            if(checker.isAlive()){
                Color checkerColor = (checker.getTeam() == 1) ? Color.RED : Color.BLUE;
                graphics.setColor(checkerColor);
                graphics.fillOval(checker.getX() * _tile_size, checker.getY() * _tile_size, _tile_size, _tile_size);
            }
        }
    }

    public void resetTurn() {
        teamTurn = 1;
        GameGUI.teamText.setText((teamTurn == 1) ? "Team 1" : "Team 2");
        GameGUI.teamText.setForeground((teamTurn == 1) ? Color.RED : Color.BLUE);
    }
    
    

}
