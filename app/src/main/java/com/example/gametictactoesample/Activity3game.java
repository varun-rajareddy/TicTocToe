package com.example.gametictactoesample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.usb.UsbConfiguration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.gametictactoesample.MainActivity.KEY_NAME1;
import static com.example.gametictactoesample.MainActivity.KEY_NAME2;

public class Activity3game extends AppCompatActivity {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static final String KEY_WINNER_NAME = "Winner Name ";
    public static final String KEY_WIN_SCORE = "Winner Score ";

    TextView textViewRetriveName1;
    TextView textViewRetriveName2;
    TextView textViewPlayer1Score;
    TextView textViewPlayer2Score;
    TextView textViewPlayerturn;

    // 0: yellow, 1: red, 2: empty

    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    int activePlayer = 0;

    boolean gameActive = true;
    int countSteps =0;
    int player1Score = 0;
    int player2Score = 0;

    String name1 , name2; // used to stored retived names from data base




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity3game);

        textViewRetriveName1 = findViewById(R.id.textView_Retrive_Player1);
        textViewRetriveName2 = findViewById(R.id.textView_Retrive_Player2);

        loadNames(textViewRetriveName1);




    }


    public void loadNames(View v) {
        db.collection("Game").document("Tic tac toe notes").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                             name1 = documentSnapshot.getString(KEY_NAME1);
                             name2 = documentSnapshot.getString(KEY_NAME2);

                            //Map<String, Object> note = documentSnapshot.getData();
                            textViewRetriveName1.setText( name1);
                            textViewRetriveName2.setText( name2);
                        } else {
                            Toast.makeText(Activity3game.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity3game.this, "Error!", Toast.LENGTH_SHORT).show();
                        // Log.d(TAG, e.toString());
                    }
                });
    }

    public void dropIn(View view){
        ImageView counter = (ImageView) view;

        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        textViewPlayerturn = findViewById(R.id.textView_Player_Turn);
        textViewPlayer1Score = findViewById(R.id.textView_Score_Player1);
        textViewPlayer2Score = findViewById(R.id.textView_Score_Player2);

        if ( (gameState[tappedCounter] == 2 && gameActive ) && countSteps <=9 ) {

            gameState[tappedCounter] = activePlayer;

            counter.setTranslationY(-1500);


            if (activePlayer == 0) {

                counter.setImageResource(R.drawable.yellow);

                activePlayer = 1;
                countSteps++;
                textViewPlayerturn.setText("Player 2 Turn");

            } else {

                counter.setImageResource(R.drawable.red);

                activePlayer = 0;
                countSteps++;
                textViewPlayerturn.setText("Player 1 Turn");
            }

            counter.animate().translationYBy(1500).rotation(3600).setDuration(300);

            for (int[] winningPosition : winningPositions) {

                String winner = "";
                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2) {



                    gameActive = false;



                    if (activePlayer == 1) {

                        winner = "Yellow";
                        player1Score++;

                    } else {

                        winner = "Red";
                        player2Score++;

                    }

                    Button playAgainButton = (Button) findViewById(R.id.playAgainButton);





                  textViewPlayerturn.setText(winner + " has won!");

                    if(player1Score == 3 ){
                        savePlayer1Score(name1,player1Score);
                        quitGame();
                    }else if(player2Score ==3){
                        savePlayer2Score(name2,player2Score);
                        quitGame();
                    }

                    textViewPlayer2Score.setText(String.valueOf(player2Score));
                    textViewPlayer1Score.setText(String.valueOf(player1Score));



                  //  textViewPlayerturn.setVisibility(View.INVISIBLE);

                    playAgainButton.setVisibility(View.VISIBLE);

                }

                if(countSteps >= 9 && winner == "" ){

                    Button playAgainButton = (Button) findViewById(R.id.playAgainButton);

                    textViewPlayerturn.setText( " Draw!");

                 //   textViewPlayerturn.setVisibility(View.INVISIBLE);

                    playAgainButton.setVisibility(View.VISIBLE);

                }

            }
        }
    }

    public void playAgain(View v){

        Button playAgainButton = (Button) findViewById(R.id.playAgainButton);

        playAgainButton.setVisibility(View.INVISIBLE);

        textViewPlayerturn.setText("Player 1 Turn");



        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);

        for(int i=0; i<gridLayout.getChildCount(); i++) {

            ImageView counter = (ImageView) gridLayout.getChildAt(i);

            counter.setImageDrawable(null);

        }


        for (int i=0; i<gameState.length; i++) {

            gameState[i] = 2;

        }

        activePlayer = 0;

        gameActive = true;

        countSteps = 0;

        winningPositions = new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};



    }

    public  void quitGame(){


        Intent intent4 = new Intent(this, Activity4.class);

        startActivity(intent4);


    }

    public void savePlayer1Score(String name1, int player1Score){

        Map<String, Object> note2 = new HashMap<>();
        note2.put(KEY_WINNER_NAME , name1);
        note2.put(KEY_WIN_SCORE , String.valueOf(player1Score));

        db.collection("GameScore").document("Tic tac toe winner notes").set(note2)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Activity3game.this, "Score saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity3game.this, "Error!", Toast.LENGTH_SHORT).show();
                      //  Log.d(TAG, e.toString());
                    }
                });

    }

    public void savePlayer2Score(String name2, int player2Score){

        Map<String, Object> note2 = new HashMap<>();
        note2.put(KEY_WINNER_NAME , name2);
        note2.put(KEY_WIN_SCORE , String.valueOf(player2Score));

        db.collection("GameScore").document("Tic tac toe winner notes").set(note2)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Activity3game.this, "Score saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity3game.this, "Error!", Toast.LENGTH_SHORT).show();
                        //  Log.d(TAG, e.toString());
                    }
                });

    }
}