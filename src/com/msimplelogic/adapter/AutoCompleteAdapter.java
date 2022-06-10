package com.msimplelogic.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
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

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> data;
    Context mcontext;
    SpinnerListAdapter spinner_list_adapter;
    RecyclerView recyclerView;
    //RelativeLayout searchLout;
    Spinner spnBusinessDiv, spnCategory, spnProduct,spnBu;
    DataBaseHelper dbvoc;
    androidx.appcompat.widget.AppCompatButton list_ok,show_list_fromsearch,list_cancel;

    public AutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource) {
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
                recyclerView = ((Activity) mcontext).findViewById(R.id.search_spinner_recycleview);
                //spnBusinessDiv = ((Activity) mcontext).findViewById(R.id.spnBusinessDiv);
//                spnCategory = ((Activity) mcontext).findViewById(R.id.spnCategory);
//                spnProduct = ((Activity) mcontext).findViewById(R.id.spnProduct);
                //spnBu = ((Activity) mcontext).findViewById(R.id.spnBu);
                list_ok = ((Activity) mcontext).findViewById(R.id.list_ok);
                list_cancel = ((Activity) mcontext).findViewById(R.id.list_cancel);
              //  show_list_fromsearch = ((Activity) mcontext).findViewById(R.id.show_list_fromsearch);
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

//                                    View view = ((Activity)mcontext).getCurrentFocus();
//                                    if (view != null) {
//                                        InputMethodManager imm = (InputMethodManager)mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
//                                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                                    }

                                    //searchLout.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    list_ok.setVisibility(View.VISIBLE);
                                    list_cancel.setVisibility(View.VISIBLE);
                                 //   show_list_fromsearch.setVisibility(View.VISIBLE);
                                   // spnBusinessDiv.setVisibility(View.GONE);
//                                    spnCategory.setVisibility(View.GONE);
//                                    spnProduct.setVisibility(View.GONE);
                                    //spnBu.setVisibility(View.GONE);
                                    AutoCompleteTextView s = ((Activity) mcontext).findViewById(R.id.Product_Variant_search);
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
                                    list_ok.setVisibility(View.GONE);
                                    list_cancel.setVisibility(View.GONE);
                                 //   show_list_fromsearch.setVisibility(View.GONE);
                                    //searchLout.setVisibility(View.GONE);
                                    //spnBusinessDiv.setVisibility(View.VISIBLE);
//                                    spnCategory.setVisibility(View.VISIBLE);
//                                    spnProduct.setVisibility(View.VISIBLE);
                                    //spnBu.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    } catch (Exception ex) {
                        ((Activity) mcontext).runOnUiThread(new Runnable() {
                            public void run() {
                                recyclerView.setVisibility(View.GONE);
                                list_ok.setVisibility(View.GONE);
                                list_cancel.setVisibility(View.GONE);
                               // show_list_fromsearch.setVisibility(View.GONE);
                                //searchLout.setVisibility(View.GONE);
                                //spnBusinessDiv.setVisibility(View.VISIBLE);
//                                spnCategory.setVisibility(View.VISIBLE);
//                                spnProduct.setVisibility(View.VISIBLE);
                                //spnBu.setVisibility(View.VISIBLE);
                            }
                        });
                        ex.printStackTrace();
                    } finally {


                    }
                } else {
                    ((Activity) mcontext).runOnUiThread(new Runnable() {
                        public void run() {
                            recyclerView.setVisibility(View.GONE);
                            list_ok.setVisibility(View.GONE);
                            list_cancel.setVisibility(View.GONE);
                          //  show_list_fromsearch.setVisibility(View.GONE);
                            //searchLout.setVisibility(View.GONE);
                            //spnBusinessDiv.setVisibility(View.VISIBLE);
//                            spnCategory.setVisibility(View.VISIBLE);
//                            spnProduct.setVisibility(View.VISIBLE);
                            //spnBu.setVisibility(View.VISIBLE);
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
