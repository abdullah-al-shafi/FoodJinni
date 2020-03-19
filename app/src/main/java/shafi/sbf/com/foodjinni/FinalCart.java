package shafi.sbf.com.foodjinni;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import shafi.sbf.com.foodjinni.Adapter.RecylaerViewAdapter;
import shafi.sbf.com.foodjinni.pojo.CommonAll;
import shafi.sbf.com.foodjinni.pojo.ConfirmOrder;
import shafi.sbf.com.foodjinni.pojo.RestaurantDetails;
import shafi.sbf.com.foodjinni.register.User;

public class FinalCart extends AppCompatActivity implements TestAdapter.OrderListener {

    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference orderRef;

    RecyclerView test;
    private TestAdapter adapterT;

    private int mHour, mMinute;
    private  int m2,m3;

    TextView price,finalPrice,StoreName;


    CardView cardView;

    private ConfirmOrder confirmOrder;

    private List<OrderChart> orderCharts = new ArrayList<OrderChart>();

    RestaurantDetails restaurantDetails;
    User user;

    double totalPrice,vatpri;

    Dialog mydialog;

    String includeVatPrice,area;
    String userId;

    private FirebaseAuth auth;
    private FirebaseUser userauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_cart);


        confirmOrder =(ConfirmOrder) getIntent().getSerializableExtra("curt");

        area = getIntent().getStringExtra("area");
        
        

        mydialog = new Dialog(this);

        orderCharts = confirmOrder.getChartList();

        restaurantDetails = confirmOrder.getRestaurantDetails();

        user = confirmOrder.getUser();

        test = findViewById(R.id.foodRecyTest);
//        Time = findViewById(R.id.time);


        price = findViewById(R.id.showPrice);
        finalPrice = findViewById(R.id.showPriceF);
        StoreName = findViewById(R.id.store_name);

        StoreName.setText(confirmOrder.getRestaurantDetails().getRestaurantName());


        auth = FirebaseAuth.getInstance();
        userauth=auth.getCurrentUser();
        userId = userauth.getUid();

        rootRef = FirebaseDatabase.getInstance().getReference();

        userRef = rootRef.child("User").child(userId).child("OrderList");

        orderRef = rootRef.child("Restaurant").child(area).child(confirmOrder.getRestaurantDetails().getRestaurantID()).child("ActivOrder");

        cardView = findViewById(R.id.curt_master2F);


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(totalPrice>0) {
                    ConfirmPArchas();
                }else {
                    Toast.makeText(FinalCart.this, "Add food first...!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        

        adapterT = new TestAdapter(FinalCart.this, orderCharts);
        LinearLayoutManager llm = new LinearLayoutManager(FinalCart.this);
        llm.setOrientation(RecyclerView.VERTICAL);
        test.setLayoutManager(llm);
        test.setAdapter(adapterT);

        totalPrice = 0;
        for (OrderChart c: orderCharts){
            double price = Double.parseDouble(c.getProductPrice());
            totalPrice += price;
        }
        price.setText(new DecimalFormat("##.##").format(totalPrice));
        vatpri = totalPrice+(totalPrice*15.0)/100.0 ;
        includeVatPrice = String.valueOf((totalPrice+(totalPrice*15.0)/100.0));
        finalPrice.setText(new DecimalFormat("##.##").format(vatpri));


    }

    private void ConfirmPArchas() {

        final String id = orderRef.push().getKey();

        final ConfirmOrder confirmOrder= new ConfirmOrder(id,orderCharts, restaurantDetails, user, String.valueOf(totalPrice), includeVatPrice,"Pending", CommonAll.address);

        //Set value to database
        orderRef.child(id).child("details").setValue(confirmOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    userRef.child(id).child("details").setValue(confirmOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(FinalCart.this, "Order complete Successfully...!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FinalCart.this,FoodActivity.class);
                            intent.putExtra("valu",restaurantDetails);
                            startActivity(intent);
                            finish();

                        }
                    });

                } else {

                    Toast.makeText(FinalCart.this, "Failed to add Doctor", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FinalCart.this, "Failed : "+e, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onOrderDetails(OrderChart orderChart) {

        orderCharts.remove(orderChart);
        totalPrice = 0;
        for (OrderChart c: orderCharts){
            double price = Double.parseDouble(c.getProductPrice());
            totalPrice += price;
        }
        price.setText(new DecimalFormat("##.##").format(totalPrice));
        double vatpri = totalPrice+(totalPrice*15.0)/100.0 ;
        includeVatPrice = String.valueOf((totalPrice+(totalPrice*15.0)/100.0));
        finalPrice.setText(new DecimalFormat("##.##").format(vatpri));
    }

    @Override
    public void onPlus() {
        totalPrice = 0;
        for (OrderChart c: orderCharts){
            double price = Double.parseDouble(c.getProductPrice());
            totalPrice += price;
        }
        price.setText(new DecimalFormat("##.##").format(totalPrice));
        double vatpri = totalPrice+(totalPrice*15.0)/100.0 ;
        includeVatPrice = String.valueOf((totalPrice+(totalPrice*15.0)/100.0));
        finalPrice.setText(new DecimalFormat("##.##").format(vatpri));
    }

    @Override
    public void onMinus() {
        totalPrice = 0;
        for (OrderChart c: orderCharts){
            double price = Double.parseDouble(c.getProductPrice());
            totalPrice += price;
        }
        price.setText(new DecimalFormat("##.##").format(totalPrice));
        double vatpri = totalPrice+(totalPrice*15.0)/100.0 ;
        includeVatPrice = String.valueOf((totalPrice+(totalPrice*15.0)/100.0));
        finalPrice.setText(new DecimalFormat("##.##").format(vatpri));
    }

    @Override
    public void Note(final OrderChart orderChart) {
/*        //Dialog open
        mydialog.setContentView(R.layout.custom_addtocurt);
        TextView txtclose =(TextView) mydialog.findViewById(R.id.txtclose2);
        TextView txtname =(TextView) mydialog.findViewById(R.id.FoodETNAme);
        TextView txtnote2 =(TextView) mydialog.findViewById(R.id.add_note);
        TextView txtprice =(TextView) mydialog.findViewById(R.id.foodPrice);
        ImageView image =(ImageView) mydialog.findViewById(R.id.add_note0);
        txtnote =(EditText) mydialog.findViewById(R.id.etNote);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.dismiss();
            }
        });

        txtnote2.setText("ADD NOTE");
        txtname.setText(orderChart.getProductName());


        image.setVisibility(View.INVISIBLE);
        txtprice.setVisibility(View.INVISIBLE);

        CardView confirm = mydialog.findViewById(R.id.cart);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                orderChart.setNote(txtnote.getText().toString());

                mydialog.dismiss();
            }
        });

        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();*/
    }

    @Override
    public void onBackPressed() {
        if(totalPrice>0) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure want to back?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FinalCart.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
//            super.onBackPressed();

        }else {
            Intent intent = new Intent(FinalCart.this,FoodActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
