package AI;

import checker.Checker;
import java.awt.List;
import java.awt.Point;
import java.util.Arrays;
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
    
    public static Iterable<Point> getMovements(Point position){
        Point[] movements = new Point[4];
        movements[0] = new Point(position.x+1,position.y+1);
        movements[1] = new Point(position.x-1,position.y+1);
        movements[2] = new Point(position.x+1,position.y-1);
        movements[3] = new Point(position.x-1,position.y-1);
        
        Iterable<Point> iterable = () -> prepareIterator(movements);
        return iterable;
    }
    
    private static Iterator<Point> prepareIterator(Point[] movements){
        Iterator<Point> it = Arrays.stream(movements)
            .filter(ax -> ax != null)
            .iterator();
        
        return it;
    }

    public static Iterable<Point> getAttackMovements(Point position) {
        Point[] movements = new Point[4];
        movements[0] = new Point(position.x+2,position.y+2);
        movements[1] = new Point(position.x-2,position.y+2);
        movements[2] = new Point(position.x+2,position.y-2);
        movements[3] = new Point(position.x-2,position.y-2);
        
        Iterable<Point> iterable = () -> prepareIterator(movements);
        return iterable;
    }
    
}
