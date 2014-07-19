package crawlpackage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.io.FileUtils;
//import org.apache.http.client.*;

import urlStruct.urlStruct;
//drug crawler
public class Crawl {
	//MasterList tells which pages to crawl
	public ArrayList<String> masterListCrawl = new ArrayList<String>();
	HashMap<String, Integer> map = new HashMap<String, Integer>();
	
	Stack<urlStruct> urllinks = new Stack<urlStruct>();
	jbdcconnectpackage.ConnectionDB connection = new jbdcconnectpackage.ConnectionDB(); 
	public Vector <String> proxies = connection.getProxies();
	static int proxySelect=0; 

	
	//returns a queue, urllinks, which holds the multiple review pages for a drug ie adderall 1, adderall 2
	int randomNum = 1; 
	public Stack<urlStruct> pageiterator(String strURL) 
	{
		try {
			randomNum = 1 + (int)(Math.random()*2); 
			TimeUnit.SECONDS.sleep(randomNum);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String currentURL = strURL;
		System.out.println("CurrentURL: " + currentURL); 
		try {
			System.out.println("Connecting to " + currentURL);
 	       	System.setProperty("http.proxyHost", proxies.get(proxySelect));
	        System.setProperty("http.proxyPort", "3128");
	        
	        proxySelect = proxySelect == proxies.size() - 1 ? 0 : proxySelect + 1;
	        System.out.println("Using proxy: " + proxies.get(proxySelect));
			Connection connection = Jsoup.connect(currentURL);
			 Document doc = connection.userAgent("Mozilla").get();
			 
				//System.out.println("Title statements: " + currentURL);
				String directory =   "/home/crawlers/drugs/";
				writeToText("webpages.txt", currentURL );
				urlStruct URLStruc = new urlStruct(); 
				URLStruc.url = currentURL; 
				String file = fileDownload(currentURL, directory);
				//System.out.println("Downloaded" + currentURL); 
				String path = directory + file; 
				//System.out.println("Path to file: " + path); 
				writeToText("webpages.txt", path );
				URLStruc.filePath = path; 
				urllinks.push(URLStruc);
				java.util.Date dt = new java.util.Date();
				java.text.SimpleDateFormat sdf = 
	            	     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentTime = sdf.format(dt);
				//jbdcconnectpackage.ConnectionDB connection2 = new jbdcconnectpackage.ConnectionDB(); 
				if (currentURL.contains("page")) 
				{
					System.out.println("Added page of " + currentURL); 
					//connection2.insertCrawledURL(currentURL, path, dt); 
				}
			 
			 /*
			if (urllinks.isEmpty())
			{
				System.out.println("Title statements: " + currentURL);
				//String directory =  System.getProperty("user.dir") +"/webpages/"; 
				String directory =   "/home/crawlers/drugs/";
				writeToText("webpages.txt", currentURL );
				urlStruct URLStruc = new urlStruct(); 
				URLStruc.url = currentURL; 
				String file = fileDownload(currentURL, directory);
				String path = directory + file; 
				System.out.println("Path to file: " + path); 
				writeToText("webpages.txt", path );
				URLStruc.filePath = path; 
				urllinks.add(URLStruc);           			            	
				java.util.Date dt = new java.util.Date();
				java.text.SimpleDateFormat sdf = 
	            	     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentTime = sdf.format(dt);
            	jbdcconnectpackage.ConnectionDB connection2 = new jbdcconnectpackage.ConnectionDB(); 
				connection2.insertCrawledURL(currentURL, path, dt); 
			}*/
			Elements links = doc.select("td.paging-list-next").select("a[href]"); 
				for (Element link : links) 
				{
					String reviewpage = link.attr("abs:href");

					pageiterator(reviewpage); 				

				}

		} catch (UnsupportedMimeTypeException err) {
			System.out.println("[Error]: HTTP Connection failed. Unsupported mime type.");
		} catch (SocketTimeoutException err) {
			System.out.println("[Error]: Could not connect to " + strURL
					+ ": connection timed out.");
		} catch (HttpStatusException err) {
			System.out.println("[Error]: HTTP error fetchung url " + strURL);
		} catch (UnknownHostException err) {
			System.out.println("[Error]: Cannot connect to unknown host.");
		} catch (IOException err) {
			System.out.println("[Error]: Jsoup can not connect to " + strURL); // "\n\t"
																				// +
																				// err.getMessage());
		}
		System.out.println("urlinks size "+ urllinks.size()); 
		return urllinks; 
	}

//					System.out.println("vector size"+ urllinks.size()); 
//This gets all the review links starting at a certain alphabet letter ie ) getting only A 
	//string str is the root website for an alphabet letter
	public void getAllReviewLinks(String strurl)
	{
		//next pages
		//gets all different pages for an alhabet a1.html , a2.html
		Queue <String> nextPageList = nextPages(strurl);
		System.out.println("Size of queue: " + nextPageList.size());
		System.out.println("Starting to crawl: ");
		getDiscLinks(strurl);
		String nextURL=""; 
		if (nextPageList.isEmpty())
		{
			return; 
		}
		while (nextPageList.size() <= 10)
		{
			
			nextURL = nextPageList.poll();
			System.out.println(nextURL); 
			getDiscLinks(nextURL);
			System.out.println("After Crawl size of " +  nextPageList.size()); 
			
			
		}
		/*
		while (!nextPageList.isEmpty())
		{
			nextURL = nextPageList.poll();
			System.out.println(nextURL); 
			getDiscLinks(nextURL);
			System.out.println("After Crawl size of " +  nextPageList.size()); 
		}
		*/
		System.out.println("masterListCrawl size: " + masterListCrawl); 
	}
	
	
	//returns links with valid drug reviews
	public void getDiscLinks(String strURL) 
	{		
		System.out.println("Crawling: "+ strURL);
		if (strURL.length() == 0)
			return;
		// System.out.println("Thread " + myID + " is crawling " + strURL);
		try {
			// System.out.println("Thread " + myID + " Connecting to " +
			// strURL); 	       	System.setProperty("http.proxyHost", proxies.get(proxySelect));
	        System.setProperty("http.proxyPort", "3128");
	        
	        proxySelect = proxySelect == proxies.size() - 1 ? 0 : proxySelect + 1;
	        System.out.println("Using proxy: " + proxies.get(proxySelect));
			Connection connection = Jsoup.connect(strURL);
			Document doc = connection.userAgent("Mozilla").get();

			Elements links = doc.select("ul.doc-type-list").select("a[href]"); 
			
			for (Element link : links) {
				String titlehref = link.attr("abs:href");
				//System.out.println("Page: "+ titlehref); 
				String reviewpage = hasReviews(titlehref);
				if (reviewpage!=null)
				{
					if (!map.containsKey(reviewpage))
					{
						map.put(reviewpage, 1);
						masterListCrawl.add(reviewpage); 
						//String directory =  System.getProperty("user.dir") +"\\webpages\\"; 

						//File newFile = new File(directory + fileName(reviewpage)); 
						//newFile.mkdir(); 
						//writeToText("webpagesCrawled.txt", reviewpage );
					}
				}
			}

		} catch (UnsupportedMimeTypeException err) {
			System.out
					.println("[Error]: HTTP Connection failed. Unsupported mime type.");
		} catch (SocketTimeoutException err) {
			System.out.println("[Error]: Could not connect to " + strURL
					+ ": connection timed out.");
		} catch (HttpStatusException err) {
			System.out.println("[Error]: HTTP error fetchung url " + strURL);
		} catch (UnknownHostException err) {
			System.out.println("[Error]: Cannot connect to unknown host.");
		} catch (IOException err) {
			System.out.println("[Error]: Jsoup can not connect to " + strURL); // "\n\t"
																// err.getMessage());
		}
		System.out.println("Done"); 
	}
	
	
	public String hasReviews(String strURL)
	{

		if (strURL.length() == 0)
			return null;
		try {
			Connection connection = Jsoup.connect(strURL);
			Document doc = connection.get();		
			Elements links = doc.select("div[id=sidebox-reviews]").select("a[href]"); 
			
			for (Element link : links) {
				String titlehref = link.attr("abs:href");
				if (titlehref.contains("members_comments_add"))
				{
					return null; 
				}
				else
				{
					return titlehref; 
				}	
			}

		} catch (UnsupportedMimeTypeException err) {
			System.out
					.println("[Error]: HTTP Connection failed. Unsupported mime type.");
		} catch (SocketTimeoutException err) {
			System.out.println("[Error]: Could not connect to " + strURL
					+ ": connection timed out.");
		} catch (HttpStatusException err) {
			System.out.println("[Error]: HTTP error fetchung url " + strURL);
		} catch (UnknownHostException err) {
			System.out.println("[Error]: Cannot connect to unknown host.");
		} catch (IOException err) {
			System.out.println("[Error]: Jsoup can not connect to " + strURL); // "\n\t"
																// err.getMessage());
		}
		return null;
	}
	
	public Queue<String> nextPages(String strURL)
	{
		Queue <String> nextPageList = new LinkedList<String>();
		if (strURL.length() == 0)
			return null;
		try {
			Connection connection = Jsoup.connect(strURL);
			Document doc = connection.get();		
			Elements links = doc.select("div[class *= paging-list paging-list").select("td.paging-list-index").select("a[href]"); 

			int indicatorFlag = 0; 
			for (Element link : links) {
				String titlehref = link.attr("abs:href");
				String texttitle = link.text();
				if (isInteger(texttitle))
				{
					indicatorFlag=1; 
					System.out.println(titlehref);
					nextPageList.add(titlehref); 
					System.out.println("titlehref next pages :" + titlehref); 
				}
				
			}
			if (indicatorFlag ==0){
				System.out.println("no more pages"); 
			}

		} catch (UnsupportedMimeTypeException err) {
			System.out
					.println("[Error]: HTTP Connection failed. Unsupported mime type.");
		} catch (SocketTimeoutException err) {
			System.out.println("[Error]: Could not connect to " + strURL
					+ ": connection timed out.");
		} catch (HttpStatusException err) {
			System.out.println("[Error]: HTTP error fetchung url " + strURL);
		} catch (UnknownHostException err) {
			System.out.println("[Error]: Cannot connect to unknown host.");
		} catch (IOException err) {
			System.out.println("[Error]: Jsoup can not connect to " + strURL); // "\n\t"
																// err.getMessage());
		}
		return nextPageList;
	}
	
	public boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
	public ArrayList<String > getmasterListCrawl()
	{
		return masterListCrawl; 
		
	}
	public static void  fileUrl(String fAddress, String localFileName, String destinationDir) {
	    OutputStream outStream = null;
	    URLConnection  uCon = null;
	    System.out.println("url" + fAddress);
	    System.out.println("filename" + localFileName); 
	    System.out.println("destionation" + destinationDir);

	    InputStream is = null;
	    try {
	        URL Url;
	        byte[] buf;
	        int ByteRead,ByteWritten=0;
	        Url= new URL(fAddress);
	        outStream = new BufferedOutputStream(new
	        FileOutputStream(destinationDir+"/"+localFileName));

	        uCon = Url.openConnection();
	        is = uCon.getInputStream();
	        buf = new byte[1024];
	        while ((ByteRead = is.read(buf)) != -1) {
	            outStream.write(buf, 0, ByteRead);
	            ByteWritten += ByteRead;
	        }
	    }catch (Exception e) {
	        e.printStackTrace();
	        return; 
	        }
	    finally {
	            try {
	            is.close();
	            outStream.close();
	            }
	            catch (IOException e) {
	        e.printStackTrace();
	            }
	        }
	}

	public String  fileDownload(String fAddress, String destinationDir)
	{    
	    String fileName = fileName(fAddress);
        fileUrl(fAddress,fileName,destinationDir);
        return fileName; 
	}
	
	public String fileName(String fAddress)
	{
		String fileName = fAddress.substring(fAddress.indexOf("s/")+2);
	    System.out.println("File name : " + fileName); 
	    String strippedFileName	= fileName;  
	    if (fileName.contains("/"))
	    		{
	    	strippedFileName = fileName.replaceAll("/","-");
	    		}; 
        return strippedFileName ;
	}
	
	
	
	public void writeToText(String filename, String text)
	{
		Writer output;
		try {
			output = new BufferedWriter(new FileWriter(filename, true));
			output.append(text);
			((BufferedWriter) output).newLine(); 
			output.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error text file"); 
			e.printStackTrace();
		}
	}
	
	
	
}

//crawling method starts crawling drugs alphabetically, masterlist is an array of arrays containing all the extracted fields of the website