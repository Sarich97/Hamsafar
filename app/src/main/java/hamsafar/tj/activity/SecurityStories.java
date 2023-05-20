package hamsafar.tj.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;


import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.adapters.DirectoryAdapter;
import hamsafar.tj.activity.models.DirectoryModel;


public class SecurityStories extends AppCompatActivity {

    private static final String TAG = "FRAGMENTDIRECT";
    private RecyclerView recyclerViewDirect;
    ArrayList<DirectoryModel> directoryModelList = new ArrayList<>();
    DirectoryAdapter directoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_stories);


        recyclerViewDirect = findViewById(R.id.recyclerViewSecurity);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerViewDirect.setLayoutManager(gridLayoutManager);
        directoryAdapter = new DirectoryAdapter(this, directoryModelList);
        recyclerViewDirect.setAdapter(directoryAdapter);
        securityinitData();

    }

    private void securityinitData() {
        GradientDrawable gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xFF62A9D3, 0xFF9091FB});
        GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xFF1DA189, 0xFF048D93});
        GradientDrawable gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff9462D5, 0xFFB591E3});
        GradientDrawable gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffDCA0DC, 0xffF1A6A0});
        GradientDrawable gradient5 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffFC9955, 0xffFF978E});
        GradientDrawable gradient6 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffD8B55B, 0xffD9B926});
        GradientDrawable gradient7 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff56B58B, 0xff5AAFC3});
        GradientDrawable gradient8 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff56D3F3, 0xff5AAFC3});

        directoryModelList.add(new DirectoryModel("ПДД",getString(R.string.traffic_rules), gradient3));
        directoryModelList.add(new DirectoryModel("Скоростной режим",getString(R.string.traffic_rules1), gradient6));
        directoryModelList.add(new DirectoryModel("Ремень безопасности",getString(R.string.traffic_rules2), gradient7));
        directoryModelList.add(new DirectoryModel("ТО автомобиля",getString(R.string.traffic_rules3), gradient5));
        directoryModelList.add(new DirectoryModel("Телефон и руль: опасное сочетание",getString(R.string.traffic_rules4), gradient2));
        directoryModelList.add(new DirectoryModel("Защита маленьких пассажиров",getString(R.string.traffic_rules5), gradient4));
    }
}