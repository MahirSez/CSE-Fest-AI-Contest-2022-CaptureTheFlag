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
        for(int i = 0 ; i < row ; i++) {
            String str = scanner.next();
        }

        my_base_x = scanner.nextInt();
        my_base_y = scanner.nextInt();


        opp_base_x = scanner.nextInt();
        opp_base_y = scanner.nextInt();
        System.err.println(my_base_x + " " + my_base_y + " " + opp_base_x + " " + opp_base_y);

        System.err.print("Entering game loop\n");
        while (true) {
            int my_score, opp_score;
            int my_flag_x, my_flag_y, myCaptured;
            int opp_flag_x, opp_flag_y, oppCaptured;
            int alive_cnt, opp_seen_cnt;

            my_score = scanner.nextInt();
            opp_score = scanner.nextInt();


            my_flag_x = scanner.nextInt();
            my_flag_y= scanner.nextInt();
            myCaptured = scanner.nextInt();

            opp_flag_x = scanner.nextInt();
            opp_flag_y= scanner.nextInt();
            oppCaptured = scanner.nextInt();

            alive_cnt = scanner.nextInt();
            
            for(int i = 0 ; i < alive_cnt ; i++) {
                int id, x, y, health;
                id = scanner.nextInt();
                x = scanner.nextInt();
                y = scanner.nextInt();
                health = scanner.nextInt();
            }

            opp_seen_cnt = scanner.nextInt();
            for(int i = 0 ; i < opp_seen_cnt ; i++) {
                int id, x, y, health;
                id = scanner.nextInt();
                x = scanner.nextInt();
                y = scanner.nextInt();
                health = scanner.nextInt();
                System.err.println("Seeing " + id + " " + x + " " + y);
            }
            StringBuilder str = new StringBuilder();
            for(int i = 0 ; i < alive_cnt ; i++) {
                if(i > 0) str.append(" | ");
                if(oppCaptured == 1) str.append(String.format("MOVE %d %d %d", i, my_base_x, my_base_y) );
                else str.append(String.format("MOVE %d %d %d", i, opp_base_x, opp_flag_y) );
            }
            System.out.println(str.toString());
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