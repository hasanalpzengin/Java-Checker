package AI;

import checker.Checker;
import java.awt.List;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hasalp
 */
public class Computer {
    
    public static ArrayList<Point> getMovements(Point position){
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(position.x+1,position.y+1));
        points.add(new Point(position.x-1,position.y+1));
        points.add(new Point(position.x+1,position.y-1));
        points.add(new Point(position.x-1,position.y-1));
        
        Collections.shuffle(points);
        
        return points;
    }
    
    public static ArrayList<Point> getAttackMovements(Point position) {
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(position.x+2,position.y+2));
        points.add(new Point(position.x-2,position.y+2));
        points.add(new Point(position.x+2,position.y-2));
        points.add(new Point(position.x-2,position.y-2));
        
        Collections.shuffle(points);
        
        return points;
    }
    
}
