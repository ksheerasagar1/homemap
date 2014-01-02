import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;

import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.User;

public class RealTimeFeatures {

	public ArrayList<String> statusfeatures = new ArrayList<String>();
	private ArrayList<String> textfeatures = new ArrayList<String>();


	private ArrayList<ArrayList<String>> textfeaturesets = new ArrayList<ArrayList<String>>();


	public RealTimeFeatures(String featureFileName){
		try {
			loadKeywordFeatureFile(featureFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Add all status features names
		statusfeatures.add("#event");
		statusfeatures.add("location");
		statusfeatures.add("time");
		statusfeatures.add("screenNameEvent");
		statusfeatures.add("screenNameETL");
		statusfeatures.add("screenNameBidTickets");
		statusfeatures.add("urlInfo");
		/*statusfeatures.add("@");
		statusfeatures.add("retweet_count");
		statusfeatures.add("in_reply_to");*/
	}

	private void loadKeywordFeatureFile(String fn) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fn));
		String line;
		// reading in features into featureNames and featureSets
		while ((line = br.readLine()) != null) {
			if (line.length() > 0) {
				// System.out.println(line);
				String[] splits = StringUtils.split(line, '|');
				textfeatures.add(splits[0]);
				String[] set = StringUtils.split(splits[1], ',');
				ArrayList<String> list = new ArrayList<String>();
				for (int i = 0; i < set.length; i++) {
					list.add(set[i].toLowerCase());
				}
				textfeaturesets.add(list);
			}
		}
		br.close();
	}

	public double[] convertTweetToFeatures(String line) {
		double frequency[] = new double[textfeaturesets.size()];
		int j, k;
		line = line.toLowerCase();
		for (j = 0; j < textfeaturesets.size(); j++) {
			for (k = 0; k < textfeaturesets.get(j).size(); k++) {
				frequency[j] += StringUtils.countMatches(line, (textfeaturesets
						.get(j)).get(k));
			}
		}
		return frequency;
	}

	public double[] getFeaturesFromStatus(Status s){
		// Get features from status object
		User u = s.getUser();
		HashtagEntity[] hashTags = s.getHashtagEntities();
		double[] freq = new double[statusfeatures.size()];
		
		int j=0;
		// Hash tag for #event
		for(int i=0; i < hashTags.length; i++) {
			if(hashTags[i].getText().equals("event")) {
				freq[j]++;
				break;
			}
		}
		j++;
		// Check the presence of location information
		if(Utilities.containsLocation(s.getText()) == true)
			freq[j]++;
		j++;
		// Check for the presence of time or date information in tweet text
		if(Utilities.containsTimeOrDate(s.getText()) == true)
			freq[j]++;			
		j++;
		// ScreenName with keyword event
		if(u.getScreenName().toLowerCase().contains("event") == true)
			freq[j]++;			
		j++;
		// ScreenName with keyword ETL
		if(u.getScreenName().contains("ETL") == true)
			freq[j]++;			
		j++;
		// ScreenName with keyword event
		if(u.getScreenName().contains("BidTickets") == true)
			freq[j]++;			
		j++;
		// if url is present or not
		if(s.getURLEntities().length > 0)
			freq[j]++;
		return freq;
	}
	


	public double[] getAllFeatures(Status s){
		double[] freq = new double[statusfeatures.size() + textfeatures.size()];
		double[] sfreq = getFeaturesFromStatus(s);
		double[] tfreq = convertTweetToFeatures(s.getText());
		for(int i = 0; i < sfreq.length; i++)
			freq[i] = sfreq[i];
		for(int i = sfreq.length; i < freq.length; i++)
			freq[i] = tfreq[i - sfreq.length];
		return freq;
	}


	public ArrayList<String> getFeatureNames() {
		ArrayList<String> ret = new ArrayList<String>();
		ArrayList<String> sfns = new ArrayList<String>(statusfeatures);
		ArrayList<String> fns = new ArrayList<String>(textfeatures);
		ret.addAll(sfns);
		ret.addAll(fns);
		ret.add("label");
		return ret;
	}
}
