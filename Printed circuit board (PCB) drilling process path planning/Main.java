// 此版本V3.0对于超过100的城市进行优化，超过100的城市会选择最近的部分城市进行局部搜索
// import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import solver.lk.LKSolver;
import util.City;
import util.Instance;
import util.Solution;

public class Main {

    public static void main(String[] args) {
        String problemType = "robot_path_planning"; // Change as needed
        // String problemType = "uav_path_planning";
        // String problemType = "gene_path_planning"; 
        int N = 100;

        // int[] numbers = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        double bestDistance = Double.POSITIVE_INFINITY;

        String filePath = "";
        String savePath = "";

        if (problemType.equals("robot_path_planning")) {
            filePath = "./robot_path_lib/";
            savePath = "./result/robot_path_result/";
        } else if (problemType.equals("uav_path_planning")) {
            filePath = "./uav_path_lib/";
            savePath = "./result/uav_path_result/";
        } else if (problemType.equals("gene_path_planning")) {
            filePath = "./gene_path_lib/";
            savePath = "./result/gene_path_result/";
        }

        String[] files = new java.io.File(filePath).list();

        if (files != null) {
            for (String file : files) {
                // file = "kroB100.txt";
                String inputFilePath = filePath + file;
                String saveData = "";
                double[][] cityPosition = TSPUtils.readCityPosition(inputFilePath);
                double[][] distanceMatrix = TSPUtils.tspDistance(cityPosition);
                int nCities = cityPosition.length;
                int[] chrom = new int[nCities];
                for (int i = 0; i < nCities; i++) {
                    chrom[i] = i + 1;
                }
                double[] distanceArray = new double[N];

                int[][] chromArray = Fibonacci.fibonacci(N, chrom);
                // 拿到均布解，进行初次搜索
                for (int i = 0; i < N; i++) {
                    SeedResult sequence = LocalSearch.seedFunction(distanceMatrix, chromArray[i]);
                    int[] cityList = LocalSearch.localSearch3Opt(distanceMatrix, sequence, 1, 3, 1); // 快速搜索一下，用于筛选
                    distanceArray[i] = LocalSearch.distanceCalc(distanceMatrix, cityList);
                    int lastIdx = cityList.length;
                    chromArray[i] = Arrays.copyOf(cityList, lastIdx);
                }
                // 获取最小值和对应的序列
                double minDistance = Double.POSITIVE_INFINITY;
                int minIndex = -1;
                for (int i = 0; i < N; i++) {
                    if (distanceArray[i] < minDistance) {
                        minDistance = distanceArray[i];
                        minIndex = i;
                    }
                }

                int[][] chromArray1 = TSPUtils.chromarraySelecter(chromArray, minIndex, N); // 筛选出满足需求的序列

                double[] distanceArray1 = new double[N];
                for (int j = 0; j < N; j++){
                    SeedResult sequence2 = LocalSearch.seedFunction(distanceMatrix, chromArray[j]);
                    ArrayList<City> cities = new ArrayList<>();
                    int[] chrom1 = new int[nCities];
                    for (int m = 0; m < sequence2.seq.length; m++){
                        cities.add(new City(m + 1, cityPosition[m]));
                        chrom1[m] = sequence2.seq[m] - 1;
                    }
                    Instance instance = new Instance(cities);
                    LKSolver solver = new LKSolver(instance, 0);
                    Solution solution = solver.solve(chrom1);
                    chromArray1[j] = Arrays.copyOf(solution.getPath(), solution.getPath().length); // 将更新的数据推入
                    distanceArray1[j] = solution.getDistance();
                }

                minDistance = Double.POSITIVE_INFINITY;
                for (int i = 0; i < N; i++) {
                    if (distanceArray1[i] < minDistance) {
                        minDistance = distanceArray1[i];
                        minIndex = i;
                    }
                } 

                int[] bestPath = chromArray1[minIndex];
                bestDistance = minDistance;                
                for (int i : bestPath) {
                    saveData += i + 1 + " ";
                }
                saveData += "\n" + bestDistance;
                try (FileWriter fileWriter = new FileWriter(savePath + file.substring(0, file.length() - 4) + ".txt")) {
                    fileWriter.write(saveData.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println(bestDistance);
            }
        }
    }
}
