package com.jesusmelian;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here
        final String ROUTE = "/var/log/";
        boolean terminate = false;

        //ventana para escribir comandos
        Session session = null;
        //creamos un canal de ejecución
        ChannelExec channel = null;

        //DATOS DE CONEXIÓN
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduzca el nombre de usuario");
        final String USERNAME = sc.nextLine();
        System.out.println("Introduzca el host");
        final String HOST = sc.nextLine();
        System.out.println("Introduzca el puerto");
        final int PORT = sc.nextInt();

        sc.nextLine();
        System.out.println("Introduzca la contraseña");
        final String PASSWORD = sc.nextLine();


        try {
            if(USERNAME != null && HOST != null ) {
                //CREO LA SESIÓN CON LOS PARÁMETROS
                session = new JSch().getSession(USERNAME, HOST, PORT);
                session.setPassword(PASSWORD);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                channel = (ChannelExec) session.openChannel("exec");

                while(!terminate) {
                    System.out.println("Inserte el nombre del archivo de registro");
                    String archive = sc.nextLine();

                    //COMPRUEBO QUE EL ARCHIVO TERMINE EN .log
                    while (!comprobeArchive(archive)) {
                        System.out.println("El archivo debe terminar en .log");
                        System.out.println("Inserte el nombre del archivo de registro");
                        archive = sc.nextLine();
                    }

                    channel.setCommand("cd " + ROUTE + ";" + " cat " + archive);
                    ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                    channel.setOutputStream(responseStream);

                    channel.connect();


                    while (channel.isConnected()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    String responseString = new String(responseStream.toByteArray());

                    System.out.println(responseString);

                    System.out.println("¿DESEA CONTINUAR? S/N");
                    String option = sc.nextLine();

                    //Si introduce "N" o algo que no sea "S" el programa continuará
                    if(!option.equalsIgnoreCase("S")){
                        terminate = true;
                    }
                }
            }
        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            if( session != null){
                session.disconnect();
            }
            if(channel != null){
                channel.disconnect();
            }
        }
    }

    public static boolean comprobeArchive(String archive) {
        String extension = archive.substring(archive.length()-4);
        //System.out.println(extension);
        if(extension.equals(".log")){
            //System.out.println("DEVUELVO TRUE");
            return true;
        }
        return false;
    }

}
