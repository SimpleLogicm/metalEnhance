package com.msimplelogic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.Message;

import java.util.List;


public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    private List<Message> mMessages;
    private int[] mUsernameColors;

    MyCallBack myCallback;

    public interface MyCallBack{
        void listenerMethod(ProgressBar progressBar);
    }

    public ChatMessageAdapter(Context context, List<Message> messages) {
        mMessages = messages;
        mUsernameColors = context.getResources().getIntArray(R.array.username_colors);
        //this.myCallback = myCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;
        switch (viewType) {
            case Message.TYPE_MESSAGE:
                layout = R.layout.item_message;
                break;
            case Message.TYPE_RESPONSE:
                layout = R.layout.chatdemo_pricedapter;
                break;
            case Message.TYPE_ACTION:
                layout = R.layout.item_action;
                break;
        }
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Message message = mMessages.get(position);
        viewHolder.setMessage(message.getMessage());
        viewHolder.setUsername(message.getUsername());
        viewHolder.setRP(message.getRP());

//        if(message.getType() == 1)
//        {
//            viewHolder.c_progress.setVisibility(View.GONE);
//        }

    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mUsernameView;
        private TextView mMessageView;
        private TextView messageRP;
        private ProgressBar c_progress;
        public ViewHolder(View itemView) {
            super(itemView);

            mUsernameView =  itemView.findViewById(R.id.username);
            mMessageView =  itemView.findViewById(R.id.message);
            messageRP =  itemView.findViewById(R.id.messageRP);
            c_progress =  itemView.findViewById(R.id.c_progress);

        }

        public void setUsername(String username) {
            if (null == mUsernameView) return;
            mUsernameView.setText(username);

            try
            {
                // mUsernameView.setTextColor(getUsernameColor(username));
            }catch (Exception ex)
            {
                // mUsernameView.setTextColor(getUsernameColor(username));
                ex.printStackTrace();
            }

        }

        public void setRP(String RP) {
            if (null == messageRP) return;
            messageRP.setText(RP);

            try
            {
                // mUsernameView.setTextColor(getUsernameColor(username));
            }catch (Exception ex)
            {
                // mUsernameView.setTextColor(getUsernameColor(username));
                ex.printStackTrace();
            }

        }

        public void setMessage(String message) {
            if (null == mMessageView) return;
            mMessageView.setText(message);
        }

        private int getUsernameColor(String username) {
            int hash = 7;
            for (int i = 0, len = username.length(); i < len; i++) {
                hash = username.codePointAt(i) + (hash << 5) - hash;
            }
            int index = Math.abs(hash % mUsernameColors.length);
            return mUsernameColors[index];
        }
    }
}
