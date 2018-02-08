package dahe0070.thirty;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A controller class for the UI elements. Only handles
 * UI update / interaction and logic is handled in the parent activity.
 * Displays a result table for players, if singleplayer is choosen, multiplayer
 * table is hidden.
 */
public class ResultActivityFragment extends Fragment {


    public ResultActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setupTable();
    }

    /**
     * Function used to setup the table result view for first player
     */
    public void setupTablePlayer1() {

        ResultActivity resultActivity = ((ResultActivity) getActivity());

        for (int i = 4; i < 13; i++) {
            String name = "txt" + i + "Score";
            int id = getResources().getIdentifier(name, "id", getContext().getPackageName());       // Loop all textviews used to display the score
            if (id != 0) {
                TextView combo = (TextView) getView().findViewById(id);                             // set the score to appropriate value from hashmap
                combo.setText("" + resultActivity.getScore(i, 1));
            }
        }

        TextView low = (TextView) getView().findViewById(R.id.txtLowScore);                         // setup the score for LOW values
        low.setText("" + resultActivity.getScore(88, 1));

        TextView playerOne = (TextView) getView().findViewById(R.id.tvPlayer1);
        playerOne.setText(resultActivity.getPlayerName(1));

        TextView totalScore = (TextView) getView().findViewById(R.id.txtTotalOut);
        resultActivity.totalScore(1);
        String tot = "" + resultActivity.totalScore(1);
        totalScore.setText(tot);

    }


    /**
     * Function used to setup the table for result view for second player
     */
    public void setupTableP2(){
        ResultActivity resultActivity = ((ResultActivity) getActivity());

        for (int i = 4; i < 13; i++) {                                                              // Loop all textviews used to display the score
            String name = "txt" + i + "Score" + 2;
            int id = getResources().getIdentifier(name, "id", getContext().getPackageName());
            if (id != 0) {
                TextView combo = (TextView) getView().findViewById(id);
                combo.setText("" + resultActivity.getScore(i, 2));                                  // set the score to appropriate value from hashmap
            }
        }

        TextView low2 = (TextView) getView().findViewById(R.id.txtLowScore2);                       // setup the score for LOW values
        low2.setText("" + resultActivity.getScore(88, 2));

        TextView player2 = (TextView) getView().findViewById(R.id.tvPlayer2);                       // set the second player name
        player2.setText(resultActivity.getPlayerName(2));

        TextView totalScore2 = (TextView) getView().findViewById(R.id.txtTotalOut2);                // set the total score for this player
        resultActivity.totalScore(2);
        String tot = "" + resultActivity.totalScore(2);
        totalScore2.setText(tot);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ResultActivity resultActivity = ((ResultActivity) getActivity());                           // get the parent activity reference

        setupTablePlayer1();                                                                        // setup the result table for first player
        if (resultActivity.getMultiplayer()){
            setupTableP2();                                                                         // if game is multiplayer, also set table for second player
            TextView winner = (TextView) getView().findViewById(R.id.txtWinnerOut);                 // Also setup text for winner
            winner.setText(resultActivity.calcWinner());
        } else {
            //TableLayout player2Table = (TableLayout) getView().findViewById(R.id.playerTwoTable);
            //player2Table.setVisibility(View.GONE);
            hidePlayer2();                                                                          // If singleplayer, the table for player 2 should not be shown
        }

    }

    /**
     * Hides all views related to the player 2 table and the winenr text
     */
    public void hidePlayer2(){
        TextView tvPlayer2 = (TextView) getView().findViewById(R.id.tvPlayer2);
        tvPlayer2.setVisibility(View.GONE);

        TextView tvVal = (TextView) getView().findViewById(R.id.tvVal2);
        tvVal.setVisibility(View.GONE);

        TextView tvLow2 = (TextView) getView().findViewById(R.id.tvLow2);
        tvLow2.setVisibility(View.GONE);

        TextView txtLowScore2 = (TextView) getView().findViewById(R.id.txtLowScore2);
        txtLowScore2.setVisibility(View.GONE);

        TextView tvTotal2 = (TextView) getView().findViewById(R.id.tvTotal2);
        tvTotal2.setVisibility(View.GONE);

        TextView tvPoang2 = (TextView) getView().findViewById(R.id.tvPoang2);
        tvPoang2.setVisibility(View.GONE);


        for (int i = 4; i < 13; i++) {
            String name = "tvScore" + i + "P2";
            int id = getResources().getIdentifier(name, "id", getContext().getPackageName());
            if (id != 0) {
                TextView currTv = (TextView) getView().findViewById(id);
                currTv.setVisibility(View.GONE);
            }
        }

        for (int i = 4; i < 13; i++) {
            String name = "txt" + i + "Score" + 2;
            int id = getResources().getIdentifier(name, "id", getContext().getPackageName());
            if (id != 0) {
                TextView currTxt = (TextView) getView().findViewById(id);
                currTxt.setVisibility(View.GONE);
            }
        }

        TextView winner = (TextView) getView().findViewById(R.id.txtWinnerOut);
        winner.setVisibility(View.GONE);

        TextView totalScore2 = (TextView) getView().findViewById(R.id.txtTotalOut2);
        totalScore2.setVisibility(View.GONE);
    }

}
