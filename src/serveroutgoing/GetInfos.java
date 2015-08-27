package serveroutgoing;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;





public class GetInfos {
	static String[] data;
public GetInfos(String ip) {
			
	


	
	// TODO Auto-generated constructor stub
}
public static String INFOS(String ip){
	String returne = " ";
	try {
		
		
		Socket socket = new Socket();
		
	     String[] ipd = ip.split(":");
		socket.connect(new InetSocketAddress(ipd[0], Integer.parseInt(ipd[1])), 1 * 1000);
		
	
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		out.write(0xFE);
		
		StringBuilder str = new StringBuilder();
		
		int b;
		while ((b = in.read()) != -1) {
			if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
				str.append((char) b);
			}
		}
		
		data = str.toString().split("§");
		String motd = data[0];
		int onlinePlayers = Integer.valueOf(data[1]);
		int maxPlayers = Integer.valueOf(data[2]);
		
    returne = motd + "_" + onlinePlayers + "_" + maxPlayers;
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return returne;
}
}
