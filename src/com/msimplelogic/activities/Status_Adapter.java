package com.msimplelogic.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.msimplelogic.activities.R;

public class Status_Adapter extends ArrayAdapter<HashMap<String, String>> {
    public Status_Adapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}
	customButtonListener customListner;
    private ArrayList<HashMap<String, String>> dataArrayList;
    String ORDER_ID = "order_id";
	String MANAGER_REMARK = "manager_remarks";
	String ORDER_STATUS = "order_status";
	String ITEM_DESC = "item_desc";
	String ITEM_AMOUNT = "item_tamount";
    String QUOTATION_ID = "quotation_id";
    String MODIFY_VALUE = "modify_value";
    String APPROVED_BYADMIN = "approved_amount_by_admin";
	
    HashMap<String, String> getData = new HashMap<String, String>();
    String order_code = "";
    String original_id="";

    public interface customButtonListener {
        public void onButtonClickListner(int position, String value1,String value2,View v);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    private Context context;
    private ArrayList<String> data = new ArrayList<String>();

    public Status_Adapter(Context context,  ArrayList<HashMap<String, String>> dataItem) {
        super(context, R.layout.status_list_txt, dataItem);
        this.dataArrayList = dataItem;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.new_quotationstatus, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView
                    .findViewById(R.id.ordername);

            viewHolder.address = (TextView) convertView
                    .findViewById(R.id.status);
            
            viewHolder.distance = (TextView) convertView
                    .findViewById(R.id.remark);

            viewHolder.quotationId = (TextView) convertView
                    .findViewById(R.id.quotation_id);

            viewHolder.modifyValue = (TextView) convertView
                    .findViewById(R.id.modify_value);

            viewHolder.approvedByAdmin = (TextView) convertView
                    .findViewById(R.id.approved_byadmin);

            viewHolder.date=convertView
                    .findViewById(R.id.date);

//            viewHolder.childButtonnew = (Button) convertView
//                    .findViewById(R.id.childButtonnew);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        getData = dataArrayList.get(position);
        try {

            if (getData.get(ORDER_ID).toString() != null && getData.get(ORDER_ID).toString() != "null") {
                viewHolder.text.setText(getData.get(ORDER_ID));
            }

            if (getData.get(MANAGER_REMARK).toString() != null && getData.get(MANAGER_REMARK).toString() != "null") {
                if (getData.get(MANAGER_REMARK).toString().equalsIgnoreCase("no comment")){
                    viewHolder.address.setText("No remark");
                }else {
                    viewHolder.address.setText(getData.get(MANAGER_REMARK));
                }

            }
            
            if (getData.get(ORDER_STATUS).toString() != null && getData.get(ORDER_STATUS).toString() != "null") {
                viewHolder.distance.setText(getData.get(ORDER_STATUS));
            }

            if (getData.get(QUOTATION_ID).toString() != null && getData.get(QUOTATION_ID).toString() != "null") {
                viewHolder.quotationId.setText(getData.get(QUOTATION_ID));
            }

            if (getData.get(MODIFY_VALUE).toString() != null && getData.get(MODIFY_VALUE).toString() != "null") {
                viewHolder.modifyValue.setText(getData.get(MODIFY_VALUE));
            }
            viewHolder.approvedByAdmin.setText(getData.get(APPROVED_BYADMIN));
        }
        catch(Exception et) {et.printStackTrace();}
        //viewHolder.text.setText(temp);
        order_code = viewHolder.text.getText().toString();

//            viewHolder.childButtonnew.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    getData = dataArrayList.get(position);
//
//                    if (customListner != null) {
//                        customListner.onButtonClickListner(position, getData.get(CUSTOMER_ADDRESS).toString(),getData.get(CUSTOMER_DISTANCE).toString(),v);
//                    }
//
//                }
//            });

        return convertView;
    }

    public class ViewHolder {
        TextView text;
        TextView address;
        TextView distance;
        TextView quotationId;
        TextView modifyValue;
        TextView approvedByAdmin;
        TextView date;
       // Button editorder;
        Button childButtonnew;
    }
}
