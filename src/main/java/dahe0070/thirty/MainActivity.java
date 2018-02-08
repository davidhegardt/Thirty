package dahe0070.thirty;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * David Hegardt 2017-07-02
 * Main activity is the controller class
 * handling logic for the dice throw.
 * Does not interact with UI elements directly, uses fragment for this.
 * MainActivity let's the user throw dice and choose which dice to save for scoring.
 * User can throw all the dice up to 3 times.
 */

public class MainActivity extends AppCompatActivity {

    private Dice[] diceArray = new Dice[7];             // Array of Dice objects to be used in the game
    private MediaPlayer mediaPlayer;                    // Used to play sound of rolling dice
    private Ruleset player1Rules;                          // Game logic and rules class
    private Ruleset player2Rules;                           // Rule class for player2
    private int playerCount;
    private int currPlayer = 1;
    public boolean multiplayer = false;
    private String playerOneName;
    private String playerTwoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        playerCount = intent.getIntExtra(getString(R.string.PlayerNr),0);

        if (playerCount == 1){
            playerOneName = intent.getStringExtra(getString(R.string.player_one));
        } else if (playerCount == 2){
            multiplayer = true;
            playerOneName = intent.getStringExtra(getString(R.string.player_one));
            playerTwoName = intent.getStringExtra(getString(R.string.player_two));
        }

        FragmentManager fragmentManager = getSupportFragmentManager();                                          // Retrieve the fragment to be used for UI-interaction
        PlayFragment playFragment = (PlayFragment) fragmentManager.findFragmentById(R.id.play_fragment);

        if (savedInstanceState != null) {
            playFragment = (PlayFragment) fragmentManager.getFragment(savedInstanceState, getString(R.string.playfragment));      // Retrieve the fragment in case onCreate() is called when already setup
        } else {
            playFragment.switchScoreBtn(false);
            playFragment.switchRoll(true);
            player1Rules = new Ruleset();                                                              // Setup a new rule-object on creation
            if (multiplayer){
                player2Rules = new Ruleset();
                player2Rules.setTurn(0);

            }
        }

    }

    public String getCurrPlayer() {
        if (currPlayer == 1) {
            return playerOneName;
        } else {
            return playerTwoName;
        }
    }

    public int throwNumber() {
        if (currPlayer == 1) {
            return player1Rules.getThrowNr();
        } else {
            return player2Rules.getThrowNr();
        }
    }

    public int turnNumber() {

        if (currPlayer == 1) {
            return player1Rules.getTurn();
        } else {
            return player2Rules.getTurn();
        }
    }

    private void rollAllDice(){

        Random random = new Random(System.currentTimeMillis());

        for (int i = 0; i < diceArray.length; i++) {                                            // If the number of throws is 1 - 3 throw new dice
            if (!isSaved(i)) {                                                                  // If user has choosed to save a dice, do not create new dice
                diceArray[i] = new Dice(random.nextInt(6) + 1);
            }
        }
    }

    /**
     * Function to handle logic when the roll dice button is pressed.
     * Create new random value with time-seed and create a new Dice object and add to the array.
     * Display logic is handled in the fragment-class.
     */
    public void throwDice() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        PlayFragment playFragment = (PlayFragment) fragmentManager.findFragmentById(R.id.play_fragment);        // Get reference to the fragment-class

        if (currPlayer == 1) {
            if (player1Rules.getThrowNr() < 4) {
                rollAllDice();
                player1Rules.incThrows();                                                                    // increase number of throws
                playFragment.switchScoreBtn(true);
            }
            if (player1Rules.getThrowNr() == 3) {                                                            // Once the turn is finished, put the final dice into the array
                playFragment.switchRoll(false);                                     // Disable roll button
                playFragment.switchScoreBtn(true);                                  // Enable button to calculate score

            }

        }
        else if (currPlayer == 2) {
            if (player2Rules.getThrowNr() < 4) {
                rollAllDice();
                player2Rules.getThrowNr();                                                                    // increase number of throws
                playFragment.switchScoreBtn(true);
            }
            if (player2Rules.getThrowNr() == 3) {                                                            // Once the turn is finished, put the final dice into the array
                playFragment.switchRoll(false);                                     // Disable roll button
                playFragment.switchScoreBtn(true);                                  // Enable button to calculate score

            }
        }

    }

    private void sumTheDice(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        PlayFragment playFragment = (PlayFragment) fragmentManager.findFragmentById(R.id.play_fragment);        // Get reference to the fragment-class

        if (currPlayer == 1){
            for (Dice n : diceArray) {                                                               // For calculation in Score-activity
                player1Rules.addDice(n);
            }

            playFragment.switchRoll(false);                                     // Disable roll button
        } else if (currPlayer == 2){
            for (Dice n : diceArray) {                                                               // For calculation in Score-activity
                player1Rules.addDice(n);
            }

            playFragment.switchRoll(false);                                     // Disable roll button
        }
    }

    /**
     * Function called when user presses the finish button.
     * Start new activity to calculate the score
     */
    public void scoreTable() {
        sumTheDice();
        Intent gameScore = new Intent(this,ScoreActivity.class);
        if (currPlayer == 1) {
            gameScore.putExtra(getString(R.string.Rules), player1Rules);                  // Send the ruleset to the new activity
            gameScore.putExtra(getString(R.string.CurrPlayer),currPlayer);
            gameScore.putExtra(getString(R.string.PlayerName),playerOneName);
        } else if (currPlayer == 2){
            gameScore.putExtra(getString(R.string.Rules),player2Rules);
            gameScore.putExtra(getString(R.string.CurrPlayer),currPlayer);
            gameScore.putExtra(getString(R.string.PlayerName),playerTwoName);
        }

        startActivityForResult(gameScore, 1);

        //gameRules.calcFours();
    }

    /**
     * Used when user returns from the Score-activity. Retrieve the updated ruleset and scoring.
     * Also reset some paramaters to be ready for a new round.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                //currPlayer = data.getIntExtra("CurrPlayer",0);
                if (currPlayer == 1) {
                    player1Rules = (Ruleset) data.getSerializableExtra(getString(R.string.ResRules));            // Retrieve the updated ruleset
                } else if (currPlayer == 2){
                    player2Rules = (Ruleset) data.getSerializableExtra(getString(R.string.ResRules));
                }

                diceArray = new Dice[7];                                                // reset dice to be thrown
                nextTurn();                                                             // call function to update turn

            }
            if (requestCode == Activity.RESULT_CANCELED){

            }
        }
    }

    /**
     * Function called to update and reset the display when a turn is over and returned
     * from Score-activity
     */
    public void nextTurn() {
        if (multiplayer){
            if (currPlayer == 2 && player2Rules.getTurn() == 10){     // Check if player 2 has finished the last round
                startResultActivity();              // All combinations used, end game
            }
        }

        if (multiplayer) {                          // Switch player to the next player
            if (currPlayer == 1) {
                currPlayer = 2;
            } else if (currPlayer == 2) {
                currPlayer = 1;
            }
        }

        if (currPlayer == 1) {
            player1Rules.incTurn();                      // Inc turn counter
            player1Rules.setThrowNr(0);                     // Reset number of throws for user
            player1Rules.clearDiceList();                  // clear the list of saved dice
        } else if (currPlayer == 2){
            player2Rules.incTurn();
            player2Rules.setThrowNr(0);
            player2Rules.clearDiceList();
        }

        // If playing singleplayer, just check if player one has finished all combinations
        if(!multiplayer){
            if(player1Rules.getTurn() == 11){
                startResultActivity();
            }
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        PlayFragment playFragment = (PlayFragment) fragmentManager.findFragmentById(R.id.play_fragment);

        playFragment.updateView();                  // Update UI for the new turn

    }

    public void saveDice(int choosenID) {
        diceArray[choosenID].toggleSave();
    }

    /**
     * Check if a dice is saved
     * @param choosenID id of the dice in the array
     * @return if saved or not
     */
    public boolean isSaved(int choosenID) {
        if (diceArray[choosenID] != null){
            if (!diceArray[choosenID].saveStatus()) {
                return false;
            } else {
                return true;
            }
        }
        return false;

    }

    /**
     * Function to play a dice-rolling sound when user throws dice
     *
     */
    public void playSound() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mediaPlayer != null) {
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                }
            });
            AssetFileDescriptor afd = getAssets().openFd("dicerolling.mp3");
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve a image Resid for the dice - if the dice is saved one imagelink should be used.
     * If the dice is not saved, the default colored dice should be used.
     * Used when user clicks a die of the thrown dice
     * @param dieNumber number in the array that should change image
     * @return id of the image to de displayed
     */
    public int getDiceImage(int dieNumber) {

        int resId = 0;
        resId = getResources().getIdentifier(diceArray[dieNumber].imageLink(), "drawable",getPackageName());

        return resId;
    }

    /**
     * Shows a general description and the rules of the game for the user.
     *
     */
    public void createDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(getString(R.string.About));
        alertDialog.setMessage(getString(R.string.about_game));
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
     * Called to setup the toolbar menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void startResultActivity(){
        Intent resultActivity = new Intent(this,ResultActivity.class);

        HashMap<String,Integer> player1Map = new HashMap<String, Integer>();           // Send a hashmap of combination and score
        HashMap<String,Integer> player2Map = new HashMap<String, Integer>();           // Send a hashmap of combination and score

        player1Map = player1Rules.getHashMap();

        resultActivity.putExtra(getString(R.string.Player1Map),player1Map);
        resultActivity.putExtra(getString(R.string.multiplayer),multiplayer);

        resultActivity.putExtra(getString(R.string.player_one),playerOneName);
        resultActivity.putExtra(getString(R.string.PlayerOneTurn),player1Rules.getTurn());

        if (multiplayer){
            player2Map = player2Rules.getHashMap();
            resultActivity.putExtra(getString(R.string.Player2Map),player2Map);
            resultActivity.putExtra(getString(R.string.player_two),playerTwoName);
            resultActivity.putExtra(getString(R.string.PlayerTwoTurn),player2Rules.getTurn());
        }


        startActivity(resultActivity);                                // Start activity to display result of combinations and score
    }

    /**
     * Handle clicks in the toolbar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_scoretable : startResultActivity();            // Show the current score
                break;
            case R.id.about_game:
                createDialog();         // Show information about the game
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * Called if the instance was saved for any reason - used to save the state of the fragment
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        PlayFragment playFragment = (PlayFragment) fragmentManager.findFragmentById(R.id.play_fragment);
        fragmentManager.putFragment(outState,getString(R.string.playfragment),playFragment);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Toast.makeText(this, R.string.finish_game_error,Toast.LENGTH_SHORT).show(); // Show message if user presses back button
    }
}
