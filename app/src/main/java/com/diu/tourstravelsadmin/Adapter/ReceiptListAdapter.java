package com.diu.tourstravelsadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.diu.tourstravelsadmin.Activity.BookingDetailsActivity;
import com.diu.tourstravelsadmin.Activity.ReceiptConfirmActivity;
import com.diu.tourstravelsadmin.Model.ModelPayment;
import com.diu.tourstravelsadmin.R;

import java.util.ArrayList;

public class ReceiptListAdapter extends RecyclerView.Adapter<ReceiptListAdapter.ViewHolder> {

    private final Context context;
    ArrayList<ModelPayment> payments;

    public ReceiptListAdapter(Context context, ArrayList<ModelPayment> payments) {
        this.context = context;
        this.payments = payments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recepit_list,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptListAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.totalAmount.setText("Total Amount: "+payments.get(i).getTotalAmount());
        viewHolder.paymentDate.setText("Date of Payment: "+payments.get(i).getDateOfPayment());
        viewHolder.trxId.setText("Payment TrxId: "+payments.get(i).getPaymentTrxId());
        viewHolder.bookingId.setText("Booking No: "+payments.get(i).getBookingId());

        if (payments.get(i).getPaymentStatus().equals("Not Confirmed")){
            viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FFEC6C61"));
        }

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ReceiptConfirmActivity.class);
                intent.putExtra("packageName",payments.get(i).getPackageName());
                intent.putExtra("bookingId",payments.get(i).getBookingId());
                intent.putExtra("phoneNo",payments.get(i).getPhoneNo());
                intent.putExtra("bookingName",payments.get(i).getBookingName());
                intent.putExtra("dateOfBooking",payments.get(i).getBookingDate());
                intent.putExtra("totalAmount",payments.get(i).getTotalAmount());
                intent.putExtra("dateOfPayment",payments.get(i).getDateOfPayment());
                intent.putExtra("trxId",payments.get(i).getPaymentTrxId());
                intent.putExtra("paymentStatus",payments.get(i).getPaymentStatus());
                intent.putExtra("paymentId",payments.get(i).getPaymentId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookingId,totalAmount, paymentDate, trxId;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookingId = (TextView) itemView.findViewById(R.id.bookingId);
            totalAmount = (TextView) itemView.findViewById(R.id.totalAmount);
            paymentDate = (TextView) itemView.findViewById(R.id.paymentDate);
            trxId = (TextView) itemView.findViewById(R.id.trxId);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }
    }

    public void setValues(ModelPayment payment){
        payments.add(0,payment);
        notifyDataSetChanged();
    }
}
