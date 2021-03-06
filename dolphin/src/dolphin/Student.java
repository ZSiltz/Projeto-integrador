/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dolphin;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Student {

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Robot robot;

    public Student(String useless,String name, String ip) throws IOException, AWTException {
        Socket socket = new Socket(InetAddress.getByName(ip), 5555);
        robot = new Robot();
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        out.writeObject(name);
        out.flush();
    }

    public void run() throws ClassNotFoundException {
        try {
            while (true) {
                RobotAction action = (RobotAction) in.readObject();
                Object result = action.execute(robot);
                if (result != null) {
                    out.writeObject(result);
                    out.flush();
                    out.reset();
                }
            }
        } catch (IOException ex) {
            System.out.println("Connection closed");
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            Student student = new Student("",args[0], args[1]);
            student.run();
        } catch (Exception ex) {
            System.err.println("Correct Usage:\n\tjava dolphin/Student <name> <ip>");
        }

    }
}
