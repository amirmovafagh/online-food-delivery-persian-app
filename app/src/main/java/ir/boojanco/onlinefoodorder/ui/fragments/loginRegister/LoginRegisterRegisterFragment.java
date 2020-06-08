package ir.boojanco.onlinefoodorder.ui.fragments.loginRegister;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.FragmentLoginRegisterBinding;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.viewmodels.LoginRegisterViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.LoginRegisterViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.LoginRegisterAuth;

public class LoginRegisterRegisterFragment extends Fragment implements LoginRegisterAuth {
    private static final String TAG = LoginRegisterRegisterFragment.class.getSimpleName();
    private String verificationCodeTimerKeyExtra = "verificationCodeTimer";
    private String phoneNumberKeyExtra = "phoneNumber";
    private String passwordKeyExtra = "password";
    private FragmentLoginRegisterBinding binding;
    private LoginRegisterViewModel viewModel;

    @Inject
    Application application;
    @Inject
    LoginRegisterViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;

    EditText password, phoneNum;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // assign singleton instances to fields
        // We need to cast to `App` in order to get the right method
        ((App) getActivity().getApplication()).getComponent().inject(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_register, container, false);
        viewModel = new ViewModelProvider(this, factory).get(LoginRegisterViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.loginRegisterAuth = this;

        phoneNum = binding.loginPhoneEdtText;
        //set font on password editText
        password = binding.loginPasswordEdtText;
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());

        password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
                return true;
            }
            return false;
        });

        binding.buttonRegisterActivity.setOnClickListener(v -> {
        });
        binding.buttonEnterAsGuest.setOnClickListener(v -> {
            if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.loginRegisterFragment) {
                Navigation.findNavController(getView()).navigate(R.id.action_loginRegisterFragment_to_mainActivity);
                getActivity().finish();
                binding.cvWaitingResponse.setVisibility(View.GONE);
            }

        });


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onStarted() {
        binding.cvWaitingResponse.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoginSuccess(LoginUserResponse loginUserResponse) {
        if (loginUserResponse != null) {
            if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.loginRegisterFragment) {
                sharedPreferences.setUserAuthTokenKey(loginUserResponse.getId());
                sharedPreferences.setPhoneNumber(loginUserResponse.getMobile());
                Navigation.findNavController(getView()).navigate(R.id.action_loginRegisterFragment_to_mainActivity);
                getActivity().finish();
                binding.cvWaitingResponse.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onRegisterSuccess(Long time, String phoneNumber, String password) {
        if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.loginRegisterFragment) {
            Bundle bundle = new Bundle();
            bundle.putLong(verificationCodeTimerKeyExtra, time);
            bundle.putString(phoneNumberKeyExtra, phoneNumber);
            bundle.putString(passwordKeyExtra, password);
            Navigation.findNavController(getView()).navigate(R.id.action_loginRegisterFragment_to_verificationFragment, bundle);
            binding.cvWaitingResponse.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String Error) {
        binding.cvWaitingResponse.setVisibility(View.GONE);
        Snackbar snackbar = Snackbar.make(binding.mainContent, "" + Error, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}