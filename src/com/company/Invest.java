package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Invest {
    private ArrayList<Integer> generatedCompanies = new ArrayList<>();

    public void setGeneratedCompanies(ArrayList<Integer> generatedCompanies) {
        this.generatedCompanies = generatedCompanies;
    }

    /**
     * Чрез този метод генерираме на случаен принцип 3 компании
     * и ги извеждаме в конзолата като опции, от които играчът да избере.
     */
    public void generateCompanies() {
        Random random = new Random();
        System.out.println("Инвестирайте разумно и изберете компания:");
        for (int i = 0; i < 3; i++) {
            int generate = random.nextInt(6) + 1;
            if (this.generatedCompanies.contains(generate)) {
                i--;
                continue;
            }
            switch (generate) {
                case 1:
                    System.out.println("(1) : Macrosoft | min : 500 | risk/reward : 5.0");
                    this.generatedCompanies.add(1);
                    break;
                case 2:
                    System.out.println("(2) : Bombs Away | min : 400 | risk/reward : 0.5");
                    this.generatedCompanies.add(2);
                    break;
                case 3:
                    System.out.println("(3) : Clock Work Orange | min : 300 | risk/reward : 1.5");
                    this.generatedCompanies.add(3);
                    break;
                case 4:
                    System.out.println("(4) : Maroders unated | min : 200 | risk/reward : 2");
                    this.generatedCompanies.add(4);
                    break;
                case 5:
                    System.out.println("(5) : Fatcat incorporated | min : 100 | risk/reward : 2.5");
                    this.generatedCompanies.add(5);
                    break;
                case 6:
                    System.out.println("(6) : Macrosoft | min : 50 | risk/reward : 5.0");
                    this.generatedCompanies.add(6);
                    break;
            }
        }
        System.out.println("(7) : Не ми се инвестира повече!");
    }

    public ArrayList<Integer> getGeneratedCompanies() {
        return this.generatedCompanies;
    }


    /**
     * В този метод определяме минималната сума при инвестиция
     * спрямо компанията, която сме избрали.
     * @param number - номер на компанията
     */
    public int minimumCompanyInvestment(int number) {
        int min = 0;
        switch (number) {
            case 1:
               min = 500;
            break;
            case 2:
                min = 400;
            break;
            case 3:
                min =  300;
            break;
            case 4:
                min =  200;
            break;
            case 5:
                min =  100;
            break;
            case 6:
                min =  50;
            break;
        }
        return min;
    }
}
