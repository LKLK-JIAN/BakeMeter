package illuminomete.com.example.android.bakemeter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2017/11/29.
 */
public class MyAdapter extends BaseAdapter {
    ViewHolder holder;
    Context context;
    public static List<BluetoothDevice> devices =new ArrayList<>();

    public MyAdapter(Context context){
        this.context=context;
    }
   public void addDevice(BluetoothDevice device){
            if (!devices.contains(device)){
                devices.add(device);
            }
        }


    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int i) {
        return devices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        holder=new ViewHolder();
        view=View.inflate(context,R.layout.listlayout,null);
        holder.name=(TextView) view.findViewById(R.id.name);
        holder.address=(TextView) view.findViewById(R.id.address);
        view.setTag(holder);
        //加入数据
        BluetoothDevice device =devices.get(i);
        String name =device.getName();
        if (name!=null&&name.length()>0){
           holder.name.setText(device.getName());
        }else {
            holder.name.setText("00000");
        }
        String address =device.getAddress();
       holder.address.setText(address);
        return view;
    }

    private class ViewHolder {
        TextView name;
        TextView address;
    }
}
