package uz.sanjar.adok;

import java.util.ArrayList;
import java.util.Collections;

public class QuestionManager {
    private ArrayList<QuestionData> data;
    private int totalQuestion = 0;
    private int currentPosition = 0;
    private int totalTrues;
    private int totalFalse;

    private int time;

    public QuestionManager() {

    }

    public QuestionManager(ArrayList<QuestionData> data) {
        this.data = data;
        Collections.shuffle(this.data);
        totalQuestion = data.size();
    }

    public void setData(ArrayList<QuestionData> data) {
        this.data = data;
        Collections.shuffle(this.data);
        totalQuestion = data.size();
    }

    private QuestionData getCurrentQuestion() {
        return this.data.get(currentPosition);
    }

    public String getQuestion() {
        return getCurrentQuestion().getQuestion();
    }

    public String variantA() {
        return getCurrentQuestion().getAnswerA();
    }

    public String variantB() {
        return getCurrentQuestion().getAnswerB();
    }

    public String variantC() {
        return getCurrentQuestion().getAnswerC();
    }

    public boolean checkAnswer(String answer) {
        boolean isTrue = false;
        if (answer.equalsIgnoreCase(getCurrentQuestion().getAnswer())) {
            isTrue = true;
            totalTrues++;
        } else {
            isTrue = false;
            totalFalse++;
        }
        if (hasQuestion()) {
            currentPosition++;
        }
        return isTrue;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTotalFalse() {
        return totalFalse;
    }

    public int getTotalTrues() {
        return totalTrues;
    }

    boolean isFinish() {
        return currentPosition == totalQuestion;
    }

    public boolean hasQuestion() {
        return currentPosition < totalQuestion;
    }

    public int getCurrentLevel() {
        return currentPosition + 1;
    }

    public int getTotal() {
        return data.size();
    }
}
