package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.user.GetUserOrderCommentResponse;
import ir.boojanco.onlinefoodorder.models.user.OrderItem;
import ir.boojanco.onlinefoodorder.ui.fragments.userorders.OrdersDataSource;
import ir.boojanco.onlinefoodorder.ui.fragments.userorders.OrdersDataSourceFactory;
import ir.boojanco.onlinefoodorder.ui.fragments.userorders.OrdersDataSourceInterface;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.OrdersFragmentInterface;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrdersViewModel extends ViewModel implements OrdersDataSourceInterface {
    private static final String TAG = OrdersViewModel.class.getCanonicalName();
    private Context context;
    private UserRepository userRepository;
    public OrdersFragmentInterface fragmentInterface;
    private String userAuthToken;
    public MutableLiveData<Boolean> waitingResponseAnimateLivedata;

    public LiveData<PagedList<OrderItem>> userOrdersPagedListLiveData;
    public LiveData<PageKeyedDataSource<Integer, OrderItem>> userOrdersPageKeyedDataSourceLiveData;

    public OrdersViewModel(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
        waitingResponseAnimateLivedata = new MutableLiveData<>();
        waitingResponseAnimateLivedata.setValue(true);
    }

    public void getUserOrders(String authToken) {
        this.userAuthToken = authToken;
        OrdersDataSourceFactory ordersDataSourceFactory = new OrdersDataSourceFactory(userRepository, this, authToken);
        userOrdersPageKeyedDataSourceLiveData = ordersDataSourceFactory.getOrdersLiveDataSource();
        PagedList.Config config =
                (new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)).setPageSize(OrdersDataSource.PAGE_SIZE)
                        .build();
        userOrdersPagedListLiveData = (new LivePagedListBuilder(ordersDataSourceFactory, config)).build();
    }

    /*check comments for contetnt score state*/
    public void getOrderComment(long orderId) {
        Observable<GetUserOrderCommentResponse> observable = userRepository.getUserOrderCommentResponseObservable(userAuthToken, orderId);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetUserOrderCommentResponse>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {

                    if (e instanceof NoNetworkConnectionException)
                        fragmentInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            Log.i(TAG, " " + jsonObject.getString("message"));
                        } catch (Exception d) {
                            Log.i(TAG, " " + d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(GetUserOrderCommentResponse commentResponse) {
                    fragmentInterface.onSuccessOrderComment(commentResponse);
                }
            });
        }
    }

    public void addOrderComment(long orderId, float foodQuality, float systemEx, float arrivalTime, float personnelBehaviour, String userComment) {
        GetUserOrderCommentResponse commentBody = new GetUserOrderCommentResponse(orderId, userComment, foodQuality, arrivalTime, systemEx, personnelBehaviour);
        Observable<Response<Void>> observable = userRepository.addUserOrderCommentResponseObservable(userAuthToken, commentBody);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Response<Void>>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {

                    if (e instanceof NoNetworkConnectionException)
                        fragmentInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            Log.i(TAG, " " + jsonObject.getString("message"));
                        } catch (Exception d) {
                            Log.i(TAG, " " + d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(Response<Void> voidResponse) {
                    fragmentInterface.onFailure("نظر شما ثبت شد");
                }
            });
        }
    }

    @Override
    public void onStartedOrdersData() {
        fragmentInterface.onStarted();
    }

    @Override
    public void onSuccessOrdersData() {
        fragmentInterface.onSuccess();
    }

    @Override
    public void onFailureOrdersData(String error) {
        fragmentInterface.onFailure(error);
    }
}
