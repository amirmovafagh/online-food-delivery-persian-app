package ir.boojanco.onlinefoodorder.ui.fragments;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.UserProfileFragmentBinding;
import ir.boojanco.onlinefoodorder.viewmodels.UserProfileViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.UserProfileViewModelFactory;

public class UserProfileFragment extends Fragment {

    private UserProfileFragmentBinding binding;
    private UserProfileViewModel viewModel;
    @Inject
    UserProfileViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.user_profile_fragment,container, false);
        viewModel = new ViewModelProvider(this, factory).get(UserProfileViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
