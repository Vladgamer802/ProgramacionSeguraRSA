import java.net.*;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;
import java.io.*;


public class ClienteReserva{

    public static void main(String[] args) throws Exception {
        
        //Se generan claves publica y privada
        System.out.println("Generando claves...");

        KeyPairGenerator generador = KeyPairGenerator.getInstance("RSA");
        generador.initialize(2048);
        KeyPair claves = generador.generateKeyPair();

        PublicKey clavePublica = claves.getPublic();
        PrivateKey clavePrivada = claves.getPrivate();
        System.out.println("Claves generadas");

        //Conexcion al servidor
        Socket conexion = new Socket("localhost", 7000);
        System.out.println("Conectado al servidor");

        //Se envia clave publica
        DataOutputStream dos = new DataOutputStream(conexion.getOutputStream());
        byte[] claveBytes = clavePublica.getEncoded();

        dos.writeInt(claveBytes.length);
        dos.write(claveBytes);

        System.out.println("Clave publica enviada");

        //Se recibe mensaje cifrado
        DataInputStream dis = new DataInputStream(conexion.getInputStream());

        int longitudMensaje = dis.readInt();
        byte[] mensajeCifrado = new byte[longitudMensaje];
        dis.readFully(mensajeCifrado);

        System.out.println("Mensaje cifrado recibido");

        //Se descifra mensaje
        Cipher descifrador = Cipher.getInstance("RSA");
        descifrador.init(Cipher.DECRYPT_MODE, clavePrivada);

        byte[] mensajeDescifrado = descifrador.doFinal(mensajeCifrado);
        String mensaje = new String(mensajeDescifrado);

        System.out.println("Mensaje descifrado " + mensaje);

    }
}