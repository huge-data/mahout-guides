package mia.recommender.ch02;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

public class Evaluatorintro {

	private Evaluatorintro() {
		//
	}

	public static void main(String[] args) throws IOException, TasteException {

		RandomUtils.useTestSeed();
		DataModel model = new FileDataModel(new File("data/ch02/intro.csv"));

		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();

		// 构建与之前相同的recommender，用于测试
		RecommenderBuilder builder = new RecommenderBuilder() {
			@Override
			public Recommender buildRecommender(DataModel model) throws TasteException {
				UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
				return new GenericUserBasedRecommender(model, neighborhood, similarity);
			}
		};

		// 使用70%的数据训练，30%的数据测试
		double score = evaluator.evaluate(builder, null, model, 0.7, 1.0);
		System.out.println(score);
	}

}
