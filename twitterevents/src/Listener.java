import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class Listener implements StatusListener {
	private static final String DATE_FORMAT_NOW = "yyyy-MM-dd";
	BufferedWriter out = null;
	RealTimeFeatures rtf;
	DBHandler dbw;
	TweetNER ner;
	
	// Constructor
	public Listener(){		
		rtf = new RealTimeFeatures("config/features.txt");
		dbw = new DBHandler();
		ner = new TweetNER("lib/stanford-ner/classifiers/english.all.3class.distsim.crf.ser.gz");
	}

	// Get current time, it will be used as fileName to store tweets in that
	// day.
	private static String getTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}
	
	private void writeTextAndData(Status status,Boolean isEvent,double[] sfts){
		try {
			String filename = "data/" + getTime();
			File file = new File(filename);
			 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			
			BufferedWriter labelout = new BufferedWriter(new FileWriter(file.getAbsoluteFile(),true));
			labelout.write(Long.toString(status.getId()) + ":");
			if(isEvent)
				labelout.write("1");
			else
				labelout.write("0");
			labelout.write(":" + status.getText().replace("\n", "") + "\n");
			for(int i = 0; i < sfts.length; i++){
				labelout.write(Double.toString(sfts[i]) + ",");
			}
			labelout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	// Handle a status, it will be called when a status comes.
	public void onStatus(Status status) {
		EventDetection ed = new EventDetection();
		boolean isEvent = ed.classify(status,rtf,"nb");
		writeTextAndData(status,isEvent,rtf.getFeaturesFromStatus(status));
		if(isEvent)
		{
			System.out.println("Event Found");
			String regaddr = ner.getAddress(status.getText());
			String annotated = null;
			try {
				annotated = ner.annotate(status.getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
			List<String> locs;
			String nerloc = null;
			if( (locs = TweetNER.getLocations(annotated)).size() > 0)
			{
				nerloc = locs.get(0);
				nerloc = nerloc.toLowerCase();
			}
			
			if(regaddr != null)
				regaddr=regaddr.toLowerCase();
			
			String category = ner.categorize(status.getText());
			dbw.addTweet(status, nerloc,regaddr,category);
		}
	}

	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
	}

	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
	}

	public void onException(Exception ex) {
		ex.printStackTrace();
	}

	public void onScrubGeo(int arg0, long arg1) {

	}

	@Override
	public void onScrubGeo(long arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStallWarning(StallWarning arg0) {
		// TODO Auto-generated method stub
		
	}
};