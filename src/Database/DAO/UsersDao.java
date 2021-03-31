package Database.DAO;

import Database.Entities.Users;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public interface UsersDao {

   public void UsersDao();

   public ArrayList<Users> getAllUsers();
   public Users getSingleUsers(int usersId);
   public Users getSingleUsers(String usersName);
   public void updateUsersName(Users users, String newName, String currentUsersName);
   public void updateUsersPassword(Users users, String newPassword, String currentUsersName);
   public boolean deleteUsers(Users users, String currentUsersName);
   public void addUsers(@NotNull String usersName, String password, String currentUsersName);
}
