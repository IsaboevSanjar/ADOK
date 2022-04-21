package uz.sanjar.adok;

import java.util.ArrayList;

public class QuestionData {
    private String question;
    private String answer;
    private String answerA;
    private String answerB;
    private String answerC;
    private ArrayList<String> variants = new ArrayList<>();

    public QuestionData(ArrayList<String> variants) {
        this.variants = variants;
    }

    public QuestionData(String question, String answer, String answerA, String answerB, String answerC) {
        this.question = question;
        this.answer = answer;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
    }

    public void setVariants(ArrayList<String> variants) {
        this.variants = variants;
        variants.add(answerA);
        variants.add(answerB);
        variants.add(answerC);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }
}
