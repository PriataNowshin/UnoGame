/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unogame;
import java.util.*;
import javax.swing.ImageIcon;
/**
 *
 * @author PRIATA
 */
public class Deck {
    
    private Cards[] cards;
    private int totalCards;
    
    public Deck(){
        cards = new Cards[108];   
        reset();

    }
    
    public void reset(){
        Cards.Colour colours [] = Cards.Colour.values();
        totalCards = 0;
        
        for (int i=0; i<colours.length-1; i++){
            Cards.Colour colour = colours[i];
            cards[totalCards++] = new Cards(Cards.Number.getNumber(0), colour);
            
            for (int j=1; j<10 ; j++){
                cards[totalCards++] = new Cards(Cards.Number.getNumber(j), colour);
                cards[totalCards++] = new Cards(Cards.Number.getNumber(j), colour);
            }
            
            Cards.Number numbers[] = new Cards.Number[]{Cards.Number.DrawTwo, Cards.Number.Skip, Cards.Number.Reverse};
            for (Cards.Number number : numbers){
                cards[totalCards++] = new Cards(number, colour);
                cards[totalCards++] = new Cards(number, colour);
            }      
        }
        
        Cards.Number numbers[] = new Cards.Number[]{Cards.Number.PowerCard, Cards.Number.DrawFour};
        for (Cards.Number number : numbers){
            for (int i=0; i<4; i++){
                cards[totalCards++] = new Cards(number, Cards.Colour.PowerCard);
            }
        }
    }
    
    public void replaceDeckWith(ArrayList<Cards> cards){
        this.cards = cards.toArray(new Cards[cards.size()]);
        this.totalCards = this.cards.length;
    }
    
    public boolean isEmpty(){
        return totalCards == 0;
    }
    
    public void shuffle(){
        int var = cards.length;
        Random random = new Random();
        
        for (int i=0; i<cards.length; i++){
            int randomvalue = i + random.nextInt(var-i);
            Cards randomCard = cards[randomvalue];
            cards[i] = randomCard;           
        }
    }
    
    public Cards drawCard() throws IllegalArgumentException{
        if (isEmpty()){
            throw new IllegalArgumentException("No cards in deck so can not draw any card");
        }
        return cards[--totalCards];
    }
    
    public ImageIcon drawCardImage()throws IllegalArgumentException{
        if (isEmpty()){
           throw new IllegalArgumentException("No cards in deck so can not draw any Image"); 
        }
        return new ImageIcon(cards[--totalCards].toString() +".png");
    }
    
    public Cards[] drawCard(int n) {
        if (n<0){
            throw new IllegalArgumentException("must draw positivecards but tries to draw "+ n +" cards"  );
        }
        if (n>totalCards){
            throw new IllegalArgumentException("can not draw "+ n +" cards as there are only "+ totalCards +" cards in deck" );
        }
        Cards [] r = new Cards[n];
        for (int i=0; i<n; i++){
            r[i] = cards[--totalCards];
        }
        return r;
    }
    
}
