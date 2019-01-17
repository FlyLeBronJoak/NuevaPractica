

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author xp
 */
public class GestorConexion {

    Connection conn1;

    public GestorConexion() {
        conn1 = null;

        try {
            String url1 = "jdbc:mysql://localhost:3306/discografica"
                    + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            String user = "root";
            String password = "root";

            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Conectado a discografica...");
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: dirección o usuario/clave no válida");
            ex.printStackTrace();
        }
    }

    public void cerrar_conexion() {
        try {
            conn1.close();
            System.out.println("Conexion cerrada");
        } catch (Exception e) {
            System.out.println("Error al cerrar la conexion");
            e.printStackTrace();
        }

    }

    public void annadirColumna() {
        try {
            Statement sta = conn1.createStatement();
            sta.executeUpdate("ALTER TABLE album ADD caratula VARCHAR(10)");
            sta.close();

            System.out.println("Columna añadida");

        } catch (Exception e) {
            System.out.println("Error al añadir columna");
        }
    }

    public void insertar() {
        try {
            Statement sta = conn1.createStatement();
            sta.executeUpdate("INSERT INTO album VALUES (3, 'Greatest Hits', '2019')");
            sta.close();
            System.out.println("Se ha insertado con exito");
        } catch (Exception e) {
            System.out.println("ERROR al hacer un Insert");
            e.printStackTrace();
        }
    }

    public void consulta_Statement() {
        try {
            Statement sta = conn1.createStatement();
            String query = "SELECT * FROM album WHERE titulo like 'TE%'";
            ResultSet rs = sta.executeQuery(query);
            while (rs.next()) {
                System.out.println("ID - " + rs.getInt("id") + ", Título " + rs.getString("titulo")
                        + ", Autor " + rs.getString("autor"));
            }
            rs.close();
            sta.close();
        } catch (SQLException ex) {
            System.out.println("ERROR:al consultar");
            ex.printStackTrace();
        }
    }

    public void consulta_preparedStatement() {
        try {
            String query = "SELECT * FROM album WHERE titulo like ?";
            PreparedStatement pst = conn1.prepareStatement(query);
            pst.setString(1, "B%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                System.out.println("ID - " + rs.getInt("id") + ", Título " + rs.getString("titulo")
                        + ", Autor " + rs.getString("autor"));
            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            System.out.println("ERROR:al consultar");
            ex.printStackTrace();
        }
    }

    public void insertar_con_commit() {
        try {
            conn1.setAutoCommit(false);
            Statement sta = conn1.createStatement();
            sta.executeUpdate("INSERT INTO album " + "VALUES (5, ‘Let it be', ‘The Beatles')");
            sta.executeUpdate("INSERT INTO album " + "VALUES (6, 'Abbey Road', ‘The Beatles')");
            conn1.commit();
        } catch (SQLException ex) {
            System.out.println("ERROR:al hacer un Insert");
            try {
                if (conn1 != null) {
                    conn1.rollback();
                }
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            ex.printStackTrace();
        }
    }

}
