package com.example.xuedan_zou_myrun2;

class WekaClassifier {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier.N23ca3b364(i);
        return p;
    }
    static double N23ca3b364(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 0;
        } else if (((Double) i[64]).doubleValue() <= 2.6399) {
            p = WekaClassifier.N5608c5625(i);
        } else if (((Double) i[64]).doubleValue() > 2.6399) {
            p = WekaClassifier.N5a8fa6f38(i);
        }
        return p;
    }
    static double N5608c5625(Object []i) {
        double p = Double.NaN;
        if (i[20] == null) {
            p = 0;
        } else if (((Double) i[20]).doubleValue() <= 0.417184) {
            p = 0;
        } else if (((Double) i[20]).doubleValue() > 0.417184) {
            p = WekaClassifier.N3e3e4366(i);
        }
        return p;
    }
    static double N3e3e4366(Object []i) {
        double p = Double.NaN;
        if (i[11] == null) {
            p = 0;
        } else if (((Double) i[11]).doubleValue() <= 0.981774) {
            p = 0;
        } else if (((Double) i[11]).doubleValue() > 0.981774) {
            p = WekaClassifier.N286e3b437(i);
        }
        return p;
    }
    static double N286e3b437(Object []i) {
        double p = Double.NaN;
        if (i[23] == null) {
            p = 1;
        } else if (((Double) i[23]).doubleValue() <= 0.548962) {
            p = 1;
        } else if (((Double) i[23]).doubleValue() > 0.548962) {
            p = 0;
        }
        return p;
    }
    static double N5a8fa6f38(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 1;
        } else if (((Double) i[64]).doubleValue() <= 17.540968) {
            p = WekaClassifier.N4abc4b159(i);
        } else if (((Double) i[64]).doubleValue() > 17.540968) {
            p = 2;
        }
        return p;
    }
    static double N4abc4b159(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 16.669646) {
            p = WekaClassifier.N56e2c42010(i);
        } else if (((Double) i[6]).doubleValue() > 16.669646) {
            p = WekaClassifier.Ndccc4413(i);
        }
        return p;
    }
    static double N56e2c42010(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 772.745774) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 772.745774) {
            p = WekaClassifier.Nf0f599c11(i);
        }
        return p;
    }
    static double Nf0f599c11(Object []i) {
        double p = Double.NaN;
        if (i[9] == null) {
            p = 1;
        } else if (((Double) i[9]).doubleValue() <= 5.079606) {
            p = 1;
        } else if (((Double) i[9]).doubleValue() > 5.079606) {
            p = WekaClassifier.N2810deb012(i);
        }
        return p;
    }
    static double N2810deb012(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 2;
        } else if (((Double) i[3]).doubleValue() <= 19.790699) {
            p = 2;
        } else if (((Double) i[3]).doubleValue() > 19.790699) {
            p = 1;
        }
        return p;
    }
    static double Ndccc4413(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 2;
        } else if (((Double) i[3]).doubleValue() <= 20.222295) {
            p = 2;
        } else if (((Double) i[3]).doubleValue() > 20.222295) {
            p = WekaClassifier.N405fdbbb14(i);
        }
        return p;
    }
    static double N405fdbbb14(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 1;
        } else if (((Double) i[3]).doubleValue() <= 49.631985) {
            p = 1;
        } else if (((Double) i[3]).doubleValue() > 49.631985) {
            p = 2;
        }
        return p;
    }
}