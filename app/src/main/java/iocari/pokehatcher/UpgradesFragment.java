package iocari.pokehatcher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class UpgradesFragment extends Fragment {
    private View v;

    private PlayerVM playerVM;
    private UpgradesVM upgradesVM;

    private Button uncommonBuy1;
    private Button uncommonBuy10;
    private Button rareBuy1;
    private Button rareBuy10;

    private TextView eggMultiplierLvl;
    private TextView eggMultiplierValue;
    private Button eggMultiplierBtn;

    private TextView critChanceLvl;
    private TextView critChanceValue;
    private Button critChanceBtn;

    private TextView extraEggChanceLvl;
    private TextView extraEggChanceValue;
    private Button extraEggChanceBtn;

    public UpgradesFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_upgrades, container, false);

        upgradesVM = new ViewModelProvider(requireActivity()).get(UpgradesVM.class);
        playerVM = new ViewModelProvider(requireActivity()).get(PlayerVM.class);

        initView();

        return v;
    }

    private void initView() {
        this.uncommonBuy1 = v.findViewById(R.id.upgradesEggsListUncommonEggBuy1);
        this.uncommonBuy10 = v.findViewById(R.id.upgradesEggsListUncommonEggBuy10);
        this.rareBuy1 = v.findViewById(R.id.upgradesEggsListRareEggBuy1);
        this.rareBuy10 = v.findViewById(R.id.upgradesEggsListRareEggBuy10);
        this.eggMultiplierLvl = v.findViewById(R.id.upgradesEggMultiplierLvl);
        this.eggMultiplierValue = v.findViewById(R.id.upgradesEggMultiplierValue);
        this.eggMultiplierBtn = v.findViewById(R.id.upgradesEggMultiplierBtn);
        this.critChanceLvl = v.findViewById(R.id.upgradesCritChanceLvl);
        this.critChanceValue = v.findViewById(R.id.upgradesCritChanceValue);
        this.critChanceBtn = v.findViewById(R.id.upgradesCritChanceBtn);
        this.extraEggChanceLvl = v.findViewById(R.id.upgradesExtraEggChanceLvl);
        this.extraEggChanceValue = v.findViewById(R.id.upgradesExtraEggChanceValue);
        this.extraEggChanceBtn = v.findViewById(R.id.upgradesExtraEggChanceBtn);

        this.uncommonBuy1.setOnClickListener(this::buy1Uncommon);
        this.uncommonBuy10.setOnClickListener(this::buy10Uncommon);
        this.rareBuy1.setOnClickListener(this::buy1Rare);
        this.rareBuy10.setOnClickListener(this::buy10Rare);
        this.eggMultiplierBtn.setOnClickListener(this::upgradeEggMultiplier);
        this.critChanceBtn.setOnClickListener(this::upgradeCritChance);
        this.extraEggChanceBtn.setOnClickListener(this::upgradeExtraEggChance);

        this.updateUpgrades();
    }

    private void updateUpgrades() {
        this.updateEggMultiplierUpgrade();
        this.updateCritChanceUpgrade();
        this.updateExtraEggChanceUpgrade();
    }

    private void updateEggMultiplierUpgrade() {
        if (this.playerVM.getCurrentStepMultiplierUpgrade() == 9) {
            this.eggMultiplierLvl.setText("Lvl Needed: ---");
            this.eggMultiplierBtn.setEnabled(false);
        } else {
            this.eggMultiplierLvl.setText(String.format("Lvl Needed: %d", this.upgradesVM.getStepMultiplierUpgrade(this.playerVM.getCurrentStepMultiplierUpgrade() + 1).getLvlNeeded()));
        }
        this.eggMultiplierValue.setText(String.format("x%.2f", this.playerVM.getClickMultiplierValue()));
    }

    private void updateCritChanceUpgrade() {
        if (this.playerVM.getCurrentCritChanceUpgrade() == 9) {
            this.critChanceLvl.setText("Lvl Needed: ---");
            this.critChanceBtn.setEnabled(false);
        } else {
            this.critChanceLvl.setText(String.format("Lvl Needed: %d", this.upgradesVM.getCritChanceUpgrade(this.playerVM.getCurrentCritChanceUpgrade() + 1).getLvlNeeded()));
        }
        this.critChanceValue.setText(String.format("%.2f%%", this.playerVM.getCritChanceValue()));
    }

    private void updateExtraEggChanceUpgrade() {
        if (this.playerVM.getCurrentExtraEggChanceUpgrade() == 9) {
            this.extraEggChanceLvl.setText("Lvl Needed: ---");
            this.extraEggChanceBtn.setEnabled(false);
        } else {
            this.extraEggChanceLvl.setText(String.format("Lvl Needed: %d", this.upgradesVM.getExtraEggChanceUpgrade(this.playerVM.getCurrentExtraEggChanceUpgrade() + 1).getLvlNeeded()));
        }
        this.extraEggChanceValue.setText(String.format("%.2f%%", this.playerVM.getExtraEggChanceValue()));
    }

    private void buy1Uncommon(View view) {
        if (playerVM.getMoneyValue() >= 100) {
            playerVM.buyEggs(1, 1);
        } else {
            Toast.makeText(v.getContext(), "Not enough money!", Toast.LENGTH_SHORT).show();
        }
    }

    private void buy10Uncommon(View view) {
        if (playerVM.getMoneyValue() >= 1000) {
            playerVM.buyEggs(1, 10);
        } else {
            Toast.makeText(v.getContext(), "Not enough money!", Toast.LENGTH_SHORT).show();
        }
    }

    private void buy1Rare(View view) {
        if (playerVM.getMoneyValue() >= 250) {
            playerVM.buyEggs(2, 1);
        } else {
            Toast.makeText(v.getContext(), "Not enough money!", Toast.LENGTH_SHORT).show();
        }
    }

    private void buy10Rare(View view) {
        if (playerVM.getMoneyValue() >= 2500) {
            playerVM.buyEggs(2, 10);
        } else {
            Toast.makeText(v.getContext(), "Not enough money!", Toast.LENGTH_SHORT).show();
        }
    }

    private void upgradeEggMultiplier(View view) {
        Upgrade nextUpgrade = this.upgradesVM.getStepMultiplierUpgrade(this.playerVM.getCurrentStepMultiplierUpgrade()+1);

        if (this.playerVM.getLevelValue() >= nextUpgrade.getLvlNeeded()) {
            this.playerVM.upgradeStepMultiplier(nextUpgrade);
            this.updateEggMultiplierUpgrade();
        } else {
            Toast.makeText(v.getContext(), "Not Experienced Enough!", Toast.LENGTH_SHORT).show();
        }
    }

    private void upgradeCritChance(View view) {
        Upgrade nextUpgrade = this.upgradesVM.getCritChanceUpgrade(this.playerVM.getCurrentCritChanceUpgrade()+1);

        if (this.playerVM.getLevelValue() >= nextUpgrade.getLvlNeeded()) {
            this.playerVM.upgradeCritChance(nextUpgrade);
            this.updateCritChanceUpgrade();
        } else {
            Toast.makeText(v.getContext(), "Not Experienced Enough!", Toast.LENGTH_SHORT).show();
        }
    }

    private void upgradeExtraEggChance(View view) {
        Upgrade nextUpgrade = this.upgradesVM.getExtraEggChanceUpgrade(this.playerVM.getCurrentExtraEggChanceUpgrade()+1);

        if (this.playerVM.getLevelValue() >= nextUpgrade.getLvlNeeded()) {
            this.playerVM.upgradeExtraEggChance(nextUpgrade);
            this.updateExtraEggChanceUpgrade();
        } else {
            Toast.makeText(v.getContext(), "Not Experienced Enough!", Toast.LENGTH_SHORT).show();
        }
    }
}
