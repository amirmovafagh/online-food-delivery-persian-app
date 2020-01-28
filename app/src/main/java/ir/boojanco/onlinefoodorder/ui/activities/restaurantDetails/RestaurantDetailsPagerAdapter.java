package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments.RestaurantCommentFragment;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments.RestaurantFoodMenuFragment;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments.RestaurantInfoFragment;

public class RestaurantDetailsPagerAdapter extends FragmentPagerAdapter {
    private static final int COMMENTS = 0;
    private static final int MENU = 1;
    private static final int INFO = 2;
    private static final int[] TABS = new int[]{COMMENTS, MENU, INFO};

    private Context context;
    public RestaurantDetailsPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (TABS[position])
        {
            case COMMENTS:
                return RestaurantCommentFragment.newInstance();
            case MENU:
                return RestaurantFoodMenuFragment.newInstance();
            case INFO:
                return RestaurantInfoFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TABS.length;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (TABS[position])
        {
            case COMMENTS:
                return "نظرات";
            case MENU:
                return "منو";
            case INFO:
                return "اطلاعات";
        }
        return null;
    }

}
