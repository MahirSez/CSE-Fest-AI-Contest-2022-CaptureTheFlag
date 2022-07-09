import com.codingame.gameengine.runner.MultiplayerGameRunner;

// test-run-moidda

public class SkeletonMain {
    public static void main(String[] args) {

        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

        gameRunner.addAgent(Freezer.class, "red");
        gameRunner.addAgent(Flamer.class, "blue");

//        gameRunner.addAgent(Waiter.class, "red");
//        gameRunner.addAgent(Waiter.class, "blue");

//        gameRunner.addAgent(Waiter.class, "red");
//        gameRunner.addAgent(Waiter_with_more_score.class, "blue");

//        gameRunner.addAgent(Waiter.class, "red");
//        gameRunner.addAgent(Self_killer.class, "blue");

        gameRunner.setSeed(243L);
        gameRunner.start();
    }
}
