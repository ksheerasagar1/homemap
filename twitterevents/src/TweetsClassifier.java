import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class TweetsClassifier {
	private Classifier cls;
	private double negProb;

	public static Classifier loadModel(String modelName) throws Exception {
		return (Classifier) weka.core.SerializationHelper.read(modelName
				+ ".model");
	}

	// Created for read-time classifying
	public void init(String modelName) {
		try {
			if (modelName.equals("nb")) {
				cls = loadModel("models/naivebayes_2100");
			} 
			else {
				System.out.println("[ERROR] Please choose the right model!");
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	// Set up instance attributes
	private FastVector createdFastVector(ArrayList<String> featureNames) {
		FastVector atts = new FastVector();
		for (int i = 0; i < featureNames.size() ; i++) {
			atts.addElement(new Attribute(featureNames.get(i)));
		}
		return atts;
	}

	// Set up instance values
	private Instances createInstance(double[] tweetData,
			ArrayList<String> featureNames) {
		FastVector fv = createdFastVector(featureNames);
		Instances data = new Instances("isEvent", fv, 0);
		data.setClassIndex(data.numAttributes() - 1);
		data.add(new Instance(1.0, tweetData));		
		return data;
	}

	// Created for read-time classifying
	public boolean isEvent(double[] tweetData, ArrayList<String> featureNames)
			throws Exception {
		Instances i = createInstance(tweetData, featureNames);
		negProb = cls.distributionForInstance(i.firstInstance())[0];
		if (cls.classifyInstance(i.firstInstance()) == 1.0 || negProb <= 0.4) {
			return true;
		} else
			return false;
	}
}