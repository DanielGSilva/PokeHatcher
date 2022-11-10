package iocari.pokehatcher;

import androidx.arch.core.internal.SafeIterableMap;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EggVM extends ViewModel {
    private MutableLiveData<Integer> eggType = new MutableLiveData<>();
    private MutableLiveData<Integer> eggSteps = new MutableLiveData<>();
    private MutableLiveData<Integer> imgSrc = new MutableLiveData<>();
    private MutableLiveData<String> name = new MutableLiveData<>();

    private MutableLiveData<String> eggHatchName = new MutableLiveData<>();
    private MutableLiveData<Integer> eggHatchImgSrc = new MutableLiveData<>();
    private MutableLiveData<Long> eggHatchMoneyTxt = new MutableLiveData<>();
    private MutableLiveData<Long> eggHatchXPTxt = new MutableLiveData<>();

    public MutableLiveData<Integer> getEggType() {
        return this.eggType;
    }

    public Integer getEggTypeValue() {
        return this.eggType.getValue();
    }

    public Integer getEggStepsValue() {
        return this.eggSteps.getValue();
    }

    public Integer getImgSrcValue() {
        return this.imgSrc.getValue();
    }

    public String getNameValue() {
        return this.name.getValue();
    }

    public String getEggHatchNameValue() {
        return this.eggHatchName.getValue();
    }

    public Integer getEggHatchImgSrcValue() {
        return this.eggHatchImgSrc.getValue();
    }

    public Long getEggHatchMoneyTxtValue() {
        return this.eggHatchMoneyTxt.getValue();
    }

    public Long getEggHatchXPTxtValue() {
        return this.eggHatchXPTxt.getValue();
    }

    public void setEggType(Integer eggType) {
        this.eggType.setValue(eggType);
    }

    public void setEggSteps(Integer eggSteps) {
        this.eggSteps.setValue(eggSteps);
    }

    public void setImgSrc(Integer imgSrc) {
        this.imgSrc.setValue(imgSrc);
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public void setEggHatchName(String name) {
        this.eggHatchName.setValue(name);
    }

    public void setEggHatchImgSrc(Integer src) {
        this.eggHatchImgSrc.setValue(src);
    }

    public void setEggHatchMoneyTxt(Long eggHatchMoneyTxt) {
        this.eggHatchMoneyTxt.setValue(eggHatchMoneyTxt);
    }

    public void setEggHatchXPTxt(Long eggHatchXPTxt) {
        this.eggHatchXPTxt.setValue(eggHatchXPTxt);
    }
}
