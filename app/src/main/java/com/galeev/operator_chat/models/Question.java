package com.galeev.operator_chat.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    private String role;
    private String timestamp;
    private String firstName;
    private String lastName;
    private String questionTitle;
    private String question;

    // Конструктор и методы получения и установки значений для полей role, timestamp, firstName, lastName, questionTitle и question

    // Конструктор для инициализации объекта Question
    public Question(String role, String timestamp, String firstName, String questionTitle, String question) {
        this.role = role;
        this.timestamp = timestamp;
        this.firstName = firstName;
        this.lastName = lastName;
        this.questionTitle = questionTitle;
        this.question = question;
    }

    protected Question(Parcel in) {
        role = in.readString();
        timestamp = in.readString();
        firstName = in.readString();
        lastName = in.readString();
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
        dest.writeString(lastName);
        dest.writeString(questionTitle);
        dest.writeString(question);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}