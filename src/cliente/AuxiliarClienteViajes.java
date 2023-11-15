package cliente;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import comun.MyStreamSocket;

/**
 * Esta clase es un modulo que proporciona la logica de aplicacion
 * para el Cliente del sevicio de viajes usando sockets de tipo stream
 */

public class AuxiliarClienteViajes {

	private final MyStreamSocket mySocket; // Socket de datos para comunicarse con el servidor
	JSONParser parser;

	/**
	 * Construye un objeto auxiliar asociado a un cliente del servicio.
	 * Crea un socket para conectar con el servidor.
	 * @param	hostName	nombre de la maquina que ejecuta el servidor
	 * @param	portNum		numero de puerto asociado al servicio en el servidor
	 */
	AuxiliarClienteViajes(String hostName, String portNum) 
			throws SocketException, UnknownHostException, IOException {

		// IP del servidor
		InetAddress serverHost = InetAddress.getByName(hostName);
		// Puerto asociado al servicio en el servidor
		int serverPort = Integer.parseInt(portNum);
		//Instantiates a stream-mode socket and wait for a connection.
		this.mySocket = new MyStreamSocket(serverHost, serverPort);
		/**/  System.out.println("Hecha peticion de conexion");
		parser = new JSONParser();
	} // end constructor

	/**
	 * Devuelve los viajes ofertados desde un origen dado
	 * 
	 * @param origen	origen del viaje ofertado
	 * @return array JSON de viajes desde un origen. array vacio si no hay ninguno
	 */
	public JSONArray consultaViajes(String origen) {

		JSONObject consulta = new JSONObject();
		consulta.put("peticion", 1);
		consulta.put("origen", origen);

		String respuesta;
		JSONArray array = new JSONArray();
		try {
			mySocket.sendMessage(consulta.toJSONString());
			respuesta = mySocket.receiveMessage();
			array = (JSONArray) parser.parse(respuesta);
		} catch (Exception e) { e.printStackTrace();}

		return array;
	} // end consultaViajes



	/**
	 * Un pasajero reserva un viaje
	 * 
	 * @param codviaje		codigo del viaje
	 * @param codcliente	codigo del pasajero
	 * @return	objeto JSON con los datos del viaje. Vacio si no se ha podido reservar
	 */
	public JSONObject reservaViaje(String codviaje, String codcliente) {
		JSONObject consulta = new JSONObject();
		consulta.put("peticion", 2);
		consulta.put("codviaje", codviaje);
		consulta.put("codcli",codcliente);

		String respuesta;
		JSONObject viaje = new JSONObject();
		try {
			mySocket.sendMessage(consulta.toJSONString());
			respuesta = mySocket.receiveMessage();
			viaje = (JSONObject) parser.parse(respuesta);

		} catch (Exception e) { e.printStackTrace();}
		return viaje;

	} // end reservaViaje
	
	/**
	 * Un pasajero anula una reserva
	 * 
	 * @param codviaje		codigo del viaje
	 * @param codcliente	codigo del pasajero
	 * @return	objeto JSON con los datos del viaje. Vacio si no se ha podido reservar
	 */
	public JSONObject anulaReserva(String codviaje, String codcliente) {
		// POR IMPLEMENTAR
		return null; // cambiar por el retorno correcto
	} // end anulaReserva

	/**
	 * Un cliente oferta un nuevo viaje
	 * 
	 * @param codprop	codigo del cliente que hace la oferta
	 * @param origen	origen del viaje
	 * @param destino	destino del viaje
	 * @param fecha		fecha del viaje en formato dd/mm/aaaa
	 * @param precio	precio por plaza
	 * @param numplazas	numero de plazas ofertadas
	 * @return	viaje ofertado en formatoJSON. Vacio si no se ha podido ofertar
	 */
	public JSONObject ofertaViaje(String codprop, String origen, String destino, String fecha, long precio, long numplazas) {
		JSONObject consulta = new JSONObject();
		consulta.put("peticion", 4);
		consulta.put("codprop", codprop);
		consulta.put("origen", origen);
		consulta.put("destino", destino);
		consulta.put("fecha", fecha);
		consulta.put("precio", precio);
		consulta.put("numplazas", numplazas);

		String respuesta;
		JSONObject viaje = new JSONObject();
		try {
			mySocket.sendMessage(consulta.toJSONString());
			respuesta = mySocket.receiveMessage();
			viaje = (JSONObject) parser.parse(respuesta);

		} catch (Exception e) { e.printStackTrace();}
		return viaje;
	} // end ofertaViaje

	/**
	 * Un cliente borra una oferta de viaje
	 * 
	 * @param codviaje		codigo del viaje
	 * @param codcliente	codigo del pasajero
	 * @return	objeto JSON con los datos del viaje. Vacio si no se ha podido reservar
	 */
	public JSONObject borraViaje(String codviaje, String codcliente) {
		JSONObject consulta = new JSONObject();
		consulta.put("peticion", 5);
		consulta.put("codviaje", codviaje);
		consulta.put("codprop",codcliente);

		String respuesta;
		JSONObject viaje = new JSONObject();
		try {
			mySocket.sendMessage(consulta.toJSONString());
			respuesta = mySocket.receiveMessage();
			viaje = (JSONObject) parser.parse(respuesta);

		} catch (Exception e) { e.printStackTrace();}
		return viaje;
	} // end borraViaje

	/**
	 * Finaliza la conexion con el servidor
	 */
	public void cierraSesion( ) {

		JSONObject consulta = new JSONObject();
		consulta.put("peticion", 0);
		try {
			mySocket.sendMessage(consulta.toJSONString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	} // end done
} //end class