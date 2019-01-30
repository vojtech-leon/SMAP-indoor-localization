package cz.uhk.vojtele1.indoorpositiontest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cz.uhk.vojtele1.indoorpositiontest.R;
import cz.uhk.vojtele1.indoorpositiontest.model.WifiScan;

import java.util.List;

public class WifiScanListAdapter extends RecyclerView.Adapter<WifiScanListAdapter.WifiScanHolder> {

    private final LayoutInflater inflater;
    private List<WifiScan> wifiScans; // Cached copy of words

    public WifiScanListAdapter(Context context) { inflater = LayoutInflater.from(context); }

    @Override
    public WifiScanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WifiScanHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WifiScanHolder holder, int position) {
        if (wifiScans != null) {
            WifiScan current = wifiScans.get(position);
            holder.textViewSSID.setText(current.getSsid());
            holder.textViewBSSID.setText(current.getBssid());
            holder.textViewSignal.setText(String.valueOf(current.getRssi()));
            holder.textViewTimestamp.setText(String.valueOf(current.getTimestamp()));
            holder.textViewX.setText(String.valueOf(current.getX()));
            holder.textViewY.setText(String.valueOf(current.getY()));
        } else {
            // Covers the case of data not being ready yet.
            holder.textViewSSID.setText(R.string.loading);
            holder.textViewBSSID.setText(R.string.loading);
            holder.textViewSignal.setText(R.string.loading);
            holder.textViewTimestamp.setText(R.string.loading);
            holder.textViewX.setText(R.string.loading);
            holder.textViewY.setText(R.string.loading);
        }
    }

    public void setWifiScans(List<WifiScan> wifiScans){
        this.wifiScans = wifiScans;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // wifiScans has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (wifiScans != null)
            return wifiScans.size();
        else return 0;
    }

    class WifiScanHolder extends RecyclerView.ViewHolder {
        private final TextView textViewSSID;
        private final TextView textViewBSSID;
        private final TextView textViewSignal;
        private final TextView textViewTimestamp;
        private final TextView textViewX;
        private final TextView textViewY;

        private WifiScanHolder(View itemView) {
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