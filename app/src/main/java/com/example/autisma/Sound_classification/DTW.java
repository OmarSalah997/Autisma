package com.example.autisma.Sound_classification;

public final class DTW {

    public static class Result {

        private final double  Distance;

        public Result( final double pDistance) {

            this.Distance    = pDistance;
        }
        public final double     getDistance() { return this.Distance;    }
    }


    public DTW() { }

    public DTW.Result compute(final int[] s, final int[] t) {

        final int lN = s.length;
        final int lM = t.length;

        if(lN == 0 || lM == 0) {
            return new DTW.Result(Double.NaN);
        }
        final double[][] lL            = new double[lN][lM];
        final double[][] lG            = new double[lN][lM];

        int i, j;

        for(i = 0; i < lN; i++) {

            final float ls= s[i];
            for(j = 0; j < lM; j++) {

                lL[i][j] = this.getDistanceBetween(ls, t[j]);
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

        while ((i + j) != 0) {

            if(i == 0) {
                j -= 1;
            }
            else if(j == 0) {
                i -= 1;

            }

        }

        return new DTW.Result(((lG[lN - 1][lM - 1]) ));
    }


    public double getDistanceBetween(double p1, double p2) {
        return (p1 - p2) * (p1 - p2);
    }



}