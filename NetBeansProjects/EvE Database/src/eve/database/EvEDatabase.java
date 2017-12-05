/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eve.database;


import java.sql.SQLException;
import javax.swing.JFrame;

/**
 *
 * @author spencer
 */
public class EvEDatabase {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
        MapGenerator start = new MapGenerator();
        JFrame frame= new JFrame("Eve Trader Map");
        frame.add(start);
        frame.setSize(800, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        
        
    }
    
}
