import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TweetNER {
	AbstractSequenceClassifier  classifier;
	Pattern address;
	Matcher addrmatch;
	String addressRegex;
	private ArrayList<HashSet> categoryHashsets = new ArrayList<HashSet>();
	private ArrayList<String> categories = new ArrayList<String>();


	public TweetNER(String serializedClassifier){
		addressRegex = "[0-9]+ ([a-z]+ )+(ALLEE|ALLEY|ALLY|ALY|ANEX|ANNEX|ANNX|ANX|ARC|ARCADE|AV|AVE|AVEN|AVENU|AVENUE|AVN|AVNUE|BAYOO|BAYOU|BCH|BEACH|BEND|BND|BLF|BLUF|BLUFF|BLUFFS|BOT|BOTTM|BOTTOM|BTM|BLVD|BOUL|BOULEVARD|BOULV|BR|BRANCH|BRNCH|BRDGE|BRG|BRIDGE|BRK|BROOK|BROOKS|BURG|BURGS|BYP|BYPA|BYPAS|BYPASS|BYPS|CAMP|CMP|CP|CANYN|CANYON|CNYN|CYN|CAPE|CPE|CAUSEWAY|CAUSWAY|CSWY|CEN|CENT|CENTER|CENTR|CENTRE|CNTER|CNTR|CTR|CENTERS|CIR|CIRC|CIRCL|CIRCLE|CRCL|CRCLE|CIRCLES|CLF|CLIFF|CLFS|CLIFFS|CLB|CLUB|COMMON|COR|CORNER|CORNERS|CORS|COURSE|CRSE|COURT|CRT|CT|COURTS|CTS|COVE|CV|COVES|CK|CR|CREEK|CRK|CRECENT|CRES|CRESCENT|CRESENT|CRSCNT|CRSENT|CRSNT|CREST|CROSSING|CRSSING|CRSSNG|XING|CROSSROAD|CURVE|DALE|DL|DAM|DM|DIV|DIVIDE|DV|DVD|DR|DRIV|DRIVE|DRV|DRIVES|EST|ESTATE|ESTATES|ESTS|EXP|EXPR|EXPRESS|EXPRESSWAY|EXPW|EXPY|EXT|EXTENSION|EXTN|EXTNSN|EXTENSIONS|EXTS|FALL|FALLS|FLS|FERRY|FRRY|FRY|FIELD|FLD|FIELDS|FLDS|FLAT|FLT|FLATS|FLTS|FORD|FRD|FORDS|FOREST|FORESTS|FRST|FORG|FORGE|FRG|FORGES|FORK|FRK|FORKS|FRKS|FORT|FRT|FT|FREEWAY|FREEWY|FRWAY|FRWY|FWY|GARDEN|GARDN|GDN|GRDEN|GRDN|GARDENS|GDNS|GRDNS|GATEWAY|GATEWY|GATWAY|GTWAY|GTWY|GLEN|GLN|GLENS|GREEN|GRN|GREENS|GROV|GROVE|GRV|GROVES|HARB|HARBOR|HARBR|HBR|HRBOR|HARBORS|HAVEN|HAVN|HVN|HEIGHT|HEIGHTS|HGTS|HT|HTS|HIGHWAY|HIGHWY|HIWAY|HIWY|HWAY|HWY|HILL|HL|HILLS|HLS|HLLW|HOLLOW|HOLLOWS|HOLW|HOLWS|INLET|INLT|ISLAND|ISLND|ISLANDS|ISLNDS|ISS|ISLE|ISLES|JCT|JCTION|JCTN|JUNCTION|JUNCTN|JUNCTON|JCTNS|JCTS|JUNCTIONS|KEY|KY|KEYS|KYS|KNL|KNOL|KNOLL|KNLS|KNOLLS|LAKE|LK|LAKES|LKS|LAND|LANDING|LNDG|LNDNG|LA|LANE|LANES|LN|LGT|LIGHT|LIGHTS|LF|LOAF|LCK|LOCK|LCKS|LOCKS|LDG|LDGE|LODG|LODGE|LOOP|LOOPS|MALL|MANOR|MNR|MANORS|MNRS|MDW|MEADOW|MDWS|MEADOWS|MEDOWS|MEWS|MILL|ML|MILLS|MLS|MISSION|MISSN|MSN|MSSN|MOTORWAY|MNT|MOUNT|MT|MNTAIN|MNTN|MOUNTAIN|MOUNTIN|MTIN|MTN|MNTNS|MOUNTAINS|NCK|NECK|ORCH|ORCHARD|ORCHRD|OVAL|OVL|OVERPASS|PARK|PK|PRK|PARKS|PARKWAY|PARKWY|PKWAY|PKWY|PKY|PARKWAYS|PKWYS|PASS|PASSAGE|PATH|PATHS|PIKE|PIKES|PINE|PINES|PNES|PL|PLACE|PLAIN|PLN|PLAINES|PLAINS|PLNS|PLAZA|PLZ|PLZA|POINT|PT|POINTS|PTS|PORT|PRT|PORTS|PRTS|PR|PRAIRIE|PRARIE|PRR|RAD|RADIAL|RADIEL|RADL|RAMP|RANCH|RANCHES|RNCH|RNCHS|RAPID|RPD|RAPIDS|RPDS|REST|RST|RDG|RDGE|RIDGE|RDGS|RIDGES|RIV|RIVER|RIVR|RVR|RD|ROAD|RDS|ROADS|ROUTE|ROW|RUE|RUN|SHL|SHOAL|SHLS|SHOALS|SHOAR|SHORE|SHR|SHOARS|SHORES|SHRS|SKYWAY|SPG|SPNG|SPRING|SPRNG|SPGS|SPNGS|SPRINGS|SPRNGS|SPUR|SPURS|SQ|SQR|SQRE|SQU|SQUARE|SQRS|SQUARES|STA|STATION|STATN|STN|STRA|STRAV|STRAVE|STRAVEN|STRAVENUE|STRAVN|STRVN|STRVNUE|STREAM|STREME|STRM|ST|STR|STREET|STRT|STREETS|SMT|SUMIT|SUMITT|SUMMIT|TER|TERR|TERRACE|THROUGHWAY|TRACE|TRACES|TRCE|TRACK|TRACKS|TRAK|TRK|TRKS|TRAFFICWAY|TRFY|TR|TRAIL|TRAILS|TRL|TRLS|TUNEL|TUNL|TUNLS|TUNNEL|TUNNELS|TUNNL|TPK|TPKE|TRNPK|TRPK|TURNPIKE|TURNPK|UNDERPASS|UN|UNION|UNIONS|VALLEY|VALLY|VLLY|VLY|VALLEYS|VLYS|VDCT|VIA|VIADCT|VIADUCT|VIEW|VW|VIEWS|VWS|VILL|VILLAG|VILLAGE|VILLG|VILLIAGE|VLG|VILLAGES|VLGS|VILLE|VL|VIS|VIST|VISTA|VST|VSTA|WALK|WALKS|WALL|WAY|WY|WAYS|WELL|WELLS|WLS)((,|\\.) [a-z]+(,[a-z]+)?)?";
		//set/load classifier
		classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
		//compile regex for future use
		address = Pattern.compile(addressRegex,Pattern.CASE_INSENSITIVE);

		String categoryFile = "config/EventCategories.txt";
		try {
			fillSets(categoryFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void fillSets(String categoryFile) throws IOException{
		FileReader file = new FileReader(categoryFile);
		BufferedReader br = new BufferedReader(file);
		String cat;
		while ((cat = br.readLine())!= null){
			String[] temp = cat.split(":");
			if(temp[0] == null)
				System.out.println("bad category file input");
			else
			{
				categories.add(temp[0]);
				HashSet<String> catHash = new HashSet<String>();
				String[] words = temp[1].split(";");
				for(String word : words){
					catHash.add(word);
				}
				categoryHashsets.add(catHash);
			}	
		}
		br.close();
	}

	public String categorize(String s)
	{
		String t = s.toUpperCase();
		int[] results = new int[categoryHashsets.size()];
		for(int i = 0; i < results.length; i++)
			results[i] = 0 ;

		for(int i = 0; i < categoryHashsets.size(); i++){
			for(Object word : categoryHashsets.get(i)){
				if(StringUtils.contains(t, ((String) word).toUpperCase())){
					results[i]++;
				}

			}
		}
		
		// Get maximum match starting from beginning
		int max = 0; int index = results.length;
		for(int i=0;i<results.length;i++)
		{
			if(max < results[i])
			{
				max = results[i];
				index = i;
			}
		}
		
		if(index == results.length)
			return "Other Events";
		else
			return this.categories.get(index);
	}

	/* Return string annotated with Organization,People,Location.
	 * See commented out example(bottom) for annotating a file at once instead*/
	public String annotate(String tweet) throws IOException {
		//return classifier.classifyToString(tweet)					//this tags all words
		return classifier.classifyWithInlineXML(tweet);             //this is for inline xml
		//return classifier.classifyToString(tweet, "xml", true);   //this is for xml
	}

	/* Return locations from an annotated string (based on inline xml)*/
	public static List<String> getLocations(String annotatedTweet){
		String [] pieces = annotatedTweet.split("<LOCATION>|</LOCATION>");
		List<String> ret = new LinkedList();
		//all odd numbered pieces are between location tags
		for (int i = 1; i < pieces.length; i+=2){
			ret.add(pieces[i]);
		}
		return ret;
	}

	public String getAddress(String tweet){
		addrmatch = address.matcher(tweet);
		if(addrmatch.lookingAt())
			return addrmatch.group();
		else return null;
	}
}