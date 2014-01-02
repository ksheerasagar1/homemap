import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Count {
	public static String[] stopwords = {"a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost",
		"alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "amoungst", "amount", "an", "and",
		"another", "any", "anyhow", "anyone", "anything", "anyway", "anywhere", "are", "around", "as", "at", "back", "be", "became",
		"because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides",
		"between", "beyond", "bill", "both", "bottom", "but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt",
		"cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven", "else",
		"elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few",
		"fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from",
		"front", "full", "further", "get", "give", "go", "had", "has", "hasnt",
		"have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself",
		"him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into",
		"is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many",
		"may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must",
		"my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none",
		"noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto",
		"or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own", "part", "per", "perhaps",
		"please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she",
		"should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something",
		"sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their",
		"them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon",
		"these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru",
		"thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until",
		"up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever",
		"where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while",
		"whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet",
		"you", "your", "yours", "yourself", "yourselves", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1.", "2.", "3.", "4.", "5.", "6.", "11",
		"7.", "8.", "9.", "12", "13", "14", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
		"terms", "CONDITIONS", "conditions", "values", "interested.", "care", "sure", ".", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "{", "}", "[", "]", ":", ";", ",", "<", ".", ">", "/", "?", "_", "-", "+", "=",
		"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
		"contact", "grounds", "buyers", "tried", "said,", "plan", "value", "principle.", "forces", "sent:", "is,", "was", "like",
		"discussion", "tmus", "diffrent.", "layout", "area.", "thanks", "thankyou", "hello", "bye", "rise", "fell", "fall", "psqft.", "http://", "km", "miles"};

	private static String readFile( String file ) throws IOException {
		BufferedReader reader = new BufferedReader( new FileReader (file));
		String         line = null;
		StringBuilder  stringBuilder = new StringBuilder();
		String         ls = System.getProperty("line.separator");

		while( ( line = reader.readLine() ) != null ) {
			stringBuilder.append( line );
			stringBuilder.append( ls );
		}

		return stringBuilder.toString();
	}

	private static Map sortByComparator(Map unsortMap) {

		List list = new LinkedList(unsortMap.entrySet());

		// sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue())
						.compareTo(((Map.Entry) (o1)).getValue());
			}
		});

		// put sorted list into map again
		//LinkedHashMap make sure order in which keys were inserted
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	public static void getWordsWithCount() throws IOException
	{

		Map<String,Integer> wordsWithCount = new HashMap<String, Integer>();
		String sentances = readFile("tweets.txt");
		LinkedList<String> returnedwords = new LinkedList<String>();
		String[] words = sentances.split("\\s+");
		Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");

		for (String word : words)
		{
			Matcher matcher = pattern.matcher(word); 
			if(word.length() < 3)
				continue;
			if(matcher.find() && matcher.start() == 0 && matcher.end() == word.length()-1)
			{
				if(wordsWithCount.containsKey(word))
				{
					wordsWithCount.put(word, wordsWithCount.get(word)+1);
				}
				else
				{
					wordsWithCount.put(word, 1);
				}
			}
		}

		Map<String,Integer> wordsWithCountSorted = sortByComparator(wordsWithCount);
		Iterator it = wordsWithCountSorted.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			if(Integer.parseInt(pairs.getValue().toString())< 2)
				continue;

			if(!Arrays.asList(stopwords).contains((String) pairs.getKey()))
			{
				returnedwords.add((String) pairs.getKey());
			}
		}
        File file = new File("featuresmore.txt");
        BufferedWriter output = new BufferedWriter(new FileWriter(file));
		for (String word : returnedwords){
			output.write(word);
			output.newLine();
		}
		output.close();
	}
	public static void main(String[] args) throws IOException {
		getWordsWithCount();
	}
}
