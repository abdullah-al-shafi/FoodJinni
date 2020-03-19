package shafi.sbf.com.foodjinni;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.MyViewHolder> {


    private Context context;
    private List<OrderChart> addFoods;
    private OrderListener listener;


    public TestAdapter(Context context, List<OrderChart> addFoods) {
        this.context = context;
        this.addFoods = addFoods;
        listener = (OrderListener) context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView FoodName, Foodquen,FoodPrice,plus,minus,noteShow;

        ImageView Delet,edit;
        LinearLayout getDetailsLayout;


        public MyViewHolder(View view) {
            super(view);

            getDetailsLayout = itemView.findViewById(R.id.getFoodDetails222);

            FoodName = (TextView) view.findViewById(R.id.food_name);
            Foodquen = (TextView) view.findViewById(R.id.food_qu);
            FoodPrice = (TextView) view.findViewById(R.id.food_priceS);
            noteShow = (TextView) view.findViewById(R.id.note_Show);
            plus = (TextView) view.findViewById(R.id.food_plu);
            minus = (TextView) view.findViewById(R.id.food_mi);
            Delet = (ImageView) view.findViewById(R.id.delete);
            edit = (ImageView) view.findViewById(R.id.food_edit);



        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final OrderChart addFood = addFoods.get(position);

        final Dialog mydialog = new Dialog(context);

        holder.FoodName.setText(addFood.getProductName());
        holder.Foodquen.setText(addFood.getProductQuantity());
        holder.FoodPrice.setText(addFood.getProductPrice());

        holder.noteShow.setText(addFood.getNote());


        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int quen = Integer.parseInt(addFood.getProductQuantity());
                double pri = Double.parseDouble(addFood.getProductPrice());
                double basePri = pri/quen;
                ++quen;
                holder.Foodquen.setText(String.valueOf(quen));
                addFood.setProductQuantity(String.valueOf(quen));
                addFood.setProductPrice(String.valueOf(basePri*quen));
                holder.FoodPrice.setText(String.valueOf(basePri*quen));

                listener.onPlus();

            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int quen = Integer.parseInt(addFood.getProductQuantity());
                if (quen>1){
                double pri = Double.parseDouble(addFood.getProductPrice());
                double basePri = pri/quen;
                --quen;
                holder.Foodquen.setText(String.valueOf(quen));
                addFood.setProductQuantity(String.valueOf(quen));
                addFood.setProductPrice(String.valueOf(basePri*quen));
                holder.FoodPrice.setText(String.valueOf(basePri*quen));

                    listener.onMinus();
            }
            }
        });
        holder.Delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onOrderDetails(addFood);
                notifyDataSetChanged();

            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

/*                listener.Note(addFood);
                notifyDataSetChanged();
                holder.noteShow.setText(addFood.getNote());*/


                //Dialog open
                mydialog.setContentView(R.layout.custom_addtocurt);
                CardView txtclose =(CardView) mydialog.findViewById(R.id.txtclose2);
                TextView txtname =(TextView) mydialog.findViewById(R.id.FoodETNAme);
                TextView txtnote2 =(TextView) mydialog.findViewById(R.id.add_note);
                TextView txtprice =(TextView) mydialog.findViewById(R.id.foodPrice);
                ImageView image =(ImageView) mydialog.findViewById(R.id.add_note0);
                final EditText txtnote =(EditText) mydialog.findViewById(R.id.etNote);
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mydialog.dismiss();
                    }
                });

                txtnote2.setText("ADD NOTE");
                txtname.setText(addFood.getProductName());


                image.setVisibility(View.INVISIBLE);
                txtprice.setVisibility(View.INVISIBLE);

                CardView confirm = mydialog.findViewById(R.id.cart);

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        addFood.setNote(txtnote.getText().toString());
                        holder.noteShow.setText(addFood.getNote());
                        mydialog.dismiss();
                    }
                });

                mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mydialog.show();
            }
        });




    }

    @Override
    public int getItemCount() {
        return addFoods.size();
    }

    public interface OrderListener{
        void onOrderDetails(OrderChart orderChart);
        void onPlus();
        void onMinus();
        void Note(OrderChart orderChart);
    }

}
