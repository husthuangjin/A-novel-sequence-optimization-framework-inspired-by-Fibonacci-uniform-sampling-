import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TSPUtils {
    
    public static double[][] readCityPosition(String filename) {
        List<double[]> positions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                double[] pos = new double[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    pos[i] = Double.parseDouble(parts[i]); //与TSP案例保持一致
                }
                positions.add(pos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return positions.toArray(new double[0][]);
    }

    public static int countAdjacentEdges(int[] seq1, int[] seq2) {
        // 将序列转换为边的列表
        String[] edges1 = getEdgesArray(seq1);
        String[] edges2 = getEdgesArray(seq2);

        // 统计两个序列之间相邻边的不同个数
        int count = 0;
        for (int i = 0; i < seq1.length; i++) {
            if (!edges1[i].equals(edges2[i]) && !edges1[i].equals(reverseEdge(edges2[i]))) {
                count++;
            }
        }

        return count;
    }

    private static String[] getEdgesArray(int[] seq) {
        String[] edges = new String[seq.length];
        for (int i = 0; i < seq.length - 1; i++) {
            edges[i] = seq[i] + "," + seq[i + 1];
        }
        edges[seq.length - 1] = seq[seq.length - 1] + "," + seq[0]; // 回到初始位置
        return edges;
    }

    private static String reverseEdge(String edge) {
        String[] parts = edge.split(",");
        return parts[1] + "," + parts[0];
    }

    public static int[][] chromarraySelecter(int[][] chrom_select, int minIndex, int select_num){

        int[][] chromArray1_select = new int[select_num][chrom_select[0].length];
        int[] dis_chrom = new int[chrom_select.length];

        // countAdjacentEdges(seq1, seq2)
        List<Double> numbers = new ArrayList<>();
    
        List<Double> sortedNumbers = new ArrayList<>(numbers);
        Collections.sort(sortedNumbers);

        for (int i = 0; i < chrom_select.length; i++){
            if (i != minIndex){
                dis_chrom[i] = countAdjacentEdges(chrom_select[minIndex], chrom_select[i]);
            }
            else{
                dis_chrom[i] = 0;
            }
        }

        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < dis_chrom.length; i++) {
            indexes.add(i);
        }

        Collections.sort(indexes, (a, b) -> Integer.compare(dis_chrom[b], dis_chrom[a]));
    
        for (int i = 0; i < select_num; i++){
            chromArray1_select[i] = Arrays.copyOf(chrom_select[indexes.get(i)], chrom_select[indexes.get(i)].length);
        }
        // chromArray1_select[select_num-1] = Arrays.copyOf(chrom_select[minIndex], chrom_select[minIndex].length);
    
        return chromArray1_select;
    }

    public static double[][] tspDistance(double[][] cityPosition) {
        int nCities = cityPosition.length;
        double[][] distanceMatrix = new double[nCities][nCities];
    
        for (int i = 0; i < nCities; i++) {
            for (int j = 0; j < nCities; j++) {
                double distanceSquared = 0.0;
                for (int k = 0; k < cityPosition[i].length; k++) {
                    double diff = cityPosition[i][k] - cityPosition[j][k];
                    distanceSquared += diff * diff;
                }
                distanceMatrix[i][j] = Math.round(Math.sqrt(distanceSquared));
            }
        }
    
        return distanceMatrix;
    } 
}
