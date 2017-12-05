/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eve.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author spencer
 */
public class Database_IO {
    
             // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://24.107.44.218:3306/EveOnline";

   //  Database credentials
   static final String USER = "Spencer";
   static final String PASS = "spencer";
   
  
   String []systemName= new String[10000];
   Double []x= new Double[10000];
   Double []y= new Double[10000];
   Double []security= new Double[10000];
   int []regionID= new int[10000];
   int []solarSystemID= new int[10000];
   int [][]connectTo= new int[10000][20];
    
    public int[][] GetConnectsTo(){
        return connectTo;
    }
    public Double[] GetSystemLocX(){
        return x;
    }
    public Double[] GetSystemLocY(){
        return y;
    }
    public Double[] GetSystemSecurity(){
        return security;
    }
    public String[] GetSystemName(){
        return systemName;
    }
    public int[] GetRegionID(){
        return regionID;
    }
    public int[] GetSolarSystemID(){
        return solarSystemID;
    }
    public void loadSystemInfo(String RegionName,boolean Highsec,boolean Lowsec, boolean Nullsec,int transFilter,boolean useID,int filterQuantity,String item, String filterType) throws SQLException {
        //
        Connection conn = null;
        Statement stmt = null;
        String append="";
        String midpend="";
        //String item2= request.getParameter(item);
        int idnum;
        if(!useID){
            idnum=typeNametoID(item);
            item=Integer.toString(idnum);
        }
        if(Highsec&&Lowsec&&Nullsec){
        }else{
           if(Lowsec){ 
             if(Nullsec){
              append=" AND mapSolarSystems.security<.5";   
             }else if(Highsec){
              append=" AND mapSolarSystems.security>.1";
             }else{
               append=" AND (mapSolarSystems.security>.1 AND mapSolarSystems.security<.5) ";
             }
           }
           else if(Highsec){
               if(Nullsec){
                   append=" AND (mapSolarSystems.security>.5 OR mapSolarSystems.security<.1)";
               }
               else{
                   append=" AND (mapSolarSystems.security>.5)";
               }
           }else if(Nullsec){
               append=" AND (mapSolarSystems.security<.1)";
           }
        }
        
        if(transFilter==1){
            midpend=",transactions";
             append=append +" AND transactions.typeid = '"+item+"' AND transactions.systemid = mapSolarSystems.solarSystemID";
        }

        
        try{
           //STEP 2: Register JDBC driver
           Class.forName("com.mysql.jdbc.Driver");

           //STEP 3: Open a connection
           System.out.println("Connecting to a selected database...");
           conn = DriverManager.getConnection(DB_URL, USER, PASS);
           System.out.println("Connected database successfully...");
           String sql= null;
           stmt = conn.createStatement();
            if(transFilter==3){
                if(filterType.equals("Connections")){
                    
                }
                if(filterType.equals("Stations")){
                    if(RegionName==null){
                        sql="SELECT DISTINCT mapSolarSystems.regionID,solarSystemID,solarSystemName,x,z,security FROM mapSolarSystems JOIN stations on mapSolarSystems.solarSystemID = stations.systemid WHERE mapSolarSystems.regionID <11000001 AND (SELECT COUNT(*) FROM stations) >"+Integer.toString(filterQuantity)+append;
                    }
                    else{
                        sql="SELECT DISTINCT mapSolarSystems.regionID,solarSystemID,solarSystemName,x,z,security FROM mapSolarSystems JOIN stations on mapSolarSystems.solarSystemID = stations.systemid WHERE (SELECT COUNT(*) FROM stations) >"+Integer.toString(filterQuantity)+append;

                    }
                }
                if(filterType.equals("Sell Orders")){
                    if(RegionName==null){
                        sql="SELECT DISTINCT mapSolarSystems.regionID,solarSystemID,solarSystemName,x,z,security FROM mapSolarSystems JOIN transactions on mapSolarSystems.solarSystemID = transactions.systemid WHERE mapSolarSystems.regionID <11000001  AND transactions.bid=0 AND (SELECT COUNT(*) FROM transactions) >"+Integer.toString(filterQuantity)+append;
                    }
                    else{
                        sql="SELECT DISTINCT mapSolarSystems.regionID,solarSystemID,solarSystemName,x,z,security FROM mapSolarSystems JOIN transactions on mapSolarSystems.solarSystemID = transactions.systemid WHERE transactions.bid=0 AND (SELECT COUNT(*) FROM transactions) >"+Integer.toString(filterQuantity)+append;

                    }
                }
                if(filterType.equals("Buy Orders")){
                    if(RegionName==null){
                        sql="SELECT DISTINCT mapSolarSystems.regionID,solarSystemID,solarSystemName,x,z,security FROM mapSolarSystems JOIN transactions on mapSolarSystems.solarSystemID = transactions.systemid WHERE transactions.bid=1 AND mapSolarSystems.regionID <11000001 AND (SELECT COUNT(*) FROM transactions) >"+Integer.toString(filterQuantity)+append;
                    }
                    else{
                        sql="SELECT DISTINCT mapSolarSystems.regionID,solarSystemID,solarSystemName,x,z,security FROM mapSolarSystems JOIN transactions on mapSolarSystems.solarSystemID = transactions.systemid WHERE transactions.bid=1 AND (SELECT COUNT(*) FROM transactions) >"+Integer.toString(filterQuantity)+append;

                    }
                }
            }else if(RegionName==null){
            sql = "SELECT DISTINCT mapSolarSystems.regionID,solarSystemID,solarSystemName,x,z,security FROM mapSolarSystems"+midpend+" WHERE mapSolarSystems.regionID <11000001"+append;
            }else{
            sql = "SELECT DISTINCT regions.regionID,solarSystemID,solarSystemName,x,z,security FROM mapSolarSystems,regions"+midpend+" WHERE mapSolarSystems.regionID = regions.regionid AND regions.name = '"+RegionName+"'"+append;  
            }
           System.out.println(sql);
           
           ResultSet rs = stmt.executeQuery(sql);
           int i =0;
           while(rs.next()){
                systemName[i]= rs.getString("solarSystemName");
                x[i]= rs.getDouble("x");
                y[i]= rs.getDouble("z");
                security[i]= rs.getDouble("security");
                regionID[i]= rs.getInt("regionID");
                solarSystemID[i]= rs.getInt("solarSystemID");
              i++;
           }
           rs.close();

        }catch(SQLException se){
           //Handle errors for JDBC
           se.printStackTrace();
        }
        catch(Exception e){
           //Handle errors for Class.forName
           e.printStackTrace();
        }
        finally{
           //finally block used to close resources
           try{ 
              if(conn!=null)
                 conn.close();
           }
           catch(SQLException se){
              se.printStackTrace();
           }//end finally try
        }//end try
        
    }
    public int[][] loadConnectsTo(int SysIDs[],String RegionName,boolean Highsec,boolean Lowsec, boolean Nullsec,int transFilter,boolean useID,int filterQuantity,String item,String filterType){
        int [][]connections= new int[SystemCount(RegionName,Highsec,Lowsec,Nullsec,transFilter,useID,filterQuantity,item,filterType)][20];
        Connection conn = null;
        Statement stmt = null;
        String append="";
        String midpend="";
        int idnum;
        if(!useID){
            idnum=typeNametoID(item);
            item=Integer.toString(idnum);
        }
        if(Highsec&&Lowsec&&Nullsec){
        }else{
           if(Lowsec){ 
             if(Nullsec){
              append=" AND systems.securityLvl<.5";   
             }else if(Highsec){
              append=" AND systems.securityLvl>.1";
             }else{
               append=" AND (systems.securityLvl>.1 AND systems.securityLvl<.5) ";
             }
           }
           else if(Highsec){
               if(Nullsec){
                   append=" AND (systems.securityLvl>.5 OR systems.securityLvl<.1)";
               }
               else{
                   append=" AND (systems.securityLvl>.5)";
               }
           }else if(Nullsec){
               append=" AND (systems.securityLvl<.1)";
           }
        }
        if(transFilter==1){
            midpend=",transactions";
             append=append +" AND transactions.typeid = '"+item+"' AND transactions.systemid = systems.systemid";
        }
        
        try{
           //STEP 2: Register JDBC driver
           Class.forName("com.mysql.jdbc.Driver");

           //STEP 3: Open a connection
           System.out.println("Connecting to a selected database...");
           conn = DriverManager.getConnection(DB_URL, USER, PASS);
           System.out.println("Connected database successfully...");
           String sql="";
           stmt = conn.createStatement();
            if(transFilter==3){
                if(filterType.equals("Stations")){
                    if(RegionName==null){
                        sql="SELECT DISTINCT target,source FROM connectsto,systems JOIN stations on systems.systemid = stations.systemid WHERE systems.systemid=target AND (SELECT COUNT(*) FROM stations) >"+Integer.toString(filterQuantity)+append;
                    }
                    else{
                        sql="SELECT DISTINCT target,source FROM connectsto,systems JOIN stations on systems.systemid = stations.systemid WHERE systems.systemid=target AND regions.name = '"+RegionName+"' AND systems.regionid = regions.regionid AND (SELECT COUNT(*) FROM stations) >"+Integer.toString(filterQuantity)+append;

                    }
                }
                if(filterType.equals("Sell Orders")){
                    if(RegionName==null){
                        sql="SELECT DISTINCT target,source FROM connectsto,systems JOIN transactions on transactions.systemid = systems.systemid WHERE systems.systemid=target AND transactions.bid=0 AND (SELECT COUNT(*) FROM transactions) >"+Integer.toString(filterQuantity)+append;
                    }
                    else{
                        sql="SELECT DISTINCT target,source FROM connectsto,systems JOIN transactions on transactions.systemid = systems.systemid WHERE systems.systemid=target AND regions.name = '"+RegionName+"' AND transactions.bid=0 AND systems.regionid = regions.regionid AND (SELECT COUNT(*) FROM transactions) >"+Integer.toString(filterQuantity)+append;

                    }
                }
                if(filterType.equals("Buy Orders")){
                    if(RegionName==null){
                        sql="SELECT DISTINCT target,source FROM connectsto,systems JOIN transactions on transactions.systemid = systems.systemid WHERE transactions.bid=1 AND systems.systemid=target AND (SELECT COUNT(*) FROM transactions) >"+Integer.toString(filterQuantity)+append;
                    }
                    else{
                        sql="SELECT DISTINCT target,source FROM connectsto,systems JOIN transactions on transactions.systemid = systems.systemid WHERE transactions.bid=1 AND systems.systemid=target AND regions.name = '"+RegionName+"' AND systems.regionid = regions.regionid AND (SELECT COUNT(*) FROM transactions) >"+Integer.toString(filterQuantity)+append;

                    }
                }
            }else if(RegionName==null){
            sql = "SELECT DISTINCT * FROM connectsto";
            sql.concat(append);

           }else{
            sql= "SELECT DISTINCT target,source FROM connectsto,systems,regions"+midpend+" WHERE systems.systemid = target AND systems.regionid = regions.regionid AND regions.name = '"+RegionName+"'"+append;
            
            

           }
            try (ResultSet rs = stmt.executeQuery(sql)) {
                int i=0;
                int j=0;
                //System.out.println(SysIDs[i]+"\n");
                while(rs.next()){
                    if(i<SysIDs.length&&i<connections.length)
                    if(rs.getInt("target")==SysIDs[i]){
                        connections[i][j] = rs.getInt("source");
                        j++;
                    }else{
                        j=0;
                        i++;
                    }
                }}
        }catch(SQLException | ClassNotFoundException se){
           //Handle errors for JDBC
           se.printStackTrace();
        }
       //Handle errors for Class.forName

        finally{
           //finally block used to close resources
           try{ 
              if(conn!=null)
                 conn.close();
           }
           catch(SQLException se){
           }//end finally try
        }//end try
        System.out.println("finished");
        return connections;
    }
    public int SystemCount(String RegionName,boolean Highsec,boolean Lowsec, boolean Nullsec,int transFilter,boolean useID,int filterQuantity,String item,String filterType){
        int count=0;
        Connection conn = null;
        Statement stmt = null;
        String append="";
        String midpend="";
        int idnum;
        if(!useID){
            idnum=typeNametoID(item);
            item=Integer.toString(idnum);
        }
        if(Highsec&&Lowsec&&Nullsec){
        }else{
           if(Lowsec){ 
             if(Nullsec){
              append=" AND mapSolarSystems.security<.5";   
             }else if(Highsec){
              append=" AND mapSolarSystems.security>.1";
             }else{
               append=" AND (mapSolarSystems.security>.1 AND mapSolarSystems.security<.5) ";
             }
           }
           else if(Highsec){
               if(Nullsec){
                   append=" AND (mapSolarSystems.security>.5 OR mapSolarSystems.security<.1)";
               }
               else{
                   append=" AND (mapSolarSystems.security>.5)";
               }
           }else if(Nullsec){
               append=" AND (mapSolarSystems.security<.1)";
           }
        }
        if(transFilter==1){
            midpend=",transactions";
            append=append +" AND transactions.typeid = '"+item+"' AND transactions.systemid = mapSolarSystems.solarSystemID";
        }
        try{
           //STEP 2: Register JDBC driver
           Class.forName("com.mysql.jdbc.Driver");

           //STEP 3: Open a connection
           System.out.println("Connecting to a selected database...");
           conn = DriverManager.getConnection(DB_URL, USER, PASS);
           System.out.println("Connected database successfully...");

           stmt = conn.createStatement();

           String sql="";
           if(transFilter==3){
                if(filterType.equals("Connections")){
                    
                }
                if(filterType.equals("Stations")){
                    if(RegionName==null){
                        sql="SELECT COUNT(DISTINCT mapSolarSystems.regionID,solarSystemID,solarSystemName,x,z,security) FROM mapSolarSystems JOIN stations on mapSolarSystems.solarSystemID = stations.systemid WHERE mapSolarSystems.regionID <11000001 AND (SELECT COUNT(*) FROM stations) >"+Integer.toString(filterQuantity)+append;
                    }
                    else{
                        sql="SELECT COUNT(DISTINCT mapSolarSystems.regionID,solarSystemID,solarSystemName,x,z,security) FROM mapSolarSystems,regions JOIN stations on mapSolarSystems.solarSystemID = stations.systemid WHERE regions.name = '"+RegionName+"' AND (SELECT COUNT(*) FROM stations) >"+Integer.toString(filterQuantity)+append;

                    }
                }
                if(filterType.equals("Sell Orders")){
                    if(RegionName==null){
                        sql="SELECT COUNT(DISTINCT mapSolarSystems.regionID,solarSystemID,solarSystemName,x,z,security) FROM mapSolarSystems JOIN transactions on mapSolarSystems.solarSystemID = transactions.systemid WHERE mapSolarSystems.regionID <11000001 AND transactions.bid=0 AND (SELECT COUNT(*) FROM transactions) >"+Integer.toString(filterQuantity)+append;
                    }
                    else{
                        sql="SELECT COUNT(DISTINCT mapSolarSystems.regionID,solarSystemID,solarSystemName,x,z,security) FROM mapSolarSystems,regions JOIN transactions on mapSolarSystems.solarSystemID = transactions.systemid WHERE regions.name = '"+RegionName+"' AND transactions.bid=0 AND (SELECT COUNT(*) FROM transactions) >"+Integer.toString(filterQuantity)+append;

                    }
                }
                if(filterType.equals("Buy Orders")){
                    if(RegionName==null){
                        sql="SELECT COUNT(DISTINCT mapSolarSystems.regionID,solarSystemID,solarSystemName,x,z,security) FROM mapSolarSystems JOIN transactions on mapSolarSystems.solarSystemID = transactions.systemid WHERE transactions.bid=1 AND mapSolarSystems.regionID <11000001 AND (SELECT COUNT(*) FROM transactions) >"+Integer.toString(filterQuantity)+append;
                    }
                    else{
                        sql="SELECT COUNT(DISTINCT mapSolarSystems.regionID,solarSystemID,solarSystemName,x,z,security) FROM mapSolarSystems,regions JOIN transactions on mapSolarSystems.solarSystemID = transactions.systemid WHERE transactions.bid=1 AND regions.name = '"+RegionName+"' AND (SELECT COUNT(*) FROM transactions) >"+Integer.toString(filterQuantity)+append;

                    }
                }
            }else if(RegionName==null){
                sql = "SELECT COUNT(DISTINCT mapSolarSystems.regionID,solarSystemID,solarSystemName,x,z,security) FROM mapSolarSystems"+midpend+" WHERE mapSolarSystems.regionID <11000001"+append;
                sql.concat(append);
            }else{
                 sql = "SELECT COUNT(DISTINCT mapSolarSystems.regionID,solarSystemID,solarSystemName,x,z,security) FROM mapSolarSystems,regions"+midpend+" WHERE mapSolarSystems.regionID = regions.regionid AND regions.name = '"+RegionName+"'"+append;
                 

           }
           System.out.println(sql);
           ResultSet rs = stmt.executeQuery(sql);
           if(rs.next()){
               count = rs.getInt(1);
           }
           rs.close();

        }catch(SQLException se){
           //Handle errors for JDBC
           se.printStackTrace();
        }
        catch(Exception e){
           //Handle errors for Class.forName
           e.printStackTrace();
        }
        finally{
           //finally block used to close resources
           try{ 
              if(conn!=null)
                 conn.close();
           }
           catch(SQLException se){
              se.printStackTrace();
           }//end finally try
        }//end try
        return count;
    }
    public int typeNametoID(String name){
        Connection conn = null;
        Statement stmt = null;
        int ID=0;
         try{
           //STEP 2: Register JDBC driver
           Class.forName("com.mysql.jdbc.Driver");

           //STEP 3: Open a connection
           System.out.println("Connecting to a selected database...");
           conn = DriverManager.getConnection(DB_URL, USER, PASS);
           System.out.println("Connected database successfully...");

           stmt = conn.createStatement();

           String sql = "SELECT typeid FROM resources WHERE name= '"+name+"' ";
           ResultSet rs = stmt.executeQuery(sql);
           
           if(rs.next()){
              ID = rs.getInt("typeid");
           }
           rs.close();

        }catch(SQLException se){
           //Handle errors for JDBC
           se.printStackTrace();
        }catch(Exception e){
           //Handle errors for Class.forName
           e.printStackTrace();
        }finally{
           //finally block used to close resources
           try{ 
              if(conn!=null)
                 conn.close();
           }catch(SQLException se){
              se.printStackTrace();
           }//end finally try
        }//end try
        
        return ID;
    }
    public String[] getRegionNames() throws SQLException {
        String RegionNames[]= new String[200];
        Connection conn = null;
        Statement stmt = null;
        
        try{
           //STEP 2: Register JDBC driver
           Class.forName("com.mysql.jdbc.Driver");

           //STEP 3: Open a connection
           System.out.println("Connecting to a selected database...");
           conn = DriverManager.getConnection(DB_URL, USER, PASS);
           System.out.println("Connected database successfully...");

           stmt = conn.createStatement();

           String sql = "SELECT name FROM regions WHERE regionid <11000001";
           ResultSet rs = stmt.executeQuery(sql);
           int i =0;
           while(rs.next()){

              RegionNames[i] = rs.getString("name");

              //Display values
              System.out.print("\n" + RegionNames[i]);
              i++;
           }
           rs.close();

        }catch(SQLException se){
           //Handle errors for JDBC
           se.printStackTrace();
        }catch(Exception e){
           //Handle errors for Class.forName
           e.printStackTrace();
        }finally{
           //finally block used to close resources
           try{ 
              if(conn!=null)
                 conn.close();
           }catch(SQLException se){
              se.printStackTrace();
           }//end finally try
        }//end try
        return RegionNames;
    }
}
