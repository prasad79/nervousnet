package ch.ethz.coss.nervousnet.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ch.ethz.coss.nervousnet.R;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;

	public ImageAdapter(Context c, String[] labels, Integer[] icons) {
		mContext = c;
		mLabels = labels;// mContext.getResources().getStringArray(R.array.main_grid);
		mThumbIds = icons;
	}

	@Override
	public int getCount() {
		return mThumbIds.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View MyView = convertView;

		if (convertView == null) {
			/* we define the view that will display on the grid */

			// Inflate the layout
			LayoutInflater li = ((Activity) mContext).getLayoutInflater();
			MyView = li.inflate(R.layout.category_grid_item, null);
		}
		// Add The Text!!!
		TextView tv = (TextView) MyView.findViewById(R.id.grid_item_text);
		tv.setText(mLabels[position]);
		// Add The Image!!!
		ImageView iv = (ImageView) MyView.findViewById(R.id.grid_item_image);
		iv.setImageResource(mThumbIds[position]);

		return MyView;
	}

	// references to our images
	private Integer[] mThumbIds = { R.drawable.ic_missing, R.drawable.ic_missing, R.drawable.ic_missing,
			R.drawable.ic_missing, R.drawable.ic_missing, R.drawable.ic_missing };

	// references to our labels
	private String[] mLabels;

}