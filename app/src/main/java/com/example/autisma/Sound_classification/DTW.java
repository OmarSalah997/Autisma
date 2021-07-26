package com.example.autisma.Sound_classification;

public final class DTW {

    public static class Result {

        public double  mDistance;

        public Result( double pDistance) {

            this.mDistance    = pDistance;
        }
    }

    public DTW() { }

    public DTW.Result compute(int[] s,int[] t) {

       int lN = s.length;
      int lM = t.length;

        if(lN == 0 || lM == 0) {
            return new DTW.Result( Double.NaN);
        }

    double[][] lL  = new double[lN][lM];
       double[][] lG  = new double[lN][lM];

        int i, j;

        for(i = 0; i < lN; i++) {
            float ss =s[i];
            for(j = 0; j < lM; j++) {
                lL[i][j] = this.getDistanceBetween(ss, t[j]);
            }
        }

        lG[0][0] = lL[0][0];

        for(i = 1; i < lN; i++) {
            lG[i][0] = lL[i][0] + lG[i - 1][0];
        }

        for(j = 1; j < lM; j++) {
            lG[0][j] = lL[0][j] + lG[0][j - 1];
        }

        for (i = 1; i < lN; i++) {
            for (j = 1; j < lM; j++) {
                lG[i][j] = (Math.min(Math.min(lG[i-1][j], lG[i-1][j-1]), lG[i][j-1])) + lL[i][j];
            }
        }

        return new DTW.Result(((lG[lN - 1][lM - 1])));
    }


    public double getDistanceBetween(double p1, double p2) {
        return (p1 - p2) * (p1 - p2);
    }


}