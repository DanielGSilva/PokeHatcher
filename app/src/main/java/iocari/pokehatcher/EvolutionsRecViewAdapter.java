package iocari.pokehatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EvolutionsRecViewAdapter extends RecyclerView.Adapter<EvolutionsRecViewAdapter.ViewHolder> {
    View v;

    private PlayerVM playerVM;
    private DexVM dexVM;

    private ArrayList<ArrayList<Integer>> evolutions;
    private DexItem pokemon;

    public EvolutionsRecViewAdapter() {
    }

    public void setPokemon (DexItem pokemon) {
        this.pokemon = pokemon;
        this.evolutions = pokemon.getEvolutions();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dex_entry_quantity_item, parent, false);

            this.playerVM = new ViewModelProvider((ViewModelStoreOwner) parent.getContext()).get(PlayerVM.class);
            this.dexVM = new ViewModelProvider((ViewModelStoreOwner) parent.getContext()).get(DexVM.class);

            EvolutionsRecViewAdapter.ViewHolder holder = new EvolutionsRecViewAdapter.ViewHolder(v, viewType);
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();

            params.height = (int) (parent.getHeight() * 0.3);
            holder.itemView.setLayoutParams(params);
            return holder;
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.evolution_item, parent, false);
            EvolutionsRecViewAdapter.ViewHolder holder = new EvolutionsRecViewAdapter.ViewHolder(v, viewType);
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();

            params.height = (int) (parent.getHeight() * 0.5);
            holder.itemView.setLayoutParams(params);

            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.pQty.setText(String.format("Qty: %d", pokemon.getCount()));
            holder.price.setText(String.format("%d", pokemon.getPrice()));
        } else {
            DexItem i = this.dexVM.getDexItem(evolutions.get(position-1).get(0));
            if (i.hasCaught()) {
                holder.img.setImageResource(v.getResources().getIdentifier(String.format("p_%d", i.getNumber()), "drawable", v.getContext().getPackageName()));
            }
            holder.qty.setText(String.format("%d", evolutions.get(position-1).get(1)));
        }
    }

    @Override
    public int getItemCount() {
        return evolutions.size() +1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView qty;
        private Button evolve;

        public TextView pQty, price;
        public Button sell1, sell10;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if (viewType == 0) {
                pQty = v.findViewById(R.id.dexEntryQty);
                price = v.findViewById(R.id.dexEntryPrice);
                sell1 = v.findViewById(R.id.dexEntrySell1);
                sell10 = v.findViewById(R.id.dexEntrySell10);

                sell1.setOnClickListener(this::sell1);
                sell10.setOnClickListener(this::sell10);
            } else {
                img = itemView.findViewById(R.id.dexEntryEvolutionsItemImg);
                qty = itemView.findViewById(R.id.dexEntryEvolutionsItemQty);
                evolve = itemView.findViewById(R.id.dexEntryEvolutionsItemEvolve);

                evolve.setOnClickListener(this::evolve);
            }
        }

        private void sell1(View view) {
            if (pokemon.getCount() >= 1) {
                pokemon.sell(1);
                dexVM.setDexItem(pokemon);
                playerVM.gainMoney(pokemon.getPrice());
                pQty.setText(String.format("Qty: %d", pokemon.getCount()));
            } else {
                Toast.makeText(v.getContext(), "You don't have enough!", Toast.LENGTH_SHORT).show();
            }
        }

        private void sell10(View view) {
            if (pokemon.getCount() >= 10) {
                pokemon.sell(10);
                dexVM.setDexItem(pokemon);
                playerVM.gainMoney(pokemon.getPrice()*10L);
                pQty.setText(String.format("Qty: %d", pokemon.getCount()));
            } else {
                Toast.makeText(v.getContext(), "You don't have enough!", Toast.LENGTH_SHORT).show();
            }
        }

        private void evolve(View view) {
            ArrayList<Integer> evolution = evolutions.get(getAdapterPosition()-1);
            if (pokemon.getCount() >= evolution.get(1)) {
                DexItem evolved = dexVM.getDexItem(evolution.get(0));
                evolved.hatch();
                pokemon.evolve(evolution.get(1));
                dexVM.setDexItem(evolved);
                dexVM.setDexItem(pokemon);
                notifyItemChanged(0);
                notifyItemChanged(getAdapterPosition());
            } else {
                Toast.makeText(v.getContext(), String.format("You need %d more!", evolution.get(1) - pokemon.getCount()), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
