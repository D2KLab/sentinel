package com.eurecom.sentinel;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cmu.arktweetnlp.Tagger.TaggedToken;

/**
 * Represents one Tweet and saves some features and the preprocessed versions of the Tweet\
 * 
 * @author SentiNEL, Webis
 * Code is base on Webis System
 * SentiNEL add targetWordList, targetBegin, targetEnd attributes
 * SentiNEL add getTargetWordList method
 * SentiNEL add setTargetWordList method
 * SentiNEL add getTargetBegin method
 * SentiNEL add getTargetEnd method
 * SentiNEL add getTargetContent method
 * SentiNEL add setNGramsTarget method
 * SentiNEL add getNGramsTarget method
 * SentiNEL add setCharNGramListTarget method
 * SentiNEL add getCharNGramListTarget method
 * SentiNEL modify toString method
*/
public class Tweet {
	private String rawTweet;
	private String tweetID;
	private String tweetString;
	private String sentiment;
	private List<TaggedToken> wordList;
	private List<TaggedToken> targetWordList;
	private List<TaggedToken> wordListRaw;
	private List<TaggedToken> wordListCollapsed;
	private Map<String, String> wordListStanford;
	private boolean lastEmoticon = false;
	private int negationCount = 0;
	private Set<String> nGramList;
	private Set<String> charNGramList;
	private Map<String, Integer> posTagList;
	private Set<String> clusterList;
	private Set<String> emoticonList;
	private Set<String> stemList;

	// target positioin
	private String targetBegin;
	private String targetEnd;
	private Set<String> nGramListTarget;
	private Set<String> charNGramListTarget;

    /*
    public Tweet(String tweet, String senti, String tID){
    	rawTweet = tweet;
        sentiment = senti;
    	tweetID = tID;
    }
    */


	/**
	 * Create Tweet
	 * @param tweet content of tweet
	 * @param senti sentiment of the target
	 * @param tID tweet id
	 * @param targetBegin beginning of the target position
	 * @param targetEnd end of the target postition
	 */
	public Tweet(String tweet, String senti, String tID, String targetBegin, String targetEnd){
		rawTweet = tweet;
		sentiment = senti;
		tweetID = tID;
		this.targetBegin = targetBegin;
		this.targetEnd = targetEnd;
	}

	public List<TaggedToken> getTargetWordList() {
		return targetWordList;
	}

	public void setTargetWordList(List<TaggedToken> targetWordList) {
		this.targetWordList = targetWordList;
	}

	public String getTargetBegin() {
		return targetBegin;
	}

	public String getTargetEnd() {
		return targetEnd;
	}

	public void setNGramsTarget(Set<String> nGramListTarget) {
		this.nGramListTarget = nGramListTarget;
	}

	public Set<String> getNGramListTarget() {
		return this.nGramListTarget;
	}

	public void setCharNGramListTarget(Set<String> charNGramListTarget) {
		this.charNGramListTarget = charNGramListTarget;
	}

	public Set<String> getCharNGramListTarget() {
		return this.charNGramListTarget;
	}

	/**
	 * return the content of target term
	 * @return
	 */
	public String getTargetContent() {
		String targetBegin = getTargetBegin();
		String targetEnd = getTargetEnd();
		//String[] words = getRawTweetString().split("\\s+");
		String[] words = getTweetString().split("\\s+");
		String target = "";
		for (int i = Integer.parseInt(targetBegin); i <= Integer.parseInt(targetEnd) && i < words.length; i++) {
			target += words[i] + " ";
		}
		return target;
	}


	public String getRawTweetString() {
		return this.rawTweet;
	}

	public String getTweetID() {
		return this.tweetID;
	}

	public String getTweetString() {
		return this.tweetString;
	}

	public void setTweetString(String tstring) {
		this.tweetString = tstring;
	}

	public String getSentiment() {
		return this.sentiment;
	}

	public void setWordList(List<TaggedToken> wList){
		this.wordList = wList;
	}

	public List<TaggedToken> getWordList() {
		return this.wordList;
	}

	public List<TaggedToken> getRawWordList() {
		return this.wordListRaw;
	}

	public void setRawWordList(List<TaggedToken> wListRaw) {
		this.wordListRaw = wListRaw;
	}

	public List<TaggedToken> getCollapsedWordList() {
		return this.wordListCollapsed;
	}

	public void setCollapseList(List<TaggedToken> wListCol) {
		this.wordListCollapsed = wListCol;
	}

	public void setStanfordWordList(Map<String, String> sList) {
		this.wordListStanford = sList;

	}
	public Map<String, String> getStanfordWordList() {
		return this.wordListStanford;
	}

	public void setLastEmoticon(boolean b) {
		this.lastEmoticon = b;
	}

	public boolean isLastEmoticon(){
		return this.lastEmoticon;
	}

	public int getNegationCount() {
		return this.negationCount;
	}

	public void setNegationCount(int nCount) {
		this.negationCount = nCount;
	}

	public Set<String> getnGramList() {
		return this.nGramList;
	}

	public void setNGrams(Set<String> nGList) {
		this.nGramList = nGList;
	}

	public Set<String> getCharNGramList() {
		return this.charNGramList;
	}

	public void setCharNGramList(Set<String> nGramList) {
		this.charNGramList = nGramList;
	}

	public Map<String, Integer> getPosTagList() {
		return this.posTagList;
	}

	public void setPosTags(Map<String, Integer> tagMap) {
		this.posTagList = tagMap;

	}

	public Set<String> getClusterList() {
		return this.clusterList;
	}

	public void setClusters(Set<String> cList) {
		this.clusterList = cList;

	}

	public Set<String> getEmoticonList() {
		return this.emoticonList;
	}

	public void setEmoticons(Set<String> emoticons) {
		this.emoticonList = emoticons;
	}

	public Set<String> getStemList() {
		return this.stemList;
	}

	public void setStemList(Set<String> sList) {
		this.stemList = sList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((tweetString == null) ? 0 : tweetString.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tweet other = (Tweet) obj;
		if (this.rawTweet == null) {
			if (other.rawTweet != null)
				return false;
		} else if (!this.tweetID.equals(other.tweetID)) {
			return false;
		}
		else if (!this.targetBegin.equals(other.targetBegin)) {
			return false;
		} else if (!this.targetEnd.equals(other.targetEnd)) {
			return false;
		}
		return true;
	}


	@Override
	public String toString() {
		return "Tweet [ tweetID=" + tweetID
				+ ", targetBegin=" + targetBegin
				+ ", targetEnd=" + targetEnd
				+ ", sentiment=" + sentiment
				+ "rawTweet=" + rawTweet + "]";

	}


}
