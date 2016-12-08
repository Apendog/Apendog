package alpha.com.apendog;

/**
 * Created by user on 11/28/2016.
 */

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START blog_user_class]
@IgnoreExtraProperties
public class UserProfile {

    public String username = null;
    public String email = null;
    public boolean hasPetProfile = false;
    public String petUid = null;


    public UserProfile() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserProfile(String username, String email, boolean hasPetProfile, String petUid) {
        this.username = username;
        this.email = email;
        this.hasPetProfile = hasPetProfile;
        this.petUid = petUid;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("email", email);
        result.put("hasPetProfile", hasPetProfile);
        result.put("petUid", petUid);

        return result;
    }

}
