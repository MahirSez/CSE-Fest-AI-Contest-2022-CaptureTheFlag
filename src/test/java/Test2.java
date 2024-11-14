import java.util.ArrayList;
import java.util.Scanner;

public class Test2 {

    public static class Bot {
        int id;
        int x, y;
        int health;
        int timeout;

        public Bot(int id, int x, int y, int health, int timeout) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.health = health;
            this.timeout = timeout;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int row, col;
        int my_base_x, my_base_y;
        int opp_base_x, opp_base_y;

        row = scanner.nextInt();
        col = scanner.nextInt();

        ArrayList<String> dunia = new ArrayList<>();
        ArrayList<String> powerName = new ArrayList<>();
        ArrayList<Integer> powerPrice = new ArrayList<>();
        ArrayList<Integer> powerDamage = new ArrayList<>();

        for(int i = 0 ; i < row ; i++) {
            String str = scanner.next();
            dunia.add(str);
            System.err.println(str);
        }

        my_base_x = scanner.nextInt();
        my_base_y = scanner.nextInt();


        opp_base_x = scanner.nextInt();
        opp_base_y = scanner.nextInt();

        // power up input
        for(int i = 0 ; i < 3 ; i++) {
            String name;
            int price, damage;
            name = scanner.next();
            price = scanner.nextInt();
            damage = scanner.nextInt();
            powerName.add(name);
            powerPrice.add(price);
            powerDamage.add(damage);
        }

        System.err.print("Entering game loop\n");
        int captured = 0;
        while (true) {
            int my_score, opp_score;
            int my_flag_x, my_flag_y, my_carrier;
            int opp_flag_x, opp_flag_y, opp_carrier;
            int alive_cnt, opp_seen_cnt;
            int visible_coin_cnt;

            my_score = scanner.nextInt();
            opp_score = scanner.nextInt();

            my_flag_x = scanner.nextInt();
            my_flag_y= scanner.nextInt();
            my_carrier = scanner.nextInt();

            opp_flag_x = scanner.nextInt();
            opp_flag_y= scanner.nextInt();
            opp_carrier = scanner.nextInt();

            System.err.println("Score: " + my_score);

            alive_cnt = scanner.nextInt();
            ArrayList<Test1.Bot> bots = new ArrayList<>();
            for(int i = 0 ; i < alive_cnt ; i++) {
                int id, x, y, health, timeout;
                id = scanner.nextInt();
                x = scanner.nextInt();
                y = scanner.nextInt();
                health = scanner.nextInt();
                timeout = scanner.nextInt();
                bots.add(new Test1.Bot(id, x, y, health, timeout));
                System.err.println(id + ": health(" + health + "), pos(" + x + "," + y + "), timeout(" + timeout + ")");
            }

            ArrayList<Test1.Bot> oppBots = new ArrayList<>();
            opp_seen_cnt = scanner.nextInt();
            for(int i = 0 ; i < opp_seen_cnt ; i++) {
                int id, x, y, health, timeout;
                id = scanner.nextInt();
                x = scanner.nextInt();
                y = scanner.nextInt();
                health = scanner.nextInt();
                timeout = scanner.nextInt();
                System.err.println("Seeing " + id + " " + x + " " + y + " " + health + " " + timeout);
                oppBots.add(new Test1.Bot(id, x, y, health, timeout));
            }

            visible_coin_cnt = scanner.nextInt();
            for(int i = 0 ; i < visible_coin_cnt ; i++) {
                int x, y;
                x = scanner.nextInt();
                y = scanner.nextInt();
            }

            StringBuilder str = new StringBuilder();
            for(int i = 0 ; i < alive_cnt ; i++) {
                if(i > 0) str.append(" | ");
                Test1.Bot bot = bots.get(i);
                if(bot.id == 0) str.append(String.format("MOVE %d 12 12", bot.id));
                else str.append(String.format("WAIT %d", bot.id));
            }
            System.out.println(str);
        }
    }

}
