package iocari.pokehatcher;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class DexFragment extends Fragment {
    View v;

    private DexVM dexVM;

    private boolean menuOpened;

    private ConstraintLayout dexMenuLayout;
    private ImageView menuButton;

    private TextView dexMenuFilterObtainedAllBtn, dexMenuFilterObtainedBtn, dexMenuFilterUnobtainedBtn;

    private TextView dexMenuFilterEggTypeAllBtn;
    private ImageView dexMenuEggType0Btn, dexMenuEggType1Btn, dexMenuEggType2Btn, dexMenuEggType3Btn;

    private TextView dexMenuFilterEvolutionStageAllBtn;
    private TextView dexMenuEvolutionStage0Btn, dexMenuEvolutionStage1Btn, dexMenuEvolutionStage2Btn;

    private TextView dexMenuFilterReadyToEvolveBtn, dexMenuFilterFinalFormsBtn;

    private TextView dexMenuSortByNumberBtn, dexMenuSortByQtyBtn;

    private RecyclerView dexRec;
    private DexRecViewAdapter adapter = new DexRecViewAdapter();

    public DexFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_dex, container, false);

        this.dexVM = new ViewModelProvider(requireActivity()).get(DexVM.class);

        dexRec = v.findViewById(R.id.dexRec);
        dexRec.setHasFixedSize(true);

        this.dexVM.getDexItems().observe(getViewLifecycleOwner(), dexItemsObserver);
        this.dexVM.getObtainedFilters().observe(getViewLifecycleOwner(), obtainedFiltersObserver);
        this.dexVM.getEggTypeFilters().observe(getViewLifecycleOwner(), eggTypeFiltersObserver);
        this.dexVM.getEvolutionStageFilters().observe(getViewLifecycleOwner(), evolutionStageFiltersObserver);
        this.dexVM.getQOLFilters().observe(getViewLifecycleOwner(), qOLFiltersObserver);
        this.dexVM.getSortOptions().observe(getViewLifecycleOwner(), sortOptionsObserver);

        adapter.setObtainedFilters(this.dexVM.getObtainedFiltersValue());
        adapter.setEggTypeFilters(this.dexVM.getEggTypeFiltersValue());
        adapter.setEvolutionStageFilters(this.dexVM.getEvolutionStageFiltersValue());
        adapter.setQOLFilters(this.dexVM.getQOLFiltersValue());
        adapter.setSortOptions(this.dexVM.getSortOptionsValue());
        adapter.setDexItems(this.dexVM.getDexItemsValue());
        dexRec.setAdapter(adapter);
        dexRec.setLayoutManager(new GridLayoutManager(v.getContext(), 4));

        this.initMenuView();
        this.menuOpened = false;

        return v;
    }

    private Observer<ArrayList<DexItem>> dexItemsObserver = new Observer<ArrayList<DexItem>>() {
        @Override
        public void onChanged(ArrayList<DexItem> dexItems) {
            adapter.setDexItems(dexItems);
        }
    };

    private Observer<ArrayList<Boolean>> obtainedFiltersObserver = new Observer<ArrayList<Boolean>>() {
        @Override
        public void onChanged(ArrayList<Boolean> obtainedFilters) {
            updateObtainedFiltersView(obtainedFilters);
            adapter.setObtainedFilters(obtainedFilters);
        }
    };

    private Observer<ArrayList<Boolean>> eggTypeFiltersObserver = new Observer<ArrayList<Boolean>>() {
        @Override
        public void onChanged(ArrayList<Boolean> eggTypeFilters) {
            updateEggTypeFiltersView(eggTypeFilters);
            adapter.setEggTypeFilters(eggTypeFilters);
        }
    };

    private Observer<ArrayList<Boolean>> evolutionStageFiltersObserver = new Observer<ArrayList<Boolean>>() {
        @Override
        public void onChanged(ArrayList<Boolean> evolutionStageFilters) {
            updateEvolutionStageFiltersView(evolutionStageFilters);
            adapter.setEvolutionStageFilters(evolutionStageFilters);
        }
    };

    private Observer<ArrayList<Boolean>> qOLFiltersObserver = new Observer<ArrayList<Boolean>>() {
        @Override
        public void onChanged(ArrayList<Boolean> qOLFilters) {
            updateQOLFiltersView(qOLFilters);
            adapter.setQOLFilters(qOLFilters);
        }
    };

    private Observer<ArrayList<Boolean>> sortOptionsObserver = new Observer<ArrayList<Boolean>>() {
        @Override
        public void onChanged(ArrayList<Boolean> sortOptions) {
            updateSortOptionsView(sortOptions);
            adapter.setSortOptions(sortOptions);
        }
    };

    private void initMenuView() {
        this.dexMenuLayout = v.findViewById(R.id.dexMenuLayout);
        this.menuButton = v.findViewById(R.id.dexMenuButton);

        this.dexMenuFilterObtainedAllBtn = v.findViewById(R.id.dexMenuObtainedFiltersAll);
        this.dexMenuFilterObtainedBtn = v.findViewById(R.id.dexMenuFiltersObtained);
        this.dexMenuFilterUnobtainedBtn = v.findViewById(R.id.dexMenuFiltersUnobtained);

        this.dexMenuFilterEggTypeAllBtn = v.findViewById(R.id.dexMenuEggTypeFiltersAll);
        this.dexMenuEggType0Btn = v.findViewById(R.id.dexMenuFiltersEggType0);
        this.dexMenuEggType1Btn = v.findViewById(R.id.dexMenuFiltersEggType1);
        this.dexMenuEggType2Btn = v.findViewById(R.id.dexMenuFiltersEggType2);
        this.dexMenuEggType3Btn = v.findViewById(R.id.dexMenuFiltersEggType3);

        this.dexMenuFilterEvolutionStageAllBtn = v.findViewById(R.id.dexMenuEvolutionStageFiltersAll);
        this.dexMenuEvolutionStage0Btn = v.findViewById(R.id.dexMenuFiltersBasic);
        this.dexMenuEvolutionStage1Btn = v.findViewById(R.id.dexMenuFiltersStage1);
        this.dexMenuEvolutionStage2Btn = v.findViewById(R.id.dexMenuFiltersStage2);

        this.dexMenuFilterReadyToEvolveBtn = v.findViewById(R.id.dexMenuFiltersReadyToEvolve);
        this.dexMenuFilterFinalFormsBtn = v.findViewById(R.id.dexMenuFiltersFinalForms);

        this.dexMenuSortByNumberBtn = v.findViewById(R.id.dexMenuSortByNumber);
        this.dexMenuSortByQtyBtn = v.findViewById(R.id.dexMenuSortByQty);

        this.menuButton.setOnClickListener(this::clickMenuButton);

        this.dexMenuFilterObtainedAllBtn.setOnClickListener(this::clickObtainedAllFilterButton);
        this.dexMenuFilterObtainedBtn.setOnClickListener(this::clickObtainedFilterButton);
        this.dexMenuFilterUnobtainedBtn.setOnClickListener(this::clickUnobtainedFilterButton);

        this.dexMenuFilterEggTypeAllBtn.setOnClickListener(this::clickEggTypeAllFilterButton);
        this.dexMenuEggType0Btn.setOnClickListener(this::clickEggType0FilterButton);
        this.dexMenuEggType1Btn.setOnClickListener(this::clickEggType1FilterButton);
        this.dexMenuEggType2Btn.setOnClickListener(this::clickEggType2FilterButton);
        this.dexMenuEggType3Btn.setOnClickListener(this::clickEggType3FilterButton);

        this.dexMenuFilterEvolutionStageAllBtn.setOnClickListener(this::clickEvolutionStageAllFilterButton);
        this.dexMenuEvolutionStage0Btn.setOnClickListener(this::clickEvolutionStage0FilterButton);
        this.dexMenuEvolutionStage1Btn.setOnClickListener(this::clickEvolutionStage1FilterButton);
        this.dexMenuEvolutionStage2Btn.setOnClickListener(this::clickEvolutionStage2FilterButton);

        this.dexMenuFilterReadyToEvolveBtn.setOnClickListener(this::clickReadyToEvolveFilterButton);
        this.dexMenuFilterFinalFormsBtn.setOnClickListener(this::clickFinalFormsFilterButton);

        this.dexMenuSortByNumberBtn.setOnClickListener(this::clickSortByNumberButton);
        this.dexMenuSortByQtyBtn.setOnClickListener(this::clickSortByQtyButton);
    }

    private void updateObtainedFiltersView(ArrayList<Boolean> obtainedFilters) {
        boolean obtained = obtainedFilters.get(0);
        boolean unobtained = obtainedFilters.get(1);

        int blackColor = requireActivity().getColor(R.color.black);
        int greyColor = requireActivity().getColor(R.color.grey_text);

        if (obtained) {
            this.dexMenuFilterObtainedBtn.setBackgroundResource(R.drawable.filter_selected);
            this.dexMenuFilterObtainedBtn.setTextColor(blackColor);
        } else {
            this.dexMenuFilterObtainedBtn.setBackgroundResource(R.drawable.filter_unselected);
            this.dexMenuFilterObtainedBtn.setTextColor(greyColor);
        }

        if (unobtained) {
            this.dexMenuFilterUnobtainedBtn.setBackgroundResource(R.drawable.filter_selected);
            this.dexMenuFilterUnobtainedBtn.setTextColor(blackColor);
        } else {
            this.dexMenuFilterUnobtainedBtn.setBackgroundResource(R.drawable.filter_unselected);
            this.dexMenuFilterUnobtainedBtn.setTextColor(greyColor);
        }

        if (obtained && unobtained) {
            this.dexMenuFilterObtainedAllBtn.setBackgroundResource(R.drawable.filter_selected);
            this.dexMenuFilterObtainedAllBtn.setTextColor(blackColor);
        } else {
            this.dexMenuFilterObtainedAllBtn.setBackgroundResource(R.drawable.filter_unselected);
            this.dexMenuFilterObtainedAllBtn.setTextColor(greyColor);
        }
        this.dexMenuFilterObtainedBtn.setPadding(0,0,0,0);
    }

    private void updateEggTypeFiltersView(ArrayList<Boolean> eggTypeFilters) {
        boolean eggType0 = eggTypeFilters.get(0);
        boolean eggType1 = eggTypeFilters.get(1);
        boolean eggType2 = eggTypeFilters.get(2);
        boolean eggType3 = eggTypeFilters.get(3);

        int blackColor = requireActivity().getColor(R.color.black);
        int greyColor = requireActivity().getColor(R.color.grey_text);

        if (eggType0) {
            this.dexMenuEggType0Btn.setBackgroundResource(R.drawable.filter_selected);
        } else {
            this.dexMenuEggType0Btn.setBackgroundResource(R.drawable.filter_unselected);
        }

        if (eggType1) {
            this.dexMenuEggType1Btn.setBackgroundResource(R.drawable.filter_selected);
        } else {
            this.dexMenuEggType1Btn.setBackgroundResource(R.drawable.filter_unselected);
        }

        if (eggType2) {
            this.dexMenuEggType2Btn.setBackgroundResource(R.drawable.filter_selected);
        } else {
            this.dexMenuEggType2Btn.setBackgroundResource(R.drawable.filter_unselected);
        }

        if (eggType3) {
            this.dexMenuEggType3Btn.setBackgroundResource(R.drawable.filter_selected);
        } else {
            this.dexMenuEggType3Btn.setBackgroundResource(R.drawable.filter_unselected);
        }

        if (eggType0 && eggType1 && eggType2 && eggType3) {
            this.dexMenuFilterEggTypeAllBtn.setBackgroundResource(R.drawable.filter_selected);
            this.dexMenuFilterEggTypeAllBtn.setTextColor(blackColor);
        } else {
            this.dexMenuFilterEggTypeAllBtn.setBackgroundResource(R.drawable.filter_unselected);
            this.dexMenuFilterEggTypeAllBtn.setTextColor(greyColor);
        }
        this.dexMenuFilterObtainedBtn.setPadding(0,0,0,0);
    }

    private void updateEvolutionStageFiltersView(ArrayList<Boolean> evolutionStageFilters) {
        boolean evolutionStage0 = evolutionStageFilters.get(0);
        boolean evolutionStage1 = evolutionStageFilters.get(1);
        boolean evolutionStage2 = evolutionStageFilters.get(2);

        int blackColor = requireActivity().getColor(R.color.black);
        int greyColor = requireActivity().getColor(R.color.grey_text);

        if (evolutionStage0) {
            this.dexMenuEvolutionStage0Btn.setBackgroundResource(R.drawable.filter_selected);
            this.dexMenuEvolutionStage0Btn.setTextColor(blackColor);
        } else {
            this.dexMenuEvolutionStage0Btn.setBackgroundResource(R.drawable.filter_unselected);
            this.dexMenuEvolutionStage0Btn.setTextColor(greyColor);
        }

        if (evolutionStage1) {
            this.dexMenuEvolutionStage1Btn.setBackgroundResource(R.drawable.filter_selected);
            this.dexMenuEvolutionStage1Btn.setTextColor(blackColor);
        } else {
            this.dexMenuEvolutionStage1Btn.setBackgroundResource(R.drawable.filter_unselected);
            this.dexMenuEvolutionStage1Btn.setTextColor(greyColor);
        }

        if (evolutionStage2) {
            this.dexMenuEvolutionStage2Btn.setBackgroundResource(R.drawable.filter_selected);
            this.dexMenuEvolutionStage2Btn.setTextColor(blackColor);
        } else {
            this.dexMenuEvolutionStage2Btn.setBackgroundResource(R.drawable.filter_unselected);
            this.dexMenuEvolutionStage2Btn.setTextColor(greyColor);
        }

        if (evolutionStage0 && evolutionStage1 && evolutionStage2) {
            this.dexMenuFilterEvolutionStageAllBtn.setBackgroundResource(R.drawable.filter_selected);
            this.dexMenuFilterEvolutionStageAllBtn.setTextColor(blackColor);
        } else {
            this.dexMenuFilterEvolutionStageAllBtn.setBackgroundResource(R.drawable.filter_unselected);
            this.dexMenuFilterEvolutionStageAllBtn.setTextColor(greyColor);
        }
        this.dexMenuFilterObtainedBtn.setPadding(0,0,0,0);
    }

    private void updateQOLFiltersView(ArrayList<Boolean> qOLFilters) {
        boolean readyToEvolveFilter = qOLFilters.get(0);
        boolean finalFormsFilter = qOLFilters.get(1);

        int blackColor = requireActivity().getColor(R.color.black);
        int greyColor = requireActivity().getColor(R.color.grey_text);

        if (readyToEvolveFilter) {
            this.dexMenuFilterReadyToEvolveBtn.setBackgroundResource(R.drawable.filter_selected);
            this.dexMenuFilterReadyToEvolveBtn.setTextColor(blackColor);
        } else {
            this.dexMenuFilterReadyToEvolveBtn.setBackgroundResource(R.drawable.filter_unselected);
            this.dexMenuFilterReadyToEvolveBtn.setTextColor(greyColor);
        }

        if (finalFormsFilter) {
            this.dexMenuFilterFinalFormsBtn.setBackgroundResource(R.drawable.filter_selected);
            this.dexMenuFilterFinalFormsBtn.setTextColor(blackColor);
        } else {
            this.dexMenuFilterFinalFormsBtn.setBackgroundResource(R.drawable.filter_unselected);
            this.dexMenuFilterFinalFormsBtn.setTextColor(greyColor);
        }
    }

    private void updateSortOptionsView(ArrayList<Boolean> sortOptions) {
        boolean sortByNumber = sortOptions.get(0);

        int blackColor = requireActivity().getColor(R.color.black);
        int greyColor = requireActivity().getColor(R.color.grey_text);

        if (sortByNumber) {
            this.dexMenuSortByNumberBtn.setBackgroundResource(R.drawable.filter_selected);
            this.dexMenuSortByNumberBtn.setTextColor(blackColor);
            this.dexMenuSortByQtyBtn.setBackgroundResource(R.drawable.filter_unselected);
            this.dexMenuSortByQtyBtn.setTextColor(greyColor);
        } else {
            this.dexMenuSortByNumberBtn.setBackgroundResource(R.drawable.filter_unselected);
            this.dexMenuSortByNumberBtn.setTextColor(greyColor);
            this.dexMenuSortByQtyBtn.setBackgroundResource(R.drawable.filter_selected);
            this.dexMenuSortByQtyBtn.setTextColor(blackColor);
        }
    }

    private void clickMenuButton(View view) {
        if (this.menuOpened) {
            this.closeMenu();
        } else {
            this.openMenu();
        }
    }

    private void closeMenu() {
        this.menuOpened = false;

        float finalY = (float) (this.v.getBottom()*0.95);

        this.dexMenuLayout.animate()
                .y(finalY)
                .withEndAction(() -> this.menuButton.setImageResource(R.drawable.up_arrow))
                .start();
    }

    private void openMenu() {
        this.menuOpened = true;

        float finalY = (float) (this.v.getBottom()*0.5);

        this.dexMenuLayout.animate()
                .y(finalY)
                .withEndAction(() -> this.menuButton.setImageResource(R.drawable.down_arrow))
                .start();
    }

    private void clickObtainedAllFilterButton(View view) {
        ArrayList<Boolean> obtainedFilters = this.dexVM.getObtainedFiltersValue();
        boolean obtained = obtainedFilters.get(0);
        boolean unobtained = obtainedFilters.get(1);

        if (obtained && unobtained) {
            obtainedFilters = new ArrayList<>(Arrays.asList(false, false));
        } else {
            obtainedFilters = new ArrayList<>(Arrays.asList(true, true));
        }

        this.dexVM.setObtainedFilters(obtainedFilters);
    }

    private void clickObtainedFilterButton(View view) {
        ArrayList<Boolean> obtainedFilters = this.dexVM.getObtainedFiltersValue();
        boolean value = obtainedFilters.get(0);

        obtainedFilters.set(0, !value);
        this.dexVM.setObtainedFilters(obtainedFilters);
    }

    private void clickUnobtainedFilterButton(View view) {
        ArrayList<Boolean> obtainedFilters = this.dexVM.getObtainedFiltersValue();
        boolean value = obtainedFilters.get(1);

        obtainedFilters.set(1, !value);
        this.dexVM.setObtainedFilters(obtainedFilters);
    }

    private void clickEggTypeAllFilterButton(View view) {
        ArrayList<Boolean> eggTypeFilters = this.dexVM.getEggTypeFiltersValue();
        boolean eggType0 = eggTypeFilters.get(0);
        boolean eggType1 = eggTypeFilters.get(1);
        boolean eggType2 = eggTypeFilters.get(2);
        boolean eggType3 = eggTypeFilters.get(3);

        if (eggType0 && eggType1 && eggType2 && eggType3) {
            eggTypeFilters = new ArrayList<>(Arrays.asList(false, false, false, false));
        } else {
            eggTypeFilters = new ArrayList<>(Arrays.asList(true, true, true, true));
        }

        this.dexVM.setEggTypeFilters(eggTypeFilters);
    }

    private void clickEggType0FilterButton(View view) {
        ArrayList<Boolean> eggTypeFilters = this.dexVM.getEggTypeFiltersValue();
        boolean value = eggTypeFilters.get(0);

        eggTypeFilters.set(0, !value);
        this.dexVM.setEggTypeFilters(eggTypeFilters);
    }

    private void clickEggType1FilterButton(View view) {
        ArrayList<Boolean> eggTypeFilters = this.dexVM.getEggTypeFiltersValue();
        boolean value = eggTypeFilters.get(1);

        eggTypeFilters.set(1, !value);
        this.dexVM.setEggTypeFilters(eggTypeFilters);
    }

    private void clickEggType2FilterButton(View view) {
        ArrayList<Boolean> eggTypeFilters = this.dexVM.getEggTypeFiltersValue();
        boolean value = eggTypeFilters.get(2);

        eggTypeFilters.set(2, !value);
        this.dexVM.setEggTypeFilters(eggTypeFilters);
    }

    private void clickEggType3FilterButton(View view) {
        ArrayList<Boolean> eggTypeFilters = this.dexVM.getEggTypeFiltersValue();
        boolean value = eggTypeFilters.get(3);

        eggTypeFilters.set(3, !value);
        this.dexVM.setEggTypeFilters(eggTypeFilters);
    }


    private void clickEvolutionStageAllFilterButton(View view) {
        ArrayList<Boolean> evolutionStageFilters = this.dexVM.getEvolutionStageFiltersValue();
        boolean evolutionStage0 = evolutionStageFilters.get(0);
        boolean evolutionStage1 = evolutionStageFilters.get(1);
        boolean evolutionStage2 = evolutionStageFilters.get(2);

        if (evolutionStage0 && evolutionStage1 && evolutionStage2) {
            evolutionStageFilters = new ArrayList<>(Arrays.asList(false, false, false));
        } else {
            evolutionStageFilters = new ArrayList<>(Arrays.asList(true, true, true));
        }

        this.dexVM.setEvolutionStageFilters(evolutionStageFilters);
    }

    private void clickEvolutionStage0FilterButton(View view) {
        ArrayList<Boolean> evolutionStageFilters = this.dexVM.getEvolutionStageFiltersValue();
        boolean value = evolutionStageFilters.get(0);

        evolutionStageFilters.set(0, !value);
        this.dexVM.setEvolutionStageFilters(evolutionStageFilters);
    }

    private void clickEvolutionStage1FilterButton(View view) {
        ArrayList<Boolean> evolutionStageFilters = this.dexVM.getEvolutionStageFiltersValue();
        boolean value = evolutionStageFilters.get(1);

        evolutionStageFilters.set(1, !value);
        this.dexVM.setEvolutionStageFilters(evolutionStageFilters);
    }

    private void clickEvolutionStage2FilterButton(View view) {
        ArrayList<Boolean> evolutionStageFilters = this.dexVM.getEvolutionStageFiltersValue();
        boolean value = evolutionStageFilters.get(2);

        evolutionStageFilters.set(2, !value);
        this.dexVM.setEvolutionStageFilters(evolutionStageFilters);
    }

    private void clickReadyToEvolveFilterButton(View view) {
        ArrayList<Boolean> qOLFilters = this.dexVM.getQOLFiltersValue();
        boolean value = qOLFilters.get(0);

        if (value) {
            qOLFilters.set(0, false);
        } else {
            qOLFilters.set(0, true);
            qOLFilters.set(1, false);
        }

        this.dexVM.setQOLFilters(qOLFilters);
    }

    private void clickFinalFormsFilterButton(View view) {
        ArrayList<Boolean> qOLFilters = this.dexVM.getQOLFiltersValue();
        boolean value = qOLFilters.get(1);

        if (value) {
            qOLFilters.set(1, false);
        } else {
            qOLFilters.set(1, true);
            qOLFilters.set(0, false);
        }

        this.dexVM.setQOLFilters(qOLFilters);
    }

    private void clickSortByNumberButton(View view) {
        ArrayList<Boolean> sortOptions = this.dexVM.getSortOptionsValue();
        boolean value = sortOptions.get(0);

        if (!value) {
            sortOptions = new ArrayList<>(Arrays.asList(true, false));
            this.dexVM.setSortOptions(sortOptions);
        }
    }

    private void clickSortByQtyButton(View view) {
        ArrayList<Boolean> sortOptions = this.dexVM.getSortOptionsValue();
        boolean value = sortOptions.get(1);

        if (!value) {
            sortOptions = new ArrayList<>(Arrays.asList(false, true));
            this.dexVM.setSortOptions(sortOptions);
        }
    }
}
