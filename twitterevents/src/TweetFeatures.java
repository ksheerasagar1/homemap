
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import au.com.bytecode.opencsv.CSVWriter;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import twitter4j.HashtagEntity;

/**
 * Extract and build features from the tweeted text.
 * 
 *
 */
public class TweetFeatures {

        private Tweet tweet;
        Set<String> eventKeywords;
        
        // Modeling only presence and absence of features
        // Like location, date, time, etc.        
        private LinkedHashMap<String, Boolean> featuresMap;
        
        public TweetFeatures(Tweet tweet) throws IOException {
                this.tweet = tweet;
                eventKeywords = new HashSet<String>();
                
                // Pick up the list of features to look for from the file 'FeaturesList.txt'
                BufferedReader br = new BufferedReader(new FileReader("featuresList.txt"));
                String line;
                
                while((line = br.readLine()) != null) {
                        // Accept the whole line as a new feature. Note that each line usually 
                        // contains one word or an average maximum of three words.
                        eventKeywords.add(line);
                }
                
                br.close();
                initFeaturesMap();
        }
        
        public void initFeaturesMap() {
                featuresMap = new LinkedHashMap<String, Boolean>();
                
                featuresMap.put("#event", false);
                
                // Update hashtags info to featuresMap for predefined set of features
                HashtagEntity[] hashTags = tweet.getHashTags();
                for(int i=0; i < hashTags.length; i++) {
                        if(hashTags[i].getText().equals("event")) {
                                featuresMap.put("#event", true);
                                break;
                        }
                }
                
                // Initialize the features from eventKeywords
                Iterator<String> it = eventKeywords.iterator();
                while(it.hasNext()) {
                        featuresMap.put(it.next(), false);
                }

                updateFeatures();
        }
        
        public void updateFeatures() {
                String tweetText = StopWords.removeStopWords(tweet.getTweetedText());
                
                // Check the presence of location information
                /*featuresMap.put("location", Utilities.containsLocation(tweet.getTweetedText()));
                
                // Check the presence of time or date information
                featuresMap.put("time", Utilities.containsTimeOrDate(tweetText));
                
                // ScreenName with keyword event
                featuresMap.put("screenNameEvent", tweet.getStatusObject().getUser()
                        .getScreenName().toLowerCase().contains("event"));
                
                featuresMap.put("screenNameETL", tweet.getStatusObject().getUser()
                                .getScreenName().contains("ETL"));
                
                featuresMap.put("screenNameBidTickets", tweet.getStatusObject().getUser()
                                .getScreenName().contains("BidTickets"));
                
                // URL info in tweet text
                featuresMap.put("urlInfo", (tweet.getStatusObject().getURLEntities().length > 0));*/
                
                // Keywords presence
                String[] list = tweetText.toLowerCase().split("\\s+");
                for(int i=0; i < list.length; i++) {
                        if(eventKeywords.contains(list[i]))
                                featuresMap.put(list[i], true);
                }
        }
        
        public void flushFeaturesToFile(String csvFile, boolean append, String label)
                throws IOException {
                
                // If file doesn't exist then write out the first line with feature names
                if(!(new File(csvFile).exists())) {
                        CSVWriter w = new CSVWriter(new FileWriter(csvFile, append));
                        String[] title = ArrayUtils.addAll(featuresMap.keySet()
                                .toArray(new String[0]), "label");
                        w.writeNext(title);
                        w.close();
                }
                
                // open the csv file in append mode
                CSVWriter writer = new CSVWriter(new FileWriter(csvFile, append));
                StringBuilder sb = new StringBuilder();
                
                for(Map.Entry<String, Boolean> entry: featuresMap.entrySet()) {
                        sb.append(entry.getValue() + "#@XF");
                }
                
                sb.append(label);
                
                String[] data = sb.toString().split("#@XF");
                writer.writeNext(data);
                writer.close();
        }
        
        public boolean getPresence(String feature) {
                if(!featuresMap.containsKey(feature))
                        featuresMap.put(feature, false);
                
                return featuresMap.get(feature);
        }
        
        public void updatePresence(String feature) {
                featuresMap.put(feature, true);
        }
        
        public HashMap<String, Boolean> getFeaturesMap() {
                return featuresMap;
        }
}
