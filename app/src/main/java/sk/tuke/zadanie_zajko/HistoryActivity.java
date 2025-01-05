package sk.tuke.zadanie_zajko;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<HistoryHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        recyclerView = findViewById(R.id.rv_score_view);
        layoutManager = new LinearLayoutManager(this);

        loadSortedData();

    }

    class DbGetData extends AsyncTask<Void, Void, List<Player>>{
        @Override
        protected List<Player> doInBackground(Void ... voids){
            PlayerDao playerDao = DbTools.getDbContext(new WeakReference<>(HistoryActivity.this)).playerDao();
            return playerDao.getAllPlayersSortedByScore();
        }
        @Override
        protected void onPostExecute(List<Player> players){
            super.onPostExecute(players);

            adapter = new HistoryAdapter(players, new WeakReference<Context>(HistoryActivity.this));
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
    }
    private void loadSortedData() {
        DbGetData taskLoadData = new DbGetData();
        taskLoadData.execute();
    }
    public void back(View view){
        Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
        startActivity(intent);
    }
}