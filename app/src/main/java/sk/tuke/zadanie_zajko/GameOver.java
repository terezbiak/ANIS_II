package sk.tuke.zadanie_zajko;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


public class GameOver extends AppCompatActivity{

    TextView tvPoints;
    TextView highestScore;
    int currentPoints = 0;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);

        SharedPreferences preferences = getSharedPreferences("my_pref", Context.MODE_PRIVATE);
        String playersName = preferences.getString("playerName", "guest");

        tvPoints = findViewById(R.id.tvPoints);
        currentPoints = getIntent().getExtras().getInt("points", 0);
        tvPoints.setText(String.valueOf(currentPoints));
        updateHighestScore();
        DbGetData taskLoadData = new DbGetData();
        Player player = new Player(playersName, currentPoints);
        taskLoadData.execute(player);
        getGlobalHighestScore(new GlobalScoreCallback() {
            @Override
            public void onGlobalScoreReceived(Long globalHighestScore) {
                Log.d("GameOver", "Global highest score is: " + globalHighestScore);
                highestScore = findViewById(R.id.tvHighest);
                highestScore.setText(String.valueOf(globalHighestScore));
            }

            @Override
            public void onError(String message) {
                Log.w("GameOver", message);

            }
        });


    }


    class DbGetData extends AsyncTask<Player, Integer, List<Player>> {

        @Override
        protected List<Player> doInBackground(Player... players) {
            PlayerDao playerDao = DbTools.getDbContext(new WeakReference<>(GameOver.this)).playerDao();

            // Vlož nového hráča bez ohľadu na to, či databáza už obsahuje dáta
            playerDao.insertPlayers(players);

            // Vráť všetkých hráčov vrátane nového
            return playerDao.getAllPlayersSortedByScore();
        }

        @Override
        protected void onPostExecute(List<Player> players) {
            super.onPostExecute(players);

            if (!players.isEmpty()) {

                for (Player player : players) {
                    // Vytlač hráčov alebo spracuj podľa potreby
                    Log.d("Player", "ID: " + player.getId() + ", Name: " + player.getPlayerName() + ", Score: " + player.getPlayerScore());
                }
            }
        }
    }

    public void restart(View view){
        Intent intent = new Intent(GameOver.this, Tutorial.class);
        startActivity(intent);
        finish();
    }

    public void exit(View view){
        finish();
    }

    public void openScoreView(View view){
        new DbGetData().execute();
        Intent intent = new Intent(GameOver.this, HistoryActivity.class);
        startActivity(intent);
    }

    private void updateHighestScore() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userDocRef = db.collection("users").document(userEmail);

            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Long highestScore = document.getLong("highestScore");
                        if (highestScore == null || currentPoints > highestScore) {
                            // Aktualizujte najvyššie skóre v databáze
                            userDocRef.update("highestScore", currentPoints)
                                    .addOnSuccessListener(aVoid -> Log.d("GameOver", "Highest score updated successfully"))
                                    .addOnFailureListener(e -> Log.w("GameOver", "Error updating highest score", e));
                        }
                    } else {
                        // Dokument neexistuje, vytvorte nový dokument s aktuálnym skóre
                        Map<String, Object> data = new HashMap<>();
                        data.put("highestScore", currentPoints);

                        userDocRef.set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("GameOver", "New user document created with highest score");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("GameOver", "Error creating new user document", e);
                                    }
                                });

                    }
                } else {
                    Log.d("GameOver", "Error getting user document: ", task.getException());
                }
            });
        }
    }

    //highest score of the player
    private void getHighestScore(ScoreCallback callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail(); // Alebo user.getUid(), ak používate UID ako ID dokumentu
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userDocRef = db.collection("users").document(userEmail);

            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Long highestScore = document.getLong("highestScore");
                        callback.onScoreReceived(highestScore != null ? highestScore : 0);
                    } else {
                        callback.onError("Document does not exist.");
                    }
                } else {
                    callback.onError("Failed to fetch document: " + task.getException().getMessage());
                }
            });
        } else {
            callback.onError("User not logged in.");
        }
    }

    //global highest score
    private void getGlobalHighestScore(GlobalScoreCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .orderBy("highestScore", Query.Direction.DESCENDING) // Zoradenie dokumentov podľa skóre, najvyššie prvé
                .limit(1) // Získajte len dokument s najvyšším skóre
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            // Predpokladajme, že každý dokument obsahuje pole "highestScore"
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            Long globalHighestScore = documentSnapshot.getLong("highestScore");
                            callback.onGlobalScoreReceived(globalHighestScore != null ? globalHighestScore : 0);
                        } else {
                            callback.onError("No scores found in database.");
                        }
                    } else {
                        callback.onError("Error fetching scores: " + task.getException().getMessage());
                    }
                });
    }


}
