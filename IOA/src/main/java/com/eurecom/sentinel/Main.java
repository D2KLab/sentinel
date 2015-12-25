package com.eurecom.sentinel;

public class Main {
	
	static String mode;
	static String PATH = "";
	private static long startTime = System.currentTimeMillis();
	
	/**
	 * Main function
	 * @param args Command-Line Arguments
	 * @throws Exception
	 */	
	public static void main(String[] args) throws Exception{
		/*names of arff file where stores the features
		 * train train_dataset, call trainSvstem to create arff file, if names == "", use defaut features
		 * eval test_dataset, first train classifier with arff file, then evaluate with test_dataset
		 */
		String nameOfTrain = "";
		String saveName = "";
		
		if (args.length != 2) {
			System.out.println("[usage] <mode: train | eval> <datasetname>");
			System.exit(0);
		} else {
			PATH = args[1];
		}

		SentimentanalysisECIR sentimentanalysis = new SentimentanalysisECIR(PATH);
		switch(args[0]) {
			case "eval":
				sentimentanalysis.testSystem(nameOfTrain);
				break;
			case "train":
				sentimentanalysis.trainSystem(saveName);
				break;
			default:
				throw new IllegalArgumentException("Invalid mode: " + args[0]);
		}
		
		long endTime = System.currentTimeMillis();
        System.out.println("It took " + ((endTime - startTime) / 1000) + " seconds");
				
	}
}	
