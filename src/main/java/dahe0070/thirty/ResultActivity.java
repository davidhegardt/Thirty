package dahe0070.thirty;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * David Hegardt 2017-07-02
 * Class to display the results of the game.
 * Controller class handles logic for displaying results.
 * Does not interact with UI elements directly, this is done in the fragment.
 *
 */
public class ResultActivity extends AppCompatActivity {

    private HashMap<String,Integer> hashMap1 = new HashMap<>(); // Hashmap tracks combination / score for combination
    private boolean multiPlayer;
    private HashMap<String,Integer> hashMap2 = new HashMap<>(); // Hashmap for player 2, if multiplayer
    private String playerOneName;
    private String playerTwoName;
    private int playerOneTurn;                                // Current turn for players
    private int playerTwoTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        hashMap1 = (HashMap<String, Integer>) intent.getSerializableExtra(getString(R.string.Player1Map));            // Retrieve the hashmap for first player
        multiPlayer = intent.getBooleanExtra(getString(R.string.multiplayer),false);                                  // Check if game is multiplayer
        playerOneName = intent.getStringExtra(getString(R.string.player_one));                                         // Retrieve player name
        playerOneTurn = intent.getIntExtra(getString(R.string.PlayerOneTurn),0);                                     // Retrieve the turn number

        if (multiPlayer){
            hashMap2 = (HashMap<String, Integer>) intent.getSerializableExtra(getString(R.string.Player2Map));        // If game is multiplayer, retrieve hashmap for second player
            playerTwoName = intent.getStringExtra(getString(R.string.player_two));
            playerTwoTurn = intent.getIntExtra(getString(R.string.PlayerTwoTurn),0);
        }


        setTitle(getString(R.string.Result_table));

    }

    /**
     * Retrieve player name
     * @param playerNr
     * @return name of the player
     */
    public String getPlayerName(int playerNr){
        if (playerNr == 1){
            return playerOneName;
        } else {
            return playerTwoName;
        }
    }

    public boolean getMultiplayer(){
        return multiPlayer;
    }

    /**
     * Function to calculate the score to be displayed
     * @param comboNr for which combination
     * @param playerNr  for which player
     * @return the score for this combination
     */
    public int getScore(int comboNr, int playerNr){
        if (playerNr == 1) {
            if (comboNr == 88) {                            // 88 is used for LOW score
                String combo = "LOW";
                int score = hashMap1.get(combo);
                return score;
            }

            String combo = "Combo" + comboNr;               // Retrieve value from hashmap
            int score = hashMap1.get(combo);

            return score;

        } else {
            if (comboNr == 88) {
                String combo = "LOW";
                int score = hashMap2.get(combo);
                return score;
            }

            String combo = "Combo" + comboNr;
            int score = hashMap2.get(combo);

            return score;
        }
       // return 1;
    }

    /**
     * Summarize all combinations and return the total value
     * @param playerNr the concerned player
     * @return the total score of all combinations
     */
    public int totalScore(int playerNr){
        int totalSum = 0;

        if (playerNr == 1) {

            for (Integer f : hashMap1.values()) {           // Loop hashmap values
                totalSum += f;
            }

        } else {
            for (Integer g : hashMap2.values()) {           // Loop hashmap values
                totalSum += g;
            }
        }

        return totalSum;
    }

    /**
     * Check if all combinations are over and game should end
     * @return end or not
     */
    private boolean gameDone(){
        if (!multiPlayer){
            if (playerOneTurn == 11){                                         // If singleplayer, player will advance to turn 11
                return true;
            } else return false;
        }

        if (playerTwoTurn == 10){                                      // If player 2 has finished all turns the game is over.
            return true;
        } else {
            return false;
        }
    }

    /**
     * Function to determine who is leading and who wins the whole game
     * @return name of the winner
     */
    public String calcWinner(){
        if (multiPlayer){                                       // Winner can only be determined if 2 people are playing...
            int p1Score = totalScore(1);
            int p2Score = totalScore(2);

            if (gameDone()){                                    // If the game is done, compare total score
                if (p1Score > p2Score){
                    return getString(R.string.winner_is) + playerOneName;
                } else if (p2Score == p1Score){
                    return getString(R.string.tied_match);
                } else {
                    return getString(R.string.winner_is) + playerTwoName;
                }
            }

            if (p1Score > p2Score){                             // If the game is not finished, compare total score
                return playerOneName + getString(R.string.is_leading);
            } else if (p1Score == p2Score){
                return getString(R.string.both_tied);
            } else {
                return playerTwoName + getString(R.string.is_leading);
            }


        }

        else return "No one is winner..";
    }

    /**
     * Function called when user presses back button
     */
    @Override
    public void onBackPressed() {
        if (gameDone()){                                                            // If the game is done, it should end if user presses back button
            Toast.makeText(this, R.string.game_done,Toast.LENGTH_SHORT).show();     // So users cannot continue to play
            Intent restart = new Intent(this,StartScreenActivity.class);
            ComponentName cn = restart.getComponent();
            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);           // Restart component and activites and return to start screen
            startActivity(mainIntent);
        } else {
            super.onBackPressed();                                                  // If the game is not done, just return to previous screen
        }
    }
}
