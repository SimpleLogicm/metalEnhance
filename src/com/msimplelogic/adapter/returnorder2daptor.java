package com.msimplelogic.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msimplelogic.activities.Check_Null_Value;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.returnordermodel;

import java.util.List;

public class returnorder2daptor extends RecyclerView.Adapter<returnorder2daptor.Holder> {
    Context context;
    List<returnordermodel> array;
    String price = "";
    public returnorder2daptor(Context context, List<returnordermodel> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public returnorder2daptor.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_returnorder2, parent, false);
        return new returnorder2daptor.Holder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull returnorder2daptor.Holder holder, int position) {

        holder.editTextRP.setText(array.get(position).getRp());
        holder.editTextMRP.setText(array.get(position).getMrp());
        holder.name.setText(array.get(position).getName());
        holder.p_code.setText(array.get(position).getCode());

        holder.editTextQuantity1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.return_order_remarks.getText().toString())) {
                    Global_Data.Orderproduct_remarks.put(position + "&" + holder.p_code.getText().toString(), holder.return_order_remarks.getText().toString());
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.editTextbatchno.getText().toString())) {
                    Global_Data.Orderproduct_detail1.put(position + "&" + holder.p_code.getText().toString(), holder.editTextbatchno.getText().toString());
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.editTextQuantity1.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.editTextMRP.getText().toString()) && Integer.parseInt(String.valueOf(editable)) > 0) {
                    // edit.put("string", s.toString());

                    try {
                        Double value = Double.valueOf(holder.editTextQuantity1.getText().toString()) * Double.valueOf(holder.editTextMRP.getText().toString());
                        holder.txtPrice1.setText(String.valueOf(value));
                        Global_Data.return_oredr.put(position + "&" + holder.p_code.getText().toString(), editable.toString() + "pq" + String.valueOf(value));

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                } else {
                    holder.txtPrice1.setText("");
                    Global_Data.return_oredr.put(position + "&" + holder.p_code.getText().toString(), "");
                }



            }
        });

        holder.editTextbatchno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {



                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.editTextbatchno.getText().toString())) {

                    try {

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.return_order_remarks.getText().toString())) {
                            Global_Data.Orderproduct_remarks.put(position + "&" + holder.p_code.getText().toString(), holder.return_order_remarks.getText().toString());
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.editTextbatchno.getText().toString())) {
                            Global_Data.Orderproduct_detail1.put(position + "&" + holder.p_code.getText().toString(), holder.editTextbatchno.getText().toString());
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.editTextQuantity1.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.editTextMRP.getText().toString())) {
                            Double value = Double.valueOf(holder.editTextQuantity1.getText().toString()) * Double.valueOf(holder.editTextMRP.getText().toString());
                            holder.txtPrice1.setText(String.valueOf(value));
                            Global_Data.return_oredr.put(position + "&" + holder.p_code.getText().toString(), holder.editTextQuantity1.getText().toString() + "pq" + String.valueOf(value));

                        }



                    } catch (Exception exception) {
                    exception.printStackTrace();
                }


                }else {
                    //holder.txtPrice1.setText("");
                    Global_Data.Orderproduct_detail1.put(position + "&" + holder.p_code.getText().toString(), "");
                }


            }
        });

        holder.return_order_remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.return_order_remarks.getText().toString())) {

                    try {

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.return_order_remarks.getText().toString())) {
                            Global_Data.Orderproduct_remarks.put(position + "&" + holder.p_code.getText().toString(), holder.return_order_remarks.getText().toString());
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.editTextbatchno.getText().toString())) {
                            Global_Data.Orderproduct_detail1.put(position + "&" + holder.p_code.getText().toString(), holder.editTextbatchno.getText().toString());
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.editTextQuantity1.getText().toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(holder.editTextMRP.getText().toString())) {
                            Double value = Double.valueOf(holder.editTextQuantity1.getText().toString()) * Double.valueOf(holder.editTextMRP.getText().toString());
                            holder.txtPrice1.setText(String.valueOf(value));
                            Global_Data.return_oredr.put(position + "&" + holder.p_code.getText().toString(), holder.editTextQuantity1.getText().toString() + "pq" + String.valueOf(value));

                        }



                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }


                }else {
                    //holder.txtPrice1.setText("");
                    Global_Data.Orderproduct_detail1.put(position + "&" + holder.p_code.getText().toString(), "");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView editTextRP,editTextMRP,name,p_code;
        EditText editTextQuantity1,editTextbatchno,txtPrice1,return_order_remarks;
        public Holder(@NonNull View itemView) {
            super(itemView);

            editTextRP=itemView.findViewById(R.id.editTextRP);
            editTextMRP=itemView.findViewById(R.id.editTextMRP);
            name=itemView.findViewById(R.id.name);
            p_code=itemView.findViewById(R.id.p_code);
            editTextQuantity1=itemView.findViewById(R.id.editTextQuantity1);
            editTextbatchno=itemView.findViewById(R.id.editTextbatchno);
            txtPrice1=itemView.findViewById(R.id.txtPrice1);
            return_order_remarks=itemView.findViewById(R.id.return_order_remarks);

        }
    }

    public void put(){



    }
}
