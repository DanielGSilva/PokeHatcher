package iocari.pokehatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    EggFragment eggFragment;
    DexFragment dexFragment;
    UpgradesFragment upgradesFragment;

    Fragment selectedFragment;
    BottomNavigationView bottomNav;

    private PlayerVM playerVM;
    private DexVM dexVM;
    private UpgradesVM upgradesVM;
    private EggVM eggVM;

    TextView playerLevel, playerXp, playerMoney, uncommonEggQty, rareEggQty, legendaryEggQty;
    ImageButton commonEggBtn, uncommonEggBtn, rareEggBtn, legendaryEggBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dexVM = new ViewModelProvider(this).get(DexVM.class);
        initDexItems();
        initEggTypes();
        ArrayList<Boolean> obtainedFilters = new ArrayList<>(Arrays.asList(true, true));
        ArrayList<Boolean> eggTypeFilters = new ArrayList<>(Arrays.asList(true, true, true, true));
        ArrayList<Boolean> evolutionStageFilters = new ArrayList<>(Arrays.asList(true, true, true));
        ArrayList<Boolean> qOLFilters = new ArrayList<>(Arrays.asList(false, false));
        ArrayList<Boolean> sortOptions = new ArrayList<>(Arrays.asList(true, false));
        dexVM.setObtainedFilters(obtainedFilters);
        dexVM.setEggTypeFilters(eggTypeFilters);
        dexVM.setEvolutionStageFilters(evolutionStageFilters);
        dexVM.setQOLFilters(qOLFilters);
        dexVM.setSortOptions(sortOptions);

        upgradesVM = new ViewModelProvider(this).get(UpgradesVM.class);
        initUpgrades();

        playerVM = new ViewModelProvider(this).get(PlayerVM.class);
        initPlayerVM();

        eggVM = new ViewModelProvider(this).get(EggVM.class);
        initEggVM();

        dexFragment = new DexFragment();
        upgradesFragment = new UpgradesFragment();
        eggFragment = new EggFragment();
        selectedFragment = eggFragment;

        initPlayerView();
        this.bottomNav = findViewById(R.id.bottom_nav);
        this.bottomNav.setOnItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, eggFragment).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();

        this.savePlayer();
        this.saveDex();
    }

    private final BottomNavigationView.OnItemSelectedListener navListener = item -> {

        switch (item.getItemId()) {
            case R.id.nav_egg:
                selectedFragment = eggFragment;
                break;
            case R.id.nav_dex:
                selectedFragment = dexFragment;
                break;
            case R.id.nav_upgrades:
                selectedFragment = upgradesFragment;
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, selectedFragment).commit();
        return true;
    };

    private void initDexItems() {
        String[] name = this.getResources().getStringArray(R.array.gen1_names);
        String[] type1 = this.getResources().getStringArray(R.array.gen1_type1);
        String[] type2 = this.getResources().getStringArray(R.array.gen1_type2);
        String[] classification = this.getResources().getStringArray(R.array.gen1_classification);
        String[] height = this.getResources().getStringArray(R.array.gen1_height);
        String[] weight = this.getResources().getStringArray(R.array.gen1_weight);
        String[] eggType = this.getResources().getStringArray(R.array.gen1_eggtype);
        String[] stage = this.getResources().getStringArray(R.array.gen1_stage);
        String[] evolutions = this.getResources().getStringArray(R.array.gen1_evolutions);
        String[] price = this.getResources().getStringArray(R.array.gen1_price);

        ArrayList<DexItem> dexItems = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            dexItems.add(new DexItem(name[i], i+1, type1[i], type2[i], classification[i], height[i], weight[i], this.getResources().getIdentifier(String.format("p_%d", i+1), "drawable", this.getPackageName()), Integer.parseInt(eggType[i]), Integer.parseInt(stage[i]), Long.parseLong(price[i]), getEvolutionList(evolutions[i])));
        }

        FileInputStream fis;
        try {
            fis = getApplicationContext().openFileInput("dex.txt");

            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                String[] pokemon = stringBuilder.toString().split("\n");

                for (String s : pokemon) {
                    String[] data = s.split("/");
                    DexItem dexItem = dexItems.get(Integer.parseInt(data[0])-1);
                    dexItem.setCaught(true);
                    dexItem.setCount(Integer.parseInt(data[1]));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.dexVM.setDexItems(dexItems);
    }

    private ArrayList<ArrayList<Integer>> getEvolutionList(String evolution) {
        ArrayList<ArrayList<Integer>> evolutionList = new ArrayList<>();

        if (evolution.equals("/")) return evolutionList;

        ArrayList<Integer> evo = new ArrayList<>();

        for (String s : evolution.split(",")) {
            String[] evoS = s.split("/");

            evo.add(Integer.parseInt(evoS[0]));
            evo.add(Integer.parseInt(evoS[1]));

            evolutionList.add(evo);
        }

        return evolutionList;
    }

    private void initEggTypes() {
        ArrayList<ArrayList<Integer>> eggTypes = new ArrayList<>();
        ArrayList<Integer> commonEggs = new ArrayList<>();
        ArrayList<Integer> uncommonEggs = new ArrayList<>();
        ArrayList<Integer> rareEggs = new ArrayList<>();
        ArrayList<Integer> legendaryEggs = new ArrayList<>();

        for (DexItem d : this.dexVM.getDexItemsValue()) {
            if (!d.isBasic()) continue;
            switch (d.getEggType()) {
                case 0:
                    commonEggs.add(d.getNumber());
                    break;
                case 1:
                    uncommonEggs.add(d.getNumber());
                    break;
                case 2:
                    rareEggs.add(d.getNumber());
                    break;
                case 3:
                    legendaryEggs.add(d.getNumber());
                    break;
                default:
                    break;
            }
        }

        eggTypes.add(0, commonEggs);
        eggTypes.add(1, uncommonEggs);
        eggTypes.add(2, rareEggs);
        eggTypes.add(3, legendaryEggs);

        this.dexVM.setEggTypes(eggTypes);
    }

    private void initUpgrades() {
        ArrayList<Upgrade> eggMultiplierUpgrades = new ArrayList<>();
        ArrayList<Upgrade> critChanceUpgrades = new ArrayList<>();
        ArrayList<Upgrade> extraEggChanceUpgrades = new ArrayList<>();

        String[] eggMultiplierLvl = this.getResources().getStringArray(R.array.eggMultiplier_lvl);
        String[] eggMultiplierValue = this.getResources().getStringArray(R.array.eggMultiplier_value);
        String[] critChanceLvl = this.getResources().getStringArray(R.array.critChance_lvl);
        String[] critChanceValue = this.getResources().getStringArray(R.array.critChance_value);
        String[] extraEggChanceLvl = this.getResources().getStringArray(R.array.extraEggChance_lvl);
        String[] extraEggChanceValue = this.getResources().getStringArray(R.array.extraEggChance_value);

        for (int i = 0; i < eggMultiplierLvl.length; i++) {
            eggMultiplierUpgrades.add(new Upgrade(Long.parseLong(eggMultiplierLvl[i]), Double.parseDouble(eggMultiplierValue[i]), i));
            critChanceUpgrades.add(new Upgrade(Long.parseLong(critChanceLvl[i]), Double.parseDouble(critChanceValue[i]), i));
            extraEggChanceUpgrades.add(new Upgrade(Long.parseLong(extraEggChanceLvl[i]), Double.parseDouble(extraEggChanceValue[i]), i));
        }

        this.upgradesVM.setStepMultiplierUpgrades(eggMultiplierUpgrades);
        this.upgradesVM.setCritChanceUpgrades(critChanceUpgrades);
        this.upgradesVM.setExtraEggChanceUpgrades(extraEggChanceUpgrades);
    }

    private void initPlayerView() {
        this.playerLevel = findViewById(R.id.playerLevelTxt);
        this.playerXp = findViewById(R.id.playerXpTxt);
        this.playerMoney = findViewById(R.id.playerMoneyTxt);

        this.uncommonEggQty = findViewById(R.id.playerUncommonEggTxt);
        this.rareEggQty = findViewById(R.id.playerRareEggTxt);
        this.legendaryEggQty = findViewById(R.id.playerLegendaryEggTxt);

        this.commonEggBtn = findViewById(R.id.playerCommonEggImg);
        this.uncommonEggBtn = findViewById(R.id.playerUncommonEggImg);
        this.rareEggBtn = findViewById(R.id.playerRareEggImg);
        this.legendaryEggBtn = findViewById(R.id.playerLegendaryEggImg);

        this.updatePlayerView();
    }

    private void initPlayerVM() {
        FileInputStream fis;
        try {
            fis = getApplicationContext().openFileInput("player.txt");

            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                String[] player = stringBuilder.toString().split("\n");

                this.playerVM.setLevelValue(Long.parseLong(player[0]));
                this.playerVM.setXpValue(Long.parseLong(player[1]));
                this.playerVM.setMoneyValue(Long.parseLong(player[2]));
                this.playerVM.setClickMultiplier(Double.parseDouble(player[3]));
                this.playerVM.setCritChance(Double.parseDouble(player[4]));
                this.playerVM.setExtraEggChance(Double.parseDouble(player[5]));
                this.playerVM.setEggQtys(Integer.parseInt(player[6]), Integer.parseInt(player[7]), Integer.parseInt(player[8]));
                this.playerVM.setEggProgresses(Integer.parseInt(player[9]), Integer.parseInt(player[10]), Integer.parseInt(player[11]), Integer.parseInt(player[12]));
                this.playerVM.setCurrentUpgrades(new ArrayList<>(Arrays.asList(Integer.parseInt(player[13]), Integer.parseInt(player[14]), Integer.parseInt(player[15]))));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            this.playerVM.setLevelValue(0L);
            this.playerVM.setXpValue(0L);
            this.playerVM.setMoneyValue(0L);

            this.playerVM.setClickMultiplier(1.0);
            this.playerVM.setCritChance(0.5);
            this.playerVM.setExtraEggChance(0.1);

            this.playerVM.setEggQtys(1, 10, 100);
            this.playerVM.setEggProgresses(0, 0, 0, 0);
            this.playerVM.setCurrentUpgrades(new ArrayList<>(Arrays.asList(0, 0, 0)));
        }

        this.playerVM.getLevel().observe(this, levelObserver);
        this.playerVM.getXp().observe(this, xpObserver);
        this.playerVM.getXpNeeded().observe(this, xpObserver);
        this.playerVM.getMoney().observe(this, moneyObserver);

        this.playerVM.getUncommonEggs().observe(this, uncommonEggsObserver);
        this.playerVM.getRareEggs().observe(this, rareEggsObserver);
        this.playerVM.getLegendaryEggs().observe(this, legendaryEggsObserver);
    }

    private void initEggVM() {
        this.eggVM.setEggType(0);
        this.eggVM.setEggSteps(2560);
        this.eggVM.setImgSrc(R.drawable.egg_common);
        this.eggVM.setName("Common Egg");
        this.eggVM.setEggHatchName("");
        this.eggVM.setEggHatchImgSrc(R.drawable.egg_common);
        this.eggVM.setEggHatchMoneyTxt(this.playerVM.getEggMoney(this.eggVM.getEggTypeValue()));
        this.eggVM.setEggHatchXPTxt(this.playerVM.getEggXP(this.eggVM.getEggTypeValue()));

        this.eggVM.getEggType().observe(this, eggTypeObserver);
    }

    private void updatePlayerView() {
        this.playerLevel.setText(String.format("Lvl %d", this.playerVM.getLevelValue()));
        this.playerXp.setText(String.format("%d/%d", this.playerVM.getXpValue(), this.playerVM.getXpNeededValue()));
        this.playerMoney.setText(String.format("%s", this.playerVM.getMoneyText()));
        this.uncommonEggQty.setText(String.format("%d", this.playerVM.getEggQty(1)));
        this.rareEggQty.setText(String.format("%d", this.playerVM.getEggQty(2)));
        this.legendaryEggQty.setText(String.format("%d", this.playerVM.getEggQty(3)));
    }

    private Observer<Long> levelObserver = new Observer<Long>() {
        @Override
        public void onChanged(Long value) {
            playerLevel.setText(String.format("Lvl %d", playerVM.getLevelValue()));
            playerLevelUpAnimation();
        }
    };

    private Observer<Long> xpObserver = new Observer<Long>() {
        @Override
        public void onChanged(Long value) {
            playerXp.setText(playerVM.getXPText());
        }
    };

    private Observer<Long> moneyObserver = new Observer<Long>() {
        @Override
        public void onChanged(Long value) {
            playerMoney.setText(playerVM.getMoneyText());
        }
    };

    private Observer<Integer> uncommonEggsObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            uncommonEggQty.setText(playerVM.getEggQtyText(1));
        }
    };

    private Observer<Integer> rareEggsObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            rareEggQty.setText(playerVM.getEggQtyText(2));
        }
    };

    private Observer<Integer> legendaryEggsObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            legendaryEggQty.setText(playerVM.getEggQtyText(3));
        }
    };

    private Observer<Integer> eggTypeObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            unselectEggs();
            switch(integer) {
                case 0:
                    commonEggBtn.setBackground(getDrawable(R.drawable.round_corners_selected));
                    break;
                case 1:
                    uncommonEggBtn.setBackground(getDrawable(R.drawable.round_corners_selected));
                    break;
                case 2:
                    rareEggBtn.setBackground(getDrawable(R.drawable.round_corners_selected));
                    break;
                case 3:
                    legendaryEggBtn.setBackground(getDrawable(R.drawable.round_corners_selected));
                    break;
                default:
                    break;
            }
        }
    };

    public void unsetPlayerListeners() {
        this.commonEggBtn.setOnClickListener(null);
        this.uncommonEggBtn.setOnClickListener(null);
        this.rareEggBtn.setOnClickListener(null);
        this.legendaryEggBtn.setOnClickListener(null);
        this.unsetMenuListeners();
    }

    public void setPlayerListeners() {
        this.commonEggBtn.setOnClickListener(this::clickCommonEgg);
        this.uncommonEggBtn.setOnClickListener(this::clickUncommonEgg);
        this.rareEggBtn.setOnClickListener(this::clickRareEgg);
        this.legendaryEggBtn.setOnClickListener(this::clickLegendaryEgg);
        this.setMenuListeners();
    }

    private void unsetMenuListeners() {
        Menu menu = this.bottomNav.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setEnabled(false);
        }
    }

    private void setMenuListeners() {
        Menu menu = this.bottomNav.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setEnabled(true);
        }
    }

    public void clickCommonEgg(View view) {
        this.eggFragment.changeEgg(0);
        this.selectEggFragment();
    }

    public void clickUncommonEgg(View view) {
        if (this.playerVM.getEggQty(1) > 0) {
            this.eggFragment.changeEgg(1);
            this.selectEggFragment();
        }
    }

    public void clickRareEgg(View view) {
        if (this.playerVM.getEggQty(2) > 0) {
            this.eggFragment.changeEgg(2);
            this.selectEggFragment();
        }
    }

    public void clickLegendaryEgg(View view) {
        if (this.playerVM.getEggQty(3) > 0) {
            this.eggFragment.changeEgg(3);
            this.selectEggFragment();
        }
    }

    private void selectEggFragment() {
        this.selectedFragment = this.eggFragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, selectedFragment).commit();
        this.bottomNav.setSelectedItemId(R.id.nav_egg);
    }

    private void unselectEggs() {
        this.commonEggBtn.setBackground(getDrawable(R.drawable.round_corners_unselected));
        this.uncommonEggBtn.setBackground(getDrawable(R.drawable.round_corners_unselected));
        this.rareEggBtn.setBackground(getDrawable(R.drawable.round_corners_unselected));
        this.legendaryEggBtn.setBackground(getDrawable(R.drawable.round_corners_unselected));
    }

    private void playerLevelUpAnimation() {
        this.playerLevel.animate()
                .scaleX(1.5F)
                .scaleY(1.1F)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        playerLevel.animate()
                                .scaleX(1)
                                .scaleY(1)
                                .start();
                    }
                })
                .start();
    }

    private void savePlayer() {
        StringBuilder builder = new StringBuilder();

        builder.append(this.playerVM.getLevelValue()).append("\n");
        builder.append(this.playerVM.getXpValue()).append("\n");
        builder.append(this.playerVM.getMoneyValue()).append("\n");
        builder.append(this.playerVM.getClickMultiplierValue()).append("\n");
        builder.append(this.playerVM.getCritChanceValue()).append("\n");
        builder.append(this.playerVM.getExtraEggChanceValue()).append("\n");
        builder.append(this.playerVM.getEggQty(1)).append("\n");
        builder.append(this.playerVM.getEggQty(2)).append("\n");
        builder.append(this.playerVM.getEggQty(3)).append("\n");
        builder.append(this.playerVM.getEggProgress(0)).append("\n");
        builder.append(this.playerVM.getEggProgress(1)).append("\n");
        builder.append(this.playerVM.getEggProgress(2)).append("\n");
        builder.append(this.playerVM.getEggProgress(3)).append("\n");
        builder.append(this.playerVM.getCurrentStepMultiplierUpgrade()).append("\n");
        builder.append(this.playerVM.getCurrentCritChanceUpgrade()).append("\n");
        builder.append(this.playerVM.getCurrentExtraEggChanceUpgrade()).append("\n");

        try (FileOutputStream fos = getApplicationContext().openFileOutput("player.txt", Context.MODE_PRIVATE)) {
            fos.write(builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDex() {
        StringBuilder builder = new StringBuilder();

        for (DexItem d : this.dexVM.getDexItemsValue()) {
            if (d.hasCaught()) {
                builder.append(d.getNumber()).append("/");
                builder.append(d.getCount()).append("\n");
            }
        }

        try (FileOutputStream fos = getApplicationContext().openFileOutput("dex.txt", Context.MODE_PRIVATE)) {
            fos.write(builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}