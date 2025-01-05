package sk.tuke.zadanie_zajko;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

public class GameViewFragment extends Fragment {

    private GameView gameView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gameView = new GameView(getActivity());
        return gameView;
    }

    public void refreshGameState() {

    }
}
