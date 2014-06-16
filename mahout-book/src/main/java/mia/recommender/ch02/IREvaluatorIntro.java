package mia.recommender.ch02;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IREvaluatorIntro {

	private static Logger logger = LoggerFactory.getLogger(IREvaluatorIntro.class);

	private IREvaluatorIntro() {
		//
	}

	/**
	 * 测试函数
	 */
	public static void main(String[] args) throws IOException, TasteException {

		logger.warn("Start ... ");
		RandomUtils.useTestSeed();
		DataModel model = new FileDataModel(new File("data/ch02/intro.csv"));

		RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();

		// 构建同样的recommender用于测试
		RecommenderBuilder builder = new RecommenderBuilder() {
			@Override
			public Recommender buildRecommender(DataModel model) throws TasteException {
				UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
				return new GenericUserBasedRecommender(model, neighborhood, similarity);
			}
		};

		// 评估精度，并回调步骤2
		IRStatistics stats = evaluator.evaluate(builder, null, model, null, 2,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
		System.out.println(stats.getPrecision());
		System.out.println(stats.getRecall());
	}

}
