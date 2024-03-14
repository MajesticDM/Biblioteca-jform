package vistas.login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import clases.usuario;
import dbConexion.dbConnect;

public class login extends JDialog {
    private JButton IniciarSesion;
    private JButton Cancelar;
    private JTextField txtUsuario;
    private JPasswordField PsrContrasena;
    private JPanel panelLogin;
    private String nombreUsuario = "";

    public login(JFrame parent){
            super(parent);
            setTitle("Iniciar sesión");
            setContentPane(panelLogin);
            setMinimumSize(new Dimension(450,474));
            setModal(true);
            setLocationRelativeTo(parent);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        IniciarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loginUser();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        Cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    private void loginUser() throws SQLException {
    String usuario = txtUsuario.getText();
    String contrasena = String.valueOf(PsrContrasena.getPassword());
        String[] options = new String[2];
        options[0] = "Continuar";
        options[1] = "Salir";

        if(usuario.isEmpty() || contrasena.isEmpty()){
            JOptionPane.showMessageDialog(this,"Llena todos los campos","¡Oops!",JOptionPane.ERROR_MESSAGE);
            return;
        }

        consultarLogin(usuario, contrasena);
        if(!Objects.equals(nombreUsuario, "")){


            int opcion = JOptionPane.showOptionDialog(
                    this, "Hola, " + nombreUsuario + " ¡qué gusto verte!",
                    "Bienvenido",0,
                    JOptionPane.INFORMATION_MESSAGE,null,options, null);
            if (opcion == JOptionPane.YES_OPTION) {
                System.out.print("Redirigido");
            } else if (opcion == JOptionPane.NO_OPTION) {
                dispose();
            }
            nombreUsuario = "";
        }else {
            JOptionPane.showMessageDialog(this,"Algo salió mal","¡Oops!",JOptionPane.ERROR_MESSAGE);
            nombreUsuario = "";
        }


    }
    private boolean consultarLogin(String usuario, String contrasena) throws SQLException {
        ResultSet existe = dbConnect.consultarLogin(usuario, contrasena);
        while (existe.next()){
            nombreUsuario = existe.getString("NOMBRE");
        }
        return existe.next();
    }
    public static void main(String[] args) {
        login login = new login(null);
    }
}
