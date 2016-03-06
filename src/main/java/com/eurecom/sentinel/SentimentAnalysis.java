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
 * Handels traning, testing and evaluation of the SentiNEL system.
 * 
 * @author SentiNEL, Webis
 * Codes based on Webis system 
 * SentiNEL modify the printResultToFile method
 * SentiNEL add checkPositionFormat method to get rid of the tweet whose target
 * term's end position is bigger than the tweet total length
 */
public class SentimentAnalysis {

	private Set<Tweet> tweetList = new HashSet<Tweet>();
	private String PATH = "";
	private boolean debug = false;

	// public static final String ANSI_RED = "\u001B[31m";
	// public static final String ANSI_RESET = "\u001B[0m";

	/**
	 * Constructor loads all Tweets from a Path.
	 * 
	 * @param path
	 *            the path to the Tweetfile.
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public SentimentAnalysis(String path) throws FileNotFoundException,
			UnsupportedEncodingException {
		this.PATH = path; // path to train or test file
		loadTweets(path);
	}

	/**
	 * Trains system
	 * 
	 * @param savename
	 *            optional filename for the arff file
	 */
	public void trainSystem(String savename) throws IOException {
		SentimentSystemSentinel sentinelSystem = new SentimentSystemSentinel(tweetList);
		sentinelSystem.train(savename);
	}

	/**
	 * Tests and evaluate a specific system
	 * 
	 * @param trainname
	 *            optional filename of the arff file
	 */
	public void testSystem(String trainname) throws Exception {
		SentimentSystemSentinel sentinelSystem = new SentimentSystemSentinel(tweetList);
		this.evalModel(sentinelSystem.test(trainname));
	}

	/**
	 * Parse Tweets from train or test file
	 * 
	 * @param path
	 *            path to train or test file
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private void loadTweets(String path) throws FileNotFoundException,
			UnsupportedEncodingException {
		File file = new File("resources/tweets/" + path + ".txt");
		Scanner scanner = new Scanner(file);
		int multiple = 0;
		int count = 0;
		while (scanner.hasNextLine()) {
			String[] line = scanner.nextLine().split("\t");
			if (line.length == 6 && checkPositionFormat(line[5], line[3])) {
				if (line[5].equals("Not Available")) {
					System.out.println("Tweet not available: " + line[0]);
				}
				if (line[0].equals("NA")) {
					if (!storeTweetUni(line[5], line[4], line[1], line[2],
							line[3])) {
						System.out.println("Tweet already in list: " + line[1]);
						multiple++;
					} else {
						count++;
					}
				} else {
					if (!storeTweetUni(line[5], line[4], line[0], line[2],
							line[3])) {
						System.out.println("Tweet already in list: " + line[0]);
						multiple++;
					} else {
						count++;
					}
				}
			} else {
				// not formal tweet including : target term out of posiont, less than 6 field
				System.out.println("Wrong format: " + line[0] + " length: "
						+ line.length);
			}
		}
		System.out.println("duplicated Tweets: " + multiple);
		System.out.println("Tweets: " + count);
		scanner.close();
	}

	private boolean checkPositionFormat(String string, String position) {
		String[] words = string.split("\\s+");
		if (Integer.parseInt(position) >= words.length) {
			return false;
		}
		return true;
	}

	/**
	 * Stores Tweet in tweetList, if not already in there
	 * 
	 * @param tweetString
	 *            the Tweetstring, rawTweet
	 * @param senti
	 *            the Tweet Sentiment
	 * @param tweetID
	 *            the Tweet ID
	 * @param targetBegin
	 *            target term beginning position
	 * @param targetEnd
	 *            target term end position
	 * @return true if the Tweet was added to the list, false if the Tweet was
	 *         already in the list
	 * @throws UnsupportedEncodingException
	 */
	private boolean storeTweetUni(String tweetString, String senti,
			String tweetID, String targetBegin, String targetEnd)
			throws UnsupportedEncodingException {
		Tweet tweet = new Tweet(tweetString, senti, tweetID, targetBegin,
				targetEnd);
		if (this.tweetList.add(tweet)) {
			if (debug) {
				System.out.println(tweet.toString());
			}
			return true;
		} else {
			if (debug) {
				System.out.println(tweet.toString());
			}
			return false;
		}
	}

	/**
	 * Evaluate a specific system
	 * 
	 * @param resultMap
	 *            a map with all classified Tweets
	 * @throws Exception
	 */
	private void evalModel(Map<String, ClassificationResult> resultMap)
			throws Exception {
		// System.out.println("Starting eval Model");
		// System.out.println("Tweets: " + tweetList.size());
		// matrix stores actualSentiment and resultSentiment
		double[][] matrix = new double[3][3];
		Map<String, Integer> classValue = new HashMap<String, Integer>();
		classValue.put("positive", 0);
		classValue.put("neutral", 1);
		classValue.put("negative", 2);

		// resultMapToPrint store <tweetIDWithPosition, result sentiment>
		Map<String, Integer> resultMapToPrint = new HashMap<String, Integer>();
		for (Map.Entry<String, ClassificationResult> tweet : resultMap
				.entrySet()) {
			String tweetIDWithTargetPosition = tweet.getKey();
			ClassificationResult senti = tweet.getValue();
			double[] useSentiArray = { 0, 0, 0 };
			for (int i = 0; i < 3; i++) {
				useSentiArray[i] = (senti.getResultDistribution()[i]);
			}

			// useSenti: the result sentiment after the system,defaut value is
			// neutral
			int useSenti = 1;
			if (useSentiArray[0] > useSentiArray[1]
					&& useSentiArray[0] > useSentiArray[2]) {
				useSenti = 0;
			}
			if (useSentiArray[2] > useSentiArray[0]
					&& useSentiArray[2] > useSentiArray[1]) {
				useSenti = 2;
			}
			resultMapToPrint.put(tweetIDWithTargetPosition, useSenti);
			if (!tweet.getValue().getTweet().getSentiment().equals("unknwn")) {
				Integer actualSenti = classValue.get(tweet.getValue()
						.getTweet().getSentiment());
				matrix[actualSenti][useSenti]++;
			}
		}
		if (matrix.length != 0) {
			System.out.println(matrix[0][0] + " | " + matrix[0][1] + " | "
					+ matrix[0][2]);
			System.out.println(matrix[1][0] + " | " + matrix[1][1] + " | "
					+ matrix[1][2]);
			System.out.println(matrix[2][0] + " | " + matrix[2][1] + " | "
					+ matrix[2][2]);
			score(matrix);
		}
		printResultToFile(resultMapToPrint);
	}

	/**
	 * Calculates the F1 Score
	 * 
	 * @param matrix
	 *            the confusion matrix
	 */
	private void score(double[][] matrix) {
		double precisionA = matrix[0][0]
				/ (matrix[0][0] + matrix[1][0] + matrix[2][0]);
		double precisionB = matrix[1][1]
				/ (matrix[1][1] + matrix[2][1] + matrix[0][1]);
		double precisionC = matrix[2][2]
				/ (matrix[2][2] + matrix[0][2] + matrix[1][2]);
		double precision = (precisionA + precisionB + precisionC) / 3;

		double recallA = matrix[0][0]
				/ (matrix[0][0] + matrix[0][1] + matrix[0][2]);
		double recallB = matrix[1][1]
				/ (matrix[1][1] + matrix[1][2] + matrix[1][0]);
		double recallC = matrix[2][2]
				/ (matrix[2][2] + matrix[2][0] + matrix[2][1]);
		double recall = (recallA + recallB + recallC) / 3;

		double accuracy = 0;
		double total = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				total += matrix[i][j];
			}
		}
		accuracy = (matrix[0][0] + matrix[1][1] + matrix[2][2]) / total;
				
		double f1 = 2 * ((precision * recall) / (precision + recall));
		double f1A = 2 * ((precisionA * recallA) / (precisionA + recallA));
		// double f1B = 2 * ((precisionB * recallB) / (precisionB + recallB));
		double f1C = 2 * ((precisionC * recallC) / (precisionC + recallC));

		System.out.println("precision: " + precision);
		System.out.println("recall: " + recall + "\n");
		System.out.println("accuracy: " + accuracy + "\n" );
		System.out.println("precisionPos: " + precisionA);
		System.out.println("recallPos: " + recallA + "\n");
		System.out.println("precisionNeutral: " + precisionB);
		System.out.println("recallNeutral: " + recallB + "\n");
		System.out.println("precisionNeg: " + precisionC);
		System.out.println("recallNeg: " + recallC + "\n");
		System.out.println("f1: " + f1);
		System.out.println("f1 without neutral: " + (f1A + f1C) / 2);
		
	}

	/**
	 * Prints the result of the sentiment analysis to the result file errorcount
	 * includes bad predict and tweet "Not available"
	 * 
	 * @param resultMapToPrint
	 *            a map with the results for all Tweets
	 * @throws FileNotFoundException
	 */
	protected void printResultToFile(Map<String, Integer> resultMapToPrint)
			throws FileNotFoundException {
		int errorcount = 0;
		Map<Integer, String> classValue = new HashMap<Integer, String>();
		classValue.put(0, "positive");
		classValue.put(1, "neutral");
		classValue.put(2, "negative");
		File file = new File("resources/tweets/" + this.PATH + ".txt");
		PrintStream tweetPrintStream = new PrintStream(new File(
				"output/result.txt"));
		PrintStream tweetPrintStreamError = new PrintStream(new File(
				"output/error_analysis.txt"));
		Scanner scanner = new Scanner(file);
		
		/******first format of output**********/
		/*
		while (scanner.hasNextLine()) {
			String[] line = scanner.nextLine().split("\t");
			if (line.length == 6) {
				String id = line[0] + " " + line[2] + " " + line[3];
				// put target term in the output
				String[] words = line[5].split("\\s+");
				String target = "";
				for (int i = Integer.parseInt(line[2]); i <= Integer
						.parseInt(line[3]) && i < words.length; i++) {
					target = target + words[i] + " ";
				}
				line[3] = line[3] + "\ttarget: " + target;
				
				if (!line[5].equals("Not Available")) {
					String resultSenti = classValue.get(resultMapToPrint
							.get(id)); // result sentiment
					String initialSenti = line[4]; // initial sentiment
					if (!initialSenti.equals(resultSenti)) {
						errorcount++;
						line[4] = "[Error] Initial: " + initialSenti
								+ "\tResult: " + resultSenti + "\t";
						if (debug) {
							System.out.print(StringUtils.join(line, "\t"));
							System.out.println();
						}
						tweetPrintStreamError.print(StringUtils
								.join(line, "\t"));
						tweetPrintStreamError.println();
					} else {
						line[4] = "[OK] 	Initial: " + initialSenti
								+ "\tResult: " + resultSenti + "\t";
						if (debug) {
							System.out.print(StringUtils.join(line, "\t"));
							System.out.println();
						}
					}
				} else if (line[5].equals("Not Available")) {
					errorcount++;
				}
			}

			//tweetPrintStream.print(StringUtils.join(line, "\t"));
			tweetPrintStream.println(line[5]);
			tweetPrintStream.println(line[3] +  " -> " + line[4]);
		}
		*/
		while (scanner.hasNextLine()) {
			String[] line = scanner.nextLine().split("\t");
			if (line.length == 6) {
				String id = line[0] + " " + line[2] + " " + line[3];
				// put target term in the output
				String[] words = line[5].split("\\s+");
				String target = "";
				for (int i = Integer.parseInt(line[2]); i <= Integer
						.parseInt(line[3]) && i < words.length; i++) {
					target = target + words[i] + " ";
				}

				
				if (!line[5].equals("Not Available")) {
					String resultSenti = classValue.get(resultMapToPrint
							.get(id)); // result sentiment
					String initialSenti = line[4]; // initial sentiment
					// error
					if (!initialSenti.equals(resultSenti)) {
						errorcount++;
						line[3] = line[3] + "\ttarget: " + target;
						line[4] = "[Error] Initial: " + initialSenti
								+ "\tResult: " + resultSenti + "\t\t";
						if (debug) {
							System.out.print(StringUtils.join(line, "\t"));
							System.out.println();
						}
						tweetPrintStreamError.print(StringUtils
								.join(line, "\t"));
						tweetPrintStreamError.println();
					} else {
					// correct
						tweetPrintStream.println(line[5]);
						tweetPrintStream.println(target +  " ->  " + resultSenti + "\n");
					}
				} else if (line[5].equals("Not Available")) {
					errorcount++;
				}
			}
		}
		
		scanner.close();
		tweetPrintStream.close();
		tweetPrintStreamError.close();
		if (errorcount != 0)
			System.out.println("Error prediction number: " + errorcount);
	}
}
