package ir.boojanco.onlinefoodorder.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ir.boojanco.onlinefoodorder.ui.activities.cart.CartActivity;
import ir.boojanco.onlinefoodorder.ui.activities.LoginActivity;
import ir.boojanco.onlinefoodorder.ui.activities.MainActivity;
import ir.boojanco.onlinefoodorder.ui.activities.RegisterActivity;
import ir.boojanco.onlinefoodorder.ui.activities.payment.PaymentActivity;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.RestaurantDetailsActivity;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments.RestaurantFoodMenuFragment;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments.RestaurantInfoFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.MapDialogCartFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.MapDialogProfileFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.userorders.OrdersFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.UserProfileFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.home.HomeFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurants.RestaurantFragment;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface AppComponent {
    void inject(LoginActivity loginActivity);

    void inject(MainActivity mainActivity);
    void inject(RegisterActivity registerActivity);
    void inject(HomeFragment homeFragment);
    void inject(RestaurantFragment restaurantFragment);
    void inject(UserProfileFragment userProfileFragment);
    void inject(OrdersFragment ordersFragment);
    void inject(RestaurantDetailsActivity restaurantFoodActivity);
    void inject(RestaurantFoodMenuFragment restaurantFoodMenuFragment);
    void inject(RestaurantInfoFragment restaurantInfoFragment);
    void inject(CartActivity cartActivity);
    void inject(MapDialogCartFragment mapDialogCartFragment);
    void inject(MapDialogProfileFragment mapDialogProfileFragment);
    void inject(PaymentActivity paymentActivity);

}

