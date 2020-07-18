package ir.boojanco.onlinefoodorder.ui.fragments.userProfile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.List;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.UserProfileFragmentBinding;
import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.CityAdapter;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.CustomStateCityDialog;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.StateAdapter;
import ir.boojanco.onlinefoodorder.ui.fragments.MapDialogProfileFragment;
import ir.boojanco.onlinefoodorder.util.persianDate.PersianDate;
import ir.boojanco.onlinefoodorder.util.persianDate.PersianDateFormat;
import ir.boojanco.onlinefoodorder.viewmodels.UserProfileViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.UserProfileViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.AddressRecyclerViewInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.StateCityDialogInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.UserProfileInterface;


public class UserProfileFragment extends Fragment implements AddressRecyclerViewInterface, UserProfileInterface, StateCityDialogInterface, DatePickerDialog.OnDateSetListener {
    private String TAG = UserProfileFragment.class.getSimpleName();
    private UserProfileFragmentBinding binding;
    private UserProfileViewModel viewModel;
    @Inject
    UserProfileViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;
    @Inject
    Application application;

    private Toolbar toolbar;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;
    private DialogFragment mapFragment;
    private CityAdapter cityAdapter;
    private CustomStateCityDialog stateCityDialog;
    private RecyclerView recyclerViewUserAddress;
    private AddressProfileAdapter addressAdapter;
    private NestedScrollView bottom_sheet, bottom_sheet_profile;
    private BottomSheetBehavior sheetBehavior, sheetBehaviorProfile;
    private PersianCalendar persianCalendar;
    private LiveData<PagedList<UserAddressList>> userAddressPaged;
    private PersianDateFormat pdformater;
    private PersianDate pdate;
    private Snackbar snackbar;
    private ChipGroup chipGroup;
    private AutoTransition transition;
    private Button buttonChangePass;
    private CoordinatorLayout mainLayout;


    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.user_profile_fragment, container, false);
        viewModel = new ViewModelProvider(this, factory).get(UserProfileViewModel.class);
        viewModel.userProfileInterface = this;
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        toolbar = binding.toolbar;

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(
                toolbar, navController, appBarConfiguration);

        //handle bottom sheets
        bottom_sheet = binding.bottomSheet;
        bottom_sheet_profile = binding.bottomSheetProfile;
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        mainLayout = binding.coordinateLayoutMain;
        sheetBehavior.setGestureInsetBottomIgnored(true);
        sheetBehaviorProfile = BottomSheetBehavior.from(bottom_sheet_profile);
        sheetBehaviorProfile.setGestureInsetBottomIgnored(true);
        transition = new AutoTransition();
        buttonChangePass = binding.bottomSheetEditProfileInclude.btnChangePass;

        if (sharedPreferences.getUserAuthTokenKey() == null || sharedPreferences.getUserAuthTokenKey().isEmpty()) {//when user is not login in the system
            viewModel.profileChangeVisibility.postValue(false); // show login reg button
            binding.cvWaitingResponse.setVisibility(View.GONE);
            return binding.getRoot();
        }
        viewModel.profileChangeVisibility.postValue(true); // show profile

        viewModel.getUserAddress(sharedPreferences.getUserAuthTokenKey());
        recyclerViewUserAddress = binding.recyclerViewUserAddressHorizontalItems;
        recyclerViewUserAddress.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewUserAddress.setHasFixedSize(true);
        addressAdapter = new AddressProfileAdapter(this, application);
        recyclerViewUserAddress.setAdapter(addressAdapter);


        chipGroup = binding.bottomSheetAddressInclude.chipGroupAddressTag;

        //go to order fragment
        binding.frameLayoutOrders.setOnClickListener(v -> navController.navigate(R.id.action_userProfileFragment_to_ordersFragment));
        binding.frameLayoutTransactions.setOnClickListener(v -> navController.navigate(R.id.action_userProfileFragment_to_transactionsFragment));
        binding.frameLayoutFaveRestaurants.setOnClickListener(v -> navController.navigate(R.id.action_userProfileFragment_to_faveRestaurantsFragment));
        binding.frameLayoutFaveFoods.setOnClickListener(v -> navController.navigate(R.id.action_userProfileFragment_to_faveFoodsFragment));
        viewModel.getUserProfileInfo(sharedPreferences.getUserAuthTokenKey());

        viewModel.phoneNumberLiveData.postValue(sharedPreferences.getPhoneNumber());

        userAddressPaged = viewModel.userAddressPagedListLiveData;
        userAddressPaged.observe(getActivity(), userAddressLists -> addressAdapter.submitList(userAddressLists)); //set PagedList user address

        viewModel.setChipGroup(chipGroup);

        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        buttonChangePass.setOnClickListener(v -> {
            if (binding.bottomSheetEditProfileInclude.linTextFields.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(mainLayout, transition);
                binding.bottomSheetEditProfileInclude.linTextFields.setVisibility(View.VISIBLE);


            } else {
                TransitionManager.beginDelayedTransition(mainLayout, transition);
                binding.bottomSheetEditProfileInclude.linTextFields.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onRecyclerViewAddressClick(View v, UserAddressList userAddress) {
        switch (v.getId()) {
            case R.id.img_edit_address:
                viewModel.editUserAddress(userAddress);
                sheetBehaviorProfile.setState(BottomSheetBehavior.STATE_HIDDEN);
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                break;
            case R.id.img_remove_address:
                removeAddressDialog(userAddress.getId());
                break;
        }
    }

    private void removeAddressDialog(long userAddressId) {
        // setup the alert builder
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity(), R.style.AlertDialogCustomRTL);
        builder.setCancelable(true);
        builder.setTitle("حذف آدرس");
        builder.setMessage("آیا می\u200cخواهید آدرس را حذف کنید؟");
        // add the buttons
        builder.setPositiveButton("حذف", (dialog, which) -> {
            viewModel.deleteUserAddress(userAddressId);
        });
        builder.setNegativeButton("انصراف", (dialog, which) -> dialog.cancel());
        // create and show the alert dialog
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    public void onStarted() {
        binding.cvWaitingResponse.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateAddressRecyclerView() {
        if (userAddressPaged.hasObservers()) {
            userAddressPaged.removeObservers(getActivity());
            userAddressPaged = viewModel.userAddressPagedListLiveData;
            userAddressPaged.observe(getActivity(), userAddressLists -> addressAdapter.submitList(userAddressLists)); //set PagedList user address
        }
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }


    @Override
    public void showAddressBottomSheet() {
        if (mapFragment.isVisible())
            mapFragment.dismiss();
        sheetBehaviorProfile.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void showMapDialogFragment() {
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragment = getChildFragmentManager().findFragmentByTag("mapDialogUserProfile");
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }

        fragmentTransaction.addToBackStack(null);
        mapFragment = new MapDialogProfileFragment(viewModel);
        //mapFragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Panel);
        mapFragment.show(fragmentTransaction, "mapDialogUserProfile");
    }

    @Override
    public void showDatePickerDialog(long birthDateTimeMill) {

        pdformater = new PersianDateFormat("Y/m/d");
        pdate = new PersianDate();

        persianCalendar = new PersianCalendar(birthDateTimeMill);
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                persianCalendar.getPersianYear(),
                persianCalendar.getPersianMonth(),
                persianCalendar.getPersianDay()
        );

        datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void showStateCityCustomDialog() {
        if (stateCityDialog != null)
            stateCityDialog.show();
        else
            onFailure("خطا در دریافت اطلاعات استان ها");
    }

    @Override
    public void onSuccessGetUserProfileInfo() {
        binding.cvWaitingResponse.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessGetStates(List<AllStatesList> statesLists) {
        StateAdapter stateAdapter = new StateAdapter(this, application);
        cityAdapter = new CityAdapter(this, application);
        stateCityDialog = new CustomStateCityDialog(getActivity(), stateAdapter, statesLists, cityAdapter);
        stateCityDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onSuccessGetcities(List<AllCitiesList> citiesLists) {
        cityAdapter.setCitiesLists(citiesLists);
    }


    @Override
    public void onSuccessGetAddress() {

    }

    @Override
    public void onLogoutUser() {
        if (getActivity() != null) {
            exitFromUserAccount();
        }

    }

    private void exitFromUserAccount() {
        // setup the alert builder
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity(), R.style.AlertDialogCustomRTL);
        builder.setCancelable(true);
        builder.setTitle("خروج از حساب کاربری");
        builder.setMessage("آیا می\u200cخواهید از حساب کاربری خارج شوید؟");
        // add the buttons
        builder.setPositiveButton("خروج", (dialog, which) -> {
            sharedPreferences.removeUserAuthTokenKey();
            sharedPreferences.removePhoneNumber();
            getActivity().moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        });
        builder.setNegativeButton("انصراف", (dialog, which) -> dialog.cancel());
        // create and show the alert dialog
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    public void onEditUserProfile() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehaviorProfile.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onFailure(String Error) {
        binding.cvWaitingResponse.setVisibility(View.GONE);
        snackbar = Snackbar.make(binding.coordinateLayoutMain, "" + Error, Snackbar.LENGTH_SHORT);
        snackbar.show();

    }

    @Override
    public void hideBottomSheet() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehaviorProfile.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void goToLoginRegisterActivity() {
        if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.userProfileFragment) {
            if (getActivity() != null) getActivity().finish();
            Navigation.findNavController(getView()).navigate(R.id.action_userProfileFragment_to_enterActivity);
        }
    }

    @Override
    public void closeBottomSheet() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        sheetBehaviorProfile.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onStateItemClick(AllStatesList state) {
        viewModel.setStateId(state.getId());
        viewModel.state.postValue(state.getName());
        viewModel.city.postValue(null);
        viewModel.getCities(state.getId());
    }

    @Override
    public void onCityItemClick(AllCitiesList city) {
        viewModel.setCityId(city.getId());
        viewModel.city.setValue(city.getName());
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int yaer, int month, int day) {

        pdate.setShYear(yaer);
        pdate.setShMonth(month + 1);
        pdate.setShDay(day);
        viewModel.setBirthDateTimeMill(pdate.getTime());
        pdate = new PersianDate(pdate.getTime());
        viewModel.birthDateLiveData.setValue("" + pdformater.format(pdate));
        Log.i(TAG, "" + pdformater.format(pdate));
    }

}
