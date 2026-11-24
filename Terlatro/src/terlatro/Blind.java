package terlatro;

import java.util.Objects;

/**
 * Represents a standard Blind in Balatro (Small Blind, Big Blind, or Boss Blind).
 * Blinds define the chip score threshold required to progress to the next round,
 * along with the dollar payout upon defeating the blind and the ability to skip.
 *
 * @author Marcus Podnar
 */
public class Blind {

    /**
     * Categories of blinds in a standard Balatro Ante.
     */
    public enum BlindType {
        SMALL("Small Blind", 1.0, 3),
        BIG("Big Blind", 1.5, 4),
        BOSS("Boss Blind", 2.0, 5);

        private final String defaultName;
        private final double scoreMultiplier;
        private final int defaultReward;

        BlindType(String defaultName, double scoreMultiplier, int defaultReward) {
            this.defaultName = defaultName;
            this.scoreMultiplier = scoreMultiplier;
            this.defaultReward = defaultReward;
        }

        public String getDefaultName() {
            return defaultName;
        }

        public double getScoreMultiplier() {
            return scoreMultiplier;
        }

        public int getDefaultReward() {
            return defaultReward;
        }
    }

    private String name;
    private BlindType type;
    private int ante;
    private int targetScore;
    private int reward;
    private String description;
    private boolean defeated;
    private boolean skipped;

    /**
     * Default constructor initializing an Ante 1 Small Blind.
     */
    public Blind() {
        this(BlindType.SMALL, 1);
    }

    /**
     * Constructor generating a standard blind scaled by Ante and BlindType.
     *
     * @param type the type of blind (Small, Big, Boss)
     * @param ante the ante number (must be >= 1)
     */
    public Blind(BlindType type, int ante) {
        this(
            (type != null) ? type.getDefaultName() : "Small Blind",
            (type != null) ? type : BlindType.SMALL,
            Math.max(1, ante),
            calculateTargetScore(ante, (type != null) ? type : BlindType.SMALL),
            (type != null) ? type.getDefaultReward() : 3,
            "Score at least " + calculateTargetScore(ante, (type != null) ? type : BlindType.SMALL) + " chips."
        );
    }

    /**
     * Minimal constructor for custom blinds.
     *
     * @param name display name of the blind
     * @param targetScore chips required to defeat the blind
     * @param reward money awarded upon clearing the blind
     */
    public Blind(String name, int targetScore, int reward) {
        this(name, BlindType.SMALL, 1, targetScore, reward, "Score at least " + targetScore + " chips.");
    }

    /**
     * Comprehensive constructor accepting all core fields.
     *
     * @param name display name of the blind
     * @param type the type classification
     * @param ante the ante number
     * @param targetScore chip goal required to beat the round
     * @param reward money reward paid on victory
     * @param description flavor or effect description
     */
    public Blind(String name, BlindType type, int ante, int targetScore, int reward, String description) {
        this.name = (name == null || name.trim().isEmpty()) ? "Unnamed Blind" : name;
        this.type = (type == null) ? BlindType.SMALL : type;
        this.ante = Math.max(1, ante);
        this.targetScore = Math.max(0, targetScore);
        this.reward = Math.max(0, reward);
        this.description = (description == null) ? "" : description;
        this.defeated = false;
        this.skipped = false;
    }

    /**
     * Standard Balatro base chip requirement curve per Ante (White Stake).
     *
     * @param ante the ante number
     * @return base chip requirement before blind-specific multipliers
     */
    public static int getBaseChipsForAnte(int ante) {
        if (ante <= 1) return 300;
        switch (ante) {
            case 2: return 800;
            case 3: return 2800;
            case 4: return 6000;
            case 5: return 11000;
            case 6: return 20000;
            case 7: return 35000;
            case 8: return 50000;
            default:
                // Exponential scaling for Endless mode (Ante > 8)
                double scaled = 50000.0 * Math.pow(2.2, ante - 8);
                return (int) Math.min(Integer.MAX_VALUE, Math.round(scaled));
        }
    }

    /**
     * Calculates the target chip requirement given an Ante and BlindType.
     *
     * @param ante the current ante
     * @param type the blind type
     * @return the calculated target score
     */
    public static int calculateTargetScore(int ante, BlindType type) {
        int base = getBaseChipsForAnte(ante);
        double mult = (type != null) ? type.getScoreMultiplier() : 1.0;
        return (int) Math.round(base * mult);
    }

    /**
     * Factory method for creating a Small Blind.
     *
     * @param ante the ante number
     * @return configured Small Blind instance
     */
    public static Blind createSmallBlind(int ante) {
        return new Blind(BlindType.SMALL, ante);
    }

    /**
     * Factory method for creating a Big Blind.
     *
     * @param ante the ante number
     * @return configured Big Blind instance
     */
    public static Blind createBigBlind(int ante) {
        return new Blind(BlindType.BIG, ante);
    }

    /**
     * Factory method for creating a Boss Blind.
     *
     * @param ante the ante number
     * @return configured Boss Blind instance
     */
    public static BossBlind createBossBlind(int ante) {
        return new BossBlind(ante);
    }

    /**
     * Checks if a given chip score beats this blind's target score.
     *
     * @param score the current accumulated round score
     * @return true if score meets or exceeds targetScore
     */
    public boolean isBeaten(int score) {
        return score >= targetScore;
    }

    /**
     * Evaluates round score. If target is met or exceeded, marks blind as defeated.
     *
     * @param score accumulated chips
     * @return true if successfully defeated, false otherwise
     */
    public boolean evaluateScore(int score) {
        if (isBeaten(score)) {
            this.defeated = true;
            return true;
        }
        return false;
    }

    /**
     * Determines whether this blind can be skipped.
     * In Balatro, Boss Blinds cannot be skipped.
     *
     * @return true if eligible for skipping
     */
    public boolean canSkip() {
        return !defeated && !skipped && type != BlindType.BOSS;
    }

    /**
     * Skips this blind if permissible.
     *
     * @return true if successfully skipped
     */
    public boolean skip() {
        if (canSkip()) {
            this.skipped = true;
            return true;
        }
        return false;
    }

    /**
     * Claims the prize money reward if the blind was defeated.
     *
     * @return the payout dollar amount, or 0 if not defeated
     */
    public int claimReward() {
        return defeated ? reward : 0;
    }

    /**
     * Resets the round state for this blind.
     */
    public void reset() {
        this.defeated = false;
        this.skipped = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = (name == null || name.trim().isEmpty()) ? "Unnamed Blind" : name;
    }

    public BlindType getType() {
        return type;
    }

    public void setType(BlindType type) {
        this.type = (type == null) ? BlindType.SMALL : type;
    }

    public int getAnte() {
        return ante;
    }

    public void setAnte(int ante) {
        this.ante = Math.max(1, ante);
    }

    public int getTargetScore() {
        return targetScore;
    }

    public void setTargetScore(int targetScore) {
        this.targetScore = Math.max(0, targetScore);
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = Math.max(0, reward);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = (description == null) ? "" : description;
    }

    public boolean isDefeated() {
        return defeated;
    }

    public void setDefeated(boolean defeated) {
        this.defeated = defeated;
    }

    public boolean isSkipped() {
        return skipped;
    }

    public void setSkipped(boolean skipped) {
        this.skipped = skipped;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Blind other = (Blind) obj;
        return ante == other.ante
                && targetScore == other.targetScore
                && reward == other.reward
                && defeated == other.defeated
                && skipped == other.skipped
                && type == other.type
                && Objects.equals(name, other.name)
                && Objects.equals(description, other.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, ante, targetScore, reward, description, defeated, skipped);
    }

    @Override
    public String toString() {
        String status = defeated ? " [DEFEATED]" : (skipped ? " [SKIPPED]" : "");
        return String.format("[%s (Ante %d)] %s | Goal: %,d Chips | Reward: $%d%s%n  -> %s",
                type, ante, name, targetScore, reward, status, description);
    }
}
