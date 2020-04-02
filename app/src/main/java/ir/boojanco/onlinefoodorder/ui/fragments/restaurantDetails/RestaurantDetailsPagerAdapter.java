package ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments.RestaurantCommentFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments.RestaurantFoodMenuFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments.RestaurantInfoFragment;

public class RestaurantDetailsPagerAdapter extends FragmentPagerAdapter {
    private static final int COMMENTS = 0;
    private static final int MENU = 1;
    private static final int INFO = 2;
    private static final int[] TABS = new int[]{COMMENTS, MENU, INFO};
    private Bundle fragmentBundle;

    public RestaurantDetailsPagerAdapter(@NonNull FragmentManager fm, int behavior, Bundle data) {
        super(fm, behavior);
        this.fragmentBundle = data;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (TABS[position]) {
            case COMMENTS:
                final RestaurantCommentFragment restaurantCommentFragment = new RestaurantCommentFragment();
                restaurantCommentFragment.setArguments(this.fragmentBundle);
                return restaurantCommentFragment;
            case MENU:
                final RestaurantFoodMenuFragment restaurantFoodMenuFragment = new RestaurantFoodMenuFragment();
                restaurantFoodMenuFragment.setArguments(this.fragmentBundle);
                return restaurantFoodMenuFragment;

            case INFO:
                final RestaurantInfoFragment restaurantInfoFragment = new RestaurantInfoFragment();
                restaurantInfoFragment.setArguments(this.fragmentBundle);
                return restaurantInfoFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return TABS.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (TABS[position]) {
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
