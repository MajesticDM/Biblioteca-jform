package vistas.inicio;

import clases.genero;
import clases.libro;
import clases.salida;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.mysql.cj.jdbc.result.ResultSetImpl;
import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import dbConexion.dbConnect;
import vistas.login.login;

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
    private JPanel panelOnline;

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
        consultarSiEstoyEnLinea();
        cargarDatosDeInicio();

        PanelInicio.addComponentListener(new ComponentAdapter() {
        });
        PanelInicio.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    consultarSiEstoyEnLinea();
                    cargarTablaPrestamos();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        guardarSalida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    if(txtCliente.getText().isEmpty() || txtCElectronico.getText().isEmpty() || txtComentario.getText().isEmpty()){
                        JOptionPane.showMessageDialog(null,"Todos los campos son obligatorios","Campos vacíos",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    consultarSiEstoyEnLinea();
                    CrearNuevaSalida();
                    cargarTablaPrestamos();
                    cargarDatosDeInicio();
                } catch (SQLException ex) {
                    return;
                } catch (DocumentException | FileNotFoundException ex) {
                    throw new RuntimeException(ex);
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
                    consultarSiEstoyEnLinea();
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
                    if(txtGenero.getText().isEmpty()){
                        JOptionPane.showMessageDialog(null,"El género es obligatorio","Campo vacío",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    consultarSiEstoyEnLinea();
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
                    if(txtNombreLibro.getText().isEmpty()){
                        JOptionPane.showMessageDialog(null,"El nombre del Libro es obligatorio","Campo vacío",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    consultarSiEstoyEnLinea();
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
                    consultarSiEstoyEnLinea();
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
                    consultarSiEstoyEnLinea();
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
        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                login login = new login(null);
                login.setVisible(true);
            }
        });
        editarSalida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                noEditarSalida();
            }
        });
    }

    private void crearFacturaSalida(salida salida) throws FileNotFoundException, DocumentException {

        LocalDateTime FechaLocal = LocalDateTime.now();
        DateTimeFormatter FechaParaFormato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String FechaFormateada = FechaLocal.format(FechaParaFormato);

        Document doc = new Document();

        String nombreDocumento = cxTipoSalida.getSelectedItem() + "_"+ salida.cliente.replace(" ", "-") +"_"+ cxLibro.getSelectedItem().toString().replace(" ", "")+ "_" + FechaFormateada.replace(" ", "-").replace(":","-");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar factura");

        fileChooser.setSelectedFile(new File(nombreDocumento));

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos PDF", "pdf");
        fileChooser.setFileFilter(filter);

        int seleccion = fileChooser.showSaveDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();

            try {
                String nombreArchivo = archivoSeleccionado.getAbsolutePath();
                if (!nombreArchivo.toLowerCase().endsWith(".pdf")) {
                    nombreArchivo += ".pdf";
                }

                PdfWriter pdf = PdfWriter.getInstance(doc, new FileOutputStream(nombreArchivo));

                doc.open();

                PdfContentByte cb = pdf.getDirectContent();
                Graphics g = cb.createGraphicsShapes(PageSize.LETTER.getWidth(),PageSize.LETTER.getHeight());

                g.setColor(Color.black);
                g.drawRect(1, 1, 593, 790);

                g.setColor(new Color(148, 61, 0));
                g.fillOval(290, 90, 280, 100);

                Font font1 = new Font("Tahoma", Font.BOLD + Font.ITALIC, 35);
                g.setFont(font1);

                g.setColor(Color.BLACK);
                g.drawString("Gracias por su compra", 40, 150);

                g.setColor(Color.WHITE);
                g.drawString("compra", 305, 150);

                font1 = new Font("Tahoma", Font.BOLD + Font.ITALIC, 25);
                g.setFont(font1);

                g.setColor(Color.BLACK);
                g.drawString("Datos de la salida", 70, 300);

                //Datos de la Salida

                font1 = new Font("Tahoma", Font.BOLD + Font.ITALIC, 15);
                g.setFont(font1);
                g.setColor(Color.BLACK);
                g.drawString("Cliente - " + salida.cliente, 70, 330);
                g.setColor(Color.BLACK);
                g.drawString("Correo - "+ salida.correo, 70, 360);
                g.setColor(Color.BLACK);
                g.drawString("Libro - "+ cxLibro.getSelectedItem(), 70, 390);
                g.setColor(Color.BLACK);
                g.drawString("Fecha salida - "+ FechaFormateada, 70, 450);
                g.setColor(Color.BLACK);
                g.drawString("Tipo de salida - "+ cxTipoSalida.getSelectedItem(), 70, 480);
                g.setColor(Color.BLACK);
                g.drawString("Nota - "+ salida.comentario, 70, 510);

                //Icono QR para ir a Github
                try{
                    ImageIcon img1 = new ImageIcon(Objects.requireNonNull(getClass().getResource("qrcode_github.com.png")));
                    g.drawImage(img1.getImage(), 480, 670, 90, 90, null);
                }catch (Exception e){
                    JOptionPane.showMessageDialog(this,"El error es la ubicación de la img");
                }


                Font font2 = new Font("Tahoma", Font.PLAIN, 7);
                g.setFont(font2);
                g.setColor(Color.BLACK);
                g.drawString("Escanea el código QR para conocer más acerca ", 430, 770);
                g.drawString("del desarrollador", 480, 780);

                doc.close();
                pdf.close();

                JOptionPane.showMessageDialog(null, "Salida guardada correctamente en: " + nombreArchivo);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al guardar la factura: " + e);
            }
        }
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
        JOptionPane.showMessageDialog(this,"Cada salida genera factura, si cambias algo, la factura del cliente no serviría de nada.","Información",JOptionPane.INFORMATION_MESSAGE);
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


        //Tengo dos columnas que son ID, por eso las oculto pero no las elimino
        TableColumn columna = jTableLibros.getColumnModel().getColumn(1);
        columna.setMinWidth(0);
        columna.setMaxWidth(0);
        columna.setWidth(0);
        columna.setPreferredWidth(0);
        columna.setResizable(false);

        TableColumn columna2 = jTableLibros.getColumnModel().getColumn(4);
        columna2.setMinWidth(0);
        columna2.setMaxWidth(0);
        columna2.setWidth(0);
        columna2.setPreferredWidth(0);
        columna2.setResizable(false);

        rs = dbConnect.cargarGeneros();
        cxGeneroLibro.removeAllItems();

        while (rs.next()) {
            cxGeneroLibro.addItem(rs.getString("GENERO_DESCRIPCION"));
        }

    }
    private void cargarTablaPrestamos() throws SQLException {
        ResultSet rs;
        try {
     rs = dbConnect.ConsultarLibrosPrestados();
}catch (Exception ex){
     consultarSiEstoyEnLinea();
            rs = dbConnect.ConsultarLibrosPrestados();
}


        //Cargar Tabla Salidas
        TablePrestamos.setModel(DbUtils.resultSetToTableModel(rs));

        //Tengo dos columnas que son ID, por eso las oculto pero no las elimino
        TableColumn columna = TablePrestamos.getColumnModel().getColumn(7);
        columna.setMinWidth(0);
        columna.setMaxWidth(0);
        columna.setWidth(0);
        columna.setPreferredWidth(0);
        columna.setResizable(false);

        TableColumn columna2 = TablePrestamos.getColumnModel().getColumn(8);
        columna2.setMinWidth(0);
        columna2.setMaxWidth(0);
        columna2.setWidth(0);
        columna2.setPreferredWidth(0);
        columna2.setResizable(false);

        //Para cargr combos
        CargarComboLibros();
        CargarComboTipoSalida();
    }
    private void CrearNuevaSalida() throws SQLException, DocumentException, FileNotFoundException {
        salida salida = new salida();

        salida.cliente = txtCliente.getText();
        salida.id_Libro = cxLibro.getSelectedIndex();
        salida.id_Salida = cxTipoSalida.getSelectedIndex();
        salida.correo = txtCElectronico.getText();
        salida.comentario = txtComentario.getText();

        String respuesta = dbConnect.InsertarSalida(salida);

        JOptionPane.showMessageDialog(this,respuesta,"Información",JOptionPane.INFORMATION_MESSAGE);

        crearFacturaSalida(salida);
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

    private void consultarSiEstoyEnLinea() throws SQLException{

        boolean conectado = dbConnect.consultarOnline();

        if(conectado){
         panelOnline.setBackground(Color.green);
        }else{
            JOptionPane.showMessageDialog(this,"Parece que no estás en línea, revisa la conexión a base de datos","Sin conexion",JOptionPane.ERROR_MESSAGE);
            panelOnline.setBackground(Color.red);
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
