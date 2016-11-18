package alpha.com.apendog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.NumberPicker;



public class loginActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    public NumberPicker np  = null;
    public NumberPicker np2 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Getting picker widget from XML for age selector

        // This one is the number of years or months for age
        np = (NumberPicker) findViewById(R.id.numberPicker2);

        // This one selects months or years
        np2 = (NumberPicker) findViewById(R.id.numberPicker);


        //this makes it so values in pickers cannot be modified by user,
        //    only selected
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        // makes the picker cycle
        np.setWrapSelectorWheel(true);

        // We are populating the month/year picker with two values-"month" and "year"
        np2.setMinValue(0);

        np2.setMaxValue(1);

        np2.setDisplayedValues(new String[]{"months", "years"});

        np.setMinValue(0);

        np.setMaxValue(12);

        np2.setOnValueChangedListener(this);
    }

    public void onValueChange(NumberPicker np2, int oldVal, int newVal) {
        if (np2.getValue() == 0) {
            np.setMinValue(0);
            np.setMaxValue(12);
        }
        else {
            np.setMinValue(1);
            np.setMaxValue(25);
        }
    }

}
