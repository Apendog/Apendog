package alpha.com.apendog;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 12/5/2016.
 */

public class BaseActivity {

    public String petUid = null;
    public String userUid = null;
    public String time = null;
    public int duration = 0;
    public String activityType = null;


    public BaseActivity() {
        // Default constructor required for calls to DataSnapshot.getValue(baseActivity.class)
    }

    public BaseActivity(String petUid, String userUid, String time, int duration, String activityType) {
        this.petUid = petUid;
        this.userUid = userUid;
        this.time = time;
        this.duration = duration;
        this.activityType = activityType;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("pUid", petUid);
        result.put("userUid", userUid);
        result.put("time", time);
        result.put("duration", duration);
        result.put("activityType", activityType);

        return result;
    }

}