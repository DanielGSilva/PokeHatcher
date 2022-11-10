package iocari.pokehatcher;

import static java.util.Objects.requireNonNull;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class DexVM extends ViewModel {
    private MutableLiveData<ArrayList<DexItem>> dexItems = new MutableLiveData<>();
    private ArrayList<ArrayList<Integer>> eggTypes = new ArrayList<>();

    private MutableLiveData<ArrayList<Boolean>> obtainedFilters = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Boolean>> eggTypeFilters = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Boolean>> evolutionStageFilters = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Boolean>> qOLFilters = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Boolean>> sortOptions = new MutableLiveData<>();

    public MutableLiveData<ArrayList<DexItem>> getDexItems() {
        return dexItems;
    }

    public ArrayList<DexItem> getDexItemsValue() {
        return dexItems.getValue();
    }

    public MutableLiveData<ArrayList<Boolean>> getObtainedFilters() {
        return this.obtainedFilters;
    }

    public MutableLiveData<ArrayList<Boolean>> getEggTypeFilters() {
        return this.eggTypeFilters;
    }

    public MutableLiveData<ArrayList<Boolean>> getEvolutionStageFilters() {
        return this.evolutionStageFilters;
    }

    public MutableLiveData<ArrayList<Boolean>> getQOLFilters() {
        return qOLFilters;
    }

    public MutableLiveData<ArrayList<Boolean>> getSortOptions() {
        return sortOptions;
    }

    public void setDexItems(ArrayList<DexItem> dexItems) {
        this.dexItems.setValue(dexItems);
    }

    public void setEggTypes(ArrayList<ArrayList<Integer>> eggTypes) {
        this.eggTypes = eggTypes;
    }

    public void setObtainedFilters(ArrayList<Boolean> obtainedFilters) {
        this.obtainedFilters.setValue(obtainedFilters);
    }

    public void setEggTypeFilters(ArrayList<Boolean> eggTypeFilters) {
        this.eggTypeFilters.setValue(eggTypeFilters);
    }

    public void setEvolutionStageFilters(ArrayList<Boolean> evolutionStageFilters) {
        this.evolutionStageFilters.setValue(evolutionStageFilters);
    }

    public void setQOLFilters(ArrayList<Boolean> qOLFilters) {
        this.qOLFilters.setValue(qOLFilters);
    }

    public void setSortOptions(ArrayList<Boolean> sortOptions) {
        this.sortOptions.setValue(sortOptions);
    }

    public DexItem getDexItem (Integer dexNumber) {
        return requireNonNull(this.dexItems.getValue()).get(dexNumber-1);
    }

    public void setDexItem(DexItem dexItem) {
        ArrayList<DexItem> newDex = Objects.requireNonNull(this.dexItems.getValue());
        newDex.set(dexItem.getNumber()-1, dexItem);
        this.setDexItems(newDex);
    }

    public ArrayList<Integer> getPokemonFromEggType(Integer eggType) {
        return this.eggTypes.get(eggType);
    }

    public ArrayList<Boolean> getObtainedFiltersValue() {
        return this.obtainedFilters.getValue();
    }

    public ArrayList<Boolean> getEggTypeFiltersValue() {
        return this.eggTypeFilters.getValue();
    }

    public ArrayList<Boolean> getEvolutionStageFiltersValue() {
        return this.evolutionStageFilters.getValue();
    }

    public ArrayList<Boolean> getQOLFiltersValue() {
        return this.qOLFilters.getValue();
    }

    public ArrayList<Boolean> getSortOptionsValue() {
        return this.sortOptions.getValue();
    }
}
