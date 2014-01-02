import java.sql.*;
import com.google.code.geocoder.model.LatLng;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.Place;
import twitter4j.Status;
import twitter4j.URLEntity;

public class DBHandler {
	private static String user = "root";
	private static String pw = "root";
	private static String dbUrl = "jdbc:mysql://127.0.0.1/test";
	private static String dbClass = "com.mysql.jdbc.Driver";


	private static String tweetdbInsert = "replace into tweets (userid,text,created,retweetcount, urls, hashtags, lat, lon, category, tweetid) VALUES(?,?,?,?,?,?,?,?,?,?)";
	int tweetCommitCount;
	Connection con;

	DBHandler(){
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(dbUrl, user, pw);
			con.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public double[] getGPS(String loc, boolean filter) {
		double[] a = {Double.MAX_VALUE,Double.MAX_VALUE,0.0};
		if(loc != null)
		{
			LatLng locCoordinates = Utilities.getCoordinatesFromAddress(loc);
			a[0] = Double.parseDouble(locCoordinates.getLat().toPlainString());
			a[1] = Double.parseDouble(locCoordinates.getLng().toPlainString());			
		}
		return a;
	}	
	
	public String handleURL(URLEntity[] urls){
		String urlStr = "";
		if (urls != null) {			
			for (URLEntity ue : urls) {
				urlStr = urlStr + " " + ue;
			}			
		}
		return urlStr;
	}

	public String handleHashTags(HashtagEntity[] htes)
	{
		String hts="";
		if(htes!=null){
			for(HashtagEntity hte:htes)
				hts=hts+" "+hte.getText();
		}
		return hts;
	}

	public void addTweet(Status s, String NERloc, String regLoc, String category)
	{
		//url string
		URLEntity[] urls = s.getURLEntities();
		String urlStr = handleURL(urls);
		//hashtag string
		HashtagEntity[] htes=s.getHashtagEntities();
		String hts = handleHashTags(htes);
		
		double lat=Double.MAX_VALUE,lon=Double.MAX_VALUE;
		Place p;
		if(s.getGeoLocation() != null)
		{
			lat = s.getGeoLocation().getLatitude();
			lon = s.getGeoLocation().getLongitude();
		}
		else if((p = s.getPlace()) != null)
		{
			if (p.getBoundingBoxCoordinates()!=null) 
			{
				GeoLocation[][] box = p.getBoundingBoxCoordinates();    
				for (int i = 0;i < box.length; i++) {
					for (int j = 0;j < box[0].length; j++) {
						lat += box[i][j].getLatitude();
						lon += box[i][j].getLongitude();
					}       
				}
				lat = lat/4;
				lon = lon/4;

			}
			else if (p.getGeometryCoordinates() != null) {
				GeoLocation[][] box = p.getGeometryCoordinates();       
				for (int i = 0;i < box.length; i++) {
					for (int j = 0;j < box[0].length; j++) {
						lat += box[i][j].getLatitude();
						lon += box[i][j].getLongitude();
					}       
				}
				lat = lat / 4;
				lon = lon /4;
			}
		}
		else if(NERloc != null)
		{
			double[] loc = getGPS(NERloc,true);
			lat = loc[0];
			lon = loc[1];
		}
		else if(regLoc != null)
		{
			double [] loc = getGPS(regLoc,true);
			lat = loc[0];
			lon = loc[1];
		}
		try {
			tweetCommitCount++;
			PreparedStatement pstmt = con.prepareStatement(tweetdbInsert);
			pstmt.setLong(1, s.getUser().getId());
			pstmt.setString(2, s.getText());
			pstmt.setLong(3, s.getCreatedAt().getTime()/1000);
			pstmt.setLong(4, s.getRetweetCount());
			pstmt.setString(5, urlStr);
			pstmt.setString(6,hts);
			pstmt.setDouble(7, lat);
			pstmt.setDouble(8, lon);
			pstmt.setString(9, category);
			pstmt.setLong(10, s.getId());
			pstmt.executeUpdate();
			pstmt.close();
			if(tweetCommitCount > 1)
			{
				tweetCommitCount = 0;
				con.commit();
			}
		} catch (SQLException e) {
			System.out.println("tweets database error");
			System.out.println(Long.toString(s.getId()));
			e.printStackTrace();
		}
	}
}