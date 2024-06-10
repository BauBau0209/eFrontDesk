
package Main;
import javax.swing.JFrame;

public class main {
    public static void main(String[] args){
        JFrame f = new JFrame("eFrontDesk Login");    
        login loginFrame = new login(f);
        f.add(loginFrame);
        f.setSize(800,500);
        f.setVisible(true);
    }
}

