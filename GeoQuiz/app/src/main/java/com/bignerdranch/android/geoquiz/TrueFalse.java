package com.bignerdranch.android.geoquiz;


public class TrueFalse {
    private int mQuestion;
    private boolean mTrueQuestion;

    public TrueFalse(int question, boolean truequestion) {
        mQuestion = question;
        mTrueQuestion = truequestion;
    }

    public int getmQuestion() {
        return mQuestion;
    }

    public void setmQuestion(int question) {
        this.mQuestion = mQuestion;
    }

    public boolean ismTrueQuestion() {
        return mTrueQuestion;
    }

    public void setmTrueQuestion(boolean truequestion) {
        this.mTrueQuestion = mTrueQuestion;
    }
}
