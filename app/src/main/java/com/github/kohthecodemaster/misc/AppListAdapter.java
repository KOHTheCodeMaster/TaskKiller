package com.github.kohthecodemaster.misc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.kohthecodemaster.R;
import com.github.kohthecodemaster.pojo.TaskRowPojo;
import com.github.kohthecodemaster.pojo.AppSettingsPojo;
import com.github.kohthecodemaster.service.TaskKillerAccessibilityService;

import java.util.function.Consumer;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {

    private static final String TAG = "L0G-AppListAdapter";
    private final TaskRowPojo[] arrTaskRowPojo;
    private static int selectedItemsCount;
    private final Consumer<String> consumerUpdateAppsSelectedTV;

    public AppListAdapter(TaskRowPojo[] arrTaskRowPojo, Consumer<String> consumerUpdateAppsSelectedTV) {
        this.arrTaskRowPojo = arrTaskRowPojo;
        this.consumerUpdateAppsSelectedTV = consumerUpdateAppsSelectedTV;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TaskRowPojo currentTaskRowPojo = arrTaskRowPojo[position];
        holder.textView.setText(currentTaskRowPojo.getTitle());
        holder.imageView.setImageDrawable(currentTaskRowPojo.getImgIcon());
        holder.checkBox.setOnClickListener(view -> {
//            Log.v(TAG, "onBindViewHolder: Checkbox Clicked - " + currentTaskRowPojo.getTitle());

            boolean taskRemoved = TaskKillerAccessibilityService.tasksToBeKilled
                    .removeIf(appSettingsPojo -> appSettingsPojo.getPackageName().equals(currentTaskRowPojo.getPackageName()));

            if (taskRemoved) selectedItemsCount--;
            else {
                TaskKillerAccessibilityService.tasksToBeKilled.add(new AppSettingsPojo(currentTaskRowPojo.getPackageName()));
                selectedItemsCount++;
            }
            String strText = "Selected " + selectedItemsCount + " / " + arrTaskRowPojo.length;
            consumerUpdateAppsSelectedTV.accept(strText);
        });
    }

    @Override
    public int getItemCount() {
        return arrTaskRowPojo.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.idLogoImageView);
            this.textView = itemView.findViewById(R.id.idAppTitleTextView);
            this.checkBox = itemView.findViewById(R.id.idCheckBox);
        }
    }
}