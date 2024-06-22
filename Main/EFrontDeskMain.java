package Main;

public class EFrontDeskMain {

    public static void main(String[] args) {
        try{
            login loginFrame = new login();
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setResizable(false);
            loginFrame.setVisible(true);
        } catch (Exception e){
            e. printStackTrace();
        }
        
    }
}
