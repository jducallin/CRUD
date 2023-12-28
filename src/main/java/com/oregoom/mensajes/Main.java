package com.oregoom.mensajes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Realizar la conexión una vez al inicio
            Connection conectar = DriverManager.getConnection(
                    "jdbc:mysql://localhost/mensajes_db?serverTimezone=UTC",
                    "root",
                    "123456789");

            System.out.println("Conexión exitosa");

            while (true) {
                // Menú de opciones
                System.out.println("Seleccione una acción:");
                System.out.println("1. Consultar registros");
                System.out.println("2. Insertar un nuevo mensaje");
                System.out.println("3. Actualizar un mensaje existente");
                System.out.println("4. Eliminar un mensaje existente");
                System.out.println("0. Salir");

                // Obtener la opción del usuario
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir la nueva línea después del entero

                switch (opcion) {
                    case 1:
                        // Listar registros
                        listarRegistros(conectar);
                        break;
                    case 2:
                        // Insertar un nuevo mensaje
                        System.out.println("Insertar un nuevo mensaje:");
                        System.out.print("Mensaje: ");
                        String nuevoMensaje = scanner.nextLine();
                        System.out.print("Autor: ");
                        String nuevoAutor = scanner.nextLine();
                        System.out.print("Fecha (YYYY-MM-DD): ");
                        String nuevaFecha = scanner.nextLine();
                        insertarRegistro(conectar, nuevoMensaje, nuevoAutor, nuevaFecha);
                        listarRegistros(conectar);
                        break;
                    case 3:
                        // Actualizar un mensaje existente
                        System.out.println("Actualizar un mensaje existente:");
                        System.out.print("ID del mensaje a actualizar: ");
                        int idActualizar = scanner.nextInt();
                        scanner.nextLine(); // Consumir la nueva línea después del entero
                        System.out.print("Nuevo mensaje: ");
                        String mensajeActualizar = scanner.nextLine();
                        System.out.print("Nuevo autor: ");
                        String autorActualizar = scanner.nextLine();
                        System.out.print("Nueva fecha (YYYY-MM-DD): ");
                        String fechaActualizar = scanner.nextLine();
                        actualizarRegistro(conectar, idActualizar, mensajeActualizar, autorActualizar, fechaActualizar);
                        listarRegistros(conectar);
                        break;
                    case 4:
                        // Eliminar un mensaje existente
                        System.out.println("Eliminar un mensaje existente:");
                        System.out.print("ID del mensaje a eliminar: ");
                        int idEliminar = scanner.nextInt();
                        scanner.nextLine(); // Consumir la nueva línea después del entero
                        eliminarRegistro(conectar, idEliminar);
                        listarRegistros(conectar);
                        break;
                    case 0:
                        // Salir del programa
                        System.out.println("Saliendo del programa. ¡Hasta luego!");
                        // Cerrar la conexión antes de salir
                        conectar.close();
                        scanner.close();
                        System.exit(0);
                    default:
                        // Opción no válida
                        System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
                }
            }
        } catch (SQLException e) {
            // Manejar excepciones SQL
            e.printStackTrace();
        }
    }

    // Método para listar registros en la base de datos
    static void listarRegistros(Connection conectar) throws SQLException {
        try (
            PreparedStatement ps = conectar.prepareStatement("SELECT * FROM mensajes");
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                // Obtener datos del resultado y mostrarlos
                int id = rs.getInt("id_mensaj");
                String mensaje = rs.getString("mensaje");
                String autor = rs.getString("autor");
                String fecha = rs.getString("fecha");

                System.out.printf("%d %s %s %s%n", id, mensaje, autor, fecha);
            }
        }
    }

    // Método para insertar un nuevo registro en la base de datos
    static void insertarRegistro(Connection conectar, String mensaje, String autor, String fecha) throws SQLException {
        try (
            PreparedStatement ps = conectar.prepareStatement(
                    "INSERT INTO mensajes (mensaje, autor, fecha) VALUES (?, ?, ?)")
        ) {
            ps.setString(1, mensaje);
            ps.setString(2, autor);
            ps.setString(3, fecha);

            // Ejecutar la inserción y mostrar mensaje de éxito o fracaso
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Nuevo mensaje insertado correctamente.");
            } else {
                System.out.println("No se pudo insertar el mensaje.");
            }
        }
    }

    // Método para actualizar un registro existente en la base de datos
    static void actualizarRegistro(Connection conectar, int idMensaje, String nuevoMensaje, String nuevoAutor, String nuevaFecha)
            throws SQLException {
        try (
            PreparedStatement ps = conectar.prepareStatement(
                    "UPDATE mensajes SET mensaje = ?, autor = ?, fecha = ? WHERE id_mensaj = ?")
        ) {
            ps.setString(1, nuevoMensaje);
            ps.setString(2, nuevoAutor);
            ps.setString(3, nuevaFecha);
            ps.setInt(4, idMensaje);

            // Ejecutar la actualización y mostrar mensaje de éxito o fracaso
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Mensaje actualizado correctamente.");
            } else {
                System.out.println("No se pudo actualizar el mensaje. Puede que el ID no exista.");
            }
        }
    }

    // Método para eliminar un registro existente en la base de datos
    static void eliminarRegistro(Connection conectar, int idMensaje) throws SQLException {
        try (
            PreparedStatement ps = conectar.prepareStatement(
                    "DELETE FROM mensajes WHERE id_mensaj = ?")
        ) {
            ps.setInt(1, idMensaje);

            // Ejecutar la eliminación y mostrar mensaje de éxito o fracaso
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Mensaje eliminado correctamente.");
            } else {
                System.out.println("No se pudo eliminar el mensaje. Puede que el ID no exista.");
            }
        }
    }
}