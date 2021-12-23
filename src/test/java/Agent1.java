import java.util.Scanner;

public class Agent1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        for(int i = 0 ; i < 3 ; i++) {
            String input = scanner.nextLine();
            System.err.println("I got " + input);
        }
        while (true) {
            String input = scanner.nextLine();
            System.err.println("I got " + input);
            System.out.println("my output");
        }
    }
}

