package alpha.com.apendog;

/******************************************
 *    ====DOG PROFILE======
 *    a simple class that holds the dog
 *    info from the sign up page in private
 *    variables. Has getters/setters
 */

public class dogProfile {
    private String dogName = null;
    private int dogAge = 0; //in months!
    private int dogWeight = 0;
    private int dogEnergy = 0;

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
}
