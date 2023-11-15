package servidor;

import java.io.IOException;
import java.net.SocketException;


import comun.MyStreamSocket;
import gestor.GestorViajes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Clase ejecutada por cada hebra encargada de servir a un cliente del servicio de viajes.
 * El metodo run contiene la logica para gestionar una sesion con un cliente.
 */

class HiloServidorViajes implements Runnable {


    private MyStreamSocket myDataSocket;
    private GestorViajes gestor;

    /**
     * Construye el objeto a ejecutar por la hebra para servir a un cliente
     *
     * @param    myDataSocket    socket stream para comunicarse con el cliente
     * @param    unGestor        gestor de viajes
     */
    HiloServidorViajes(MyStreamSocket myDataSocket, GestorViajes unGestor) {
        // POR IMPLEMENTAR
        this.myDataSocket = myDataSocket;
        gestor = unGestor;
    }

    /**
     * Gestiona una sesion con un cliente
     */
    public void run() {
        String operacion = "0";
        boolean done = false;
        // ...
        try {
            while (!done) {
                // Recibe una petición del cliente
                String peticion = myDataSocket.receiveMessage();
                System.out.println(peticion);

                // Extrae la operación y sus parámetros
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(peticion);
                operacion = jsonObject.get("peticion").toString();

                switch (operacion) {
                    case "0":
                        gestor.guardaDatos();
                        done = true;
                        myDataSocket.close();
                        break;

                    case "1": { // Consulta los viajes con un origen dado
                        JSONArray viajes = gestor.consultaViajes((String) jsonObject.get("origen"));
                        System.out.println(viajes.toJSONString());
                        if (viajes != null) {
                            myDataSocket.sendMessage(viajes.toJSONString());
                        }
                        break;
                    }
                    case "2": { // Reserva una plaza en un viaje
                        try {
                            JSONObject viaje = gestor.reservaViaje(jsonObject.get("codviaje").toString(), jsonObject.get("codprop").toString());
                            if (viaje != null) {
                                myDataSocket.sendMessage(viaje.toJSONString());
                            } else {
                                JSONObject respuestaError = new JSONObject();
                                respuestaError.put("error", "La reserva no pudo realizarse.");
                                myDataSocket.sendMessage(respuestaError.toJSONString());
                            }
                        } catch (Exception e) {
                            System.out.println("Exception en reserva: " + e.getMessage());
                        }
                        break;
                    }

                    case "3": { // Pone en venta un articulo
                        // ...

                        break;
                    }
                    case "4": { // Oferta un viaje
                        // ...

                        break;
                    }
                    case "5": { // Borra un viaje
                        try {
                            JSONObject viaje = gestor.borraViaje(jsonObject.get("codviaje").toString(), jsonObject.get("codprop").toString());
                            if (viaje != null) {
                                System.out.println(viaje);
                                myDataSocket.sendMessage(viaje.toJSONString());
                            } else {
                                JSONObject respuestaError = new JSONObject();
                                respuestaError.put("error", "La reserva no pudo realizarse.");
                                myDataSocket.sendMessage(respuestaError.toJSONString());
                            }
                        } catch (Exception e) {
                            System.out.println("Exception en reserva: " + e.getMessage());
                        }
                        break;
                    }

                } // fin switch
            } // fin while
        } // fin try
        catch (SocketException ex) {
            System.out.println("Capturada SocketException");
        } catch (IOException ex) {
            System.out.println("Capturada IOException");
        } catch (Exception ex) {
            System.out.println("Exception caught in thread: " + ex);
        } // fin catch
    } //fin run

} //fin class 