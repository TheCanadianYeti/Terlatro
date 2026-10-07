package ca.sheridancollege.project;

import java.util.Objects;

/**
 * Concrete child class representing a Joker card in Balatro.
 * Tracks rarity, flat mult additions, multiplicative multipliers, and chip bonuses.
 *
 * @author Marcus Podnar
 */
public class JokerCard extends Card {

    public enum Rarity {
        COMMON, UNCOMMON, RARE, LEGENDARY
    }

    private Rarity rarity;
    private int multBonus;
    private double xMult;
    private int chipBonus;

   public JokerCard(String name, String description, int cost, Rarity rarity, int multBonus, double xMult, int chipBonus, String artFilePath) {
    super(name, description, cost, artFilePath);
    this.rarity = (rarity == null) ? Rarity.COMMON : rarity;
    this.multBonus = Math.max(0, multBonus);
    this.xMult = Math.max(1.0, xMult);
    this.chipBonus = Math.max(0, chipBonus);
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(rarity).append(" Joker] ").append(getName());
        sb.append(" | ").append(getDescription());
        sb.append(" (Cost: $").append(getCost()).append(", Sell: $").append(getSellValue()).append(")");
        return sb.toString();
    }
}