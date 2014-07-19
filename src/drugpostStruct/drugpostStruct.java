package drugpostStruct;

import java.sql.Date;

public class drugpostStruct {
	public String url;
	public int replyid;
	public int parentpostid;
	public String source;
	public String userid;
	public String posttime;
	public String title;
	public String body;
	public String drugname;
	public String disorder;
	public String tags;
	public int rating;
	public int likes;
	
	public drugpostStruct()
	{
		url = null;
		replyid = 0; 
		parentpostid =0; 
		source = "http://www.drugs.com"; 
		userid = null; 
		posttime = null;
		title = null; 
		body = null; 
		drugname = null; 
		disorder = null;
		tags = null;
		rating = 0; 
		likes = 0; 
	}
}
