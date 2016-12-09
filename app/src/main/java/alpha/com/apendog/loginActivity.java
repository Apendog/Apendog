package alpha.com.apendog;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class loginActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {
    public dogProfile dogProfile = new dogProfile();
    int weight = 0;
    int energyLevel = -1;
    public Button doneButton = null;
    public NumberPicker ageNumberPicker   = null;
    public NumberPicker monthYearPicker   = null;
    public SeekBar      weightSeeker      = null;
    public TextView     weightLabel       = null;
    public ImageView    dogPic            = null;
    public EditText     dogName           = null;
    public EditText     calorieCount      = null;
    public NumberPicker calorieTypePicker = null;
    public AlertDialog alertDialog = null;

    private static final String TAG = "Ed-Log";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Auth creation

/***************************************************************
 *  Getting Auth instance and setting listener.
 ***************************************************************/
        mAuth = FirebaseAuth.getInstance();

        // Getting Database instance.
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

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
            }
        };

/***************************************************************
 *  hook up the widgets-populate them-sset up listeners
 ***************************************************************/

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
        //set value change listener
        monthYearPicker.setOnValueChangedListener(this);

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

        calorieCount = (EditText) findViewById(R.id.editText3);
        calorieTypePicker = (NumberPicker) findViewById(R.id.numberPicker3);
        calorieTypePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        calorieTypePicker.setMinValue(0);
        calorieTypePicker.setMaxValue(1);
        calorieTypePicker.setDisplayedValues(new String[]{"kcal/kg", "kcal/cup"});

        // an alert box for if form not valid.
        alertDialog = new AlertDialog.Builder(loginActivity.this).create();
        alertDialog.setTitle("Form Incomplete");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Got It",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

    }

    /****************************************************
     * CALCULATE BUSINESS ONE
     *  this calculates how long the dog can hold bladder.
     *  Needs to know dogAge so make sure dogAge is set
     *  before this is executed
     ***************************************************/
    public void calcBusinessOne() {
        // if dog is less than eight months then it can hold
        //  its bladder an hour for each month alive.
        if (dogProfile.getDogAge() < 8) {
            dogProfile.setPeeHours(dogProfile.getDogAge());
        }
        // if older than 8 months then 8 hours is average dog bladder hold time
        else {
            dogProfile.setPeeHours(8);
        }
        dogProfile.setLastPeed(new Date());
    }
    /*************************************
     * CALCULATE BUSINESS TWO
     *  This calculates how long dog can hold poo.
     *  Needs to know dogAge so make sure dogAge is set
     *  before this is executed.
     *************************************/
    public void calBusinessTwo() {
        dogProfile.setPooHours(24);
        dogProfile.setLastPooed(new Date());
    }
    /*************************************
     * CALCULATE FEED
     *  Calculates how many calories the dog should eat each meal
     *************************************/
    public void calFeed() {
        int weight;
        double dailyCal;
        double multiplier;
        int mealCal;
        if (dogProfile.getDogAge() <= 4) {
            multiplier = 3;
        }
        else if (dogProfile.getDogAge() > 4 && dogProfile.getDogAge() < 9) {
            multiplier = 2;
        }
        else {
            multiplier = 1;
        }

        weight = dogProfile.getDogWeight();
        dailyCal =  weight / 2.2;
        double j = Math.pow(dailyCal, 0.750);
        dailyCal = 70 * j;
        dailyCal = dailyCal * multiplier;
        mealCal = (int) Math.round(dailyCal / 2);
        dogProfile.setCalPerMeal(mealCal);
        boolean meal0 = false;
        boolean meal1 = false;
        dogProfile.setMeal0(meal0);
        dogProfile.setMeal1(meal1);
    }
    /*************************************
     * CALCULATE EXERCISE
     *  calculates how long the walks need to be and how many
     *************************************/
    public void calExercise() {
        int energyLevel = dogProfile.getDogEnergy();
        switch (energyLevel) {
            case 0:
                dogProfile.setWalkCount(1);
                dogProfile.setWalkDuration(15);
                break;
            case 1:
                dogProfile.setWalkCount(1);
                dogProfile.setWalkDuration(20);
                break;
            case 2:
                dogProfile.setWalkCount(2);
                dogProfile.setWalkDuration(15);
                break;
            case 3:
                dogProfile.setWalkCount(2);
                dogProfile.setWalkDuration(30);
        }
        boolean walk0 = false;
        boolean walk1 = false;
        dogProfile.setWalk0(walk0);
        dogProfile.setWalk1(walk1);
    }
    /****************************************
     * DO CALCULATIONS
     *  executes methods that calculate dog data
     ****************************************/
    public void doCalculations() {
        calcBusinessOne();
        calBusinessTwo();
        calFeed();
        calExercise();
    }

    /*****************************************************
     * * ON RADIO BUTTON CLICKED
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
        IOException e = new IOException("-Dog Name\n");
        if (dogName.getText().toString().isEmpty()) {
            throw e;
        }
        else {
            dogProfile.setDogName(dogName.getText().toString());
        }
    }
    public void setDogAge() throws IOException {
        IOException e = new IOException("-Dog Age\n");
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
        IOException e = new IOException("-Dog Weight\n");
        if (weightSeeker.getProgress() < 1) {
            throw e;
        }
        else {
            dogProfile.setDogWeight(weightSeeker.getProgress());
        }
    }
    public void setDogEnergy() throws IOException  {
        IOException e = new IOException("-Dog Energy\n");
        if(energyLevel == -1) {
            throw e;
        }
        else {
            dogProfile.setDogEnergy(energyLevel);
        }
    }
    public void setCalorieCount() throws IOException {
        IOException e = new IOException("-Calorie Count of Food");
        if (calorieCount.getText().toString().isEmpty()){
            throw e;
        }
        else {
            double calorieDouble = Integer.parseInt(calorieCount.getText().toString());
            // if picker is kg -> convert
            if (calorieTypePicker.getValue() == 0){
                calorieDouble = calorieDouble * .10520751;
                int calorieInt = (int) Math.round(calorieDouble);
                dogProfile.setCalorieCount(calorieInt);
            }
            else {
                dogProfile.setCalorieCount(Integer.parseInt(calorieCount.getText().toString()));
            }
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    /*******************************************
     * Send Dog Data
     *  send dog data to the database.
     *  Will run when "Done" button is clicked
     *  and the form is valid.
     *******************************************/
    public void sendDogData() {
        Toast.makeText(this, "Sending...", Toast.LENGTH_SHORT).show();
        final String userId = getUid();
        final String dogName = dogProfile.getDogName();
        final int dogAge = dogProfile.getDogAge();
        final int dogWeight = dogProfile.getDogWeight();
        final int dogEnergy = dogProfile.getDogEnergy();
        final int calorieCount = dogProfile.getCalorieCount();
        final int peeHours = dogProfile.getPeeHours();
        final int pooHours = dogProfile.getPooHours();
        final int calPerMeal = dogProfile.getCalPerMeal();
        final int walkDuration = dogProfile.getWalkDuration();
        final int walkCount = dogProfile.getWalkCount();
        final Date lastPeed = dogProfile.getLastPeed();
        final Date lastPooed = dogProfile.getLastPooed();
        final boolean walk0 = dogProfile.getWalk0();
        final boolean walk1 = dogProfile.getWalk1();
        final boolean meal0 = dogProfile.getMeal0();
        final boolean meal1 = dogProfile.getMeal1();



        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
//                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (userId == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(loginActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeDogProfile(userId, dogName, dogAge, dogWeight, dogEnergy, calorieCount, peeHours,
                                    pooHours, calPerMeal, walkDuration, walkCount, lastPeed, lastPooed, walk0,
                                    walk1, meal0, meal1);
                        }

                        // Finish this Activity, back to the stream
//                        setEditingEnabled(true);
                        Toast.makeText(loginActivity.this,
                                "Success! Created a new dog profile.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
//                        setEditingEnabled(true);
                        Toast.makeText(loginActivity.this,
                                "Error: Dog profile was not created.",
                                Toast.LENGTH_SHORT).show();
                        // [END_EXCLUDE]
                    }
                });

    }

    /**********************************
     * DONE BUTTON CLICK
     *  this is executed when button is clicked
     *  and sets the variables of dogProfile.
     *********************************/
    public void doneButtonClick(View view) {
        String errorMessage = " ";
        boolean formValid = true;
        Log.d("doneButtonClick-Clicked", "Button was clicked!");
        // Set name
        try {
            setDogName();
        } catch (IOException e) {
            Log.d("doneButtonClick-Clicked", "Dog Name Missing.");
            formValid = false;
            errorMessage = e.getMessage();
        }
        // Set age
        try {
            setDogAge();
        } catch (IOException e) {
            Log.d("doneButtonClick-Clicked", "Dog Age Not Selected");
            formValid = false;
            errorMessage = errorMessage + e.getMessage();
        }
        // Set weight
        try {
            setDogWeight();
        } catch (IOException e) {
            Log.d("doneButtonClick-Clicked", "Dog Weight Not Set");
            formValid = false;
            errorMessage = errorMessage + e.getMessage();
        }
        // Set energy level
        try {
            setDogEnergy();
        } catch (IOException e) {
            Log.d("doneButtonClick-Clicked", "Dog Energy Not Set");
            formValid = false;
            errorMessage = errorMessage + e.getMessage();
        }
        // set calorie count
        try {
            setCalorieCount();
        }
        catch (IOException e){
            Log.d("doneButtonClick-Clicked", "Calorie Count Not Set");
            errorMessage = errorMessage + e.getMessage();
        }
        doCalculations();
        if(formValid) {
            Log.d("Print of Dog Data", "Variables in dogProfile class:");
            Log.d("Print of Dog Data", "Dog Name: " + dogProfile.getDogName());
            Log.d("Print of Dog Data", "Dog Age: " + String.valueOf(dogProfile.getDogAge()));
            Log.d("Print of Dog Data", "Dog Weight: " + String.valueOf(dogProfile.getDogWeight()));
            Log.d("Print of Dog Data", "Dog Energy: " + String.valueOf(dogProfile.getDogEnergy()));
            Log.d("Print of Dog Data", "Calorie Count (kcal/cup): " + String.valueOf(dogProfile.getCalorieCount()));
            Log.d("Print of Dog Data", "Pee Hours: " + String.valueOf(dogProfile.getPeeHours()));
            Log.d("Print of Dog Data", "Poo Hours: " + String.valueOf(dogProfile.getPooHours()));
            Log.d("Print of Dog Data", "Calories Per Meal: " + String.valueOf(dogProfile.getCalPerMeal()));
            Log.d("Print of Dog Data", "Individual Walk Duration: " + String.valueOf(dogProfile.getWalkDuration()));
            Log.d("Print of Dog Data", "Amount of Walks Needed: " + String.valueOf(dogProfile.getWalkCount()));
            Log.d("Print of Dog Data", "Last Peed: " + dogProfile.getLastPeed().toString());
            Log.d("Print of Dog Data", "Last Pooed: " + dogProfile.getLastPooed().toString());

            sendDogData();
            Intent i = new Intent(loginActivity.this, dogHub.class);
            i.putExtra("dogProfile", dogProfile);
            startActivity(i);
        }
        else {
            alertDialog.setMessage("Please Fill Out The Following:\n\n" + errorMessage);
            alertDialog.show();
        }

    }

    /***************************************************************
     *  Write to the DB
     ***************************************************************/
    private void writeDogProfile(String uid, String dogName, int dogAge, int dogWeight, int dogEnergy, int calorieCount, int peeHours,
                                 int pooHours, int calPerMeal, int walkDuration, int walkCount, Date lastPeed, Date lastPooed, boolean walk0,
                                 boolean walk1, boolean meal0, boolean meal1) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneousl

        String key = mDatabase.child("dProfile").push().getKey();
//        int pCount = mDatabase.child("dProfile/" + uid + "/petCount");
        dogProfile dProfile = new dogProfile(uid, dogName, dogAge, dogWeight, dogEnergy, calorieCount, peeHours,
        pooHours, calPerMeal, walkDuration, walkCount, lastPeed, lastPooed, walk0,
        walk1, meal0, meal1); //this function needs values passed to it.
        Map<String, Object> postValues = dProfile.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/dProfile/" + key, postValues);
        childUpdates.put("/oProfile/" + uid + "/pet/", key);
        childUpdates.put("/oProfile/" + uid + "/hasPetProfile/", true);

        mDatabase.updateChildren(childUpdates);
    }


    /***************************************************************
     *  Starting and stoping the Listeners
     ***************************************************************/
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
