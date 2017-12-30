package illuminomete.com.example.android.bakemeter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by android on 2017/12/26.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Integer> datas;

    public RecyclerAdapter(Context context, List<Integer> datas) {
        layoutInflater = LayoutInflater.from(context);
        this.datas = datas;
    }
    /**
     * ItemClick的回调接口
     * @author zhy
     *
     */
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recyclerlayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.recyclerIV = (ImageView) view.findViewById(R.id.recyelerIV);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.recyclerIV.setImageResource(datas.get(position));
        if(mOnItemClickLitener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLitener.onItemClick(holder.itemView,position);
                }
            });

        }
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
      public ViewHolder(View itemView) {
          super(itemView);
      }
      ImageView recyclerIV;
  }
}
