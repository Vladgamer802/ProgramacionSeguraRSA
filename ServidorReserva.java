import java.net.*;
import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;

public class ServidorReserva {
    public static void main (String[] args) throws Exception {
        
        //Se genera servidor
        ServerSocket servidor = new ServerSocket(7000);
        System.out.println("Servidor esperando conexion en puerto 7000");

        Socket conexion = servidor.accept();
        
        //Se recibe clave publica
        DataInputStream dis = new DataInputStream(conexion.getInputStream());
        
        int longitudClave = dis.readInt();
        byte[] clavePublicaBytes = new byte[longitudClave];
        dis.readFully(clavePublicaBytes);
        
        KeyFactory kf = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(clavePublicaBytes);
        PublicKey clavePublicaCliente = kf.generatePublic(spec);

        //Cifrado de mensaje
        String mensaje = "Reserva confirmada para el dia 15";

        Cipher cifrador = Cipher.getInstance("RSA");
        cifrador.init(Cipher.ENCRYPT_MODE, clavePublicaCliente);

        byte[] mensajeCifrado = cifrador.doFinal(mensaje.getBytes());
        System.out.println("Mensaje cifrado");

        //Se envia el mensaje cifrado
        DataOutputStream dos = new DataOutputStream(conexion.getOutputStream());
        dos.writeInt(mensajeCifrado.length);
        dos.write(mensajeCifrado);

        conexion.close();
        servidor.close();
        
    }
}