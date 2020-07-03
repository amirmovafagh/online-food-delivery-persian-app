package ir.boojanco.onlinefoodorder.ui.fragments.logo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.airbnb.lottie.LottieAnimationView;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.FragmentMazeehLogoBinding;
import ir.boojanco.onlinefoodorder.ui.activities.MainActivity;
import ir.boojanco.onlinefoodorder.viewmodels.MazeehLogoViewModel;

public class MazeehLogoFragment extends Fragment {

    private FragmentMazeehLogoBinding binding;
    private MazeehLogoViewModel viewModel;

    @Inject
    MySharedPreferences sharedPreferences;

    private LottieAnimationView animationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mazeeh_logo, container, false);
        viewModel = new ViewModelProvider(this).get(MazeehLogoViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        //Back pressed Logic for fragment
        View v = binding.getRoot();
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener((v1, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
            }
            return false;
        });

        animationView = binding.animationMazeehLogo;

        ((App) getActivity().getApplication()).getComponent().inject(this);

        animationView.addAnimatorUpdateListener(animation -> {

            float progress = ((float) animation.getAnimatedValue() * 100);
            if ((int) progress == 85) {
                if (sharedPreferences.getUserAuthTokenKey() != null) {

                    /*getActivity().finish();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
                    if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.mazeehLogoFragment) {
                        getActivity().finish();
                        Navigation.findNavController(getView()).navigate(R.id.action_mazeehLogoFragment_to_mainActivity);

                    }
                } else if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.mazeehLogoFragment) {
                    Navigation.findNavController(getView()).navigate(R.id.action_mazeehLogoFragment_to_loginFragment);
                }
            }

        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}