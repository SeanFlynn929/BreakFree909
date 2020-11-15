package www.youtube.The_Break_Free_Program;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static www.youtube.The_Break_Free_Program.MainActivity.SHAREDPREFERENCES;

public class SecondActivity extends AppCompatActivity {

    EditText notes;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        notes = findViewById(R.id.toolsNotes);
        notes.setText(SHAREDPREFERENCES.getString("TOOLSNOTES", ""));
        save = findViewById(R.id.button_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHAREDPREFERENCES.edit().putString("TOOLSNOTES", notes.getText().toString()).apply();
                Toast.makeText(SecondActivity.this, "Notes saved", Toast.LENGTH_SHORT).show();
            }
        });



    }


}