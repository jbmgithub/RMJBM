package com.rm.rmjbm.adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rm.rmjbm.R;
import com.rm.rmjbm.model.StockDataModel;

import java.util.List;

public class StockDataAdapter extends RecyclerView.Adapter<StockDataAdapter.MyViewHolder> {
    private List<StockDataModel> stockList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvStoreLoc, tvBatchNumber, tvStock;

        public MyViewHolder(View view) {
            super(view);
            tvStoreLoc = view.findViewById(R.id.tvSLLRStoreLoc);
            tvBatchNumber = view.findViewById(R.id.tvSLLRBatchNumber);
            tvStock = view.findViewById(R.id.tvSLLRStock);
        }
    }

    public StockDataAdapter(List<StockDataModel> stockList) {
        this.stockList = stockList;
    }

    @Override
    public StockDataAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.scan_label_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        ModelDispatchLabelList labelList = dispatchLabelList.get(position);
//        String strPickList = labelList.getPListNo().replaceFirst("^0*", "");
        holder.tvStoreLoc.setText(stockList.get(position).getLGORT());
        holder.tvBatchNumber.setText(stockList.get(position).getCHARG());
        holder.tvStock.setText(String.format("%.0f", Float.parseFloat(stockList.get(position).getCLABS())));
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
