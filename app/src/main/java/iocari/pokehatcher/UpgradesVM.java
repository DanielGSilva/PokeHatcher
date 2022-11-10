package iocari.pokehatcher;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class UpgradesVM extends ViewModel {
    private MutableLiveData<ArrayList<Upgrade>> stepMultiplierUpgrades = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Upgrade>> critChanceUpgrades = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Upgrade>> extraEggChanceUpgrades = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Upgrade>> getStepMultiplierUpgrades() {
        return stepMultiplierUpgrades;
    }

    public void setStepMultiplierUpgrades(ArrayList<Upgrade> eggMultiplierUpgrades) {
        this.stepMultiplierUpgrades.setValue(eggMultiplierUpgrades);
    }

    public MutableLiveData<ArrayList<Upgrade>> getCritChanceUpgrades() {
        return critChanceUpgrades;
    }

    public void setCritChanceUpgrades(ArrayList<Upgrade> critChanceUpgrades) {
        this.critChanceUpgrades.setValue(critChanceUpgrades);
    }

    public MutableLiveData<ArrayList<Upgrade>> getExtraEggChanceUpgrades() {
        return extraEggChanceUpgrades;
    }

    public void setExtraEggChanceUpgrades(ArrayList<Upgrade> extraEggChanceUpgrades) {
        this.extraEggChanceUpgrades.setValue(extraEggChanceUpgrades);
    }

    public Upgrade getStepMultiplierUpgrade(Integer i) {
        return Objects.requireNonNull(this.stepMultiplierUpgrades.getValue()).get(i);
    }

    public Upgrade getCritChanceUpgrade(Integer i) {
        return Objects.requireNonNull(this.critChanceUpgrades.getValue()).get(i);
    }

    public Upgrade getExtraEggChanceUpgrade(Integer i) {
        return Objects.requireNonNull(this.extraEggChanceUpgrades.getValue()).get(i);
    }
}
