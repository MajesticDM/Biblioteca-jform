package vistas.inicio;

import clases.genero;
import clases.libro;
import clases.salida;
import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ServiceLoader;

import dbConexion.dbConnect;

public class dashboard extends JDialog {
    private JButton salirButton;
    private JButton loginButton;
    private JTextPane panelLibrosPrestados;
    private JTextPane panelLibrosVendidos;
    private JTextPane panelTotalLibros;
    private JTabbedPane PanelInicio;
    private JPanel panelDashboard;
    private JTabbedPane SubPanelInventario;
    private JTable TablePrestamos;
    private JTextField txtCliente;
    private JButton guardarSalida;
    private JButton editarSalida;
    private JComboBox cxLibro;
    private JTextField txtCElectronico;
    private JTextField txtComentario;
    private JComboBox cxTipoSalida;
    private JTable jTableLibros;
    private JTextField txtNombreLibro;
    private JButton guardarLibro;
    private JButton editarLibro;
    private JComboBox cxGeneroLibro;
    private JComboBox cxEstadoLibro;
    private JPanel PanelDashboard;
    private JPanel PanelPrestar;
    private JPanel PanelInventario;
    private JPanel SubPanelLibros;
    private JPanel SubPanelGeneros;
    private JTable JtableGeneros;
    private JComboBox cxEstadoGenero;
    private JTextField txtGenero;
    private JButton GuardarGenero;
    private JButton EditarGenero;

    private static String[] estados = {"Activo","Inactivo"};

    public dashboard(JFrame parent) {
        super(parent);
        setTitle("Biblioteca");
        setContentPane(panelDashboard);
        setMinimumSize(new Dimension(500, 700));
        setModal(false);

        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setVisible(true);


        PanelInicio.addComponentListener(new ComponentAdapter() {
        });
        PanelInicio.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {

                    cargarTablaPrestamos();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        editarSalida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        
            }


        });
        guardarSalida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CrearNuevaSalida();
                } catch (SQLException ex) {
                    return;
                }
            }
        });


        PanelInicio.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                EditarGenero.setEnabled(false);
                editarLibro.setEnabled(false);

                cxEstadoGenero.removeAllItems();
                cxEstadoLibro.removeAllItems();
                for (String estado : estados) {
                    cxEstadoGenero.addItem(estado);
                    cxEstadoLibro.addItem(estado);
                }

                try {
                    cargarTablaLibros();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        SubPanelInventario.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //Cargar tabla géneros
                //Inhabilitar botón editar


                //Cargar las tablas para actualizarlas en cada cambio del panel
                //cargarTablaGeneros();
                //cargarTablaLibros();
            }
        });

        GuardarGenero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    crearNuevoGenero();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        guardarLibro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    crearNuevoLibro();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        TablePrestamos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                DefaultTableModel model = (DefaultTableModel)TablePrestamos.getModel();
                int filaSeleccionada = TablePrestamos.getSelectedRow();

                txtGenero.setText(model.getValueAt(filaSeleccionada,0).toString());
                cxEstadoGenero.setSelectedIndex(model.getValueAt(filaSeleccionada,1).toString() == "Activo" ? 0 : 1);

             }
        });
    }

    private void crearNuevoLibro() throws SQLException {
        libro libro = new libro();
        libro.libro = txtNombreLibro.getText();
        libro.idx_genero = (cxGeneroLibro.getSelectedIndex() + 1);
        libro.bitEstado = (cxEstadoLibro.getSelectedIndex() == 0 ? true : false);

        String respuesta = dbConnect.InsertarNuevoLibro(libro);
        JOptionPane.showMessageDialog(this,respuesta,"Información",JOptionPane.INFORMATION_MESSAGE);
    }

    private void crearNuevoGenero()  throws SQLException{
        genero genero = new genero();
        genero.genero = txtGenero.getText();
        genero.bitEstado =  (cxEstadoGenero.getSelectedIndex() == 0 ? true : false);

        String respuesta = dbConnect.InsertarNuevoGenero(genero);
        JOptionPane.showMessageDialog(this,respuesta,"Información",JOptionPane.INFORMATION_MESSAGE);
    }

    private void cargarTablaGeneros() throws SQLException{
      ResultSet rs =  dbConnect.cargarGeneros();

       jTableLibros.setModel(DbUtils.resultSetToTableModel(rs));
    }

    private void cargarTablaLibros() throws SQLException{
        ResultSet rs = dbConnect.cargarLibros();
        jTableLibros.setModel(DbUtils.resultSetToTableModel(rs));

        rs = dbConnect.cargarGeneros();

        while (rs.next()) {
            cxGeneroLibro.addItem(rs.getString("GENERO_DESC"));
        }

    }
    private void cargarTablaPrestamos() throws SQLException {

        ResultSet rs = dbConnect.ConsultarLibrosPrestados();

        //Cargar Tabla Salidas
        TablePrestamos.setModel(DbUtils.resultSetToTableModel(rs));

        //Para cargr combos
        CargarComboLibros();
        CargarComboTipoSalida();
    }
    private void CrearNuevaSalida() throws SQLException{
        salida salida = new salida();

        salida.cliente = txtCliente.getText();
        salida.id_Libro = cxLibro.getSelectedIndex();
        salida.id_Salida = cxTipoSalida.getSelectedIndex();
        salida.correo = txtCElectronico.getText();
        salida.comentario = txtComentario.getText();

        String respuesta = dbConnect.InsertarSalida(salida);
        JOptionPane.showMessageDialog(this,respuesta,"Información",JOptionPane.INFORMATION_MESSAGE);

    }

    private void CargarComboLibros() throws SQLException{
        ArrayList<String> libros = new ArrayList<String>();

        ResultSet rs = dbConnect.cargarLibros();

        while (rs.next()) {
            cxLibro.addItem(rs.getString("GENERO_DESC"));
        }
    }

    private void CargarComboTipoSalida() throws SQLException{
        ArrayList<String> libros = new ArrayList<String>();

        ResultSet rs = dbConnect.cargarTipoSalida();

        while (rs.next()) {
            cxTipoSalida.addItem(rs.getString("LIBRO"));
        }
    }
    public static void main(String[] args) {dashboard dashboard = new dashboard(null);}
}
