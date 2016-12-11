package alpha.com.apendog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class AddPet extends AppCompatActivity {

    public String label;
    public String text;
    public String petUid;
    public String userUid;
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        Intent intent = getIntent();
        userUid = intent.getStringExtra(AddPet.EXTRA_MESSAGE);
        mDatabase = FirebaseDatabase.getInstance().getReference();

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
        writeSharedPet();
        Intent intent = new Intent(AddPet.this, dogHub.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        EditText editText = (EditText) findViewById(R.id.sharedDogUid);
        petUid = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, petUid);
        startActivity(intent);
        Log.d("Ed-Log", "AddPet--- Ran" + petUid);
    }

    /***************************************************************
     * Write new pet to Db.
     ***************************************************************/
    private void writeSharedPet() {
        mDatabase.child("oProfile").child(userUid).child("pet").setValue(petUid);
        mDatabase.child("oProfile").child(userUid).child("hasPetProfile").setValue(true);
    }

    public void clipBoard(){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }

}
