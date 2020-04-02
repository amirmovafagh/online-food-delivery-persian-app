package ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.util.ArrayList;


import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.data.database.CartItem;
import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.food.AllFoodList;
import ir.boojanco.onlinefoodorder.models.food.GetAllFoodResponse;
import ir.boojanco.onlinefoodorder.models.restaurantPackage.AllPackagesResponse;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.FoodTypeHeader;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.ListItemType;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantFoodMenuInterface;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class RestaurantFoodMenuViewModel extends ViewModel {
    private static final String TAG = RestaurantFoodMenuViewModel.class.getSimpleName();
    public RestaurantFoodMenuInterface foodInterface;
    RestaurantRepository restaurantRepository;
    Context context;
    private String userAuthToken;
    private CompositeDisposable compositeDisposable;
    private CartDataSource cartDataSource;
    private FoodItem foodItem;
    private FoodTypeHeader foodTypeHeader;
    private ArrayList<ListItemType> items;
    private ArrayList<String> foodTypeIndex;
    public Long extraRestaurantId;
    public MutableLiveData<GetAllFoodResponse> allFoodMutableLiveData;
    public MutableLiveData<AllPackagesResponse> allPackagesMutableLiveData;
    public MutableLiveData<Integer> cartItemCount;


    public RestaurantFoodMenuViewModel(Context context, RestaurantRepository restaurantRepository, CartDataSource cartDataSource) {
        allFoodMutableLiveData = new MutableLiveData<>();
        cartItemCount = new MutableLiveData<>();
        allPackagesMutableLiveData = new MutableLiveData<>();


        items = new ArrayList<>();
        foodTypeIndex = new ArrayList<>();
        compositeDisposable = new CompositeDisposable();
        this.cartDataSource = cartDataSource;
        this.context = context;
        this.restaurantRepository = restaurantRepository;
    }

    public void onStop() {
        compositeDisposable.clear();
    }

    public void addToCartItemDB(FoodItem item, Long restaurantId) {
        CartItem cartItem = new CartItem();
        cartItem.setFoodId(item.getId());
        cartItem.setFoodName(item.getName());
        cartItem.setFoodImage(item.getLogo());
        cartItem.setFoodPrice(item.getCost());
        cartItem.setFoodDiscount(item.getDiscount());
        cartItem.setFoodQuantity(item.getCount());
        cartItem.setRestaurantId(restaurantId);

        compositeDisposable.add((Disposable) cartDataSource.insertOrReplaceAll(cartItem)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(() -> {

                    getCartCountItem(extraRestaurantId);

                }, throwable -> {
                    Toast.makeText(context, "{add Cart throwable}->" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                })
        );
    }

    public void getCartCountItem(Long restaurantId) {
        cartDataSource.countItemInCart(restaurantId).subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer integer) {
                cartItemCount.setValue(integer);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(context, "{count cart}" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAllFood(String authToken, long restaurantId) {
        userAuthToken = authToken;
        Observable<GetAllFoodResponse> observable = restaurantRepository.getAllFood(authToken, restaurantId);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetAllFoodResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        foodInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            foodInterface.onFailure(jsonObject.getString("message"));
                        } catch (Exception d) {
                            foodInterface.onFailure(d.getMessage());
                        }
                    }

                }

                @Override
                public void onNext(GetAllFoodResponse getAllFoodResponse) {
                    foodInterface.onStarted();
                    allFoodMutableLiveData.setValue(getAllFoodResponse);
                    foodInterface.onSuccess(makeFoodAndHeaderList(getAllFoodResponse), allFoodMutableLiveData, foodTypeIndex);
                }
            });
        }
    }

    private ArrayList<ListItemType> makeFoodAndHeaderList(GetAllFoodResponse getAllFoodResponse) {
        if (getAllFoodResponse.secondaryList() != null)
            for (int i = 0; i < getAllFoodResponse.secondaryList().size(); i++) {
                String foodType = getAllFoodResponse.secondaryList().get(i);

                foodTypeHeader = new FoodTypeHeader(foodType);
                foodTypeIndex.add(foodType);
                items.add(foodTypeHeader);
                if (getAllFoodResponse.getMainList() != null)
                    for (int j = 0; j < getAllFoodResponse.getMainList().getAllFoodList().size(); j++) {


                        if (getAllFoodResponse.getMainList().getAllFoodList() != null) {
                            AllFoodList allFoodList = getAllFoodResponse.getMainList().getAllFoodList().get(j);
                            if (allFoodList != null)
                                for (int n = 0; n < allFoodList.getFoodTypeList().size(); n++) {
                                    if (foodType.equals(allFoodList.getFoodTypeList().get(n)) || foodType.equalsIgnoreCase(allFoodList.getFoodTypeList().get(n)) || foodType == allFoodList.getFoodTypeList().get(n)) {
                                        foodItem = new FoodItem();
                                        foodItem.setId(allFoodList.getId());
                                        foodItem.setCost(allFoodList.getCost());
                                        foodItem.setDiscount(allFoodList.getDiscountPercent());
                                        foodItem.setDetails(allFoodList.getDetails());
                                        foodItem.setLogo(allFoodList.getLogo());
                                        foodItem.setName(allFoodList.getName());
                                        foodItem.setPoint(allFoodList.getPoint());
                                        items.add(foodItem);
                                        foodTypeIndex.add(" ");
                                    }
                                }
                        }

                    }
            }
        return items;
    }

    public void getRestaurantPackages(String authToken, long restaurantId) {
        Observable<AllPackagesResponse> observable = restaurantRepository.getAllPackagesResponse(authToken, restaurantId);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<AllPackagesResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                    if (e instanceof NoNetworkConnectionException)
                        foodInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            foodInterface.onFailure(jsonObject.getString("message"));
                        } catch (Exception d) {
                            foodInterface.onFailure(d.getMessage());
                        }
                    }

                }

                @Override
                public void onNext(AllPackagesResponse getAllPackagesResponse) {
                    foodInterface.onStarted();
                    allPackagesMutableLiveData.setValue(getAllPackagesResponse);
                    foodInterface.onSuccessRestaurantPackages(allPackagesMutableLiveData);
                }
            });
        }
    }

    public void onFoodFavoriteCheckedChanged(long foodId, boolean isChecked) {
        if (isChecked) {
            addToFavoriteList(foodId);
        } else {
            removeFromFavoriteList(foodId);
        }
    }

    private void removeFromFavoriteList(long foodId) {
        Observable<Response<Void>> observable = restaurantRepository.removeFoodFromFavoriteList(userAuthToken, foodId);

        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Response<Void>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                    if (e instanceof NoNetworkConnectionException)
                        foodInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            foodInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            foodInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(Response<Void> voidResponse) {
                    foodInterface.onFailure("از مورد علاقه ها حذف شد");

                }
            });
        }
    }

    private void addToFavoriteList(long foodId) {
        Observable<Response<Void>> observable = restaurantRepository.addFoodToFavoriteList(userAuthToken, foodId);

        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Response<Void>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                    if (e instanceof NoNetworkConnectionException)
                        foodInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            foodInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            foodInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(Response<Void> voidResponse) {
                    foodInterface.onFailure("به مورد علاقه ها افزوده شد");

                }
            });
        }
    }
}
