package hamsafar.tj.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        recyclerViewDirect.setHasFixedSize(true);
        recyclerViewDirect.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        directoryAdapter = new DirectoryAdapter(this, directoryModelList);
        recyclerViewDirect.setAdapter(directoryAdapter);
        initData();
    }

    private void initData() {
        directoryModelList.add(new DirectoryModel("Приложение Hamsafar",getString(R.string.about_us)));
        directoryModelList.add(new DirectoryModel("Как создать поездку?",getString(R.string.how_creatr_tripD)));
        directoryModelList.add(new DirectoryModel("Как создать заявку?",getString(R.string.how_creatr_tripF)));
        directoryModelList.add(new DirectoryModel("Зачем нужен рейтинг?",getString(R.string.reating_descrip)));


    }
}