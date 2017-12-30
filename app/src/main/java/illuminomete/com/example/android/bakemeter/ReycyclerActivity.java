package illuminomete.com.example.android.bakemeter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReycyclerActivity extends AppCompatActivity {
    ImageView  recyclerscale;
    CopyOfRecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    List<Integer> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data= new ArrayList<Integer>(Arrays.asList(R.drawable.loader_0,R.drawable.loader_1,R.drawable.loader_2,R.drawable.loader_3,
                R.drawable.loader_4,R.drawable.loader_5,R.drawable.loader_6,R.drawable.loader_7,R.drawable.loader_8,R.drawable.loader_9));
        setContentView(R.layout.activity_reycycler);
        recyclerView=(CopyOfRecyclerView) findViewById(R.id.recycler);
        recyclerscale=(ImageView) findViewById(R.id.recyclerscale);
        recyclerAdapter=new RecyclerAdapter(this,data);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapter.setOnItemClickLitener(new RecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(view.getContext(),position+"",Toast.LENGTH_SHORT).show();
                recyclerscale.setImageResource(data.get(position));
            }
        });
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setOnItemScrollChangeListener(new CopyOfRecyclerView.OnItemScrollChangeListener() {
            @Override
            public void onChange(View view, int position) {
                recyclerscale.setImageResource(data.get(position));
            }
        });

    }
}
