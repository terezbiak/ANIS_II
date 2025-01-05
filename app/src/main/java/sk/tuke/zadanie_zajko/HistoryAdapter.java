package sk.tuke.zadanie_zajko;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {
    private List<Player> _data;
    private WeakReference<Context> _context;

    public HistoryAdapter(List<Player> data, WeakReference<Context> contectReference){
        _context = contectReference;
        _data = data;
    }

    public void refreshData(List<Player> data){
        _data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(_context.get()).inflate(R.layout.score_view, viewGroup, false);
        return new HistoryHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HistoryHolder scoreHolder, int i){
        scoreHolder.player.setText(_data.get(i).getPlayerName());
        scoreHolder.score.setText(String.valueOf(_data.get(i).getPlayerScore()));


    }

    @Override
    public int getItemCount(){
        if (_data != null) {
            return _data.size();
        }
        return 0;
    }
}
