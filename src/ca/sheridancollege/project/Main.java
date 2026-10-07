/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ca.sheridancollege.project;

/**
 *
 * @author Marcus
 */
public class Main {

    public static void main(String[] args) {
        Card card = new PlayingCard(PlayingCard.Suit.SPADES, PlayingCard.Rank.ACE);
        Card joker = new JokerCard("Joker", "+4 Mult", 2, JokerCard.Rarity.COMMON, 4, 1.0, 0, null);

        System.out.println(card);
        System.out.println(joker);
    }
}