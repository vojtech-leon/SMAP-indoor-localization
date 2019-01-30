package cz.uhk.vojtele1.indoorpositiontest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cz.uhk.vojtele1.indoorpositiontest.R;
import cz.uhk.vojtele1.indoorpositiontest.model.BleScan;
import cz.uhk.vojtele1.indoorpositiontest.model.WifiScan;

import java.util.List;

public class BleScanListAdapter extends RecyclerView.Adapter<BleScanListAdapter.BleScanHolder> {

    private final LayoutInflater inflater;
    private List<BleScan> bleScans; // Cached copy of words

    public BleScanListAdapter(Context context) { inflater = LayoutInflater.from(context); }

    @Override
    public BleScanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new BleScanHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BleScanHolder holder, int position) {
        if (bleScans != null) {
            BleScan current = bleScans.get(position);
            holder.textViewBSSID.setText(current.getAddress());
            holder.textViewSignal.setText(String.valueOf(current.getRssi()));
            holder.textViewTimestamp.setText(String.valueOf(current.getTimestamp()));
            holder.textViewX.setText(String.valueOf(current.getX()));
            holder.textViewY.setText(String.valueOf(current.getY()));
        } else {
            // Covers the case of data not being ready yet.
            holder.textViewBSSID.setText(R.string.loading);
            holder.textViewSignal.setText(R.string.loading);
            holder.textViewTimestamp.setText(R.string.loading);
            holder.textViewX.setText(R.string.loading);
            holder.textViewY.setText(R.string.loading);
        }
        holder.textViewSSID.setVisibility(View.GONE);
    }

    public void setBleScans(List<BleScan> bleScans){
        this.bleScans = bleScans;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // wifiScans has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (bleScans != null)
            return bleScans.size();
        else return 0;
    }

    class BleScanHolder extends RecyclerView.ViewHolder {
        private final TextView textViewSSID;
        private final TextView textViewBSSID;
        private final TextView textViewSignal;
        private final TextView textViewTimestamp;
        private final TextView textViewX;
        private final TextView textViewY;

        private BleScanHolder(View itemView) {
            super(itemView);
            textViewSSID = itemView.findViewById(R.id.textViewSSID);
            textViewBSSID = itemView.findViewById(R.id.textViewBSSID);
            textViewSignal = itemView.findViewById(R.id.textViewSignal);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
            textViewX = itemView.findViewById(R.id.textViewX);
            textViewY = itemView.findViewById(R.id.textViewY);
        }
    }
}