package com.eurecom.sentinel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * Handels traning, testing and evaluation of the Sentimentsystems.
 */
public class SentimentanalysisECIR {

	private Set<Tweet> tweetList = new HashSet<Tweet>();
	private String PATH =  "";
	private boolean debug = true;
	//public static final String ANSI_RED = "\u001B[31m";
	//public static final String ANSI_RESET = "\u001B[0m";
	
	/**
	 * Constructor loads all Tweets from a Path.
	 * 
	 * @param path the path to the Tweetfile.
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public SentimentanalysisECIR(String path) throws FileNotFoundException, UnsupportedEncodingException {
		this.PATH = path; //path to train or test file
		loadTweets(path);
	}
	
	/**
	 * Trains systemNRC
	 * 
	 * @param savename optional filename for the arff file
	 */
	public void trainSystem(String savename) throws IOException {
		SentimentSystemNRC nrcSystem = new SentimentSystemNRC(tweetList);
		nrcSystem.train(savename);
	}	
	
	/**
	 * Tests and evaluate a specific system
	 * 
	 * @param trainname optional filename of the arff file
	 */
	public void testSystem(String trainname) throws Exception {
		SentimentSystemNRC nrcSystem = new SentimentSystemNRC(tweetList);
		this.evalModel(nrcSystem.test(trainname));
	}
	
	/**
	 * Parse Tweets from train or test file
	 * 
	 * @param path path to train or test file
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private void loadTweets(String path) throws FileNotFoundException, UnsupportedEncodingException{
		File file = new File("resources/tweets/" + path + ".txt");
		Scanner scanner = new Scanner(file);
		int multiple = 0;
		while (scanner.hasNextLine()) {
			String[] line = scanner.nextLine().split("\t");
			if (line.length == 6){
				if (line[0].equals("NA")){
					if (!storeTweetUni(line[5], line[4], line[1], line[2], line[3])){
						System.out.println("Tweet already in list: " + line[1]);
						multiple++;
					}
				}
				else{
					if (!storeTweetUni(line[5], line[4], line[0], line[2], line[3])){
						System.out.println("Tweet already in list: " + line[0]);
						multiple++;
					}
				}
			}
			else{
			    System.out.println("Wrong format: " + line[0]);
			}
		}
		System.out.println("multiple Tweets: " + multiple);
		scanner.close();
	}
	
	/**
	 * Stores Tweet in tweetList, if not already in there
	 * 
	 * @param tweetString the Tweetstring
	 * @param senti the Tweet Sentiment
	 * @param tweetID the Tweet ID
	 * @param targetBegin target term beginning position
	 * @param targetEnd target term end position
	 * @return true if the Tweet was added to the list, false if the Tweet was already in the list
	 * @throws UnsupportedEncodingException
	 */
	private boolean storeTweetUni(String tweetString, String senti, String tweetID, String targetBegin, String targetEnd) throws UnsupportedEncodingException{
		Tweet tweet = new Tweet(tweetString, senti, tweetID, targetBegin, targetEnd);		
	    if(this.tweetList.add(tweet)){
	    	if (debug) {
	    		System.out.println(tweet.toString());
	    	}
	    	return true;
	    }
	    else {
	    	if (debug) {
	    		System.out.println(tweet.toString());
	    	}
	    	return false;
		}
    }
	
	/**
	 * Evaluate a specific system
	 * 
	 * @param resultMap a map with all classified Tweets
	 * @throws Exception
	 */
	private void evalModel(Map<String, ClassificationResult> resultMap) throws Exception {
		System.out.println("Starting eval Model");
		System.out.println("Tweets: " +  tweetList.size());
		//matrix stores actualSentiment and resultSentiment
		double[][] matrix = new double[3][3];
		Map<String, Integer> classValue = new HashMap<String, Integer>();
		classValue.put("positive", 0);
		classValue.put("neutral", 1);
		classValue.put("negative", 2);
		
		// resultMapToPrint store <tweetIDWithPosition, result sentiment>
		Map<String, Integer> resultMapToPrint = new HashMap<String, Integer>();
		for (Map.Entry<String, ClassificationResult> tweet : resultMap.entrySet()){
			String tweetIDWithTargetPosition = tweet.getKey();
			ClassificationResult senti = tweet.getValue();
			double[] useSentiArray = {0,0,0};
			for (int i = 0; i < 3; i++){
				useSentiArray[i] = (senti.getResultDistribution()[i]);
			}
			
			// useSenti: the result sentiment after the system,defaut value is neutral
			int useSenti = 1; 
			if(useSentiArray[0] > useSentiArray[1] && useSentiArray[0] > useSentiArray[2]){
				useSenti = 0;
			}
			if(useSentiArray[2] > useSentiArray[0] && useSentiArray[2] > useSentiArray[1]){
				useSenti = 2;
			}
			resultMapToPrint.put(tweetIDWithTargetPosition, useSenti);
			if (!tweet.getValue().getTweet().getSentiment().equals("unknwn")){
				Integer actualSenti = classValue.get(tweet.getValue().getTweet().getSentiment());
				matrix[actualSenti][useSenti]++;
			}
		}
		if (matrix.length != 0){
			System.out.println(matrix[0][0] +  " | " + matrix[0][1] + " | " + matrix[0][2]);
			System.out.println(matrix[1][0] +  " | " + matrix[1][1] + " | " + matrix[1][2]);
			System.out.println(matrix[2][0] +  " | " + matrix[2][1] + " | " + matrix[2][2]);
			score(matrix);
		}
		printResultToFile(resultMapToPrint);
	}
	
	/**
	 * Calculates the F1 Score
	 * 
	 * @param matrix the confusion matrix
	 */
	private void score(double[][] matrix){
		double precisionA = matrix[0][0] / (matrix[0][0] + matrix[1][0] + matrix[2][0]);
		double precisionB = matrix[1][1] / (matrix[1][1] + matrix[2][1] + matrix[0][1]);
		double precisionC = matrix[2][2] / (matrix[2][2] + matrix[0][2] + matrix[1][2]);

		double precision = (precisionA + precisionB + precisionC) / 3;
		
		double recallA = matrix[0][0] / (matrix[0][0] + matrix[0][1] + matrix[0][2]);
		double recallB = matrix[1][1] / (matrix[1][1] + matrix[1][2] + matrix[1][0]);
		double recallC = matrix[2][2] / (matrix[2][2] + matrix[2][0] + matrix[2][1]);
		double recall = (recallA + recallB + recallC) / 3;
		
		double f1 = 2 * ((precision * recall) / (precision + recall));
		double f1A = 2 * ((precisionA * recallA) / (precisionA + recallA));
//		double f1B = 2 * ((precisionB * recallB) / (precisionB + recallB));
		double f1C = 2 * ((precisionC * recallC) / (precisionC + recallC));
		
		System.out.println("precision: " + precision);
		System.out.println("recall: " + recall);
//	    System.out.println("precisionPos: " + precisionA);
//	    System.out.println("recallPos: " + recallA);
//	    System.out.println("precisionNeg: " + precisionC);
//	    System.out.println("recallNeg: " + recallC);
		System.out.println("f1: " + f1);
		System.out.println("f1 without neutral: " + (f1A + f1C) / 2);
	}
	
	/**
	 * Prints the result of the sentiment analysis to the result file
	 * errorcount includes bad predict and tweet "Not available"
	 * 
	 * @param resultMapToPrint a map with the results for all Tweets
	 * @throws FileNotFoundException
	 */	
	protected void printResultToFile (Map<String, Integer> resultMapToPrint) throws FileNotFoundException {
		int errorcount = 0;
        Map<Integer, String> classValue = new HashMap<Integer, String>();
        classValue.put(0, "positive");
        classValue.put(1, "neutral");
        classValue.put(2, "negative");
        File file = new File("resources/tweets/" + this.PATH + ".txt");
        PrintStream tweetPrintStream = new PrintStream(new File("output/result.txt"));
        PrintStream tweetPrintStreamError = new PrintStream(new File("output/error_analysis/error.txt"));
        Scanner scanner = new Scanner(file);
        
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("\t");
            String id = line[0] + " " + line[2] + " " + line[3];
            if (line[0].equals("NA")){
            	id = line[1];
            }
            if (line.length == 6 && !line[5].equals("Not Available")){        
                String resultSenti = classValue.get(resultMapToPrint.get(id)); //result sentiment
                String initialSenti = line[4]; // initial sentiment
                if (resultSenti != null){
                    //line[4] = resultSenti;
                    if (!initialSenti.equals(resultSenti)){
                    	errorcount++;
                    	line[4] = "[Error] Inital: " + initialSenti + " Result: " + resultSenti;
                    	if (debug) {
                       		System.out.print(StringUtils.join(line, "\t"));
                    		System.out.println();
                    	}
                    	tweetPrintStreamError.print(StringUtils.join(line, "\t"));
                    	tweetPrintStreamError.println();
                    } else {
                    	line[4] = "[OK] 	Result: " + resultSenti;
                    	if (debug) {
                    		System.out.print(StringUtils.join(line, "\t"));
                    		System.out.println();
                    	}
                    }
                } else {
                    System.out.println("Error while printResultToFile (result sentiment == null): tweetIDWithPosition:" + id);
                    errorcount++;
                    line[2] = "neutral";
                }
            } else if (line.length == 6 && line[5].equals("Not Available")){
                errorcount++;
            } else {
            	System.out.println(line[0]);
            }
            tweetPrintStream.print(StringUtils.join(line, "\t"));
            tweetPrintStream.println();
        }
        scanner.close();
        tweetPrintStream.close();
        tweetPrintStreamError.close();
        if (errorcount != 0) System.out.println("Not Available tweets: " + errorcount);
	}
}
