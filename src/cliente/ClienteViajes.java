package cliente;

import gestor.Viaje;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Scanner;

public class ClienteViajes {
    public static int menu(Scanner teclado) {
        int opcion;
        System.out.println("\n\n");
        System.out.println("=====================================================");
        System.out.println("============            MENU        =================");
        System.out.println("=====================================================");
        System.out.println("0. Salir");
        System.out.println("1. Consultar viajes con un origen dado");
        System.out.println("2. Reservar un viaje");
        System.out.println("3. Anular una reserva");
        System.out.println("4. Ofertar un viaje");
        System.out.println("5. Borrar un viaje");
        do {
            System.out.print("\nElige una opcion (0..5): ");
            opcion = teclado.nextInt();
        } while ((opcion < 0) || (opcion > 5));
        teclado.nextLine(); // Elimina retorno de carro del buffer de entrada
        return opcion;
    }


    /**
     * Programa principal. Muestra el menú repetidamente y atiende las peticiones del cliente.
     *
     * @param args no se usan argumentos de entrada al programa principal
     */
    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        // Crea un gestor de viajes
        AuxiliarClienteViajes aux = null;
        try {
            aux = new AuxiliarClienteViajes("localhost", "12345");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONObject obj;

        System.out.print("Introduce tu codigo de cliente: ");
        String codcli = teclado.nextLine();

        int opcion;
        do {
            opcion = menu(teclado);
            switch (opcion) {
                case 0: // Guardar los datos en el fichero y salir del programa

                    // POR IMPLEMENTAR
                    aux.cierraSesion();
                    System.out.println("Cerrando sesion...");
                    break;
                case 1: { // Consultar viajes con un origen dado
                    // POR IMPLEMENTAR
                    System.out.print("Indica el origen del viaje: ");
                    String origen = teclado.nextLine();
                    JSONArray list = aux.consultaViajes(origen);
                    if (list.isEmpty()) {
                        System.out.println("No hay viajes con ese origen.");
                    } else {


                        for (Object object : list) {
                            if (object instanceof JSONObject) {
                                JSONObject viaje = (JSONObject) object;
                                System.out.println(formatearViaje(viaje));
                            }
                        }
                    }
                    break;
                }

                case 2: { // Reservar un viaje

                    // POR IMPLEMENTAR
                    System.out.print("Indica el viaje a reservar: ");
                    String codviaje = teclado.nextLine();

                    obj = aux.reservaViaje(codviaje, codcli);
                    if (obj.get("codviaje") != null) {
                        System.out.println(formatearViaje(obj));

                    } else {
                        System.out.println("No hay viajes con ese código.");

                    }

                    break;

                }

                case 3: { // Anular una reserva

                    // POR IMPLEMENTAR
                    System.out.print("Indica el viaje que quieres anular: ");
                    String codviaje = teclado.nextLine();
                    obj = aux.anulaReserva(codviaje, codcli);
                    System.out.println(obj);
                    break;
                }

                case 4: { // Ofertar un viaje

                    // POR IMPLEMENTAR
                    System.out.print("Dame el origen del viaje: ");
                    String origen = teclado.nextLine();
                    System.out.print("Dame el destino del viaje: ");
                    String destino = teclado.nextLine();
                    System.out.print("Dime la fecha del viaje(dd-MM-yyyy): ");
                    String fecha = teclado.nextLine();
                    System.out.print("Dime el precio del viaje: ");
                    long precio = teclado.nextLong();
                    System.out.print("Dime el numero de plazas disponibles: ");
                    long plazas = teclado.nextLong();
                    obj = aux.ofertaViaje(codcli, origen, destino, fecha, precio, plazas);
                    System.out.println(obj);
                    break;
                }

                case 5: { // Borrar un viaje ofertado

                    // POR IMPLEMENTAR
                    System.out.print("Indica el viaje que quieres borrar: ");
                    String codviaje = teclado.nextLine();
                    obj = aux.borraViaje(codviaje, codcli);
                    System.out.println(obj);
                    break;
                }

            } // fin switch

        } while (opcion != 0);

    } // fin de main

    private static String formatearViaje(JSONObject viaje) {
        String resultado = "Código del Viaje: " + viaje.get("codviaje") +
                ", Origen: " + viaje.get("origen") +
                ", Destino: " + viaje.get("destino") +
                ", Fecha: " + viaje.get("fecha") +
                ", Precio: " + viaje.get("precio") +
                ", Plazas Disponibles: " + viaje.get("numplazas");

        JSONArray pasajeros = (JSONArray) viaje.get("pasajeros");
        if (pasajeros != null && !pasajeros.isEmpty()) {
            resultado += ", Pasajeros: ";
            for (int i = 0; i < pasajeros.size(); i++) {
                resultado += pasajeros.get(i).toString();
                if (i < pasajeros.size() - 1) {
                    resultado += ", ";
                }
            }
        } else {
            resultado += ", Pasajeros: Ninguno";
        }

        return resultado;
    }


} // fin class