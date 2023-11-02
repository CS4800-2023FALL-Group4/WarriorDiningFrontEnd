package csustan.edu.cs4800.warriordiningapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class MainActivity extends RecyclerView.Adapter implements View.OnClickListener {
    GetMenu getMenu;

    @Override
    public void onAttachFragment(Context context) {
        super.onAttachFragment(context.this);
        getMenu = (GetMenu) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // bundle used as a method of creating a container to pass data from fragmentA to fragmentB
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // preferences button, not finished
        android.widget.ImageButton button = new ImageButton(R.id.prefButton);
        button.setOnClickListener(this);

        // get menu data, not finished, need backend access
            // going to use a fill in menu data set for now
        GetMenu[] menuSet = new GetMenu[]{getMenu};

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.prefButton;
            // switch fragment to preferences tab and/or preferences screen
                // fragment is like the activity_main ui, a new fragment is like a new page
                // a screen is like an overlay over activity_main, it pauses activity_main
            break;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // not finished
        // declaring a viewholder to bind to the recycler menu
        val viewHolder = LayoutInflater.from(viewGroup.context).inflate(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, viewGroup, false);
        return viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // not finished
        // changing recycler menu to menu data
        viewHolder.textView.text = menuSet[i];
    }

    @Override
    public int getItemCount() {
        return menuSet.size();
    }
}