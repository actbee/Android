package com.example.xuedan_zou_myrun2;

public class WekaClassifier {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier.N312ad5022(i);
        return p;
    }
    static double N312ad5022(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 1;
        } else if (((Double) i[64]).doubleValue() <= 12.504672) {
            p = WekaClassifier.N58d9a8fc3(i);
        } else if (((Double) i[64]).doubleValue() > 12.504672) {
            p = 2;
        }
        return p;
    }
    static double N58d9a8fc3(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 33.653391) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 33.653391) {
            p = 1;
        }
        return p;
    }
}
