package com.example.gametictactoesample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.gametictactoesample.Activity3game.KEY_WINNER_NAME;
import static com.example.gametictactoesample.Activity3game.KEY_WIN_SCORE;

public class Activity4 extends AppCompatActivity {
    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView textViewWinnerName;
    TextView textViewWinnerScore;
    Button buttonMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        textViewWinnerName = findViewById(R.id.textView_winner_name);
        textViewWinnerScore = findViewById(R.id.textView_winner_score);

        buttonMainMenu = findViewById(R.id.button_main_menu);
         loadScoreWinner();
    }

    private void loadScoreWinner() {

        db.collection("GameScore").document("Tic tac toe winner notes").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String WinnerName = documentSnapshot.getString(KEY_WINNER_NAME);
                            String WinnerScore = documentSnapshot.getString(KEY_WIN_SCORE);

                            //Map<String, Object> note = documentSnapshot.getData();
                            textViewWinnerName.setText(WinnerName + " has Won!");
                            textViewWinnerScore.setText("Score : " + WinnerScore);
                        } else {
                            Toast.makeText(Activity4.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity4.this, "Error!", Toast.LENGTH_SHORT).show();
                        // Log.d(TAG, e.toString());
                    }
                });
    }


}