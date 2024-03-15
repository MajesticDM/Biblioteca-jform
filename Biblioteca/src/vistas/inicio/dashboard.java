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
import java.util.Objects;

import dbConexion.dbConnect;

public class dashboard extends JDialog {
    private JButton salirButton;
    private JButton loginButton;
    private  JTextPane panelLibrosPrestados;
    private  JTextPane panelLibrosVendidos;
    private  JTextPane panelLibrosObsequiados;
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
    private JButton CancelarGenero;
    private JButton CancelarLibro;
    private JTextField ID_GENERO;
    private JTextField ID_LIBRO;
    private JTextField IDX_GENERO_LIBRO;

    private static String[] estados = {"Activo","Inactivo"};

    public dashboard(JFrame parent) throws SQLException {
        super(parent);
        setTitle("Biblioteca");
        setContentPane(panelDashboard);
        setMinimumSize(new Dimension(800, 550));
        setModal(false);

        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setVisible(true);
        cargarDatosDeInicio();

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
                noEditarSalida();
            }


        });
        guardarSalida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CrearNuevaSalida();
                    cargarTablaPrestamos();
                    cargarDatosDeInicio();
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
                CancelarGenero.setEnabled(false);
                CancelarLibro.setEnabled(false);
                ID_GENERO.setVisible(false);
                ID_LIBRO.setVisible(false);
                IDX_GENERO_LIBRO.setVisible(false);

                cxEstadoGenero.removeAllItems();
                cxEstadoLibro.removeAllItems();
                for (String estado : estados) {
                    cxEstadoGenero.addItem(estado);
                    cxEstadoLibro.addItem(estado);
                }

                try {
                    cargarTablaLibros();
                    cargarTablaGeneros();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        GuardarGenero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    crearNuevoGenero();
                    cargarTablaGeneros();

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
                    cargarTablaLibros();
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

                txtCliente.setText(model.getValueAt(filaSeleccionada,3).toString());
                txtComentario.setText(model.getValueAt(filaSeleccionada,5).toString());
                txtCElectronico.setText(model.getValueAt(filaSeleccionada,6).toString());
                cxLibro.setSelectedIndex((Integer) model.getValueAt(filaSeleccionada,7) -1);
                cxTipoSalida.setSelectedIndex((Integer) model.getValueAt(filaSeleccionada,8) -1);
             }
        });
        JtableGeneros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                GuardarGenero.setEnabled(false);
                EditarGenero.setEnabled(true);
                CancelarGenero.setEnabled(true);

                DefaultTableModel model = (DefaultTableModel)JtableGeneros.getModel();
                int filaSeleccionada = JtableGeneros.getSelectedRow();

                ID_GENERO.setText(model.getValueAt(filaSeleccionada,0).toString());
                txtGenero.setText(model.getValueAt(filaSeleccionada,1).toString());
                cxEstadoGenero.setSelectedIndex(Objects.equals(model.getValueAt(filaSeleccionada, 2).toString(), "true") ? 0 : 1);
            }
        });
        CancelarGenero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuardarGenero.setEnabled(true);
                EditarGenero.setEnabled(false);
                CancelarGenero.setEnabled(false);
                txtGenero.setText("");
            }
        });
        EditarGenero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editarGeneroSeleccionado();
                    cargarTablaGeneros();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        CancelarLibro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarLibro.setEnabled(false);
                CancelarLibro.setEnabled(false);
                guardarLibro.setEnabled(true);
                txtNombreLibro.setText("");
            }
        });
        editarLibro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editarLibroSeleccionado();
                    cargarTablaLibros();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        jTableLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                editarLibro.setEnabled(true);
                CancelarLibro.setEnabled(true);
                guardarLibro.setEnabled(false);

                DefaultTableModel model = (DefaultTableModel)jTableLibros.getModel();
                int filaSeleccionada = jTableLibros.getSelectedRow();

                ID_LIBRO.setText(model.getValueAt(filaSeleccionada,0).toString());
                IDX_GENERO_LIBRO.setText(model.getValueAt(filaSeleccionada,1).toString());

                txtNombreLibro.setText(model.getValueAt(filaSeleccionada,2).toString());

                cxGeneroLibro.setSelectedIndex(Integer.parseInt(model.getValueAt(filaSeleccionada,1).toString()) - 1);
                cxEstadoLibro.setSelectedIndex(Objects.equals(model.getValueAt(filaSeleccionada, 3).toString(), "true") ? 0 : 1);



            }
        });
    }

    private void editarLibroSeleccionado() throws SQLException {
        libro libro = new libro();
        libro.id_libro = Integer.parseInt(ID_LIBRO.getText());
        libro.idx_genero = cxGeneroLibro.getSelectedIndex() + 1;
        libro.bitEstado = (cxEstadoLibro.getSelectedIndex() == 0);

        libro.libro = txtNombreLibro.getText();

        String respuesta = dbConnect.ActualizaLibro(libro);
        JOptionPane.showMessageDialog(this,respuesta,"Información",JOptionPane.INFORMATION_MESSAGE);
    }

    private void editarGeneroSeleccionado() throws SQLException {

        genero genero = new genero();
        genero.id_genero = Integer.parseInt(ID_GENERO.getText());
        genero.genero = txtGenero.getText();
        genero.bitEstado = (cxEstadoGenero.getSelectedIndex() == 0);

        String respuesta = dbConnect.ActualizaGenero(genero);
        JOptionPane.showMessageDialog(this,respuesta,"Información",JOptionPane.INFORMATION_MESSAGE);
    }

    private void crearNuevoLibro() throws SQLException {
        libro libro = new libro();
        libro.libro = txtNombreLibro.getText();
        libro.idx_genero = (cxGeneroLibro.getSelectedIndex() + 1);
        libro.bitEstado = (cxEstadoLibro.getSelectedIndex() == 0 ? true : false);

        String respuesta = dbConnect.InsertarNuevoLibro(libro);
        JOptionPane.showMessageDialog(this,respuesta,"Información",JOptionPane.INFORMATION_MESSAGE);
    }

    public void noEditarSalida(){
        JOptionPane.showMessageDialog(this,"Este libro ya generó factura, si cambias algo, la factura del cliente no serviría de nada.","Información",JOptionPane.INFORMATION_MESSAGE);
    }
    private void crearNuevoGenero()  throws SQLException{
        genero genero = new genero();
        genero.genero = txtGenero.getText();
        genero.bitEstado =  (cxEstadoGenero.getSelectedIndex() == 0);

        String respuesta = dbConnect.InsertarNuevoGenero(genero);
        JOptionPane.showMessageDialog(this,respuesta,"Información",JOptionPane.INFORMATION_MESSAGE);
    }

    private void cargarTablaGeneros() throws SQLException{
        GuardarGenero.setEnabled(true);
        EditarGenero.setEnabled(false);
        CancelarGenero.setEnabled(false);
        txtGenero.setText("");

      ResultSet rs =  dbConnect.cargarGeneros();

       JtableGeneros.setModel(DbUtils.resultSetToTableModel(rs));
    }

    private void cargarTablaLibros() throws SQLException{
        guardarLibro.setEnabled(true);
        editarLibro.setEnabled(false);
        CancelarLibro.setEnabled(false);
        txtNombreLibro.setText("");
        ResultSet rs = dbConnect.cargarLibros();
        jTableLibros.setModel(DbUtils.resultSetToTableModel(rs));

        rs = dbConnect.cargarGeneros();
        cxGeneroLibro.removeAllItems();

        while (rs.next()) {
            cxGeneroLibro.addItem(rs.getString("GENERO_DESCRIPCION"));
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
        cxLibro.removeAllItems();

        while (rs.next()) {
            cxLibro.addItem(rs.getString("NOMBRE_LIBRO"));
        }
    }

    private void cargarDatosDeInicio() throws SQLException{

        ResultSet rs =  dbConnect.cargarDatosInicio();

        while (rs.next()) {
            panelLibrosPrestados.setText(rs.getString("PRESTAMOS"));
            panelLibrosVendidos.setText(rs.getString("VENTAS"));
            panelLibrosObsequiados.setText(rs.getString("OBSEQUIOS"));
        }

    }
    private void CargarComboTipoSalida() throws SQLException{
        ArrayList<String> libros = new ArrayList<String>();

        ResultSet rs = dbConnect.cargarTipoSalida();
        cxTipoSalida.removeAllItems();

        while (rs.next()) {
            cxTipoSalida.addItem(rs.getString("NOMBRE_SALIDA"));
        }
    }
    public static void main(String[] args) throws SQLException { dashboard dashboard = new dashboard(null);}
}
