package com.example.gametictactoesample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


   public static final String TAG = "MainActivity";
    public static final String KEY_NAME1 = "name1";
    public static final String KEY_NAME2 = "name2";

    public EditText editTextName1;
    public EditText editTextName2;

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextName1 = findViewById(R.id.edit_Text_Name1);
        editTextName2 = findViewById(R.id.edit_Text_Name2);



        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

    }


    public void openActivity2() {

        Intent intent = new Intent(this, Activity2.class);

        startActivity(intent);
    }



    public void saveNote(View v) {
        String name1 = editTextName1.getText().toString();
        String name2 = editTextName2.getText().toString();

        Map<String, Object> note = new HashMap<>();
        note.put(KEY_NAME1 , name1);
        note.put(KEY_NAME2 , name2);

        db.collection("Game").document("Tic tac toe notes").set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Names saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }
}