package shafi.sbf.com.foodjinni;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.List;



public class FoodRecylaerViewAdapter extends RecyclerView.Adapter<FoodRecylaerViewAdapter.MyViewHolder> {


    private Context context;
    private List<AddFoodDetails> addFoods;

    private FoodListiner listener;

    public FoodRecylaerViewAdapter(Context context, List<AddFoodDetails> addFoods) {
        this.context = context;
        this.addFoods = addFoods;
        listener = (FoodListiner) context;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView FoodName, FoodDis,FoodPrice,FoodNumber;

        CardView getDetailsLayout;


        public MyViewHolder(View view) {
            super(view);

            getDetailsLayout = itemView.findViewById(R.id.curt_master2);

            FoodName = (TextView) view.findViewById(R.id.food_name2);
            FoodDis = (TextView) view.findViewById(R.id.food_dis2);
            FoodPrice = (TextView) view.findViewById(R.id.food_price2);
            FoodNumber = (TextView) view.findViewById(R.id.food_number);

        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_item2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final AddFoodDetails addFood = addFoods.get(position);

        holder.FoodName.setText(addFood.getFoodName());
        holder.FoodDis.setText(addFood.getFoodDescription());
        holder.FoodPrice.setText(addFood.getFoodPrice());
        holder.FoodNumber.setText(addFood.getFoodNumber());

        holder.getDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFooodCurt(addFood);
            }
        });

    }

    @Override
    public int getItemCount() {
        return addFoods.size();
    }



    public interface FoodListiner{
        void onFooodCurt(AddFoodDetails addFood);
    }
}
