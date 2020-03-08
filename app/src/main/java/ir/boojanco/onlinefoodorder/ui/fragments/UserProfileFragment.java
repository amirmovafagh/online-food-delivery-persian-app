package ir.boojanco.onlinefoodorder.ui.fragments;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.UserProfileFragmentBinding;
import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;
import ir.boojanco.onlinefoodorder.ui.activities.cart.AddressAdapter;
import ir.boojanco.onlinefoodorder.viewmodels.UserProfileViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.UserProfileViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.AddressRecyclerViewInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.UserProfileInterface;

public class UserProfileFragment extends Fragment implements AddressRecyclerViewInterface , UserProfileInterface {

    private UserProfileFragmentBinding binding;
    private UserProfileViewModel viewModel;
    @Inject
    UserProfileViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;
    @Inject
    Application application;

    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;
    private DialogFragment mapFragment;
    private RecyclerView recyclerViewUserAddress;
    private AddressAdapter addressAdapter;
    private NestedScrollView bottom_sheet;
    private BottomSheetBehavior sheetBehavior;

    private LiveData<PagedList<UserAddressList>> userAddressPaged;
    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.user_profile_fragment,container, false);
        viewModel = new ViewModelProvider(this, factory).get(UserProfileViewModel.class);
        viewModel.userProfileInterface = this;
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.getUserAddress(sharedPreferences.getUserAuthTokenKey());
        recyclerViewUserAddress = binding.recyclerViewUserAddressHorizontalItems;
        recyclerViewUserAddress.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewUserAddress.setHasFixedSize(true);
        addressAdapter = new AddressAdapter(this, application, false);
        recyclerViewUserAddress.setAdapter(addressAdapter);

        bottom_sheet = binding.bottomSheet;
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        //go to order fragment
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        binding.frameLayoutOrders.setOnClickListener(v -> navController.navigate(R.id.action_userProfileFragment_to_ordersFragment));
        binding.frameLayoutFaveRestaurants.setOnClickListener(v -> navController.navigate(R.id.action_userProfileFragment_to_faveRestaurantsFragment));
        binding.frameLayoutFaveFoods.setOnClickListener(v -> navController.navigate(R.id.action_userProfileFragment_to_faveFoodsFragment));
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
        switch (v.getId()){
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

    }

    @Override
    public void updateAddressRecyclerView(int position) {
        if (userAddressPaged.hasObservers()){
            userAddressPaged.removeObservers(getActivity());
            userAddressPaged = viewModel.userAddressPagedListLiveData;
            userAddressPaged.observe(getActivity(), userAddressLists -> addressAdapter.submitList(userAddressLists)); //set PagedList user address
        }
        Snackbar snackbar = Snackbar.make(binding.coordinateLayoutMain,"This is Simple Snackbar",Snackbar.LENGTH_SHORT);
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
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragment = getActivity().getSupportFragmentManager().findFragmentByTag("mapDialogUserProfile");
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }

        fragmentTransaction.addToBackStack(null);
        mapFragment = new MapDialogProfileFragment(viewModel);
        //mapFragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Panel);

        mapFragment.show(fragmentTransaction, "mapDialogUserProfile");
    }

    @Override
    public void showStateCityCustomDialog() {

    }

    @Override
    public void onSuccessGetStates(List<AllStatesList> statesLists) {

    }

    @Override
    public void onSuccessGetcities(List<AllCitiesList> citiesLists) {

    }


    @Override
    public void onSuccessGetAddress() {

    }

    @Override
    public void onFailure(String Error) {
        Toast.makeText(application, ""+Error, Toast.LENGTH_SHORT).show();
    }
}
