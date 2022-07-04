import com.codingame.gameengine.runner.MultiplayerGameRunner;

// test-run-moidda

public class SkeletonMain {
    public static void main(String[] args) {

        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

        gameRunner.addAgent(Flamer.class, "red");
        gameRunner.addAgent(Freezer.class, "blue");

        gameRunner.setSeed(243L);

        // Another way to add a player
        // gameRunner.addAgent("python3 /home/user/player.py");
        

        gameRunner.start();
    }
}
