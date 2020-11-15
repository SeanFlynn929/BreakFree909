package www.youtube.The_Break_Free_Program;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static www.youtube.The_Break_Free_Program.MainActivity.ADDICTIONSCORE;
import static www.youtube.The_Break_Free_Program.MainActivity.MESCORE;
import static www.youtube.The_Break_Free_Program.MainActivity.SHARED_PREFS;
import static www.youtube.The_Break_Free_Program.MainActivity.upAddictionScore;


public class ClockFragment extends Fragment {

    Chronometer chronometer;
    SharedPreferences sharedPref;
    EditText moneySaved;
    TextView savedMoney;
    Float savingMoney;
    Button startBtn, stopBtn, newEntryBtn;
    ArrayList<DateAmount> graphData;
    ArrayList<JournalEntry> journalEntries;
    LinearLayout journal;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock, container, false);

        graphData = loadData();
        journalEntries = loadJournalEntries();
        journal = view.findViewById(R.id.journal);
        setJournalEntries();
        sharedPref = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        moneySaved = view.findViewById(R.id.moneyEdit);
        savingMoney = sharedPref.getFloat("MONEYSAVED", 0.00f);
        moneySaved.setText("$" + savingMoney);
        savedMoney = view.findViewById(R.id.moneySaved);
        moneySaved.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (Character.isDigit(moneySaved.getText().toString().charAt(0)))
                    moneySaved.setText("$" + moneySaved.getText());

                savingMoney = Float.parseFloat(moneySaved.getText().toString().substring(1));
                savingMoney = (float) Math.round(savingMoney * 100) / 100;
                sharedPref.edit().putFloat("MONEYSAVED", savingMoney).apply();
            }

        });

        chronometer = view.findViewById(R.id.chronometer);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long t = SystemClock.elapsedRealtime() - chronometer.getBase();
                int d = (int) t / 1000 / 60 / 60 / 24;
                int h = (int) (t - d * 86400000) / 3600000;
                int m = (int) (t - d * 86400000 - h * 3600000) / 60000;
                int s = (int) (t - d * 86400000 - h * 3600000 - m * 60000) / 1000;
                String dd = d < 10 ? "0" + d : d + "";
                String hh = h < 10 ? "0" + h : h + "";
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";
                chronometer.setText(dd + ":" + hh + ":" + mm + ":" + ss);
                savedMoney.setText("$" + (float) Math.round(savingMoney * t / 1000 / 60 / 60 / 24 * 100) / 100 + " Saved so far!");
            }
        });

        startBtn = view.findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startChronometer();
            }
        });

        stopBtn = view.findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {
                resetChronometer();
                upAddictionScore();
                addTodayMessUp();
            }
        });

        newEntryBtn = view.findViewById(R.id.newEntryButton);
        newEntryBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {
                journalEntries.add(new JournalEntry(LocalDate.now(), "New Entry"));
                saveJournalEntries();
                setJournalEntries();
            }
        });

        getChronometerTime();


        return view;
    }

    private void setChronometerTime() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("TIME", chronometer.getBase());
        editor.putBoolean("GOING", true);
        editor.apply();
    }

    private void getChronometerTime() {
        if (sharedPref.getBoolean("GOING", false)) {
            chronometer.setBase(sharedPref.getLong("TIME", SystemClock.elapsedRealtime()));
            chronometer.start();
        }
    }

    private void startChronometer() {
        if (!sharedPref.getBoolean("GOING", false)) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            setChronometerTime();
            chronometer.start();
        }
    }

    private void resetChronometer() {
        chronometer.stop();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("GOING", false);
        editor.apply();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addTodayMessUp() {
        if (graphData.size() < 1) {
            graphData.add(new DateAmount(LocalDate.now().minusDays(1), 0));
        }
        DateAmount today = graphData.get(graphData.size() - 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (today.getDate().equals( LocalDate.now())) {
                graphData.add(new DateAmount(LocalDate.now(), today.getAmount() + 1));
            } else {
                graphData.add(new DateAmount(LocalDate.now(), 1));
            }
        }
        saveData();
    }

    private void saveData() {
        try {
            FileOutputStream output = getActivity().getApplicationContext().openFileOutput("graphData.data", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(output);
            out.writeObject(graphData);
            out.close();
        } catch (FileNotFoundException e) {e.printStackTrace();}
          catch (IOException e) {e.printStackTrace();}

    }

    private ArrayList<DateAmount> loadData() {
        ArrayList<DateAmount> data = null;
        try {
            FileInputStream input = getActivity().getApplicationContext().openFileInput("graphData.data");
            ObjectInputStream in = new ObjectInputStream(input);
            data = (ArrayList<DateAmount>) in.readObject();
            in.close();
            input.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(getActivity(), "No file for graph data", Toast.LENGTH_SHORT).show();
            graphData = new ArrayList<>();
            saveData();
            return graphData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;

    }

    public ArrayList<JournalEntry> loadJournalEntries() {
        ArrayList<JournalEntry> journalEntries = new ArrayList<>();
        try {
            FileInputStream input = getActivity().getApplicationContext().openFileInput("journalEntries.data");
            ObjectInputStream in = new ObjectInputStream(input);
            journalEntries = (ArrayList<JournalEntry>) in.readObject();
            in.close();
            input.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(getActivity(), "Creating file for journal data", Toast.LENGTH_SHORT).show();
            saveJournalEntries();
            return journalEntries;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return journalEntries;
    }

    public void saveJournalEntries() {
        try {
            FileOutputStream output = getActivity().getApplicationContext().openFileOutput("journalEntries.data", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(output);
            out.writeObject(journalEntries);
            out.close();
        } catch (FileNotFoundException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
    }

    @RequiresApi(api = Build.VERSION_CODES.O)

    private void setJournalEntries() {
        journal.removeAllViews();
        for (int i = 0; i < journalEntries.size(); i++) {
            JournalView journalView = new JournalView(getActivity(), null);
            journalView.setEntry(journalEntries.get(i).getEntry());
            journalView.setDate(journalEntries.get(i).getDate());
            journalView.setId(i);
            journalView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    journalView.toggleEdit();
                }
            });
            journalView.popUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    journalEntries = loadJournalEntries();
                    setJournalEntries();
                }
            });
            journalView.builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    journalEntries = loadJournalEntries();
                    setJournalEntries();
                }
            });

            journal.addView(journalView);
        }
    }




}
