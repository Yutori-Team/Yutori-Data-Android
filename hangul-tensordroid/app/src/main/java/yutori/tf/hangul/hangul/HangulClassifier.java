package yutori.tf.hangul.hangul;

import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;


public class HangulClassifier {

    private TensorFlowInferenceInterface tfInterface;

    private String inputName;
    private String keepProbName;
    private String outputName;
    private int imageDimension;

    private List<String> labels;
    private float[] output;
    private String[] outputNames;


    public static HangulClassifier create(AssetManager assetManager,
                                          String modelPath, String labelFile, int inputDimension,
                                          String inputName, String keepProbName,
                                          String outputName) throws IOException {

        HangulClassifier classifier = new HangulClassifier();

        classifier.inputName = inputName;
        classifier.keepProbName = keepProbName;
        classifier.outputName = outputName;

        classifier.labels = readLabels(assetManager, labelFile);

        classifier.tfInterface = new TensorFlowInferenceInterface(assetManager, modelPath);
        int numClasses = classifier.labels.size();

        classifier.imageDimension = inputDimension;

        classifier.outputNames = new String[] { outputName };

        classifier.outputName = outputName;

        classifier.output = new float[numClasses];

        return classifier;
    }


    public String[] classify(final float[] pixels) {

        tfInterface.feed(inputName, pixels, 1, imageDimension, imageDimension, 1);
        tfInterface.feed(keepProbName, new float[] { 1 });

        tfInterface.run(outputNames);

        tfInterface.fetch(outputName, output);

        TreeMap<Float,Integer> map = new TreeMap<>();
        for (int i = 0; i < output.length; i++) {
            map.put( output[i], i );
        }

        Arrays.sort(output);
        String[] topLabels = new String[5];
        for (int i = output.length; i > output.length-5; i--) {
            topLabels[output.length - i] = labels.get(map.get(output[i-1]));
        }
        return topLabels;
    }


    private static List<String> readLabels(AssetManager am, String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(am.open(fileName)));

        String line;
        List<String> labels = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            labels.add(line);
        }
        reader.close();
        return labels;
    }
}
