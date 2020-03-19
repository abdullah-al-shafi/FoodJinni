package shafi.sbf.com.foodjinni;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import shafi.sbf.com.foodjinni.pojo.ConfirmOrder;
import shafi.sbf.com.foodjinni.pojo.RestaurantDetails;
import shafi.sbf.com.foodjinni.register.User;

public class FoodActivity extends AppCompatActivity implements MenuCatagoriAdapter.CatagoriListAdapterListener ,FoodRecylaerViewAdapter.FoodListiner{

    RecyclerView Category,FoodRecycler, test;

    private List<AddFoodDetails> addFoods = new ArrayList<AddFoodDetails>();
    private List<OrderChart> orderCharts = new ArrayList<OrderChart>();
    private List<CatagoriPojo> catagoriPojos = new ArrayList<>();

    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference areaRef;
    private DatabaseReference catagRef;

    Dialog mydialog;

    private boolean checkNothing = false;

    TextView Item;
    String catagory;

    FrameLayout frameLayout;

    private double totalPrice;

    int it = 0;

    EditText txtnote;

    private TestAdapter adapterT;

    TextView Name,Type;
    ImageView Image;

    String area;

    private RestaurantDetails restaurantDetails;
    private User user;

    private FirebaseAuth auth;
    private FirebaseUser userauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        restaurantDetails = (RestaurantDetails) getIntent().getSerializableExtra("valu");
        Name = findViewById(R.id.resturentName);
        Type = findViewById(R.id.resturentType);
        Image = findViewById(R.id.resturentImage);

        mydialog = new Dialog(this);

        rootRef = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        userauth=auth.getCurrentUser();
        String userId=userauth.getUid();



        FirebaseDatabase.getInstance().getReference("User").child(userId).child("details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                user = dataSnapshot.getValue(User.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Name.setText(restaurantDetails.getRestaurantName());
        Type.setText(restaurantDetails.getRestaurantType());
        Picasso.get().load(restaurantDetails.getImage()).into(Image);

        Item = findViewById(R.id.item);
        Category = findViewById(R.id.menuName);
        FoodRecycler = findViewById(R.id.foodRecy);

        frameLayout = findViewById(R.id.fram);

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onDatabase();
            }
        });

        findArea();

    }


    private void findArea() {

        areaRef = rootRef.child("Restaurant").child("Area").child(restaurantDetails.getRestaurantID());

        areaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                AreaPojo h = dataSnapshot.getValue(AreaPojo.class);

                area= h.getArea();

                Catago();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void Catago() {
        catagRef = rootRef.child("Restaurant").child(area).child(restaurantDetails.getRestaurantID()).child("catagori");
        catagRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                catagoriPojos.clear();

//                Toast.makeText(FoodActivity.this, ""+dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
//                historyTV.setText(String.valueOf(dataSnapshot.getValue()));
                for (DataSnapshot hd: dataSnapshot.getChildren()){
                    CatagoriPojo doc = hd.getValue(CatagoriPojo.class);
                    catagoriPojos.add(doc);
                }

                MenuCatagoriAdapter recylaerViewAdapter = new MenuCatagoriAdapter(FoodActivity.this, catagoriPojos);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                Category.setLayoutManager(mLayoutManager);
                Category.setItemAnimator(new DefaultItemAnimator());
                Category.setAdapter(recylaerViewAdapter);

                showFirstList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showFirstList() {

        userRef = rootRef.child("Restaurant").child(area).child(restaurantDetails.getRestaurantID()).child("Food List").child(catagoriPojos.get(0).getCatagori());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addFoods.clear();
                Log.e("PATH_SHOW","Show: "+dataSnapshot.getValue());
                //historyTV.setText(String.valueOf(dataSnapshot.getValue()));
                for (DataSnapshot hd: dataSnapshot.getChildren()){
                    AddFoodDetails doc = hd.child("details").getValue(AddFoodDetails.class);
                    addFoods.add(doc);
                }

                FoodRecylaerViewAdapter  recylaerViewAdapter = new FoodRecylaerViewAdapter(FoodActivity.this,addFoods);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                FoodRecycler.setLayoutManager(mLayoutManager);
                FoodRecycler.setItemAnimator(new DefaultItemAnimator());
                FoodRecycler.setAdapter(recylaerViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onFooodCurt(AddFoodDetails addFood) {
        addToChart(addFood);
    }


    private void addToChart(final AddFoodDetails addFood) {

        //Dialog open
        mydialog.setContentView(R.layout.custom_addtocurt);
        CardView txtcncNote=(CardView) mydialog.findViewById(R.id.txtclose2);
        TextView txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
        TextView txtname =(TextView) mydialog.findViewById(R.id.FoodETNAme);
        TextView txtsub =(TextView) mydialog.findViewById(R.id.FoodETSub);
        TextView txtprice =(TextView) mydialog.findViewById(R.id.foodPrice);
        txtnote =(EditText) mydialog.findViewById(R.id.etNote);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.dismiss();
            }
        });

        txtcncNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.dismiss();
            }
        });

        txtname.setText(addFood.getFoodName());
        txtsub.setText(addFood.getFoodDescription());
        txtprice.setText(addFood.getFoodPrice());



        CardView confirm = mydialog.findViewById(R.id.cart);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(addFood);
                frameLayout.setVisibility(View.VISIBLE);
                mydialog.dismiss();
            }
        });

        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();



    }

    private void openDialog(AddFoodDetails addFood) {
        String Note = txtnote.getText().toString();

        if (Note.isEmpty()){
            Note = "  ";
        }


        checkNothing = false;
        it=it+1;
        Item.setText(String.valueOf(it));
        for (OrderChart c: orderCharts){
            if (c.getProductName().equals(addFood.getFoodName())){
                int i = Integer.parseInt(c.getProductQuantity());
                i++;
                orderCharts.remove(c);
                c.setProductQuantity(String.valueOf(i));
                double price = Double.parseDouble(addFood.getFoodPrice());
                c.setProductPrice(String.valueOf(price*i));
                c.setNote(Note);
                orderCharts.add(c);
                checkNothing =  true;
                break;
            }else {
                checkNothing = false;
            }
        }
        if (!checkNothing){

            OrderChart chart = new OrderChart(addFood.getFoodName(),addFood.getFoodDescription(), String.valueOf(1), addFood.getFoodPrice(), Note);
            orderCharts.add(chart);
        }
    }

    public void onDatabase(){
        String id = "PUSH ID";
        totalPrice = 0;
        for (OrderChart c: orderCharts){
            double price = Double.parseDouble(c.getProductPrice());
            totalPrice += price;
        }
        String includeVatPrice = String.valueOf((totalPrice+(totalPrice*15.0)/100.0));
        ConfirmOrder cd = new ConfirmOrder(id, orderCharts,restaurantDetails,user, String.valueOf(totalPrice),includeVatPrice,"null");
        Intent intent = new Intent(new Intent(FoodActivity.this,FinalCart.class));
        intent.putExtra("curt",cd);
        intent.putExtra("area",area);
        startActivity(intent);
        finish();

    }

    @Override
    public void onCompleteBooking(CatagoriPojo hb) {

        catagory = hb.getCatagori();

        userRef = rootRef.child("Restaurant").child(area).child(restaurantDetails.getRestaurantID()).child("Food List").child(catagory);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addFoods.clear();
                //historyTV.setText(String.valueOf(dataSnapshot.getValue()));
                for (DataSnapshot hd: dataSnapshot.getChildren()){
                    AddFoodDetails doc = hd.child("details").getValue(AddFoodDetails.class);
                    addFoods.add(doc);
                }

                FoodRecylaerViewAdapter  recylaerViewAdapter = new FoodRecylaerViewAdapter(FoodActivity.this,addFoods);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                FoodRecycler.setLayoutManager(mLayoutManager);
                FoodRecycler.setItemAnimator(new DefaultItemAnimator());
                FoodRecycler.setAdapter(recylaerViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
