package com.msimplelogic.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.msimplelogic.activities.DataBaseHelper;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.SpinerListModel;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapterStock extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> data;
    Context mcontext;
    SpinnerListAdapter spinner_list_adapter;
    RelativeLayout list_container_stock;
    RecyclerView recyclerView;
    //RelativeLayout searchLout;
    Spinner spnBusinessDiv, spnCategory, spnProduct,spnBu;
    DataBaseHelper dbvoc;
    Button up_stock_search;

    public AutoCompleteAdapterStock(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.data = new ArrayList<>();
        this.mcontext = context;
        dbvoc = new DataBaseHelper(mcontext);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                list_container_stock = ((Activity) mcontext).findViewById(R.id.list_container_stock);
                recyclerView = ((Activity) mcontext).findViewById(R.id.spinner_recycleview_stock);
                spnCategory = ((Activity) mcontext).findViewById(R.id.up_stock_spnCategory);
                spnProduct = ((Activity) mcontext).findViewById(R.id.up_stock_spnProduct);
                up_stock_search = ((Activity) mcontext).findViewById(R.id.up_stock_search);
                //spnBu = ((Activity) mcontext).findViewById(R.id.spnBu);
               // hidden_buttonlayout = ((Activity) mcontext).findViewById(R.id.hidden_buttonlayout_stock);
                //searchLout = ((Activity) mcontext).findViewById(R.id.search_lout);
                if (constraint != null) {
                    try {
                        Log.d("SearchS","SearchS "+constraint.toString());
                        ArrayList<String> suggestions = new ArrayList<>();

//                        List<Local_Data> contacts2 = dbvoc.getAllVariant();
//                        //results2.add("Select All");
//                        //result_product.add("Select All");
//                        for (Local_Data cn : contacts2) {
//                            results2.add(cn.getStateName());
//                            //result_product.add(cn.getStateName());
//                        }

                        List<SpinerListModel> cont1 = dbvoc.varientserch(constraint.toString());

                        if (cont1.size() > 0) {
                            Global_Data.spiner_list_modelList.clear();
                            for (SpinerListModel cnt1 : cont1) {
                                SpinerListModel spiner_list_model = new SpinerListModel();
//                                spiner_list_model.setBusiness_category("");
//                                spiner_list_model.setBusiness_unit("");
                                spiner_list_model.setPrimary_category("");
                                spiner_list_model.setProduct_variant(cnt1.getProduct_variant());
                                spiner_list_model.setSub_category("");
                                spiner_list_model.setCode(cnt1.getCode());
                                spiner_list_model.setSelected(false);

                                Global_Data.spiner_list_modelList.add(spiner_list_model);

                                String pro_varient = cnt1.getProduct_variant();
                                String term = pro_varient;
                                suggestions.add(term);
                            }


                            results.values = Global_Data.spiner_list_modelList;
                            results.count = Global_Data.spiner_list_modelList.size();

                            ((Activity) mcontext).runOnUiThread(new Runnable() {
                                public void run() {

                                    //searchLout.setVisibility(View.VISIBLE);
                                    list_container_stock.setVisibility(View.VISIBLE);
                                    //hidden_buttonlayout.setVisibility(View.VISIBLE);


                                    spnCategory.setVisibility(View.GONE);
                                    spnProduct.setVisibility(View.GONE);
                                    up_stock_search.setVisibility(View.GONE);

                                    //spnBu.setVisibility(View.GONE);
                                    AutoCompleteTextView s = ((Activity) mcontext).findViewById(R.id.up_stock_Product_Variant);
                                    s.dismissDropDown();
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));
                                    spinner_list_adapter = new SpinnerListAdapter(mcontext, Global_Data.spiner_list_modelList);
                                    recyclerView.setAdapter(spinner_list_adapter);

                                }
                            });


                            data = suggestions;
                        } else {
                            ((Activity) mcontext).runOnUiThread(new Runnable() {
                                public void run() {
                                    recyclerView.setVisibility(View.GONE);
                                    list_container_stock.setVisibility(View.GONE);
                                    spnCategory.setVisibility(View.VISIBLE);
                                    spnProduct.setVisibility(View.VISIBLE);
                                    up_stock_search.setVisibility(View.VISIBLE);
                                    //spnBu.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    } catch (Exception ex) {
                        ((Activity) mcontext).runOnUiThread(new Runnable() {
                            public void run() {
                                //recyclerView.setVisibility(View.GONE);
                                list_container_stock.setVisibility(View.GONE);
                                spnCategory.setVisibility(View.VISIBLE);
                                spnProduct.setVisibility(View.VISIBLE);
                                up_stock_search.setVisibility(View.VISIBLE);

                            }
                        });
                        ex.printStackTrace();
                    } finally {


                    }
                } else {
                    ((Activity) mcontext).runOnUiThread(new Runnable() {
                        public void run() {
                            //recyclerView.setVisibility(View.GONE);
                            list_container_stock.setVisibility(View.GONE);
                            spnCategory.setVisibility(View.VISIBLE);
                            spnProduct.setVisibility(View.VISIBLE);
                            up_stock_search.setVisibility(View.VISIBLE);

                        }
                    });
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else notifyDataSetInvalidated();
            }
        };
    }

}
