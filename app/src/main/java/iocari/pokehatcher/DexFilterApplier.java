package iocari.pokehatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class DexFilterApplier {

    private static Comparator<DexItem> dexNumberComparator = new Comparator<DexItem>() {
        @Override
        public int compare(DexItem d1, DexItem d2) {
            return Integer.compare(d1.getNumber(), d2.getNumber());
        }
    };

    private static Comparator<DexItem> qtyDescComparator = new Comparator<DexItem>() {
        @Override
        public int compare(DexItem d1, DexItem d2) {
            if (d1.hasCaught() && !d2.hasCaught()) return -1;
            else if (!d1.hasCaught() && d2.hasCaught()) return 1;

            int result = Integer.compare(d1.getCount(), d2.getCount());
            return result == 0 ? Integer.compare(d1.getNumber(), d2.getNumber()) : -result;
        }
    };

    public static ArrayList<DexItem> getFilteredDexItems(ArrayList<DexItem> dexItems, ArrayList<Boolean> obtainedFilters, ArrayList<Boolean> eggTypeFilters, ArrayList<Boolean> evolutionStageFilters, ArrayList<Boolean> qOLFilters, ArrayList<Boolean> sortOptions) {
        ArrayList<DexItem> filtered = new ArrayList<>();

        boolean filterObtained = !obtainedFilters.get(0);
        boolean filterUnobtained = !obtainedFilters.get(1);

        for (int i = 0; i < dexItems.size(); i++) {
            DexItem dexItem = dexItems.get(i);

            if (filterObtained && dexItem.hasCaught()) continue;
            if (filterUnobtained && !dexItem.hasCaught()) continue;

            int eggType = dexItem.getEggType();
            if (!eggTypeFilters.get(eggType)) continue;

            int evolutionStage = dexItem.getStage();
            if (!evolutionStageFilters.get(evolutionStage)) continue;

            if (qOLFilters.get(0) && !dexItem.canEvolve()) continue;
            if (qOLFilters.get(1) && !dexItem.isFinal()) continue;

            filtered.add(dexItem);
        }

        if (sortOptions.get(0)) {
            filtered.sort(dexNumberComparator);
        } else if (sortOptions.get(1)) {
            filtered.sort(qtyDescComparator);
        }

        return filtered;
    }
}
