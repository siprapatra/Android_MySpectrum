package com.sipra.myspectrum;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<CustomerModel> salesManModels;

    public UserListAdapter(Context context, ArrayList<CustomerModel> item) {
        this.mContext = context;
        this.salesManModels = item;

    }

    public void addItem(ArrayList<CustomerModel> item) {

        salesManModels = item;
        notifyItemInserted(item.size() - 1);

    }

    public void updateItem(CustomerModel item, int Position) {

        salesManModels.set(Position, item);
        notifyItemChanged(Position);
    }

    public void addAll(ArrayList<CustomerModel> item) {

        salesManModels = item;
        notifyDataSetChanged();
    }

    public void removeItem(int pos) {

        notifyItemRemoved(pos);
        salesManModels.remove(pos);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        try {
            holder.txt_name.setText(salesManModels.get(position).getName());
            holder.txt_pass.setText(salesManModels.get(position).getPass());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name, txt_pass;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_name = (TextView) itemView.findViewById(R.id.txt_userName);
            txt_pass = (TextView) itemView.findViewById(R.id.txt_password);
        }

    }

    @Override
    public int getItemCount() {
        return salesManModels.size();
    }

}