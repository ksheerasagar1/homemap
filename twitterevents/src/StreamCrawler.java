
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import twitter4j.FilterQuery;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class StreamCrawler implements Runnable {
	private TwitterStream twitterStream;
	private String track[];
	private double[][] geo;
	private boolean useGeo;
	
	public StreamCrawler(String[] track, double[][] geo, boolean useGeo)
			throws IOException {
		StatusListener listener = new Listener();
		this.track = track;
		this.geo = geo;
		this.useGeo = useGeo;
		twitterStream = new TwitterStreamFactory().getInstance();
		twitterStream.addListener(listener);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		FilterQuery query = new FilterQuery();
		// if you want to track geo, add this line/
		if (useGeo)
			query.locations(geo);
		// if you want to track keywords, add this line.
		else
			query.track(track);
		twitterStream.filter(query);
		// if you do not to focus on geo or keywords
		// use twitterStream.sample(); instead of twitterStream.filter(query);
	}

	public static void main(String[] args) throws IOException {

		if (args.length == 0) {
			System.out.println("Please enter filename or chicago");
		}
		
		//String loc = "chicago";
		
		String[] track;

		double chilat = 41.878114;
		double chilong = -87.629798;
		double[][] geo = { { chilong - .5, chilat - .5 },
				{ chilong + .5, chilat + .5 } };

		if (args[0].equals("chicago")) {
			StreamCrawler sc = new StreamCrawler(null, geo, true);
			sc.run();
		} else {
			FileReader file = new FileReader(args[0]);
			BufferedReader br = new BufferedReader(file);
			ArrayList<String> kws = new ArrayList<String>();
			String kw;
			int count = 0;
			while ((kw = br.readLine()) != null) {
				if (kw.length() > 0) {
					kws.add(kw);
					count++;
				}
			}
			br.close();
			track = new String[count];
			int index = 0;
			for (String word : kws) {
				track[index] = word;
				index++;
			}
			StreamCrawler sc = new StreamCrawler(track, geo, false);
			sc.run();
		}
	}
}