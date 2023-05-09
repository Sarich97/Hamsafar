package hamsafar.tj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import hamsafar.tj.R;
import jp.shts.android.storiesprogressview.StoriesProgressView;

public class SecurityStories extends AppCompatActivity {

    StoriesProgressView storiesProgressView;
    ImageView imageViewStories;

    int counter = 0;
    int [] imageStoriesResource = new int[] {
            R.drawable.iamge1,
            R.drawable.car,
            R.drawable.iamge1
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_stories);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageViewStories = findViewById(R.id.imageViewStories);

        storiesProgressView = findViewById(R.id.imageStoriesProgress);
        storiesProgressView.setStoriesCount(3);
        storiesProgressView.setStoryDuration(8000L);

        storiesProgressView.setStoriesListener(new StoriesProgressView.StoriesListener() {
            @Override
            public void onNext() {
                imageViewStories.setImageResource(imageStoriesResource[++counter]);
            }

            @Override
            public void onPrev() {
                if ((counter - 1) < 0) return;
                imageViewStories.setImageResource(imageStoriesResource[--counter]);
            }

            @Override
            public void onComplete() {
                onBackPressed();
                finish();
            }
        });
        storiesProgressView.startStories();

        imageViewStories.setImageResource(imageStoriesResource[counter]);
        imageViewStories.setOnClickListener(v -> {
            storiesProgressView.skip();

        });
    }

    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
    }
}