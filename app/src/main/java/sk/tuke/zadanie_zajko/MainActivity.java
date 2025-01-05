
package sk.tuke.zadanie_zajko;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private String displayName;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestLocationPermission();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userDocRef = db.collection("users").document(userEmail);

            userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String userName = document.getString("userName");
                            ((TextView) findViewById(R.id.user)).setText("Hello " + userName +"!");

                            SharedPreferences preferences = getSharedPreferences("my_pref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("playerName", userName);
                            editor.apply();
                        } else {
                            ((TextView) findViewById(R.id.user)).setText("Hello guest!");

                            SharedPreferences preferences = getSharedPreferences("my_pref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("playerName", "guest");
                            editor.apply();
                        }
                    } else {
                        // Dotaz zlyhal
                        ((TextView) findViewById(R.id.user)).setText("Hello guest!");
                        Log.d("Firestore", "Error getting documents: ", task.getException());
                        SharedPreferences preferences = getSharedPreferences("my_pref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("playerName", "guest");
                        editor.apply();
                    }
                }
            });
        } else {
            // Používateľ nie je prihlásený
            ((TextView) findViewById(R.id.user)).setText("Hello guest!");
            SharedPreferences preferences = getSharedPreferences("my_pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("playerName", "guest");
            editor.apply();
        }



    }
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // access accepted
            } else {
                // access dennied
            }
        }
    }

    public void startGame(View view) {
        //GameView gameView = new GameView(this);

        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String playersName = preferences.getString("userName", "guest");

        //((TextView) findViewById(R.id.user)).setText("Hello " + playersName);


        SharedPreferences preferences_score = getSharedPreferences("userName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences_score.edit();
        editor.putString("playerName", playersName);
        editor.apply();

        Intent intent = new Intent(MainActivity.this, Tutorial.class);
        startActivity(intent);
    }

    public void login(View view){
        Intent intent = new Intent(MainActivity.this, RegisterAndLogin.class);
        startActivity(intent);
    }
}





