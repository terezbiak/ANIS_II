package sk.tuke.zadanie_zajko;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryHolder extends RecyclerView.ViewHolder {
    TextView player;
    TextView score;
    public HistoryHolder(@NonNull View itemView){
        super(itemView);
        player = itemView.findViewById(R.id.player_name);
        score = itemView.findViewById(R.id.score);
    }
}
