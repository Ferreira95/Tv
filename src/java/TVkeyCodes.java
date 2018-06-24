/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Rafael Cardoso
 */
public enum TVkeyCodes {
    // Mudar de canal: 40/38 – up and down 
    UP(40), DOWN(39),
    
    // 49/50/51/52/53/54/55/56/57 – teclas 1 a 9 do comando
    ONE(49), TWO(50), TRHEE(51), FOUR(52), FIVE(53),
    SIX(54), SEVEN(55), EIGHT(56), NINE(57),
    
    // Biblioteca: 48 – Abrir e fechar biblioteca
    //              13 – OK
    //              37/39 – esquerda e direita
    OPEN_CLOSE_LIB(48), OK(13), LEFT(37), RIGHT(39),
    
    // Enviar vídeo: 8 – Backspace
    SEND_VIDEO(8);

    public int keyCode;

    private TVkeyCodes(int keyCode) {
        this.keyCode = keyCode;
    }

}
