package alpha.com.apendog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import alpha.com.apendog.BaseActivity;

public class dogHub extends AppCompatActivity {

    public TextView peeTicker;
    public TextView pooTicker;
    public Button peeDoneButton;
    public Button pooDoneButton;
    public CheckBox walkCheck0;
    public CheckBox walkCheck1;
    public Button  walkedDoneButton;
    public CheckBox mealCheck0;
    public CheckBox mealCheck1;
    public Button mealDoneButton;
    public TextView dogName;
    public String dogKey;
    public String mpetUid;
              //delete this ^^^^^^^^^^^^^^^^^^^^^^ After the petUid is passed with intent



// need to check the spellings on this

    DatabaseReference mDatabase;
    private ValueEventListener mActivityListener;
    public dogProfile dProfile1 = null;

    private FirebaseAuth mAuth;

    private static final String TAG = "Ed-Log";
    private static String uid;

    private FirebaseAuth.AuthStateListener mAuthListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_hub);

        Intent intent = getIntent();
        mpetUid = intent.getStringExtra(AddPet.EXTRA_MESSAGE);
        if (mpetUid == null)
        {
            mpetUid = intent.getStringExtra(loginActivity.EXTRA_MESSAGE);
        }
        Log.d(TAG, "mPetUid: " + mpetUid);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        // put name from profile on hub
        dogName = (TextView) findViewById(R.id.dogNameView);
        peeTicker = (TextView) findViewById(R.id.peeTicker);
        pooTicker = (TextView) findViewById(R.id.pooTicker);
        peeDoneButton = (Button) findViewById(R.id.peeDoneButton);
        pooDoneButton = (Button) findViewById(R.id.pooDoneButton);
        walkCheck0 = (CheckBox) findViewById(R.id.walkCheck0);
        walkCheck1 = (CheckBox) findViewById(R.id.walkCheck1);
        walkedDoneButton = (Button) findViewById(R.id.walkedDoneButton);
        mealCheck0 = (CheckBox) findViewById(R.id.mealCheck0);
        mealCheck1 = (CheckBox) findViewById(R.id.mealCheck1);
        mealDoneButton = (Button) findViewById(R.id.mealDoneButton);



        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    uid = user.getUid();
                    getUserProfile();
                    getDogProfile();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Intent intent = new Intent(dogHub.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]

                // [END_EXCLUDE]
            }
        };



    }



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        // [START basicActivity_listener]
        ValueEventListener dogProfileListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                dProfile1 = dataSnapshot.getValue(dogProfile.class);

                // [START_EXCLUDE]
                dogName.setText("Let " + dProfile1.dogName + " go...");
                Log.d(TAG, "dProfile1: " + dProfile1);
                peeDoneButton.setText(dProfile1.dogName + " peed!");
                pooDoneButton.setText(dProfile1.dogName + " pooed!");
                walkCheck0.setText("Walked for " + String.valueOf(dProfile1.walk0) + " minutes");
                walkCheck1.setText("Walked for " + String.valueOf(dProfile1.walk1) + " minutes");
                walkedDoneButton.setText("I walked " + dProfile1.dogName + "!");
                mealCheck0.setText("Fed " + String.valueOf(dProfile1.meal0) + " k/cal of food");
                mealCheck1.setText("Fed " + String.valueOf(dProfile1.meal1) + " k/cal of food");
                mealDoneButton.setText("I fed " + dProfile1.dogName + "!");

                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(dogHub.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        /*mActivityReference.addValueEventListener(dogProfileListener);*/

        // [END post_value_event_listener]

        mActivityListener = dogProfileListener;

        //myDogProfile = (dogProfile)getIntent().getSerializableExtra("dogProfile");

    }
    // [END on_start_add_listener]


    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
//        hideProgressDialog();
    }
    // [END on_stop_remove_listener]


    /***************************************************************
     *  Write to the DB. Call this to write to Firebase
     ***************************************************************/
    private void writeBaseActivity(String petUid, String userUid, String time, int duration, String activityType) {
        BaseActivity mActivity = new BaseActivity(petUid, userUid, time, duration, activityType); //this function needs values passed to it.
        Map<String, Object> postValues = mActivity.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Activities/", postValues);
        mDatabase.updateChildren(childUpdates);
    }

    /*******
     * This is to get the dog profile one time
     */
    private void getDogProfile() {

        FirebaseDatabase.getInstance().getReference().child("dProfile").child(mpetUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information

                        dProfile1 = dataSnapshot.getValue(dogProfile.class);
                        dogName.setText("Let " + dProfile1.dogName + " go...");
                        Log.d(TAG, "dProfile1: " + dProfile1);
                        peeDoneButton.setText(dProfile1.dogName + " peed!");
                        pooDoneButton.setText(dProfile1.dogName + " pooed!");
                        walkCheck0.setText("Walked for " + String.valueOf(dProfile1.walk0) + " minutes");
                        walkCheck1.setText("Walked for " + String.valueOf(dProfile1.walk1) + " minutes");
                        walkedDoneButton.setText("I walked " + dProfile1.dogName + "!");
                        mealCheck0.setText("Fed " + String.valueOf(dProfile1.meal0) + " k/cal of food");
                        mealCheck1.setText("Fed " + String.valueOf(dProfile1.meal1) + " k/cal of food");
                        mealDoneButton.setText("I fed " + dProfile1.dogName + "!");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void getUserProfile() {

        FirebaseDatabase.getInstance().getReference().child("oProfile").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        UserProfile user = dataSnapshot.getValue(UserProfile.class);
                        String userName = user.username;
                        String email = user.email;
                        boolean hasPetProfile = user.hasPetProfile;
                        //dogKey = user.petUid;

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    public void sharePetProfile (View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mpetUid);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void signOut() {
        mAuth.signOut();
    }
    public void signoutButtonClick(View view) {
     signOut();
    }
}
