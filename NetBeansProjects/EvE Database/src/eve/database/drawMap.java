/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eve.database;

import java.awt.Color;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import static java.lang.Math.abs;
import javax.swing.JPanel;
/**
 *
 * @author spencer
 */
public class drawMap extends JPanel {
    StarSystem[] systems;
    drawMap(StarSystem[] syses){
        systems=syses;
        setBackground(Color.BLACK); 
        setOpaque(true);
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
        Rectangle[] stars = new Rectangle[10000];
        int tempx=0,tempy=0,tempx2=0,tempy2=0;
        Double minx=getMinXSys(systems);
        Double miny=getMinYSys(systems);
        Double maxx=getMaxXSys(systems);
        Double maxy=getMaxYSys(systems);
        Double dmulty;
        if(maxx>maxy)
                dmulty= (maxy/maxx);
        else
            dmulty=(maxx/maxy);
        float multy =dmulty.floatValue();
        float multx= 1-multy;
        System.out.println(multx+" "+multy);
        for(int i=0;i<systems.length;i++){
            //systems[i]= new StarSystem();
            
            tempx=normalizeStar(systems[i].x,minx,maxx,multx*4000);
            tempy=normalizeStar(systems[i].y,miny,maxy,multy*1300);
            //System.out.println("("+tempx+","+tempy+")\n");
            stars[i]= new Rectangle(tempx,tempy,7,7);
            if(systems[i].sec_status>.5d){
                g2.setColor(Color.BLUE);
            }else if(systems[i].sec_status>.1d){
                g2.setColor(Color.ORANGE);
            }else{
                g2.setColor(Color.RED);
            }
            g2.draw(stars[i]);
            for(int j=0;j<systems[i].connectsTo.length;j++){
                for(int k=0;k<systems.length;k++){
                    if(systems[i].connectsTo[j]==systems[k].systemID){
                        tempy2=normalizeStar(systems[k].y,miny,maxy,multy*1300);
                        tempx2=normalizeStar(systems[k].x,minx,maxx,multx*4000);
                        g2.drawLine(tempx, tempy, tempx2, tempy2);

                    }
                }
            }
        }
    }
}
