package com.example.ahsens.speakupenglish;

/**
 * Created by Ahsen's on 5/6/2018.
 */

public class AIResult {

    String marks;
    String orgtxt;
    String rectxt;
    String peraname;
    String correctword;

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getOrgtxt() {
        return orgtxt;
    }

    public void setOrgtxt(String orgtxt) {
        this.orgtxt = orgtxt;
    }

    public String getRectxt() {
        return rectxt;
    }

    public void setRectxt(String rectxt) {
        this.rectxt = rectxt;
    }

    public String getPeraname() {
        return peraname;
    }

    public void setPeraname(String peraname) {
        this.peraname = peraname;
    }

    public String getCorrectword() {
        return correctword;
    }

    public void setCorrectword(String correctword) {
        this.correctword = correctword;
    }

    public String getWrongword() {
        return wrongword;
    }

    public void setWrongword(String wrongword) {
        this.wrongword = wrongword;
    }

    String wrongword;


}
