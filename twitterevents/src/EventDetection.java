import java.text.SimpleDateFormat;
import java.util.Calendar;
import twitter4j.Status;


public class EventDetection {
	private static final String DATE_FORMAT_NOW = "yyyy-MM-dd";

	// Get current time, it will be used as fileName to store tweets in that
	// day.
	 static String getTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	// real-time classifying for social classifier
	public boolean classify(Status s, RealTimeFeatures rtf, String model) {
		TweetsClassifier tc = new TweetsClassifier();
		tc.init(model);
		double[] tweetData = null;
		boolean result;
		tweetData = rtf.getAllFeatures(s);//plus tag?
		try {
			
			System.out.println("come here");
			result = tc.isEvent(tweetData, rtf.getFeatureNames());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}


	public static void main(String[] args) {
		//RealTimeFeatures rtf = new RealTimeFeatures("config/features.txt");
		//EventDetection cd = new EventDetection();
//		boolean b = cd.classify("rt @music_producer_: @youngdonny 16 fire beats http://limelinx.com/files/a427dbc8e9915c10e265166722aa1a06f",0,rtf);
//		if(b)
//			System.out.println("true");
//		else
//			System.out.println("false");
//		cd
//				.classify(
//						"severe thunderstorm warning for mississippi county in ar until 1:30pm cdt. turn to local radio/tv for updates #arwx",
//						181818181);
	}
}
