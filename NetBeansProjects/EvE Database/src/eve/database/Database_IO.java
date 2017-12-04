/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eve.database;

import static com.sun.webkit.perf.WCGraphicsPerfLogger.log;
import static java.lang.Math.log;
import static java.lang.StrictMath.log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static sun.util.logging.LoggingSupport.log;

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
    public void loadSystemInfo(String RegionName) throws SQLException {
        //
        Connection conn = null;
        Statement stmt = null;
        try{
           //STEP 2: Register JDBC driver
           Class.forName("com.mysql.jdbc.Driver");

           //STEP 3: Open a connection
           System.out.println("Connecting to a selected database...");
           conn = DriverManager.getConnection(DB_URL, USER, PASS);
           System.out.println("Connected database successfully...");
           String sql;
           stmt = conn.createStatement();
           if(RegionName==null){
            sql = "SELECT regionID,solarSystemID,solarSystemName,x,z,security FROM mapSolarSystems WHERE regionID <11000001";
           }else{
            sql = "SELECT regions.regionID,solarSystemID,solarSystemName,x,z,security FROM mapSolarSystems,regions WHERE mapSolarSystems.regionID = regions.regionid AND regions.name = '"+RegionName+"'";
            
           }
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
    public int[][] loadConnectsTo(int SysIDs[],String RegionName){
        int [][]connections= new int[SystemCount(RegionName)][20];
        Connection conn = null;
        Statement stmt = null;
        try{
           //STEP 2: Register JDBC driver
           Class.forName("com.mysql.jdbc.Driver");

           //STEP 3: Open a connection
           System.out.println("Connecting to a selected database...");
           conn = DriverManager.getConnection(DB_URL, USER, PASS);
           System.out.println("Connected database successfully...");
           String sql;
           stmt = conn.createStatement();
           if(RegionName==null){
            sql = "SELECT * FROM connectsto";
           }else{
            sql= "SELECT target,source FROM connectsto,systems,regions WHERE systems.systemid = target AND systems.regionid = regions.regionid AND regions.name = '"+RegionName+"'";
           }
            try (ResultSet rs = stmt.executeQuery(sql)) {
                int i=0;
                int j=0;
                //System.out.println(SysIDs[i]+"\n");
                while(rs.next()){
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
    public int SystemCount(String RegionName){
        int count=0;
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

           String sql;
           if(RegionName==null){
                sql = "SELECT COUNT(*) FROM mapSolarSystems WHERE regionID <11000001";
            }else{
                 sql = "SELECT COUNT(*) FROM mapSolarSystems,regions WHERE mapSolarSystems.regionID = regions.regionid AND regions.name = '"+RegionName+"'";

           }
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
        System.out.println("Goodbye!");
        return RegionNames;
    }
}
