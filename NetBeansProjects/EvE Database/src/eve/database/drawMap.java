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
    Double dmulty;
    float multy;
    float multx;
    int xwidth=6000;
    int ywidth=1300;
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
        if(maxx>maxy)
                dmulty= (maxy/maxx);
        else
            dmulty=(maxx/maxy);
        multy =dmulty.floatValue();
        multx= 1-multy;
        for(int i=0;i<systems.length;i++){
            
            tempx=normalizeStar(systems[i].x,minx,maxx,multx*xwidth);
            tempy=normalizeStar(systems[i].y,miny,maxy,multy*ywidth);
            systems[i].star= new Rectangle(tempx,tempy,7,7);
            systems[i].connectionx=new int[systems[i].connectsTo.length];
            systems[i].connectiony=new int[systems[i].connectsTo.length];
            for(int j=0;j<systems[i].connectsTo.length;j++){
                for(int k=0;k<systems.length;k++){
                    if(systems[i].connectsTo[j]==systems[k].systemID){
                        systems[i].connectiony[j]=normalizeStar(systems[k].y,miny,maxy,multy*ywidth);
                        systems[i].connectionx[j]=normalizeStar(systems[k].x,minx,maxx,multx*xwidth);
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
    int normalizeStar(Double i,Double min,Double max,float multiplier){
        //System.out.println(i+"\n");
        i=((i+abs(min))/(abs(max)+abs(min)))*multiplier;
        //System.out.println(i+"\n");
        //System.out.println(i.intValue());
        return i.intValue();
    }
    
    /**
     *
     * @param inputArray
     * @return
     */
    public static double getMaxXSys(StarSystem[] inputArray){ 
        Double maxValue = inputArray[0].x; 
        for(int i=0;i < inputArray.length;i++){ 
          if(inputArray[i].x > maxValue){ 
             maxValue = inputArray[i].x; 
          } 
        } 
        return maxValue; 
    }
    public static Double getMaxYSys(StarSystem[] inputArray){ 
        Double maxValue = inputArray[0].y; 
        for(int i=0;i < inputArray.length;i++){ 
          if(inputArray[i].y > maxValue){ 
             maxValue = inputArray[i].y; 
          } 
        } 
        return maxValue; 
    }
    public static Double getMinXSys(StarSystem[] inputArray){ 
        Double minValue = inputArray[0].x; 
        for(int i=0;i<inputArray.length;i++){ 
          if(inputArray[i].x < minValue){ 
            minValue = inputArray[i].x; 
          } 
        } 
        return minValue; 
    }
    public static Double getMinYSys(StarSystem[] inputArray){ 
    Double minValue = inputArray[0].y; 
        for(int i=0;i<inputArray.length;i++){ 
          if(inputArray[i].y < minValue){ 
            minValue = inputArray[i].y; 
          } 
        } 
        return minValue; 
    } 
    
    
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
