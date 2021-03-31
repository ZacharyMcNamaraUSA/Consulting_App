package Database.DAO;

import Database.Entities.Customers;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public interface CustomersDao {

   public Customers getSingleCustomers(int customersId);
   public boolean deleteCustomer(Customers deleteCustomer);
   public void updateCustomer(int currentCustomerID, Customers newCustomer);
   public ArrayList<Customers> getAllCustomers();
}
