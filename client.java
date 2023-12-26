import java.io.*;
import java.net.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class client extends JFrame  {

    Socket socket;
    
    BufferedReader br;
    PrintWriter out;

    //declare components 
    private JLabel heading = new JLabel("client area");
    private JTextArea messageArea =new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);

    
    public client(){
        try{
            System.out.println("sending request to server");
             socket = new Socket("127.0.0.1",7770);
             System.out.println("connection done");
             br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream());
                createGUI();
                handleEvents();
                startReading();
               // startWriting();
               
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent e) {
              // System.out.println("key released"+ e.getKeyCode());
                if(e.getKeyCode()==10){
                    //System.out.println("you have pressed enter key");
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me : " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                } 
            }

            @Override
            public void keyTyped(KeyEvent e) {
                
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }
            
        });
    }
    private void createGUI(){
        
        this.setTitle("CLIENT MESSAGES");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       this.getContentPane().setBackground(Color.PINK);
        //coding for component 
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
       
        
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        messageArea.setEditable(false);

        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        this.setLayout(new BorderLayout());
        
        //adding the components to frame 
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);

        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);
    }
    public void startReading(){
        Runnable r1=() ->{
          System.out.println("reader started...");
          try{

          
          while(true){
              

              
              String msg = br.readLine();
              if(msg.equals("exit")){
                  System.out.println("server terminated the chat");
                  JOptionPane.showMessageDialog(this,"server terminated the chat" );
                  messageInput.setEnabled(false);
                  socket.close();
                  break;
              }
            // System.out.println(" server : " +  msg);
            messageArea.append("Server : " + msg+ "\n");
          
         
          }
        } catch(Exception e ){
                  //e.printStackTrace();
                  System.out.println("Connection Closed");
          }
        };
        new Thread(r1).start();
  }
  public void startWriting()
        {
                Runnable r2 = () -> {
                    System.out.println("writer started...");
                    try{
                    while(true) {
                        
                            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                            String content = br1.readLine();
                            out.println(content);
                            out.flush();
                        
                            if(content.equals("exit")){
                                socket.close();
                                break;
                            }
                    }
                }catch(Exception e){
                            //e.printStackTrace();
                            System.out.println("Connection Closed");
                        }
                };
                new Thread(r2).start();
            }
    public static void main(String[] args) {
        System.out.println("this is client");
        new client();
       }}
