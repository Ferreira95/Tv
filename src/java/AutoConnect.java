
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortException;

public class AutoConnect {
    /**SerialCommunication instância a  classe SerialCommunicationUSB. */
    public static SerialCommunicationUSB SerialCommunication;
            
    /**Quantidade de conexões bem sucedidas, nesse contexto a quantidade da ultima conexão. */
    public static int lastConnection = 0;
    
    /**Flag de controle para verificação das portas USB. */
    public static int flag =  0;
    
    /**Array que com os nomes das portas USB, nesse contexto armazena apenas as portas que estão em uso pelo sistema do usuário.*/
    public static String[] ports;
    
    /**Variavel para ajudar no controle do @For-1.*/
    static int controlFor=0;
    
    public static void TimerC(){
        Timer timer = new Timer();
        
        /**Atributo da @Tarefa-1 (AutoConnect.java).*/
        final long seconds = 3000;
        
        /**Atributo da @Tarefa-1 (AutoConnect.java).*/
        final long wait = 1500;   
            
        
        TimerTask task = new TimerTask(){                        
            @Override
            public void run(){
                
                /**Lendo portas disponiveis, ou seja as que estão conectadas no momento.*/
                ports = SerialCommunication.listPorts();
                
                /**For-1 (AutoConnect.java): Procurar Porta.*/
                for(controlFor=0 ; controlFor < ports.length && lastConnection == SerialCommunication.connectionCounter ; controlFor++){          
                        flag = 1;                
                        
                        /**Debug, informações no console para ver o funcionamento e possiveis erros.*/

                        System.out.println("@AutoConnect, run(): searching ports:");
                        System.out.println("-number of ports: "+ports.length);
                        System.out.println("-last connection: "+lastConnection);
                        System.out.println("-connection counter: "+SerialCommunication.connectionCounter);
                        System.out.println("-connection status: "+SerialCommunication.status);                                            
                        SerialCommunication.Connect(ports[controlFor]);                        
                        System.out.println("-connected to port: "+ports[controlFor]);
                        System.out.println("-------------End run()-----------------");
                                                                                                                            
                        try { //Delay 2s
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException ex) { Logger.getLogger(AutoConnect.class.getName()).log(Level.SEVERE, null, ex); }
                        
                        /**Conexão sucedida, o Óculos Mouse foi encontrado.*/
                        if(SerialCommunication.status==true){
                            lastConnection=SerialCommunication.connectionCounter;
                            System.out.println("@AutoConeect, run(): port found and connected");
                            
                            try { 
                                TimeUnit.SECONDS.sleep(2); //Delay 2s
                            } catch (InterruptedException ex) {
                                Logger.getLogger(AutoConnect.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        
                        //Ainda em Teste: erro, ou seja Óculos foi desconectado
                        if(SerialCommunication.status==false && SerialCommunication.connectionCounter >0){
                            System.out.println("@AutoConnect, run(): IOM disconnected");
                        }
                        
                        //Desconexão de um hardware errado
                        if(SerialCommunication.status==false && SerialCommunication.Opened()){
                            try {
                                SerialCommunication.Disconnect();
                                System.out.println("@AutoConnect, run(): disconnected");
                            } catch (SerialPortException ex) { Logger.getLogger(AutoConnect.class.getName()).log(Level.SEVERE, null, ex); }
                                
                        }                                                
                }
                                
                if(flag==0){
                    lastConnection=SerialCommunication.connectionCounter;
                }
                
                flag=0;
            }
        };
        /**Tarefa-1 (AutoConnect.java**/        
        timer.scheduleAtFixedRate(task, wait, seconds);     
    }        
       
}
