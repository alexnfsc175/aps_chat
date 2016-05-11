package socket;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatCliente extends JFrame {
	Socket socket;
	PrintWriter escritor;
	JTextField textoParaEnviar;
	String nome;
	JTextArea textoRecebido;
	Scanner leitor;
	
	private class EscutaServidor implements Runnable{

		@Override
		public void run() {
			try{
				String texto;
				while((texto = leitor.nextLine())!=null){
					textoRecebido.append(texto + "\n");
				}
			}catch(Exception e){}
		}
		
		
	}
	
	public ChatCliente(String nome){
		super("Chat : "+nome);
		this.nome = nome;
		
		Font fonte = new Font("serif", Font.PLAIN, 26);
		textoParaEnviar = new JTextField();
		textoParaEnviar.setFont(fonte);
		JButton botao = new JButton("Enviar");
		botao.setFont(fonte);
		botao.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				escritor.println(nome + " : " +textoParaEnviar.getText());
				escritor.flush();
				textoParaEnviar.setText("");
				textoParaEnviar.requestFocus();
			}
			
		});
		Container envio = new JPanel();
		envio.setLayout(new BorderLayout());
		envio.add(BorderLayout.CENTER, textoParaEnviar);
		envio.add(BorderLayout.EAST, botao);
		
		
		textoRecebido = new JTextArea();
		textoRecebido.setFont(fonte);
		JScrollPane scroll = new JScrollPane(textoRecebido);
		
		this.getContentPane().add(BorderLayout.CENTER, scroll);
		this.getContentPane().add(BorderLayout.SOUTH, envio);
		
		this.configurarRede();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500,500);
		this.setVisible(true);
	}
	
	private void configurarRede(){
		try{
			this.socket = new Socket("127.0.0.1", 5000);
			this.escritor = new PrintWriter(socket.getOutputStream());
			this.leitor = new Scanner(socket.getInputStream());
			new Thread(new EscutaServidor()).start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		new ChatCliente("Alex");
		new ChatCliente("Caio");
		new ChatCliente("Welington");
	}

}