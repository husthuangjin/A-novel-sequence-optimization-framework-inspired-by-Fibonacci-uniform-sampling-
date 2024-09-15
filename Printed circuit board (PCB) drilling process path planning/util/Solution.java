package util;

public final class Solution {  
  
    private final int[] path;  
    private final double distance;

    public Solution(int[] path, double distance) {  
        this.path = path;  
        this.distance = distance;
    }  
  
    public double score(City[] cities) {  
        double totalDistance = 0;  
        for (int i = 0; i < path.length - 1; i++) {  
            totalDistance += cities[path[i]].distanceTo(cities[path[i+1]]);  
        }  
        totalDistance += cities[path[path.length - 1]].distanceTo(cities[path[0]]);  
        return totalDistance;  
    }  
  
    public int[] getPath() {  
        return path;  
    } 
     
    public double getDistance() {  // 添加获取最佳距离的方法  
        return distance;  
    } 
}  