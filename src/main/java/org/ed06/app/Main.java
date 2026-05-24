package org.ed06.app;

import org.ed06.model.Cliente;
import org.ed06.model.Habitacion;
import org.ed06.model.Hotel;


import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Variales locales
        // Definimos las diferentes opciones del menú
        String[] opciones = {
                "1. Registrar habitación",
                "2. Listar habitaciones disponibles",
                "3. Reservar habitación",
                "4. Listar reservas",
                "5. Listar clientes",
                "6. Registrar cliente",
                "7. Salir"};

        String[] tiposHabitacion = {"SIMPLE", "DOBLE", "SUITE", "LITERAS"};
        boolean salir = false;

        // Creamos un menú para el administrador con las diferentes opciones proporcionadas
        Hotel hotel = new Hotel("El mirador", "Calle Entornos de Desarrollo 6", "123456789");

        valoresIniciales(hotel);

        // Mostramos el menú
        while (!salir) {
            mostrarMenu(opciones);
            int opcion = scanner.nextInt();
            scanner.nextLine();
            switch (opcion) {
                case 1:
                    registrarHabitacion(hotel, tiposHabitacion);
                    break;
                case 2:
                    hotel.listarHabitacionesDisponibles();
                    break;
                case 3:
                    reservarHabitacion(hotel, tiposHabitacion);
                    break;
                case 4:
                    hotel.listarReservas();
                    break;
                case 5:
                    hotel.listarClientes();
                    break;
                case 6:
                    registrarClientee(hotel);
                    break;
                case 7:
                    System.out.println("Saliendo del programa...");
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida");
                    break;
            }
        }
        scanner.close();
    }

    private static void mostrarMenu(String[] opcion) {
        System.out.println("Menú:");
        for(String menu : opcion){
            System.out.println(menu);
        }
    }

    public static void valoresIniciales(Hotel hotel){
        // Registramos algunas habitaciones
        hotel.registrarHabitacion("SIMPLE", 50);
        hotel.registrarHabitacion("DOBLE", 80);
        hotel.registrarHabitacion("SUITE", 120);
        hotel.registrarHabitacion("LITERAS", 200);
        hotel.registrarHabitacion("SIMPLE", 65);
        hotel.registrarHabitacion("DOBLE", 100);
        hotel.registrarHabitacion("SUITE", 150);
        hotel.registrarHabitacion("LITERAS", 250);


        // Registramos algunos clientes
        hotel.registrarCliente("Daniel", "daniel@daniel.com", "12345678A", true);
        hotel.registrarCliente("Adrián", "adrian@adrian.es", "87654321B", false);
    }

    public static void registrarHabitacion(Hotel hotel, String[] tiposHabitacion){
        String tipo;
        double precioBase;
        boolean tipoExsiste = false;

        do{
            System.out.println("Introduce el tipo de habitación (SIMPLE, DOBLE, SUITE, LITERAS): ");
            tipo = scanner.nextLine().toUpperCase();

            for(String tipos : tiposHabitacion){
                if (tipo.equals(tipos)){
                    tipoExsiste = true;
                }
            }
        }while(!tipoExsiste);

        System.out.println("Introduce el precio base de la habitación: ");
        precioBase = scanner.nextDouble();

        hotel.registrarHabitacion(tipo, precioBase);
        System.out.println("Habitación registrada: " + tipo + " - Precio base: " + precioBase);
    }

    public static void reservarHabitacion(Hotel hotel, String[] tiposHabitacion){
        int clienteId;
        int numeroHabitacion;
        String tipoHabitacion;

        System.out.println("Introduce el id del cliente: ");
        clienteId = scanner.nextInt();

        System.out.println("Introduce el tipo de habitación (SIMPLE, DOBLE, SUITE, LITERA): ");
        tipoHabitacion = scanner.next();

        LocalDate fechaEntrada = getFechaEntrada();

        LocalDate fechaSalida = getFechaSalida();

        numeroHabitacion = hotel.reservarHabitacion(clienteId, tipoHabitacion, fechaEntrada,
                fechaSalida);

        if (numeroHabitacion != -1){
            System.out.println("Datos de la habitacion");
            Habitacion habitacion = hotel.getHabitacion(numeroHabitacion);
            System.out.println(
                    "Habitación #" + habitacion.getNumero() + " - Tipo: " + habitacion.getTipo()
                            + " - Precio base: " + habitacion.getPrecioBase());
            System.out.println("Número de habitación reservada: " + numeroHabitacion);
        }
    }

    private static LocalDate getFechaEntrada() {
        int mes;
        int anio;
        int dia;
        System.out.println("Introduce la fecha de entrada (año): ");
        anio = scanner.nextInt();

        System.out.println("Introduce la fecha de entrada (mes): ");
        mes = scanner.nextInt();

        System.out.println("Introduce la fecha de entrada (día): ");
        dia = scanner.nextInt();

        LocalDate fechaEntrada = LocalDate.of(anio, mes, dia);
        return fechaEntrada;
    }

    private static LocalDate getFechaSalida() {
        int anio;
        int mes;
        int dia;
        System.out.println("Introduce la fecha de salida (año): ");
        anio = scanner.nextInt();

        System.out.println("Introduce la fecha de salida (mes): ");
        mes = scanner.nextInt();

        System.out.println("Introduce la fecha de salida (día): ");
        dia = scanner.nextInt();

        LocalDate fechaSalida = LocalDate.of(anio, mes, dia);
        return fechaSalida;
    }

    public static void registrarClientee(Hotel hotel){
        String nombre;
        String email;
        String dni;

        while(true) {
            try {
                System.out.println("Introduce el nombre del cliente: ");
                nombre = scanner.next();
                Cliente.validarNombre(nombre);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Nombre no válido. Inténtalo de nuevo.");
            }
        }
        while (true) {
            try {
                System.out.println("Introduce el email del cliente: ");
                email = scanner.next();
                Cliente.validarEmail(email);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Email no válido. Inténtalo de nuevo.");
            }
        }
        while (true) {
            try {
                System.out.println("Introduce el DNI del cliente: ");
                dni = scanner.next();
                Cliente.validarDni(dni);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("DNI no válido. Inténtalo de nuevo.");
            }
        }
        System.out.println("¿Es VIP? (true/false): ");
        boolean esVip = scanner.nextBoolean();
        hotel.registrarCliente(nombre, email, dni, esVip);
    }
}