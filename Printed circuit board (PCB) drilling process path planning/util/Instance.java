package util;

import java.util.ArrayList;

public class Instance {

    private final City[] cities;

    public Instance(ArrayList<City> cities) {
        this.cities = new City[cities.size()];
        cities.toArray(this.cities);
    }

    public City[] getCities() {
        return cities;
    }
}
