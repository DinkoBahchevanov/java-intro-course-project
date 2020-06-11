package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Мonopolie {
    public void play() {
        Scanner scanner = new Scanner(System.in);

        String[] designBoardWithStrings = new String[20];
        designBoardWithStrings = fillDesignBoard(designBoardWithStrings);
        Object[] allBoardSquares = new Object[20];
        allBoardSquares = fillBoardSquares(allBoardSquares);

        String[] boardToPrint = new String[20];
        Arrays.fill(boardToPrint, "_");

        ArrayList<Object> board = new ArrayList<>();
        fillBoard(board, allBoardSquares);

        int cycle = -1;

        Player human = new Player();
        Player bot = new Player();
        human.setName("human");
        bot.setName("bot");

        ArrayList<Integer> investedMoneyForHuman = new ArrayList<>();
        ArrayList<Integer> investedMoneyForBot = new ArrayList<>();
        int generatedCompany = 0;
        ArrayList<Double> trapLossesForHuman = new ArrayList<>();
        ArrayList<Double> trapLossesForBot = new ArrayList<>();

        Player firstPlayer = determiningThePlayerToStart(human, bot);

        boolean isHumanTurn = false;
        if (firstPlayer.equals(human)) {
            isHumanTurn = true;
        }

        while (human.getMoney() > 0 && bot.getMoney() > 0) {

            if (bot.getPosition() == 0 && human.getPosition() != 0 && human.getPosition() != 1 && human.getPosition() != 2) {
                isHumanTurn = true;
            } else if (human.getPosition() == 0 && bot.getPosition() != 0 && bot.getPosition() != 1 && bot.getPosition() != 2) {
                isHumanTurn = false;
            }
            if (isHumanTurn) {
                if (human.getPosition() == 19) {
                    human.setPosition(0);
                } else {
                    System.out.println("Човека се мести!");
                    human.rollDiceAndSetPosition();
                }
                isHumanTurn = false;
            } else {
                if (bot.getPosition() == 19) {
                    bot.setPosition(0);
                } else {
                    System.out.println("Бота се мести!");
                    bot.rollDiceAndSetPosition();
                }
                isHumanTurn = true;
            }
            if (bot.getPosition() == 0 && human.getPosition() == 0) {

                for (Double value : trapLossesForHuman) {
                    System.out.printf("Човек: %f - %f = %f%n", human.getMoney()
                            , value, human.getMoney() - value);
                    human.setMoney(human.getMoney() - value);
                }

                for (Double aDouble : trapLossesForBot) {
                    bot.setMoney(bot.getMoney() - aDouble);
                }
                System.out.println();
                human.setMoney(human.getMoney() + 1000);
                bot.setMoney(bot.getMoney() + 1000);

                trapLossesForBot.clear();
                trapLossesForHuman.clear();

                human.calculateInvestments(human, investedMoneyForHuman);
                bot.calculateInvestments(bot, investedMoneyForBot);

                investedMoneyForHuman.clear();
                investedMoneyForBot.clear();

                setEvilPlansToZero(board, designBoardWithStrings);
                isHumanTurn = human.getMoney() > bot.getMoney();

            }
            if (!isHumanTurn) {
                int number = 0;
                int positionOfHuman = human.getPosition();

                switch (designBoardWithStrings[positionOfHuman]) {
                    case "trap":
                        trapGenerateForHuman(board, human, number, allBoardSquares, positionOfHuman, scanner, trapLossesForHuman);
                        break;
                    case "chance":
                        chanceGenerate(human);
                        break;
                    case "steal":
                        stealGenerate(human, board, positionOfHuman);
                        break;
                    case "start":
                        break;
                    case "partyHard":
                        System.out.println("Губите 25 парички! (PartyHard)");
                        human.setMoney(human.getMoney() - 25);
                        break;
                    case "invest":
                        generateInvestment(human, generatedCompany, investedMoneyForHuman, scanner);
                        generatedCompany = 0;
                        break;
                }
            } else {
                int number = 0;
                int positionOfBot = bot.getPosition();

                switch (designBoardWithStrings[positionOfBot]) {
                    case "trap":
                        trapGenerateForBot(bot, positionOfBot, trapLossesForBot, board, number, allBoardSquares, scanner);
                        break;
                    case "chance":
                        chanceGenerate(bot);
                        break;
                    case "steal":
                        stealGenerate(bot, board, positionOfBot);
                        break;
                    case "start":
                        break;
                    case "partyHard":
                        System.out.println("Губите 25 парички! (PartyHard)");
                        bot.setMoney(bot.getMoney() - 25);
                        break;
                    case "invest":
                        generateInvestment(bot, generatedCompany, investedMoneyForBot, scanner);
                        generatedCompany = 0;
                        break;
                }
            }
            System.out.printf("Human money: %.2f%n", human.getMoney());
            System.out.printf("Bot money: %.2f%n", bot.getMoney());
            printBoard(boardToPrint, human, bot);
            System.out.println();
        }
        if (bot.getMoney() <= 0) {
            System.out.println("Човекът победи!");
        } else System.out.println("Ботът победи!");
    }

    /**
     * В този метод се генерират Trap-овете за съответния играч.
     * Ако квадратчето е свободно, се запълва със съответния трап, избран от бота.
     * Ако е заето от играча, стъпил върху Trap-a, капанът се активира за съчия играч и в зависимост
     * от случайността, ще се приложи за самия играч, или пък няма (както е по условие).
     * Ако е зает от противнокивият играч, капанът задължително се активира за този играч, който е върху него.
     *
     * @param bot              - играчът, попаднал върху Trap-a.
     * @param number           - съответния вид капан, който е избран (между 1 и 6)
     * @param allBoardSquares  - масив с всички квадратчета
     * @param positionOfBot    - позицията на бота
     * @param trapLossesForBot - лист, в който държим загубите на бота от Trap квадратчета, като ги
     * изчисляваме накрая на цикъла(вадим ги от общия брой събрани пари, ако изобщо има загуби).
     */
    private void trapGenerateForBot(Player bot, int positionOfBot, ArrayList<Double> trapLossesForBot
            , ArrayList<Object> board, int number, Object[] allBoardSquares, Scanner scanner) {

        Trap trap = (Trap) board.get(positionOfBot);
        if (trap.isTrapFree() && bot.getPossibilityToSetTrap()) {
            trap.printTrapMenu();
            System.out.print("Въведи число: ");
            number = Integer.parseInt(scanner.nextLine());

            if (number == 6) return;

            while (number < 1 || number > 6){
                System.out.print("Въведи команда от номер 1 до номер 6: ");
                number = Integer.parseInt(scanner.nextLine());
            }

            trap.set(bot, number, trap);
            trap.setTakenFrom("bot");
            board.set(positionOfBot, trap);

            trap.setIsFree(false);

        } else if (trap.getTakenFrom().equals("bot")) {
            int dice = rollDiceForOwnTrap();
            if (dice % 3 != 0) {
                trap.applyTrapForBot(trap, trapLossesForBot, board, bot, trap.getType(), positionOfBot);
                System.out.println("Ботът попадна на собствен капан!");
            }
        } else if (trap.getTakenFrom().equals("human")) {
            System.out.println("Ботът попадна на капан!");
            trap.applyTrapForBot(trap, trapLossesForBot, board, bot, trap.getType(), positionOfBot);
        }
    }

    /**
     * В този метод генерираме инвестициите съответно на човека/бота.
     * Ако играчът има StealPlan(№ 2), той получава плюс 100 парички
     *
     * @param player                 - бот/човек
     * @param generatedCompany       - променлива, в която държим типа на генерираната компания
     * @param investedMoneyForPlayer - количество инвестирани пари
     */
    private void generateInvestment(Player player, int generatedCompany
            , ArrayList<Integer> investedMoneyForPlayer, Scanner scanner) {

        if (player.hasStealPlan() && player.getCurrentPlan() == 2) {
            System.out.printf("%s печели 100 парички, тъй като има stealPlan № 2" +
                    "(получава 100 папрички при следващо квадратче Invest)%n", player.getName());
            player.setMoney(player.getMoney() + 100);
        }
        Invest invest = new Invest();
        invest.generateCompanies();
        ArrayList<Integer> generatedCompanies = invest.getGeneratedCompanies();

        int company = Integer.parseInt(scanner.nextLine());

        if (invest.minimumCompanyInvestment(company) > player.getMoney() && player.getName().equals("human")) {
            System.out.println("Човекът няма достатъчно пари, за да инвестира в тази компания" +
                    " - продължаваме със следвашия ход на Бота!");
            return;
        } else if (invest.minimumCompanyInvestment(company) > player.getMoney() && player.getName().equals("bot")) {
            System.out.println("Ботът няма достатъчно пари, за да инвестира в тази компания" +
                    " - продължаваме със следвашия ход на Човека!");
            return;
        }
        while (company != 7 && player.getMoney() > 0) {
            if (!generatedCompanies.contains(company)) {
                System.out.print("Избери някоя от визуализираните компании: ");
                company = Integer.parseInt(scanner.nextLine());
                continue;
            }
            generatedCompany = company;
            System.out.print("Въведи пари: ");
            int money = Integer.parseInt(scanner.nextLine());
            while (money > player.getMoney()) {
                System.out.print("Нямаш достатъчно пари. Въведи отново: ");
                money = Integer.parseInt(scanner.nextLine());
                System.out.println();
            }
            while (money < invest.minimumCompanyInvestment(company)) {
                System.out.println("Въведи необходимата минимална сума!");
                money = Integer.parseInt(scanner.nextLine());
            }
            investedMoneyForPlayer.add(money);
            player.setMoney(player.getMoney() - money);
            System.out.print("Избери отново от менюто с опции: ");
            company = Integer.parseInt(scanner.nextLine());
        }
        player.addInvestment(generatedCompany);
    }

    /**
     * В този метод се генерират StealPlan - вете на съответния играч
     * Ако съответният играч има вече StealPlan от №3 и попадне на такова квадратче, той получава 100 парички
     * Ако няма StealPlan, се генерира такъв.
     *
     * @param player           - играчът(човек или бот)
     * @param positionOfPlayer - позицията на съответния играч
     */
    private void stealGenerate(Player player, ArrayList<Object> board, int positionOfPlayer) {
        Steal steal = (Steal) board.get(positionOfPlayer);

        if (!player.getRightToSetEvilPlan()){
            player.setRightToSetEviPlan(true);
            return;
        }
        if (player.hasStealPlan() && player.getCurrentPlan() == 3) {
            System.out.printf("%s печели 100 парички, тъй като попада на квадратче StealPlan " +
                    "и вече има такъв план от № 3%n", player.getName());
            player.setMoney(player.getMoney() + 100);
        }
        if (steal.isFree() && !player.hasStealPlan()) {
            int plan = steal.generateEvilPlan();
            steal.setPlan(plan);
            steal.setIsFree(false);
            player.setStealPlan(true);
            player.addStealPlan(plan);
        }
    }

    /**
     * В този метод се генерират квадратчетата Chance - в зависимост от случайността,
     * играчът може да спечели или загуби парички.
     *
     * @param player - съответният играч попаднал на квадратчето(човекът или ботът).
     */

    private void chanceGenerate(Player player) {
        Random random = new Random();
        int dice = random.nextInt(9) + 1;
        Chance chance = new Chance();

        if (player.hasStealPlan() && player.getCurrentPlan() == 1) {
            player.setMoney(player.getMoney() + 100);
        }
        if (!player.getChanceSquareNegative()) {
            if (dice % 2 == 0) {
                System.out.println("Днес е радостен ден за вас!");
                dice = random.nextInt(100) + 1;
                chance.getPositiveChance(player, dice);
            } else {
                System.out.println("Ти изтегли късата клечка!");
                dice = random.nextInt(100) + 1;
                chance.getNegativeChance(player, dice);
                player.setChanceSquareNegative(false);
            }
        } else {
            System.out.println("Ти изтегли късата клечка!");
            dice = random.nextInt(100) + 1;
            chance.getNegativeChance(player, dice);
        }
    }

    /**
     * В този метод се генерират Trap-овете за съответния играч.
     * Ако квадратчето е свободно, се запълва със съответния трап, избран от човека.
     * Ако е заето от играча, стъпил върху Trap-a, капанът се активира за съчия играч и в зависимост
     * от случайността, ще се приложи за самия играч, или пък няма (както е по условие).
     * Ако е зает от противнокивият играч, капанът задължително се активира за този играч, който е върху него.
     *
     * @param human              - играчът, попаднал върху Trap-a.
     * @param number             - съответния вид капан, който е избран (между 1 и 6)
     * @param allBoardSquares    - масив с всички квадратчета
     * @param positionOfHuman    - позицията на човека
     * @param trapLossesForHuman - лист, в който държим загубите на човека от Trap квадратчета, като ги
     *                           изчисляваме накрая на цикъла(вадим ги от общия брой събрани пари, ако изобщо има загуби).
     */
    private void trapGenerateForHuman(ArrayList<Object> board, Player human, int number
            , Object[] allBoardSquares, int positionOfHuman, Scanner scanner, ArrayList<Double> trapLossesForHuman) {
        Trap trap = (Trap) board.get(positionOfHuman);

        if (trap.isTrapFree() && human.getPossibilityToSetTrap()) {
            trap.printTrapMenu();
            System.out.print("Въведи число: ");
            number = Integer.parseInt(scanner.nextLine());
            while (number < 1 || number > 6){
                System.out.print("Въведи команда от номер 1 до номер 6: ");
                number = Integer.parseInt(scanner.nextLine());
            }
            if (number == 6) return;

            trap.set(human, number, trap);
            trap.setTakenFrom("human");
            board.set(positionOfHuman, trap);

            trap.setIsFree(false);
            allBoardSquares[positionOfHuman] = trap;

        } else if (trap.getTakenFrom().equals("human")) {
            int dice = rollDiceForOwnTrap();
            if (dice % 3 != 0) {
                System.out.println("Човекът попадна на собствен капан!");
                trap.applyTrapForHuman(trap, trapLossesForHuman, board, human, trap.getType(), positionOfHuman);
            }
        } else if (trap.getTakenFrom().equals("bot")) {
            System.out.println("Човекът попадна на капан!");
            trap.applyTrapForHuman(trap, trapLossesForHuman, board, human, trap.getType(), positionOfHuman);
        }
    }

    /**
     * Чрез този метод визуализираме игралната дъска в конзолата, на която можем да видим S (start) и
     * съответно двамата играчи (ботът и човекът). Когато те са на едно и съчо квадратче(позиция),
     * се визуализира само единият от тях.
     *
     * @param boardToPrint - Масив, който съдържа "_" на празните места, S на старта и
     *                       буквичка H или B (human or bot) в съответствие с техните позиции.
     * @param human        - човекът
     * @param bot          - ботът
     */
    private void printBoard(String[] boardToPrint, Player human, Player bot) {
        System.out.println("Текущи позиции: ");
        Arrays.fill(boardToPrint, "_");
        boardToPrint[bot.getPosition()] = "B";
        boardToPrint[human.getPosition()] = "H";
        System.out.print("S ");
        for (int i = 1; i < 8; i++) {
            System.out.print(boardToPrint[i] + " ");
        }
        System.out.println();
        System.out.print(boardToPrint[19]);
        for (int i = 0; i < 13; i++) {
            System.out.print(" ");
        }
        System.out.println(boardToPrint[8]);
        System.out.print(boardToPrint[18]);
        for (int i = 0; i < 13; i++) {
            System.out.print(" ");
        }
        System.out.println(boardToPrint[9]);
        for (int i = 17; i > 10; i--) {
            System.out.print(boardToPrint[i] + " ");
        }
        System.out.println(boardToPrint[10]);
    }

    /**
     * В този метод сетваме всички stealPlan квадратчета на 0 (незаети)
     *
     * @param board                  - лист, който съдържа нашите квадратчета
     * @param designBoardWithStrings - масив, който съдържа нашите квадратчета
     * като String стойност (самия тип на квадратчето, имената им).
     */
    private void setEvilPlansToZero(ArrayList<Object> board, String[] designBoardWithStrings) {
        for (int i = 0; i < board.size(); i++) {
            if (designBoardWithStrings[i].equals("steal")) {
                Steal steal = (Steal) board.get(i);
                steal.setPlan(0);
                board.set(i, steal);
            }
        }
    }

    /**
     * Когато играч попадне на собствен капан, по условие се хвърля 10-стенен зар.
     * Точно това прави и този метод.
     */
    private int rollDiceForOwnTrap() {
        Random random = new Random();
        return random.nextInt(10) + 1;
    }

    private void fillBoard
            (ArrayList<Object> board, Object[] allBoardSquares) {
        board.addAll(Arrays.asList(allBoardSquares).subList(0, 20));
    }

    /**
     * В този метод пълним нашата дъска с всички квадратчета (класове).
     *
     * @param allBoardSquares - масив, който съдържа обекти - нашите квадратчета (класове).
     */
    private Object[] fillBoardSquares(Object[] allBoardSquares) {
        allBoardSquares = new Object[]{new Start(), new Trap(), new Trap(), new Trap(), new Invest(),
                new PartyHard(), new Trap(), new Chance(),
                new Steal(), new Trap(), new Chance(), new Invest(),
                new Trap(), new PartyHard(), new Steal(), new Trap(),
                new PartyHard(), new Chance(), new Invest(), new Steal()};
        return allBoardSquares;
    }

    /**
     * В този метод пълним нашата designBoard с имента на всички квадратчета (и съответно правилните им позиции)
     *
     * @param designBoard - масив, който съдържа имената на квадратчетата.
     */
    private String[] fillDesignBoard(String[] designBoard) {
        designBoard = new String[]{"start", "trap", "trap", "trap", "invest", "partyHard", "trap"
                , "chance", "steal", "trap", "chance", "invest", "trap", "partyHard", "steal"
                , "trap", "partyHard", "chance", "invest", "steal"};
        return designBoard;
    }

    /**
     * Чрез този метод определяме на случаен принцип кой от двамата че започне
     * в същинското начало на програмата (и съответно играта) - човекът или ботът.
     *
     * @param human - чоекът
     * @param bot   - ботът
     */
    private Player determiningThePlayerToStart(Player human, Player bot) {
        Random random = new Random();

        int dice = random.nextInt(2) + 1;
        if (dice == 1) {
            return human;
        }
        return bot;
    }
}
