package ir.boojanco.onlinefoodorder.ui.fragments.loginRegister;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.FragmentLoginRegisterBinding;
import ir.boojanco.onlinefoodorder.viewmodels.LoginRegisterViewModel;

public class LoginRegisterFragment extends Fragment {

    private FragmentLoginRegisterBinding binding;
    private LoginRegisterViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_register, container, false);
        viewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getContext(), "AMIR", Toast.LENGTH_SHORT).show();
//        Navigation.findNavController(getActivity(), R.id.enter_nav_host_fragment).navigate(R.id.action_login_register_fragment_to_main_activity);
    }
}