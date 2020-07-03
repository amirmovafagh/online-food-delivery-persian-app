package ir.boojanco.onlinefoodorder.ui.fragments.login;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

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
import ir.boojanco.onlinefoodorder.databinding.FragmentLoginBinding;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.viewmodels.LoginViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.LoginViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.LoginAuth;

public class LoginFragment extends Fragment implements LoginAuth {
    private static final String TAG = LoginFragment.class.getSimpleName();
    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;

    @Inject
    Application application;
    @Inject
    LoginViewModelFactory factory;
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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        viewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.loginAuth = this;

        phoneNum = binding.loginPhoneEdtText;
        //set font on password editText
        password = binding.loginPasswordEdtText;
        //password.setTypeface(Typeface.DEFAULT);
        //password.setTransformationMethod(new PasswordTransformationMethod());

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
            if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.loginFragment) {
                Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_mainActivity);
                getActivity().finish();
                binding.cvWaitingResponse.setVisibility(View.GONE);
            }

        });


        return binding.getRoot();
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputManager != null)
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onStarted() {
        hideKeyboard();
        binding.cvWaitingResponse.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoginSuccess(LoginUserResponse loginUserResponse) {
        if (loginUserResponse != null) {
            if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.loginFragment) {
                sharedPreferences.setUserAuthTokenKey(loginUserResponse.getId());
                sharedPreferences.setPhoneNumber(loginUserResponse.getMobile());
                Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_mainActivity);
                getActivity().finish();
                binding.cvWaitingResponse.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void goToRegisterFragment() {
        if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.loginFragment) {
            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_registerFragment);
        }
    }

    @Override
    public void onFailure(String Error) {
        hideKeyboard();
        binding.cvWaitingResponse.setVisibility(View.GONE);
        Snackbar snackbar = Snackbar.make(binding.mainContent, "" + Error, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void goToForgotPassFragment() {
        if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.loginFragment) {
            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_forgotPassFragment);
        }
    }
}