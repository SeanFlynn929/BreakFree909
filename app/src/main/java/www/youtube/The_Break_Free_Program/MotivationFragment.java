package www.youtube.The_Break_Free_Program;

import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;

import static www.youtube.The_Break_Free_Program.MainActivity.ADDICTIONSCORE;
import static www.youtube.The_Break_Free_Program.MainActivity.MESCORE;


public class MotivationFragment extends Fragment {
    private int quoteNum = 0;
    TextView quoteText, addictionScore, meScore;
    GraphView graphView;
    ArrayList<DateAmount> graphData;
    private Resources res;
    private String[] Quotes;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_motivation, container, false);

        Button quoteButton = view.findViewById(R.id.button);
        quoteButton.setOnClickListener(v -> getNextQuote());
        quoteText = view.findViewById(R.id.textView11);
        res = getActivity().getResources();
        Quotes = res.getStringArray(R.array.quotes);
        quoteText.setText(Quotes[quoteNum]);

        //Set scores
        addictionScore = view.findViewById(R.id.addictionScore);
        meScore = view.findViewById(R.id.meScore);
        graphView = view.findViewById(R.id.dotGraph);
        graphView.getGridLabelRenderer().setLabelFormatter(new LabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    Date d = new Date((long) (value));
                    return (String) DateFormat.format("MM/dd", d);
                }
                return "" + (int) value;
            }

            @Override
            public void setViewport(Viewport viewport) {

            }
        });

        graphView.setTitle("Times messed up per day");

        meScore.setText(MESCORE + "");
        addictionScore.setText(ADDICTIONSCORE + "");

        graphData = new ArrayList<DateAmount>();

        graphData = loadData();
        setGraph();

        return view;
    }

    private void getNextQuote() {
        if (++quoteNum == Quotes.length) {
            quoteNum = 0;
        }
        quoteText.setText(Quotes[quoteNum]);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setGraph() {
        PointsGraphSeries<DataPoint> lineSeries = new PointsGraphSeries<>(getDataPoints());
        graphView.addSeries(lineSeries);
        graphView.getViewport().setMinX(lineSeries.getLowestValueX());
        graphView.getViewport().setMaxX(lineSeries.getHighestValueX() + (1000 * 60 * 60 * 24));
        graphView.getViewport().setXAxisBoundsManual(true);
        int labels = (int) Math.round( (lineSeries.getHighestValueX() - lineSeries.getLowestValueX() ) / (1000 * 60 * 60 * 24)) + 2;
        graphView.getGridLabelRenderer().setNumHorizontalLabels(labels);
        graphView.getGridLabelRenderer().setHumanRounding(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private DataPoint[] getDataPoints() {
        DataPoint[] data = new DataPoint[graphData.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = new DataPoint(Date.from(graphData.get(i).getDate().atStartOfDay().toInstant(ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now()))), graphData.get(i).getAmount());
        }
        return data;
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
            Toast.makeText(getActivity(), "No file for data", Toast.LENGTH_SHORT).show();
            graphData = new ArrayList<>();
            return graphData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;

    }

}
