package prelim.controller;

import prelim.model.Athlete;

import java.io.*;
import java.util.*;

public class DataHandler {
    static HashMap<Integer, Athlete> athleteMap;

    public static void readFile(File file) throws IOException {
        // Buffered Reader obj
        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        // Instantiate Hashmap obj. Integer is the athlete id.
        athleteMap = new HashMap<Integer, Athlete>();

        Athlete athleteObj;
        String lineRead;
        while ((lineRead = fileReader.readLine()) != null) {

            // Split obj where there is a comma, but don't if it's between quotation marks
            String[] lineSplit = lineRead.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

            // if athlete ID exists in hashmap
            if (athleteMap.containsKey(Integer.parseInt(lineSplit[0]))) {

                // Get the obj in hashmap and just add the new event to the event arrayList
                athleteMap.get(Integer.parseInt(lineSplit[0])).addEventStanding(lineSplit);
            } else {
                // Add athlete to arrayList
                athleteObj = new Athlete(lineSplit);
                athleteMap.put(Integer.parseInt(lineSplit[0]), athleteObj);
            }
        }
    }

    public TreeMap<String, Double> aveHeightPerCountry() {
        HashMap<String, ArrayList<Integer>> heightCountryRecord = new HashMap<>();

        // Sort them by city
        for (Integer key : athleteMap.keySet()) {
            if (heightCountryRecord.containsKey(athleteMap.get(key).getTeam())) {
                 heightCountryRecord.get(athleteMap.get(key).getTeam()).add(athleteMap.get(key).getHeight());
            } else {
                ArrayList<Integer> heightList = new ArrayList<>();
                heightList.add(athleteMap.get(key).getHeight());
                heightCountryRecord.put(athleteMap.get(key).getTeam(), heightList);
            }
        }

        // Get Average per country
        HashMap<String, Double> avePerCountry = new HashMap<String, Double>();
        for (String key : heightCountryRecord.keySet()) {
            ArrayList<Integer> heightList = heightCountryRecord.get(key);
            double average = heightList.stream()
                    .mapToDouble(Integer::doubleValue)
                    .average()
                    .orElse(0.0);
            avePerCountry.put(key,average);
        }

        // Return only the top 3
        String top1 = avePerCountry.keySet()
                .stream()
                .max((x, y) -> avePerCountry.get(x).compareTo(avePerCountry.get(y)))
                .orElse(null);

        String top2 = avePerCountry.keySet()
                .stream()
                .filter(key -> key != top1)
                .max((x,y) -> avePerCountry.get(x).compareTo(avePerCountry.get(y)))
                .orElse(null);

        String top3 = avePerCountry.keySet()
                .stream()
                .filter(key -> key != top1 && key != top2)
                .max((x,y) -> avePerCountry.get(x).compareTo(avePerCountry.get(y)))
                .orElse(null);

        TreeMap<String, Double> top3Map = new TreeMap<>();
        top3Map.put(top3,avePerCountry.get(top3));
        top3Map.put(top2,avePerCountry.get(top2));
        top3Map.put(top1,avePerCountry.get(top1));


        return top3Map;
    }

    /**
     * This method tallies all the gold medals for each country based on the filters applied
     * @author Jasmin, Ramon Emmiel P.
     * @return sortedTopCountries The Map(TreeMap) that contains the top countries with gold medals
     */
    public Map<String, Integer> topCountriesMedals(){
        Map<String, Integer> topCountries = new HashMap<>();

        for (Athlete athlete : athleteMap.values()){
            topCountries.put(athlete.getNOC(), topCountries.getOrDefault(athlete.getNOC(),0) + 1);
        }

        Map<String, Integer> sortedTopCountries = new TreeMap<>(
                Comparator.comparing(topCountries::get).reversed()
        );

        sortedTopCountries.putAll(topCountries);

        return sortedTopCountries;
    }

    /**
     * This method calculates the total number of medals for each sport and returns a sorted map
     * with the sports as keys and their corresponding total medal counts as values.
     * Returns a TreeMap containing the top 3 sports with the highest total medal counts.
     *
     * @return A TreeMap containing the top 3 sports and their corresponding total medal counts.
     */
    public TreeMap<String, Integer> topSportsWithMostMedals() {
        TreeMap<String, Integer> topSports = new TreeMap<>();

        // iterate through the athletes and update the topSports map
        for (Athlete athlete : athleteMap.values()) {
            List<String> sports = athlete.getSport();

            // iterate through the sports for each athlete
            for (String sport : sports) {
                List<String> medals = athlete.getMedal();

                // assuming each medal is represented as a String
                topSports.put(sport, topSports.getOrDefault(sport, 0) + medals.size());
            }
        }
        // creates a sorted map based on the total medal count in descending order
        TreeMap<String, Integer> sortedTopSports = new TreeMap<>(
                Comparator.comparing(topSports::get).reversed()
        );

        // populates the sorted map with data from the unsorted map
        sortedTopSports.putAll(topSports);

        // creates a TreeMap to store the top 3 sports and their total medal counts
        TreeMap<String, Integer> top3Sports = new TreeMap<>();

        // adds the top 3 sports to the new TreeMap
        int count = 0;
        for (Map.Entry<String, Integer> entry : sortedTopSports.entrySet()) {
            top3Sports.put(entry.getKey(), entry.getValue());
            count++;
            if (count >= 3) {
                break;
            }
        }

        return top3Sports;
    }

    public static void printMap() {
        int x=0;
        for (int key : athleteMap.keySet()) {
            x++;
            System.out.println(x + ": " + athleteMap.get(key));
        }
    }

}
