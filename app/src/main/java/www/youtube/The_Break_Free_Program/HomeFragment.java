package www.youtube.The_Break_Free_Program;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;
import static www.youtube.The_Break_Free_Program.MainActivity.upMeScore;


public class HomeFragment extends Fragment {

    ArrayList<DateAmount> graphData;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Button urgeButton = view.findViewById(R.id.button1);
        urgeButton.setOnClickListener(v -> openSecondActivity(getActivity()));

        createNotificationChannel();
        Button setBtn = view.findViewById(R.id.setBtn);
        setBtn.setOnClickListener((v -> setAlarm()));

        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(v -> cancelAlarm());

        return view;
    }

    public void openSecondActivity(Context context) {
        upMeScore();
        Intent intent = new Intent(context, SecondActivity.class);
        startActivity(intent);
    }

    public void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "BreakFreeChannel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("BreakFree", name, importance);


            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setAlarm() {
        TimePicker timePicker = getActivity().findViewById(R.id.timePicker);
        EditText text = getActivity().findViewById(R.id.editText);
        Intent intent = new Intent(getActivity(), NotificationPublisher.class);
        intent.putExtra("message", text.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager =(AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        calendar.set(Calendar.MINUTE, timePicker.getMinute());
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 1);
        }

        long timeTrigger = calendar.getTimeInMillis();

        alarmManager.set(AlarmManager.RTC_WAKEUP, timeTrigger, pendingIntent);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        Date resultDate = new Date(timeTrigger);
        Toast.makeText(getActivity(), "Reminder set for " + sdf.format(resultDate), Toast.LENGTH_LONG).show();
    }

    private void cancelAlarm() {
        Intent intent = new Intent(getActivity(), NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
        Toast.makeText(getActivity(), "Reminder Cancelled", Toast.LENGTH_LONG).show();
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



    //        public class HomeFragment extends AppCompatActivity {
//package mypack;

//public class HomeFragment extends AppCompatActivity


    }


//package mypack;

//public class HomeFragment extends Fragment implements AppCompatActivity{

//public class HomeFragment extends Fragment, AppCompatActivity{

//public class HomeFragment extends Fragment {

//public class HomeFragment extends AppCompatActivity {
//}
//public class HomeFragment extends Fragment {
//}
// $$$ public class HomeFragment extends AppCompatActivity {