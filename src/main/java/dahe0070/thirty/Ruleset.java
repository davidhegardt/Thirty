package dahe0070.thirty;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by David Hegardt on 2017-06-20.
 * Model class used to handle the scoring and keeping
 * track of turns throws and dice also spinner items used for scoring.
 * This handles the game rules and keeps tracks of users progress in the game.
 * Ruleset object can be instantiated to create a new player.
 */

public class Ruleset implements Serializable {

    private int turn;                                                     // The current turn
    private int throwNr;                                                     // The current throw
    private ArrayList<Dice> savedDiceList = new ArrayList<>();
    private HashMap<String,Integer> scoreHash = new HashMap<>();
    private ArrayList<String> comboList = new ArrayList<>();

    public Ruleset() {
        this.turn = 1;
        this.throwNr = 0;
        setupMap();
        setupSpinnerItems();
    }

    /**
     * Sets up hashkeys and values for all combinations to be used
     */
    private void setupMap(){
        scoreHash.put("LOW",0);
        for (int i = 4; i < 13;i++ ){
            scoreHash.put("Combo"+i,0);
        }
    }

    /**
     * Setup the combination choices to be used in the spinner
     * @return the list of spinner items
     */
    private ArrayList setupSpinnerItems(){
        comboList.add("Select a combination to use");
        comboList.add("LOW");
        for (int i = 4; i < 13; i++){
            comboList.add("All combinations of " + i);
        }

        return comboList;
    }

    public ArrayList getSpinnerItems(){
        return comboList;
    }

    public void removeSpinnerItem(int index){
        comboList.remove(index);
    }

    /**
     * Updates the hashmap
     * @param key combination
     * @param value score for combination
     */
    public void updateMap(String key,int value){
        scoreHash.put(key,value);

    }

    public HashMap<String, Integer> getHashMap(){
        return scoreHash;
    }

    public void addDice(Dice newDice) {
        savedDiceList.add(newDice);
    }                       // Add dice to saved list of dice

    public void clearDiceList() {savedDiceList.clear();}

    public void incThrows() {
        throwNr++;
    }                                           // Increase counters

    public void incTurn() {
        turn++;
    }

    public void setThrowNr(int value) {
        this.throwNr = value;
    }

    public int getThrowNr() {
        return throwNr;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int nr) {this.turn = nr;}

    /**
     * Function to retrieve values of the saved dice list
     * @return int array of dice values
     */
    public int[] getDiceValues() {
        int[] diceValues = new int[savedDiceList.size()];
        for (int i = 1; i < diceValues.length; i++){
            diceValues[i] = savedDiceList.get(i).getValue();

        }


        return diceValues;
    }

    /**
     * Function to retrieve the Dice objects of the saved dice list
     * @return Dice array of the dices
     */
    public Dice[] getDiceArray() {
        Dice[] diceArray = new Dice[savedDiceList.size()];
        for (int i = 1; i < diceArray.length; i++){
            diceArray[i] = savedDiceList.get(i);

        }


        return diceArray;
    }

}
