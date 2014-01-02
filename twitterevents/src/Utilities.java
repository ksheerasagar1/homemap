import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import edu.stanford.nlp.ling.CoreLabel;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.LatLng;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;

/**
 * This class encloses all the utility methods to extract useful fields from 
 * twitter text or tweets
 * 
 *
 */
public class Utilities {
        
        public static LatLng getCoordinatesFromAddress(String address) {
                final Geocoder geocoder = new Geocoder();
                GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(address)
                        .setLanguage("en").getGeocoderRequest();
                GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
                
                LatLng locCoordinates = geocoderResponse.getResults().get(0).getGeometry().getLocation();
                
                return locCoordinates;
        }
        
        public static String getAddressFromCoordinates(LatLng location) {
                final Geocoder geocoder = new Geocoder();
                GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setLocation(location)
                        .setLanguage("en").getGeocoderRequest();
                GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
                
                String address = geocoderResponse.getResults().get(0).getFormattedAddress();
                
                return address;
        }
        
        public static boolean containsLocation(String text) {
                String threeClassSerializedClassifier = "lib/stanford-ner/classifiers/english.all.3class.distsim.crf.ser.gz";
                AbstractSequenceClassifier<CoreLabel> threeClassClassifier = CRFClassifier
                                .getClassifierNoExceptions(threeClassSerializedClassifier);
                
                String classifiedString = threeClassClassifier.classifyWithInlineXML(text);
                NERParsedObject po = new NERParsedObject();
                po.parseThreeClassifiedString(classifiedString);
                
                ArrayList<String> locations = po.getLocations();
                Set<String> locationKeywords = new HashSet<String>();
                locationKeywords.addAll(Arrays.asList(new String[] {"place", "at", "@", "house", "home"}));
                
                if(locations != null && locations.size() > 0)
                        return true;
                
                text.toLowerCase();
                String[] list = text.split("\\s+");
                for(int i=0; i < list.length; i++) {
                        if(locationKeywords.contains(list[i]))
                                return true;
                }
                
                return false;
        }
        
        public static boolean containsTimeOrDate(String text) {
                String sevenClassSerializedClassifier = "lib/stanford-ner/classifiers/english.muc.7class.distsim.crf.ser.gz";
                AbstractSequenceClassifier<CoreLabel> sevenClassClassifier = CRFClassifier.getClassifierNoExceptions(sevenClassSerializedClassifier);
                
                String classifiedString = sevenClassClassifier.classifyWithInlineXML(text);
                NERParsedObject po = new NERParsedObject();
                po.parseSevenClassifiedString(classifiedString);
                
                List<String> date = po.getDateList();
                if(date != null && date.size() > 0)
                        return true;
                
                List<String> time = po.getTimeList();
                if(time != null && time.size() > 0)
                        return true;
                
                Set<String> timeKeywords = new HashSet<String>();
                timeKeywords.addAll(Arrays.asList(new String[] {"today", "tomorrow", "monday", "tuesday", 
                        "wednesday", "thursday", "friday", "saturday", "sunday", "evening", "morning", 
                        "weekend", "afternoon"}));
                
                text.toLowerCase();
                String[] list = text.split("\\s+");
                for(int i=0; i < list.length; i++) {
                        if(timeKeywords.contains(list[i]))
                                return true;
                }
                
                return false;
        }
        
        
}
