package ir.boojanco.onlinefoodorder.ui.fragments.forgotPass;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

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
import ir.boojanco.onlinefoodorder.databinding.FragmentForgotPassBinding;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.ui.fragments.loginRegister.VerificationFragment;
import ir.boojanco.onlinefoodorder.viewmodels.ForgotPassViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.VerificationViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.VerificationViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.ForgotPassInterface;

public class ForgotPassFragment extends Fragment implements ForgotPassInterface {
    private static String TAG = ForgotPassFragment.class.getSimpleName();
    private ForgotPassViewModel viewModel;
    private FragmentForgotPassBinding binding;
    @Inject
    MySharedPreferences sharedPreferences;
    @Inject
    VerificationViewModelFactory factory;

    private AutoTransition transition;
    private ConstraintLayout mainLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_pass, container, false);
        viewModel = new ViewModelProvider(this, factory).get(ForgotPassViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.setForgotPassInterface(this);

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
        binding.cvWaitingResponse.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(LoginUserResponse loginUserResponse) {

    }

    @Override
    public void onFailure(String Error) {
        binding.cvWaitingResponse.setVisibility(View.GONE);
        Snackbar snackbar = Snackbar.make(binding.mainContent, "" + Error, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void onGetVerificationCode() {
        if (binding.buttonResetPass.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(mainLayout, transition);
            binding.textView2.setVisibility(View.VISIBLE);
            binding.otpView.setVisibility(View.VISIBLE);
            binding.btnSendAgainVerifyCode.setVisibility(View.VISIBLE);

        } else {
            TransitionManager.beginDelayedTransition(mainLayout, transition);
            binding.textView2.setVisibility(View.GONE);
            binding.otpView.setVisibility(View.GONE);
            binding.btnSendAgainVerifyCode.setVisibility(View.GONE);
        }
    }
}