The crawler performs the below tasks

 i.  Collect tweets listening to Twitter Stream
ii.  Perform real time classification for is the tweet type an Event
	- Loads the specified classifier model
        - Extract features from tweet status object - We define two types of features 
		a. Keyword based features from tweet text
		b. Status features from Twitter Object.
iii.  Location is extracted from tweet text once classified as event. Three ways to identify location in the below order
	a. Tweet geo tags if available
	b. Tweet place if available - Use the center of the bounding box around the place.
	c. Pattern Matching using regular expressions - The string address is decoded to text using google geo encoder.
	d. Stanford NER annotation - The string address is decoded to text using google geo encoder.
iv.  Category label is assigned based on rule based classifier. A category with maximum similarity with event text is chosen.
v.   Push to database by extracting the relevant fields.

Run in Eclipse:
Command line arguments: Path to the keywords file eg.trackterms.txt
Note: The keywords file contains a term per line to be tracked on twitter stream

Configuration:
models: Directory where all the models for event extraction reside.
data:   Logging of tweets with labels into text files that ordered by timestamp.
config:
i. EventCategories.txt - A file with categories and all terms relevant to the cateogory. one category per line.
ii.Features.txt - All keyword features used to match tweet content. one per line

        - 
