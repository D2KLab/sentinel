##Introduction
We will introduce how to use the lexicons, including the introduction of the features of lexicons we extracted and the way to extract them from the lexicons. 

##Features of lexicons
We used **NRC Hashtag Sentiment Lexicon, Sentiment140 Lexicon, NRC Emotion Lexicon, MPQA, Bing Liu Lexicon, AFINN-11 Lexicon and SentiWordNet Lexicon**. For each entry of the seven dictionaries, the dictionary score is either positive, negative, or zero. For a target term and each individual dictionary, several features are computed:
<br/>- Total count of tokens in the target term with sentiment score greater than 0
<br/>- The sum of the sentiment scores for all tokens in the target
<br/>- The maximal sentiment score
<br/>- The non-zero sentiment score of the last token in the target

##How to extract features
**NRC Hashtag Sentiment Lexicon:** the lexicons we used are located in lexi/hashtag/ folder. We used unigrams (unigrams-pmilexicon.txt) and bigrams lexicons (bigrams-pmilexicon.txt). We get the sentimentScore as the score of the word.
<br/>
**The format of the lexicons is:**<br/>
term\<tab\>sentimentScore\<tab\>numPositive<\tab\>numNegative
<br/>
**Examples of NRC Hashtag Sentiment Lexicon:**<br/>
\#amazing	5.096	4761	47<br/>
\#nice	5.035	1620	17<br/>
zayns	5	6	0<br/>

<br/>
**Sentiment140 Lexicon:** the lexicons we used are located in lexi/sentiment140/folder. We used  unigrams (unigrams-pmilexicon.txt) and bigrams lexicons (bigrams-pmilexicon.txt). We get the sentimentScore as the score of the word.
<br/>
**The format of the lexicons is the same as NRC Hashtag Sentiment Lexicon:**<br/>
term\<tab\>sentimentScore\<tab\>numPositive<tab>numNegative
<br/>
**Examples of NRC Hashtag Sentiment Lexicon:**<br/>
add everyone	7.352	1485	1<br/>
or pay	5.975	1499	4<br/>
train or	5.561	1487	6<br/>

<br/><br/>
**NRC Emotion Lexicon:** the lexicon we used is located in lexi/ folder. The file name of the lexicon is NRC-emotion-lexicon-wordlevel-alphabetized-v0.92.txt. If the word has a positive attribute with value 1, set the score of the term with value 1. If the word has a negative attribute with value 1, set the score of the term with value -1.
<br/>
**Examples of NRC Emotion Lexicon:**<br/>
admire	anger	0<br/>
admire	anticipation	0<br/>
admire	disgust	0<br/>
admire	fear	0<br/>
admire	joy	0<br/>
admire	negative	0<br/>
admire	positive	1<br/>
admire	sadness	0<br/>
admire	surprise	0<br/>
admire	trust	1<br/>
adrift	anticipation	1<br/>
adrift	disgust	0<br/>
adrift	fear	1<br/>
adrift	joy	0<br/>
adrift	negative	1<br/>
adrift	positive	0<br/>
adrift	sadness	1<br/>
adrift	surprise	0<br/>
adrift	trust	0<br/>

<br/><br/>
**MPQA:** the lexicon we used is located in lexi/ folder. The file name of the MPQA lexicon is subjclueslen1-HLTEMNLP05.tff. If prior polarity of the word is negative, set value 1. If prior polarity of the word is positive, set value -1. Besides, if the type of the word is strongsubj, multiple the value by 5.
<br/>
**Format of the lexicon:<br/>**
type\<tab\>word1\<tab\>pos1\<tab\>stemmed\<tab\>prior polarity
<br/>
**Example of the lexicons:**<br/>
type=strongsubj len=1 word1=abasement pos1=anypos stemmed1=y priorpolarity=negative

<br/><br/>
**Bing Liu Lexicon: **the lexicons we used are located in lexicons/bingliu/ folder. The names of the lexicons are negative-words.txt and positive-words.txt. Set the score of all the negative words (which are in negative-words.txt) -1, those of positive (which are in positive-words.txt) words 1.

<br/><br/>
**AFINN-11 Lexicon:** the lexicon we used is located in lexi/ folder. The lexicon’s name is AFINN-111.txt. We get the score of the word in the lexicon.
<br/>
**Format of AFINN Lexicons:**<br/>
word<tab>score
<br/>
**Example of AFINN Lexicon:**
abandon	-2
abandoned	-2
abandons	-2

<br/>
**SentiWordNet Lexicon:** the lexicon we used is located in lexi/ folder. The lexicon’s name is SentiWordNet\_3.0.0.txt. We get the positive and negative scores from the third and forth fields respectively. The word is in forth field.
<br/>
**Example of SentiWordNet Lexicon:	**
a	00001740	0.125	0	able\#1	(usually followed by 'to') having the necessary means or skill or know-how or authority to do something; "able to swim"; "she was able to program her computer"; "we were at last able to buy a car"; "able to get a grant for the project"
a	00002098	0	0.75	unable\#1	(usually followed by 'to') not having the necessary means or skill or know-how; "unable to get to town without a car"; "unable to obtain funds"

