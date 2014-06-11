package hha.aiml;

import bitoflife.chatterbean.AliceBot;


public class BotEmotion {
	
	Robot bot;
	
	int vitality; // 活跃度 困倦-兴奋
	int happiness; // 快乐度 忧伤-快乐
	int confidence;// 置信度 不确定-确定
	int mighty; // 强势度 恐惧-愤怒
	// 范围均是0~10000

	// 性格 情感变化率？ 0-1000%
	int vitality_d;
	int happiness_d;
	int confidence_d;
	int mighty_d;

	// 不确定性 百分比 0-1000%
	int vitality_u;
	int happiness_u;
	int confidence_u;
	int mighty_u;

	public void init(Robot _bot) {
		
		bot = _bot;
		
		vitality = (int) (N_rand(5000.0, 50.0) + 0.5);
		happiness = (int) (N_rand(5000.0, 50.0) + 0.5);
		confidence = (int) (N_rand(5000.0, 50.0) + 0.5);
		mighty = (int) (N_rand(5000.0, 50.0) + 0.5);

		vitality_d = (int) (N_rand(80.0, 5.0) + 0.5);
		happiness_d = (int) (N_rand(80.0, 5.0) + 0.5);
		confidence_d = (int) (N_rand(80.0, 5.0) + 0.5);
		mighty_d = (int) (N_rand(80.0, 5.0) + 0.5);

		vitality_u = (int) (N_rand(15.0, 9.0) + 0.5);
		happiness_u = (int) (N_rand(15.0, 9.0) + 0.5);
		confidence_u = (int) (N_rand(15.0, 9.0) + 0.5);
		mighty_u = (int) (N_rand(15.0, 9.0) + 0.5);

	}
	
	public void UpdateEmotion()
	{
		bot.setProperty("Emotion", "");
	}

	public int normalization(int value) {
		if (value < 0)
			return 0;
		if (value > 10000)
			return 10000;
		return value;
	}

	public int getVitality() {
		return vitality;
	}

	public void setVitality(int vitality) {
		this.vitality = normalization(vitality);
	}

	public void changeVitality(int vitality) {
		this.vitality += normalization(vitality) * vitality_d * 0.01;
		this.vitality += normalization(vitality) * vitality_u
				* N_rand(0, 1) * 0.01;
		this.vitality = normalization(this.vitality);
	}

	public int getHappiness() {
		return happiness;
	}

	public void setHappiness(int happiness) {
		happiness = normalization(happiness);
	}

	public void changeHappiness(int happiness) {
		this.happiness += normalization(happiness) * happiness_d * 0.01;
		this.happiness += normalization(happiness) * happiness_u
				* N_rand(0, 1) * 0.01;
		this.happiness = normalization(this.happiness);
	}

	public int getConfidence() {
		return confidence;
	}

	public void setConfidence(int confidence) {
		this.confidence = normalization(confidence);
	}

	public void changeConfidence(int confidence) {
		this.confidence += normalization(confidence) * confidence_d
				* 0.01;
		this.confidence += normalization(confidence) * confidence_u
				* N_rand(0, 1) * 0.01;
		this.confidence = normalization(this.confidence);
	}

	public int getMighty() {
		return mighty;
	}

	public void setMighty(int mighty) {
		this.mighty = normalization(mighty);
	}

	public void changeMighty(int mighty) {
		this.mighty += normalization(mighty) * mighty_d * 0.01;
		this.mighty += normalization(mighty) * mighty_u * N_rand(0, 1)
				* 0.01;
		this.mighty = normalization(this.mighty);
	}

	static java.util.Random r = new java.util.Random();
	// 正态分布随机数产生
	public static double N_rand(double miu, double sigma2) {
		
		return r.nextGaussian() * Math.sqrt(sigma2) + miu;
	}

	// 泊松分布随机数的产生，代码如下： （未测试）
	public static double P_rand(double Lamda) { // 泊松分布
		double x = 0, b = 1, c = Math.exp(-Lamda), u;
		do {
			u = Math.random();
			b *= u;
			if (b >= c)
				x++;
		} while (b >= c);
		return x;
	}
}
