package shafi.sbf.com.foodjinni;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import shafi.sbf.com.foodjinni.pojo.ConfirmOrder;

public class MyOrderList extends NaviBase implements ActiveOrderAdapter.OnGetListener{

    private List<ConfirmOrder> orderCharts = new ArrayList<ConfirmOrder>();

    private DatabaseReference rootRef;
    private DatabaseReference orderRef;

    private FirebaseAuth auth;
    private FirebaseUser userauth;

    RecyclerView Category,FoodRecycler, test;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_list);

        auth = FirebaseAuth.getInstance();
        userauth=auth.getCurrentUser();
        userId=userauth.getUid();

        mtollbarText=(TextView) findViewById(R.id.toolbar_text_base);
        mtollbarText.setText("My Order");
        mtollbarText.setCompoundDrawablesWithIntrinsicBounds( R.drawable.food_jinni_24, 0, 0, 0);

        rootRef = FirebaseDatabase.getInstance().getReference();

        FoodRecycler = findViewById(R.id.foodRecy);

        orderRef = rootRef.child("User").child(userId).child("OrderList");

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                orderCharts.clear();

                for (DataSnapshot donnerSnapshot : dataSnapshot.getChildren()) {
                    ConfirmOrder confirmOrder = donnerSnapshot.child("details").getValue(ConfirmOrder.class);
                    orderCharts.add(confirmOrder);
                }
                ActiveOrderAdapter recylaerViewAdapter = new ActiveOrderAdapter(MyOrderList.this,orderCharts);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MyOrderList.this);
                FoodRecycler.setLayoutManager(mLayoutManager);
                FoodRecycler.setItemAnimator(new DefaultItemAnimator());
                FoodRecycler.setAdapter(recylaerViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    @Override
    public void onGet(ConfirmOrder confirmOrder, int position) {
        Intent intent = new Intent(MyOrderList.this,OrderFoodDetails.class);
        intent.putExtra("valu",confirmOrder);
        startActivity(intent);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.nav_myorder: return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.nav_home)
        {
            startActivity(new Intent(MyOrderList.this,MainActivity.class));
            finish();

        }

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyOrderList.this,MainActivity.class));
        finish();
    }
}
