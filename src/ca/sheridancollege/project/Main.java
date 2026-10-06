/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ca.sheridancollege.project;

/**
 *
 * @author marcu
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Test Code for the Implementation of the Card Parent Class and the JokerCard/PlayingCard Child Classes
        Card card = new PlayingCard(PlayingCard.Suit.SPADES, PlayingCard.Rank.ACE);
        Card joker = new JokerCard("Joker", "+4 Mult", 2, JokerCard.Rarity.COMMON, 4, 1.0, 0);

        System.out.println(card);
        System.out.println(joker);
    }
    
}
