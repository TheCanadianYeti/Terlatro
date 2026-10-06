/**
 * SYST 17796 Project Base code.
 * Students can modify and extend to implement their game.
 * Add your name as an author and the date!
 */
package ca.sheridancollege.project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

/**
 * Base abstract Card class designed for Balatro card game implementations.
 * Provides core properties shared by all card entities (Playing Cards, Jokers, Tarots).
 *
 * @author Marcus Podnar
 */
public abstract class Card {

    // Unique display name of the card (e.g., "Ace of Spades", "Joker", "The Fool")
    private String name;

    // Rules text, trigger condition, or score effect description
    private String description;

    // Cost to buy the card from the shop in dollars
    private int cost;

    // Pointer to an external text file storing the ASCII art for the card face
    private File artFile;

    // Minimal constructor initializing card name with defaults for other fields
    public Card(String name) {
        this(name, "", 0, null);
    }

    // Standard constructor initializing card name, rules description, and base cost
    public Card(String name, String description, int cost) {
        this(name, description, cost, null);
    }

    // Comprehensive constructor accepting all core fields including the ASCII art file path
    public Card(String name, String description, int cost, String artFilePath) {
        // Enforce fallback value for missing or empty names
        this.name = (name == null || name.trim().isEmpty()) ? "Unnamed Card" : name;
        this.description = (description == null) ? "" : description;
        // Prevent negative dollar values for the base price
        this.cost = Math.max(0, cost);
        // Link ASCII art text file descriptor if path exists, otherwise leave null
        this.artFile = (artFilePath == null || artFilePath.trim().isEmpty()) ? null : new File(artFilePath);
    }

    // Returns card display name
    public String getName() {
        return name;
    }

    // Updates card display name with fallback protection
    public void setName(String name) {
        this.name = (name == null || name.trim().isEmpty()) ? "Unnamed Card" : name;
    }

    // Returns card rules or effect text
    public String getDescription() {
        return description;
    }

    // Updates card rules text
    public void setDescription(String description) {
        this.description = (description == null) ? "" : description;
    }

    // Returns base store purchase cost
    public int getCost() {
        return cost;
    }

    // Updates base store purchase cost, disallowing negative values
    public void setCost(int cost) {
        this.cost = Math.max(0, cost);
    }

    // Computes resale value based on standard Balatro economics: half purchase price, min 1
    public int getSellValue() {
        return Math.max(1, cost / 2);
    }

    // Retrieves ASCII art file reference
    public File getArtFile() {
        return artFile;
    }

    // Assigns ASCII art file reference directly
    public void setArtFile(File artFile) {
        this.artFile = artFile;
    }

    // Assigns ASCII art file reference using a path string
    public void setArtFilePath(String artFilePath) {
        this.artFile = (artFilePath == null || artFilePath.trim().isEmpty()) ? null : new File(artFilePath);
    }

    // Checks whether the ASCII art file is configured and exists on disk
    public boolean hasArtFile() {
        return artFile != null && artFile.exists();
    }

    // Reads and returns the raw ASCII art string from the text file
    public String loadAsciiArt() {
        if (!hasArtFile()) {
            return "[No ASCII Art Found]";
        }
        try {
            return Files.readString(artFile.toPath());
        } catch (IOException e) {
            return "[Error reading ASCII art: " + e.getMessage() + "]";
        }
    }

    // Checks equality across all core card attributes
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Card other = (Card) obj;
        return cost == other.cost 
                && Objects.equals(name, other.name) 
                && Objects.equals(description, other.description)
                && Objects.equals(artFile, other.artFile);
    }

    /**
     * Students should implement this method for their specific children classes
     */
    @Override
    public abstract String toString();

}

