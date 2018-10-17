package com.wunder.test.wunderapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.ViewHolder> {

    private List<Car> cars;
    private Context context;

    public CarListAdapter(List<Car> cars, Context context) {
        this.cars = cars;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.car_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Car car = this.cars.get(i);
        viewHolder.carName.setText(car.getId() + " " + car.getName());
        viewHolder.carVin.setText(car.getVin());

        viewHolder.carDataWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("SELECTED_CAR", car.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView carName;
        public TextView carVin;
        public LinearLayout carDataWrapper;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.carName = itemView.findViewById(R.id.car_name);
            this.carVin = itemView.findViewById(R.id.car_vin);
            this.carDataWrapper = itemView.findViewById(R.id.car_data_wrapper);
        }
    }
}
