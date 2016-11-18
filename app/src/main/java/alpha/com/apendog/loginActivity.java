package alpha.com.apendog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;


public class loginActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    int weight = 0;
    public NumberPicker ageNumberPicker = null;
    public NumberPicker monthYearPicker = null;
    public SeekBar      weightSeeker    = null;
    public TextView     weightLabel     = null;
    public ImageView    dogPic          = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Getting picker widgets from XML for age and weight pickers
            // number of years or months for age
        ageNumberPicker = (NumberPicker) findViewById(R.id.numberPicker2);
            // selects months or years
        monthYearPicker = (NumberPicker) findViewById(R.id.numberPicker);

        // seeks weight
        weightSeeker = (SeekBar) findViewById(R.id.seekBar);

        // weight label
        weightLabel = (TextView) findViewById(R.id.textView8);
        weightLabel.setText(String.valueOf(weight));

        //so that values in pickers cannot be modified by user
        ageNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        monthYearPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        // makes the picker cycle
        ageNumberPicker.setWrapSelectorWheel(true);

        // set dog pic and scale it
        dogPic = (ImageView) findViewById(R.id.imageView);
        /*dogPic.setScaleX(dogPic.getScaleX()/2);
        dogPic.setScaleY(dogPic.getScaleY()/2);*/



        // to populate the month/year picker with two values-"month" and "year"
        monthYearPicker.setMinValue(0);
        monthYearPicker.setMaxValue(1);
        monthYearPicker.setDisplayedValues(new String[]{"months", "years"});

        // to populate the amount of month/years to 0-12 to start
        ageNumberPicker.setMinValue(0);
        ageNumberPicker.setMaxValue(12);

        // set max on seeker
        weightSeeker.setMax(200);

        // When the seeker is seeking run these
        weightSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            // send the seeker value to label
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                weightLabel.setText(String.valueOf(progress) + " pounds");
                float myFloat = (float)progress;
                double myTestDouble = (myFloat/150) + 0.5;
                float scaleFloat = (float)myTestDouble / 2;
                dogPic.setScaleX(scaleFloat);
                dogPic.setScaleY(scaleFloat);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //set value change listener
        monthYearPicker.setOnValueChangedListener(this);
    }

    // if monthYearPicker value changes. This runs
    public void onValueChange(NumberPicker np2, int oldVal, int newVal) {

        //if it is months. ageNumber picker is 0-12. Else 1-25.
        if (np2.getValue() == 0) {
            ageNumberPicker.setMinValue(0);
            ageNumberPicker.setMaxValue(12);
        }
        else {
            ageNumberPicker.setMinValue(1);
            ageNumberPicker.setMaxValue(25);
        }
    }

}
