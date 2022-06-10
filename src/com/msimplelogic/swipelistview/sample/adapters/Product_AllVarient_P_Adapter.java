package com.msimplelogic.swipelistview.sample.adapters;

/**
 * Created by vinod on 16/03/17.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.msimplelogic.activities.Check_Null_Value;
import com.msimplelogic.activities.DataBaseHelper;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Product_AllVarient_P_Adapter extends ArrayAdapter<HashMap<String, String>> {
    String price_str;
    public TextView tx;
    static Double sum = 0.0;
    customButtonListener customListner;
    ArrayList<String> list1 = new ArrayList<String>();
    ArrayList<String> list2 = new ArrayList<String>();
    ArrayList<String> list3 = new ArrayList<String>();
    ArrayList<String> list4 = new ArrayList<String>();
    ArrayList<String> list5 = new ArrayList<String>();
    ArrayList<String> list6 = new ArrayList<String>();
    ArrayList<String> list7 = new ArrayList<String>();
    ArrayList<String> list8 = new ArrayList<String>();
    ArrayList<String> list9 = new ArrayList<String>();
    ViewHolder viewHolder;
    ArrayList<String> Amount_tpp = new ArrayList<String>();
    static final String TAG_ITEMNAME = "product_name";
    static final String TAG_QTY = "total_qty";
    static final String TAG_PRICE = "MRP";
    static final String TAG_RP = "RP";
    static final String TAG_AMOUNT = "amount";
    static final String TAG_ITEM_NUMBER = "item_number";
    static final String TAG_MINQTY = "product_minqty";
    static final String TAG_PKGQTY = "product_pkgqty";
    static final String TAG_REMARKS = "product_remarks";
    HashMap<String, String> getData = new HashMap<String, String>();
    private ArrayList<HashMap<String, String>> dataAray;
    HashMap<String, String> dataIthjem;
    DataBaseHelper dbvoc;

    public interface customButtonListener {
        public void onButtonClickListner(int position, String value, View v);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    private Context context;

    public Product_AllVarient_P_Adapter(Context context, ArrayList<HashMap<String, String>> dataItem1, ArrayList<String> list1, ArrayList<String> list2, ArrayList<String> list3, ArrayList<String> list4, ArrayList<String> list5, ArrayList<String> list6, ArrayList<String> list7, ArrayList<String> list8, ArrayList<String> list9) {
        super(context, R.layout.all_varients_adapternew, dataItem1);
        this.dataAray = dataItem1;
        this.list1 = list1;
        this.list2 = list2;
        this.list3 = list3;
        this.list4 = list4;
        this.list5 = list5;
        this.list6 = list6;
        this.list7 = list7;
        this.list8 = list8;
        this.list9 = list9;

        this.context = context;
    }

//    @Override
//    public int getCount() {
//        return data.size();
//    }
//
//    @Override
//    public Product getItem(int position) {
//        return data.get(position);
//    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public boolean isEnabled(int position) {
//        if (position == 2) {
//            return false;
//        } else {
//            return true;
//        }
//    }

    @SuppressLint("ShowToast")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //final Product item = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.all_varients_adapternew, parent, false);

            holder = new ViewHolder();
            holder.mWatcher1 = new MutableWatcher1();
            holder.mWatcher2 = new MutableWatcher2();
            holder.mWatcher3 = new MutableWatcher3();
            holder.mWatcher4 = new MutableWatcher4();
            holder.mWatcher5 = new MutableWatcher5();
            holder.mWatcher6 = new MutableWatcher6();
            holder.mWatcher7 = new MutableWatcher7();
            holder.mWatcher8 = new MutableWatcher8();
            holder.Productnamerpmrp = (TextView) convertView.findViewById(R.id.Productnamerpmrp);
            holder.pidp = (TextView) convertView.findViewById(R.id.pidp);
            holder.productquantity = (EditText) convertView.findViewById(R.id.productquantityp);
            holder.edit_comment = (EditText) convertView.findViewById(R.id.edit_comment);
            holder.edit_detail1 = (EditText) convertView.findViewById(R.id.edit_detail1);
            holder.edit_detail2 = (EditText) convertView.findViewById(R.id.edit_detail2);
            holder.edit_detail3 = (EditText) convertView.findViewById(R.id.edit_detail3);
            holder.edit_detail4 = (EditText) convertView.findViewById(R.id.edit_detail4);
            holder.edit_detail5 = (EditText) convertView.findViewById(R.id.edit_detail5);

            holder.totalprice = (TextView) convertView.findViewById(R.id.totalpricep);
            holder.mrpv = (TextView) convertView.findViewById(R.id.mrpv);
            holder.mrpvnew = (TextView) convertView.findViewById(R.id.mrpvnew);
            holder.rpv = (TextView) convertView.findViewById(R.id.rpv);
            holder.scheme = (TextView) convertView.findViewById(R.id.scheme);
            //holder.edit_comments = (EditText) convertView.findViewById(R.id.editText4);

            //holder.edit_comment.setRawInputType(Configuration.KEYBOARDHIDDEN_YES);
            // holder.edit_comment.setInputType(InputType.TYPE_CLASS_TEXT);
            // holder.rpvnew = (TextView) convertView.findViewById(R.id.rpvnew);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.productquantity.setRawInputType(Configuration.KEYBOARD_QWERTY);
        holder.productquantity.addTextChangedListener(holder.mWatcher1);
        holder.mWatcher1.setActive(false);

        // holder.productquantity.setRawInputType(Configuration.KEYBOARD_QWERTY);
        holder.edit_comment.addTextChangedListener(holder.mWatcher3);
        holder.mWatcher3.setActive(false);

        holder.edit_detail1.addTextChangedListener(holder.mWatcher4);
        holder.mWatcher4.setActive(false);

        holder.edit_detail2.addTextChangedListener(holder.mWatcher5);
        holder.mWatcher5.setActive(false);

        holder.edit_detail3.addTextChangedListener(holder.mWatcher6);
        holder.mWatcher6.setActive(false);

        holder.edit_detail4.addTextChangedListener(holder.mWatcher7);
        holder.mWatcher7.setActive(false);

        holder.edit_detail5.addTextChangedListener(holder.mWatcher8);
        holder.mWatcher8.setActive(false);

        holder.totalprice.addTextChangedListener(holder.mWatcher2);
        holder.mWatcher2.setActive(false);

        getData = dataAray.get(position);

        holder.Productnamerpmrp.setText(getData.get(TAG_ITEMNAME));
        holder.pidp.setText(getData.get(TAG_ITEM_NUMBER));
        holder.mrpv.setText(getData.get(TAG_PRICE));
        holder.rpv.setText(getData.get(TAG_RP));

        // for label RP change
        SharedPreferences spf1 = context.getSharedPreferences("SimpleLogic", 0);
        String rpstr = spf1.getString("var_rp", "");
        String mrpstr = spf1.getString("var_mrp", "");

        String order_product_detail2 = spf1.getString("order_product_detail2", "");
        String order_product_detail3 = spf1.getString("order_product_detail3", "");
        String order_product_detail4 = spf1.getString("order_product_detail4", "");
        String order_product_detail5 = spf1.getString("order_product_detail5", "");
        String order_product_detail6 = spf1.getString("order_product_detail6", "");

        if (order_product_detail2.length() > 0) {
            holder.edit_detail1.setHint(order_product_detail2);
        }

        if (order_product_detail3.length() > 0) {
            holder.edit_detail2.setHint(order_product_detail3);
        }

        if (order_product_detail4.length() > 0) {
            holder.edit_detail3.setHint(order_product_detail4);
        }

        if (order_product_detail5.length() > 0) {
            holder.edit_detail4.setHint(order_product_detail5);
        }

        if (order_product_detail6.length() > 0) {
            holder.edit_detail5.setHint(order_product_detail6);
        }
//        price_str=spf1.getString("var_total_price", "");

        if (rpstr.length() > 0) {
            holder.mrpvnew.setText("[MRP] : " + getData.get(TAG_PRICE) + "\n" + "[" + rpstr + "] : " + getData.get(TAG_RP) + "\n" + "[MIN QTY] : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(getData.get(TAG_MINQTY)) + "\n" + "[PKG QTY] : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(getData.get(TAG_PKGQTY)));
        } else {
            holder.mrpvnew.setText("[MRP] : " + getData.get(TAG_PRICE) + "\n" + "[RP] : " + getData.get(TAG_RP) + "\n" + "[MIN QTY] : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(getData.get(TAG_MINQTY)) + "\n" + "[PKG QTY] : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(getData.get(TAG_PKGQTY)));
        }

        if (mrpstr.length() > 0) {
            holder.mrpvnew.setText("[" + mrpstr + "] : " + getData.get(TAG_PRICE) + "\n" + "[RP] : " + getData.get(TAG_RP) + "\n" + "[MIN QTY] : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(getData.get(TAG_MINQTY)) + "\n" + "[PKG QTY] : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(getData.get(TAG_PKGQTY)));
        } else {
            holder.mrpvnew.setText("[MRP] : " + getData.get(TAG_PRICE) + "\n" + "[RP] : " + getData.get(TAG_RP) + "\n" + "[MIN QTY] : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(getData.get(TAG_MINQTY)) + "\n" + "[PKG QTY] : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(getData.get(TAG_PKGQTY)));
        }

        // holder.mrpvnew.setText("[MRP] : " + getData.get(TAG_PRICE) + "\n" + "[RP] : " + getData.get(TAG_RP) + "\n" + "[MIN QTY] : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(getData.get(TAG_MINQTY)) + "\n" + "[PKG QTY] : " + Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(getData.get(TAG_PKGQTY)));
        //holder.rpvnew.setText("RP : " + getData.get(TAG_RP));

        holder.edit_comment.setText(list4.get(position), TextView.BufferType.EDITABLE);

        holder.edit_detail1.setText(list5.get(position), TextView.BufferType.EDITABLE);
        holder.edit_detail2.setText(list6.get(position), TextView.BufferType.EDITABLE);
        holder.edit_detail3.setText(list7.get(position), TextView.BufferType.EDITABLE);
        holder.edit_detail4.setText(list8.get(position), TextView.BufferType.EDITABLE);
        holder.edit_detail5.setText(list9.get(position), TextView.BufferType.EDITABLE);

        holder.productquantity.setText(list1.get(position), TextView.BufferType.EDITABLE);

        holder.totalprice.setText(list2.get(position), TextView.BufferType.EDITABLE);
        holder.scheme.setText(list3.get(position));

        holder.mWatcher1.setPosition(position);
        holder.mWatcher1.setActive(true);

        holder.mWatcher2.setPosition(position);
        holder.mWatcher2.setActive(true);

        holder.mWatcher3.setPosition(position);
        holder.mWatcher3.setActive(true);

        holder.mWatcher4.setPosition(position);
        holder.mWatcher4.setActive(true);

        holder.mWatcher5.setPosition(position);
        holder.mWatcher5.setActive(true);

        holder.mWatcher6.setPosition(position);
        holder.mWatcher6.setActive(true);

        holder.mWatcher7.setPosition(position);
        holder.mWatcher7.setActive(true);

        holder.mWatcher8.setPosition(position);
        holder.mWatcher8.setActive(true);

        //  holder.productquantity.setId(position);

        //  holder.totalprice.setId(position);

        //we need to update adapter once we finish with editing

        holder.productquantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                HashMap<String, String> edit = new HashMap<>();

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.productquantity.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.mrpv.getText().toString()) && Integer.parseInt(String.valueOf(s)) > 0) {
                    // edit.put("string", s.toString());

                    try {

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_comment.getText().toString())) {
                            Global_Data.Orderproduct_remarks.put(position + "&" + holder.pidp.getText().toString(), holder.edit_comment.getText().toString());
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail3.getText().toString())) {
                            Global_Data.Orderproduct_detail3.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail3.getText().toString());
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail1.getText().toString())) {
                            Global_Data.Orderproduct_detail1.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail1.getText().toString());
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail2.getText().toString())) {
                            Global_Data.Orderproduct_detail2.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail2.getText().toString());
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail4.getText().toString())) {
                            Global_Data.Orderproduct_detail4.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail4.getText().toString());
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail5.getText().toString())) {
                            Global_Data.Orderproduct_detail5.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail5.getText().toString());
                        }
                        Double value = Double.valueOf(holder.productquantity.getText().toString()) * Double.valueOf(holder.mrpv.getText().toString());

//                        if(price_str.length()>0)
//                        {
//                            holder.totalprice.setText(price_str+" : " + String.valueOf(value));
//                        }else{
//                                 holder.totalprice.setText("PRICE : " + String.valueOf(value));
//                             }
                        holder.totalprice.setText("PRICE : " + String.valueOf(value));
                        Global_Data.Order_hashmap.put(position + "&" + holder.pidp.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + holder.Productnamerpmrp.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString());

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                } else {
                    holder.totalprice.setText("");
                    Global_Data.Order_hashmap.put(position + "&" + holder.pidp.getText().toString(), "");
                }

            }
        });

        holder.edit_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_comment.getText().toString())) {
                    Global_Data.Orderproduct_remarks.put(position + "&" + holder.pidp.getText().toString(), holder.edit_comment.getText().toString());

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail3.getText().toString())) {
                        Global_Data.Orderproduct_detail3.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail3.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail1.getText().toString())) {
                        Global_Data.Orderproduct_detail1.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail1.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail2.getText().toString())) {
                        Global_Data.Orderproduct_detail2.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail2.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail4.getText().toString())) {
                        Global_Data.Orderproduct_detail4.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail4.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail5.getText().toString())) {
                        Global_Data.Orderproduct_detail5.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail5.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.productquantity.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.mrpv.getText().toString())) {
                        // edit.put("string", s.toString());
                        try {
                            Double value = Double.valueOf(holder.productquantity.getText().toString()) * Double.valueOf(holder.mrpv.getText().toString());
                            //holder.totalprice.setText("PRICE : " + String.valueOf(value));

                            Global_Data.Order_hashmap.put(position + "&" + holder.pidp.getText().toString(), holder.productquantity.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.Productnamerpmrp.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString());

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                } else {
                    Global_Data.Orderproduct_remarks.put(position + "&" + holder.pidp.getText().toString(), "");
                }
            }
        });

        holder.edit_detail1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail1.getText().toString())) {
                    Global_Data.Orderproduct_detail1.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail1.getText().toString());

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_comment.getText().toString())) {
                        Global_Data.Orderproduct_remarks.put(position + "&" + holder.pidp.getText().toString(), holder.edit_comment.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail2.getText().toString())) {
                        Global_Data.Orderproduct_detail2.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail2.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail3.getText().toString())) {
                        Global_Data.Orderproduct_detail3.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail3.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail4.getText().toString())) {
                        Global_Data.Orderproduct_detail4.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail4.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail5.getText().toString())) {
                        Global_Data.Orderproduct_detail5.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail5.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.productquantity.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.mrpv.getText().toString())) {
                        // edit.put("string", s.toString());
                        try {
                            Double value = Double.valueOf(holder.productquantity.getText().toString()) * Double.valueOf(holder.mrpv.getText().toString());
                            //holder.totalprice.setText("PRICE : " + String.valueOf(value));

                            Global_Data.Order_hashmap.put(position + "&" + holder.pidp.getText().toString(), holder.productquantity.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.Productnamerpmrp.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString());

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                } else {
                    Global_Data.Orderproduct_detail1.put(position + "&" + holder.pidp.getText().toString(), "");
                }
            }
        });

        holder.edit_detail2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail2.getText().toString())) {
                    Global_Data.Orderproduct_detail2.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail2.getText().toString());

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_comment.getText().toString())) {
                        Global_Data.Orderproduct_remarks.put(position + "&" + holder.pidp.getText().toString(), holder.edit_comment.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail1.getText().toString())) {
                        Global_Data.Orderproduct_detail1.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail1.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail3.getText().toString())) {
                        Global_Data.Orderproduct_detail3.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail3.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail4.getText().toString())) {
                        Global_Data.Orderproduct_detail4.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail4.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail5.getText().toString())) {
                        Global_Data.Orderproduct_detail5.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail5.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.productquantity.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.mrpv.getText().toString())) {
                        // edit.put("string", s.toString());
                        try {
                            Double value = Double.valueOf(holder.productquantity.getText().toString()) * Double.valueOf(holder.mrpv.getText().toString());
                            //holder.totalprice.setText("PRICE : " + String.valueOf(value));

                            Global_Data.Order_hashmap.put(position + "&" + holder.pidp.getText().toString(), holder.productquantity.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.Productnamerpmrp.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString());

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                } else {
                    Global_Data.Orderproduct_detail2.put(position + "&" + holder.pidp.getText().toString(), "");
                }
            }
        });

        holder.edit_detail3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail3.getText().toString())) {
                    Global_Data.Orderproduct_detail3.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail3.getText().toString());

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_comment.getText().toString())) {
                        Global_Data.Orderproduct_remarks.put(position + "&" + holder.pidp.getText().toString(), holder.edit_comment.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail1.getText().toString())) {
                        Global_Data.Orderproduct_detail1.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail1.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail2.getText().toString())) {
                        Global_Data.Orderproduct_detail2.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail2.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail4.getText().toString())) {
                        Global_Data.Orderproduct_detail4.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail4.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail5.getText().toString())) {
                        Global_Data.Orderproduct_detail5.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail5.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.productquantity.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.mrpv.getText().toString())) {
                        // edit.put("string", s.toString());
                        try {
                            Double value = Double.valueOf(holder.productquantity.getText().toString()) * Double.valueOf(holder.mrpv.getText().toString());
                            //holder.totalprice.setText("PRICE : " + String.valueOf(value));

                            Global_Data.Order_hashmap.put(position + "&" + holder.pidp.getText().toString(), holder.productquantity.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.Productnamerpmrp.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString());

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                } else {
                    Global_Data.Orderproduct_detail3.put(position + "&" + holder.pidp.getText().toString(), "");
                }
            }
        });

        holder.edit_detail4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail4.getText().toString())) {
                    Global_Data.Orderproduct_detail4.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail4.getText().toString());

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_comment.getText().toString())) {
                        Global_Data.Orderproduct_remarks.put(position + "&" + holder.pidp.getText().toString(), holder.edit_comment.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail1.getText().toString())) {
                        Global_Data.Orderproduct_detail1.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail1.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail2.getText().toString())) {
                        Global_Data.Orderproduct_detail2.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail2.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail3.getText().toString())) {
                        Global_Data.Orderproduct_detail3.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail3.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail5.getText().toString())) {
                        Global_Data.Orderproduct_detail5.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail5.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.productquantity.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.mrpv.getText().toString())) {
                        // edit.put("string", s.toString());
                        try {
                            Double value = Double.valueOf(holder.productquantity.getText().toString()) * Double.valueOf(holder.mrpv.getText().toString());
                            //holder.totalprice.setText("PRICE : " + String.valueOf(value));

                            Global_Data.Order_hashmap.put(position + "&" + holder.pidp.getText().toString(), holder.productquantity.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.Productnamerpmrp.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString());

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                } else {
                    Global_Data.Orderproduct_detail4.put(position + "&" + holder.pidp.getText().toString(), "");
                }
            }
        });

        holder.edit_detail5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail5.getText().toString())) {
                    Global_Data.Orderproduct_detail5.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail5.getText().toString());

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_comment.getText().toString())) {
                        Global_Data.Orderproduct_remarks.put(position + "&" + holder.pidp.getText().toString(), holder.edit_comment.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail1.getText().toString())) {
                        Global_Data.Orderproduct_detail1.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail1.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail2.getText().toString())) {
                        Global_Data.Orderproduct_detail2.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail2.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail4.getText().toString())) {
                        Global_Data.Orderproduct_detail4.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail4.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.edit_detail3.getText().toString())) {
                        Global_Data.Orderproduct_detail3.put(position + "&" + holder.pidp.getText().toString(), holder.edit_detail3.getText().toString());
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.productquantity.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.mrpv.getText().toString())) {
                        // edit.put("string", s.toString());
                        try {
                            Double value = Double.valueOf(holder.productquantity.getText().toString()) * Double.valueOf(holder.mrpv.getText().toString());
                            //holder.totalprice.setText("PRICE : " + String.valueOf(value));

                            Global_Data.Order_hashmap.put(position + "&" + holder.pidp.getText().toString(), holder.productquantity.getText().toString() + "pq" + String.valueOf(value) + "pprice" + holder.Productnamerpmrp.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString());

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                } else {
                    Global_Data.Orderproduct_detail5.put(position + "&" + holder.pidp.getText().toString(), "");
                }
            }
        });
        // holder.productquantity.setText();
        // holder.totalprice.setText(getData.get(TAG_ITEM_NUMBER));

        return convertView;
    }

    static class ViewHolder {
        TextView Productnamerpmrp, pidp, mrpv, rpv, mrpvnew, rpvnew, scheme;
        EditText productquantity, edit_comment, edit_detail1, edit_detail2, edit_detail3, edit_detail4, edit_detail5;
        ;
        TextView totalprice;
        public MutableWatcher1 mWatcher1;
        public MutableWatcher2 mWatcher2;
        public MutableWatcher3 mWatcher3;
        public MutableWatcher4 mWatcher4;
        public MutableWatcher5 mWatcher5;
        public MutableWatcher6 mWatcher6;
        public MutableWatcher7 mWatcher7;
        public MutableWatcher8 mWatcher8;
    }

    class MutableWatcher1 implements TextWatcher {
        private int mPosition;
        private boolean mActive;

        void setPosition(int position) {
            mPosition = position;
        }

        void setActive(boolean active) {
            mActive = active;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mActive) {
                list1.set(mPosition, s.toString());
            }
        }
    }

    class MutableWatcher3 implements TextWatcher {
        private int mPosition;
        private boolean mActive;

        void setPosition(int position) {
            mPosition = position;
        }

        void setActive(boolean active) {
            mActive = active;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mActive) {
                list4.set(mPosition, s.toString());
            }
        }
    }

    class MutableWatcher2 implements TextWatcher {
        private int mPosition;
        private boolean mActive;

        void setPosition(int position) {
            mPosition = position;
        }

        void setActive(boolean active) {
            mActive = active;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mActive) {
                list2.set(mPosition, s.toString());
            }
        }
    }

    class MutableWatcher4 implements TextWatcher {
        private int mPosition;
        private boolean mActive;

        void setPosition(int position) {
            mPosition = position;
        }

        void setActive(boolean active) {
            mActive = active;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mActive) {
                list5.set(mPosition, s.toString());
            }
        }
    }

    class MutableWatcher5 implements TextWatcher {
        private int mPosition;
        private boolean mActive;

        void setPosition(int position) {
            mPosition = position;
        }

        void setActive(boolean active) {
            mActive = active;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mActive) {
                list6.set(mPosition, s.toString());
            }
        }
    }

    class MutableWatcher6 implements TextWatcher {
        private int mPosition;
        private boolean mActive;

        void setPosition(int position) {
            mPosition = position;
        }

        void setActive(boolean active) {
            mActive = active;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mActive) {
                list7.set(mPosition, s.toString());
            }
        }
    }

    class MutableWatcher7 implements TextWatcher {
        private int mPosition;
        private boolean mActive;

        void setPosition(int position) {
            mPosition = position;
        }

        void setActive(boolean active) {
            mActive = active;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mActive) {
                list8.set(mPosition, s.toString());
            }
        }
    }


    class MutableWatcher8 implements TextWatcher {
        private int mPosition;
        private boolean mActive;

        void setPosition(int position) {
            mPosition = position;
        }

        void setActive(boolean active) {
            mActive = active;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mActive) {
                list9.set(mPosition, s.toString());
            }
        }
    }

}
