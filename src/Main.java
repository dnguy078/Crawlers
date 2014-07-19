import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import urlStruct.urlStruct;

public class Main 
{
	public static Queue <String> alphabetList = new LinkedList<String>();
	public Main() {
		// TODO Auto-generated constructor stus
	}

	static crawlpackage.Crawl crawler = new crawlpackage.Crawl(); 
	public static void insertReviewPage(String str)
	{
	Stack <urlStruct> masterList;
	//ArrayList<String> userIDList = new ArrayList<String>();

	extractpackage.Extract extracter = new extractpackage.Extract(); 
	jbdcconnectpackage.ConnectionDB connection = new jbdcconnectpackage.ConnectionDB(); 
	//connection.clearDB(); 
	
	//gets all the review pages for the certain drugdrug
	masterList = crawler.pageiterator(str); 

	urlStruct extractCurrentURL; 
	
	try{
		masterList.peek(); 
	}
	catch( EmptyStackException e )
	{
		System.out.println("Empty stack"); 
		return; 
		
	}
	
	
	int [] replyid =  new int[] {1}; 
	replyid[0] = 1; 
	
	///need to reverse this queue 
	try {
		while (masterList.peek() != null){
			extractCurrentURL = masterList.pop(); 
			System.out.println("Popped: "+ extractCurrentURL.url); 
			//insert into database 
				//connection.deleteuserID(extractCurrentURL.url); 
				extracter.toHealthForumPostDB(extractCurrentURL.url, extractCurrentURL.filePath, replyid); 
			}
		}
	catch( EmptyStackException e )
	{
		System.out.println("Empty stack"); 
		return; 
	}
	}
	
	
	
	
	
	public static void reparsingPages()
	{

		jbdcconnectpackage.ConnectionDB connection = new jbdcconnectpackage.ConnectionDB(); 
		Vector<String> downloadedPages = connection.reparse(); 
		int randomNum = 1; 
		System.out.println("downloaded pages: " + downloadedPages.size()); 
		for (int i = 0; i < downloadedPages.size(); i++)
		{
			//insertReviewPage(downloadedPages.get(i));
			try {
				randomNum = 1 + (int)(Math.random()*4); 
				TimeUnit.SECONDS.sleep(randomNum);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		System.out.println("Complete");
		
	}
	
	public static void reparsingPages2()
	{

		jbdcconnectpackage.ConnectionDB connection = new jbdcconnectpackage.ConnectionDB(); 
		ArrayList<String> downloadedPages = test(); 
		int randomNum = 1; 
		System.out.println("Single pages: " + downloadedPages.size()); 
		for (int i = 820; i < downloadedPages.size(); i++)
		{
			insertReviewPage(downloadedPages.get(i));
			System.out.println(downloadedPages.get(i)); 
			try {
				randomNum = 1 + (int)(Math.random()*2); 
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Currently at : " + i + " pages ");
			System.out.println("Remaining pages to reenter: " + (downloadedPages.size()-i) ); 
		}
		System.out.println("Complete");
	}
	
	
	public static void fillalphabetList(String strURL)
	{
		 if(strURL.length() == 0) return;
	        try{
	            //System.out.println("Thread " + myID + " Connecting to " + strURL);
	            Connection connection = Jsoup.connect(strURL);

	            Document doc = connection.userAgent("Mozilla").get();

	            Elements links = doc.select("div[class = boxList boxListAZDrugs]").select("a[href]"); ; 
	            for(Element link : links){
	            	String titlehref = link.attr("abs:href");
	                System.out.println("Title statements: " + titlehref); 
	                alphabetList.add(titlehref); 
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
		System.out.println("Filled alphabetlist:" + alphabetList.size()); 
	}
	public static ArrayList<String> test()
	{
		 jbdcconnectpackage.ConnectionDB connection = new jbdcconnectpackage.ConnectionDB(); 
		 HashMap<String, String> map = connection.reparse2(); 
		 ArrayList<String> singlePages = new ArrayList<String>();
		 int i = 0;
		for (Map.Entry<String, String> entry : map.entrySet())
		{
			i++; 
		    //System.out.println(entry.getKey() + "/" + entry.getValue());
		    if (!entry.getKey().contains("?page="))
		    {
		    	singlePages.add(entry.getKey()); 
		    //	System.out.println("Added" + entry.getKey()); 
		    }
		}
		System.out.println("I: " + i);
		System.out.println("single size " + singlePages.size()); 
		return singlePages; 
	}
	
	public class replyid
	{
		int replyid; 
		public replyid()
		{
			replyid = 1; 
		}
	}

	public static void main(String[] args) {
		//reparsingPages(); 

		 jbdcconnectpackage.ConnectionDB connection = new jbdcconnectpackage.ConnectionDB(); 
		 connection.removeMismatch(); 
		//reparsingPages2(); 
		 System.out.println("Finished Delating");
		 /*
		 http://www.drugs.com/comments/tamoxifen/

		 
		 
			 http://www.drugs.com/comments/selegiline/emsam.html
				 http://www.drugs.com/comments/omeprazole-sodium-bicarbonate/

				 http://www.drugs.com/comments/tamoxifen/
	
				 http://www.drugs.com/comments/magnesium-sulfate-potassium-sulfate-sodium-sulfate/suprep-bowel-prep-kit.html

				 http://www.drugs.com/comments/pancrelipase/pancrease-mt-20.html
			
				 http://www.drugs.com/comments/diphenhydramine/nytol.html
			
				 http://www.drugs.com/comments/technetium-tc-99m-tilmanocept/
*/
		 
		//extractpackage.Extract extracter = new extractpackage.Extract(); 
		
		//replyid replyidStruct = null;
		//reparsingPages2(); 
		
		 //connection.findDuplicate("Anonymous"); 
		
		
		//System.out.println("replyid " + replyid[0]); 
		//insertReviewPage("http://www.drugs.com/comments/amphetamine-dextroamphetamine/adderall.html");		
		//extractpackage.Extract extracter = new extractpackage.Extract(); 
		//extracter.toHealthForumPostDB("test ", "http://www.drugs.com/comments/bismuth-subcitrate-potassium-metronidazole-tetracycline/");
		
		 //jbdcconnectpackage.ConnectionDB connection = new jbdcconnectpackage.ConnectionDB(); 
		 //connection.clearDB();
		
		
		
		// Main code 
		/*   // 
		jbdcconnectpackage.ConnectionDB connection = new jbdcconnectpackage.ConnectionDB(); 
		crawlpackage.Crawl crawler = new crawlpackage.Crawl(); 
		fillalphabetList("http://www.drugs.com/drug_information.html"); 
		System.out.println("Start: "); 
		System.out.println("Removed the top: " + alphabetList.remove()); 
		System.out.println("Removed the next top " + alphabetList.poll()); 
		System.out.println("Removed the next top " + alphabetList.poll()); 
		while (!alphabetList.isEmpty())
		{
			String alphabetCurrent = alphabetList.peek();
			crawler.getAllReviewLinks(alphabetList.poll()); //gets all reviews for a A
			ArrayList<String> masterList = crawler.getmasterListCrawl(); //returns a masterlist of all reviews
			System.out.println("Size: "  + masterList.size()); 
			for (int i = 0; i < masterList.size(); i++)
			{	
				System.out.println("Masterlist" + masterList.get(i)); 
				insertReviewPage(masterList.get(i));		
			}
			try {
				crawler.writeToText("log.txt", "Complete crawling " + alphabetCurrent + " Will sleep for 5 hours" );
				TimeUnit.MINUTES.sleep(5);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		//end of main code
	}
}



