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
public class Map {
    Database_IO base= new Database_IO();
    Map(String RegionName,boolean Highsec,boolean Lowsec,boolean Nullsec,int transFilter,boolean useID,int FilterQuantity,String item,String filterType) throws SQLException{
        base.loadSystemInfo(RegionName,Highsec,Lowsec,Nullsec,transFilter,useID,FilterQuantity,item,filterType);
        
        StarSystem[] Systems= new StarSystem[base.SystemCount(RegionName,Highsec,Lowsec,Nullsec,transFilter,useID,FilterQuantity,item,filterType)];
        
        //int SystemSources[]=base.GetConnectsFrom();
        //System.out.print( Systems[0].name+"\n");
        String SysNames[]= base.GetSystemName();
        Double SysListX[]= base.GetSystemLocX();
        Double SysListY[]= base.GetSystemLocY();
        Double SysListSec[]=base.GetSystemSecurity();
        int SysRegionIDs[]= base.GetRegionID();
        int SysSysIDs[];
        SysSysIDs = base.GetSolarSystemID();
        int SysConnections[][]=base.loadConnectsTo(SysSysIDs,RegionName,Highsec,Lowsec,Nullsec,transFilter,useID,FilterQuantity,item,filterType);
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
        frame.setSize(1800,1050);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        frame.setVisible(true);
        
        drawMap mappy = new drawMap(Systems);
        frame.add(mappy);
    }
}
