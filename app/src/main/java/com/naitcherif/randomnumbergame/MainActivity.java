package com.naitcherif.randomnumbergame;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private final int nbr_guesses_allowed = 6;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private int the_number;
    private int score;
    private int nbr_trys;
    private Button play_button;
    private ProgressBar progressBar;
    private TextView scoreLabel;
    private TextView trysLabel;
    private EditText textInput;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play_button = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);
        scoreLabel = findViewById(R.id.scoreLabel);
        trysLabel = findViewById(R.id.essaysLabel);
        textInput = findViewById(R.id.editText);
        listView = findViewById(R.id.logListView);
        progressBar.setMax(nbr_guesses_allowed);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
        listView.smoothScrollToPosition(0);
        init();
    }

    private void getRandomNumber() {
        the_number = new Random().nextInt(100) + 1;
        Log.i("getRandomNumber: ", String.valueOf(the_number));
    }

    private void replay() {
        getRandomNumber();
        nbr_trys = 0;
        String text = "0 / " + nbr_guesses_allowed;
        trysLabel.setText(text);
        progressBar.setProgress(nbr_trys);
        listItems.clear();
        adapter.notifyDataSetChanged();
    }

    private void init() {
        score = 0;
        scoreLabel.setText(String.valueOf(score));
        replay();
    }

    private void showAlert(String title, String content) {
        new AlertDialog.Builder(this).setTitle(title)
                .setMessage(content)
                .setPositiveButton("Quit", (dialog, which) -> {
                })
                .setNegativeButton(R.string.replay_eng, (dialog, which) -> {
                    init();
                })
                .show();
    }


    public void buttonClick(View view) {
        nbr_trys += 1;
        if (nbr_trys > nbr_guesses_allowed) {
            showAlert("Game already over !", "");
        } else {
            try {
                int input_number = Integer.valueOf(textInput.getText().toString());
                progressBar.setProgress(nbr_trys);
                String text = nbr_trys + " / " + nbr_guesses_allowed;
                trysLabel.setText(text);
                if (the_number == input_number) {
                    Toast.makeText(this, "You won !", Toast.LENGTH_SHORT).show();
                    setScore();
                    replay();
                } else if (nbr_trys == nbr_guesses_allowed) {
                    showAlert("You lost !", "The number was " + the_number + "\n\nYour score is " + score);
                } else {
                    addToView(input_number);
                    if (the_number > input_number) {
                        Toast.makeText(this, "The number is bigger", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "The number is smaller", Toast.LENGTH_SHORT).show();
                    }
                }
                textInput.setText("");
            } catch (Exception e) {
                System.out.println("Error click button" + e.getMessage());
                Toast.makeText(this, "You did not enter a number", Toast.LENGTH_SHORT).show();
            }

        }


    }

    private void setScore() {
        int base_increment = 20;
        score += base_increment * nbr_guesses_allowed / nbr_trys;
        scoreLabel.setText(String.valueOf(score));
    }

    private void addToView(int input_number) {
        String s = ". " + input_number + (input_number > the_number ? " > x" : " < x");
        listItems.add(0, listItems.size() + 1 + s);
        adapter.notifyDataSetChanged();
    }

    public void replay(View view) {
        showAlert("Replay", "Do you want to reset the game ?");
    }
}
