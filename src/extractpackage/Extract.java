package extractpackage;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Stack;
import java.util.Vector;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import drugpostStruct.drugpostStruct;


public class Extract 
{
	public void reparse()
	{
		jbdcconnectpackage.ConnectionDB connectDB = new jbdcconnectpackage.ConnectionDB(); 
		Vector<String> downloadedPages = connectDB.reparse(); 
		System.out.println("Downloaded pages = " + downloadedPages.size());
		for (int i = 0; i < 5; i++)
		{
			System.out.println("Current crawling" + downloadedPages.get(i));
			//toHealthForumPostDB("testing" , downloadedPages.get(i));
			
			
		}
	
		System.out.println("Done!!!"); 
		
	}

	
	
	
	
	public void toHealthForumPostDB(String urllink, String strURL, int [] replyid)
	{
		drugpostStruct post = new drugpostStruct(); 
    	if(strURL.length() == 0) return;
        try{
            //Connection connection = Jsoup.connect(urllink);
            //Document doc = connection.userAgent("Mozilla").get();
        	//read file
        	
            File in = new File(strURL);
            System.out.println(in.exists()); 
            System.out.println("FilePath: " + strURL); 
            BufferedReader br = new BufferedReader(new FileReader(strURL));     
            if (br.readLine() == null) {
                System.out.println("empty file" + strURL); 
                br.close(); 
                return; 
            }
            br.close(); 
            Document doc = Jsoup.parse(in,"utf-8"); 

            Iterator<org.jsoup.nodes.Element> itr = doc.select("div:eq(1) > table").iterator(); 

            itr.next(); 
            Stack<org.jsoup.nodes.Element> reversedList = new Stack<org.jsoup.nodes.Element>(); 
            int id = 0; 
            while (itr.hasNext()){
            	reversedList.push(itr.next()); 
            }
            
		try {
			while (reversedList.peek() != null){
	            	Element current = reversedList.pop();
	            	String reviewline =  current.select("tbody > tr:eq(0) > td:eq(0)").text(); 
	            	//parses review line to obtain userid, likes
	            	String userid = reviewline.substring(reviewline.indexOf("by ")+3,reviewline.indexOf(":")); 
	            	System.out.println(userid); 
	            	if (userid.contains("(taken"))
	            	{
	            		System.out.println("Stripped Taken from userid");
	            		userid = userid.substring(0,userid.indexOf("(t")-1);
	            		
	            	}

	            	String likes = "";  
	            	likes = current.select("[id*=count]").text();
	            	
	                //*[@id="intelliTXT"]/div[2]/table[5]/tbody/tr[1]/td[4]/div
	            	String ratings = current.select("tbody > tr:eq(0) > td:eq(3) > div").text(); 
	            	//body
	            	//*[@id="intelliTXT"]/div[2]/table[2]/tbody/tr[2]/td/div/p/span/text()
	            	String body = current.select("tbody > tr:eq(1) > td > div > p > span").text(); 
	            	//drug name and disorder
	            	//*[@id="intelliTXT"]/div[2]/table[2]/tbody/tr[2]/td/div/p/b
	            	String drugline = current.select("tbody > tr:eq(1) > td > div > p > b").text(); 
	            	//System.out.println("Drugline" + drugline);
	            	//parse drugline
	            	String drug  = ""; 
	            	String disorder = ""; 
	            	if (drugline.contains("for")){
	            		
	            		drug = drugline.substring(0,drugline.indexOf("for")); 

	            		disorder = drugline.substring(drugline.indexOf("for") + 3); 
	            	}
	            	else
	            	{
	            		drug = drugline; 
	            	}
	            	
	            	//date
	            	////*[@id="intelliTXT"]/div[2]/table[2]/tbody/tr[3]/td
	            	post.url = urllink;
	            	
	            	
	            	String date = current.select("tbody > tr:eq(2) > td").text(); 
	            	
	            	if (!userid.equals(""))
	            	{

	            		if (userid.length() >= 50)
	            		{
	            			userid = userid.substring(0,49);
	            		}
	            		post.userid = userid;
	            	}
	            	if (!date.equals(""))
	            	{
	            		post.posttime = date;
	            	}
	            	if (!body.equals(""))
	            	{
	            		post.body = body; 
	            	}
	            	if (!drug.equals("")){
	            		if (drug.length() >= 50)
	            		{
	            			drug = drug.substring(0,49);
	            		}
	            		post.drugname = drug; 
	            	}

	            	if (!disorder.equals("")){
	            		post.disorder = disorder; 
	            	}
	            	if (!likes.equals(""))
	            	{
	            		int likeConvert = Integer.parseInt(likes);
	            		post.likes = likeConvert;
	            	}
	            	if (!ratings.equals(""))
	            	{
	            		Double ratingConvert = Double.valueOf(ratings);
	            		int ratingScale = (int) (ratingConvert * 10); 
	            		post.rating = ratingScale; 
	            	}
	            	if (post.userid != null)
	             	{
		        		jbdcconnectpackage.ConnectionDB connectDB = new jbdcconnectpackage.ConnectionDB(); 
		            	post.replyid = replyid[0]; 
		            	System.out.println(post.url); 
		            	System.out.println("Replyid " + replyid[0]); 
		            	//System.out.println("Body" + body ); 
		            	//System.out.println("Drug" + drug); 
		            	//System.out.println("date" + date); 
		            	//System.out.println("Disorder" + disorder); 
		            	//System.out.println("Ratings " + ratings); 
		            	//System.out.println("likes " + likes); 
		            	System.out.println("userid " + userid); 
		            	if (userid.matches("Anonymous"))
		            	{
		            		post.userid = connectDB.findDuplicate("Anonymous"); 
		            		System.out.println("newuser:" + post.userid); 
		            	}
		
		            	connectDB.insertPostDB(post);
		            	replyid[0]++; 
		            	
	            	}
				}
			}
			catch( EmptyStackException e )
			{
				System.out.println("Empty stack"); 
				return; 
			}
        }
        catch(UnsupportedMimeTypeException err){
            System.out.println("[Error]: HTTP Connection failed. Unsupported mime type.");
        }catch(SocketTimeoutException err){
            System.out.println("[Error]: Could not connect to " + strURL + ": connection timed out.");
        }catch(HttpStatusException err){
            System.out.println("[Error]: HTTP error fetchung url " + strURL);
        }catch(UnknownHostException err){
            System.out.println("[Error]: Cannot connect to unknown host.");
        }catch(IOException err){
            System.out.println("[Error]: Jsoup can not connect to " + strURL); //"\n\t" + err.getMessage());
        }
	}
}