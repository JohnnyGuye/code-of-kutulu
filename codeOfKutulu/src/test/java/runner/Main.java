package runner;

import com.codingame.gameengine.runner.MultiplayerGameRunner;


public class Main {
	
	static String JOHNNY_PICTURE 	= "https://static.codingame.com/servlet/fileservlet?id=14267188816585&format=ide_menu_avatar";
	static String NMAHOUDE_PICTURE 	= "https://static.codingame.com/servlet/fileservlet?id=9701936828280&format=ide_menu_avatar";
	static String EULERSCHE_PICTURE 	= "https://static.codingame.com/servlet/fileservlet?id=16390390383303&format=ide_menu_avatar";
	static String KUTULU_PICTURE 	= "https://pbs.twimg.com/profile_images/3192634881/a727e18d3434bd5e99e6a39d6329a985.jpeg";
	
	public static void main(String[] args) {
      
	    MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        
        gameRunner.addAgent(runner.tests.random.Player.class, "JohnnyYuge", JOHNNY_PICTURE);
        gameRunner.addAgent(runner.tests.random.Player.class, "Nmahoude", NMAHOUDE_PICTURE);
        gameRunner.addAgent(runner.tests.random.Player.class, "EulerscheZahl" , EULERSCHE_PICTURE);
        gameRunner.addAgent(runner.tests.random.Player.class, "Kutulu",KUTULU_PICTURE);

        gameRunner.start(8888);

    }
}
