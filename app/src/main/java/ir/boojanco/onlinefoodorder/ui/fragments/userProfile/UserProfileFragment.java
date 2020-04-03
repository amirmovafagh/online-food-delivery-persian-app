package ir.boojanco.onlinefoodorder.ui.fragments.userProfile;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
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
    private NestedScrollView bottom_sheet;
    private BottomSheetBehavior sheetBehavior;
    private PersianCalendar persianCalendar;
    private LiveData<PagedList<UserAddressList>> userAddressPaged;
    private PersianDateFormat pdformater;
    private PersianDate pdate;

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

        viewModel.getUserAddress(sharedPreferences.getUserAuthTokenKey());
        recyclerViewUserAddress = binding.recyclerViewUserAddressHorizontalItems;
        recyclerViewUserAddress.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewUserAddress.setHasFixedSize(true);
        addressAdapter = new AddressProfileAdapter(this, application);
        recyclerViewUserAddress.setAdapter(addressAdapter);


        bottom_sheet = binding.bottomSheet;
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        //go to order fragment
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        binding.frameLayoutOrders.setOnClickListener(v -> navController.navigate(R.id.action_userProfileFragment_to_ordersFragment));
        binding.frameLayoutFaveRestaurants.setOnClickListener(v -> navController.navigate(R.id.action_userProfileFragment_to_faveRestaurantsFragment));
        binding.frameLayoutFaveFoods.setOnClickListener(v -> navController.navigate(R.id.action_userProfileFragment_to_faveFoodsFragment));

        viewModel.getUserProfileInfo(sharedPreferences.getUserAuthTokenKey());
        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userAddressPaged = viewModel.userAddressPagedListLiveData;
        userAddressPaged.observe(getActivity(), userAddressLists -> addressAdapter.submitList(userAddressLists)); //set PagedList user address

    }

    @Override
    public void onRecyclerViewAddressClick(View v, UserAddressList userAddress, int position) {
        switch (v.getId()) {
            case R.id.img_edit_address:
                viewModel.editUserAddress(userAddress, position);
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                break;
            case R.id.img_remove_address:
                break;
        }
    }

    @Override
    public void onStarted() {
        binding.cvWaitingResponse.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateAddressRecyclerView(int position) {
        if (userAddressPaged.hasObservers()) {
            userAddressPaged.removeObservers(getActivity());
            userAddressPaged = viewModel.userAddressPagedListLiveData;
            userAddressPaged.observe(getActivity(), userAddressLists -> addressAdapter.submitList(userAddressLists)); //set PagedList user address
        }
        Snackbar snackbar = Snackbar.make(binding.coordinateLayoutMain, "This is Simple Snackbar", Snackbar.LENGTH_SHORT);
        snackbar.show();
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


    @Override
    public void showAddressBottomSheet() {
        if (mapFragment.isVisible())
            mapFragment.dismiss();
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
            Toast.makeText(application, "خطا در دریافت اطلاعات استان ها", Toast.LENGTH_LONG).show();
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
        sharedPreferences.removeUserAuthTokenKey();
        if (getActivity() != null)
            getActivity().moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    @Override
    public void onEditUserProfile() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onFailure(String Error) {
        Toast.makeText(application, "" + Error, Toast.LENGTH_SHORT).show();
        binding.cvWaitingResponse.setVisibility(View.GONE);
    }

    @Override
    public void hideBottomSheet() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onStateItemClick(AllStatesList state) {
        viewModel.setStateId(state.getId());
        viewModel.state.setValue(state.getName());
        viewModel.city.setValue(null);
        viewModel.getCities(sharedPreferences.getUserAuthTokenKey(), state.getId());
    }

    @Override
    public void onCityItemClick(AllCitiesList city) {
        viewModel.setCityId(city.getId());
        viewModel.city.setValue(city.getName());
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int yaer , int month, int day) {

        pdate.setShYear(yaer);
        pdate.setShMonth(month+1);
        pdate.setShDay(day);
        viewModel.setBirthDateTimeMill(pdate.getTime());
        pdate= new PersianDate(pdate.getTime());
        viewModel.birthDateLiveData.setValue(""+pdformater.format(pdate));
        Log.i(TAG,""+pdformater.format(pdate));
    }
}
