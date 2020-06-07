package ir.boojanco.onlinefoodorder.ui.fragments.logo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.airbnb.lottie.LottieAnimationView;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.FragmentMazeehLogoBinding;
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

        animationView = binding.animationMazeehLogo;

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((App) getActivity().getApplication()).getComponent().inject(this);

        animationView.addAnimatorUpdateListener(animation -> {

            float progress = ((float) animation.getAnimatedValue() * 100);
            if ((int) progress == 99) {

                //Navigation.findNavController(getView()).navigate(R.id.action_mazeehLogoFragment_to_loginRegisterFragment);
                //Navigation.findNavController(getActivity(), R.id.enter_nav_host_fragment).navigate(R.id.action_mazeehLogoFragment_to_loginRegisterFragment);
                if (sharedPreferences.getUserAuthTokenKey() != null) {
                    if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.mazeehLogoFragment) {
                        Navigation.findNavController(getView()).navigate(R.id.action_mazeehLogoFragment_to_mainActivity);
                        getActivity().finish();
                    }
                } else if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.mazeehLogoFragment) {
                    Navigation.findNavController(getView()).navigate(R.id.action_mazeehLogoFragment_to_loginRegisterFragment);
                }
            }

        });
    }
}