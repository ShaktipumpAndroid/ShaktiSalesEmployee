package searchlist.complaint;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;
import java.util.List;

import bean.ComplaintImage;

public class Complaint_Image_ListViewAdapter extends BaseAdapter {

    Context mContext;
    private List<Complaint_Image_Name> complaintSearchlist = new ArrayList<>();

    private ImageSelectionListner imageSelectionListener;

    public Complaint_Image_ListViewAdapter(Context context, List<Complaint_Image_Name> complaintSearchlist) {
        mContext = context;
        this.complaintSearchlist = complaintSearchlist;
    }

    @Override
    public int getCount() {
        return complaintSearchlist.size();
    }

    @Override
    public Complaint_Image_Name getItem(int position) {
        return complaintSearchlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;

        holder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.search_listview_complaint_image, null);

        holder.image_name = (TextView) view.findViewById(R.id.image_name);
        holder.complete_icon = (TextView) view.findViewById(R.id.complete_icon);
        holder.image_name.setText(complaintSearchlist.get(position).getName());

         if(complaintSearchlist.get(position).getImgSelected()!=null&&complaintSearchlist.get(position).getImgSelected()){
             holder.complete_icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);
         }

        view.setOnClickListener(view1 -> imageSelectionListener.ImgSelectionLis(position,complaintSearchlist.get(position)));

        return view;
    }

    public void ImgSelectionListner(ImageSelectionListner actDocList) {
        try {
            imageSelectionListener = actDocList;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
    public interface ImageSelectionListner {
        void ImgSelectionLis(int position, Complaint_Image_Name complaintImageName);

    }


    public static class ViewHolder {
        TextView image_name, complete_icon;
    }
}