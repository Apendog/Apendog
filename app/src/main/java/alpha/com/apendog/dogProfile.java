package alpha.com.apendog;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/******************************************
 *    ====DOG PROFILE======
 *    a simple class that holds the dog
 *    info from the sign up page in private
 *    variables. Has getters/setters
 */
@IgnoreExtraProperties
@SuppressWarnings("serial")
public class dogProfile implements Serializable{
    public String uid = null;
    public String dogName = null;
    private int dogAge = 0;       //in months!
    private int dogWeight = 0;    //in pounds
    private int dogEnergy = 0;    //how energetic dog is
    private int calorieCount = 0; //kcal/CUP
    private int peeHours = 0;     //how long the dog can hold pee
    private int pooHours = 0;     //how long the dog can hold poo
    private int calPerMeal = 0;   //amount of cal per meal
    private int walkDuration = 0; //how long the individual walks must be
    private int walkCount = 0;    //how many walks per day
    private Date lastPeed;        //last time the dog peed
    private Date lastPooed;       //last time the dog took a dookie
    private boolean walks[];      //walks;
    private boolean meals[];     //the dog's two meals




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

    public int getPeeHours() {return peeHours;}

    public int getPooHours() {return pooHours;}

    public int getCalPerMeal() {return calPerMeal;}

    public int getWalkDuration() {return walkDuration;}

    public int getWalkCount() {return walkCount;}

    public Date getLastPeed() {return lastPeed;}

    public Date getLastPooed() {return lastPooed;}

    public boolean[] getWalks() {return walks;}

    public boolean[] getMeals() {return meals;}

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

    public void setPeeHours(int peeHours) {this.peeHours = peeHours;}

    public void setPooHours(int pooHours) {this.pooHours = pooHours;}

    public void setCalPerMeal(int calPerMeal) {this.calPerMeal = calPerMeal;}

    public void setWalkDuration(int walkDuration) {this.walkDuration = walkDuration;}

    public void setWalkCount(int walkCount) {this.walkCount = walkCount;}

    public void setLastPeed(Date lastPeed) {this.lastPeed = lastPeed;}

    public void setLastPooed(Date lastPooed) {this.lastPooed = lastPooed;}

    public void setWalks(boolean walks[]) {this.walks = walks;}

    public void setMeals(boolean meals[]) {this.meals = meals;}

}
