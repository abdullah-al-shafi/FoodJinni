package shafi.sbf.com.foodjinni;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.List;

import shafi.sbf.com.foodjinni.pojo.ConfirmOrder;



public class ActiveOrderAdapter extends RecyclerView.Adapter<ActiveOrderAdapter.MyViewHolder>{


    private Context context;
    private List<ConfirmOrder> confirmOrders;

    private OnGetListener onGet;

    public ActiveOrderAdapter(Context context, List<ConfirmOrder> confirmOrders) {
        this.context = context;
        this.confirmOrders = confirmOrders;
        onGet = (OnGetListener) context;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView location, type;

        LinearLayout getDetailsLayout,TypeBack;


        public MyViewHolder(View view) {
            super(view);

            getDetailsLayout = itemView.findViewById(R.id.card);
            TypeBack = itemView.findViewById(R.id.type_bac);

            location = (TextView) view.findViewById(R.id.location_show);
            type = (TextView) view.findViewById(R.id.typeText);


        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.active_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final ConfirmOrder confirmOrder = confirmOrders.get(position);

        holder.location.setText(confirmOrder.getRestaurantDetails().getRestaurantName());
        holder.type.setText(confirmOrder.getStatus());

        if (confirmOrder.getStatus().equals("Pending")){
            holder.TypeBack.setBackgroundColor(Color.parseColor("#00ACC1"));
        }
        else {
            holder.TypeBack.setBackgroundColor(Color.parseColor("#43A047"));
        }

        holder.getDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onGet.onGet(confirmOrder,position+1);

            }
        });


    }

    @Override
    public int getItemCount() {
        return confirmOrders.size();
    }



    public interface OnGetListener{

        void onGet(ConfirmOrder confirmOrder, int position);
    }


}
