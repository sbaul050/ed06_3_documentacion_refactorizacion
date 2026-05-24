package org.ed06.model;

import java.time.LocalDate;
import java.util.*;

public class Hotel {
    private String nombre;
    private String direccion;
    private String telefono;

    private final Map<Integer,Cliente> clientes = new HashMap<>();
    private final List<Habitacion> habitaciones = new ArrayList<>();
    private final Map<Integer,List<Reserva>> reservasPorHabitacion = new HashMap<>();

    public Hotel(String nombre, String direccion, String telefono) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    // Método para agregar una nueva habitación al hotel
    public void registrarHabitacion(String tipo, double precioBase) {
        Habitacion habitacion = new Habitacion(habitaciones.size() + 1, tipo, precioBase);
        habitaciones.add(habitacion);
        reservasPorHabitacion.put(habitacion.getNumero(), new ArrayList<>());
    }



    public void listarHabitacionesDisponibles() {
        for(Habitacion habitacion : habitaciones) {
            if(habitacion.isDisponible()) {
                System.out.println("Habitación #" + habitacion.getNumero() + " - Tipo: " + habitacion.getTipo() + " - Precio base: " + habitacion.getPrecioBase());
            }
        }
    }

    public Habitacion getHabitacion(int numero) {
        for(Habitacion habitacion : habitaciones) {
            if(habitacion.getNumero() == numero) {
                return habitacion;
            }
        }
        return null;
    }

    //Método para realizar una reserva.
    // Comprueba si hay habitaciones disponibles, si existe el cliente y si las fechas son coherentes.
    // Si encuentra una habitación disponible del tipo solicitado,
    // crea una nueva reserva y la añade a la lista de reservas y devuelve el número de la habitación reservada.
    // Antes de crear la reserva, comprueba si el cliente pasa a ser VIP tras la nueva reserva,
    // en caso de que haya realizado más de 3 reservas en el último año.
    public int reservarHabitacion(int clienteId, String tipo, LocalDate fechaEntrada, LocalDate fechaSalida) {
        // Comprobamos si hay habitaciones en el hotel
        if(habitaciones.isEmpty()){
            System.out.println("No hay habitaciones en el hotel");
            return -1;
        }

        Cliente cliente = this.clientes.get(clienteId);

        if (this.clientes.get(clienteId) == null){
            System.out.println("No existe el cliente con id " + clienteId);
            return -1;
        }

        if(!fechaEntrada.isBefore(fechaSalida)){
            System.out.println("La fecha de entrada es posterior a la fecha de salida");
            return -1;
        }

        for(Habitacion habitacion : habitaciones) {
            if(habitacion.getTipo().equals(tipo.toUpperCase()) && habitacion.isDisponible()) {
                // Comprobamos si el cliente pasa a ser vip tras la nueva reserva

                cliente.nuevaReserva();
                if(cliente.numReservas > 3 && !cliente.esVip) {
                    cliente.esVip = true;
                    System.out.println("El cliente " + cliente.nombre + " ha pasado a ser VIP");
                }

                // Creamos la reserva
                Reserva reserva = new Reserva(reservasPorHabitacion.size() + 1, habitacion, cliente, fechaEntrada, fechaSalida);
                reservasPorHabitacion.get(habitacion.getNumero()).add(reserva);
                // Marcamos la habitación como no disponible
                habitacion.reservar();

                System.out.println("Reserva realizada con éxito");
                return habitacion.getNumero();
            }
        }

        System.out.println("No hay habitaciones disponibles del tipo " + tipo);
        return -1;
    }

    public void listarReservas() {
        reservasPorHabitacion.forEach((key, value) -> {
            System.out.println("Habitación #" + key);

            for(Reserva reserva: value){
                reserva.mostrarReserva();
            }
        });
    }

    public void listarClientes() {
        for(Cliente cliente : clientes.values()) {
            System.out.println("Cliente #" + cliente.id + " - Nombre: " + cliente.nombre + " - DNI: " + cliente.dni + " - VIP: " + cliente.esVip);
        }
    }

    public void registrarCliente(String nombre, String email, String dni, boolean esVip) {
        Cliente cliente = new Cliente(clientes.size() + 1, nombre, dni, email, esVip);
        clientes.put(cliente.id, cliente);
    }
}
