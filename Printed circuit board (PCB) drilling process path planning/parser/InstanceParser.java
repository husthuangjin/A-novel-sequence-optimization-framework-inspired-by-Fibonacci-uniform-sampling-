package parser;
import util.City;
import util.Instance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class InstanceParser {

    public static Instance parse(String inputFile) throws Exception {
        try (BufferedReader input = new BufferedReader(new FileReader(inputFile))) {
            ArrayList<City> cities = new ArrayList<>();

            // input.readLine(); // drop first line.

            String line;
            while ((line = input.readLine()) != null) {
                String[] data = line.split("\\s+");
                int id = Integer.parseInt(data[0]);
                double[] city_position = new double[data.length-1];
                for(int i = 0; i < data.length-1; i++){
                    city_position[i] = Double.parseDouble(data[i+1]);
                }
                cities.add(new City(id, city_position));
            }

            return new Instance(cities);
        }
    }
}
