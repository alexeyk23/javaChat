/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.simplechatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author trebushet
 */
public class SimpleChatServer {

    /**
     * @param args the command line arguments
     */
    ArrayList clientOutputStream;
    class ClientHandler implements Runnable
    {
       
        public ClientHandler(Socket s) {
            try {
                sock = s;
                InputStreamReader isr = new InputStreamReader(sock.getInputStream());
                reader  = new BufferedReader(isr);
                
            } catch (IOException e) {
            }
        }

        BufferedReader reader;
        Socket sock;
        
        @Override
        public void run() 
        {
            String message;
            try {
                while ((message = reader.readLine())!=null)                
                {  System.out.println("read: "+message);                
                sendAll(message);   }             
            } catch (IOException ex) {
                Logger.getLogger(SimpleChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void sendAll(String message)
    {
        Iterator it = clientOutputStream.iterator();
        while (it.hasNext()) 
        {
            PrintWriter writer =(PrintWriter) it.next();
            writer.println(message);
            writer.flush();
        }
    }
    static final int PORT=2323;
    public void startServer()
    {
        clientOutputStream = new ArrayList();
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                 Socket client = serverSocket.accept();
                 PrintWriter writer = new PrintWriter(client.getOutputStream());
                 clientOutputStream.add(writer);
                 
                 Thread t = new Thread(new ClientHandler(client));
                 t.start();
                 System.out.println("got a connection from client");
            }
        } catch (Exception e) {
        }
    }
    public static void main(String[] args) 
    {
        new SimpleChatServer().startServer();
    }
    
}
