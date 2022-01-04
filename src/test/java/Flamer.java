import java.util.ArrayList;
import java.util.Scanner;

public class Flamer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int row, col;
        int my_base_x, my_base_y;
        int opp_base_x, opp_base_y;


        row = scanner.nextInt();
        col = scanner.nextInt();

        for(int i = 0 ; i < row ; i++) {
            String str = scanner.next();
        }

        my_base_x = scanner.nextInt();
        my_base_y = scanner.nextInt();


        opp_base_x = scanner.nextInt();
        opp_base_y = scanner.nextInt();

        String fireName;
        int firePrice, fireDamage;
        fireName = scanner.next();
        firePrice = scanner.nextInt();
        fireDamage = scanner.nextInt();

        String freezeName;
        int freezePrice, freezeDamage;
        freezeName = scanner.next();
        freezePrice = scanner.nextInt();
        freezeDamage = scanner.nextInt();

        String mineName;
        int minePrice, mineDamage;
        mineName = scanner.next();
        minePrice = scanner.nextInt();
        mineDamage = scanner.nextInt();


        System.err.print("Entering game loop\n");
        while (true) {
            int my_score, opp_score;
            int my_flag_x, my_flag_y, my_flag_carrier;
            int opp_flag_x, opp_flag_y, opp_flag_carrier;
            int alive_cnt, opp_seen_cnt;
            int visible_coin_cnt;

            my_score = scanner.nextInt();
            opp_score = scanner.nextInt();


            my_flag_x = scanner.nextInt();
            my_flag_y= scanner.nextInt();
            my_flag_carrier = scanner.nextInt();

            opp_flag_x = scanner.nextInt();
            opp_flag_y= scanner.nextInt();
            opp_flag_carrier = scanner.nextInt();

            alive_cnt = scanner.nextInt();
            ArrayList<Integer>ids = new ArrayList<>(alive_cnt);

            for(int i = 0 ; i < alive_cnt ; i++) {
                int id, x, y, health, timeout;
                id = scanner.nextInt();
                x = scanner.nextInt();
                y = scanner.nextInt();
                health = scanner.nextInt();
                timeout = scanner.nextInt();
                ids.add(id);
            }

            opp_seen_cnt = scanner.nextInt();

            for(int i = 0 ; i < opp_seen_cnt ; i++) {
                int id, x, y, health, timeout;
                id = scanner.nextInt();
                x = scanner.nextInt();
                y = scanner.nextInt();
                health = scanner.nextInt();
                timeout = scanner.nextInt();
                System.err.println("Seeing " + id + " " + x + " " + y + " " + health + " " + timeout);
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

                if(opp_seen_cnt > 0 && my_score >= firePrice && my_flag_carrier != -1) str.append(String.format("FIRE %d", ids.get(i)));
                else if(opp_flag_carrier != -1) str.append(String.format("MOVE %d %d %d", ids.get(i), my_base_x, my_base_y) );
                else str.append(String.format("MOVE %d %d %d", ids.get(i), opp_base_x, opp_flag_y) );
            }
            System.out.println(str);
        }
    }
}
