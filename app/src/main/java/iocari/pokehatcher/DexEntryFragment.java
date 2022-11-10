package iocari.pokehatcher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class DexEntryFragment extends Fragment {
    View v;

    TextView name, number, classification, height, weight, type;
    ImageView img;
    DexItem dexItem;

    private int dexNumber;

    private DexVM dexVM;

    RecyclerView evoRec;
    EvolutionsRecViewAdapter adapter = new EvolutionsRecViewAdapter();

    public DexEntryFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.dex_entry, container, false);

        this.dexVM = new ViewModelProvider(requireActivity()).get(DexVM.class);

        Bundle args = getArguments();
        this.dexNumber = Objects.requireNonNull(args).getInt("dexNumber");

        this.dexItem = this.dexVM.getDexItem(this.dexNumber);
        this.adapter.setPokemon(this.dexItem);

        name = v.findViewById(R.id.dexEntryName);
        number = v.findViewById(R.id.dexEntryNumber);
        classification = v.findViewById(R.id.dexEntryClassification);
        height = v.findViewById(R.id.dexEntryHeight);
        weight = v.findViewById(R.id.dexEntryWeight);
        type = v.findViewById(R.id.dexEntryType);
        img = v.findViewById(R.id.dexEntryImg);


        evoRec = v.findViewById(R.id.dexEntryEvolutions);
        evoRec.setHasFixedSize(true);

        evoRec.setAdapter(adapter);
        evoRec.setLayoutManager(new LinearLayoutManager(v.getContext()));

        populate();

        return v;
    }


    public void populate() {
        name.setText(dexItem.getName());
        number.setText(String.format("%03d", dexItem.getNumber()));
        classification.setText(dexItem.getClassification() + " Pokémon");
        height.setText(dexItem.getHeight() + " m");
        weight.setText(dexItem.getWeight() + " kg");
        if (dexItem.getType2().equals("")) {
            type.setText(dexItem.getType1());
        } else {
            type.setText(dexItem.getType1() + "/" + dexItem.getType2());
        }
        img.setImageResource(dexItem.getImgId());
    }
}
