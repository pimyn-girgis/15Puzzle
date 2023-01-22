package com.example.bemen_15_puzzle;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Integer move_count;

    List <List<Integer>> buttons = new ArrayList<>();

    private void createMap(boolean test) {
        move_count = 0;

        List<Integer> list = new ArrayList<>();

        for (int i = 1; i <= 15; ++i)
            list.add(i);

        list.add(0);

        if(!test) {
            java.util.Collections.shuffle(list);
        }

        int begin = 0;
        for (List<Integer> col : buttons) {
            for (Integer id : col) {
                Integer val = (list.get(begin++));
                Button temp = findViewById(id);
                temp.setText(val == 0? "" : val.toString());
            }
        }
    }

    private boolean is_valid(int i) {
        return i < 4 && i >= 0;
    }

    private boolean is_valid(int x, int y) {
        return is_valid(x) && is_valid(y);
    }

    private boolean is_empty(int x, int y) {
        return ((Button)findViewById(buttons.get(x).get(y))).getText() == "";
    }

    private boolean is_solved() {

        outer_loop:
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {

                if(i == 3 && j == 3)
                    break outer_loop;

                Button temp = findViewById(buttons.get(i).get(j));
                Log.d("int", ((Integer)(i * 4 + j + 1)).toString());
                Log.d("text", temp.getText().toString());
                if(temp.getText() != ((Integer)(4 * i + j + 1)).toString()) {
                    return false;
                }
            }
        }

        return true;
    }

    private void win_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("You Win!");
        builder.setMessage("You have completed the puzzle in " + move_count.toString() + " moves!");
        move_count = 0;
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void move_button(final Integer pos_x, final Integer pos_y) {
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};

        for (int i = 0; i < 4; ++i) {
            int x = pos_x + dx[i];
            int y = pos_y + dy[i];
            if(is_valid(x, y) && is_empty(x, y)) {
                ++move_count;
                Button from = findViewById(buttons.get(pos_x).get(pos_y));
                Button to = findViewById(buttons.get(x).get(y));
                to.setText(from.getText());
                from.setText("");

                if(is_solved())
                    win_dialog();

                break;
            }
        }
    }

    private void get_number_buttons_ids() {
        ViewGroup layout = findViewById(R.id.buttons);
        for(int i = 0; i < layout.getChildCount(); ++i) {
            ViewGroup column = (ViewGroup)layout.getChildAt(i);
            buttons.add(new ArrayList<>());
            for(int j = 0; j < column.getChildCount(); ++j) {
                buttons.get(i).add((column.getChildAt(j)).getId());
            }
        }
    }

    private void initialize() {
        get_number_buttons_ids();

        Button start_button = findViewById(R.id.start_button);
        CheckBox test = findViewById(R.id.test_check_box);
        start_button.setOnClickListener(v -> createMap(test.isChecked()));

        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j < 4; ++j) {
                Button numbered_button = findViewById(buttons.get(i).get(j));
                int finalI = i; int finalJ = j;
                numbered_button.setOnClickListener(v -> move_button(finalI, finalJ));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

}