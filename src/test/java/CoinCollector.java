//import java.util.ArrayList;
//import java.util.Scanner;
//
//public class CoinCollector {
//
//    public static class Bot {
//        int id;
//        int x, y;
//        int health;
//        int timeout;
//
//        public Bot(int id, int x, int y, int health, int timeout) {
//            this.id = id;
//            this.x = x;
//            this.y = y;
//            this.health = health;
//            this.timeout = timeout;
//        }
//    }
//
//
//    public static class Coin {
//        int x, y;
//
//        public Coin(int x, int y) {
//            this.x = x;
//            this.y = y;
//        }
//    }
//
//
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//
//        int row, col;
//        int my_base_x, my_base_y;
//        int opp_base_x, opp_base_y;
//
//        row = scanner.nextInt();
//        col = scanner.nextInt();
//
//        ArrayList<String> dunia = new ArrayList<>();
//        ArrayList<String> powerName = new ArrayList<>();
//        ArrayList<Integer> powerPrice = new ArrayList<>();
//        ArrayList<Integer> powerDamage = new ArrayList<>();
//
//        for(int i = 0 ; i < row ; i++) {
//            String str = scanner.next();
//            dunia.add(str);
//            System.err.println(str);
//        }
//
//        my_base_x = scanner.nextInt();
//        my_base_y = scanner.nextInt();
//
//
//        opp_base_x = scanner.nextInt();
//        opp_base_y = scanner.nextInt();
//
//        // power up input
//        for(int i = 0 ; i < 3 ; i++) {
//            String name;
//            int price, damage;
//            name = scanner.next();
//            price = scanner.nextInt();
//            damage = scanner.nextInt();
//            powerName.add(name);
//            powerPrice.add(price);
//            powerDamage.add(damage);
//        }
//
//        System.err.print("Entering game loop\n");
//        int captured = 0;
//        while (true) {
//            int my_score, opp_score;
//            int my_flag_x, my_flag_y, my_carrier;
//            int opp_flag_x, opp_flag_y, opp_carrier;
//            int alive_cnt, opp_seen_cnt;
//            int visible_coin_cnt;
//            ArrayList<Bot> myBots = new ArrayList<>();
//            ArrayList<Bot> oppBots = new ArrayList<>();
//            ArrayList<Coin> coins = new ArrayList<>();
//
//            my_score = scanner.nextInt();
//            opp_score = scanner.nextInt();
//
//            my_flag_x = scanner.nextInt();
//            my_flag_y= scanner.nextInt();
//            my_carrier = scanner.nextInt();
//
//            opp_flag_x = scanner.nextInt();
//            opp_flag_y= scanner.nextInt();
//            opp_carrier = scanner.nextInt();
//
//            alive_cnt = scanner.nextInt();
//            for(int i = 0 ; i < alive_cnt ; i++) {
//                int id, x, y, health, timeout;
//                id = scanner.nextInt();
//                x = scanner.nextInt();
//                y = scanner.nextInt();
//                health = scanner.nextInt();
//                timeout = scanner.nextInt();
//                myBots.add(new Bot(id, x, y, health, timeout));
//            }
//
//            System.err.println("My Score : " + my_score);
//            System.err.println("Opp Score: " + opp_score);
//
//            opp_seen_cnt = scanner.nextInt();
//            for(int i = 0 ; i < opp_seen_cnt ; i++) {
//                int id, x, y, health, timeout;
//                id = scanner.nextInt();
//                x = scanner.nextInt();
//                y = scanner.nextInt();
//                health = scanner.nextInt();
//                timeout = scanner.nextInt();
//            }
//            visible_coin_cnt = scanner.nextInt();
//            for(int i = 0 ; i < visible_coin_cnt ; i++) {
//                int x, y;
//                x = scanner.nextInt();
//                y = scanner.nextInt();
//                coins.add(new Coin(x, y));
//            }
//
//
//            StringBuilder str = new StringBuilder();
//            for(int i = 0 ; i < alive_cnt ; i++) {
//                if(i > 0) str.append(" | ");
//                if(i < coins.size()) str.append(String.format("MOVE %d %d %d", myBots.get(i).id, coins.get(i).x, coins.get(i).y));
//                else str.append()
//            }
//            System.out.println(str);
//        }
//    }
//}
