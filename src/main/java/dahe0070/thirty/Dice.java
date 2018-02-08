package dahe0070.thirty;

import java.io.Serializable;

/**
 * Created by David Hegardt on 2017-06-19.
 * Class representing Dice object used in game.
 *  Model class. A Dice represents a real D6 dice.
 *  Values of 1 - 6 can be assigned as value.
 *  Dice uses save status to track if dice should be used or not.
 *  Dice object also keeps track of which image-link should be used
 *  corresponding to the value, also has a gred out (choosen) version of the image.
 */

public class Dice implements Serializable {

    private int id;
    private int value;                              // The value of the die thrown
    private boolean save;                           // If dice should be saved / counted in final score

    public Dice(int setValue){
        this.value = setValue;
        this.save = false;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int currId){
        this.id = currId;
    }

    public int getValue(){
        return this.value;
    }

    public void setValue(int currVal) {
        this.value = currVal;
    }

    public void toggleSave() {                          // toggle the save status
        if (!save) {
            save = true;
        } else {
            save = false;
        }
    }

    public void setSave(boolean choice) {
        this.save = choice;
    }

    public boolean saveStatus(){
        return save;
    }

    /**
     * Function to retrieve the correct image based on die-value.
     * Retrieve grey-image if dice is saved
     * @return filename for dice
     */
    public String imageLink(){
        String imgLink = "";
            switch (value) {
                case 0 :
                    break;
                case 1 :
                    if (!save) {
                        imgLink = "die1";
                    } else {
                        imgLink = "die1_save";
                    }
                    break;
                case 2 :
                    if (!save) {
                        imgLink = "die2";
                    } else {
                        imgLink = "die2_save";
                    }
                    break;
                case 3 :
                    if (!save) {
                        imgLink = "die3";
                    } else {
                        imgLink = "die3_save";
                    }
                    break;
                case 4 :
                    if (!save) {
                        imgLink = "die4";
                    } else {
                        imgLink = "die4_save";
                    }
                    break;
                case 5 :
                    if (!save) {
                        imgLink = "die5";
                    } else {
                        imgLink = "die5_save";
                    }
                    break;
                case 6 :
                    if (!save) {
                        imgLink = "die6";
                    } else {
                        imgLink = "die6_save";
                    }
                    break;
                default: imgLink = "blank.png";
                    break;
            }
        return imgLink;
    }

    /**
     * Function to retrieve correct small image based on value
     * Retrieve grey-image if dice is saved
     * @return filename for small dice
     */
    public String smallImageLink() {
        String imgLink = "";
        switch (value) {
            case 0 :
                break;
            case 1 :
                if (!save) {
                    imgLink = "die1_small";
                } else {
                    imgLink = "die1_save_small";
                }
                break;
            case 2 :
                if (!save) {
                    imgLink = "die2_small";
                } else {
                    imgLink = "die2_save_small";
                }
                break;
            case 3 :
                if (!save) {
                    imgLink = "die3_small";
                } else {
                    imgLink = "die3_save_small";
                }
                break;
            case 4 :
                if (!save) {
                    imgLink = "die4_small";
                } else {
                    imgLink = "die4_save_small";
                }
                break;
            case 5 :
                if (!save) {
                    imgLink = "die5_small";
                } else {
                    imgLink = "die5_save_small";
                }
                break;
            case 6 :
                if (!save) {
                    imgLink = "die6_small";
                } else {
                    imgLink = "die6_save_small";
                }
                break;
            default: imgLink = "blank.png";
                break;
        }
        return imgLink;
    }


}
