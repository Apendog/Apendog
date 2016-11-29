package alpha.com.apendog;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;
/******************************************
 *    ====DOG PROFILE======
 *    a simple class that holds the dog
 *    info from the sign up page in private
 *    variables. Has getters/setters
 */
@IgnoreExtraProperties
public class dogProfile {
    private String uid = null;
    private String dogName = null;
    private int dogAge = 0; //in months!
    private int dogWeight = 0;
    private int dogEnergy = 0;
    private int calorieCount = 0; //kcal/CUP


    public dogProfile() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public dogProfile(String uid, String dogName, int dogAge, int dogWeight, int dogEnergy, int calorieCount) {
        this.uid = uid;
        this.dogName = dogName;
        this.dogAge = dogAge;
        this.dogWeight = dogWeight;
        this.dogEnergy = dogEnergy;
        this.calorieCount = calorieCount;
    }

    @Exclude
    public  Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("dogName", dogName);
        result.put("dogAge", dogAge);
        result.put("dogWeight", dogWeight);
        result.put("dogEnergy", dogEnergy);
        result.put("calorieCount", calorieCount);

        return result;
    }

    // --GETTERS--
    public String getDogName() {
        return dogName;
    }

    public int getDogAge() {
        return dogAge;
    }

    public int getDogWeight() {
        return dogWeight;
    }

    public int getDogEnergy() {
        return dogEnergy;
    }

    public int getCalorieCount(){
        return calorieCount;
    }
    // --SETTERS--
    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public void setDogAge(int dogAge) {
        this.dogAge = dogAge;
    }

    public void setDogWeight(int dogWeight) {
        this.dogWeight = dogWeight;
    }

    public void setDogEnergy(int dogEnergy) {
        this.dogEnergy = dogEnergy;
    }

    public void setCalorieCount(int calorieCount) {
        this.calorieCount = calorieCount;
    }
}
