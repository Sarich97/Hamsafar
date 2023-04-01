package hamsafar.tj.activity.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import hamsafar.tj.activity.fragments.MyPostFragment;
import hamsafar.tj.activity.fragments.MyTripFragment;

public class ProfileFragmentAdapter extends FragmentStateAdapter {
    public ProfileFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1){
            return new MyTripFragment ();
        }
        return new MyPostFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
