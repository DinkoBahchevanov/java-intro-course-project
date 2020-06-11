package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Trap {
    private boolean isFree = true;
    private String takenFrom = "";
    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getTakenFrom() {
        return takenFrom;
    }

    public void setTakenFrom(String takenFrom) {
        this.takenFrom = takenFrom;
    }

    public void setIsFree(boolean takenOrNot) {
        this.isFree = takenOrNot;
    }

    public boolean isTrapFree() {
        return isFree;
    }

    /**
     * В този метод, в зависимост от избрания Trap от играча, сетваме
     * съответно типа на квадратчето Trap.
     * @param player - играчът, решил да заложи капан (Trap ще го съдържа, за да знаем на кого принадлежи капанът)
     * @param number - видът капан
     * @param trap - капанът, който сетваме
     */
    public void set(Player player, int number, Trap trap) {
        switch (number) {
            case 1:
                trap.setType(1);
                player.setMoney(player.getMoney() - 100);
                break;
            case 2:
                trap.setType(2);
                player.setMoney(player.getMoney() - 200);
                break;
            case 3:
                trap.setType(3);
                player.setMoney(player.getMoney() - 100);
                break;
            case 4:
                trap.setType(4);
                player.setMoney(player.getMoney() - 50);
                break;
            case 5:
                trap.setType(5);
                player.setMoney(player.getMoney() - 100);
                break;
        }
    }

    /**
     * В този метод прилагаме Трапът на съответния играч (на човека), който е стъпил на него.
     * @param trap - квадратчето, на което се намира играчът.
     * @param trapLossesForHuman - лист, в който държим Trap загубите за човека, които изчисляваме в края на цикъла.
     * @param board - дъска
     * @param human - играчът човек
     * @param numberOfTrap - типа на трапа
     * @param positionOfHuman - позиция на човека
     */
    public void applyTrapForHuman(Trap trap, ArrayList<Double> trapLossesForHuman, ArrayList<Object> board
            , Player human, int numberOfTrap, int positionOfHuman) {
        int moneyToPayForTrap = 0;
        switch (numberOfTrap) {
            case 1:
                trapLossesForHuman.add(human.getMoney() * 0.1);
                moneyToPayForTrap = 100;
                break;
            case 2:
                trapLossesForHuman.add(1000.0);
                moneyToPayForTrap = 200;
                break;
            case 3:
                human.setPossibilityToSetTrap(false);
                moneyToPayForTrap = 100;
                break;
            case 4:
                human.setRightToSetEviPlan(false);
                moneyToPayForTrap = 50;
                break;
            case 5:
                human.setChanceSquareNegative(true);
                moneyToPayForTrap = 100;
                break;
        }
        if (trap.getTakenFrom().equals("human") || trap.getTakenFrom().equals("bot")) {
            moneyToPayForTrap = 0;
        }
        trap.setIsFree(true);
        board.set(positionOfHuman, trap);
        if (trap.isTrapFree()) {
            human.setMoney(human.getMoney() - moneyToPayForTrap);
        }
        trap.setTakenFrom("");
    }

    /**
     * В този метод прилагаме Трапът на съответния играч (на бота), който е стъпил на него.
     * @param trap - квадратчето, на което се намира играчът.
     * @param trapLosesForBot - лист, в който държим Trap загубите за бота, които изчисляваме в края на цикъла.
     * @param board - дъска
     * @param bot - играчът бот
     * @param numberOfTrap - типа на трапа
     * @param positionOfBot - позиция на бота
     */
    public void applyTrapForBot(Trap trap, ArrayList<Double> trapLosesForBot, ArrayList<Object> board
            , Player bot, int numberOfTrap, int positionOfBot) {
        int moneyToPayForTrap = 0;
        switch (numberOfTrap) {
            case 1:
                trapLosesForBot.add(bot.getMoney() * 0.1);
                moneyToPayForTrap = 100;
                break;
            case 2:
                trapLosesForBot.add(1000.0);
                moneyToPayForTrap = 200;
                break;
            case 3:
                bot.setPossibilityToSetTrap(false);
                moneyToPayForTrap = 100;
                break;
            case 4:
                bot.setRightToSetEviPlan(false);
                moneyToPayForTrap = 50;
                break;
            case 5:
                bot.setChanceSquareNegative(true);
                moneyToPayForTrap = 100;
                break;
        }
        if (trap.getTakenFrom().equals("bot") || trap.getTakenFrom().equals("human")) {
            moneyToPayForTrap = 0;
        }
        trap.setIsFree(true);
        board.set(positionOfBot, trap);
        if (trap.isTrapFree()) {
            bot.setMoney(bot.getMoney() - moneyToPayForTrap);
        }
        trap.setTakenFrom("");
    }

    /**
     * В този метод, чрез четене от файл извеждаме на конзолата възможните избори за трап.
     */
    public void printTrapMenu() {
        File fileReference = new File("resources/trapInfo");
        try {
            FileReader fileReferenceReader = new FileReader(fileReference);
            BufferedReader bufferedReader = new BufferedReader(fileReferenceReader);

            String line = bufferedReader.readLine();
            while (line != null) {
                System.out.println(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
