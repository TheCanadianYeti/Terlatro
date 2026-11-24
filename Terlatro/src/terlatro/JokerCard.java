package terlatro;

import java.util.Objects;
import java.util.Random;

/**
 * Concrete child class representing a Joker card in Balatro.
 * Tracks rarity, flat mult additions, multiplicative multipliers, and chip bonuses.
 * Supports preset Joker types similar to BossBlind.
 *
 * @author Marcus Podnar
 */
public class JokerCard extends Card {

    public enum Rarity {
        COMMON, UNCOMMON, RARE, LEGENDARY
    }

    /**
     * Enumeration of standard Balatro Joker cards with default stats.
     */
    public enum JokerType {
        JOKER("Joker", "+4 Mult", 2, Rarity.COMMON, 4, 1.0, 0),
        GREEDY_JOKER("Greedy Joker", "Played cards with Diamond suit give +3 Mult when scored", 5, Rarity.COMMON, 3, 1.0, 0),
        LUSTY_JOKER("Lusty Joker", "Played cards with Heart suit give +3 Mult when scored", 5, Rarity.COMMON, 3, 1.0, 0),
        WRATHFUL_JOKER("Wrathful Joker", "Played cards with Spade suit give +3 Mult when scored", 5, Rarity.COMMON, 3, 1.0, 0),
        GLUTTONOUS_JOKER("Gluttonous Joker", "Played cards with Club suit give +3 Mult when scored", 5, Rarity.COMMON, 3, 1.0, 0),
        HALF_JOKER("Half Joker", "+20 Mult if played hand contains 3 or fewer cards", 5, Rarity.COMMON, 20, 1.0, 0),
        BANNER("Banner", "+30 Chips for each remaining discard", 5, Rarity.COMMON, 0, 1.0, 30),
        THE_DUO("The Duo", "X2 Mult if played hand contains a Pair", 8, Rarity.RARE, 0, 2.0, 0);

        private final String defaultName;
        private final String defaultDescription;
        private final int defaultCost;
        private final Rarity rarity;
        private final int multBonus;
        private final double xMult;
        private final int chipBonus;

        JokerType(String defaultName, String defaultDescription, int defaultCost, Rarity rarity, int multBonus, double xMult, int chipBonus) {
            this.defaultName = defaultName;
            this.defaultDescription = defaultDescription;
            this.defaultCost = defaultCost;
            this.rarity = rarity;
            this.multBonus = multBonus;
            this.xMult = xMult;
            this.chipBonus = chipBonus;
        }

        public String getDefaultName() {
            return defaultName;
        }

        public String getDefaultDescription() {
            return defaultDescription;
        }

        public int getDefaultCost() {
            return defaultCost;
        }

        public Rarity getRarity() {
            return rarity;
        }

        public int getMultBonus() {
            return multBonus;
        }

        public double getXMult() {
            return xMult;
        }

        public int getChipBonus() {
            return chipBonus;
        }
    }

    private static final Random RNG = new Random();

    private JokerType jokerType;
    private Rarity rarity;
    private int multBonus;
    private double xMult;
    private int chipBonus;

    /**
     * Default constructor creating the standard common Joker (+4 Mult).
     */
    public JokerCard() {
        this(JokerType.JOKER);
    }

    /**
     * Creates a JokerCard from a preset JokerType.
     *
     * @param jokerType the preset joker type
     */
    public JokerCard(JokerType jokerType) {
        this(jokerType, null);
    }

    /**
     * Creates a JokerCard from a preset JokerType with a specified ASCII art file.
     *
     * @param jokerType the preset joker type
     * @param artFilePath file path for ASCII art
     */
    public JokerCard(JokerType jokerType, String artFilePath) {
        super(
            (jokerType != null) ? jokerType.getDefaultName() : JokerType.JOKER.getDefaultName(),
            (jokerType != null) ? jokerType.getDefaultDescription() : JokerType.JOKER.getDefaultDescription(),
            (jokerType != null) ? jokerType.getDefaultCost() : JokerType.JOKER.getDefaultCost(),
            artFilePath
        );
        this.jokerType = (jokerType != null) ? jokerType : JokerType.JOKER;
        this.rarity = this.jokerType.getRarity();
        this.multBonus = this.jokerType.getMultBonus();
        this.xMult = this.jokerType.getXMult();
        this.chipBonus = this.jokerType.getChipBonus();
    }

    /**
     * Comprehensive custom constructor accepting all card and Joker fields.
     */
    public JokerCard(String name, String description, int cost, Rarity rarity, int multBonus, double xMult, int chipBonus, String artFilePath) {
        super(name, description, cost, artFilePath);
        this.jokerType = null;
        this.rarity = (rarity == null) ? Rarity.COMMON : rarity;
        this.multBonus = Math.max(0, multBonus);
        this.xMult = Math.max(1.0, xMult);
        this.chipBonus = Math.max(0, chipBonus);
    }

    /**
     * Factory method creating a JokerCard from a preset JokerType.
     *
     * @param type preset joker type
     * @return newly created JokerCard instance
     */
    public static JokerCard createJoker(JokerType type) {
        return new JokerCard(type);
    }

    /**
     * Factory method creating a random JokerCard from available presets.
     *
     * @return newly created random JokerCard instance
     */
    public static JokerCard createRandomJoker() {
        JokerType[] values = JokerType.values();
        return new JokerCard(values[RNG.nextInt(values.length)]);
    }

    public JokerType getJokerType() {
        return jokerType;
    }

    public void setJokerType(JokerType jokerType) {
        this.jokerType = jokerType;
        if (jokerType != null) {
            setName(jokerType.getDefaultName());
            setDescription(jokerType.getDefaultDescription());
            setCost(jokerType.getDefaultCost());
            this.rarity = jokerType.getRarity();
            this.multBonus = jokerType.getMultBonus();
            this.xMult = jokerType.getXMult();
            this.chipBonus = jokerType.getChipBonus();
        }
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = (rarity == null) ? Rarity.COMMON : rarity;
    }

    public int getMultBonus() {
        return multBonus;
    }

    public void setMultBonus(int multBonus) {
        this.multBonus = Math.max(0, multBonus);
    }

    public double getXMult() {
        return xMult;
    }

    public void setXMult(double xMult) {
        this.xMult = Math.max(1.0, xMult);
    }

    public int getChipBonus() {
        return chipBonus;
    }

    public void setChipBonus(int chipBonus) {
        this.chipBonus = Math.max(0, chipBonus);
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        JokerCard other = (JokerCard) obj;
        return multBonus == other.multBonus 
                && Double.compare(other.xMult, xMult) == 0 
                && chipBonus == other.chipBonus 
                && rarity == other.rarity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rarity, multBonus, xMult, chipBonus);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(rarity).append(" Joker] ").append(getName());
        sb.append(" | ").append(getDescription());
        sb.append(" (Cost: $").append(getCost()).append(", Sell: $").append(getSellValue()).append(")");
        return sb.toString();
    }
}
