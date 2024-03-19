package com.galeev.operator_chat.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    private final String role;
    private final String timestamp;
    private final String firstName;
    private final String questionTitle;
    private final String question;


    public Question(String role, String timestamp, String firstName, String questionTitle, String question) {
        this.role = role;
        this.timestamp = timestamp;
        this.firstName = firstName;
        this.questionTitle = questionTitle;
        this.question = question;
    }

    protected Question(Parcel in) {
        role = in.readString();
        timestamp = in.readString();
        firstName = in.readString();
        questionTitle = in.readString();
        question = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(role);
        dest.writeString(timestamp);
        dest.writeString(firstName);
        dest.writeString(questionTitle);
        dest.writeString(question);
    }

    public String getRole() {
        return role;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public String getQuestion() {
        return question;
    }
}