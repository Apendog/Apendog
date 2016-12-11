package alpha.com.apendog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class AddPet extends AppCompatActivity {

    public String label;
    public String text;
    public String petUid;
    public String userUid;
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        userUid = intent.getStringExtra(AddPet.EXTRA_MESSAGE);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        //Auth Listener Start
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    userUid = user.getUid();

                } else {
                    // User is signed out
                    Intent intent = new Intent(AddPet.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };

    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    /***************************************************************
     *  Starting and stopping the Auth Listeners
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

    /***************************************************************
     *  go to Add a Pet page
     ***************************************************************/
    public void cancel(View view){
        Intent intent = new Intent(AddPet.this, loginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Log.d("Ed-Log", "cancel AddPet--- Ran");
    }


    /***************************************************************
     * Add Pet and go to doghub
     ***************************************************************/
    public void addPet(View view){
        EditText editText = (EditText) findViewById(R.id.sharedDogUid);
        petUid = editText.getText().toString();
        userUid = getUid();
        Log.d("Ed-Log", "AddPet--- Ran" + petUid + "UserUid: " + userUid);
        Intent intent = new Intent(AddPet.this, dogHub.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(EXTRA_MESSAGE, petUid);
        startActivity(intent);
        mDatabase.child("oProfile").child(userUid).child("pet").setValue(petUid);
//        mDatabase.child("oProfile").child(userUid).child("hasPetProfile").setValue(true);

    }

    /***************************************************************
     * Write new pet to Db.
     ***************************************************************/
    private void writeSharedPet() {

    }

    public void clipBoard(){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }

}
