package edu.wm.cs.cs445.sous_chef;

public interface ClickListener {
    void onDelete(int position);
    void onSave(int position);
    void onView(int position);
}
