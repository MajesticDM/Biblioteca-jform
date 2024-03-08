package vistas.login;

import javax.swing.*;
import java.awt.*;

public class login extends JDialog {
    private JButton IniciarSesion;
    private JButton Cancelar;
    private JTextField textField1;
    private JTextField textField2;
    private JPanel panelLogin;

    public login(JFrame parent){
            super(parent);
            setTitle("Iniciar sesión");
            setContentPane(panelLogin);
            setMinimumSize(new Dimension(450,474));
            setModal(true);
            setLocationRelativeTo(parent);
            setVisible(true);
    }

    public static void main(String[] args) {
        login login = new login(null);
    }
}
