package com.diu.tourstravelsadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.diu.tourstravelsadmin.Model.ModelUser;
import com.diu.tourstravelsadmin.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> implements Filterable {

    private final Context context;
    ArrayList<ModelUser> users;
    ArrayList<ModelUser> usersAll;

    public UserListAdapter(Context context, ArrayList<ModelUser> users) {
        this.context = context;
        this.users = users;
        usersAll=new ArrayList<>(users);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_list,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.userName.setText("User Name: "+users.get(i).getUsername());
        viewHolder.email.setText("E-mail: "+users.get(i).getEmail());
        viewHolder.phoneNo.setText("Phone No: "+users.get(i).getContactNo());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<ModelUser> filteredUsersList= new ArrayList<>();
            if(constraint==null || constraint.length()==0){
                filteredUsersList.addAll(usersAll);
            }
            else {
                String searchChr = constraint.toString().toLowerCase().trim();
                for(ModelUser user:usersAll){
                    if(user.getUsername().toLowerCase().contains(searchChr)){
                        filteredUsersList.add(user);
                    }
                }
            }

            FilterResults filterResults= new FilterResults();
            filterResults.values= filteredUsersList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            users.clear();
            users.addAll((Collection<? extends ModelUser>) results.values);
            notifyDataSetChanged();

        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userName, email, phoneNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName=itemView.findViewById(R.id.userName);
            email=itemView.findViewById(R.id.email);
            phoneNo=itemView.findViewById(R.id.phoneNo);
        }
    }

    public void setValues(ModelUser user){
        users.add(0,user);
        usersAll.add(0,user);
        notifyDataSetChanged();
    }

}
