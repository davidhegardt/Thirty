package dahe0070.thirty;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
  PlayFragment handles user interaction with the dice and buttons in the
  MainActivity (dice throwing) class. If multiplayer is choosen, the players
 take turns to throw the dice each time.
 */
public class PlayFragment extends Fragment implements View.OnClickListener {


    private ArrayList<Integer> diceImageIds = new ArrayList<>();
    private FrameLayout frameLayout;
    private boolean diceThrown = false;

    public PlayFragment() {
        // Required empty public constructor
    }

    public static PlayFragment newInstance(String param1, String param2) {
        PlayFragment fragment = new PlayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_play, container, false);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Loop and all the imageDie-elements to arraylist

        for (int i = 1; i < 7; i++){
            String name = "imageDie" +i;
            int id = getResources().getIdentifier(name,"id",getContext().getPackageName());
            if (id != 0){
                diceImageIds.add(id);
            }
        }

        hideDice(true);     // Hide all the dice before throw button is pressed

        for (Integer id : diceImageIds){
            view.findViewById(id).setOnClickListener(this);         // Add on click listeners to the imageViews
        }

        view.findViewById(R.id.btnThrow).setOnClickListener(this);      // Add listener for throw-button
        view.findViewById(R.id.btnScore).setOnClickListener(this);      // Add listener for score-button
    }

    /**
     * Loop and hide / show all the dice in the image-arraylist
     * @param hide
     */
    public void hideDice(boolean hide) {
        if (hide) {
            for (Integer id : diceImageIds){
                getView().findViewById(id).setVisibility(View.INVISIBLE);
            }

        } else if (!hide) {
            for (Integer id : diceImageIds){
                getView().findViewById(id).setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    //    mListener = null;
    }

    /**
     * Function called when user clicks an object in the UI
     * @param v the current item clicked
     */
    @Override
    public void onClick(View v) {

        MainActivity main = ((MainActivity)getActivity());

        switch (v.getId()) {
            case R.id.imageDie1 : main.saveDice(1); changeColor(v);         // Call function to add this die to the list of saved dice
                break;                                                      // Change color so the die is greyed out
            case R.id.imageDie2 : main.saveDice(2); changeColor(v);
                break;
            case R.id.imageDie3 : main.saveDice(3); changeColor(v);
                break;
            case R.id.imageDie4 : main.saveDice(4); changeColor(v);
                break;
            case R.id.imageDie5 : main.saveDice(5); changeColor(v);
                break;
            case R.id.imageDie6 : main.saveDice(6); changeColor(v);
                break;
            case R.id.btnScore : main.scoreTable();
                break;
            case R.id.btnThrow :
                hideDice(false);                                            // Show the dice
                diceThrown = true;
                main.throwDice();                                           // call function to throw the dice (add dice objects with random values)

                diceOnThrow();

                TextView player = (TextView) getView().findViewById(R.id.txtCurrPlayer);
                player.setText(main.getCurrPlayer());                                       // Show the current player name

                TextView turn = (TextView) getView().findViewById(R.id.txtTurn);
                turn.setText("" + main.turnNumber());                                   // Show the current player turn

                TextView throwOut = (TextView) getView().findViewById(R.id.txtThrow);
                throwOut.setText("" + main.throwNumber());                                       // Show the current throw number


                main.playSound();                                   // Play dice-rolling sound

        }
    }

    /**
     * Handle the dice display logic when dice is thrown.
     * Shows an animation of the dice to simulate throw and changes
     * the image displayed for each die.
     */
    private void diceOnThrow(){

        MainActivity main = ((MainActivity)getActivity());

        Animation shake = AnimationUtils.loadAnimation(getContext(),R.anim.shake);          // Retrieve animation
        if (!main.isSaved(1)) {
            ImageView firstDie = (ImageView) getView().findViewById(R.id.imageDie1);        // Retrieve the correct die image and add to the ImageView and start animation
            firstDie.setImageResource(main.getDiceImage(1));
            firstDie.startAnimation(shake);
        }

        if (!main.isSaved(2)) {
            ImageView secondDie = (ImageView) getView().findViewById(R.id.imageDie2);
            secondDie.setImageResource(main.getDiceImage(2));

            secondDie.startAnimation(shake);
        }
        if (!main.isSaved(3)) {
            ImageView thirdDie = (ImageView) getView().findViewById(R.id.imageDie3);
            thirdDie.setImageResource(main.getDiceImage(3));

            thirdDie.startAnimation(shake);
        }
        if (!main.isSaved(4)) {
            ImageView fourthDie = (ImageView) getView().findViewById(R.id.imageDie4);
            fourthDie.setImageResource(main.getDiceImage(4));

            fourthDie.startAnimation(shake);
        }
        if (!main.isSaved(5)) {
            ImageView fifthDie = (ImageView) getView().findViewById(R.id.imageDie5);
            fifthDie.setImageResource(main.getDiceImage(5));

            fifthDie.startAnimation(shake);
        }
        if (!main.isSaved(6)) {
            ImageView sixthDie = (ImageView) getView().findViewById(R.id.imageDie6);
            sixthDie.setImageResource(main.getDiceImage(6));

            sixthDie.startAnimation(shake);
        }
    }

    /**
     * Function to update the current players name displayed
     */
    public void updatePlayer(){

        MainActivity main = ((MainActivity)getActivity());

        TextView player = (TextView) getView().findViewById(R.id.txtCurrPlayer);
        player.setText("Player " + main.getCurrPlayer());
    }

    /**
     * Function called when phone is rotated to setup the dice again
     */
    private void setTheDices(){

        MainActivity main = ((MainActivity)getActivity());

        Animation shake = AnimationUtils.loadAnimation(getContext(),R.anim.shake);

            ImageView firstDie = (ImageView) getView().findViewById(R.id.imageDie1);
            firstDie.setImageResource(main.getDiceImage(1));
            firstDie.startAnimation(shake);

            ImageView secondDie = (ImageView) getView().findViewById(R.id.imageDie2);
            secondDie.setImageResource(main.getDiceImage(2));

            secondDie.startAnimation(shake);

            ImageView thirdDie = (ImageView) getView().findViewById(R.id.imageDie3);
            thirdDie.setImageResource(main.getDiceImage(3));

            thirdDie.startAnimation(shake);

            ImageView fourthDie = (ImageView) getView().findViewById(R.id.imageDie4);
            fourthDie.setImageResource(main.getDiceImage(4));

            fourthDie.startAnimation(shake);

            ImageView fifthDie = (ImageView) getView().findViewById(R.id.imageDie5);
            fifthDie.setImageResource(main.getDiceImage(5));

            fifthDie.startAnimation(shake);

            ImageView sixthDie = (ImageView) getView().findViewById(R.id.imageDie6);
            sixthDie.setImageResource(main.getDiceImage(6));

            sixthDie.startAnimation(shake);

    }

    /**
     * Function called when a turn is over to prepare for a new turn
     */
    public void updateView(){
        MainActivity main = ((MainActivity)getActivity());
        diceThrown = false;                                                     // Dice has not been thrown
        switchRoll(true);                                                       // Enable throw-button
        switchScoreBtn(false);                                                  // Disable score button
        hideDice(true);                                                         // hide all the dice so user cannot click them before throw
        TextView turn = (TextView) getView().findViewById(R.id.txtTurn);
        turn.setText("" + main.turnNumber());                               // Setup turn and throw numbers

        TextView throwOut = (TextView) getView().findViewById(R.id.txtThrow);
        throwOut.setText("" + main.throwNumber());

        updatePlayer();                                                         // Update player name
    }

    /**
     * Set the throw button enable / disable
     * @param choice boolean choice
     */
    public void switchRoll(boolean choice) {
        Button roll = (Button) getView().findViewById(R.id.btnThrow);
        if (choice) {
            roll.setEnabled(true);
        } else if (!choice) {
            roll.setEnabled(false);
        }
    }

    /**
     * Set the core button enable / disable
     * @param choice boolean choice
     */
    public void switchScoreBtn(boolean choice){
        Button scoretable = (Button) getView().findViewById(R.id.btnScore);
        if (choice) {
            scoretable.setEnabled(true);
        } else if (!choice) {
            scoretable.setEnabled(false);
        }
    }


    /**
     * Function to change the color when the die is pressed to be used for save.
     * Grey dice = saved, green dice = not saved
     * @param v the current dice to change color for
     */
    public void changeColor(View v){

        MainActivity main = ((MainActivity)getActivity());

        switch (v.getId()) {
            case R.id.imageDie1 : ImageView dice1 = (ImageView)v.findViewById(R.id.imageDie1); dice1.setImageResource(main.getDiceImage(1));
                break;
            case R.id.imageDie2 : ImageView dice2 = (ImageView)v.findViewById(R.id.imageDie2); dice2.setImageResource(main.getDiceImage(2));
                break;
            case R.id.imageDie3 : ImageView dice3 = (ImageView)v.findViewById(R.id.imageDie3); dice3.setImageResource(main.getDiceImage(3));
                break;
            case R.id.imageDie4 : ImageView dice4 = (ImageView)v.findViewById(R.id.imageDie4); dice4.setImageResource(main.getDiceImage(4));
                break;
            case R.id.imageDie5 : ImageView dice5 = (ImageView)v.findViewById(R.id.imageDie5); dice5.setImageResource(main.getDiceImage(5));
                break;
            case R.id.imageDie6 : ImageView dice6 = (ImageView)v.findViewById(R.id.imageDie6); dice6.setImageResource(main.getDiceImage(6));
                break;
        }
    }

    /**
     * Called if the application is aborted
     * @param outState state of the application
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView turn = (TextView) getView().findViewById(R.id.txtTurn);
        String turnNr = turn.getText().toString();
        outState.putString(getString(R.string.turnNR),turnNr);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // If view needs to be recreated
        if (savedInstanceState != null) {
            TextView turn = (TextView) getView().findViewById(R.id.txtTurn);
            String turnNr = savedInstanceState.getString("turnNR");
            turn.setText(turnNr);
            setTheDices();
            hideDice(false);
        }
    }

    /**
     * Function called on rotation - change the layout to landscape / horizontal
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        populateViewAgain(inflater, (ViewGroup) getView());

    }

    /**
     * Function called when the phone is rotated, sets all components to be displayed properly
     * @param inflater
     * @param viewGroup
     */
    private void populateViewAgain(LayoutInflater inflater,ViewGroup viewGroup){
        viewGroup.removeAllViewsInLayout();
        View subview = inflater.inflate(R.layout.fragment_play,viewGroup);          // Change the layout

        for (Integer id : diceImageIds){
            getView().findViewById(id).setOnClickListener(this);                    // Set on click listeners again
        }

        getView().findViewById(R.id.btnThrow).setOnClickListener(this);
        getView().findViewById(R.id.btnScore).setOnClickListener(this);
        if (diceThrown) {                                                           // If the dice where thrown, display the rolled values
            setTheDices();
        } else {
            hideDice(true);                                                         // If the dice have not been thrown, hide all the dice and the scorebutton
            switchScoreBtn(false);
        }

        MainActivity main = ((MainActivity)getActivity());

        TextView turn = (TextView) getView().findViewById(R.id.txtTurn);        // Set the turn and throw number
        turn.setText("" + main.turnNumber());

        TextView throwOut = (TextView) getView().findViewById(R.id.txtThrow);
        throwOut.setText("" + main.throwNumber());

        if (main.throwNumber() == 3){                                                // Check if third throw or not
            switchRoll(false);
            switchScoreBtn(true);
        } else {
            switchRoll(true);
            //switchScoreBtn(false);
        }

        updatePlayer();                                                             // Set the player name again
    }
}
