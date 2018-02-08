package dahe0070.thirty;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment class to hande UI of the Startscreen.
 * Logic is handeled in the parent activity
 */
public class StartScreenActivityFragment extends Fragment implements View.OnClickListener {

    public StartScreenActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnSingle).setOnClickListener(this);                                 // Setup onClick-listeners
        view.findViewById(R.id.btnMultiplayer).setOnClickListener(this);
        view.findViewById(R.id.btnAbout).setOnClickListener(this);
        view.findViewById(R.id.btnQuit).setOnClickListener(this);
    }

    /**
     * Handle onclick for singleplayer, multiplayer, about the game and exit
     * @param v the selected button
     */
    @Override
    public void onClick(View v) {

        StartScreenActivity startScreen = ((StartScreenActivity) getActivity());

        switch (v.getId()){
            case R.id.btnSingle : startScreen.singleplayerDialog();
                break;
            case R.id.btnMultiplayer : startScreen.multiplayerDialog();
                break;
            case R.id.btnAbout : startScreen.createDialog();
                break;
            case R.id.btnQuit : startScreen.quit();
                break;
        }
    }
}
