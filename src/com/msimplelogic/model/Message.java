package com.msimplelogic.model;

public class Message {

    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_RESPONSE = 1;
    public static final int TYPE_ACTION = 2;

    private int mType;
    private String mMessage;
    private String RP;
    private String mUsername;
    private String progressbarflag;

    private Message() {}

    public String getProgressbarflag() {
        return progressbarflag;
    };

    public int getType() {
        return mType;
    };

    public String getMessage() {
        return mMessage;
    };

    public String getUsername() {
        return mUsername;
    };
    public String getRP() {
        return RP;
    };


    public static class Builder {
        private final int mType;
        private String mUsername;
        private String mMessage;
        private String mRP;
        private String progressbarflag;

        public Builder(int type) {
            mType = type;
        }

        public Builder username(String username) {
            mUsername = username;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Builder RP(String RP) {
            mRP = RP;
            return this;
        }

        public Builder progessd(String progressbarflags) {
            progressbarflag = progressbarflags;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.mType = mType;
            message.mUsername = mUsername;
            message.mMessage = mMessage;
            message.RP = mRP;
            message.progressbarflag = progressbarflag;
            return message;
        }
    }
}
