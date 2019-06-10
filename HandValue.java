/** 
  * File	      :	HandValue.java
  * Related files : CribbageRank.java
  * 				Combinations.java
  * Author	      :	Tang Tang
  * Project	      :	The Game of Cribbage
  * Purpose	      :	Compute the total number of points the hand of four cards
  *				    plus one start card would score according to five rules --
  *				    15s, Pairs, Runs, Flushes and "One for his nob".
  * Approach      :	Compute points that are scored according to the five rules
  * 			    respectively and then all of these points are totaled to
  * 			    find the value of a hand.
  * 				Flushes and "one for his nob" need to consider about suits;
  * 				15s, Pairs and Runs only need to consider about ranks, as
  * 				well as combinations of the ranks.
  */

import java.util.Arrays;

public class HandValue {
	
	/** Receive 5 cards on the command line and
	  * print out the number of points the hand would score.
	  * A full hand consists of five cards, as the start card
	  * is considered as part of a hand.
	  */
	public static void main(String[] args) {
		String card1 = args[0].toUpperCase();
		String card2 = args[1].toUpperCase();
		String card3 = args[2].toUpperCase();
		String card4 = args[3].toUpperCase();
		String startCard = args[4].toUpperCase();
		String[] hand = {card1, card2, card3, card4};
		String[] fullHand = {card1, card2, card3, card4, startCard};
		
		HandValue handValue = new HandValue();
		int valueOfHand = handValue.totalPoint(hand, startCard, fullHand);
		System.out.println(valueOfHand);
	}
	
	/** Total points
	  * Points scored following the five rules below are totaled
	  * to find the value of a hand.
	  */
	public int totalPoint(String[] hand, String startCard, String[] fullHand) {
		int total = 0;
		total = fifteens(fullHand) + pairs(fullHand) + runs(fullHand) + 
				flushes(hand, startCard) + nob(hand, startCard);
		return total;
	}
	
	/** Rule1 -- 15s
	  * 2 points are scored for each distinct combinations of cards that add to 15.
	  * An ace is counted as 1; a jack, queen or king is counted as 10;
	  * and other cards are counted as their face value.
	  */
	public int fifteens(String[] fullHand) {
		int fifteenScore = 0;
		String[][] combos = Combinations.combinations(fullHand);
		for(String[] combo : combos) {
			int total = 0;
			for(int i = 0; i < combo.length; i++) {
				CribbageRank rank = CribbageRank.findByAbbr(getRank(combo[i]));
				total += CribbageRank.valueOf(rank).faceValue();
			}
			if(total == 15) {
				fifteenScore += 2;
			}
		}
		return fifteenScore;
	}
	
	/** Rule2 -- Pairs
	  * 2 points are scored for each pair.
	  */
	public int pairs(String[] fullHand) {
		int pairScore = 0;
		for(int i = 0; i < fullHand.length-1; i++) {
			for(int j = i+1; j < fullHand.length; j++) {
				if(getRank(fullHand[i]).equals(getRank(fullHand[j]))) {
					pairScore += 2;
				}
			}
		}
		return pairScore;
	}
	
	/**Rule3 -- Runs
	  * 1 point is scored for each card in a run of 3 or more consecutive
	  * cards (the suits of these cards need not to be the same).
	  */
	public int runs(String[] fullHand) {
		int runScore = 0;
		//Create an array consisting of rank orders converted from the
		//array of five cards.
		int[] rankOrder = new int[fullHand.length];
		for(int i = 0; i < fullHand.length; i++) {
			CribbageRank rank = CribbageRank.findByAbbr(getRank(fullHand[i]));
			rankOrder[i] = CribbageRank.valueOf(rank).ordinal();
		}
		int[][] combos = Combinations.combinations(rankOrder);
		for(int[] combo : combos) {
			//A run consists of 3 or more consecutive cards.
			if(isConsecutive(combo) && combo.length >= 3) {
				//Find the max length among combinations of cards.
				 runScore = Math.max(runScore, combo.length);
			}
		}
		//The number of pairs in a hand can be calculated
		//according to the pairScore.
		int pairNum = pairs(fullHand)/2;
		//When there are 1 or 2 pairs in a hand, the runScore will be
		//multiply by pairScore.
		if(pairNum == 1 || pairNum == 2) {
			runScore *= pairs(fullHand);
			//When there are 3 pairs in a hand, the runScore will be
			//multiply by pairNum.
		} else if(pairNum == 3) {
			runScore *= pairNum;
		}
		return runScore;
	}
	
	/** Rule4 -- Flushes
	  * 4 points are scored if all the cards in the hand are of the same suit.
	  * 1 further point is scored if the start card is also the same suit.
	  */
	public int flushes(String[] hand, String startCard) {
		int flushScore = 0;
		boolean handFlush = true;
		boolean fullFlush = false;
		for(int i = 0; i < hand.length-1; i++) {
			if(!(getSuit(hand[i]).equals(getSuit(hand[i+1])))) {
				handFlush = false;
			}
		}
		if(handFlush) {
			if(getSuit(hand[0]).equals(getSuit(startCard))) {
				fullFlush = true;
			}
		}
		if(fullFlush) {
			flushScore = 5;
		} else if(handFlush) {
			flushScore = 4;
		}
		return flushScore;
	}
	
	/** Rule5 -- "One for his nob"
	  * 1 point is scored if the hand contains the Jack 
	  * of the same suit as the start card.
	  */
	public int nob(String[] hand, String startCard) {
		int nobScore = 0;
		for(int i = 0; i < hand.length; i++) {
			if(getRank(hand[i]).equals("J") && 
					getSuit(hand[i]).equals(getSuit(startCard))) {
				nobScore = 1;
			}
		}
		return nobScore;
	}
	
	/** Get the rank of a card from a two-character string. */
	public String getRank(String card) {
		char Rank = card.charAt(0);
		String rank = Character.toString(Rank);
		return rank;
	}
	
	/** Get the suit of a card from a two-character string. */
	public String getSuit(String card) {
		char Suit = card.charAt(1);
		String suit = Character.toString(Suit);
		return suit;
	}
	
	/** Check if cards in the array are consecutive.
	  * The array will be sorted in rank order (Ace up to King).
	  */
	public boolean isConsecutive(int[] rankOrder) {
		Arrays.sort(rankOrder);
		for(int i = 0; i < rankOrder.length-1; i++) {
			if(rankOrder[i+1] - rankOrder[i] != 1) {
				return false;
			}
		}
		return true;
	}
}