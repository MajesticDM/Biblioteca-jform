package vistas.inicio;

import clases.salida;
import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.sql.ResultSet;
import java.sql.SQLException;

import dbConexion.dbConnect;

public class dashboard extends JDialog {
    private JButton salirButton;
    private JButton loginButton;
    private JTextPane panelLibrosPrestados;
    private JTextPane panelLibrosVendidos;
    private JTextPane panelTotalLibros;
    private JTabbedPane tabbedPane1;
    private JPanel panelDashboard;
    private JTabbedPane tabbedPane2;
    private JTable TablePrestamos;
    private JTextField nCliente;
    private JButton guardarButton;
    private JButton editarButton;
    private JComboBox cxLibro;
    private JTextField CElectronico;
    private JTextField comentario;
    private JComboBox cxTipoSalida;

    public dashboard(JFrame parent) {
        super(parent);
        setTitle("Biblioteca");
        setContentPane(panelDashboard);
        setMinimumSize(new Dimension(500, 700));
        setModal(false);

        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setVisible(true);


        tabbedPane1.addComponentListener(new ComponentAdapter() {
        });
        tabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    cargarTablaPrestamos();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CrearNuevaSalida();
                } catch (SQLException ex) {
                    return;
                }
            }


        });
    }

    private void cargarTablaPrestamos() throws SQLException {

        ResultSet rs = dbConnect.ConsultarLibrosPrestados();
        TablePrestamos.setModel(DbUtils.resultSetToTableModel(rs));

    }
    private void CrearNuevaSalida() throws SQLException{
        salida salida = new salida();

        salida.cliente = nCliente.getText();
        salida.id_Libro = cxLibro.getSelectedIndex();
        salida.id_Salida = cxTipoSalida.getSelectedIndex();
        salida.correo = CElectronico.getText();
        salida.comentario = comentario.getText();

        String respuesta = dbConnect.InsertarSalida(salida);
        JOptionPane.showMessageDialog(this,respuesta,"Información",JOptionPane.INFORMATION_MESSAGE);

    }
    public static void main(String[] args) {
        dashboard dashboard = new dashboard(null);
    }
}
