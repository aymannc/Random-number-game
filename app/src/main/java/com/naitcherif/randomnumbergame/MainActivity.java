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
    private int nbr_tries;
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
        nbr_tries = 0;
        String text = nbr_tries + " / " + nbr_guesses_allowed;
        trysLabel.setText(text);
        progressBar.setProgress(nbr_tries);
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
                .setPositiveButton(getString(R.string.quit_string), (dialog, which) -> {
                })
                .setNegativeButton(getString(R.string.replay_string), (dialog, which) -> {
                    init();
                })
                .show();
    }


    public void buttonClick(View view) {
        nbr_tries++;
        if (nbr_tries > nbr_guesses_allowed) {
            showAlert(getString(R.string.game_alr_over_string), "");
        } else {
            try {
                int input_number = Integer.valueOf(textInput.getText().toString());
                progressBar.setProgress(nbr_tries);
                String text = nbr_tries + " / " + nbr_guesses_allowed;
                trysLabel.setText(text);
                if (the_number == input_number) {
                    Toast.makeText(this, R.string.win_string, Toast.LENGTH_SHORT).show();
                    setScore();
                    replay();
                } else if (nbr_tries == nbr_guesses_allowed) {
                    showAlert(getString(R.string.you_lost_string), getString(R.string.reveal_number_string) +
                            the_number + "\n\n" + getString(R.string.show_score_string) + score);
                } else {
                    addElementToListView(input_number);
                    if (the_number > input_number) {
                        Toast.makeText(this, getString(R.string.bigger_number_string), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(this, getString(R.string.small_number_string), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                textInput.setText("");
            } catch (Exception e) {
                System.out.println("Error click button" + e.getMessage());
                nbr_tries--;
                Toast.makeText(this, getString(R.string.empty_number_click), Toast.LENGTH_SHORT)
                        .show();
            }

        }


    }

    private void setScore() {
        int base_increment = 20;
        score += base_increment * nbr_guesses_allowed / nbr_tries;
        scoreLabel.setText(String.valueOf(score));
    }

    private void addElementToListView(int input_number) {
        String s = ". " + input_number + (input_number > the_number ? " > x" : " < x");
        listItems.add(0, listItems.size() + 1 + s);
        adapter.notifyDataSetChanged();
    }

    public void replay(View view) {
        showAlert(getString(R.string.reply_game), "Do you want to reset the game ?");
    }
}
