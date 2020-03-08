package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import android.view.View;

import ir.boojanco.onlinefoodorder.models.food.FavoriteFoods;


public interface FavoriteFoodsRecyclerViewInterface {
    void onRecyclerViewFaveFoodsClick(View v, FavoriteFoods favoriteFoods);
}
