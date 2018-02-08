package dahe0070.thirty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * David Hegardt 2017-07-02
 * Activity displaying options when app is started.
 * Singleplayer - starts a one player game, keeping track of this player.
 * Multiplayer - starts a 2 player game, keeping track of both players and compares
 * their score for all and individual throws.
 */
public class StartScreenActivity extends AppCompatActivity {

    private String playerOneName;
    private String playerTwoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Welcome to Thirty");
    }

    /**
     * Function called after single/ multiplayer is chosen
     * @param playerNumber number of players in the game
     */
    public void startGame(int playerNumber) {
        Intent newGame = new Intent(this,MainActivity.class);

        newGame.putExtra(getString(R.string.PlayerNr),playerNumber);
        newGame.putExtra(getString(R.string.player_one),playerOneName);                    // Set playernames to be sent to MainActivity
        newGame.putExtra(getString(R.string.player_two),playerTwoName);

        startActivity(newGame);                                         // Start the game

    }

    /**
     * Disalog to be displayed if user chooses singleplayer
     */
    public void singleplayerDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View prompt = inflater.inflate(R.layout.single_prompt,null);                // load custom dialog-layout

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(prompt);

        final EditText singlePlayer = (EditText) prompt.findViewById(R.id.editTextSinglePlayer);

        alert.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                playerOneName = singlePlayer.getText().toString();
                startGame(1);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.create();

        alert.show();
    }

    /**
     * Dialog is displayed if user chooses multiplayer
     */
    public void multiplayerDialog(){

        LayoutInflater inflater = LayoutInflater.from(this);
        View prompt = inflater.inflate(R.layout.multi_prompt,null);             // load custom dialog-layout

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(prompt);

        final EditText playerOne = (EditText) prompt.findViewById(R.id.editTextPlayerOne);          // Setup input textfield for firstplayer

        final EditText playerTwo = (EditText) prompt.findViewById(R.id.editTextPlayerTwo);          // Setup input textfield for secondplayer

        alert.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                playerOneName = playerOne.getText().toString();                                     // Set playernames based on input
                playerTwoName = playerTwo.getText().toString();
                startGame(2);                                                                       // Start multiplayer-game
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.create();

        alert.show();

    }

    /**
     * Show a dialog of information about the game
     */
    public void createDialog(){
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(StartScreenActivity.this).create();
        alertDialog.setTitle(getString(R.string.About));
        alertDialog.setMessage(getString(R.string.about_game));
        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void quit(){
        finish();
    }

}
