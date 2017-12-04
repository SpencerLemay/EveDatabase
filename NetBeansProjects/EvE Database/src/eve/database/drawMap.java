/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eve.database;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static java.lang.Math.abs;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 *
 * @author spencer
 */


public class drawMap extends JPanel  {
    StarSystem[] systems;
    Rectangle[] stars = new Rectangle[10000];
    int tempx=0,tempy=0,tempx2=0,tempy2=0;
    Double minx;
    Double miny;
    Double maxx;
    Double maxy;
    float xwidth=1000;
    float ywidth=1000;
    private int highlight = -1;
    
            JLabel sysLabel=null;

    drawMap(StarSystem[] syses){
        systems=syses;
        setBackground(Color.BLACK); 
        setOpaque(true);
        setLayout(null);
        stars = new Rectangle[10000];
        minx=getMinXSys(systems);
        miny=getMinYSys(systems);
        maxx=getMaxXSys(systems);
        maxy=getMaxYSys(systems);
        
        Double dmulty= maxy-miny;
        Double dmultx= maxx-minx;
        Double store=dmultx/dmulty;
        float mapRatio=store.floatValue();
        if(mapRatio<1){
            xwidth=xwidth*mapRatio;
        }
        System.out.println("ratio: "+mapRatio+"\n");
        for (StarSystem system : systems) {
            tempx = normalizeStar(system.x, minx, maxx, xwidth);
            tempy = normalizeStar(system.y, miny, maxy, ywidth);
            system.star = new Rectangle(tempx,tempy,7,7);
            system.connectionx = new int[system.connectsTo.length];
            system.connectiony = new int[system.connectsTo.length];
            for (int j = 0; j < system.connectsTo.length; j++) {
                for (int k = 0; k<systems.length; k++) {
                    if (system.connectsTo[j] == systems[k].systemID) {
                        system.connectiony[j] = normalizeStar(systems[k].y,miny,maxy,ywidth);
                        system.connectionx[j] = normalizeStar(systems[k].x,minx,maxx,xwidth);
                        j++;
                        k=0;
                    }
                }
            }
        }
    
    addMouseMotionListener(new MouseAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
            for(int i=0;i<systems.length;i++){    
                if (systems[i].star.contains(e.getPoint())){
                    highlight=i;
                    repaint();
                }
            }
        }
    });
    }
    private int normalizeStar(Double i,Double min,Double max,float multiplier){
        i=((i-min)/(max-min))*multiplier;
        return i.intValue();
    }
    
    /**
     *
     * @param inputArray
     * @return
     */
    public static double getMaxXSys(StarSystem[] inputArray){ 
        Double maxValue = inputArray[0].x; 
        for (StarSystem inputArray1 : inputArray) {
            if (inputArray1.x > maxValue) {
                maxValue = inputArray1.x;
            }
        } 
        return maxValue; 
    }
    public static Double getMaxYSys(StarSystem[] inputArray){ 
        Double maxValue = inputArray[0].y; 
        for (StarSystem inputArray1 : inputArray) {
            if (inputArray1.y > maxValue) {
                maxValue = inputArray1.y;
            }
        } 
        return maxValue; 
    }
    public static Double getMinXSys(StarSystem[] inputArray){ 
        Double minValue = inputArray[0].x; 
        for (StarSystem inputArray1 : inputArray) {
            if (inputArray1.x < minValue) {
                minValue = inputArray1.x;
            }
        } 
        return minValue; 
    }
    public static Double getMinYSys(StarSystem[] inputArray){ 
    Double minValue = inputArray[0].y; 
        for (StarSystem inputArray1 : inputArray) {
            if (inputArray1.y < minValue) {
                minValue = inputArray1.y;
            }
        } 
        return minValue; 
    } 
    
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for(int i=0;i<systems.length;i++){
            if(systems[i].sec_status>.5d){
                g2.setColor(Color.BLUE);
            }else if(systems[i].sec_status>.1d){
                g2.setColor(Color.ORANGE);
            }else{
                g2.setColor(Color.RED);
            }
            if(i==highlight){
                g2.setColor(Color.WHITE);
                if(sysLabel!=null)
                remove(sysLabel);
                sysLabel= new JLabel(systems[i].name);
                add(sysLabel);
                sysLabel.setLocation(systems[i].star.x, systems[i].star.y);
                sysLabel.setSize(140,30);
                sysLabel.setForeground(Color.WHITE);
            }
            g2.draw(systems[i].star);
            for(int j=0;j<systems[i].connectionx.length;j++){
                        if(systems[i].connectionx[j]!=0){
                        
                        g2.drawLine(systems[i].star.x, systems[i].star.y, systems[i].connectionx[j], systems[i].connectiony[j]);
                        }

            }
        }
      
          
    }
}
