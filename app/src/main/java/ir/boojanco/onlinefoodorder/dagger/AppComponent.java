package ir.boojanco.onlinefoodorder.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ir.boojanco.onlinefoodorder.ui.activities.MainActivity;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.CartFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.forgotPass.ForgotPassFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.login.LoginFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.logo.MazeehLogoFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.payment.PaymentFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.register.RegisterFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments.RestaurantCommentFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments.RestaurantFoodMenuFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments.RestaurantInfoFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.MapDialogCartFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.MapDialogProfileFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.favoriteFoods.FavoriteFoodsFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.favoriteRestaurants.FavoriteRestaurantsFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.RestaurantDetailsFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.userorders.OrdersFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.userProfile.UserProfileFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.home.HomeFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurants.RestaurantFragment;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface AppComponent {
    void inject(LoginFragment loginFragment);
    void inject(RegisterFragment registerFragment);
    void inject(MazeehLogoFragment mazeehLogoFragment);
    void inject(ForgotPassFragment forgotPassFragment);
    void inject(MainActivity mainActivity);
    void inject(HomeFragment homeFragment);
    void inject(RestaurantFragment restaurantFragment);
    void inject(RestaurantDetailsFragment restaurantDetailsFragment);
    void inject(UserProfileFragment userProfileFragment);
    void inject(OrdersFragment ordersFragment);
    void inject(FavoriteRestaurantsFragment favoriteRestaurantsFragment);
    void inject(FavoriteFoodsFragment favoriteFoodsFragment);
    void inject(RestaurantFoodMenuFragment restaurantFoodMenuFragment);
    void inject(RestaurantInfoFragment restaurantInfoFragment);
    void inject(RestaurantCommentFragment restaurantCommentFragment);
    void inject(CartFragment cartFragment);
    void inject(MapDialogCartFragment mapDialogCartFragment);
    void inject(MapDialogProfileFragment mapDialogProfileFragment);
    void inject(PaymentFragment paymentFragment);

}

