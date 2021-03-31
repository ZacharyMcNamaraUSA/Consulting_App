package Database.DAO;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import Database.DBConnection;
import Database.Entities.Countries;

public interface CountriesDao {


   public ArrayList<Countries> getAllCountries();
   public boolean updateCountries(int countriesOldID, String countriesNameNew);
   public boolean addCountries(Countries countriesToAdd, String currentUsersName);
   public boolean deleteCountries(Countries countriesNameToDelete);
   public void updateCountriesList();

}
