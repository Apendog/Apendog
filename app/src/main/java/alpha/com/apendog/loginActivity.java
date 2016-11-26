package alpha.com.apendog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;


public class loginActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {
    dogProfile dogProfile = new dogProfile();
    int weight = 0;
    int energyLevel = -1;
    public Button doneButton = null;
    public NumberPicker ageNumberPicker = null;
    public NumberPicker monthYearPicker = null;
    public SeekBar      weightSeeker    = null;
    public TextView     weightLabel     = null;
    public ImageView    dogPic          = null;
    public EditText     dogName         = null;

    private static final String TAG = "Ed-Log";
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Auth creation
        mAuth = FirebaseAuth.getInstance();
        //Signout  Button
//        findViewById(R.id.sign_out_button).setOnClickListener((View.OnClickListener) this);
//Auth Listener Start
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Intent intent = new Intent(loginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
//                updateUI(user);
                // [END_EXCLUDE]
            }



        };



        // Getting picker widgets from XML for age and weight pickers
            // number of years or months for age
        ageNumberPicker = (NumberPicker) findViewById(R.id.numberPicker2);
            // selects months or years
        monthYearPicker = (NumberPicker) findViewById(R.id.numberPicker);
            // set name EditText
        dogName = (EditText) findViewById(R.id.editText2);
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

    /*****************************************************
     * ON RADIO BUTTON CLICKED
     *  sees what energy level user selected
     *****************************************************/
    public void onRadioButtonClicked(View view) {

        // Is button checked?
        boolean checked = ((RadioButton)view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.lazyButton:
                if (checked) {
                energyLevel = 0;
                }
            case R.id.mildlyTemperedButton:
                if (checked) {
                    energyLevel = 1;
                }
            case R.id.energeticButton:
                if (checked) {
                    energyLevel = 2;
                }
            case R.id.extremelyHyperButton:
                if (checked) {
                    energyLevel = 3;
                }

        }
    }
    public void setDogName() throws IOException {
        IOException e = new IOException("Dog Name");
        if (dogName.getText().toString() == null) {
            throw e;
        }
        else {
            dogProfile.setDogName(dogName.getText().toString());
        }
    }
    public void setDogAge() throws IOException {
        IOException e = new IOException("Dog Weight");
        if (ageNumberPicker.getValue() == 0) {
            throw e;
        }
        else {
            if (monthYearPicker.getValue() == 1) {
                dogProfile.setDogAge(ageNumberPicker.getValue() * 12);
            } else {
                dogProfile.setDogAge(ageNumberPicker.getValue());
            }
        }
    }
    public void setDogWeight() throws IOException {
        IOException e = new IOException();
        if (weightSeeker.getProgress() < 1) {
            throw e;
        }
        else {
            dogProfile.setDogWeight(weightSeeker.getProgress());
        }
    }
    public void setDogEnergy() throws IOException  {
        IOException e = new IOException();
        if(energyLevel > -1) {
            throw e;
        }
        else {
            dogProfile.setDogEnergy(energyLevel);
        }
    }
    public void doneButtonClick() {
        // Set name
        try {
            setDogName();
        } catch (IOException e) {
            Log.d("doneButtonClick-Clicked","Dog Name Missing.");
        }
        // Set age
        try {
            setDogAge();
        } catch (IOException e) {
            Log.d("doneButtonClick-Clicked","Dog Age Not Selected");
        }
        // Set weight
        try {
            setDogWeight();
        } catch (IOException e) {
            Log.d("doneButtonClick-Clicked","Dog Weight Not Set");
        }
        // Set energy level
        try {
            setDogEnergy();
        } catch (IOException e) {
            Log.d("doneButtonClick-Clicked","Dog Energy Not Set");
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void signOut() {
        mAuth.signOut();
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
