package hamsafar.tj.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.adapters.DirectoryAdapter;
import hamsafar.tj.activity.models.DirectoryModel;

public class HelpActivity extends AppCompatActivity {


    private static final String TAG = "FRAGMENTDIRECT";
    private RecyclerView recyclerViewDirect;
    ArrayList<DirectoryModel> directoryModelList = new ArrayList<>();
    DirectoryAdapter directoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


        recyclerViewDirect = findViewById(R.id.recyclerViewDirect);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerViewDirect.setLayoutManager(gridLayoutManager);
        directoryAdapter = new DirectoryAdapter(this, directoryModelList);
        recyclerViewDirect.setAdapter(directoryAdapter);
        helpinitData();
    }

    private void helpinitData() {
        GradientDrawable gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xFF62A9D3, 0xFF9091FB});
        GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xFF1DA189, 0xFF048D93});
        GradientDrawable gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff9361D4, 0xFF9563D6});
        GradientDrawable gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffDCA0DC, 0xffF1A6A0});
        GradientDrawable gradient5 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffFC9955, 0xffFF978E});
        GradientDrawable gradient6 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffD8B55B, 0xffD9B926});
        GradientDrawable gradient7 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff56B58B, 0xff5AAFC3});
        GradientDrawable gradient8 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff56D3F3, 0xff5AAFC3});

        directoryModelList.add(new DirectoryModel("О проекте",getString(R.string.about_us), gradient1));
        directoryModelList.add(new DirectoryModel("Как создать поездку?",getString(R.string.how_creatr_tripD), gradient2));
        directoryModelList.add(new DirectoryModel("Как создать заявку?",getString(R.string.how_creatr_tripF), gradient3));
        directoryModelList.add(new DirectoryModel("Зачем нужен рейтинг?",getString(R.string.reating_descrip), gradient4));
        directoryModelList.add(new DirectoryModel("Счетчик поездок",getString(R.string.trip_count_descrip), gradient5));
        directoryModelList.add(new DirectoryModel("Почему только по 3 поездок?",getString(R.string.trip_count_descrip3), gradient6));
        directoryModelList.add(new DirectoryModel("Исключить пассажира из заявки",getString(R.string.how_delete), gradient7));
        directoryModelList.add(new DirectoryModel("Как повышается рейтинг ?",getString(R.string.how_to_set_rating), gradient8));


    }
}