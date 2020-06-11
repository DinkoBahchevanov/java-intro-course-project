package com.company;

import java.util.Random;

public class Steal {
    private boolean isFree = true;
    private int plan;


    public boolean isFree(){
        return this.isFree;
    }

    public void setIsFree(boolean free) {
        isFree = free;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public int generateEvilPlan(){
        Random random = new Random();
        return random.nextInt(3)+1;
    }
}
