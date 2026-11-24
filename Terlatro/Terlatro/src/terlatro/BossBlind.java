package terlatro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Concrete child class representing a Boss Blind in Balatro.
 * Implements the 5 active Boss Blinds:
 * - The Club: Debuffs all Club cards
 * - The Flint: Halves base chips and mult
 * - The Ox: Sets player money to $0 if most played hand is played
 * - The Window: Debuffs all Diamond cards
 * - Violet Vessel: Extremely large blind (6x base chips)
 *
 * @author Marcus Podnar
 */
public class BossBlind extends Blind {

    /**
     * Enumeration of the active Balatro Boss Blinds.
     */
    public enum BossType {
        THE_CLUB("The Club", "All Club cards are debuffed", 2.0, 1),
        THE_FLINT("The Flint", "Base Chips and Mult are halved", 2.0, 1),
        THE_OX("The Ox", "Playing your most played hand sets money to $0", 2.0, 1),
        THE_WINDOW("The Window", "All Diamond cards are debuffed", 2.0, 1),
        VIOLET_VESSEL("Violet Vessel", "Very large blind (6x base chips)", 6.0, 8);

        private final String displayName;
        private final String effectDescription;
        private final double multiplier;
        private final int minAnte;

        BossType(String displayName, String effectDescription, double multiplier, int minAnte) {
            this.displayName = displayName;
            this.effectDescription = effectDescription;
            this.multiplier = multiplier;
            this.minAnte = minAnte;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getEffectDescription() {
            return effectDescription;
        }

        public double getMultiplier() {
            return multiplier;
        }

        public int getMinAnte() {
            return minAnte;
        }
    }

    private static final Random RNG = new Random();

    private BossType bossType;
    private boolean disabled;

    // Runtime state tracking for boss mechanics during a round/ante
    private final Set<String> playedHandTypes = new HashSet<>();

    /**
     * Default constructor creating a random Ante 1 Boss Blind.
     */
    public BossBlind() {
        this(1);
    }

    /**
     * Creates a random Boss Blind appropriate for the specified Ante.
     *
     * @param ante current game ante
     */
    public BossBlind(int ante) {
        this(getRandomBossType(ante), ante);
    }

    /**
     * Creates a specific Boss Blind for a given Ante, scaling target chips automatically.
     *
     * @param bossType the specific boss encounter
     * @param ante the ante number
     */
    public BossBlind(BossType bossType, int ante) {
        super(
            (bossType != null) ? bossType.getDisplayName() : BossType.THE_CLUB.getDisplayName(),
            BlindType.BOSS,
            Math.max(1, ante),
            calculateBossTargetScore(ante, bossType),
            BlindType.BOSS.getDefaultReward(),
            (bossType != null) ? bossType.getEffectDescription() : ""
        );
        this.bossType = (bossType != null) ? bossType : BossType.THE_CLUB;
        this.disabled = false;
    }

    /**
     * Full custom constructor for BossBlind.
     *
     * @param bossType specific boss type
     * @param ante ante number
     * @param targetScore custom chip goal
     * @param reward custom dollar payout
     */
    public BossBlind(BossType bossType, int ante, int targetScore, int reward) {
        super(
            (bossType != null) ? bossType.getDisplayName() : BossType.THE_CLUB.getDisplayName(),
            BlindType.BOSS,
            Math.max(1, ante),
            targetScore,
            reward,
            (bossType != null) ? bossType.getEffectDescription() : ""
        );
        this.bossType = (bossType != null) ? bossType : BossType.THE_CLUB;
        this.disabled = false;
    }

    /**
     * Calculates the target chip requirement for a boss blind given its multiplier.
     *
     * @param ante current ante
     * @param bossType boss blind type
     * @return calculated target chips
     */
    public static int calculateBossTargetScore(int ante, BossType bossType) {
        int base = getBaseChipsForAnte(ante);
        double mult = (bossType != null) ? bossType.getMultiplier() : 2.0;
        return (int) Math.round(base * mult);
    }

    /**
     * Selects a random eligible BossType for the given Ante.
     *
     * @param ante current ante
     * @return a random eligible BossType
     */
    public static BossType getRandomBossType(int ante) {
        int safeAnte = Math.max(1, ante);
        List<BossType> eligible = new ArrayList<>();
        for (BossType type : BossType.values()) {
            if (safeAnte >= type.getMinAnte()) {
                eligible.add(type);
            }
        }
        if (eligible.isEmpty()) {
            return BossType.THE_CLUB;
        }
        return eligible.get(RNG.nextInt(eligible.size()));
    }

    /**
     * Factory method creating a random Boss Blind.
     *
     * @param ante ante number
     * @return new BossBlind instance
     */
    public static BossBlind createRandomBoss(int ante) {
        return new BossBlind(ante);
    }

    /**
     * Factory method creating a specific Boss Blind.
     *
     * @param bossType specific boss type
     * @param ante ante number
     * @return new BossBlind instance
     */
    public static BossBlind createBoss(BossType bossType, int ante) {
        return new BossBlind(bossType, ante);
    }

    /**
     * In Balatro, Boss Blinds cannot be skipped under standard rules.
     *
     * @return false always
     */
    @Override
    public boolean canSkip() {
        return false;
    }

    /**
     * Skipping is disabled for Boss Blinds.
     *
     * @return false always
     */
    @Override
    public boolean skip() {
        return false;
    }

    /**
     * Checks if a card is debuffed by the active boss blind.
     * - The Club: All Club cards are debuffed
     * - The Window: All Diamond cards are debuffed
     *
     * @param card the playing card to check
     * @return true if the card is debuffed by this boss
     */
    public boolean isCardDebuffed(PlayingCard card) {
        if (disabled || card == null) {
            return false;
        }
        switch (bossType) {
            case THE_CLUB:
                return card.getSuit() == PlayingCard.Suit.CLUBS;
            case THE_WINDOW:
                return card.getSuit() == PlayingCard.Suit.DIAMONDS;
            default:
                return false;
        }
    }

    /**
     * Modifies base chips for scoring calculation.
     * - The Flint: Halves base chips
     *
     * @param baseChips original base chips
     * @return modified base chips
     */
    public int modifyBaseChips(int baseChips) {
        if (!disabled && bossType == BossType.THE_FLINT) {
            return Math.max(1, baseChips / 2);
        }
        return baseChips;
    }

    /**
     * Modifies base mult for scoring calculation.
     * - The Flint: Halves base mult
     *
     * @param baseMult original base mult
     * @return modified base mult
     */
    public double modifyBaseMult(double baseMult) {
        if (!disabled && bossType == BossType.THE_FLINT) {
            return Math.max(1.0, baseMult / 2.0);
        }
        return baseMult;
    }

    /**
     * Checks if playing the given hand triggers money wipe.
     * - The Ox: Sets money to $0 if most played hand is played
     *
     * @param playedHand the hand type just played
     * @param mostPlayedHand player's most played hand type in the run
     * @return true if money should reset to $0
     */
    public boolean shouldResetMoney(String playedHand, String mostPlayedHand) {
        if (!disabled && bossType == BossType.THE_OX && playedHand != null && mostPlayedHand != null) {
            return playedHand.trim().equalsIgnoreCase(mostPlayedHand.trim());
        }
        return false;
    }

    /**
     * Records a played hand type for run statistics and boss tracking.
     *
     * @param handType the poker hand combination name played
     */
    public void recordPlayedHand(String handType) {
        if (handType != null && !handType.trim().isEmpty()) {
            playedHandTypes.add(handType.trim().toLowerCase());
        }
    }

    /**
     * Disables the boss effect (e.g., via Luchador Joker or Director's Cut reroll).
     */
    public void disableBoss() {
        this.disabled = true;
    }

    /**
     * Resets tracking state for the current round.
     */
    public void resetRoundState() {
        this.playedHandTypes.clear();
    }

    public BossType getBossType() {
        return bossType;
    }

    public void setBossType(BossType bossType) {
        this.bossType = (bossType != null) ? bossType : BossType.THE_CLUB;
        setName(this.bossType.getDisplayName());
        setDescription(this.bossType.getEffectDescription());
        setTargetScore(calculateBossTargetScore(getAnte(), this.bossType));
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Set<String> getPlayedHandTypes() {
        return Collections.unmodifiableSet(playedHandTypes);
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        BossBlind other = (BossBlind) obj;
        return disabled == other.disabled && bossType == other.bossType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bossType, disabled);
    }

    @Override
    public String toString() {
        String status = isDefeated() ? " [DEFEATED]" : "";
        String effectStatus = disabled ? " (DISABLED)" : "";
        return String.format("[BOSS BLIND (Ante %d)] %s%s | Goal: %,d Chips | Reward: $%d%s%n  -> Effect: %s",
                getAnte(), getName(), effectStatus, getTargetScore(), getReward(), status, getDescription());
    }
}
