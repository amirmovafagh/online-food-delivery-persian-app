package ir.boojanco.onlinefoodorder.ui.fragments;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import com.smarteist.autoimageslider.SliderViewAdapter;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.ImageSliderLayoutItemHomeBinding;

public class SliderAdapterHome extends SliderViewAdapter<SliderAdapterHome.SliderAdapterVH> {

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        ImageSliderLayoutItemHomeBinding imageSliderLayoutItemHomeBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.image_slider_layout_item_home, parent,false);
        return new SliderAdapterVH(imageSliderLayoutItemHomeBinding);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        viewHolder.imageSliderLayoutItemHomeBinding.setText("آیتم : "+ position);

        switch (position) {
            case 0:
                viewHolder.imageSliderLayoutItemHomeBinding.setImageUrl("https://wallpapergram.ir/wp-img/WallpaperGram.IR_1563054964_z11217.jpg?auto=compress&cs=tinysrgb&h=750&w=1260");
                break;
            case 1:
                viewHolder.imageSliderLayoutItemHomeBinding.setImageUrl("https://wallpapergram.ir/wp-img/WallpaperGram.IR_1563054124_z62270.jpg");
                break;
            case 2:
                viewHolder.imageSliderLayoutItemHomeBinding.setImageUrl("https://wallpapergram.ir/wp-img/WallpaperGram.IR_1563053281_z73216.jpg");
                break;
            default:
                viewHolder.imageSliderLayoutItemHomeBinding.setImageUrl("https://wallpapergram.ir/wp-img/WallpaperGram.IR_1558274303_85250.jpg");

                break;

        }

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return 4;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        private ImageSliderLayoutItemHomeBinding imageSliderLayoutItemHomeBinding;

        public SliderAdapterVH( ImageSliderLayoutItemHomeBinding imageSliderLayoutItemHomeBinding) {
            super(imageSliderLayoutItemHomeBinding.getRoot());
            this.imageSliderLayoutItemHomeBinding = imageSliderLayoutItemHomeBinding;
        }
    }
}
