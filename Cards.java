/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unogame;

/**
 *
 * @author PRIATA
 */
public class Cards {

    Cards(Colour validColour, Number validNumber) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    enum Number {
        Zero, One, Two, Three, Four, Five, Six, Seven, Eight, Nine, Skip, Reverse, DrawTwo, DrawFour, PowerCard ;
        private static final Number numberArr [] = Number.values();
        
        public static Number getNumber (int n) {
            return Number.numberArr[n];
        }
    }
    
     enum Colour {
        Green, Yellow, Red, Blue, PowerCard;
        private static final Colour colourArr [] = Colour.values();
        
        public static Colour getColour (int c) {
            return Colour.colourArr[c];
        }
    }
     
    private final Number number;
    private final Colour colour;
    
    public Cards (final Number number, final Colour colour){
        this.number = number;
        this.colour = colour;
    }
    
    public Number getNumber(){
        return this.number;
    }
    
    public Colour getColour(){
        return this.colour;
    }
    
    public String toString(){
        return colour + "_" + number;
    }
}
