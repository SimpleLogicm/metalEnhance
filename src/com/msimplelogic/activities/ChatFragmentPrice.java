package com.msimplelogic.activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.msimplelogic.adapter.ChatMessageAdapter;
import com.msimplelogic.model.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.socket.emitter.Emitter;

import static android.app.Activity.RESULT_OK;
import static com.msimplelogic.activities.Config.CHAT_SERVER_URL_KEY;

/**
 * A chat fragment containing messages view and input form.
 */
public class ChatFragmentPrice extends Fragment implements TextToSpeech.OnInitListener{

    private static final String TAG = "ChatFragmentPrice";

    private static final int REQUEST_LOGIN = 0;

    private static final int TYPING_TIMER_LENGTH = 600;

    private RecyclerView mMessagesView;
    private ImageView mic_click;
    private EditText mInputMessageView;
    // private ProgressBar c_progress2;
    private ProgressBar c_progress;
    private List<Message> mMessages = new ArrayList<Message>();
    private RecyclerView.Adapter mAdapter;
    private boolean mTyping = false;
    private Handler mTypingHandler = new Handler();
    // private String mUsername;
    //private Socket mSocket;

    private Boolean isConnected = true;

    DataBaseHelper dbvoc;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private String ss_token = "";

    public ChatFragmentPrice() {
        super();
    }

    private TextToSpeech tts;


    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAdapter = new ChatMessageAdapter(context, mMessages);
        if (context instanceof Activity) {
            //this.listener = (MainActivity) context;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        tts = new TextToSpeech(getActivity(), this);
        dbvoc = new DataBaseHelper(getActivity());
        // ss_token =  getActivity().getIntent().getExtras().getString("Token");

        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .username("").message("Waiting For Input").progessd("true").build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);



        // mAdapter.

        //AppController app = (AppController) getActivity().getApplication();
//        mSocket = app.getSocket();
//        mSocket.on(Socket.EVENT_CONNECT, onConnect);
//        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
////        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
////        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
////        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//        mSocket.on("message", onNewMessage);
//        // mSocket.on("user joined", onUserJoined);
//        // mSocket.on("user left", onUserLeft);
//        //  mSocket.on("typing", onTyping);
//        //  mSocket.on("stop typing", onStopTyping);
//        mSocket.connect();

        //startSignIn();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chatfragmentprice, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

//        mSocket.disconnect();
//
//        mSocket.off(Socket.EVENT_CONNECT, onConnect);
//        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
////        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
////        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//        mSocket.off("message", onNewMessage);
//        mSocket.off("user joined", onUserJoined);
//        mSocket.off("user left", onUserLeft);
//        mSocket.off("typing", onTyping);
//        mSocket.off("stop typing", onStopTyping);
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                // btnSpeak.setEnabled(true);

            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }
    private void speakOut(String text) {

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        mMessagesView = (RecyclerView) view.findViewById(R.id.messages);
        mic_click = view.findViewById(R.id.mic_click);
        mMessagesView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMessagesView.setAdapter(mAdapter);

        mInputMessageView = (EditText) view.findViewById(R.id.message_input);
        c_progress = (ProgressBar) view.findViewById(R.id.c_progress);
        //c_progress2 = (ProgressBar) view.findViewById(R.id.c_progress2);
        c_progress.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.white),PorterDuff.Mode.SRC_IN);

        attemptSend();


        mInputMessageView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == R.id.send || id == EditorInfo.IME_NULL) {
                    //  attemptSend();
                    return true;
                }
                return false;
            }
        });
        mInputMessageView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // if (null == mUsername) return;
//                if (!mSocket.connected()) return;
//
//                if (!mTyping) {
//                    mTyping = true;
//                    mSocket.emit("typing");
//                }

                mTypingHandler.removeCallbacks(onTypingTimeout);
                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ImageButton sendButton = (ImageButton) view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Global_Data.hideSoftKeyboard(getActivity());
                attemptSend();
            }
        });

        mic_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });




//        mInputMessageView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int DRAWABLE_LEFT = 0;
//                final int DRAWABLE_TOP = 1;
//                final int DRAWABLE_RIGHT = 2;
//                final int DRAWABLE_BOTTOM = 3;
//
//                if (event.getAction() == MotionEvent.ACTION_UP) {
////                    if (event.getRawX() >= (mInputMessageView.getRight() - mInputMessageView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
////
////
////                        return true;
////                    }
////                    else
//                        if (event.getRawX() >= (mInputMessageView.getLeft() - mInputMessageView.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
//
//
//                        promptSpeechInput();
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {


                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);



                    // Toast.makeText(getActivity(), result.get(0), Toast.LENGTH_SHORT).show();
                    // c_progress.setVisibility(View.VISIBLE);
//                    c_progress.getIndeterminateDrawable().setColorFilter(getResources()
//                            .getColor(R.color.primarycolor),PorterDuff.Mode.SRC_IN);
                    //  c_progress2.setVisibility(View.INVISIBLE);
                    Global_Data.hideSoftKeyboard(getActivity());
                    attemptSendVoice(result.get(0));



                    // txtSpeechInput.setText(result.get(0));
                }
                break;
            }
            default:
            {
                if (RESULT_OK != resultCode) {
                    getActivity().finish();
                    return;
                }

//                mUsername = data.getStringExtra("username");
//                int numUsers = data.getIntExtra("numUsers", 1);

//                addLog(getResources().getString(R.string.message_welcome));
//                addParticipantsLog(numUsers);
            }

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_leave) {
//            leave();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void addLog(String message) {
        mMessages.add(new Message.Builder(Message.TYPE_RESPONSE)
                .message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void addParticipantsLog(int numUsers) {
        addLog(getResources().getQuantityString(R.plurals.message_participants, numUsers, numUsers));
    }

    private void addMessage(String username, String message,String type_flag,String rp) {
        if(mMessages.get(0).getMessage().equalsIgnoreCase("Waiting For Input"))
        {
            mMessages.clear();
            if(type_flag.equalsIgnoreCase("user"))
            {
                mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                        .username(username).message(message).RP(rp).build());
                mAdapter.notifyItemInserted(mMessages.size() - 1);
                mAdapter.notifyDataSetChanged();

                scrollToBottom();
            }
            else
            {
                mMessages.add(new Message.Builder(Message.TYPE_RESPONSE)
                        .username(username).message(message).RP(rp).progessd("false").build());
                mAdapter.notifyItemInserted(mMessages.size() - 1);

                // int position = mMessages.size();
                // RecyclerView.ViewHolder viewHolder = mMessagesView.findViewHolderForItemId(position);
                //View view = viewHolder.itemView;

                // RecyclerView.ViewHolder viewHolder2 = mMessagesView.findViewHolderForItemId(position-1);
                //View view2 = viewHolder2.itemView;

                // ProgressBar c_progress = (ProgressBar) view.findViewById(R.id.c_progress);
                // ProgressBar c_progress2 = (ProgressBar) view2.findViewById(R.id.c_progress);

                //c_progress.setVisibility(View.GONE);
                // c_progress2.setVisibility(View.GONE);

                mAdapter.notifyDataSetChanged();
                scrollToBottom();
            }

        }
        else
        {
            if(type_flag.equalsIgnoreCase("user"))
            {
                mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                        .username(username).message(message).RP(rp).progessd("true").build());
                mAdapter.notifyItemInserted(mMessages.size() - 1);

//                try {
//                    String newValue = "false";
//                    int updateIndex = mMessages.size()-2;
//                    mMessages.set(updateIndex, new Message.Builder(Message.TYPE_MESSAGE).progessd("false").build());
//                    mAdapter.notifyItemChanged(updateIndex);
//                }
//                catch (Exception ex)
//                {
//                    ex.printStackTrace();
//                }
                scrollToBottom();
            }
            else
            {
                mMessages.add(new Message.Builder(Message.TYPE_RESPONSE)
                        .username(username).message(message).RP(rp).progessd("false").build());
                mAdapter.notifyItemInserted(mMessages.size() - 1);

//                try {
//                    String newValue = "false";
//                    int updateIndex = mMessages.size()-2;
//                    mMessages.set(updateIndex, new Message.Builder(Message.TYPE_MESSAGE).progessd("false").build());
//                    mAdapter.notifyItemChanged(updateIndex);
//                }
//                catch (Exception ex)
//                {
//                    ex.printStackTrace();
//                }
                scrollToBottom();
            }

        }

    }

    private void addTyping(String username) {
        mMessages.add(new Message.Builder(Message.TYPE_ACTION)
                .username(username).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void removeTyping(String username) {
        for (int i = mMessages.size() - 1; i >= 0; i--) {
            Message message = mMessages.get(i);
            if (message.getType() == Message.TYPE_ACTION && message.getUsername().equals(username)) {
                mMessages.remove(i);
                mAdapter.notifyItemRemoved(i);
            }
        }
    }

    private void attemptSend() {
        // if (null == mUsername) return;
       // if (!mSocket.connected()) return;

        mTyping = false;
        String message="";
        if(!Global_Data.Voice_value.equalsIgnoreCase(""))
        {
             message = Global_Data.Voice_value;
             Global_Data.Voice_value = "";
        }
        else
        {

            message = mInputMessageView.getText().toString().trim();
        }

        if (TextUtils.isEmpty(message)) {
            mInputMessageView.requestFocus();
            return;
        }

        c_progress.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.primarycolor),PorterDuff.Mode.SRC_IN);

        //c_progress.setVisibility(View.VISIBLE);

        //c_progress2.setVisibility(View.INVISIBLE);
        mInputMessageView.setText("");
        addMessage("", message,"user","");
        StringBuilder ss = new StringBuilder();
        List<Local_Data> contacts3 = dbvoc.getProductByCat(message.trim());
        if (contacts3.size() <= 0 ) {
            String[] splited_v = message.split("\\s+");
            for(int i=0;i<splited_v.length;i++)
            {
                ss.append('"' +splited_v[i]+ '"');
                if((splited_v.length-1) != i)
                {
                    ss.append(",");
                }
            }

            List<Local_Data> INOP = dbvoc.getProductByINOP(ss.toString().trim());
            if (INOP.size() <= 0 ) {

                for(int i=0;i<splited_v.length;i++)
                {
                    List<Local_Data> VirtualReSult = dbvoc.getProductByVoiceString(splited_v[i].trim());
                    if (VirtualReSult.size() <= 0 ) {
                        if((splited_v.length-1) == i)
                        {
                            addMessage("", "Sorry Record Not Found.","user","");
                            c_progress.getIndeterminateDrawable().setColorFilter(getResources()
                                    .getColor(R.color.white),PorterDuff.Mode.SRC_IN);
                            break;
                        }
                        continue;
                    }
                    else
                    {
//                        if (VirtualReSult.size() > 0 ) {
//                            break outerloop;
//                        }
                        if(VirtualReSult.size() < 6)
                        {
                            for (Local_Data cn : VirtualReSult) {

                                addMessage(cn.getProduct_nm(), cn.getMRP(),"response",cn.getRR());
                            }

                            c_progress.getIndeterminateDrawable().setColorFilter(getResources()
                                    .getColor(R.color.white),PorterDuff.Mode.SRC_IN);
                        }
//                        else
//                        {
//                            addMessage("", "Sorry Record Not Found.","response");
//                            c_progress.getIndeterminateDrawable().setColorFilter(getResources()
//                                    .getColor(R.color.white),PorterDuff.Mode.SRC_IN);
//                        }

                    }
                }

            }
            else
            {
                for (Local_Data cn : INOP) {

                    addMessage(cn.getProduct_nm(), cn.getMRP(),"response",cn.getRR());
                }

                c_progress.getIndeterminateDrawable().setColorFilter(getResources()
                        .getColor(R.color.white),PorterDuff.Mode.SRC_IN);
            }
        } else {
            for (Local_Data cn : contacts3) {

                addMessage(cn.getProduct_nm(), cn.getMRP(),"response",cn.getStateName());
                }

            c_progress.getIndeterminateDrawable().setColorFilter(getResources()
                    .getColor(R.color.white),PorterDuff.Mode.SRC_IN);


        }

       // SendData_MLTool(message);

        // perform the sending message attempt.

//        JSONObject obj = new JSONObject();
//        try {
//            // obj.put("hello", "server");
//            // obj.put("binary", new byte[42]);
//            obj.put("type", "notification");
//            obj.put("number", "021113");
//            obj.put("rname", "5e338d29ce872");
//            obj.put("msg", message);
//            obj.put("key", "5e5366_5et179");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        mSocket.emit("Hello", obj);
//        Log.d("Send Object","SO"+obj.toString());

        // mSocket.emit("new message", message);
    }

    private void attemptSendVoice(String voiceText) {
        // if (null == mUsername) return;
       // if (!mSocket.connected()) return;

        mTyping = false;

        String message = voiceText;

        mInputMessageView.setText("");

        c_progress.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.primarycolor),PorterDuff.Mode.SRC_IN);

        //c_progress.setVisibility(View.VISIBLE);

        //c_progress2.setVisibility(View.INVISIBLE);
        mInputMessageView.setText("");
        addMessage("", message,"user","");
        StringBuilder ss = new StringBuilder();
        List<Local_Data> contacts3 = dbvoc.getProductByCat(message.trim());
        if (contacts3.size() <= 0 ) {
            String[] splited_v = message.split("\\s+");
            for(int i=0;i<splited_v.length;i++)
            {
                ss.append('"' +splited_v[i]+ '"');
                if((splited_v.length-1) != i)
                {
                    ss.append(",");
                }
            }

            List<Local_Data> INOP = dbvoc.getProductByINOP(ss.toString().trim());
            if (INOP.size() <= 0 ) {

                for(int i=0;i<splited_v.length;i++)
                {
                    List<Local_Data> VirtualReSult = dbvoc.getProductByVoiceString(splited_v[i].trim());
                    if (VirtualReSult.size() <= 0 ) {
                        if((splited_v.length-1) == i)
                        {
                            addMessage("", "Sorry Record Not Found.","user","");
                            c_progress.getIndeterminateDrawable().setColorFilter(getResources()
                                    .getColor(R.color.white),PorterDuff.Mode.SRC_IN);
                            break;
                        }
                        continue;
                    }
                    else
                    {
//                        if (VirtualReSult.size() > 0 ) {
//                            break outerloop;
//                        }
                        if(VirtualReSult.size() < 6)
                        {
                            for (Local_Data cn : VirtualReSult) {

                                addMessage(cn.getProduct_nm(), cn.getMRP(),"response",cn.getRR());
                            }

                            c_progress.getIndeterminateDrawable().setColorFilter(getResources()
                                    .getColor(R.color.white),PorterDuff.Mode.SRC_IN);
                        }
//                        else
//                        {
//                            addMessage("", "Sorry Record Not Found.","response");
//                            c_progress.getIndeterminateDrawable().setColorFilter(getResources()
//                                    .getColor(R.color.white),PorterDuff.Mode.SRC_IN);
//                        }

                    }
                }

            }
            else
            {
                for (Local_Data cn : INOP) {

                    addMessage(cn.getProduct_nm(), cn.getMRP(),"response",cn.getRR());
                }

                c_progress.getIndeterminateDrawable().setColorFilter(getResources()
                        .getColor(R.color.white),PorterDuff.Mode.SRC_IN);
            }
        } else {
            for (Local_Data cn : contacts3) {

                addMessage(cn.getProduct_nm(), cn.getMRP(),"response",cn.getStateName());
            }

            c_progress.getIndeterminateDrawable().setColorFilter(getResources()
                    .getColor(R.color.white),PorterDuff.Mode.SRC_IN);


        }

        //SendData_MLTool(message);


    }

    private void startSignIn() {
        // mUsername = null;
        // Intent intent = new Intent(getActivity(), LoginActivity.class);
        // startActivityForResult(intent, REQUEST_LOGIN);
    }

    private void leave() {
        // mUsername = null;
        // mSocket.disconnect();
        //  mSocket.connect();
        // startSignIn();
    }

    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isConnected) {
                            // if (null != mUsername)


                            Online_Catalogue();

                            //mSocket.emit("type", "Helo");
                            // mSocket.emit("", "Helo");

                            isConnected = true;
                        }
                    }
                });
            }

        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "diconnected");
                    isConnected = false;
                  //  Toast.makeText(getActivity().getApplicationContext(), R.string.disconnect, Toast.LENGTH_LONG).show();
                    Global_Data.Custom_Toast(getActivity().getApplicationContext(), "Disconnected, Please check your internet connection","yes");

                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "Error connecting");
//                    Toast.makeText(getActivity().getApplicationContext(),
//                            R.string.error_connect, Toast.LENGTH_LONG).show();
                    Global_Data.Custom_Toast(getActivity().getApplicationContext(),"Failed to connect","");
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("msg");
                        // message = data.getString("output") + " " + data.getString("voutput");
                        message = data.getString("output");

                        try
                        {
                            speakOut(data.getString("output"));
                            if (data.has("cat")) {
                                ValidateMessage(data.getString("msg"),data.getString("cat"));
                            }

                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }


                        // Validate_MLToolData(username);
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }
                    //  c_progress.setVisibility(View.INVISIBLE);
                    c_progress.getIndeterminateDrawable().setColorFilter(getResources()
                            .getColor(R.color.white),PorterDuff.Mode.SRC_IN);
                    //c_progress2.setVisibility(View.VISIBLE);
                    // removeTyping(username);
                    addMessage(username, message,"server","");
                }
            });
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }

                    addLog(getResources().getString(R.string.message_user_joined, username));
                    addParticipantsLog(numUsers);
                }
            });
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }

                    addLog(getResources().getString(R.string.message_user_left, username));
                    addParticipantsLog(numUsers);
                    removeTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }
                    addTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }
                    removeTyping(username);
                }
            });
        }
    };

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;

            mTyping = false;
            //mSocket.emit("stop typing");
        }
    };

    public void Online_Catalogue() {
        String socket_domain = getResources().getString(R.string.socket_domain);

        Log.d("Server url", "Server url" + socket_domain+"connect1");

        final String url = socket_domain+"connect1";


        JSONArray COLLECTION_RECJARRAY = new JSONArray();
        //JSONObject product_value = new JSONObject();
        JSONObject COLLECTION_RECJOBJECT = new JSONObject();



        JSONObject INNER_CASH_JOB = new JSONObject();
        try {
            INNER_CASH_JOB.put("key", CHAT_SERVER_URL_KEY);
//            INNER_CASH_JOB.put("code", "x3d35v");
//            INNER_CASH_JOB.put("chatid", "0");
            INNER_CASH_JOB.put("project_id", "96");
            INNER_CASH_JOB.put("user_id", "14");
            INNER_CASH_JOB.put("token", "71672d33b6d1ef617c12f4c6937ee76ca7baa06086");
            // COLLECTION_RECJARRAY.put(INNER_CASH_JOB);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            COLLECTION_RECJOBJECT.put("Input", INNER_CASH_JOB);
            Log.d("COLLECTION OBJECT", COLLECTION_RECJOBJECT.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        // queue.add(stringRequest);



        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,INNER_CASH_JOB,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("Response", "resdponse" + response);



                        try {

                            // JSONObject json = new JSONObject(response);    // create JSON obj from string
                            JSONObject json2 = response.getJSONObject("cData");
                            String msg = json2.getString("msg");
                            String room = json2.getString("room");
                            String powered = json2.getString("powered");
                            // token_f =  json2.getString("room");

                            JSONObject obj = new JSONObject();
                            try {
                                // obj.put("hello", "server");
                                // obj.put("binary", new byte[42]);
                                obj.put("type", msg);
                                obj.put("rname", room);
                                obj.put("session", "join");
                                obj.put("key", CHAT_SERVER_URL_KEY);

//                                mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
//                                        .username(powered).message(msg).build());
//                                mAdapter.notifyItemInserted(mMessages.size() - 1);
//                                scrollToBottom();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //mSocket.emit("broadcast", obj);
                            Log.d("Broadcast Object","BO"+obj.toString());

                            if (getActivity() != null) {
//                                Toast.makeText(getActivity().getApplicationContext(),
//                                        R.string.connect, Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getActivity().getApplicationContext(),"Connected","");
                            }


//                            if (products.length() > 0) {
//                                for (int i = 0; i < products.length(); i++) {
//                                    JSONObject object = products.getJSONObject(i);
//
//                                    String msg = object.getString("msg");
//                                    String room = object.getString("room");
//                                }
//                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  finish();

                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
//        {
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                headers.put("key", CHAT_SERVER_URL_KEY);
//                headers.put("code", "x3d35v");
//                headers.put("chatid", "0");
//                headers.put("token", "12345");
//
//                return headers;
//            }
//        };
        queue.add(postRequest);
    }

    public void ValidateMessage(String rmsg,String pcode) {

        String socket_domain = getResources().getString(R.string.socket_domain);

        Log.d("Server url", "Server url" + socket_domain+"getmsg");
        final String url =  socket_domain+"getmsg";


        JSONArray COLLECTION_RECJARRAY = new JSONArray();
        //JSONObject product_value = new JSONObject();
        JSONObject COLLECTION_RECJOBJECT = new JSONObject();



        JSONObject INNER_CASH_JOB = new JSONObject();
        try {
            INNER_CASH_JOB.put("rmsg", rmsg);
            INNER_CASH_JOB.put("pcode", pcode);
            INNER_CASH_JOB.put("key", CHAT_SERVER_URL_KEY);
            INNER_CASH_JOB.put("project_id", "96");
            INNER_CASH_JOB.put("user_id", "14");
            INNER_CASH_JOB.put("chattime", "7.90");
            INNER_CASH_JOB.put("token", "71672d33b6d1ef617c12f4c6937ee76ca7baa06086");
            // COLLECTION_RECJARRAY.put(INNER_CASH_JOB);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            COLLECTION_RECJOBJECT.put("Input", INNER_CASH_JOB);
            Log.d("COLLECTION OBJECT", COLLECTION_RECJOBJECT.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        // queue.add(stringRequest);



        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,INNER_CASH_JOB,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("Response", "resdponse" + response);



                        try {

                            // JSONObject json = new JSONObject(response);    // create JSON obj from string
                            JSONObject json2 = response.getJSONObject("Data");
                            String msg = json2.getString("rmsg");


                            Log.d("Validate Response","msg"+msg);
                            // token_f =  json2.getString("room");


//                                scrollToBottom();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




//                            if (products.length() > 0) {
//                                for (int i = 0; i < products.length(); i++) {
//                                    JSONObject object = products.getJSONObject(i);
//
//                                    String msg = object.getString("msg");
//                                    String room = object.getString("room");
//                                }
//                            }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
//        {
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                headers.put("key", CHAT_SERVER_URL_KEY);
//                headers.put("code", "x3d35v");
//                headers.put("chatid", "0");
//                headers.put("token", "12345");
//
//                return headers;
//            }
//        };
        queue.add(postRequest);
    }

    public void SendData_MLTool(final String message_text) {


        String socket_domain = getResources().getString(R.string.socket_domain);

        Log.d("Server url", "Server url" + socket_domain+"chatbotmsg");

        final String url = socket_domain+"chatbotmsg";



        JSONArray COLLECTION_RECJARRAY = new JSONArray();
        //JSONObject product_value = new JSONObject();
        JSONObject COLLECTION_RECJOBJECT = new JSONObject();

        JSONObject INNER_CASH_JOB = new JSONObject();
        try {
            INNER_CASH_JOB.put("key", CHAT_SERVER_URL_KEY);
            INNER_CASH_JOB.put("sendmsg", message_text);
            INNER_CASH_JOB.put("chattime", "7.88");
            INNER_CASH_JOB.put("lasttime", "1582541452");
            INNER_CASH_JOB.put("project_id", "96");
            INNER_CASH_JOB.put("user_id", "14");
            INNER_CASH_JOB.put("token", "71672d33b6d1ef617c12f4c6937ee76ca7baa06086");
            //INNER_CASH_JOB.put("token", ss_token);
            COLLECTION_RECJARRAY.put(INNER_CASH_JOB);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            COLLECTION_RECJOBJECT.put("Input", INNER_CASH_JOB);
            Log.d("COLLECTION OBJECT", COLLECTION_RECJOBJECT.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        // queue.add(stringRequest);



        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,INNER_CASH_JOB,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("volley", "response: " + response);

                        try {

                            //JSONObject json = new JSONObject(response);    // create JSON obj from string
                            JSONObject json2 = response.getJSONObject("Data");
                            String rmsg = json2.getString("rmsg");

                            Log.d("Validate Response","CRES"+rmsg);

//                            JSONObject obj = new JSONObject();
//                            try {
//                                // obj.put("hello", "server");
//                                // obj.put("binary", new byte[42]);
//                                obj.put("sendmsg", msg);
//                                obj.put("chattime", room);
//                                obj.put("lasttime", "join");
//                                obj.put("key", CHAT_SERVER_URL_KEY);
//
////                                mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
////                                        .username(powered).message(msg).build());
////                                mAdapter.notifyItemInserted(mMessages.size() - 1);
////                                scrollToBottom();
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                            mSocket.emit("broadcast", obj);
//                            Log.d("Broadcast Object","BO"+obj.toString());

//                            if (products.length() > 0) {
//                                for (int i = 0; i < products.length(); i++) {
//                                    JSONObject object = products.getJSONObject(i);
//
//                                    String msg = object.getString("msg");
//                                    String room = object.getString("room");
//                                }
//                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  finish();

                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) ;
//        {
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                headers.put("key", CHAT_SERVER_URL_KEY);
//                headers.put("sendmsg", message_text);
//                headers.put("chattime", "7.88");
//                headers.put("lasttime", "1582541452");
//                headers.put("token", "12345");
//
//                return headers;
//            }
//        };
        queue.add(postRequest);
    }




    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
//            Toast.makeText(getActivity(),
//                    getString(R.string.speech_not_supported),
//                    Toast.LENGTH_SHORT).show();
            Global_Data.Custom_Toast(getActivity(),"Sorry! Your device doesn\\'t support speech input","");
        }
    }


}

