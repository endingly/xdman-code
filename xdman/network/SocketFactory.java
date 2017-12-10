package xdman.network;

import java.net.*;

import javax.net.ssl.*;

import xdman.Config;
import xdman.network.http.HttpContext;

import java.io.*;

public class SocketFactory {
	private static int timeOut = 0;
	private static int tcpBufSize = 1024*64;

	// private static SSLContext sslContext;

	public static SSLSocket wrapSSL(Socket socket, String host, int port) throws NetworkException {
		try {
			SSLSocket sock2 = (SSLSocket) (HttpContext.getInstance().getSSLContext().getSocketFactory())
					.createSocket(socket, host, port, true);
			sock2.startHandshake();
			return sock2;
		} catch (IOException e) {
			throw new NetworkException("Https connection failed: " + host + ":" + port);
		}
	}

	public static Socket createSocket(String host, int port) throws HostUnreachableException {
		try {
			Socket sock = new Socket();
			sock.setSoTimeout(Config.getInstance().getNetworkTimeout()*1000);
			sock.setTcpNoDelay(true);
			sock.setReceiveBufferSize(tcpBufSize);
			sock.setSoLinger(false, 0);
			sock.connect(new InetSocketAddress(host, port));
			return sock;
		} catch (IOException e) {
			throw new HostUnreachableException("Unable to connect to: " + host + ":" + port);
		}
	}
}
