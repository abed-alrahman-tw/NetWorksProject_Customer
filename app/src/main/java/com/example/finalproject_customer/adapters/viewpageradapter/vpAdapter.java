package com.example.finalproject_customer.adapters.viewpageradapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.finalproject_customer.R;
import com.example.finalproject_customer.databinding.ItemSlideImagesWelcomeBinding;
import com.example.finalproject_customer.interfaces.SendPosition;


public class vpAdapter extends PagerAdapter{

    SendPosition sendPosition;

    public vpAdapter(SendPosition sendPosition) {
        this.sendPosition = sendPosition;
    }

    public SendPosition getSendPosition() {
        return sendPosition;
    }

    public void setSendPosition(SendPosition sendPosition) {
        this.sendPosition = sendPosition;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }



    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        ItemSlideImagesWelcomeBinding binding = ItemSlideImagesWelcomeBinding.
                inflate(LayoutInflater.from(container.getContext()));

        String text = "Fast reservation with technicians \n" +
                "and craftsmen";

        int [] images  = new int[3];
        images[0] = R.drawable.onboarding1;
        images[1] = R.drawable.onboarding2;
        images[2] = R.drawable.onboarding1;

        ImageView image = binding.img;
        TextView tv1 = binding.tv1;
        Button button = binding.onbording2BtnNext;

        tv1.setText(text);
        image.setImageResource(images[position]);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendPosition.clickButton(position);

            }
        });

        container.addView(binding.getRoot());
        return binding.getRoot();
    }
}
