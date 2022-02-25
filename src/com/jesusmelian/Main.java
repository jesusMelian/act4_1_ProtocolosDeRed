package com.jesusmelian;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here
        final String ROUTE = "/var/log/";

        //ventana para escribir comandos
        Session session = null;
        //creamos un canal de ejecución
        ChannelExec channel = null;

        Scanner sc = new Scanner(System.in);
        System.out.println("Introduzca el nombre de usuario");
        final String USERNAME = sc.nextLine();
        System.out.println("Introduzca el host");
        final String HOST = sc.nextLine();
        System.out.println("Introduzca el puerto");
        final int PORT = sc.nextInt();



        System.out.println("Introduzca la contraseña");
        final String PASSWORD = sc.nextLine();
        try {
            if(USERNAME != null && HOST != null && PASSWORD != null) {
                session = new JSch().getSession(USERNAME, HOST, PORT);
                session.setPassword(PASSWORD);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                channel = (ChannelExec) session.openChannel("exec");

                System.out.println("Inserte el nombre del archivo de registro");
                String archive = sc.nextLine();
                if(comprobeArchive(archive)){
                    channel.setCommand(ROUTE+archive);
                }

            }
        } catch (Exception e) {

        }
    }

    
}
