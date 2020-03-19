package shafi.sbf.com.foodjinni.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

import shafi.sbf.com.foodjinni.R;
import shafi.sbf.com.foodjinni.pojo.RestaurantDetails;

public class RecylaerViewAdapter extends RecyclerView.Adapter<RecylaerViewAdapter.MyViewHolder> {


    private Context context;
    private List<RestaurantDetails> resDetails;

    private ResDetailsListener listener;

    public RecylaerViewAdapter(Context context, List<RestaurantDetails> resDetails) {
        this.context = context;
        this.resDetails = resDetails;
        listener = (ResDetailsListener) context;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ResName,ResType;
        public ImageView ResImage;

        CardView getDetailsLayout;


        public MyViewHolder(View view) {
            super(view);

            getDetailsLayout = itemView.findViewById(R.id.getResDetails);

            ResName = (TextView) view.findViewById(R.id.resName);
            ResType = (TextView) view.findViewById(R.id.resType);
            ResImage = (ImageView) view.findViewById(R.id.resImage);

        }
    }


    @NonNull
    @Override
    public RecylaerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecylaerViewAdapter.MyViewHolder holder, int position) {

        final RestaurantDetails restaurantDetails = resDetails.get(position);


        holder.ResName.setText(restaurantDetails.getRestaurantName());
        holder.ResType.setText("# "+restaurantDetails.getRestaurantType());
        //holder.ResImage.setBackground(restaurantDetails.getImage());

        Picasso.get().load(restaurantDetails.getImage()).into(holder.ResImage);


        holder.getDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onResDetails(restaurantDetails
                );
            }
        });

    }

    @Override
    public int getItemCount() {
        return resDetails.size();
    }

    public interface ResDetailsListener {
        void onResDetails(RestaurantDetails resDetails);
    }
}
