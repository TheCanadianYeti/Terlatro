package ca.sheridancollege.project;

import java.util.Objects;

/**
 * Concrete child class representing a standard playing card in Balatro.
 * Tracks suit, rank, and chip scoring value.
 *
 * @author Marcus Podnar
 */
public class PlayingCard extends Card {

    public enum Suit {
        SPADES, HEARTS, CLUBS, DIAMONDS
    }

    public enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6),
        SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(10), QUEEN(10), KING(10), ACE(11);

        private final int defaultChips;

        Rank(int defaultChips) {
            this.defaultChips = defaultChips;
        }

        public int getDefaultChips() {
            return defaultChips;
        }
    }

    private Suit suit;
    private Rank rank;
    private int chips;

    public PlayingCard(Suit suit, Rank rank) {
        this(suit, rank, null);
    }

    public PlayingCard(Suit suit, Rank rank, String artFilePath) {
        super(rank + " of " + suit, "Scores " + rank.getDefaultChips() + " chips when played.", 1, artFilePath);
        this.suit = suit;
        this.rank = rank;
        this.chips = rank.getDefaultChips();
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
        this.chips = rank.getDefaultChips();
    }

    public int getChips() {
        return chips;
    }

    public void setChips(int chips) {
        this.chips = Math.max(0, chips);
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        PlayingCard other = (PlayingCard) obj;
        return chips == other.chips && suit == other.suit && rank == other.rank;
    }

    @Override
    public String toString() {
        return getName() + " (+" + chips + " Chips, Cost: $" + getCost() + ")";
    }
}