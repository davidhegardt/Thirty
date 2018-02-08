package dahe0070.thirty;

import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Fragment class to handle UI interaction of the ScoreActivity.
 * Controller class to handle displaying items / user interaction.
 * Handle onclick, but logic is handeled in the ScoreActivity parent class.
 * This class shows the user the throw dice and user gets to add them togheter
 * to form the choosen combo score.
 */
public class ScoreActivityFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private int totalScore = 0;
    private int comboSelector = 0;
    private Spinner spinner;
    private int SpinnerPos;
    private boolean comboSelected = false;

    public ScoreActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_score, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null){

        }
    }

    /**
     * Called on rotation
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        refreshView(inflater,(ViewGroup) getView());
    }

    /**
     * Refreshes view to display items when rotating
     * @param inflater
     * @param viewGroup
     */
    private void refreshView(LayoutInflater inflater,ViewGroup viewGroup){
        viewGroup.removeAllViewsInLayout();
        View subView = inflater.inflate(R.layout.fragment_score, viewGroup);
        setupDices();

        TextView finalScoreOutput = (TextView) getView().findViewById(R.id.txtScoreOut);
        finalScoreOutput.setText("" + totalScore);                                          // View the final score

        ScoreActivity scoreActivity = ((ScoreActivity) getActivity());

        scoreActivity.rotateHide();                                                         // Call function to hide components on rotation

        spinner.setSelection(SpinnerPos);                                                   // set the spinner to previously selected item


    }

    /**
     * Find the correct dice values and set correct image of the saved dice from MainActivity
     */
    public void setupDices() {
        ScoreActivity scoreActivity = ((ScoreActivity) getActivity());

        TextView player = (TextView) getView().findViewById(R.id.txtPlayerNr);                      // Show the player name
        player.setText("Player " + scoreActivity.getThisPlayer());

        spinner = (Spinner) getView().findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,scoreActivity.getSpinner());         // setup the spinner and retrieve spinenr items

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        showControls(false);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        for (int i = 1; i < 7; i++){
            String name = "txtDie" +i +"Out";
            int id = getResources().getIdentifier(name,"id",getContext().getPackageName());         // Loop and select all textview to display dice values
            if (id != 0){
                TextView textDieVal = (TextView) getView().findViewById(id);
                textDieVal.setText("" + scoreActivity.getDiceValue(i));
            }
        }

        for (int i = 1; i < 7; i++){
            String name = "imgFinalDie" +i;
            int id = getResources().getIdentifier(name,"id",getContext().getPackageName());         // Loop and select all the imageviews to display the dice image
            if (id != 0){
                ImageView dice = (ImageView) getView().findViewById(id);
                dice.setImageResource(scoreActivity.getDiceSmallImage(i));
                dice.setOnClickListener(this);
            }
        }


            ImageButton btnSummarize = (ImageButton) getView().findViewById(R.id.btnSummarize);     // Set on click listeners for + button and finish
            btnSummarize.setOnClickListener(this);

            Button finish = (Button) getView().findViewById(R.id.btnFinish);
            finish.setOnClickListener(this);

    }

    /**
     * Handle click events for the buttons and image views
     * @param v clicked item
     */
    @Override
    public void onClick(View v) {

        ScoreActivity scoreActivity = ((ScoreActivity) getActivity());

        switch (v.getId()){
            case R.id.imgFinalDie1 : scoreActivity.countDice(1); changeColor(v);                    // When clicked, call function to add die to total counter change change color of that die
                break;
            case R.id.imgFinalDie2 : scoreActivity.countDice(2); changeColor(v);
                break;
            case R.id.imgFinalDie3 : scoreActivity.countDice(3); changeColor(v);
                break;
            case R.id.imgFinalDie4 : scoreActivity.countDice(4); changeColor(v);
                break;
            case R.id.imgFinalDie5 : scoreActivity.countDice(5); changeColor(v);
                break;
            case R.id.imgFinalDie6 : scoreActivity.countDice(6); changeColor(v);
                break;
            case R.id.btnSummarize : int score = scoreActivity.calcScore(comboSelector); updateScore(score);        // When + button is pressed, calculate total value and update the score
                break;
            case R.id.btnFinish : scoreActivity.onFinish(comboSelector,totalScore,SpinnerPos);                      // Call function when user presses finish button
                break;
        }

        //updateScore();
    }

    /**
     * Function to hide a specific dice once it has been used
     * @param id the dice to hide
     */
    public void hideDice(int id){
        switch (id){
            case 1 : getView().findViewById(R.id.imgFinalDie1).setVisibility(View.INVISIBLE);
                break;
            case 2 : getView().findViewById(R.id.imgFinalDie2).setVisibility(View.INVISIBLE);
                break;
            case 3 : getView().findViewById(R.id.imgFinalDie3).setVisibility(View.INVISIBLE);
                break;
            case 4 : getView().findViewById(R.id.imgFinalDie4).setVisibility(View.INVISIBLE);
                break;
            case 5 : getView().findViewById(R.id.imgFinalDie5).setVisibility(View.INVISIBLE);
                break;
            case 6 : getView().findViewById(R.id.imgFinalDie6).setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
     * Function used when an invalid combination has been used, set to default green color
     */
    public void colorAllDice(){
        ScoreActivity scoreActivity = ((ScoreActivity) getActivity());

        for (int i = 1; i < 7; i++){
            String name = "imgFinalDie" +i;                                                         // Loop all imageviews and set dice if visible
            int id = getResources().getIdentifier(name,"id",getContext().getPackageName());
            if (id != 0){
                ImageView dice = (ImageView) getView().findViewById(id);
                dice.setImageResource(scoreActivity.getDiceSmallImage(i));
            }
        }
    }

    /**
     * Function used to change color of a dice if it has been saved
     * @param v the die to change color of
     */
    public void changeColor(View v){

        ScoreActivity scoreActivity = ((ScoreActivity) getActivity());

        switch (v.getId()) {
            case R.id.imgFinalDie1 : ImageView dice1 = (ImageView)v.findViewById(R.id.imgFinalDie1); dice1.setImageResource(scoreActivity.getDiceSmallImage(1));
                break;
            case R.id.imgFinalDie2 : ImageView dice2 = (ImageView)v.findViewById(R.id.imgFinalDie2); dice2.setImageResource(scoreActivity.getDiceSmallImage(2));
                break;
            case R.id.imgFinalDie3 : ImageView dice3 = (ImageView)v.findViewById(R.id.imgFinalDie3); dice3.setImageResource(scoreActivity.getDiceSmallImage(3));
                break;
            case R.id.imgFinalDie4 : ImageView dice4 = (ImageView)v.findViewById(R.id.imgFinalDie4); dice4.setImageResource(scoreActivity.getDiceSmallImage(4));
                break;
            case R.id.imgFinalDie5 : ImageView dice5 = (ImageView)v.findViewById(R.id.imgFinalDie5); dice5.setImageResource(scoreActivity.getDiceSmallImage(5));
                break;
            case R.id.imgFinalDie6 : ImageView dice6 = (ImageView)v.findViewById(R.id.imgFinalDie6); dice6.setImageResource(scoreActivity.getDiceSmallImage(6));
                break;

        }
    }

    /**
     * Called after a combination is chosen
     * @param finalScore the calculated score
     */
    public void updateScore(int finalScore){

        ScoreActivity scoreActivity = ((ScoreActivity) getActivity());

        TextView comboScore = (TextView) getView().findViewById(R.id.txtOutCombo);
        comboScore.setText("" + finalScore);

        if (finalScore == 0) {                                          // Combination invalid
            scoreActivity.showError();                                  // Display errormessage
            scoreActivity.resetScore();                                 // Reset the added dice
            colorAllDice();                                             // Set to default colors
        }

        if (finalScore != 0) {                                          // Combination valid
            totalScore += finalScore;                                   // Add this score to the total score
            TextView finalScoreOutput = (TextView) getView().findViewById(R.id.txtScoreOut);
            finalScoreOutput.setText("" + totalScore);
            scoreActivity.resetScore();                                 // Reset dice combination array
        }

    }

    /**
     * Functin called when a spinner item is selected.
     * The spinner item is removed once used and the order of items changes.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String choice = parent.getItemAtPosition(position).toString();
        switch (choice) {
           // case 0: comboSelector = 4; spinner.setEnabled(false);
           //     break;
            case "LOW": comboSelector = 88; spinner.setEnabled(false); SpinnerPos = position; comboSelected = true; showControls(true);
                break;
            case "All combinations of 4": comboSelector = 4; spinner.setEnabled(false); SpinnerPos = position; comboSelected = true; showControls(true);         // Set the combination to add up to, disable the spinner and save the position to be removed later
                break;
            case "All combinations of 5": comboSelector = 5; spinner.setEnabled(false); SpinnerPos = position; comboSelected = true; showControls(true);
                break;
            case "All combinations of 6": comboSelector = 6; spinner.setEnabled(false); SpinnerPos = position; comboSelected = true; showControls(true);
                break;
            case "All combinations of 7": comboSelector = 7; spinner.setEnabled(false); SpinnerPos = position; comboSelected = true; showControls(true);
                break;
            case "All combinations of 8": comboSelector = 8; spinner.setEnabled(false); SpinnerPos = position; comboSelected = true; showControls(true);
                break;
            case "All combinations of 9": comboSelector = 9; spinner.setEnabled(false); SpinnerPos = position; comboSelected = true; showControls(true);
                break;
            case "All combinations of 10": comboSelector = 10; spinner.setEnabled(false); SpinnerPos = position; comboSelected = true; showControls(true);
                break;
            case "All combinations of 11": comboSelector = 11; spinner.setEnabled(false); SpinnerPos = position; comboSelected = true; showControls(true);
                break;
            case "All combinations of 12" : comboSelector = 12; spinner.setEnabled(false); SpinnerPos = position; comboSelected = true; showControls(true);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //comboSelector = 4;
        spinner.setEnabled(true);
    }

    public boolean isComboSelected(){
        return comboSelected;
    }

    public void showControls(boolean choice){
        if (!choice) {
            getView().findViewById(R.id.btnFinish).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.btnSummarize).setVisibility(View.INVISIBLE);
        } else {
            getView().findViewById(R.id.btnFinish).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.btnSummarize).setVisibility(View.VISIBLE);
        }
    }

}
