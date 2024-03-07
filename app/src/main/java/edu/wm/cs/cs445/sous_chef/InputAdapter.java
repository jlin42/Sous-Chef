package edu.wm.cs.cs445.sous_chef;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InputAdapter extends RecyclerView.Adapter<InputAdapter.ViewHolder> {

    private ArrayList<String> filtersList;
    private Context curContext;

    public InputAdapter(ArrayList<String> filtersList, Context curContext) {
        this.filtersList = filtersList;
        this.curContext = curContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_input, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String filter = filtersList.get(position);
        holder.bind(filter);
        holder.trashBtn.setOnClickListener((View.OnClickListener) v -> {
            removeAt(position);//i is your adapter position
        });
    }

    @Override
    public int getItemCount() {
        return filtersList.size();
    }

    public void removeAt(int position) {
        filtersList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, filtersList.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView filterView;
        ImageView trashBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filterView = itemView.findViewById(R.id.ingredient_input);
            trashBtn = itemView.findViewById(R.id.trash_recipe_btn);
//            trashBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    itemView.
//                }
//            });
        }

        public void bind(String filter) {
            filterView.setText(filter);
        }
    }
}
