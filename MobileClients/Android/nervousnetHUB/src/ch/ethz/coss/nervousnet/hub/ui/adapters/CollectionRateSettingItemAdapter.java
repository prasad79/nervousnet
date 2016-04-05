package ch.ethz.coss.nervousnet.hub.ui.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ch.ethz.coss.nervousnet.hub.R;
import ch.ethz.coss.nervousnet.hub.ui.CollectionRateSettingsActivity;
import ch.ethz.coss.nervousnet.vm.NervousnetConstants;

public class CollectionRateSettingItemAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] labels;
	private final Integer[] icons;
	
	static class ViewHolder {
	    public TextView text;
	    public ImageView image;
	  }

	public CollectionRateSettingItemAdapter(Context context, String [] sensor_labels,  Integer [] icons_arr) {
		super(context, R.layout.collection_rate_item, sensor_labels);
		this.context = context;
		this.labels = sensor_labels;
		this.icons = icons_arr;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.collection_rate_item, null);
		}
		
	    TextView textView = (TextView) convertView.findViewById(R.id.sensor_name);
	    ImageView imageView = (ImageView) convertView.findViewById(R.id.sensor_icon);
	    textView.setText(labels[position]);
	    imageView.setImageResource(icons[position]);
	    
	    Button button = (Button) convertView.findViewById(R.id.sensor_level_button);
	    button.setText("High");
	    button.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	createDialog();
	        }
	    });
	  
		return convertView;
	}
	
	
	public Dialog createDialog() {
	    AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    builder.setTitle("Title if Any");
	    builder.setItems(NervousnetConstants.sensor_levels, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int itemClicked) {
	                   String[] option_array = NervousnetConstants.sensor_levels;
	                   String optionSelected = option_array[itemClicked];
	           }
	    });
	    return builder.create();
	}

}