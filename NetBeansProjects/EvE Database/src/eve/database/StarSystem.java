/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eve.database;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseMotionListener;
import java.awt.Color;
import static java.awt.Color.RED;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

/**
 *
 * @author spencer
 */
public class StarSystem{
    public Double x=0.0d;
    public int systemID=0;
    public int regionID=0;
    public Double y=0.0d;
    public Double sec_status=0.0d;
    public String name="";
    public Color color= RED;
    public int size=1;
    public int[]connectionx;
    public int[]connectiony;
    public int[]connectsTo= new int[20];
    Rectangle star;
     
    
    
}