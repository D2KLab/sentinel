# SentiNEL: Sentiment Analysis from Tweets
[SentiNEL](https://github.com/MultimediaSemantics/sentinel) system is developed for sentiment analysis of tweets based on [SemEval2015 Task10-Subtask A](http://alt.qcri.org/semeval2015/task10/): Contextual Polarity Disambiguation. The purpose of SentiNEL is that given a message containing a marked instance of a word or phrase, determines whether that instance is positive, negative or neutral in that context. SentiNEL is inspired by the [NRC system](http://www.cs.toronto.edu/~xzhu/SemEval2014_NRC_t9.pdf). The code is based on [Webis system](https://github.com/webis-de/ECIR-2015-and-SEMEVAL-2015). However Webis is a system only for SemEval2015 Sub Task B (Message-level task). We modify the code and adapt it to term-level. Besides, SentiNEL extracts and adds some new features (e.g. More lexicons, Word2Vec etc.)

Key words: Sentiment analysis, Machine Learning, Data Mining, NLP

## Architectural Overview 
SentiNEL consists four steps
* Pre-train Word2Vec module: it trains the Word2Vec vectors from all the words which appears 3 times at least in the dataset 
* Extraction of features: it extracts features from the training dataset
* Train: it trains the SVM classifier with extracted features
* Evaluation: it evaluates the trained SVM classifier and tests it with testing dataset

![image](https://docs.google.com/document/d/1R1Bxj98x8F8AUZy2jTMTbcEukLnE7SgQLNnsHtgh0qk/edit)

## Corpus description
The corpus is downloaded from [SemEval-2015 Task 10 Dataset](http://alt.qcri.org/semeval2015/task10/index.php?id=data-and-tools). The following table shows the account of dataset we collected. 
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
-train_data 			set 	the train input file
-save_features_file		set 	the filename to save trained features, by default the filename is set as arff/Trained-Features.arff
```	

### example
	mvn exec:java -Dexec.args="train train"
Extract the features from training dataset: resources/tweets/train.txt, and save the extracted features in arff/Trained-Features.arff

	mvn exec:java -Dexec.args="train train model1"
Extract the features from training dataset: resources/tweets/train.txt, and save the extracted features in arff/Trained-Features-model1.arff

## Evaluation
    mvn exec:java -Dexec.args="eval test_data [saved features]"
```sh
-eval					set 	test mode
-test_data 				set 	the test input file
-saved_features_file	set 	the save trained features filename, by default the filename is set as arff/Trained-Features.arff
```	   
    
### example
	mvn exec:java -Dexec.args="eval Tweet2013-test"
Train the SentiNEL system with the extracted features: arff/Trained-Features.arff, then evaluate it with testing dataset: resources/tweets/Tweet2013-test"

	mvn exec:java -Dexec.args="eval Sms2013-test Trained-Features-model1"
Train the SentiNEL system with the extracted features: arff/Trained-Features-model1.arff, then evaluate it with testing dataset: resources/tweets/Sms2013-test"

## Output
	"I drove a Lincoln and it's a truly dream"
    Linconl -> positive
The output of SentiNEL locates in output/ folder. It has result.txt file which contains the sentiment prediction result, and error_analysis.txt which contains the wrong sentiment prediction result.
    
## Team
* Yonghui Feng
* Ahmed Abdelli
* Giuseppe Rizzo
* Raphael Troncy
