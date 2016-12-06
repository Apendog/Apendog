package alpha.com.apendog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class dogHub extends AppCompatActivity {


    public TextView dogName;
    public dogProfile myDogProfile;
// need to check the spellings on this
    private DatabaseReference mActivityReference;
    private ValueEventListener mActivityListener;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static final String TAG = "Ed-Log";

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_hub);

        // Initialize Database
        mActivityReference = FirebaseDatabase.getInstance().getReference()
                .child("Activities").child(mPostKey);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // put name from profile on hub
        dogName = (TextView) findViewById(R.id.dogNameView);
        myDogProfile = (dogProfile)getIntent().getSerializableExtra("dogProfile");
        dogName.setText(myDogProfile.getDogName());

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
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
                // [START_EXCLUDE]
                mAuthorView.setText(post.author);
                mTitleView.setText(post.title);
                mBodyView.setText(post.body);
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
        mActivityReference.addValueEventListener(activityListener);
        // [END post_value_event_listener]

        mActivityListener = activityListener;
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



    public void signOut() {
        mAuth.signOut();
    }
    public void signoutButtonClick(View view) {
     signOut();
    }
}
