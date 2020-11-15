package www.youtube.The_Break_Free_Program;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

public class JournalView extends RelativeLayout {
    private Context mContext = null;
    private RelativeLayout layout = null;
    private TextView dateView = null;
    private TextView entryView = null;
    private TextView editLink = null;
    private TextView deleteLink = null;
    private int ID;

    private LayoutParams expanded, contracted, expandedText, contractedText;
    private ArrayList<JournalEntry> journalEntries;
    public PopupWindow popUp;
    public AlertDialog.Builder builder;

    public JournalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View myView = inflater.inflate(R.layout.journal_view, this);
        layout = myView.findViewById(R.id.journalLayout);
        dateView = myView.findViewById(R.id.journalDate);
        entryView = myView.findViewById(R.id.journalEntry);
        editLink = myView.findViewById(R.id.editLink);
        deleteLink = myView.findViewById(R.id.deleteLink);
        editLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEntry();
            }
        });
        deleteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEntry();
            }
        });
        journalEntries = loadJournalEntries();
        popUp = new PopupWindow(context);
        popUp.setWidth(LayoutParams.MATCH_PARENT);

        builder = new AlertDialog.Builder(context);

        expanded = new LayoutParams(layout.getLayoutParams());
        expanded.height = LayoutParams.WRAP_CONTENT;
        contracted = new LayoutParams(layout.getLayoutParams());
        expandedText = new LayoutParams(entryView.getLayoutParams());
        expandedText.height = LayoutParams.WRAP_CONTENT;
        expandedText.setMargins(25,50,25,50);
        expandedText.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        contractedText = new LayoutParams(entryView.getLayoutParams());
        contractedText.setMargins(25,25,25,25);
        contractedText.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

    }

    public void setDate(LocalDate date) {
        dateView.setText(date.toString());
    }
    public void setEntry(String entry) {
        entryView.setText(entry);
    }
    public void setId(int ID) {this.ID = ID;  }
    public void toggleEdit() {
        if (editLink.getVisibility() == TextView.VISIBLE) {
            editLink.setVisibility(TextView.INVISIBLE);
            deleteLink.setVisibility(TextView.INVISIBLE);
            layout.setLayoutParams(contracted);
            entryView.setLayoutParams(contractedText);
        } else {
            editLink.setVisibility(TextView.VISIBLE);
            deleteLink.setVisibility(TextView.VISIBLE);
            entryView.setLayoutParams(expandedText);
            layout.setLayoutParams(expanded);
        }
    }

    private void editEntry() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.edit_popup, this, false);
        view.findViewById(R.id.entryCancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });
        EditText entryEdit = view.findViewById(R.id.entryEdit);
        entryEdit.setText(entryView.getText());
        view.findViewById(R.id.entrySave).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                entryView.setText(entryEdit.getText());
                journalEntries.get(ID).setEntry(entryEdit.getText().toString());
                saveJournalEntries();
                popUp.dismiss();
            }
        });
        popUp.setContentView(view);
        popUp.setFocusable(true);
        popUp.showAtLocation((View)layout.getParent(), Gravity.CENTER, 100, 100);
    }
    private void deleteEntry() {
        builder.setMessage("Confirm deletion of entry?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((ViewManager)layout.getParent()).removeView(layout);
                entryDelete();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public ArrayList<JournalEntry> loadJournalEntries() {
        ArrayList<JournalEntry> journalEntries = new ArrayList<>();
        try {
            FileInputStream input = mContext.getApplicationContext().openFileInput("journalEntries.data");
            ObjectInputStream in = new ObjectInputStream(input);
            journalEntries = (ArrayList<JournalEntry>) in.readObject();
            in.close();
            input.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(mContext, "Creating file for journal data", Toast.LENGTH_SHORT).show();
            saveJournalEntries();
            return journalEntries;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return journalEntries;
    }
    public void saveJournalEntries() {
        try {
            FileOutputStream output = mContext.getApplicationContext().openFileOutput("journalEntries.data", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(output);
            out.writeObject(journalEntries);
            out.close();
        } catch (FileNotFoundException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
    }
    public void entryDelete() {
        journalEntries.remove(ID);
        saveJournalEntries();
    }
}
