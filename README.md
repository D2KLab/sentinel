# Sentinel system 
This sentinel system is built to classify sentiment from Tweet. Idea is from SemEval2015 Sub Task A. The objective is to detect the sentiment of a term within a message(Term-level task).

## Approach
Sentinel system inspired by the [NRC system](http://www.cs.toronto.edu/~xzhu/SemEval2014_NRC_t9.pdf). The code is based on [Webis system](https://github.com/webis-de/ECIR-2015-and-SEMEVAL-2015). However Webis is a system only for SemEval2015 Sub Task B(Message-level task). We modified the code and adapted it to term-level. Besides, we extracted and added some new features e.g. More lexicons, Word2Vec etc.

Sentinel System is trained with Tweet2013-train.txt and Tweet2013-dev.txt. For the features of Word2Vec, we pre-trained a Word2Vec model with all the tweets(Tweet2013-train.txt, Tweet2013-dev.txt, Tweet2013-text.txt and Tweet2014-test.txt)

Thanks to theses two systems, we could reproduce sentinel system.

![image](https://docs.google.com/drawings/d/1G0UbNY2REuCkvXTFCONzqe5LV6ZiyLxHONayuElWJNU/pub?w=960&h=720)

## Corpus downloaded from SemEval2015
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
		<td>3175(60.2%)</td>
		<td>1857(35.2%)</td>
		<td>237(4.5%)</td>
		<td>5270</td>
	</tr>
</table>


## How to run
Above all, train the module with training data. Then evaluate it with testing data.
The system is in sentinel/ folder. Main class is in src/ folder.
### train Usage
	train <train_data> [<model_name>]
Train a model with train\_data, save the model as the name of model\_name. By default model is saved as the name of Trained-Features.aff

### evaluation Usage
	eval <test_data> [<model_name>]
Evaluate a model with test_data. By default using the trained model of Trained-Features.aff

### Examples
	
	> train Tweet2013-train

Train a model with training dataset in sentinel/resources/tweets/Tweet2013-train.txt, and save the model in resources/arff/Trained-Features.arff

	> train train sentinel
	
Train a model with training dataset in sentinel/resources/tweets/train.txt, and save the model in sentinel/resources/arff/Trained-Features-sentinel.arff

	> eval Tweet2013-test 
	
Evaluate the sentinel/resources/Trained-Features.arff model with the training dataset in sentinel/resources/tweets/Tweet2013-test.txt 

	> eval Tweet2013-test Trained-Features-sentinel
	
Evaluate the sentinel model which is saved in sentinel/resources/Trained-Features-sentinel.arff with the training dataset in sentinel/resources/tweets/Tweet2013-test.txt 
	
### output

    "I drove a Linconl and it's a truly dream"
    Linconl -> proper noun -> positive

    "I drove a Linconl and it was awful"
    Linconl -> proper noun -> negative

All the predictions are in sentinel/output folder. The error output is in sentinel/output/error_analysis folder
	

## Team
* Yonghui Feng
* Ahmed Abdelli
* Giuseppe Rizzo
* Raphael Troncy
