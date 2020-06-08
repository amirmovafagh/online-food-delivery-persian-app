package ir.boojanco.onlinefoodorder.ui.fragments.loginRegister;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.VerificationFragmentBinding;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.ui.activities.MainActivity;
import ir.boojanco.onlinefoodorder.viewmodels.VerificationViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.VerificationViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.VerificationInterface;

public class VerificationFragment extends Fragment implements VerificationInterface {
    private static String TAG = VerificationFragment.class.getSimpleName();
    private VerificationViewModel viewModel;
    private VerificationFragmentBinding binding;


    private String verificationCodeTimerKeyExtra = "verificationCodeTimer";
    private String phoneNumberKeyExtra = "phoneNumber";
    private String passwordKeyExtra = "password";
    @Inject
    MySharedPreferences sharedPreferences;
    @Inject
    VerificationViewModelFactory factory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.verification_fragment, container, false);
        viewModel = new ViewModelProvider(this, factory).get(VerificationViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.setVerificationInterface(this);

        binding.otpView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(binding.otpView.getWindowToken(), 0);
                viewModel.checkVerificationCode();
                return true;
            }
            return false;
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        assert getArguments() != null;
        long verificationTimer = getArguments().getLong(verificationCodeTimerKeyExtra);
        String phoneNumber = getArguments().getString(phoneNumberKeyExtra);
        String password = getArguments().getString(passwordKeyExtra);
        if (verificationTimer != 0 && phoneNumber != null && password != null) {
            viewModel.setPhoneNumber(phoneNumber);
            viewModel.setVerificationCodeTimer(verificationTimer);
            viewModel.setPassword(password);
        }else onFailure("خطا در دریافت اطلاعات");

    }

    @Override
    public void onStarted() {
        binding.cvWaitingResponse.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(LoginUserResponse loginUserResponse) {
        if (loginUserResponse != null) {
            if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.loginRegisterFragment) {
                sharedPreferences.setUserAuthTokenKey(loginUserResponse.getId());
                Navigation.findNavController(getView()).navigate(R.id.verificationFragment);
                binding.cvWaitingResponse.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onFailure(String Error) {
        binding.cvWaitingResponse.setVisibility(View.GONE);
        Snackbar snackbar = Snackbar.make(binding.mainContent, "" + Error, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}