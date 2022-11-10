package iocari.pokehatcher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DexRecViewAdapter extends RecyclerView.Adapter<DexRecViewAdapter.ViewHolder> {
    View v;

    private ArrayList<DexItem> dexItems = new ArrayList<>();
    private ArrayList<DexItem> allDexItems = new ArrayList<>();
    private ArrayList<Boolean> obtainedFilters = new ArrayList<>();
    private ArrayList<Boolean> eggTypeFilters = new ArrayList<>();
    private ArrayList<Boolean> evolutionStageFilters = new ArrayList<>();
    private ArrayList<Boolean> qOLFilters = new ArrayList<>();
    private ArrayList<Boolean> sortOptions = new ArrayList<>();
    private DexEntryFragment dexEntryFragment;

    public DexRecViewAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dex_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();

        params.height = (int) (parent.getHeight() * 0.22);
        holder.itemView.setLayoutParams(params);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DexItem i = dexItems.get(position);
        if (i.hasCaught()) {
            holder.dexImg.setImageResource(v.getResources().getIdentifier(String.format("p_%d", i.getNumber()), "drawable", v.getContext().getPackageName()));
        } else {
            holder.dexImg.setImageResource(v.getResources().getIdentifier("pokeball", "drawable", v.getContext().getPackageName()));
        }
    }


    @Override
    public int getItemCount() {
        return dexItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView dexImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dexImg = itemView.findViewById(R.id.dexRecItemImg);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            DexItem pokemon = dexItems.get(getAdapterPosition());
            if (pokemon.hasCaught()) {
                Bundle args = new Bundle();
                args.putInt("dexNumber", pokemon.getNumber());
                dexEntryFragment = new DexEntryFragment();
                dexEntryFragment.setArguments(args);
                ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, dexEntryFragment).commit();
            }
        }
    }

    public void setDexItems(ArrayList<DexItem> dexItems) {
        this.allDexItems = dexItems;
        this.filterItems();
    }

    public void setObtainedFilters(ArrayList<Boolean> obtainedFilters) {
        this.obtainedFilters = obtainedFilters;
        this.filterItems();
    }

    public void setEggTypeFilters(ArrayList<Boolean> eggTypeFilters) {
        this.eggTypeFilters = eggTypeFilters;
        this.filterItems();
    }

    public void setEvolutionStageFilters(ArrayList<Boolean> evolutionStageFilters) {
        this.evolutionStageFilters = evolutionStageFilters;
        this.filterItems();
    }

    public void setQOLFilters(ArrayList<Boolean> qOLFilters) {
        this.qOLFilters = qOLFilters;
        this.filterItems();
    }

    public void setSortOptions(ArrayList<Boolean> sortOptions) {
        this.sortOptions = sortOptions;
        this.filterItems();
    }

    private void filterItems() {
        if (this.allDexItems.isEmpty()) return;
        this.dexItems = DexFilterApplier.getFilteredDexItems(this.allDexItems, this.obtainedFilters, this.eggTypeFilters, this.evolutionStageFilters, this.qOLFilters, this.sortOptions);
        notifyDataSetChanged();
    }
}
