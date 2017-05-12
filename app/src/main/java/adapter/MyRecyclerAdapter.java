package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.jem.stockmanager.R;

import java.util.List;

/**
 * Created by jem on 2017/5/7.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<String> list;
    private LayoutInflater mInflater;

    public MyRecyclerAdapter(Context mContext, List<String> list) {
        this.context = mContext;
        this.list = list;
        mInflater = LayoutInflater.from(mContext);
    }

    public interface onItemClickListener {
        void onItemClick(View v, int pos);
    }

    private onItemClickListener clickListener;

    public void setOnItemClickListener(onItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.textView.setText(list.get(position));

        if (clickListener != null) {
            holder.textView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                        clickListener.onItemClick(holder.textView, position);

                }
            });
        }


    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}

class MyViewHolder extends ViewHolder {

    TextView textView;

    public MyViewHolder(View itemView) {

        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.list_item);
    }
}