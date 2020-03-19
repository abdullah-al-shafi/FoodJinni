package shafi.sbf.com.foodjinni;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import shafi.sbf.com.foodjinni.pojo.ConfirmOrder;

public class OrderFoodDetails extends AppCompatActivity {

    TextView Name, Fon, Mail, Total, VAt, DelivaBoy, DeliveBy;

    LinearLayout First, Second, Third, Four;

    Button Change;

    String name, fon, email, group, dtinote, dtime, note, ldis, ltime, location;

    String includeVatPrice, totalPrice, Ordernum, ActivityF;


    String formattedDate, getCurrentTime;

    int onum;

    Spinner spinner;

    RecyclerView test;
    private TestAdapter2 adapterT;

    private int mHour, mMinute;

    String store, system;

    private ConfirmOrder confirmOrder;

    private List<OrderChart> orderCharts = new ArrayList<OrderChart>();
    List<String> driverList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_food_details);

        confirmOrder = (ConfirmOrder) getIntent().getSerializableExtra("valu");

        Name = findViewById(R.id.orderName);
        Fon = findViewById(R.id.orderFonNumber);
        Mail = findViewById(R.id.orderEmail);
        Total = findViewById(R.id.showPrice);
        VAt = findViewById(R.id.showPriceF);
        test = findViewById(R.id.foodRecyTest2);


        orderCharts = confirmOrder.getChartList();
        totalPrice = confirmOrder.getTottalPrice();
        includeVatPrice = confirmOrder.getIncludeVatTotalPrice();
        name = confirmOrder.getRestaurantDetails().getRestaurantName();
        fon = confirmOrder.getRestaurantDetails().getPhone();
        email = confirmOrder.getRestaurantDetails().getEmail();


//        OrderNumber.setText("#"+Ordernum);

        Name.setText(name);
        Fon.setText(fon);
        Mail.setText(email);
        Total.setText(new DecimalFormat("##.##").format(Double.parseDouble(totalPrice)));
        VAt.setText(new DecimalFormat("##.##").format(Double.parseDouble(includeVatPrice)));


        adapterT = new TestAdapter2(OrderFoodDetails.this, orderCharts);
        LinearLayoutManager llm = new LinearLayoutManager(OrderFoodDetails.this);
        llm.setOrientation(RecyclerView.VERTICAL);
        test.setLayoutManager(llm);
        test.setAdapter(adapterT);

    }
}
