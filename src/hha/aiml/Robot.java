package hha.aiml;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import bitoflife.chatterbean.AliceBot;
import bitoflife.chatterbean.Context;
import bitoflife.chatterbean.Graphmaster;
import bitoflife.chatterbean.Match;
import bitoflife.chatterbean.aiml.AIMLParser;
import bitoflife.chatterbean.aiml.AIMLParserConfigurationException;
import bitoflife.chatterbean.aiml.AIMLParserException;
import bitoflife.chatterbean.aiml.Category;
import bitoflife.chatterbean.parser.AliceBotParser;
import bitoflife.chatterbean.parser.AliceBotParserConfigurationException;
import bitoflife.chatterbean.parser.AliceBotParserException;
import bitoflife.chatterbean.util.Searcher;

public class Robot implements Runnable {

	AliceBot bot = null;
	Context context = null;
	Graphmaster graphmaster = null;
	boolean canfind = false;
	String command = null;
	static Context Rawcontext = null;
	private ByteArrayOutputStream gossip;

	public Robot() {
		// TODO Auto-generated constructor stub

	}

	// 从一个Robot复制出其他Robot
	public Robot(Robot _bot) {
		// TODO Auto-generated constructor stub
		BotEmotion emotion = new BotEmotion();
		emotion.init(this);
		this.context = Rawcontext.clone();
		this.graphmaster = _bot.graphmaster;
		this.bot = new AliceBot();
		this.bot.setEmotion(emotion);
		this.bot.setContext(context);
		this.bot.setGraphmaster(graphmaster);
	}

	public void InitRobot() {
		try {
			BotEmotion emotion = new BotEmotion();
			emotion.init(this);

			Jcseg.init();
			Jcseg.initDic();
			Jcseg.initSeg();

			// 鍒濆鍖栨満鍣ㄤ汉
			gossip = new ByteArrayOutputStream();

			AliceBotParser parser = new AliceBotParser();
			Searcher searcher = new Searcher();
			bot = parser.parse(new FileInputStream("Bots/context.xml"),
					new FileInputStream("Bots/splitters.xml"),
					new FileInputStream("Bots/substitutions.xml"),
					searcher.search("Bots/mydomain", ".*\\.aiml"));
			bot.setEmotion(emotion);
			context = bot.getContext();
			Rawcontext = context;
			graphmaster = bot.getGraphmaster();
			context.outputStream(gossip);

		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		} catch (AliceBotParserConfigurationException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		} catch (AliceBotParserException e) {

			e.printStackTrace();
		}
	}

	public boolean isInitDone() {
		return bot != null;
	}

	public void BeginInit() {
		new Thread(this).start();
	}

	public String Respond(String str) {

		if (!isInitDone()) {
			return "";
		}

		String output = "";
		String ansString = bot.respond(str);
		bitoflife.chatterbean.Context context = bot.getContext();

		output = (String) context.property("predicate.CanNotFind");
		context.property("predicate.CanNotFind", "null");
		command = (String) context.property("predicate.Command");
		context.property("predicate.Command", "null");

		if ("True".equals(output))
			canfind = false;
		else
			canfind = true;

		return ansString;
	}

	// 给一句带有上下文的话，然后找一个可行的对话
	public int Find(String str) {
		String[] string = str.split(" ");
		Match match = new Match(bot, string);
		Category category = graphmaster.match(match);
		return category.id;
	}

	public boolean CanFindAnswer() {
		return canfind;
	}

	public String getCommand() {
		return command;
	}

	public String getProperty(String str) {
		if (!isInitDone()) {
			return "";
		}
		return (String) context.property("predicate." + str);
	}

	public void setProperty(String str, String data) {
		if (!isInitDone()) {
			return;
		}
		context.property("predicate." + str, data);
	}

	public void LearnFromStream(int id, InputStream stream) {
		try {
			AIMLParser parser = new AIMLParser();
			parser.parse(id, graphmaster, stream);
		} catch (AIMLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AIMLParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void LearnFromUrl(int id, String address) {
		URL url = null;
		AIMLParser parser = null;
		try {
			url = new URL(address);
			parser = new AIMLParser();
			parser.parse(id, graphmaster, url.openStream());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (AIMLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AIMLParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		InitRobot();
	}
}
