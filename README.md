# SentiNEL: Sentiment Analysis from Tweets
[SentiNEL](https://github.com/MultimediaSemantics/sentinel) system is developed for sentiment analysis of tweets based on [SemEval2015 Task10-Subtask A](http://alt.qcri.org/semeval2015/task10/): Contextual Polarity Disambiguation. The purpose of SentiNEL is that given a message containing a marked instance of a word or a phrase, determines whether that instance is positive, negative or neutral in that context. SentiNEL is inspired by the [IOA system](http://alt.qcri.org/semeval2015/cdrom/pdf/SemEval091.pdf). The main differences are that SentiNEL extracts more features (e.g. Char 3, 4, 5 grams, Hashtag, longer Word2Vec dimension, more lexicons etc.) for training. Besides, SentiNEL trains L2-regularized logistic regression SVM classifier with C value 0.5. The code is based on [Webis system](https://github.com/webis-de/ECIR-2015-and-SEMEVAL-2015). However, Webis is a system only for SemEval2015 Task10-Sub Task B (Message-level task). We modify the code and adapt it to term-level. The system is scored by computing F1-score for predicting positive/negative phrases. Comparing to IOA system, SentiNEL improves the F1-score from 83.90 to 88.15 on Tweet2013-test, from 84.18 to 84.73 on Sms2013-test.

Key words: Sentiment analysis, Machine Learning, Data Mining, NLP

## Architectural Overview 
SentiNEL consists of four steps:
* Pre-train Word2Vec module: it trains the Word2Vec vectors from all the words which appear at least 3 times in the dataset 
* Extraction of features: it extracts features from the training dataset
* Train: it trains the SVM classifier with extracted features
* Evaluation: it evaluates the trained SVM classifier and tests it with testing dataset

![image](https://cloud.githubusercontent.com/assets/7665292/13723294/38541c36-e860-11e5-8d58-afd3ac302450.png)

## Corpus description
The corpus is collected from [SemEval-2015 Task 10 Dataset](http://alt.qcri.org/semeval2015/task10/index.php?id=data-and-tools). The following table shows the account of dataset we collected. 
<table>
	<tr>
		<td>Corpus</td>
		<td>Positive</td>
		<td>Negative</td>
		<td>Neutral</td>
		<td>Total Tweets</td>
	</tr>
	<tr>
		<td>Tweet2013-train</td>
		<td>4484(62.5%)</td>
		<td>2329(32.5%)</td>	
		<td>356(5%)</td>
		<td>7169</td>
	</tr>
	<tr>
		<td>Tweet2013-dev</td>
		<td>506(62.6%)</td>
		<td>326(34.0%)</td>
		<td>40(3.4%)</td>
		<td>872</td>
	</tr>
	<tr>
		<td>Tweet2013-test</td>
		<td>2132(62.6%)</td>
		<td>1156(34.0%)</td>
		<td>116(3.4%)</td>
		<td>3404</td>
	</tr>
	<tr>
		<td>Sms2013-test</td>
		<td>1071(45.9%)</td>
		<td>1103(47.3%)</td>
		<td>159(6,8%)</td>
		<td>2333</td>
	</tr>
	<tr>
		<td>Tweet2014-test</td>
		<td>3568(66.5%)</td>
		<td>1606(29.9%)</td>
		<td>190(3.5%)</td>
		<td>5364</td>
	</tr>
	<tr>
		<td>Sms2014-test</td>
		<td>710(45.3%)</td>
		<td>747(46.7%)</td>
		<td>111(7.1%)</td>
		<td>1568</td>
	</tr>
</table>


## Requirements
* Java 7+
* Maven 3+

## Setting Up 
	git clone https://github.com/MultimediaSemantics/sentinel	
    mvn clean
    mvn compile
    
## Train
	mvn exec:java -Dexec.args="train train_file [save_features_file]"
```sh
-train					set 	train mode
-train_file 			set 	the input file for training
-save_features_file		set 	the file to save trained features, by default SentiNEL saves the extracted features in arff/Trained-Features.arff
```	

### example
	mvn exec:java -Dexec.args="train train"
Extract the features from training dataset: resources/tweets/train.txt, and save the extracted features in arff/Trained-Features.arff

	mvn exec:java -Dexec.args="train train model1"
Extract the features from training dataset: resources/tweets/train.txt, and save the extracted features in arff/Trained-Features-model1.arff

## Evaluation
    mvn exec:java -Dexec.args="eval test_file [saved_features_file]"
```sh
-eval					set 	test mode
-test_file 				set 	the input file for testing
-saved_features_file	set 	the file contains trained features, by default SentiNEL trains SVM classifier with arff/Trained-Features.arff
```	   
    
### example
	mvn exec:java -Dexec.args="eval Tweet2013-test"
Train SVM classifier with the extracted features: arff/Trained-Features.arff, then evaluate it with testing dataset: resources/tweets/Tweet2013-test.txt"

	mvn exec:java -Dexec.args="eval Sms2013-test Trained-Features-model1"
Train SVM classifier with the extracted features: arff/Trained-Features-model1.arff, then evaluate it with testing dataset: resources/tweets/Sms2013-test.txt"

## Output
	"I drove a Lincoln and it's a truly dream"
    Lincoln -> positive
The output of SentiNEL locates in output/ folder. result.txt file contains the sentiment prediction results, and error_analysis.txt file contains the wrong sentiment prediction results.
    
## Team
* Yonghui Feng
* Ahmed Abdelli
* Giuseppe Rizzo
* Raphael Troncy

