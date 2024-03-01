package edu.wm.cs.cs445.sous_chef;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InputAdapter extends RecyclerView.Adapter<InputAdapter.ViewHolder> {

    private ArrayList<String> filtersList;

    public InputAdapter(ArrayList<String> filtersList) {
        this.filtersList = filtersList;
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
    }

    @Override
    public int getItemCount() {
        return filtersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView filterView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filterView = itemView.findViewById(R.id.ingredient_input);
        }

        public void bind(String filter) {
            filterView.setText(filter);
        }
    }
}
