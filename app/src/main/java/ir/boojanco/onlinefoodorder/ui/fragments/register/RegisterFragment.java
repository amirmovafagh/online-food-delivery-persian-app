package ir.boojanco.onlinefoodorder.ui.fragments.register;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.FragmentRegisterBinding;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.viewmodels.RegisterViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.RegisterViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RegisterAuth;

public class RegisterFragment extends Fragment implements RegisterAuth {
    private static String TAG = RegisterFragment.class.getSimpleName();
    private RegisterViewModel viewModel;
    private FragmentRegisterBinding binding;
    @Inject
    MySharedPreferences sharedPreferences;
    @Inject
    RegisterViewModelFactory factory;

    private AutoTransition transition;
    private CoordinatorLayout mainLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        viewModel = new ViewModelProvider(this, factory).get(RegisterViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.setRegisterInterface(this);

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

        transition = new AutoTransition();
        mainLayout = binding.mainContent;

        return binding.getRoot();
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
    public void onFailure(String Error) {
        hideKeyboard();
        binding.cvWaitingResponse.setVisibility(View.GONE);
        Snackbar snackbar = Snackbar.make(binding.mainContent, "" + Error, Snackbar.LENGTH_SHORT)
                .setTextColor(getResources().getColor(R.color.materialGray900))
                .setBackgroundTint(getResources().getColor(R.color.colorLowGold));
        snackbar.show();
    }

    @Override
    public void onGetVerificationCode() {
        binding.cvWaitingResponse.setVisibility(View.GONE);
        if (binding.otpView.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(mainLayout, transition);
            binding.textView2.setVisibility(View.VISIBLE);
            binding.otpView.setVisibility(View.VISIBLE);
            binding.txtInputLayoutRegisterPassword.setVisibility(View.VISIBLE);
            binding.btnSendAgainVerifyCode.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void tryAgain() {
        binding.cvWaitingResponse.setVisibility(View.GONE);
    }

    @Override
    public void onLoginSuccess(LoginUserResponse loginUserResponse) {
        if (loginUserResponse != null) {
            if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.registerFragment) {
                sharedPreferences.setUserAuthTokenKey(loginUserResponse.getId());
                sharedPreferences.setPhoneNumber(loginUserResponse.getMobile());
                Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_mainActivity);
                getActivity().finish();
                binding.cvWaitingResponse.setVisibility(View.GONE);
            }

        }
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputManager != null)
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}