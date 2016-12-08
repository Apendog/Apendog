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
    private TextView mAuthorView;
    private TextView mUserUid;
    private TextView mBodyView;

    private String mActivityKey;
    public dogProfile myDogProfile;
// need to check the spellings on this
    private DatabaseReference mActivityReference;
    private ValueEventListener mActivityListener;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static final String TAG = "Ed-Log";
    private static String uid;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_hub);
        mActivityKey = "testActivity";
        // Initialize Database
        mActivityReference = FirebaseDatabase.getInstance().getReference()
                .child("Activities").child(mActivityKey);;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // put name from profile on hub
        dogName = (TextView) findViewById(R.id.dogNameView);
//        mAuthorView = (TextView) findViewById(R.id.petUidView);
//        mUserUid = (TextView) findViewById(R.id.userUidView);
//        mBodyView = (TextView) findViewById(R.id.durationView);
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

        getDogProfile();

        myDogProfile = (dogProfile)getIntent().getSerializableExtra("dogProfile");
        dogName.setText("Let " + myDogProfile.getDogName() + " go...");
        peeDoneButton.setText(myDogProfile.getDogName() + " peed!");
        pooDoneButton.setText(myDogProfile.getDogName() + " pooed!");
        walkCheck0.setText("Walked for " + String.valueOf(myDogProfile.getWalkDuration()) + " minutes");
        walkCheck1.setText("Walked for " + String.valueOf(myDogProfile.getWalkDuration()) + " minutes");
        walkedDoneButton.setText("I walked " + myDogProfile.getDogName() + "!");
        mealCheck0.setText("Fed " + String.valueOf(myDogProfile.getCalPerMeal()) + " k/cal of food");
        mealCheck1.setText("Fed " + String.valueOf(myDogProfile.getCalPerMeal()) + " k/cal of food");
        mealDoneButton.setText("I fed " + myDogProfile.getDogName() + "!");


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    uid = user.getUid();
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
        ValueEventListener baseActivityListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                BaseActivity baseActivity = dataSnapshot.getValue(BaseActivity.class);
                dogProfile dProfile = dataSnapshot.getValue(dogProfile.class);
                // [START_EXCLUDE]
         //       dogName.setText(dProfile.dogName);
//                mAuthorView.setText(baseActivity.petUid);
//                mUserUid.setText(baseActivity.userUid);
//                mBodyView.setText(baseActivity.duration);
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
        mActivityReference.addValueEventListener(baseActivityListener);
        // [END post_value_event_listener]

        mActivityListener = baseActivityListener;
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




        FirebaseDatabase.getInstance().getReference().child("dProfile").child("-KY5exo6b85U8aTlMx6r")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        dogProfile dProfile = dataSnapshot.getValue(dogProfile.class);

                        String uid = dProfile.uid;
                        String dogName = dProfile.dogName;
                        int dogAge = dProfile.getDogAge();
                        int dogWeight = dProfile.getDogWeight();
                        int dogEnergy = dProfile.getDogEnergy();
                        int calorieCount = dProfile.getCalorieCount();

                       dogProfile dProfile1 = new dogProfile(uid, dogName, dogAge, dogWeight, dogEnergy, calorieCount);
                        Log.d(TAG, "getDogProfile " + uid + " dogName: " + dogName + " dogWeight: " + dogWeight + " dogEnergy: " + dogEnergy + " cc: " + calorieCount);
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
                        String petUid = user.petUid;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }




    public void signOut() {
        mAuth.signOut();
    }
    public void signoutButtonClick(View view) {
     signOut();
    }
}
