package com.company;

public class Chance {
    /**
     * Чрез този метод ние взимаме задължително квадратче шанс с позитивни последици
     * и ги прилагаме върху съответния играч.
     * @param player - играч
     * @param dice - зар
     */
    public void getPositiveChance(Player player, int dice){
        if (dice <= 39 && dice >= 1){
            System.out.print("Големите надежди!");
            System.out.println(" Печелите 50 парички");
            player.setMoney(player.getMoney() + 50);
        }else if (dice >=40 && dice <= 65 ){
            System.out.print("Лолита!");
            System.out.println(" Печелите 100 парички");
            player.setMoney(player.getMoney() + 100);
        }else if (dice>=65 && dice <= 79){
            System.out.print("Гордост и предразсъдъци!");
            System.out.println(" Печелите 150 парички");
            player.setMoney(player.getMoney() + 150);
        }else if (dice >= 80 && dice <=94){
            System.out.print("Повелителя на мухите!");
            System.out.println(" Печелите 200 парички");
            player.setMoney(player.getMoney() + 200);
        }else {
            System.out.print("Хобит!");
            System.out.println(" Печелите 250 парички");
            player.setMoney(player.getMoney() + 250);
        }
    }

    /**
     * Чрез този метод ние взимаме задължително квадратче шанс с негативни последици
     * и ги прилагаме върху съответния играч.
     * @param player - играч
     * @param dice - зар
     */
    public void getNegativeChance(Player player, int dice){
        if (dice <= 39 && dice >= 1){
            System.out.print("1001 нощ!");
            System.out.println(" Губите 50 парички");
            player.setMoney(player.getMoney() - 50);
        }else if (dice >=40 && dice <= 65 ){
            System.out.print("Балът на феите!");
            System.out.println(" Губите 100 парички");
            player.setMoney(player.getMoney() - 100);
        }else if (dice>=65 && dice <= 79){
            System.out.print("Война и мир!");
            System.out.println(" Губите 150 парички");
            player.setMoney(player.getMoney() - 150);
        }else if (dice >= 80 && dice <=94){
            System.out.print("Престъпление и наказание!");
            System.out.println(" Губите 200 парички");
            player.setMoney(player.getMoney() - 200);
        }else {
            System.out.print("Гроздовете на гнева!");
            System.out.println(" Губите 250 парички");
            player.setMoney(player.getMoney() - 250);
        }
    }
}
