package layout;


import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.fragmenttest.R;
import com.example.fragmenttest.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeFragment extends Fragment implements View.OnClickListener{


    EditText timeMinuteEditText;
    EditText timeHourEditText;

    //Timer timer;
    //MyTimerTask myTimerTask;

    MainActivity mainActivity;

    //int countDownPeriod_s;
    int countDownPeriod_h;
    int countDownPeriod_m;

    long cdTimerMillisUntilFinished;
    int cdTimerSecUntilFinished;

    public boolean myCDTimerIsOn;


    String str_btn_time_start;
    String str_btn_time_reset;

    public TimeFragment() {
        // Required empty public constructor

        //for now hard set it here
        //countDownPeriod_s = 30;

        countDownPeriod_h = 0;
        countDownPeriod_m = 0;


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainActivity = (MainActivity) getActivity(); // for some reason this line returns null if put in the constructor
        str_btn_time_start = getResources().getString(R.string.btn_time_start); // for some reason this line returns null if put in the constructor
        str_btn_time_reset = getResources().getString(R.string.btn_time_reset); // for some reason this line returns null if put in the constructor

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time, container, false);
        Button btn = (Button) view.findViewById(R.id.button);
        btn.setOnClickListener(this);
        timeMinuteEditText = (EditText) view.findViewById(R.id.time_minute_editText);
        timeHourEditText = (EditText) view.findViewById(R.id.time_hour_editText);


        if (myCDTimerIsOn) {


            btn.setText(str_btn_time_reset);
        } else {


            //btn.setText(str_btn_time_start); //this line is uneccessary since the system does this by default;

        }
        refreshTimeText();

        return view;
    }



    @Override
    public void onClick(View view) {
        Button btn = (Button) view;
        //btn.setOnClickListener(this);
        String btnText = (String) btn.getText();


        if (btnText.equals(str_btn_time_start)){
/*
            timer = new Timer();
            myTimerTask = new MyTimerTask();

            timer.schedule(myTimerTask, 5000);
*/
            cdTimerStart();

            btn.setText(str_btn_time_reset);
        } else {
/*
            if (timer != null){
                timer.cancel();
                timer = null;
            }


*/
            cdTimerStop();

            btn.setText(str_btn_time_start);
        }

    }

    void cdTimerStart () {
        disableTimeTexts();

        long textTimeHour = Integer.parseInt(timeHourEditText.getText().toString());
        long textTimeMinute = Integer.parseInt(timeMinuteEditText.getText().toString());

        long millsTimeHour = textTimeHour*60*60*1000;
        long millsTimeMinute = textTimeMinute*60*1000;

        long millsTimeTotal = millsTimeHour + millsTimeMinute;
        mainActivity.startCDTimer(millsTimeTotal);


        //progress bar
        ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 0, 500); // see this max value coming back here, we animale towards that value
        animation.setDuration (5000); //in milliseconds
        animation.setInterpolator (new DecelerateInterpolator());
        animation.start ();

    }

    void cdTimerStop() {
        mainActivity.stopCDTimer();

        countDownPeriod_h = 0;
        countDownPeriod_m = 0;

        refreshTimeText();

        enableTimeTexts();
    }

    public void cdTimerOnTick (long millisUntilFinished) {
        cdTimerMillisUntilFinished = millisUntilFinished;
        double cdTimerMillisUntilFinishedDouble = (double) cdTimerMillisUntilFinished;
        double cdTimerSecUntilFinishedDouble = cdTimerMillisUntilFinishedDouble/1000;
        //cdTimerSecUntilFinished = (int) Math.ceil(cdTimerSecUntilFinishedDouble);

        double cdTimerMinUntilFinishedDouble = cdTimerSecUntilFinishedDouble/60;
        double cdTimerHourUntilFinishedDouble = cdTimerMinUntilFinishedDouble/60;

        countDownPeriod_h = (int) Math.floor(cdTimerHourUntilFinishedDouble);

        long timeTakenByHoursInMillis = countDownPeriod_h*60*60*1000;
        long timeLeftForMinutesInMillis = millisUntilFinished - timeTakenByHoursInMillis;

        double timeLeftForMinutesInMillisDouble = (double) timeLeftForMinutesInMillis;
        double timeLeftForMinutesInSecDouble = timeLeftForMinutesInMillisDouble/1000;
        double timeLeftForMinutesInMinutesDouble = timeLeftForMinutesInSecDouble/60;

        countDownPeriod_m = (int) Math.ceil(timeLeftForMinutesInMinutesDouble);

        refreshTimeText();


    }

    public void cdTimerOnFinish() {
        cdTimerStop();
    }

    private void refreshTimeText () {
        if (countDownPeriod_h < 10) {
            String sCountDownPeriod_h = "0"+countDownPeriod_h;
            timeHourEditText.setText(sCountDownPeriod_h);

        } else {
            timeHourEditText.setText(Integer.toString(countDownPeriod_h));

        }

        if (countDownPeriod_m < 10) {
            String sCountDownPeriod_m = "0"+countDownPeriod_m;
            timeMinuteEditText.setText(sCountDownPeriod_m);
        } else {
            timeMinuteEditText.setText(Integer.toString(countDownPeriod_m));

        }
    }

    private void disableTimeTexts() {
        disableEditText(timeHourEditText);
        disableEditText(timeMinuteEditText);
    }
    private void disableEditText(EditText editText) {
        //editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);

        //Log.d("getKeyListener : ", KeyListener.class.toString(editText.getKeyListener()) );

        //editText.setBackgroundColor(Color.TRANSPARENT);
    }

    private void enableTimeTexts() {
        enableEditText(timeHourEditText);
        enableEditText(timeMinuteEditText);
    }
    private void enableEditText(EditText editText) {
        //editText.setFocusable(true);
        editText.setEnabled(true);
        editText.setCursorVisible(true);
        //editText.setBackgroundColor(Color.BLACK);
    }
/*

    public class MyTimerTask extends TimerTask {

        //int iCount = 0;

        @Override
        public void run() {
            //iCount ++;

            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timeTextView.setText("success");

                    }
                });
            }

        }
    }
*/

}
