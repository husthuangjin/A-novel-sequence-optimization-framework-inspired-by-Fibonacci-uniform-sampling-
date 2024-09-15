import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class SeedResult {
    public int[] seq;
    public double dis;

    public SeedResult(int[] seq, double dis) {
        this.seq = seq;
        this.dis = dis;
    }
}

public class LocalSearch {
    
    public static double distanceCalc(double[][] Xdata, int[] cityTour) {
        double distance = 0;
        for (int k = 0; k < cityTour.length - 1; k++) {
            int m = k + 1;
            distance += Xdata[cityTour[k] - 1][cityTour[m] - 1];
        }
        distance += Xdata[cityTour[cityTour.length - 1] - 1][cityTour[0] - 1];
        return distance;
    }

    public static SeedResult seedFunction(double[][] Xdata, int[] sequence) {
        int[] seed = Arrays.copyOf(sequence, sequence.length);
        // seed[sequence.length] = sequence[0];
        double seedDistance = distanceCalc(Xdata, seed);
        return new SeedResult(seed, seedDistance);
    }
    public static List<Integer> selectPoints(double[][] Xdata, int point, int num) {
        List<Integer> selcetIndices = new ArrayList<>();
        List<Double> numbers = new ArrayList<>();

        for (int i = 0; i < Xdata[point].length; i++) {
            numbers.add(Xdata[point][i]);
        }

        List<Double> sortedNumbers = new ArrayList<>(numbers);
        Collections.sort(sortedNumbers);
        
        if (Xdata[point].length > 10){
            for (int i = 1; i <= num; i++) {
                int index = numbers.indexOf(sortedNumbers.get(i));
                numbers.set(index, -numbers.get(index)); // 因为这里面可能存在相等的，所以使用一个变一个为负数
                selcetIndices.add(index);
            }
        }
        else{
            for (int i = 1; i <= Xdata[point].length; i++) {
                selcetIndices.add(i - 1);
            }
        }
        return selcetIndices;
    }


    public static int[] localSearch2Opt(double[][] Xdata, int[] cityTour, int local_num) {
        int[] cityList = Arrays.copyOf(cityTour, cityTour.length);
        double bestDistance = distanceCalc(Xdata, cityList);
        int[] bestRoute = Arrays.copyOf(cityList, cityList.length);
        int[] seed = Arrays.copyOf(cityList, cityList.length);

        for (int i = 0; i < cityList.length - 1; i++) {
            int point = Math.max(0, i - 1);
            List<Integer> selcetIndices = selectPoints(Xdata, point, local_num);
            for (int j = 0; j < selcetIndices.size(); j++) {
                if (i < selcetIndices.get(j)){
                    // Reverse the segment between i and j
                    reverseSegment(bestRoute, i, selcetIndices.get(j));
                    // bestRoute[bestRoute.length - 1] = bestRoute[0]; // Ensure the tour is circular
                    double currentDistance = distanceCalc(Xdata, bestRoute);

                    if (currentDistance < bestDistance) {
                        bestDistance = currentDistance;
                        cityList = Arrays.copyOf(bestRoute, bestRoute.length);
                    } 
                    // Reset the bestRoute to the current cityList for the next iteration
                    bestRoute = Arrays.copyOf(seed, seed.length);
                }
            }
        }
        return cityList;
    }
    
    public static int[] localSearch3Opt(double[][] Xdata, SeedResult seedTour, int recursiveSeeding, int local_num, int local_num1) {
        int count = 0;
        int[] cityTour = seedTour.seq;
        int[] cityList = Arrays.copyOf(cityTour, cityTour.length);
        double cityListOld = seedTour.dis * 2;
        int iteration = 0;
        while (count < recursiveSeeding) {
            int[] bestRoute = Arrays.copyOf(cityList, cityList.length);
            int[] bestRoute1 = localSearch2Opt(Xdata, bestRoute, local_num);
            int[] bestRoute2 = new int[0];
            int[] bestRoute3 = new int[0];
            int[] bestRoute4 = new int[0];
            int[] bestRoute5 = new int[0];
            int[] seed = Arrays.copyOf(cityList, cityList.length);
            for (int i = 0; i < cityList.length - 2; i++) {
                int point = Math.max(0, i - 1);
                List<Integer> selcetIndices = selectPoints(Xdata, point, (int) (local_num1));
    
                for (int si = 0; si < selcetIndices.size(); si++) {
                    int j = selcetIndices.get(si);
                    if (i < j){
                        for (int sj = si + 1; sj < selcetIndices.size(); sj++) {
                            int k = selcetIndices.get(sj);
                            if (j < k){
                                bestRoute2 = Arrays.copyOfRange(bestRoute, 0, i + 1);
                                int[] subArray1 = Arrays.copyOfRange(bestRoute, j + 1, k + 1);
                                int[] subArray2 = Arrays.copyOfRange(bestRoute, i + 1, j + 1);
                                int[] subArray3 = Arrays.copyOfRange(bestRoute, k + 1, bestRoute.length);
                                bestRoute2 = appendArray(bestRoute2, subArray1);
                                bestRoute2 = appendArray(bestRoute2, subArray2);
                                bestRoute2 = appendArray(bestRoute2, subArray3);
        
                                bestRoute3 = Arrays.copyOfRange(bestRoute, 0, i + 1);
                                int[] subArray21 = reverseArray(Arrays.copyOfRange(bestRoute, i + 1, j + 1));
                                int[] subArray22 = reverseArray(Arrays.copyOfRange(bestRoute, j + 1, k + 1));
                                int[] subArray23 = Arrays.copyOfRange(bestRoute, k + 1, bestRoute.length);
                                bestRoute3 = appendArray(bestRoute3, subArray21);
                                bestRoute3 = appendArray(bestRoute3, subArray22);
                                bestRoute3 = appendArray(bestRoute3, subArray23);
        
                                bestRoute4 = Arrays.copyOfRange(bestRoute, 0, i + 1);
                                int[] subArray4 = reverseArray(Arrays.copyOfRange(bestRoute, j + 1, k + 1));
                                int[] subArray5 = Arrays.copyOfRange(bestRoute, i + 1, j + 1);
                                int[] subArray6 = Arrays.copyOfRange(bestRoute, k + 1, bestRoute.length);
                                bestRoute4 = appendArray(bestRoute4, subArray4);
                                bestRoute4 = appendArray(bestRoute4, subArray5);
                                bestRoute4 = appendArray(bestRoute4, subArray6);
        
                                bestRoute5 = Arrays.copyOfRange(bestRoute, 0, i + 1);
                                int[] subArray7 = Arrays.copyOfRange(bestRoute, j + 1, k + 1);
                                int[] subArray8 = reverseArray(Arrays.copyOfRange(bestRoute, i + 1, j + 1));
                                int[] subArray9 = Arrays.copyOfRange(bestRoute, k + 1, bestRoute.length);
                                bestRoute5 = appendArray(bestRoute5, subArray7);
                                bestRoute5 = appendArray(bestRoute5, subArray8);
                                bestRoute5 = appendArray(bestRoute5, subArray9);
        
                                // double dis0 = distanceCalc(Xdata, bestRoute);
                                // double dis1 = distanceCalc(Xdata, bestRoute1);
                                // double dis2 = distanceCalc(Xdata, bestRoute2);
                                // double dis3 = distanceCalc(Xdata, bestRoute3);
                                // double dis4 = distanceCalc(Xdata, bestRoute4);
                                // double dis5 = distanceCalc(Xdata, bestRoute5);     
                                if (distanceCalc(Xdata, bestRoute1) < distanceCalc(Xdata, bestRoute)) {
                                    bestRoute = Arrays.copyOf(bestRoute1, bestRoute1.length);
                                } 
                                else if (distanceCalc(Xdata, bestRoute2) < distanceCalc(Xdata, bestRoute)) {
                                    bestRoute = Arrays.copyOf(bestRoute2, bestRoute2.length);
                                } else if (distanceCalc(Xdata, bestRoute3) < distanceCalc(Xdata, bestRoute)) {
                                    bestRoute = Arrays.copyOf(bestRoute3, bestRoute3.length);
                                } else if (distanceCalc(Xdata, bestRoute4) < distanceCalc(Xdata, bestRoute)) {
                                    bestRoute = Arrays.copyOf(bestRoute4, bestRoute4.length);
                                } else if (distanceCalc(Xdata, bestRoute5) < distanceCalc(Xdata, bestRoute)) {
                                    bestRoute = Arrays.copyOf(bestRoute5, bestRoute5.length);
                                }  
                            }                 
                        }
                        if (distanceCalc(Xdata, bestRoute) < seedTour.dis) {
                            cityList = Arrays.copyOf(bestRoute, bestRoute.length);
                            seedTour.dis = distanceCalc(Xdata, bestRoute);
                        }
                        bestRoute = Arrays.copyOf(seed, seed.length); 
                    }
                }
            }
            count++;
            iteration++;
            System.out.println("Iteration = " + iteration + " -> Distance = " + seedTour.dis);
            if (cityListOld > seedTour.dis) {
                cityListOld = seedTour.dis;
                count = 0;
            }
        }
        return cityList;
    }

    private static int[] appendArray(int[] targetArray, int[] sourceArray) {
        int[] newArray = Arrays.copyOf(targetArray, targetArray.length + sourceArray.length);
        System.arraycopy(sourceArray, 0, newArray, targetArray.length, sourceArray.length);
        return newArray;
    }
    

    private static int[] reverseArray(int[] arr) {
        int[] reversedArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reversedArr[i] = arr[arr.length - 1 - i];
        }
        return reversedArr;
    }

    private static void reverseSegment(int[] array, int start, int end) {
        while (start < end) {
            int temp = array[start];
            array[start] = array[end];
            array[end] = temp;
            start++;
            end--;
        }
    }  
    
}
