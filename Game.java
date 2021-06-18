/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unogame;
import java.awt.Font;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
/**
 *
 * @author PRIATA
 */
public class Game {
    private int currentPlayer;
    private String [] playerID;
    private Deck deck;
    private ArrayList<ArrayList<Cards>> playerHand;
    private ArrayList<Cards> stockPile;
    
    private Cards.Colour validColour;
    private Cards.Number validNumber;
    
    boolean gameDirection;
    private Iterable<String> plaeyrID;
    
    public Game(String[] pid){
        deck = new Deck();
        deck.shuffle();
        stockPile = new ArrayList<Cards>();
        playerID = pid;
        currentPlayer = 0;
        gameDirection = false;
        playerHand = new ArrayList<ArrayList<Cards>>();
        
        for (int i=0 ; i<pid.length; i++){
            ArrayList<Cards> hand = new ArrayList<Cards>(Arrays.asList(deck.drawCard(7)));
            playerHand.add(hand);
        }
    }
    
    public void start(Game game){
        Cards card = deck.drawCard();
        validColour = card.getColour();
        validNumber = card.getNumber();
        
        if (card.getNumber() == Cards.Number.PowerCard){
            start(game);
        }
        
        if (card.getNumber() == Cards.Number.DrawFour || card.getNumber() == Cards.Number.DrawTwo){
            start(game);
        }
        
        if(card.getNumber() == Cards.Number.Skip){
            JLabel message = new JLabel(playerID[currentPlayer] + " was skipped");
            message.setFont(new Font("Arial", Font.BOLD, 48));
            JOptionPane.showMessageDialog(null, message);
            
            if(gameDirection == false){
                currentPlayer = (currentPlayer+1)% playerID.length;
            }else if(gameDirection == true){
                currentPlayer = (currentPlayer-1) % playerID.length;
                if(currentPlayer == -1){
                    currentPlayer = playerID.length - 1;
                }
            }
            //start(game);   
        }
        
        if(card.getNumber() == Cards.Number.Reverse){
            JLabel message = new JLabel(playerID[currentPlayer] + " the game direction changed");
            message.setFont(new Font("Arial", Font.BOLD, 48));
            JOptionPane.showMessageDialog(null, message);
            gameDirection ^= true;
            currentPlayer = playerID.length - 1;
            //start(game); 
        }
        
        stockPile.add(card);
    }
    
    public Cards getTopCard(){
        return new Cards(validColour, validNumber);
    }
    
    public ImageIcon getTopCardImage(){
        return new ImageIcon(validColour + "_" + validNumber +".png");
    }
    
    public boolean isGameOver(){
        for (String player: this.plaeyrID){
            if(hasEmptyHand(player)){
                return true;
            }
        }
        return false;
    }
    
    public String getCurrentPlayer(){
        return this.playerID[this.currentPlayer];
    }
    
    public String getPreviousPlayer(int i){
        int index = this.currentPlayer - i;
        if( index==-1){
            index = playerID.length -1;
        }
        return this.playerID[index];
    }
    
    public String[] getPlayer(){
        return playerID;
    }
    
    public ArrayList<Cards> getPlayerHand(String pid){
        int index = Arrays.asList(playerID).indexOf(pid);
        return playerHand.get(index);
    }
    
    public int getPlayerHandSize(String pid){
        return getPlayerHand(pid).size();
    }
    
    public Cards getPlayerCard(String pid, int choice){
        ArrayList<Cards> hand = getPlayerHand(pid);
        return hand.get(choice);
    }
    
    public boolean hasEmptyHand(String pid){
        return getPlayerHand(pid).isEmpty();
    }
    
    public boolean validCardPlay(Cards card){
        return card.getColour() == validColour || card.getNumber() == validNumber;
    }
    
    public void checkPlayerTurn(String pid) throws InvalidPlayerTurnException{
        if(this.playerID[this.currentPlayer] !=pid){
            throw new InvalidPlayerTurnException ("it is not "+ pid + " 's turn", pid);
        }
    }
    
    public void submitDraw(String pid)throws InvalidPlayerTurnException{
        checkPlayerTurn(pid);
        if(deck.isEmpty()){
            deck.replaceDeckWith(stockPile);
            deck.shuffle();
        }
        getPlayerHand(pid).add(deck.drawCard());
        if(gameDirection == false){
            currentPlayer = (currentPlayer +1) % playerID.length;
        }else if(gameDirection == true){
            currentPlayer = (currentPlayer -1) % playerID.length ;
            if (currentPlayer == -1){
                currentPlayer = playerID.length -1;
            }
        }
    }
    
    public void setCardColour(Cards.Colour colour) {
        validColour = colour;
    }
    
    public void submitPlayerCard(String pid, Cards card, Cards.Colour declaredColour)
        throws InvalidColourSubmissionException, InvalidNumberSubmissionException, InvalidPlayerTurnException{
            checkPlayerTurn(pid);
            
            ArrayList<Cards> pHand= getPlayerHand(pid);
            if(!validCardPlay(card)){
                if(card.getColour() == Cards.Colour.PowerCard){
                    validColour= card.getColour();
                    validNumber= card.getNumber();
                }
//                if(card.getColour() != validColour){
//                    JLabel message = new JLabel("invalid player move, expected colour: "+ validColour +" but the colour is "+ card.getColour());
//                    message.setFont(new Font("Arial", Font.BOLD, 48));
//                    JOptionPane.showMessageDialog(null,message);
//                    throw new InvalidColourSubmissionException("invalid player move, expected colour: "+ validColour +" but the colour is "+ card.getColour(), card.getColour(), validColour);
//                }else if (card.getNumber() != validNumber){
//                    JLabel message2 = new JLabel("invalid player move, expected number: "+ validNumber +" but the number is "+ card.getNumber());
//                    message2.setFont(new Font("Arial", Font.BOLD, 48));
//                    JOptionPane.showMessageDialog(null,message2);
//                    throw new InvalidNumberSubmissionException("invalid player move, expected number: "+ validNumber +" but the number is "+ card.getNumber(), card.getNumber(), validNumber);
//                }
                if(card.getColour() != validColour){
                    
                    if (card.getNumber() != validNumber){
                        JLabel message2 = new JLabel("invalid player move, expected number: "+ validNumber +" but the number is "+ card.getNumber());
                        message2.setFont(new Font("Arial", Font.BOLD, 48));
                        JOptionPane.showMessageDialog(null,message2);
                        throw new InvalidNumberSubmissionException("invalid player move, expected number: "+ validNumber +" but the number is "+ card.getNumber(), card.getNumber(), validNumber);
                
                    }
                    throw new InvalidColourSubmissionException("invalid player move, expected colour: "+ validColour +" but the colour is "+ card.getColour(), card.getColour(), validColour);
                }

            }
            
            pHand.remove(card);
            if(hasEmptyHand(this.playerID[currentPlayer])){
                JLabel message = new JLabel(this.playerID[currentPlayer] +" won the game! Thank you for playing");
                message.setFont(new Font("Arial", Font.BOLD, 48));
                JOptionPane.showMessageDialog(null,message);
                System.exit(0);
            }
            
            validColour = card.getColour();
            validNumber = card.getNumber();
            stockPile.add(card);
            
            if( gameDirection == false){
                currentPlayer = (currentPlayer + 1) % playerID.length;
            }else if(gameDirection == true){
                currentPlayer = (currentPlayer - 1) % playerID.length;
                if(currentPlayer == -1){
                    currentPlayer = playerID.length - 1;
                }
            }
            
            if(card.getColour() == Cards.Colour.PowerCard){
                validColour= declaredColour;
            }
            
            if(card.getNumber() == Cards.Number.DrawTwo){
                pid = playerID[currentPlayer];
                getPlayerHand(pid).add(deck.drawCard());
                getPlayerHand(pid).add(deck.drawCard());
                JLabel message = new JLabel(pid + " drew 2 cards");
                message.setFont(new Font("Arial", Font.BOLD, 48));
                JOptionPane.showMessageDialog(null,message);
            }
            
            if(card.getNumber() == Cards.Number.DrawFour){
                pid = playerID[currentPlayer];
                getPlayerHand(pid).add(deck.drawCard());
                getPlayerHand(pid).add(deck.drawCard());
                getPlayerHand(pid).add(deck.drawCard());
                getPlayerHand(pid).add(deck.drawCard());
                JLabel message = new JLabel(pid + " drew 4 cards");
                message.setFont(new Font("Arial", Font.BOLD, 48));
                JOptionPane.showMessageDialog(null,message);
            }
            
            if (card.getNumber() == Cards.Number.Skip){
                JLabel message = new JLabel(playerID[currentPlayer]+ " was skipped!");
                message.setFont(new Font("Arial", Font.BOLD, 48));
                JOptionPane.showMessageDialog(null,message);
                
                if( gameDirection == false){
                currentPlayer = (currentPlayer + 1) % playerID.length;
                }else if(gameDirection == true){
                currentPlayer = (currentPlayer - 1) % playerID.length;
                    if(currentPlayer == -1){
                        currentPlayer = playerID.length - 1;
                    }
                }
            }
            
            if(card.getNumber()== Cards.Number.Reverse){
                JLabel message = new JLabel(pid+ " changed the game direction!");
                message.setFont(new Font("Arial", Font.BOLD, 48));
                JOptionPane.showMessageDialog(null,message);
                
                gameDirection ^= true;
                if (gameDirection == true){
                    currentPlayer = (currentPlayer - 2) % playerID.length;
                    if(currentPlayer == -1){
                        currentPlayer = playerID.length - 1;
                    }
                    if(currentPlayer == -2){
                        currentPlayer = playerID.length - 2;
                    }
                }else if ( gameDirection == false){
                currentPlayer = (currentPlayer + 2) % playerID.length;
                }
            }
        
    }
}



class InvalidPlayerTurnException extends Exception{
    String playerID;
    
    public InvalidPlayerTurnException(String message, String pid){
        super(message);
        playerID = pid;
    }
    
    public String getPid(){
        return playerID;
    }
}

class InvalidColourSubmissionException extends Exception{
    private Cards.Colour expected;
    private Cards.Colour actual;
    
    public InvalidColourSubmissionException(String message, Cards.Colour actual, Cards.Colour expected){
        this.actual = actual;
        this.expected = expected;
    }
}

class InvalidNumberSubmissionException extends Exception{
    private Cards.Number expected;
    private Cards.Number actual;
    
    public InvalidNumberSubmissionException(String message, Cards.Number actual, Cards.Number expected){
        this.actual = actual;
        this.expected = expected;
    }
}