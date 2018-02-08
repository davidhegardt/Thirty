package dahe0070.thirty;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ScoreActivity class - handles the calculation of the score
 * Done by the user choosing a combination and which dice to use
 * for that combination. Does not directly handle the UI components,
 * but calls methods in the fragment-class to handle user I/O.
 */
public class ScoreActivity extends AppCompatActivity {

    private Ruleset scoring;                    // Rule object to handle score and game-logic
    private int[] diceValues;                   // values of the thrown dices
    private Dice[] diceArray;                   // Dice objects of thrown dices

    private ArrayList<Dice> scoreList = new ArrayList<>();          // List of selected dices, used to calculate score
    private ArrayList<Integer> hiddenDice = new ArrayList<>();          // List of dice that should be hidden on rotation
    private String thisPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);                            // Set the view
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);             // Setup the toolbar
        setSupportActionBar(toolbar);

        setTitle(getString(R.string.title_count_score));

        Intent intent = getIntent();
        scoring = (Ruleset) intent.getSerializableExtra(getString(R.string.Rules));           // Retrieve the updated components from MainActivity
        thisPlayer = intent.getStringExtra(getString(R.string.PlayerName));

        diceValues = scoring.getDiceValues();                               // Retrieve thrown values and the dice
        diceArray = scoring.getDiceArray();

        for (int i = 1; i < diceArray.length; i++){
            diceArray[i].setSave(false);                                    // If user previously choosed to save dice, set to unsaved
        }

        FragmentManager fragmentManager = getSupportFragmentManager();          // Retrieves reference to the fragment-class
        ScoreActivityFragment scoreFragment = (ScoreActivityFragment) fragmentManager.findFragmentById(R.id.score_fragment);

        scoreFragment.setupDices();                                         // Call function to setup the UI with correct dice values
    }

    /**
     * Sets the toolbar menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_score, menu);

        return true;
    }

    /**
     * Handle click events in the toolbar menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.help_menu:
                helpDialog();                                                   // Show instructions for scoring
                break;
        }

        return true;
    }

    public String getThisPlayer() {return thisPlayer;}

    public int getDiceValue(int diceNr) {
        return diceValues[diceNr];
    }

    public ArrayList getSpinner(){
        return scoring.getSpinnerItems();
    }

    /**
     * Show an alertDialog explaining how to choose combination and dices to calculate score
     */
    private void helpDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(ScoreActivity.this).create();
        alertDialog.setTitle(getString(R.string.Help));
        alertDialog.setMessage(getString(R.string.help_text));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    /**
     * This function is called when user presses the finish button - function is calle from fragment-class
     * @param comboIndex    Used to determine which combination to set score on
     * @param totalScore    The calculated total score for all matching combinations
     * @param spinnerPos    Position in the spinner that should be removed - prevents user from using the same combination twice
     */
    public void onFinish(int comboIndex, int totalScore, int spinnerPos){
        scoring.removeSpinnerItem(spinnerPos);
        if (comboIndex == 88){                          // To handle the LOW combination
            scoring.updateMap("LOW",totalScore);
        } else {
            scoring.updateMap("Combo" + comboIndex, totalScore);    // Update hashmap values with the new score
        }
        scoreList.clear();                              // Clear the list of choosen combinations
        Intent returnIntent = new Intent();
        returnIntent.putExtra(getString(R.string.ResRules),scoring);      // Return the updated ruleset to main-activity
        setResult(Activity.RESULT_OK,returnIntent);
        finish();                                       // Sets this activity to run in background and go back to MainActivity
    }


    /**
     * Function used when user presses a die in the fragment
     * @param choosenID position of the choosen die
     */
    public void countDice(int choosenID) {

        diceArray[choosenID].toggleSave();                  // Change save status for the choosen die

        updateScore(diceArray[choosenID]);                  // add to the score array

    }

    /**
     * Adds the dice to array of combination dices
     * @param value the dice users wants to use in this combination
     */
    private void updateScore(Dice value) {
        scoreList.add(value);
        for (Dice n : scoreList){

        }
    }

    public ArrayList getScore() {
        return scoreList;
    }

    public void resetScore() {
        scoreList.clear();
    }


    /**
     * Displays an error message if user choosen combination does not match the score
     * Called from fragment-class.
     */
    public void showError(){
        Toast.makeText(getBaseContext(), R.string.combination_error, Toast.LENGTH_SHORT).show();
    }

    /**
     * Function handles score calculation and interacts with the fragment class.
     * Sums up the values in the combination array, if there is a match the score is added to the total score
     * @param scoreOn   The choosen combination to match
     * @return
     */
    public int calcScore(int scoreOn) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        ScoreActivityFragment scoreFragment = (ScoreActivityFragment) fragmentManager.findFragmentById(R.id.score_fragment);

        int sum = 0;

        if (scoreOn == 88){                     // Checks if the LOW combination is choosed
            for (Dice i : scoreList){
                if (i.getValue() < 4){
                    sum += i.getValue();        // IF the values is under 4, add the value to the sum

                } else {
                    return 0;                   // If a larger value die is chosen, abort
                }
            }
            for (int i = 1 ; i < diceArray.length;i++) {
                if (diceArray[i].saveStatus()) {
                    scoreFragment.hideDice(i);              // Hide the dice that has already been used
                    hiddenDice.add(i);                      // Add to list of dice that should be hidden on rotation
                }
            }

            return sum;
        }

        for (Dice i : scoreList) {
            sum += i.getValue();                            // If the low combination is not used, just summarize the choosen values

        }


        if (sum == scoreOn) {                                       // If the values sum up to the combination, proceed to return this sum
            for (int i = 1 ; i < diceArray.length;i++){
                if (diceArray[i].saveStatus()){
                    scoreFragment.hideDice(i);                      // Since the dice match up and is used up, hide dice so user cannot cheat
                }
            }
            return sum;

        } else {                                            // If user has choosen an invalid combination, remove the save-status and reset the
                                                            // combination score array and have user try again.
            for (int i = 1; i < diceArray.length;i++){
                diceArray[i].setSave(false);
            }
            //scoreFragment.changeColor();
            resetScore();
            return 0;
        }

    }

    /**
     * Function used on rotation - if roll has not been made all dice should be hidden after rotation
     */
    public void rotateHide(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        ScoreActivityFragment scoreFragment = (ScoreActivityFragment) fragmentManager.findFragmentById(R.id.score_fragment);

        for (int i : hiddenDice){
            scoreFragment.hideDice(i);
        }
    }

    /**
     * Retrieve a small image Resid for the dice - if the dice is saved one imagelink should be used.
     * If the dice is not saved, the default colored dice should be used.
     * Used by fragment-class to set the correct image depending on user click
     * @param dieNumber die that should change image
     * @return  link ID of the image to be displayed
     */
    public int getDiceSmallImage(int dieNumber) {

        int resId = 0;
        resId = getResources().getIdentifier(diceArray[dieNumber].smallImageLink(), "drawable",getPackageName());       // Correct image is handled by the Dice - object

        return resId;
    }

    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        ScoreActivityFragment scoreFragment = (ScoreActivityFragment) fragmentManager.findFragmentById(R.id.score_fragment);

        if (scoreFragment.isComboSelected()) {
            Toast.makeText(this,"Use the finish button to complete game",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"Select a combination from the top menu",Toast.LENGTH_SHORT).show();
        }

    }
}
