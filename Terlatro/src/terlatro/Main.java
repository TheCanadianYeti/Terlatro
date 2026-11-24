/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package terlatro;

/**
 *
 * @author Marcus
 */
public class Main {

    public static void main(String[] args) {
        
        /*
        //Refer to the FullscreenLauncher.java file for details. 
        i didnt make it, found it online. - Marcus Podnar
        */
        if (FullscreenLauncher.relaunchIfNeeded(args, Main.class)) {
            return;
        }
        
        Card card = new PlayingCard(PlayingCard.Suit.SPADES, PlayingCard.Rank.ACE);
        Card joker = new JokerCard(JokerCard.JokerType.JOKER);

        System.out.println(card);
        System.out.println(joker);

        // Blind and Boss Blind tests
        System.out.println("\n--- Blind Tests ---");
        Blind smallBlind = Blind.createSmallBlind(1);
        BossBlind bossBlind = new BossBlind(BossBlind.BossType.THE_CLUB, 1);
        PlayingCard clubCard = new PlayingCard(PlayingCard.Suit.CLUBS, PlayingCard.Rank.KING);

        System.out.println(smallBlind);
        System.out.println(bossBlind);

        if (card instanceof PlayingCard) {
            System.out.println("Is " + card.getName() + " debuffed by " + bossBlind.getName() + "? "
                    + bossBlind.isCardDebuffed((PlayingCard) card));
        }
        System.out.println("Is " + clubCard.getName() + " debuffed by " + bossBlind.getName() + "? "
                + bossBlind.isCardDebuffed(clubCard));
        System.out.println("Small Blind beaten with 350 chips? " + smallBlind.isBeaten(350));

        // GroupOfCards test (demonstrates null cards list)
        System.out.println("\n--- Deck Test ---");
        GroupOfCards deck = new GroupOfCards(52);
        deck.standardDeck();
    }
}