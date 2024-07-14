package com.example.fetchapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fetchapplication.R;
import com.example.fetchapplication.model.MobileDetails;
import java.util.List;

public class MobileDetailsAdapter extends RecyclerView.Adapter<MobileDetailsAdapter.MobileDetailsHolder> {

    private List<MobileDetails> mobileDetailsList;
    private Context context;

    public MobileDetailsAdapter(List<MobileDetails> mobileDetailsList, Context context) {
        this.mobileDetailsList = mobileDetailsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MobileDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mobile_ui, parent, false);
        return new MobileDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MobileDetailsHolder holder, int position) {
        MobileDetails mobileDetails = mobileDetailsList.get(position);
        // If the value of name is equal to null or "" then do not display
        if (mobileDetails.getName() == null || mobileDetails.getName().isEmpty() || mobileDetails.getName().equals("null")) {
            holder.mobileName.setVisibility(View.GONE);
            holder.mobileName.setText("");
        } else {
            holder.mobileName.setVisibility(View.VISIBLE);
            holder.mobileName.setText("Name: " + mobileDetails.getName());
        }

        holder.mobileId.setText("ID: " + String.valueOf(mobileDetails.getId()));
        holder.mobileListId.setText("List ID: " + String.valueOf(mobileDetails.getListId()));
    }

    @Override
    public int getItemCount() {
        return mobileDetailsList.size();
    }

    public class MobileDetailsHolder extends RecyclerView.ViewHolder {

        private TextView mobileId, mobileListId, mobileName;

        public MobileDetailsHolder(@NonNull View itemView) {
            super(itemView);
            mobileId = itemView.findViewById(R.id.id);
            mobileListId = itemView.findViewById(R.id.listId);
            mobileName = itemView.findViewById(R.id.name);
        }
    }
}
