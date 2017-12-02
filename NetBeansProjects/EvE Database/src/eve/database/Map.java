/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eve.database;

import java.awt.Rectangle;
import java.sql.SQLException;
import javax.swing.JFrame;

/**
 *
 * @author spencer
 */
public class Map {
    Database_IO base= new Database_IO();
    Map() throws SQLException{
        base.loadSystemInfo();
        
        StarSystem[] Systems= new StarSystem[base.SystemCount()];
        
        //int SystemSources[]=base.GetConnectsFrom();
        //System.out.print( Systems[0].name+"\n");
        String SysNames[]= base.GetSystemName();
        Double SysListX[]= base.GetSystemLocX();
        Double SysListY[]= base.GetSystemLocY();
        Double SysListSec[]=base.GetSystemSecurity();
        int SysRegionIDs[]= base.GetRegionID();
        int SysSysIDs[]=base.GetSolarSystemID();
        System.out.println("here");
        int SysConnections[][]=base.loadConnectsTo(SysSysIDs);
        for(int i=0;i<Systems.length;i++){
            Systems[i]= new StarSystem();
            Systems[i].name=SysNames[i];
            Systems[i].regionID=SysRegionIDs[i];
            Systems[i].sec_status=SysListSec[i];
            Systems[i].x=SysListX[i];
            Systems[i].y=SysListY[i];
            Systems[i].systemID=SysSysIDs[i];
            Systems[i].connectsTo=SysConnections[i];
        }
        JFrame frame= new JFrame("Eve Trader Map");
        frame.setSize(1800,1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        frame.setVisible(true);
        
        drawMap mappy = new drawMap(Systems);
        frame.add(mappy);
    }
}
