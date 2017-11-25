package com.binarylemons.android.eurovisiontimemachine.model;

/**
 * Created by Luis on 26/10/2017.
 */

public class EuroEntry {

    private String mYear;
    private EuroRound mRound;
    private int mPosition;
    private int mPoints;
    private int mOrder;

    public EuroEntry(String year, EuroRound round, int order, int position, int points) {
        mYear = year;
        mRound = round;
        mOrder = order;
        mPosition = position;
        mPoints = points;
    }

    public String getYear() {
        return mYear;
    }

    public EuroRound getRound() {
        return mRound;
    }

    public int getOrder() {
        return mOrder;
    }

    public int getPosition() {
        return mPosition;
    }

    public int getPoints() {
        return mPoints;
    }

    public boolean isFinal() {
        return mRound == EuroRound.FINAL;
    }

    public boolean isSemifinal() {
        return (mRound == EuroRound.SEMIFINAL1 || mRound == EuroRound.SEMIFINAL2);
    }

    public boolean isFirstSemifinal() {
        return mRound == EuroRound.SEMIFINAL1;
    }

    public boolean isSecondSemifinal() {
        return mRound == EuroRound.SEMIFINAL2;
    }

    public boolean isWinner() {
        return mPosition == 1;
    }

    public boolean isSecondPlace() {
        return mPosition == 2;
    }

    public boolean isThirdPlace() {
        return mPosition == 3;
    }
}
