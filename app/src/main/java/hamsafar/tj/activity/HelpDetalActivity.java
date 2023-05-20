package hamsafar.tj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import hamsafar.tj.R;

public class HelpDetalActivity extends AppCompatActivity {

    private TextView titleViewDetal, DescpViewDetal;
    private String tiitlText, descpText;
    private ImageView imageViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_detal);

        titleViewDetal = findViewById(R.id.titleDirDetal);
        DescpViewDetal = findViewById(R.id.textDescpDir);
        imageViewBack = findViewById(R.id.imageViewBackButton);

        imageViewBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        tiitlText = getIntent().getStringExtra("dirTitle");
        descpText = getIntent().getStringExtra("dirDesc");


        titleViewDetal.setText(tiitlText);
        DescpViewDetal.setText(descpText);
    }
}