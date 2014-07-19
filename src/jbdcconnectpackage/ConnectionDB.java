package jbdcconnectpackage; 


import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Queue;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import drugpostStruct.drugpostStruct;


public class ConnectionDB {
	
	public class Pair{
		String url;
		String fileaddress; 
		
		public Pair(String urlinput, String urlfileAddress){
			url = urlinput; 
			urlfileAddress = fileaddress; 
		}

		public String getURL()
		{
			return url; 
		}

		public String getfAddress()
		{
			return fileaddress; 
		}
	}
	
	
	public void clearDB()
	{
		PreparedStatement pst = null; 
		Connection con = null; 
		PreparedStatement show = null; 
		try
		{
			//DELETE from HEALTHDATA.healthforumposts where source = "http://www.drugs.com";

			con = DriverManager.getConnection("jdbc:mysql://dblab-rack20.cs.ucr.edu:3306","dnguy078", "passwd");
			show = con.prepareStatement("DELETE from HEALTHDATA.healthforumposts where source = \"http://www.drugs.com\";"); 
			show.execute();

			show = con.prepareStatement("DELETE from HEALTHDATA.healthforumusers where source = \"http://www.drugs.com\";"); 
			show.execute(); 
			System.out.println("Deleted"); 

			
		}catch (SQLException ex) {
            Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
		
	}
	public void duplicates(String username)
	{
		PreparedStatement pst = null; 
		Connection con = null; 
		PreparedStatement show = null; 
		Statement stmt = null; 
		Vector<String> crawledLinks = new Vector<String>(); 
		try
		{
			//DELETE from HEALTHDATA.healthforumposts where source = "http://www.drugs.com";

			con = DriverManager.getConnection("jdbc:mysql://localhost:3306","dnguy078", "passwd");
			show = con.prepareStatement("USE crawlerMetadata;"); 
			show.execute();
			//show = con.prepareStatement("Select URI from crawlerMetadata.crawledURLS WHERE FSpath LIKE '%/home/crawlers/drugs/%';"); 
			String query = "Select URI from crawlerMetadata.crawledURLS WHERE FSpath LIKE '%anonymous%';"; 
			ResultSet rs = show.executeQuery(query); 
			//System.out.println(rs.getRow()); 
			crawlpackage.Crawl crawler = new crawlpackage.Crawl(); 

			while(rs.next())
			{
				System.out.println("Link: " + rs.getString(1)); 
				//need to check if the proxy has been blocked 
			}

		}catch (SQLException ex) {
            Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) { con.close(); } } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }		
		
		
	}

	
	
	
	
	public void insertCrawledURL(String strURL, String FSPath, Date date)
	{
		PreparedStatement pst = null; 
		Connection con = null; 
		PreparedStatement show = null; 
		System.out.println("Insert into crawledURL:"); 
		try
		{
			//DELETE from HEALTHDATA.healthforumposts where source = "http://www.drugs.com";
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306","dnguy078", "passwd");
			show = con.prepareStatement("USE crawlerMetadata;"); 
			show.execute();
			pst = con.prepareStatement("INSERT INTO crawlerMetadata.crawledURLS (URI,FSPATH, downloadDate) VALUES(?, ?, ?)"); 
			pst.setString(1, strURL); 
			pst.setString(2, FSPath); 
			pst.setTimestamp(3, new Timestamp(date.getTime())); 
			pst.execute(); 
		}catch (SQLException ex) {
            Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return; 
        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
                return;
            }
        }		
	}	
	public Vector<String> getProxies()
	{
		PreparedStatement pst = null; 
		Connection con = null; 
		PreparedStatement show = null; 
		Statement stmt = null; 
		Vector<String> allProxies = new Vector<String>(); 
		try
		{
			//DELETE from HEALTHDATA.healthforumposts where source = "http://www.drugs.com";

			con = DriverManager.getConnection("jdbc:mysql://localhost:3306","dnguy078", "passwd");
			show = con.prepareStatement("USE crawlerMetadata;"); 
			show.execute();
			show = con.prepareStatement("SELECT * from allProxies;"); 
			String query = "SELECT * from allProxies;"; 
			System.out.println("En"); 
			ResultSet rs = show.executeQuery(query); 
			System.out.println("working"); 
			//System.out.println(rs.getRow()); 
			while(rs.next())
			{
				if (rs.getString(1).substring(0,1).equals("d")) 
				{
					allProxies.add(rs.getString(1)); 
					System.out.println("added: " + rs.getString(1)); 
				}
			}
		
			
		}catch (SQLException ex) {
            Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }		
		return allProxies; 
	}
	
	public HashMap<String, String>  reparse2()
	{
		PreparedStatement pst = null; 
		Connection con = null; 
		PreparedStatement show = null; 
		Statement stmt = null; 
		HashMap<String, String> locationmap = new HashMap<String, String>();
		try
		{
			//DELETE from HEALTHDATA.healthforumposts where source = "http://www.drugs.com";

			con = DriverManager.getConnection("jdbc:mysql://localhost:3306","dnguy078", "passwd");
			show = con.prepareStatement("USE crawlerMetadata;"); 
			show.execute();

			//show = con.prepareStatement("Select URI from crawlerMetadata.crawledURLS WHERE FSpath LIKE '%/home/crawlers/drugs/%';"); 
			String query = "Select * from crawlerMetadata.crawledURLS WHERE FSpath LIKE '%/home/crawlers/drugs/%';"; 
	
			//String query2 = "Select URI from crawlerMetadata.crawledURLS WHERE FSpath LIKE '%/home/crawlers/drugs/%';"; 
			ResultSet rs = show.executeQuery(query); 
			//System.out.println(rs.getRow()); 
			crawlpackage.Crawl crawler = new crawlpackage.Crawl(); 

			while(rs.next())
			{
				//System.out.println("Link: " + rs.getString(1)); 
				//System.out.println("Address: " + rs.getString(2));
				//need to check if the proxy has been blocked 
				crawler.writeToText("reparsed.txt", rs.getString(1) );
				crawler.writeToText("reparsed.txt", rs.getString(2) ); 
				locationmap.put(rs.getString(1),rs.getString(2)); 
			}
		
			
		}catch (SQLException ex) {
            Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) { con.close(); } } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }		
		return locationmap; 
	}	

	
	private Pair Pair(String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}
	public Vector<String> reparse()
	{
		PreparedStatement pst = null; 
		Connection con = null; 
		PreparedStatement show = null; 
		Statement stmt = null; 
		Vector<String> crawledLinks = new Vector<String>(); 
		try
		{
			//DELETE from HEALTHDATA.healthforumposts where source = "http://www.drugs.com";

			con = DriverManager.getConnection("jdbc:mysql://localhost:3306","dnguy078", "passwd");
			show = con.prepareStatement("USE crawlerMetadata;"); 
			show.execute();
			//show = con.prepareStatement("Select URI from crawlerMetadata.crawledURLS WHERE FSpath LIKE '%/home/crawlers/drugs/%';"); 
			String query = "Select URI from crawlerMetadata.crawledURLS WHERE FSpath LIKE '%/home/crawlers/drugs/%';"; 
			ResultSet rs = show.executeQuery(query); 
			//System.out.println(rs.getRow()); 
			crawlpackage.Crawl crawler = new crawlpackage.Crawl(); 

			while(rs.next())
			{
				System.out.println("Link: " + rs.getString(1)); 
				//need to check if the proxy has been blocked 
				crawler.writeToText("reparsed.txt", rs.getString(1) );
				crawledLinks.add(rs.getString(1)); 
			}
		
			
		}catch (SQLException ex) {
            Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) { con.close(); } } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }		
		return crawledLinks; 
	}	
	
	public void deleteuserID(String drugname){
			PreparedStatement pst = null; 
		Connection con = null; 
		PreparedStatement show = null; 
		Statement stmt = null; 
		Vector<String> crawledLinks = new Vector<String>(); 
		try
		{
			con = DriverManager.getConnection("jdbc:mysql://dblab-rack20.cs.ucr.edu:3306","dnguy078", "passwd");
			show = con.prepareStatement("USE HEALTHDATA;"); 
			show.execute();
			
			String query = "Select userid from HEALTHDATA.healthforumposts WHERE source = \"http://www.drugs.com\" AND URL= \""+drugname +"\";"; 
			ResultSet rs = show.executeQuery(query); 

			while(rs.next())
			{
				System.out.println("userid: " + rs.getString(1)); 
				pst = con.prepareStatement("DELETE FROM HEALTHDATA.healthforumposts where source = \"http://www.drugs.com\" and userid= \""+ rs.getString(1)+ "\""); 
				pst.execute(); 
				pst = con.prepareStatement("DELETE FROM HEALTHDATA.healthforumusers where source = \"http://www.drugs.com\" and userid= \""+ rs.getString(1)+ "\""); 
				pst.execute(); 
			}
			System.out.println("Done Deleting "); 
		
			
		}catch (SQLException ex) {
            Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) { con.close(); } } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        
        }
		
		
		
	}
	
	public void removeMismatch()
	{
		PreparedStatement pst = null; 
		Connection con = null; 
		PreparedStatement show = null; 
		Statement stmt = null; 
		Vector<String> crawledLinks = new Vector<String>(); 
		try
		{
			con = DriverManager.getConnection("jdbc:mysql://dblab-rack20.cs.ucr.edu:3306","dnguy078", "passwd");
			show = con.prepareStatement("USE HEALTHDATA;"); 
			show.execute();
						
			String query = "SELECT u.userid from healthforumusers u WHERE source = \"http://www.drugs.com\" AND NOT EXISTS (SELECT * from healthforumposts p WHERE source = \"http://www.drugs.com\" AND u.userid=p.userid);"; 
			ResultSet rs = show.executeQuery(query); 
		
			//if no other values were found
			int i = 0; 
			while(rs.next())
			{
				System.out.println("userid: " + rs.getString(1)); 
				pst = con.prepareStatement("DELETE FROM HEALTHDATA.healthforumusers where source = \"http://www.drugs.com\" and userid= \""+ rs.getString(1)+ "\""); 
				pst.execute(); 
				i++; 
			}
			System.out.println("removed " + i + "Entries"); 
		
			
		}catch (SQLException ex) {
            Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) { con.close(); } } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }		

		
		
		
		
	}
	
	
	
	public String findDuplicate(String userid)
	{
		PreparedStatement pst = null; 
		Connection con = null; 
		PreparedStatement show = null; 
		Statement stmt = null; 
		Vector<String> crawledLinks = new Vector<String>(); 
		try
		{
			con = DriverManager.getConnection("jdbc:mysql://dblab-rack20.cs.ucr.edu:3306","dnguy078", "passwd");
			show = con.prepareStatement("USE HEALTHDATA;"); 
			show.execute();
			
			
			String newuserid = userid + "--x"; 
			int numDuplicates = 1000000; 
			String convNumDuplicates = Integer.toString(numDuplicates); 

			String convertedUserid = newuserid + convNumDuplicates;
			
			String query = "Select * from HEALTHDATA.healthforumusers WHERE source = \"http://www.drugs.com\" AND userid LIKE '"+newuserid +"%';"; 
			ResultSet rs = show.executeQuery(query); 
		
			//if no other values were found
			if (!rs.next())
			{
				System.out.println("no Data found"); 
				System.out.println("Returning userid: "+ convertedUserid); 
				return convertedUserid; 
			}

			else 
			{
				rs.last();
				String lastValue = rs.getString(1);
				System.out.println("LastValue" + lastValue); 
				String getNum = lastValue.substring(lastValue.indexOf("--x")+3,lastValue.indexOf("--x")+10); 
				int value = Integer.parseInt(getNum);
				value++; 
				System.out.println("Value"); 
				String nextuserid = newuserid + Integer.toString(value);
				System.out.println("nextuserid:" + nextuserid) ; 
				return nextuserid; 
			}
		
			
		}catch (SQLException ex) {
            Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) { con.close(); } } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }		
		return ""; 
	
	}
	
	public void insertPostDB(drugpostStruct postStruct)
	{
		PreparedStatement pst = null; 
		Connection con = null; 
		PreparedStatement show = null; 
		PreparedStatement inputUsersPST = null; 
		try
		{
			con = DriverManager.getConnection("jdbc:mysql://dblab-rack20.cs.ucr.edu:3306","dnguy078", "passwd");
			pst = con.prepareStatement("INSERT INTO HEALTHDATA.healthforumposts (URL, replyid, source, userid, posttime, body, drugname, disorder,rating, likes) VALUES(?,?,?,?,?,?,?,?,?,?);"); 
			inputUsersPST = con.prepareStatement("INSERT INTO HEALTHDATA.healthforumusers  (userid, source) VALUES(?,?);"); 
			
			//inputUsersPST = con.prepareStatement("DELETE FROM HEALTHDATA.healthforumusers  (userid, source) VALUES(?,?);"); 
			
			//insert into healthforumusers db
			
			
			try
			{
	            inputUsersPST.setString(1, postStruct.userid);
	            inputUsersPST.setString(2, "http://www.drugs.com");
	            inputUsersPST.executeUpdate(); 
			}
			catch (SQLException ex) {
			
	            Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
	            lgr.log(Level.SEVERE, ex.getMessage(), ex);
	            System.out.println("first catch!!!!!!"); 
	            System.out.println("reattempting"); 
            	postStruct.userid = findDuplicate(postStruct.userid);
	            inputUsersPST.setString(1, postStruct.userid);
	            inputUsersPST.setString(2, "http://www.drugs.com");
	            inputUsersPST.executeUpdate(); 
            
			} 

			//inserting into healthforumposts; 
			pst.setString(1, postStruct.url);
			pst.setInt(2,postStruct.replyid ); 
			pst.setString(3,postStruct.source);
			pst.setString(4, postStruct.userid);
			
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM dd, yyyy"); 
			java.sql.Date date = new java.sql.Date(sdf.parse(postStruct.posttime).getTime());
			
			
			pst.setDate(5, date); 
			pst.setString(6, postStruct.body);
			pst.setString(7, postStruct.drugname);
			pst.setString(8, postStruct.disorder);
			pst.setInt(9, postStruct.rating);
			pst.setInt(10, postStruct.likes);
            pst.executeUpdate();

			
			//pst.executeUpdate(); 
			System.out.println("inserted"); 
			
		}catch (SQLException ex) {
			
            Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            System.out.println("SQLException catch!!!!!!"); 
            
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (inputUsersPST != null){
                	inputUsersPST.close(); 
                }
                
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(ConnectionDB.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
		
	}

	

	
}
