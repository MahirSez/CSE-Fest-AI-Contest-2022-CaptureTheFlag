import java.util.ArrayList;
import java.util.Scanner;

public class Agent1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int row, col;
        int my_base_x, my_base_y;
        int opp_base_x, opp_base_y;


        row = scanner.nextInt();
        col = scanner.nextInt();

        my_base_x = scanner.nextInt();
        my_base_y = scanner.nextInt();

        opp_base_x = scanner.nextInt();
        opp_base_y = scanner.nextInt();

        System.err.print("Entering game loop\\n");
        while (true) {
            int my_score, opp_score;
            int my_flag_x, my_flag_y;
            int opp_flag_x, opp_flag_y;
            int alive_cnt, opp_seen_cnt;

            my_score = scanner.nextInt();
            opp_score = scanner.nextInt();

            my_flag_x = scanner.nextInt();
            my_flag_y= scanner.nextInt();

            opp_flag_x = scanner.nextInt();
            opp_flag_y= scanner.nextInt();

            alive_cnt = scanner.nextInt();
            
            for(int i = 0 ; i < alive_cnt ; i++) {
                int x, y, health;
                x = scanner.nextInt();
                y = scanner.nextInt();
                health = scanner.nextInt();
            }

            opp_seen_cnt = scanner.nextInt();
            for(int i = 0 ; i < opp_seen_cnt ; i++) {
                int x, y;
                x = scanner.nextInt();
                y = scanner.nextInt();
            }
            System.out.println("0 0");
        }
    }
}

/*

n, m
my_base
opp_base


loop() {
	my_score , opp_score

	my_flag_pos
	opp_flag_pos

	alive minon_cnt
		pos, health
	seen opp-minion_cnt
		pos
}
 */