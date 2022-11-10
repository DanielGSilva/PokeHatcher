package iocari.pokehatcher;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class PlayerVM extends ViewModel {
    private MutableLiveData<Long> level = new MutableLiveData<>();
    private MutableLiveData<Long> xp = new MutableLiveData<>();
    private MutableLiveData<Long> xpNeeded = new MutableLiveData<>();
    private MutableLiveData<Long> money = new MutableLiveData<>();

    private MutableLiveData<ArrayList<Integer>> currentUpgrades = new MutableLiveData<>();
    private MutableLiveData<Double> clickMultiplier = new MutableLiveData<>();
    private MutableLiveData<Double> critChance = new MutableLiveData<>();
    private MutableLiveData<Double> extraEggChance = new MutableLiveData<>();

    private MutableLiveData<Integer> uncommonEggs = new MutableLiveData<>();
    private MutableLiveData<Integer> rareEggs = new MutableLiveData<>();
    private MutableLiveData<Integer> legendaryEggs = new MutableLiveData<>();

    private MutableLiveData<Integer> commonEggProgress = new MutableLiveData<>();
    private MutableLiveData<Integer> uncommonEggProgress = new MutableLiveData<>();
    private MutableLiveData<Integer> rareEggProgress = new MutableLiveData<>();
    private MutableLiveData<Integer> legendaryEggProgress = new MutableLiveData<>();

    private ArrayList<Long> eggPrices;
    private ArrayList<Long> eggMoney;
    private ArrayList<Long> eggXp;

    public PlayerVM() {
        this.eggPrices = new ArrayList<>(Arrays.asList(0L, 100L, 250L, 600L));
        this.eggMoney = new ArrayList<>(Arrays.asList(20L, 50L, 125L, 300L));
        this.eggXp = new ArrayList<>(Arrays.asList(10L, 30L, 100L, 500L));
    }

    public MutableLiveData<Integer> getUncommonEggs() {
        return uncommonEggs;
    }
    public MutableLiveData<Integer> getRareEggs() {
        return rareEggs;
    }
    public MutableLiveData<Integer> getLegendaryEggs() {
        return legendaryEggs;
    }

    public MutableLiveData<Long> getLevel() {
        return this.level;
    }

    public Long getLevelValue() {
        return this.level.getValue();
    }

    public void setLevelValue(Long levelValue) {
        this.level.setValue(levelValue);
        this.calculateXpNeeded();
    }

    private void levelUp() {
        this.setXpValue(this.getXpValue() - this.getXpNeededValue());
        this.setLevelValue(this.level.getValue()+1);
    }

    public MutableLiveData<Long> getXp() {
        return this.xp;
    }

    public Long getXpValue() {
        return this.xp.getValue();
    }

    public void setXpValue (Long xpValue) {
        this.xp.setValue(xpValue);
    }

    public void gainXp (Long xpGained) {
        this.xp.setValue(this.getXpValue() + xpGained);
        while (this.getXpValue() >= this.getXpNeededValue()) {
            this.levelUp();
        }
    }

    public void gainEggXP (Integer eggType) {
        this.gainXp(this.eggXp.get(eggType));
    }

    public MutableLiveData<Long> getXpNeeded () {
        return this.xpNeeded;
    }

    public Long getXpNeededValue() {
        return this.xpNeeded.getValue();
    }

    private void calculateXpNeeded() {
        this.xpNeeded.setValue((long) (Math.pow(this.getLevelValue(), 3) - Math.pow(this.getLevelValue()-1, 3)));
    }

    public MutableLiveData<Long> getMoney() {
        return this.money;
    }

    public Long getMoneyValue() {
        return this.money.getValue();
    }

    public void setMoneyValue(Long moneyValue) {
        this.money.setValue(moneyValue);
    }

    public void gainMoney(Long moneyValue) {
        this.money.setValue(this.getMoneyValue() + moneyValue);
    }

    public void gainEggMoney(Integer eggType) {
        this.gainMoney(this.getEggMoney(eggType));
    }

    public void spendMoney(Long moneyValue) {
        this.money.setValue(this.getMoneyValue() - moneyValue);
    }

    public String getMoneyText() {
        long x = this.getMoneyValue();
        return this.getAbbreviatedQtyText(x);
    }

    public String getXPText() {
        long xp = this.getXpValue();
        long xpNeeded = this.getXpNeededValue();
        return this.getAbbreviatedQtyText(xp) + "/" + this.getAbbreviatedQtyText(xpNeeded);
    }

    public String getEggQtyText(int eggType) {
        long qty = (long) this.getEggQty(eggType);
        return this.getAbbreviatedQtyText(qty);
    }

    private String getAbbreviatedQtyText(long x) {
        int y = 0;
        long order = 0;
        String termination;
        String decimal;

        while(x/1000 >= 1) {
            order++;
            y = (int) ((x % 1000)/10);
            x /= 1000;
        }

        switch ((int) order) {
            case 0:
                termination = "";
                decimal = "";
                break;
            case 1:
                termination = "k";
                decimal = "." + String.format("%02d", y);
                break;
            case 2:
                termination = "M";
                decimal = "." + String.format("%02d", y);
                break;
            case 3:
                termination = "B";
                decimal = "." + String.format("%02d", y);
                break;
            case 4:
                termination = "T";
                decimal = "." + String.format("%02d", y);
                break;
            default:
                order -= 5;
                long a = order / 26;
                long b = order % 26;
                termination = String.valueOf((char) (a + 97)) + String.valueOf((char) (b+97));
                decimal = "." + String.format("%02d", y);
                break;
        }

        return String.valueOf(x) + decimal + termination;
    }

    public MutableLiveData<ArrayList<Integer>> getCurrentUpgrades() {
        return this.currentUpgrades;
    }

    public Integer getCurrentStepMultiplierUpgrade() {
        return Objects.requireNonNull(this.currentUpgrades.getValue()).get(0);
    }

    public Integer getCurrentCritChanceUpgrade() {
        return Objects.requireNonNull(this.currentUpgrades.getValue()).get(1);
    }

    public Integer getCurrentExtraEggChanceUpgrade() {
        return Objects.requireNonNull(this.currentUpgrades.getValue()).get(2);
    }

    public void setCurrentUpgrades(ArrayList<Integer> currentUpgrades) {
        this.currentUpgrades.setValue(currentUpgrades);
    }

    public void upgradeStepMultiplier(Upgrade upgrade) {
        this.setClickMultiplier(upgrade.getValue());
        ArrayList<Integer> newUpgrades = Objects.requireNonNull(this.currentUpgrades.getValue());
        newUpgrades.set(0, upgrade.getOrder());
        this.setCurrentUpgrades(newUpgrades);
    }

    public void upgradeCritChance(Upgrade upgrade) {
        this.setCritChance(upgrade.getValue());
        ArrayList<Integer> newUpgrades = Objects.requireNonNull(this.currentUpgrades.getValue());
        newUpgrades.set(1, upgrade.getOrder());
        this.setCurrentUpgrades(newUpgrades);
    }

    public void upgradeExtraEggChance(Upgrade upgrade) {
        this.setExtraEggChance(upgrade.getValue());
        ArrayList<Integer> newUpgrades = Objects.requireNonNull(this.currentUpgrades.getValue());
        newUpgrades.set(2, upgrade.getOrder());
        this.setCurrentUpgrades(newUpgrades);
    }

    public MutableLiveData<Double> getClickMultiplier() {
        return this.clickMultiplier;
    }

    public Double getClickMultiplierValue() {
        return this.clickMultiplier.getValue();
    }

    public void setClickMultiplier(Double clickMultiplierValue) {
        this.clickMultiplier.setValue(clickMultiplierValue);
    }

    public MutableLiveData<Double> getCritChance() {
        return this.critChance;
    }

    public Double getCritChanceValue() {
        return this.critChance.getValue();
    }

    public void setCritChance(Double critChanceValue) {
        this.critChance.setValue(critChanceValue);
    }

    public MutableLiveData<Double> getExtraEggChance() {
        return this.extraEggChance;
    }

    public Double getExtraEggChanceValue() {
        return this.extraEggChance.getValue();
    }

    public void setExtraEggChance(Double extraEggChanceValue) {
        this.extraEggChance.setValue(extraEggChanceValue);
    }

    public Integer getEggProgress(int eggType) {
        switch (eggType) {
            case 0:
                return this.commonEggProgress.getValue();
            case 1:
                return this.uncommonEggProgress.getValue();
            case 2:
                return this.rareEggProgress.getValue();
            case 3:
                return this.legendaryEggProgress.getValue();
            default:
                return 0;
        }
    }

    public void setEggProgress(int eggType, Integer progress) {
        switch (eggType) {
            case 0:
                this.commonEggProgress.setValue(progress);
                return;
            case 1:
                this.uncommonEggProgress.setValue(progress);
                return;
            case 2:
                this.rareEggProgress.setValue(progress);
                return;
            case 3:
                this.legendaryEggProgress.setValue(progress);
                return;
        }
    }

    public Integer getEggQty (int eggType) {
        switch (eggType) {
            case 1:
                return this.uncommonEggs.getValue();
            case 2:
                return this.rareEggs.getValue();
            case 3:
                return this.legendaryEggs.getValue();
            default:
                return 0;
        }
    }

    public Long getEggMoney (Integer eggType) {
        return this.eggMoney.get(eggType);
    }

    public long getEggXP (Integer eggType) {
        return this.eggXp.get(eggType);
    }

    public void setEggQty(int eggType, Integer qty) {
        switch (eggType) {
            case 1:
                this.uncommonEggs.setValue(qty);
                return;
            case 2:
                this.rareEggs.setValue(qty);
                return;
            case 3:
                this.legendaryEggs.setValue(qty);
                return;
        }
    }

    public void setEggQtys(Integer uncommonEggQty, Integer rareEggQty, Integer legendaryEggQty) {
        this.uncommonEggs.setValue(uncommonEggQty);
        this.rareEggs.setValue(rareEggQty);
        this.legendaryEggs.setValue(legendaryEggQty);
    }

    public void setEggProgresses(Integer commonEggProgress, Integer uncommonEggProgress, Integer rareEggProgress, Integer legendaryEggProgress) {
        this.commonEggProgress.setValue(commonEggProgress);
        this.uncommonEggProgress.setValue(uncommonEggProgress);
        this.rareEggProgress.setValue(rareEggProgress);
        this.legendaryEggProgress.setValue(legendaryEggProgress);
    }

    public void hatchEgg(int eggType) {
        if (eggType != 0) {
            this.setEggQty(eggType, this.getEggQty(eggType) - 1);
        }
        this.setEggProgress(eggType, 0);

        Random random = new Random();
        if (random.nextInt(100*100) < this.getExtraEggChanceValue()*100) {
            this.setEggQty(eggType+1, this.getEggQty(eggType+1) +1);
        }
    }

    public void buyEggs(int eggType, int i) {
        this.spendMoney(i * this.eggPrices.get(eggType));
        this.setEggQty(eggType, this.getEggQty(eggType) + i);
    }
}
