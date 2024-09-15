import java.util.ArrayList;
import java.util.List;

public class Fibonacci {
       public static int[][] fibonacci(int N, int[] chrom) {
        List<Double> z = new ArrayList<>();
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        double phi = (Math.sqrt(5) + 1) / 2 - 1;
        int chromLen = chrom.length;
        int[][] chromArray = new int[N][chromLen];

        for (int i = 1; i <= N; i++) {
            z.add((2.0 * i - 1) / N - 1);
        }

        for (int i = 1; i <= N; i++) {
            double zValue = z.get(i - 1);
            x.add(Math.sqrt(1 - zValue * zValue) * Math.cos(2 * Math.PI * i * phi));
            y.add(Math.sqrt(1 - zValue * zValue) * Math.sin(2 * Math.PI * i * phi));
        }

        for (int i = 0; i < N; i++) {
            List<Integer> chromSave = new ArrayList<>();
            for (int value : chrom) {
                chromSave.add(value);
            }

            int[] terrain = new int[] {
                (int) Math.round(x.get(i) * chromLen),
                (int) Math.round(y.get(i) * chromLen),
                (int) Math.round(z.get(i) * chromLen)
            };

            for (int j = 0; j < terrain.length; j++) {
                if (terrain[j] < 0)
                {
                    int index = chromLen + terrain[j] + j;
                    if (index > chromLen)
                    {
                        index = index - chromLen;
                    }
                    if (index < j){
                        chromSave.add(index, chromSave.get(j));
                        chromSave.remove(j + 1);
                    }
                    else{
                        chromSave.add(index, chromSave.get(j));
                        chromSave.remove(j);                        
                    }                    
                }
                else{
                    int index = terrain[j] + j + 1;
                    if (index > chromLen)
                    {
                        index = index - chromLen;
                    }
                    if (index < j){
                        chromSave.add(index, chromSave.get(j));
                        chromSave.remove(j + 1);
                    }
                    else{
                        chromSave.add(index, chromSave.get(j));
                        chromSave.remove(j);                        
                    }                  
                }
            }

            // 将List转换回数组
            for (int k = 0; k < chromLen; k++) {
                chromArray[i][k] = chromSave.get(k);
            }
        }

        return chromArray;
    }
}
