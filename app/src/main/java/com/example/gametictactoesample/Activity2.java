package com.example.gametictactoesample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.Key;

import static com.example.gametictactoesample.MainActivity.KEY_NAME1;
import static com.example.gametictactoesample.MainActivity.KEY_NAME2;

public class Activity2 extends AppCompatActivity {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView textViewRetriveName1;
    TextView textViewRetriveName2;
    Button buttonPlayGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        textViewRetriveName1 = findViewById(R.id.textView_retrive_name1);
        textViewRetriveName2 = findViewById(R.id.textView_retrive_name2);
        buttonPlayGame = findViewById(R.id.button_play_game);
        loadNote(textViewRetriveName1);

         buttonPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });

    }


    public void openActivity3() {

        Intent intent3 = new Intent(this,Activity3game.class);

        startActivity(intent3);
    }


    public void loadNote(View v) {
        db.collection("Game").document("Tic tac toe notes").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String name1 = documentSnapshot.getString(KEY_NAME1);
                            String name2 = documentSnapshot.getString(KEY_NAME2);

                            //Map<String, Object> note = documentSnapshot.getData();
                            textViewRetriveName1.setText("Player 1 " + name1);
                            textViewRetriveName2.setText("Player 2 " + name2);
                        } else {
                            Toast.makeText(Activity2.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity2.this, "Error!", Toast.LENGTH_SHORT).show();
                       // Log.d(TAG, e.toString());
                    }
                });
    }

}