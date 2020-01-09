package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.util.ArrayList;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.food.getAllFood.AllFoodList;
import ir.boojanco.onlinefoodorder.models.food.getAllFood.GetAllFoodResponse;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.FoodTypeHeader;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.ListItemType;
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
    private FoodItem foodItem;
    private FoodTypeHeader foodTypeHeader;
    private ArrayList<ListItemType> items ;
    public MutableLiveData<GetAllFoodResponse> allFoodMutableLiveData;

    public RestaurantFoodMenuViewModel(Context context, RestaurantRepository restaurantRepository) {
        allFoodMutableLiveData = new MutableLiveData<>();
        items =  new ArrayList<>();
        this.context = context;
        this.restaurantRepository = restaurantRepository;
    }

    public void getAllFood(String authToken, int restaurantId){
        Observable<GetAllFoodResponse> observable = restaurantRepository.getAllFood(authToken, restaurantId);
        if(observable != null){
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetAllFoodResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        foodInterface.onFailure(e.getMessage());
                    if(e instanceof HttpException){
                        Response response = ((HttpException) e).response();
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            foodInterface.onFailure(jsonObject.getString("message"));
                        }catch (Exception d){
                            foodInterface.onFailure(d.getMessage());
                        }
                    }

                }

                @Override
                public void onNext(GetAllFoodResponse getAllFoodResponse) {
                    foodInterface.onStarted();
                    allFoodMutableLiveData.setValue(getAllFoodResponse);
                    foodInterface.onSuccess(makeFoodAndHeaderList(getAllFoodResponse), allFoodMutableLiveData);
                }
            });
        }
    }

    private ArrayList<ListItemType> makeFoodAndHeaderList(GetAllFoodResponse getAllFoodResponse){
        if(getAllFoodResponse.secondaryList()!=null)
            for (int i = 0; i < getAllFoodResponse.secondaryList().size(); i++){
                String foodType = getAllFoodResponse.secondaryList().get(i);
                Log.e(TAG, ""+foodType);
                foodTypeHeader = new FoodTypeHeader(foodType);
                items.add(foodTypeHeader);
                if(getAllFoodResponse.getMainList()!= null)
                for (int j = 0; j < getAllFoodResponse.getMainList().getAllFoodList().size(); j++){


                    if(getAllFoodResponse.getMainList().getAllFoodList() != null){
                        AllFoodList allFoodList = getAllFoodResponse.getMainList().getAllFoodList().get(j);
                        if(allFoodList != null )
                            for (int n = 0; n < allFoodList.getFoodTypeList().size(); n++){
                                if( foodType.equals(allFoodList.getFoodTypeList().get(n)) || foodType.equalsIgnoreCase(allFoodList.getFoodTypeList().get(n)) || foodType == allFoodList.getFoodTypeList().get(n)){
                                    foodItem = new FoodItem();
                                    foodItem.setId(allFoodList.getId());
                                    foodItem.setCost(allFoodList.getCost());
                                    foodItem.setDiscount(allFoodList.getDiscount());
                                    foodItem.setDetails(allFoodList.getDetails());
                                    foodItem.setLogo(allFoodList.getLogo());
                                    foodItem.setName(allFoodList.getName());
                                    foodItem.setPoint(allFoodList.getPoint());
                                    items.add(foodItem);
                            }
                        }
                    }

                }
            }
        return items;
    }
}
