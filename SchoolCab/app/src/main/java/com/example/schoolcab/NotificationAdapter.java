package com.example.schoolcab;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList<NotificationModel> NotificationList;

    // Constructor
    public NotificationAdapter(Context context, ArrayList<NotificationModel> NotificationList) {
        this.context = context;
        this.NotificationList = NotificationList;
    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        // to set data to textview and imageview of each card layout
        NotificationModel model = NotificationList.get(position);
        holder.titleTV.setText(model.getTitle());
        holder.messageTV.setText("" + model.getMessage());
        holder.timeTV.setText(""+model.getTime());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return NotificationList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTV;
        private final TextView messageTV;
        private final TextView timeTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTV = itemView.findViewById(R.id.timeTextView);
            titleTV = itemView.findViewById(R.id.titleTextView);
            messageTV = itemView.findViewById(R.id.messageTextView);
        }
    }
}