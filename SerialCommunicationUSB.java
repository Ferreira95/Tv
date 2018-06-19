
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortList;
import jssc.SerialPortException;
import javax.swing.JButton;

public class SerialCommunicationUSB {
    /**SerialPort instância a classe SerialPort da biblioteca jSSC, 
     * esse objeto contem os principais metodos para a comunicação serial USB, 
     * veja mais informações <a href="http://javadox.com/org.scream3r/jssc/2.8.0/javadoc/jssc/SerialPort.html">aqui</a>.
     */
    public static SerialPort serialPort;
    
    /**Atributo que conta a quantidade de erros consecutivos da comunicação, esse erro pode ocorrer pela perda/dano de um dos dados da comunicação.*/
    public static int readErrorCounter = 0;
    
    /**Atributo que conta a quantidade de recebimentos de dados bem sucedido, usado para confirmar a estabilidade da conexão com o hardware.*/
    public static int connectionCounter = 0;
    
    /**Atributo usado para verificar se a conexão feita com o hardware é a primeira, caso seja é carregado os dados de configuração do hardware para o sistema. Por exemplo: a velocidade do mouse.*/
    public static boolean firstConnection = false;

    /** Atributo para guardar o evento */
    public static int event;

    /** Atributo de status */

    public static boolean status = false;
    
    /**
     * Método para listar as portas seriais disponivels do Sistema Operacional do usuário.
     * @return array - Nomes de Portas Sereais
     */
    public static String[] listPorts() {        
        String[] portNames = SerialPortList.getPortNames();                
        for(int i = 0; i < portNames.length; i++){
            //System.out.println(portNames[i]);
        }        
        return portNames;
    }    
    
    /**
     * Método para conexão serial
     * @param porta String - Nome da porta que deseja-se conectar
     */
    public static void Connect(String porta){
        serialPort = new SerialPort(porta);        
        try {            
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);                                                                                                         
            serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
	    status = true;

            System.out.println("@SerialCommmunicationUSB: successfully connected");
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }
    
    /**
     * Método para verificar se a conexão está aberta.
     * @return boolena - status da abertura da porta
     */
    public static boolean Opened(){
        return  serialPort.isOpened();
    }
    
    /**
     * Método para fazer a desconexão com uma porta, a desconexão é feita com a porta que o sistema está ligada no momento.
     * @throws SerialPortException 
     */
    public static void Disconnect() throws SerialPortException{
        serialPort.closePort();
        System.out.println("@SerialCommmunicationUSB: disconnected successfully");
    }
    
    /**
     * Método para enviar/escrever dados na serial.
     * @param dados String - Dados que o hardware vai ler, os dados são  principalmente de configurações ou informação do status do sistema
     * @throws SerialPortException 
     */
    public static void SendData(String dados) throws SerialPortException{
        serialPort.writeString(dados);                                            
    }

    public static void ReceiveData(String dados) { // Método para tratar os sinais do evento
	System.out.println(dados);	
	event = Integer.parseInt(dados);
	Iom4tvLayout.manager(event);
    }
    
   
    /**
     * Método/Evento que faz o recebimento dos dados enviados pela serial.
     */
    private static class PortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {              
            if(serialPortEvent.isRXCHAR() && serialPortEvent.getEventValue() > 0) {            
                try {
                    String dataReceived;
                    dataReceived = serialPort.readString(serialPortEvent.getEventValue());                                                    
                    ReceiveData(dataReceived);                    
                } catch (SerialPortException ex) {
                    Logger.getLogger(SerialCommunicationUSB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                System.out.println("@SerialCommmunicationUSB, PortReader(): else");
            }
        }
    }                 
}
