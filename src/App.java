import backEnd.User;
import frontEnd.telas.LoginScreen;

public class App {
    public static void main(String[] args) throws Exception {

        User user = new User();

        LoginScreen.main(null, user);
    
    }
}
