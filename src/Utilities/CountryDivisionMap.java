package Utilities;

import Database.DAO.CountryDaoImpl;
import Database.DAO.FirstLevelDivisionDaoImpl;
import Database.Entities.Country;
import Database.Entities.FirstLevelDivision;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class CountryDivisionMap {
    
    public HashMap<FirstLevelDivision, Country> countryDivisionHashMap = new HashMap<>();
    private FirstLevelDivisionDaoImpl firstLevelDivisionDao = new FirstLevelDivisionDaoImpl();
    private CountryDaoImpl countryDao = new CountryDaoImpl();
    
    public CountryDivisionMap() {
        ArrayList<FirstLevelDivision> allFirstLevelDivs = firstLevelDivisionDao.getAllFirstLevelDivisions();
        ArrayList<Country> allCountries = countryDao.getAllCountries();
        
        for (FirstLevelDivision division: allFirstLevelDivs) {
            addToHashMap(division, countryDao.getCountry(division.getCountryId()));
            
//            System.out.println("Mapping K=" + division.getDivisionName() + " --> V=" + countryDao.getCountry(division.getCountryId()).getCountryName());
        }
        
    }
    
    public void addToHashMap(FirstLevelDivision firstLevelDivision, Country country) {
        countryDivisionHashMap.put(firstLevelDivision, country);
    }
    
    /**
     * Method receives a Country and returns an ArrayList<FirstLevelDivision> of all FirstLevelDivisions inside that Country.
     * @param country Input Country which is compared to find
     * @return Returns the ArrayList<FirstLevelDivision> containing every FirstLevelDivision mapped to input Country.
     */
    public ArrayList<FirstLevelDivision> getEveryFirstDivision(Country country) {
        ArrayList<FirstLevelDivision> firstLevelDivisions = new ArrayList<>();
        firstLevelDivisions.clear();
        
        // iterate across every FirstLevelDivision that has been mapped to a Country.
        for (FirstLevelDivision division: countryDivisionHashMap.keySet()) {
            String divisionName = division.getDivisionName();
//            System.out.println("Testing division name = " + divisionName);
            
            // if the Country this division is mapped to equals the input Country, add the division.
            if ((countryDivisionHashMap.get(division)) == country) {
//                System.out.println("Country has this division in it!\n\tDivision Name: " + divisionName);
                firstLevelDivisions.add(division);
            }
        }
        
        
        return firstLevelDivisions;
    }
    
    
}
