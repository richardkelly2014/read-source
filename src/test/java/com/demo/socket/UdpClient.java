package com.demo.socket;

import java.io.IOException;
import java.net.*;

/**
 * Created by jiangfei on 2019/12/8.
 */
public class UdpClient {


    public static void main(String[] args) throws IOException {
        String str = "11";
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(str.getBytes(), str.getBytes().length, InetAddress.getByName("192.168.2.211"), 10002);
        socket.send(packet);
        socket.close();
    }
}
