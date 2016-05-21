package socket;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	String ip;
	int porta;
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
	
	public ChatCliente(String nome, String ip, int porta){
		super("Chat : "+nome);
		this.nome = nome;
		this.ip = ip;
		this.porta = porta;
		
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
		
		textoParaEnviar.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					escritor.println(nome + " : " +textoParaEnviar.getText());
					escritor.flush();
					textoParaEnviar.setText("");
					textoParaEnviar.requestFocus();
				}
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}});;
		
		
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
			this.socket = new Socket(ip, porta);
			this.escritor = new PrintWriter(socket.getOutputStream());
			this.leitor = new Scanner(socket.getInputStream());
			new Thread(new EscutaServidor()).start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}