package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.FavoriteFoodsFragmentInterface;

public class FavoriteFoodsViewModel extends ViewModel {
    private Context context;
    private UserRepository userRepository;
    private FavoriteFoodsFragmentInterface fragmentInterface;

    public void setFragmentInterface(FavoriteFoodsFragmentInterface fragmentInterface) {
        this.fragmentInterface = fragmentInterface;
    }

    public FavoriteFoodsViewModel(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }
}
