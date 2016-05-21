package socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatServer {

	List<PrintWriter> conexoes = new ArrayList<>();
	
	public ChatServer(){
		ServerSocket server;
		try {
			server = new ServerSocket(5000);
			while(true){
				Socket socket = server.accept();
				new Thread(new EscutaCliente(socket)).start();
				PrintWriter canalDeComunicao = new PrintWriter(socket.getOutputStream());
				conexoes.add(canalDeComunicao);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void encaminhaParaTodos(String texto){
		for(PrintWriter canalDeComunicao : conexoes){
			try{
				canalDeComunicao.println(texto);
				canalDeComunicao.flush();
			}catch(Exception e){}
		}
	}
	private class EscutaCliente implements Runnable{
		Scanner leitor;
		public EscutaCliente(Socket socket){
			try {
				leitor = new Scanner(socket.getInputStream());
				System.out.println(socket.hashCode());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			
			try{
				String texto;
				while((texto = leitor.nextLine())!= null){
					System.out.println(texto);
					encaminhaParaTodos(texto);
				}
			}catch(Exception e){}
		}
		
	}
	
	public static void main(String[] args) {
		new ChatServer();

	}

}
