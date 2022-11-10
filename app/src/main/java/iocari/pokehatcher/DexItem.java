package iocari.pokehatcher;

import java.util.ArrayList;

public class DexItem {
    private String name;
    private int number;
    private String type1;
    private String type2;
    private String classification;
    private String height;
    private String weight;
    private int eggType;
    private int stage;
    private long price;
    private ArrayList<ArrayList<Integer>> evolutions = new ArrayList<>();

    private boolean caught;
    private int count;
    private int imgId;

    public DexItem(String name, int number, String type1, String type2, String classification, String height, String weight, int imgId, int eggType, int stage, long price, ArrayList<ArrayList<Integer>> evolutions) {
        this.name = name;
        this.number = number;
        this.type1 = type1;
        this.type2 = type2;
        this.classification = classification;
        this.height = height;
        this.weight = weight;
        this.imgId = imgId;
        this.eggType = eggType;
        this.stage = stage;
        this.price = price;
        this.evolutions = evolutions;
        this.caught = false;
        this.count = 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setCaught(boolean caught) {
        this.caught = caught;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setEvolutions(ArrayList<ArrayList<Integer>> evolutions) {
        this.evolutions = evolutions;
    }

    public void hatch() {
        this.caught = true;
        this.caughtAnother();
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public int getImgId() {
        return imgId;
    }

    public String getType1() {
        return type1;
    }

    public String getType2() {
        return type2;
    }

    public String getClassification() {
        return classification;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public int getEggType() {
        return eggType;
    }

    public int getStage() {
        return stage;
    }

    public long getPrice() {
        return price;
    }

    public ArrayList<ArrayList<Integer>> getEvolutions() {
        return evolutions;
    }

    public boolean hasCaught() {
        return caught;
    }

    public void caughtAnother() {
        this.count++;
    }

    public void evolve(Integer integer) {
        this.count -= integer;
    }

    public void sell(int i) {
        this.count -= i;
    }

    public boolean isBasic() {
        return this.stage == 0;
    }

    public boolean isFinal() {
        return this.evolutions.isEmpty();
    }

    public boolean canEvolve() {
        if (this.isFinal() || !this.caught) return false;

        for (int i = 0; i < this.evolutions.size(); i++) {
            if (this.count >= this.evolutions.get(i).get(1)) return true;
        }

        return false;
    }
}
