package com.codingame.gameengine.runner;

import com.codingame.gameengine.runner.MultiplayerGameRunner;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.codingame.gameengine.runner.dto.GameResult;
import com.google.common.io.Files;

public class CommandLineInterface {
    static String JOHNNY_PICTURE 	= "https://static.codingame.com/servlet/fileservlet?id=14267188816585&format=ide_menu_avatar";
	static String NMAHOUDE_PICTURE 	= "https://static.codingame.com/servlet/fileservlet?id=9701936828280&format=ide_menu_avatar";
	static String EULERSCHE_PICTURE 	= "https://static.codingame.com/servlet/fileservlet?id=16390390383303&format=ide_menu_avatar";
	static String KUTULU_PICTURE 	= "https://pbs.twimg.com/profile_images/3192634881/a727e18d3434bd5e99e6a39d6329a985.jpeg";


	public static void main(String[] args) {
		try {
			Options options = new Options();

			options.addOption("h", false, "Print the help")
			       .addOption("p1", true, "Required. Player 1 command line.")
			       .addOption("p2", true, "Required. Player 2 command line.")
			       .addOption("p3", true, "Player 3 command line.")
			       .addOption("p4", true, "Player 4 command line.")
			       .addOption("s", false, "Server mode")
			       .addOption("l", true, "File output for logs")
			       .addOption("d", false, "Referee initial data");

			CommandLine cmd = new DefaultParser().parse(options, args);

			if (cmd.hasOption("h") || !cmd.hasOption("p1") || !cmd.hasOption("p2")) {
				new HelpFormatter().printHelp(
						"-p1 <player1 command line> -p2 <player2 command line> [-s -l <File output for logs>]",
						options);
				System.exit(0);
			}

			MultiplayerGameRunner runner = new MultiplayerGameRunner();

			Field getGameResult = GameRunner.class.getDeclaredField("gameResult");
			getGameResult.setAccessible(true);
			GameResult result = (GameResult) getGameResult.get(runner);

			if (cmd.hasOption("d")) {
				result.refereeInput = cmd.getOptionValue("d");
			}

			int playerCount = 0;

			for (int i = 1; i <= 4; ++i) {
				if (cmd.hasOption("p" + i)) {
					runner.addAgent(cmd.getOptionValue("p" + i), cmd.getOptionValue("p" + i) + " " + Integer.toString(i), JOHNNY_PICTURE);
					playerCount += 1;
				}
			}

			if (cmd.hasOption("s")) {
				runner.start();
			} else {
				Method initialize = GameRunner.class.getDeclaredMethod("initialize", Properties.class);
				initialize.setAccessible(true);
				initialize.invoke(runner, new Properties());

				Method runAgents = GameRunner.class.getDeclaredMethod("runAgents");
				runAgents.setAccessible(true);
				runAgents.invoke(runner);

				if (cmd.hasOption("l")) {
					Method getJSONResult = GameRunner.class.getDeclaredMethod("getJSONResult");
					getJSONResult.setAccessible(true);

					Files.asCharSink(Paths.get(cmd.getOptionValue("l")).toFile(), Charset.defaultCharset())
							.write((String) getJSONResult.invoke(runner));
				}

				for (int i = 0; i < playerCount; ++i) {
					System.out.println(result.scores.get(i));
				}

				for (String line : result.uinput) {
					System.out.println(line);
				}
			}

			// We have to clean players process properly
			Field getPlayers = GameRunner.class.getDeclaredField("players");
			getPlayers.setAccessible(true);
			@SuppressWarnings("unchecked")
			List<Agent> players = (List<Agent>) getPlayers.get(runner);

			if (players != null) {
				for (Agent player : players) {
					Field getProcess = CommandLinePlayerAgent.class.getDeclaredField("process");
					getProcess.setAccessible(true);
					Process process = (Process) getProcess.get(player);

					process.destroy();
				}
			}
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}

}
