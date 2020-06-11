package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    private ArrayList<Integer> investments = new ArrayList<>();
    private double money = 1000;
    private int position;
    private boolean possibilityToSetTrap = true;
    private boolean rightToSetEviPlan = true;
    private boolean isChanceSquareNegative = false;
    private boolean stealPlan = false;
    private int currentPlan;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addInvestment(int typeOfInvestedCompany) {
        this.investments.add(typeOfInvestedCompany);
    }

    public int getCurrentPlan() {
        return currentPlan;
    }

    public void setStealPlan(boolean setStealPlan) {
        this.stealPlan = setStealPlan;
    }

    public boolean getRightToSetEvilPlan(){
        return this.rightToSetEviPlan;
    }

    public boolean hasStealPlan() {
        return this.stealPlan;
    }

    public void setChanceSquareNegative(boolean chanceSquareNegative) {
        isChanceSquareNegative = chanceSquareNegative;
    }

    public boolean isChanceSquareNegative() {
        return isChanceSquareNegative;
    }

    public boolean getChanceSquareNegative(){
        return this.isChanceSquareNegative;
    }

    public int getPosition() {
        return position;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean getPossibilityToSetTrap() {
        return this.possibilityToSetTrap;
    }

    public void setPossibilityToSetTrap(boolean possibilityToSetTrap) {
        this.possibilityToSetTrap = possibilityToSetTrap;
    }

    /**
     * В този метод сетваме възможността на играча за сетване на зли планове на true или false.
     * @param setEviPlan
     */
    public void setRightToSetEviPlan(boolean setEviPlan) {
        this.rightToSetEviPlan = setEviPlan;
    }


    public double getMoney() {
        return this.money;
    }

    /**
     * В този метод сетваме озицията на съответния играч.
     */
    public void rollDiceAndSetPosition() {
        Random random = new Random();
        int dice =  random.nextInt(2)+1;

        if (this.position + dice >= 20) {
            this.position = 0;
        } else setPosition(getPosition() + dice);
    }

    public void addStealPlan(int plan) {
        this.currentPlan = plan;
    }

    /**
     * В този метод изчисляваме загубите/печалбите на съответния играч от инвестиции.
     * @param player - играч
     * @param investedMoney - каква сума е инвестирал играчът.
     */
    public void calculateInvestments(Player player, ArrayList<Integer> investedMoney) {
        Random random = new Random();
        int dice;
        for (int i = 0; i < this.investments.size(); i++) {
            switch (this.investments.get(i)) {
                case 1:
                    dice = random.nextInt(100 - (-6)) - 5;
                    if (dice > 0) {
                        player.setMoney(player.getMoney() + investedMoney.get(i) * 0.2);
                    }
                    break;
                case 2:
                    dice = random.nextInt(50 - (-11)) - 10;
                    if (dice > 0) {
                        player.setMoney(player.getMoney() + investedMoney.get(i) * 0.5);
                    }
                    break;
                case 3:
                    dice = random.nextInt(35 - (-16)) - 15;
                    if (dice > 0) {
                        player.setMoney(player.getMoney() + investedMoney.get(i) * 1.5);
                    }
                    break;
                case 4:
                    dice = random.nextInt(50 - (-19)) - 18;
                    if (dice > 0) {
                        player.setMoney(player.getMoney() + investedMoney.get(i) * 2);
                    }
                    break;
                case 5:
                    dice = random.nextInt(100 - (-26)) - 25;
                    if (dice > 0) {
                        player.setMoney(player.getMoney() + investedMoney.get(i) * 2.5);
                    }
                    break;
                case 6:
                    dice = random.nextInt(10 - (-21)) - 20;
                    if (dice > 0) {
                        player.setMoney(player.getMoney() + investedMoney.get(i) * 5.0);
                    }
                    break;
            }
        }
        this.investments.clear();
    }
}
