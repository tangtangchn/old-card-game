/** A playing card rank type designed for cribbage. It supports a single-
 *  character abbreviation for each rank, as well as providing the face
 *  value of a rank (ACE=1, KING, QUEEN, and JACK=10, other ranks are their
 *  face value), needed for counting 15s in a cribbage hand. Also provides
 *  methods to get the next smaller and larger rank of a given rank, as well
 *  as the method to get cribbage rank according to its abbreviation.
 *
 *  @author Peter Schachte schachte@unimelb.edu.au
 * 		    Tang Tang
 */

public enum CribbageRank {
        ACE('A'),
        TWO('2'),
        THREE('3'),
        FOUR('4'),
        FIVE('5'),
        SIX('6'),
        SEVEN('7'),
        EIGHT('8'),
        NINE('9'),
        TEN('T'),
        JACK('J'),
        QUEEN('Q'),
        KING('K');

        /** Single character abbreviation used to briefly print the rank. */
        private final char abbrev;

        /** @return the single-character abbreviation for this rank. */
        public char abbrev() {
            return abbrev;
        }
        
        /** @return the rank according to the single-character abbreviation. */
        public static CribbageRank findByAbbr(String abbr) {
            for(CribbageRank rank : values()) {
            	char Rank = rank.abbrev();
            	String abbrRank = Character.toString(Rank);
                if(abbrRank.equals(abbr)) {
                    return rank;
                }
            }
            return null;
        }

        /** @return the face value of the rank for counting 15's in cribbage
         *  (ACE=1, KING, QUEEN, and JACK=10, other ranks are their face value).
         */
        public int faceValue() {
            return Math.min(this.ordinal()+1, 10);
        }

        /** @return the next higher rank. */
        public CribbageRank nextHigher() {
            int value = this.ordinal() + 1;
            return value >= values().length ? null : values()[value];
        }

        /** @return the next lower rank. */
        public CribbageRank nextLower() {
            int value = this.ordinal() - 1;
            return value < 0 ? null : values()[value];
        }
        
        /** Construct a rank.
         *  @param abbrev the single-character abbreviation for this rank.
         */
        CribbageRank(char abbrev) {
            this.abbrev = abbrev;
        }
        
        /** @return the rank as a single-character string. */
        @Override
        public String toString() {
            return Character.toString(abbrev);
        }
        
        /** @return the enum constant with the specified name. */
		public static CribbageRank valueOf(CribbageRank rank) {
			return rank;
		}
}
