/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buracoaberto;

import static buracoaberto.ClasseCarta.TEMPO;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Leonardo Lara Rodrigues
 */
public class BuracoAberto extends Application {
    
    public static final boolean DEBUGMODE = false;
    public static final boolean somCartaLigado = false;
    public static final int ESPADAS = 1;
    public static final int COPAS = 2;
    public static final int PAUS = 3;
    public static final int OUROS = 4;

    public static final int AS = 1;
    public static final int DOIS = 2;
    public static final int TRES = 3;
    public static final int QUATRO = 4;
    public static final int CINCO = 5;
    public static final int SEIS = 6;
    public static final int SETE = 7;
    public static final int OITO = 8;
    public static final int NOVE = 9;
    public static final int DEZ = 10;
    public static final int VALETE = 11;
    public static final int DAMA = 12;
    public static final int REI = 13;

    public static final int VERMELHO = 1;
    public static final int AZUL = 2;

    public static final int MAXCARTAS = 104;
    public static final int UMBARALHO = 52;
    public static final int NUMCARTAS = 13;
    public static final int NUMNAIPES = 4;
    public static final int NUMCORES = 2;
    public static final int CARTASMORTO = 11;
    public static final int CARTASJOG = 11;
    public static final int JOGADORES = 4;
    public static final int MAOMAX = MAXCARTAS - (2 * CARTASMORTO) - ((JOGADORES - 1) * CARTASJOG);
    public static final int MAXLIXO = MAXCARTAS - (JOGADORES * CARTASJOG);
    public static final int MAXMONTE = MAXCARTAS - (2 * CARTASMORTO) - (JOGADORES * CARTASJOG);

    public static final int JOG1 = 0;
    public static final int JOG2 = 1;
    public static final int JOG3 = 2;
    public static final int JOG4 = 3;
    public static final int MONTE = 4;
    public static final int LIXO = 5;
    public static final int MORTO1 = 6;
    public static final int MORTO2 = 7;

    public static final int MESA1 = 8;
    public static final int MESA2 = 9;
    
    public static final int BAIXO = 5;
    public static final int MEDIO = 10;
    public static final int ALTO = 15;

    public static final int CANASTRA_LIMPA = 200;
    public static final int CANASTRA_SUJA = 100;
    public static final int CANASTRA_REAL = 1000;
    public static final int CANASTRA_SEMIREAL = 500;
    public static final int BATIDA = 100;
    public static final int PEGOUMORTO = 100;

    public static final int X_Carta = 70;
    public static final int Y_Carta = 100;

    public ClasseCarta[] baralho = new ClasseCarta[104];
    public ClasseMao[] mao;
    public ClasseMesa[] mesa;

    public boolean ja_comprou = false;
    public double vez = -1;
    public double oldvez = -1;
    public boolean jogo_acabou = false;

    public String[] naipes = {"espadas", "copas", "paus", "ouros"};
    public String[] cores = {"Vermelho", "Azul"};
    public String[] cartas = {"e ás", "e dois", "e três", "e quatro", "e cinco", "e seis", "e sete", "e oito", "e nove", "e dez", "e valete", "a dama", "e rei"};
    public String[] nomes = {"1e", "2e", "3e", "4e", "5e", "6e", "7e", "8e", "9e", "10e", "11e", "12e", "13e", "1c", "2c", "3c", "4c", "5c", "6c", "7c", "8c", "9c", "10c", "11c", "12c", "13c", "1p", "2p", "3p", "4p", "5p", "6p", "7p", "8p", "9p", "10p", "11p", "12p", "13p", "1o", "2o", "3o", "4o", "5o", "6o", "7o", "8o", "9o", "10o", "11o", "12o", "13o"};
    public static final Image vermelho = new Image(BuracoAberto.class.getResourceAsStream("/buracoaberto/vrm.png"),70.0,100.0,false,true);
    public static final Image azul = new Image(BuracoAberto.class.getResourceAsStream("/buracoaberto/azl.png"),70.0,100.0,false,true);
    public static Group root;
    public Scene scene;
    public final Region lixo = new Region(), mesadejogos = new Region();
    public int cartaLixo = 0;
    public int naipeLixo = 0;
    ClasseCarta botPegouDoLixo;        
    private static final AudioClip SUA_VEZ = new AudioClip(BuracoAberto.class.getResource("/buracoaberto/sua_vez.wav").toString());
    private static final AudioClip CARTA = new AudioClip(BuracoAberto.class.getResource("/buracoaberto/carta.wav").toString());
    private static final AudioClip APLAUSO = new AudioClip(BuracoAberto.class.getResource("/buracoaberto/aplauso.wav").toString());
    private static final AudioClip CHORO = new AudioClip(BuracoAberto.class.getResource("/buracoaberto/choro.wav").toString());
    private static final AudioClip LIMPA = new AudioClip(BuracoAberto.class.getResource("/buracoaberto/limpa.wav").toString());
    private static final AudioClip SUJA = new AudioClip(BuracoAberto.class.getResource("/buracoaberto/suja.wav").toString());
    public Timeline masterListener;
    public final double screenCenterX = 648.0;
    public final double screenCenterY = 291.0;
    
    @Override
    public void start(Stage stage) throws Exception {
        //System.out.println("start()");
        root = new Group();
        mao = new ClasseMao[8];
        mesa = new ClasseMesa[2];
        //se o Bot pega a única carta do lixo, ela é memorizada pois não poderá ser descartada.
        botPegouDoLixo = new ClasseCarta();
        botPegouDoLixo.carta = 0;
        botPegouDoLixo.naipe = 0;
        inicializaCartas();
        jogo_acabou = false;
        lixo.setLayoutX(683);
        lixo.setLayoutY(276);
        lixo.setPrefSize(400,100);
        lixo.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override public void handle(MouseEvent event) {
                event.consume();
                lixo_click();
            }
        });
        root.getChildren().add(lixo); //lixo zOrder 0
        mesadejogos.setLayoutX(283);
        mesadejogos.setLayoutY(386);
        mesadejogos.setPrefSize(800,210);
        mesadejogos.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override public void handle(MouseEvent event) {
                event.consume();
                mesadejogos_click();
            }
        });
        root.getChildren().add(mesadejogos); //mesa zOrder 1
        for (int k=0;k<mao[MONTE].cartas.size();k++){
            mao[MONTE].cartas.get(k).zOrder=k+2; //vai de 2 a 105
            root.getChildren().add(mao[MONTE].cartas.get(k));
        }
        scene = new Scene(root,1366,706,Color.FORESTGREEN);
        stage.setScene(scene);
        stage.show();
        jogo();
    }

    EventHandler<ActionEvent> playCarta = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
            CARTA.play();
        }
    };

    EventHandler<ActionEvent> stopCarta = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
            CARTA.stop();
        }
    };
    
    EventHandler<ActionEvent> playSuaVez = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
            SUA_VEZ.play();
        }
    };
    EventHandler<ActionEvent> stopSuaVez = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
            SUA_VEZ.stop();
        }
    };

    EventHandler<ActionEvent> playChoro = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
            CHORO.play();
        }
    };

    EventHandler<ActionEvent> playAplauso = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
            APLAUSO.play();
        }
    };

    EventHandler<ActionEvent> playLimpa = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
            LIMPA.play();
        }
    };

    EventHandler<ActionEvent> playSuja = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
            SUJA.play();
        }
    };

    EventHandler<ActionEvent> stopLimpa = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
            LIMPA.stop();
        }
    };

    EventHandler<ActionEvent> stopSuja = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
            SUJA.stop();
        }
    };
    
    private void inicializaCartas(){
        //System.out.println("inicializaCartas()");
        int i;
        for (int cor=VERMELHO;cor<=AZUL;cor++){
            for (int naipe=ESPADAS;naipe<=OUROS;naipe++){
                for (int ct=AS;ct<=REI;ct++){
                    i = (ct-1) + NUMCARTAS * (naipe-1) + UMBARALHO * (cor-1);
                    baralho[i] = new ClasseCarta();
                    if (ct==AS) baralho[i].valor = ALTO;
                    else if ((ct>=TRES) && (ct<=SETE)) baralho[i].valor = BAIXO;
                    else baralho[i].valor = MEDIO;
                    baralho[i].carta = ct;
                    baralho[i].naipe = naipe;
                    baralho[i].cor = cor;
                    //baralho[i].local = MONTE;
                    baralho[i].selecionada = false;
                    baralho[i].zOrder = i+2;
                    baralho[i].frente = new Image(getClass().getResourceAsStream("/buracoaberto/"+nomes[i%52]+".png"),70.0,100.0,false,true);
                    if (cor==1) baralho[i].verso = vermelho;
                    else baralho[i].verso = azul;
                    baralho[i].setImage(baralho[i].verso);
                    baralho[i].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
                        @Override public void handle(MouseEvent event) {
                            carta_click(event);
                            event.consume();
                        }
                    });
                }
            }
        }
        for (i=0;i<8;i++) mao[i] = new ClasseMao();
        for (i=0;i<2;i++) mesa[i] = new ClasseMesa();
        for (i=0;i<MAXCARTAS;i++){
            mao[MONTE].cartas.add(baralho[i]);
        }
        mao[JOG1].setPosInit(383.0, 606.0, 15.0, 0.0);
        mao[JOG2].setPosInit(1281.0, 150.0, 0.0, 15.0);
        mao[JOG3].setPosInit(383.0, -54.0, 15.0, 0.0);
        mao[JOG4].setPosInit(15.0, 150.0, 0.0, 15.0);
        mao[MONTE].setPosInit(283.0, 276.0, 7.0, 0.0);
        mao[LIXO].setPosInit(683.0, 276.0, 15.0, 0.0);
        mao[MORTO1].setPosInit(1083.0, -54.0, 1.0, 0.0);
        mao[MORTO2].setPosInit(1183.0, -54.0, 1.0, 0.0);
        mesa[0].setPosInit(283.0, 386.0, 10.0, 0.0);
        mesa[1].setPosInit(283.0, 56.0, 10.0, 0.0);
        mesa[0].posMax = 1083.0;
        mesa[1].posMax = 1083.0;
    }
    
    private void play(int k){
        PauseTransition pausa = new PauseTransition(Duration.millis(10));
        pausa.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                mao[MONTE].cartas.get(k).posX = mao[MONTE].posInitX+mao[MONTE].deltaX*k;
                mao[MONTE].cartas.get(k).posY = mao[MONTE].posInitY+mao[MONTE].deltaY*k;
                arrumaCarta(mao[MONTE].cartas.get(k),true,Duration.millis(200),true).play();
                if (DEBUGMODE) mao[MONTE].cartas.get(k).fxViraPraCima().play();
                if (k<103){
                    play(k+1);
                }
                else {
                    SequentialTransition anim = new SequentialTransition(new PauseTransition(Duration.millis(1000)));
                    anim = new SequentialTransition(anim,distribui(Duration.millis(20)));
                    anim.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent event) {
                            SequentialTransition anim2 = new SequentialTransition(new PauseTransition(Duration.millis(1)));
                            //mao[JOG1].ordenaNaipe();
                            //mao[JOG1].viraPraCima().play();
                            //anim2 = new SequentialTransition(anim2,mao[JOG1].arruma(false));
                            //anim2 = new SequentialTransition(anim2,mao[JOG2].arruma(false));
                            //anim2 = new SequentialTransition(anim2,mao[JOG3].arruma(false));
                            //anim2 = new SequentialTransition(anim2,mao[JOG4].arruma(false));
                            anim2 = new SequentialTransition(anim2,transfereCarta(MONTE,mao[MONTE].cartas.size()-1,LIXO));
                            //anim2 = new SequentialTransition(anim2,mao[LIXO].viraPraCima());
                            anim2.setOnFinished(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent event) {
                                    vez = 0;
                                }
                            });
                            anim2.play();
                            lixo.setStyle("-fx-border-color: #AAAAAA;");
                            mesadejogos.setStyle("-fx-border-color: #AAAAAA;");
                        }
                    });
                    anim.play();
                }
            }
        });
        pausa.play();
    }
    
    private void jogo () {
        //System.out.println("jogo()");
        
        embaralha();
        corta();
        embaralha();
        
        for (int k=mao[MONTE].cartas.size()-1;k>=0;k--){
            mao[MONTE].cartas.get(k).toFront();
        }
        updateZOrder();
        
        play(0); //põe o monte na mesa, dsitribui as cartas e joga a primeira no lixo.
        
        masterListener = new Timeline(); //cria um "listener" a cada 1.0s para sincronizar o jogo com as animações
        masterListener.setCycleCount(Timeline.INDEFINITE);
        masterListener.getKeyFrames().add(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
                SequentialTransition anim;
                /*
                aqui vai verificar o status da variável "vez" e vai agir de acordo.
                A variável "vez" só poderá ser modificada no evento .setonFinished de uma animação
                A variável tem a seguinte convenção:
                vez = 0 -> está na vez do jogador humano (responder a eventos de mouse)
                vez = 1.1, 2.1, 3.1 -> está na vez do Bot 1, 2 ou 3 (comprando a carta do lixo ou do monte)
                vez = 1.2, 2.2, 3.2 -> está na vez do Bot 1, 2 ou 3 (descendo jogos, inclui pegar morto em pé e bater em pé)
                vez = 1.3, 2.3, 3.3 -> está na vez do Bot 1, 2 ou 3 (descartando, inclui pegar morto após descarte e bater)
                */
                if ((vez==-1)&&(oldvez==-1)&&jogo_acabou){
                    terminaJogo();
                }
                else if (((oldvez==-1)||(oldvez==3.3)) && (vez==0)){
                    //vez do jogador 1 (responde a eventos de mouse)
                    oldvez = 0;
                    //SUA_VEZ.play();
                } 
                else if ((oldvez==0) && (vez==1.1)){
                    //passou a vez para o jogador 2 comprar
                    oldvez = 1.1;
                    anim = botCompra(JOG2);
                    anim.setOnFinished(new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event){
                            vez = 1.2;
                        }
                    });
                    anim.play();
                }
                else if ((oldvez==1.1) && (vez==1.2)){
                    //passou a vez para o jogador 2 descer
                    oldvez = 1.2;
                    anim = botDesce(JOG2);
                    anim.setOnFinished(new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event){                            
                            if (jogo_acabou) {
                                terminaJogo();
                            } else {
                                vez = 1.3;
                            }
                        }
                    });
                    anim.play();
                }
                else if ((oldvez==1.2) && (vez==1.3)) {
                    //passou a vez para o jogador 2 descartar
                    oldvez = 1.3;
                    anim = botDescarta(JOG2);
                    anim.setOnFinished(new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event){                            
                            if (jogo_acabou) {
                                terminaJogo();
                            } else {
                                vez = 2.1;
                            }
                        }
                    });
                    anim.play();
                }
                else if ((oldvez==1.3) && (vez==2.1)) {
                    //passou a vez para o jogador 3 comprar
                    oldvez = 2.1;
                    anim = botCompra(JOG3);
                    anim.setOnFinished(new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event){
                            vez = 2.2;
                        }
                    });
                    anim.play();
                }
                else if ((oldvez==2.1) && (vez==2.2)){
                    //passou a vez para o jogador 3 descer
                    oldvez = 2.2;
                    anim = botDesce(JOG3);
                    anim.setOnFinished(new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event){                            
                            if (jogo_acabou){
                                terminaJogo();
                            } else {
                                vez = 2.3;
                            }
                        }
                    });
                    anim.play();
                }
                else if ((oldvez==2.2) && (vez==2.3)){
                    //passou a vez para o jogador 3 descartar
                    oldvez = 2.3;
                    anim = botDescarta(JOG3);
                    anim.setOnFinished(new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event){                            
                            if (jogo_acabou){
                                terminaJogo();
                            } else {
                                vez = 3.1;
                            }
                        }
                    });
                    anim.play();
                }
                else if ((oldvez==2.3) && (vez==3.1)){
                    //passou a vez para o jogador 4 comprar
                    oldvez = 3.1;
                    anim = botCompra(JOG4);
                    anim.setOnFinished(new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event){
                            vez = 3.2;
                        }
                    });
                    anim.play();
                }
                else if ((oldvez==3.1) && (vez==3.2)){
                    //passou a vez para o jogador 4 descer
                    oldvez = 3.2;
                    anim = botDesce(JOG4);
                    anim.setOnFinished(new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event){                            
                            if (jogo_acabou){
                                terminaJogo();
                            } else {
                                vez = 3.3;
                            }
                        }
                    });
                    anim.play();
                }
                else if ((oldvez==3.2) && (vez==3.3)){
                    //passou a vez para o jogador 2 descartar
                    oldvez = 3.3;
                    anim = botDescarta(JOG4);
                    anim.setOnFinished(new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event){                            
                            if (jogo_acabou){
                                terminaJogo();
                            } else {
                                vez = 0;
                                SUA_VEZ.play();
                            }
                        }
                    });
                    anim.play();
                }
            }
        }));
        masterListener.play();
    }
    
    private void mesadejogos_click(){
        //System.out.println("mesadejogos_click()");
        SequentialTransition t;
        if (vez==0){
            t = desceSelecionadas(JOG1,-1);
            if (t!=null) t.play();
        }
        if (jogo_acabou){
            terminaJogo();
        }
    }
    
    private void lixo_click(){
        //System.out.println("lixo_click");
        if (vez!=0) return;
        if (!ja_comprou){
            ja_comprou=true;
            if (mao[LIXO].cartas.size()==1){ // se só tem uma carta sozinha, memoriza-a, não pode ser o descarte
                cartaLixo = mao[LIXO].cartas.get(0).carta;
                naipeLixo = mao[LIXO].cartas.get(0).naipe;
            }
            ParallelTransition pt = new ParallelTransition();
            while (!mao[LIXO].cartas.isEmpty()){
                pt.getChildren().add(transfereCarta(LIXO,mao[LIXO].cartas.size()-1,JOG1));
            }
            pt.play();
        } else {
            descarta();
        }
    }
    
    private void carta_click(MouseEvent event){
        //System.out.println("carta_click");
        if (vez!=0) return; //não está na sua vez
        SequentialTransition t = new SequentialTransition(new PauseTransition(Duration.millis(1)));
        ClasseCarta ct = (ClasseCarta)event.getSource();
        int k, iCt=-1, iMao=-1;
        //boolean t;
        k=mao[MONTE].cartas.size();
        if (k==MAXCARTAS) return; //se ainda não distribuiu as cartas, não faz nada
        
        //IDENTIFICAÇÃO DA CARTA CLICADA
        if (k>=1){ //se ainda tem pelo menos uma carta no monte, verifica se clicou no monte
            if (ct.equals(mao[MONTE].cartas.get(k-1))){ //se clicou no topo do monte
                iCt=k-1; //então iCt é o índice da carta...
                iMao=MONTE; //...da mão cujo índice é iMao.
            }
        }
        if (iCt==-1){ //se ainda não achou a carta clicada...
            for (k=0;k<mao[LIXO].cartas.size();k++){ //...procura no LIXO
                if (ct.equals(mao[LIXO].cartas.get(k))){
                    //System.out.println("Carta do Lixo clicada");
                    iCt=k;
                    iMao=LIXO;
                }
            }
        }
        if (iCt==-1){
            for (k=0;k<mao[JOG1].cartas.size();k++){ //procura nas cartas do jogador
                if (ct.equals(mao[JOG1].cartas.get(k))){
                    iCt=k;
                    iMao=JOG1;
                }
            }
        }
        if (iCt==-1){
            for (int m=0;m<mesa[0].maos.size();m++){ //procura na MESA1 (mesa[0])
                for (k=0;k<mesa[0].maos.get(m).cartas.size();k++){
                    if (ct.equals(mesa[0].maos.get(m).cartas.get(k))){
                        iCt=k;
                        iMao=100+m; //adicionando 100, quer dizer que clicou na mesa (iMao-100 = índice da mão)
                    }
                }
            }
        }
        
        //COM A DEFINIÇÃO DE QUAL CARTA FOI CLICADA, EFETUA AS AÇÕES
        if ((iMao==MONTE)&&!ja_comprou){ //compra do monte
            ja_comprou=true;
            transfereCarta(MONTE,mao[MONTE].cartas.size()-1,JOG1).play();
        }
        else if((iMao==LIXO)&&!ja_comprou){ //pega tudo do lixo
            ja_comprou=true;
            if (mao[LIXO].cartas.size()==1){ // se só tem uma carta sozinha, memoriza-a, não pode ser o descarte
                cartaLixo = mao[LIXO].cartas.get(0).carta;
                naipeLixo = mao[LIXO].cartas.get(0).naipe;
            }
            ParallelTransition pt = new ParallelTransition();
            while (!mao[LIXO].cartas.isEmpty()){
                pt.getChildren().add(transfereCarta(LIXO,mao[LIXO].cartas.size()-1,JOG1));
            }
            t=new SequentialTransition(t,pt);
            t.play();
        }
        else if((iMao==LIXO)&&ja_comprou){ //descarta
            descarta();
        }
        else if ((iMao==JOG1)&&ja_comprou){ //(de)seleciona
            mao[JOG1].cartas.get(iCt).fxToggleSelect().play();
        }
        else if (iMao>=100){
            t=desceSelecionadas(JOG1,iMao-100);
            if (t!=null){
                t.setOnFinished(new EventHandler<ActionEvent>(){
                    @Override public void handle (ActionEvent e){
                        if (jogo_acabou) terminaJogo();
                    }
                });
                t.play();
            } else {
                if (jogo_acabou) terminaJogo();
            }
        }
    }
    
    public SequentialTransition arrumaCarta (ClasseCarta ct, boolean movimento, Duration t, boolean toFront) {
        if (t==Duration.ZERO) t=Duration.seconds(TEMPO);
        if (!movimento) t=Duration.millis(1);
        TranslateTransition translate = new TranslateTransition(t);
        translate.setToX(ct.posX);
        translate.setToY(ct.posY);
        translate.setInterpolator(Interpolator.EASE_BOTH);
        RotateTransition rotate = new RotateTransition (t);
        rotate.setToAngle(ct.angulo);
        Timeline tl = new Timeline();
        tl.getKeyFrames().add(new KeyFrame(t.divide(2),new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //no meio do movimento, coloca a carta no z-order correto
                if (!toFront){
                    root.getChildren().remove(ct);
                    root.getChildren().add(ct.zOrder, ct);
                    //updateZOrder();
                }
                else {
                    root.getChildren().remove(ct);
                    root.getChildren().add(MAXCARTAS+1, ct); //zOrder das cartas vai de 2 a 105
                }
            }
        }));
        ParallelTransition transition = new ParallelTransition(ct,translate,rotate,tl);
        SequentialTransition seqtransition = new SequentialTransition(transition);
        return seqtransition;
    }  
    public SequentialTransition arrumaCarta (ClasseCarta ct, boolean movimento){
        return arrumaCarta(ct,movimento,Duration.ZERO,false);
    }
    public SequentialTransition arrumaCarta (ClasseCarta ct){
        return arrumaCarta(ct,true,Duration.ZERO,false);
    }
    public SequentialTransition arrumaCarta (ClasseCarta ct, boolean movimento, Duration t){
        return arrumaCarta(ct,movimento,t,false);
    }

    public void updateZOrder() {
        root.getChildren().remove(mesadejogos);
        root.getChildren().add(0,mesadejogos); //zOrder 1 ( add(0,mesadejogos) está correto, logo abaixo o zOrder vai se tornar 1. )
        root.getChildren().remove(lixo);
        root.getChildren().add(0,lixo); //zOrder 0
        for (int m=0;m<8;m++){
            this.mao[m].tarja.toFront();
        }
        for (int ms=0;ms<2;ms++){
            if (!this.mesa[ms].maos.isEmpty()){
                for (int m=0;m<this.mesa[ms].maos.size();m++){
                    mesa[ms].maos.get(m).tarja.toFront();
                }
            }
        }
        for (int k=0;k<104;k++){
            baralho[k].zOrder = root.getChildren().indexOf(baralho[k]);
            //System.out.println(root.getChildren().indexOf(baralho[k]));
        }
    }
    
    private SequentialTransition transfereCarta(int origem, int index, int destino) {
        return transfereCarta(origem,index,destino,-1,Duration.ZERO,null);
    }
    private SequentialTransition transfereCarta(int origem, int index, int destino, Duration tempo) {
        return transfereCarta(origem,index,destino,-1,tempo,null);
    }
    private SequentialTransition transfereCarta(int origem, int index, int destino, int mao_mesa, ClasseMao ordem) {
        return transfereCarta(origem,index,destino,mao_mesa,Duration.ZERO,ordem);
    }
    private SequentialTransition transfereCarta(int origem, int index, int destino, int mao_mesa, Duration tempo, ClasseMao ordem) {
        //System.out.println("transfereCarta()"+origem+" "+destino+" "+mao_mesa);
        SequentialTransition seqtransition = new SequentialTransition(new PauseTransition(Duration.millis(1)));
        ClasseCarta ctOrigem = mao[origem].cartas.get(index);
        ClasseMao maoDestino;
        int ms = destino - 8;
        if ((destino==MESA1)||(destino==MESA2)){
            maoDestino = mesa[ms].maos.get(mao_mesa);
        }
        else {
            maoDestino = mao[destino];
        }
            
        ctOrigem.selecionada=false; //ISSO É IMPORTANTE
        //ctOrigem.local = destino; //ISSO NÃO É IMPORTANTE
        ctOrigem.posX = maoDestino.getNextPos("X"); //isso é pro morto e pro lixo
        ctOrigem.posY = maoDestino.getNextPos("Y"); //isso também
        
        //Define o angulo que a carta deverá ficar
        ctOrigem.defineAngulo(destino);
        
        if ((destino==JOG1)||(destino==JOG2)||(destino==JOG3)||(destino==JOG4)) {
            //considera que a mão do jogador 1 está sempre organizada por naipe
            int lugar = ctOrigem.ordemNaipe();
            int indiceLugar = 0;
            if (!maoDestino.cartas.isEmpty()){
                indiceLugar = maoDestino.cartas.size();
                for (int k=0;k<maoDestino.cartas.size();k++){ //Define a posição da carta em relação às outras cartas do jogador
                    if (lugar < maoDestino.cartas.get(k).ordemNaipe()){
                        //índice do lugar na lista de cartas
                        indiceLugar = k;
                        break;
                    }
                }
                if (indiceLugar>=maoDestino.cartas.size()){
                    setZOrder(ctOrigem,maoDestino.cartas.get(maoDestino.cartas.size()-1).zOrder+1);
                } else {
                    setZOrder(ctOrigem,maoDestino.cartas.get(indiceLugar));
                }
            }

            maoDestino.cartas.add(indiceLugar,ctOrigem); //coloca a carta no lugar correto
            mao[origem].cartas.remove(index); //remove a carta da mão antiga
            
            //Faz a animação
            ParallelTransition pt = new ParallelTransition();
            if (!DEBUGMODE){
                if ((destino!=JOG1)&&(origem==LIXO)){
                    pt.getChildren().add( //cria uma animação em paralelo
                        maoDestino.cartas.get(indiceLugar).fxViraPraBaixo(tempo)
                    ); //da carta virando pra baixo
                }
                if ((destino==JOG1)&&((origem==MONTE)||(origem==MORTO1)||(origem==MORTO2))){
                    pt.getChildren().add( //cria uma animação em paralelo
                        maoDestino.cartas.get(indiceLugar).fxViraPraCima(tempo)
                    ); //da carta virando pra cima
                }
            }
            int tamanho = maoDestino.cartas.size();
            for (int k=0;k<tamanho;k++){
                //Define a nova posição de todas as cartas da mão, abrindo espaço para a carta que vai entrar
                
                if (destino==JOG3){
                    maoDestino.cartas.get(k).posX = screenCenterX - maoDestino.deltaX * (k - ((double)tamanho/2));
                    maoDestino.cartas.get(k).posY = maoDestino.posInitY;
                }
                else if (destino==JOG2){
                    maoDestino.cartas.get(k).posX = maoDestino.posInitX;
                    maoDestino.cartas.get(k).posY = screenCenterY - maoDestino.deltaY * (k - ((double)tamanho/2));
                }
                else if (destino==JOG4){
                    maoDestino.cartas.get(k).posX = maoDestino.posInitX;
                    maoDestino.cartas.get(k).posY = screenCenterY + maoDestino.deltaY * (k - ((double)tamanho/2));
                }
                else if (destino==JOG1){
                    maoDestino.cartas.get(k).posX = screenCenterX + calculaX(k,tamanho);
                    maoDestino.cartas.get(k).posY = maoDestino.posInitY - calculaY(k,tamanho);
                    maoDestino.cartas.get(k).angulo = calculaAngulo(k,tamanho)*180.0/Math.PI;
                }
                pt.getChildren().add(
                    arrumaCarta(maoDestino.cartas.get(k),true,tempo)    //e todas se movendo para o lugar correto
                );
            }   
            if (somCartaLigado){
                Timeline tl = new Timeline();
                tl.getKeyFrames().add(new KeyFrame(Duration.millis(0.0),playCarta));
                tl.getKeyFrames().add(new KeyFrame(Duration.millis(600),stopCarta));
                pt.getChildren().add(tl);
            }
            
            seqtransition = new SequentialTransition(seqtransition, pt); //adiciona à animação já existente
        }
        
        else if (destino==LIXO){
            maoDestino.cartas.add(ctOrigem);
            mao[origem].cartas.remove(index);
            
            ParallelTransition pt = new ParallelTransition();
            //move as cartas da mão de origem que estavam à direita da carta removida, para a posição correta fechando o espaço
            int tamanho = mao[origem].cartas.size();
            if (origem!=MONTE){
                for (int k=0;k<tamanho;k++){
                    if (origem==JOG3){
                        mao[origem].cartas.get(k).posX = screenCenterX - mao[origem].deltaX * (k - ((double)tamanho/2));
                        mao[origem].cartas.get(k).posY = mao[origem].posInitY;
                    }
                    else if (origem==JOG2){
                        mao[origem].cartas.get(k).posX = mao[origem].posInitX;
                        mao[origem].cartas.get(k).posY = screenCenterY - mao[origem].deltaY * (k - ((double)tamanho/2));
                    }
                    else if (origem==JOG4){
                        mao[origem].cartas.get(k).posX = mao[origem].posInitX;
                        mao[origem].cartas.get(k).posY = screenCenterY + mao[origem].deltaY * (k - ((double)tamanho/2));
                    }
                    else if (origem==JOG1){
                        mao[origem].cartas.get(k).posX = screenCenterX + calculaX(k,tamanho);
                        mao[origem].cartas.get(k).posY = mao[origem].posInitY - calculaY(k,tamanho);
                        mao[origem].cartas.get(k).angulo = calculaAngulo(k,tamanho)*180.0/Math.PI;
                    }
                    pt.getChildren().add(arrumaCarta(mao[origem].cartas.get(k),true,tempo));
                }
            }
            
            //leva a carta para o lixo
            if (!DEBUGMODE){
                if (origem!=JOG1){
                    pt.getChildren().add(maoDestino.cartas.get(maoDestino.cartas.size()-1)
                        .fxViraPraCima(tempo));
                }
            }
            //audio
            if (somCartaLigado){
                Timeline tl = new Timeline();
                tl.getKeyFrames().add(new KeyFrame(Duration.millis(0.0),playCarta));
                tl.getKeyFrames().add(new KeyFrame(Duration.millis(600),stopCarta));
                pt.getChildren().add(tl);
            }
            
            seqtransition = new SequentialTransition(seqtransition,
                new ParallelTransition(
                    pt,
                    arrumaCarta(maoDestino.cartas.get(maoDestino.cartas.size()-1),true,tempo,true)
                )
            );
        }
        
        else if ((destino==MESA1)||(destino==MESA2)) {
            Point2D pos;
            //considera que a mão da mesa está sempre organizada por naipe
            int lugar = ctOrigem.ordemCanastra(ordem);
            if (lugar==-1) lugar=0;
            int indiceLugar = 0;
            if (!maoDestino.cartas.isEmpty()){
                indiceLugar = maoDestino.cartas.size();
                for (int k=0;k<maoDestino.cartas.size();k++){ //Define a posição da carta na mesa
                    if (lugar < maoDestino.cartas.get(k).ordemCanastra(ordem)){
                        //índice do lugar na lista de cartas
                        indiceLugar = k;
                        break;
                    }
                }
                if (indiceLugar>=maoDestino.cartas.size()){
                    setZOrder(ctOrigem,maoDestino.cartas.get(maoDestino.cartas.size()-1).zOrder+1);
                } else {
                    setZOrder(ctOrigem,maoDestino.cartas.get(indiceLugar));
                }
            }
            
            maoDestino.cartas.add(indiceLugar,ctOrigem); //coloca a carta no lugar correto
            mao[origem].cartas.remove(index); //remove a carta da mão antiga

            ParallelTransition pt = new ParallelTransition();
            //move as cartas da mão de origem que estavam à direita da carta removida, para a posição correta fechando o espaço
            int tamanho = mao[origem].cartas.size();
            for (int k=0;k<tamanho;k++){
                if (origem==JOG3){
                    mao[origem].cartas.get(k).posX = screenCenterX - mao[origem].deltaX * (k - ((double)tamanho/2));
                    mao[origem].cartas.get(k).posY = mao[origem].posInitY;
                }
                else if (origem==JOG2){
                    mao[origem].cartas.get(k).posX = mao[origem].posInitX;
                    mao[origem].cartas.get(k).posY = screenCenterY - mao[origem].deltaY * (k - ((double)tamanho/2));
                }
                else if (origem==JOG4){
                    mao[origem].cartas.get(k).posX = mao[origem].posInitX;
                    mao[origem].cartas.get(k).posY = screenCenterY + mao[origem].deltaY * (k - ((double)tamanho/2));
                }
                else if (origem==JOG1){
                    mao[origem].cartas.get(k).posX = screenCenterX + calculaX(k,tamanho);
                    mao[origem].cartas.get(k).posY = mao[origem].posInitY - calculaY(k,tamanho);
                    mao[origem].cartas.get(k).angulo = calculaAngulo(k,tamanho)*180.0/Math.PI;
                }
                pt.getChildren().add(arrumaCarta(mao[origem].cartas.get(k),true,tempo));
            }

            //Se a carta estava virada pra baixo, 
            if (!DEBUGMODE){
                if ((origem==JOG2)||(origem==JOG3)||(origem==JOG4)){
                    pt.getChildren().add(
                        maoDestino.cartas.get(indiceLugar).fxViraPraCima(tempo)
                    ); //faz a animação da carta virando pra cima
                }
            }

            pos = mesa[ms].posMao(mesa[ms].maos.indexOf(maoDestino));
            maoDestino.setPosInit(pos.getX(), pos.getY(), 10.0, 0.0);
            for (int k=0;k<maoDestino.cartas.size();k++) {
                //Define a posição das cartas do jogo para abrir espaço para a carta que vai entrar
                maoDestino.cartas.get(k).posX = maoDestino.posInitX + maoDestino.deltaX * k;
                maoDestino.cartas.get(k).posY = maoDestino.posInitY;
                pt.getChildren().add(
                    arrumaCarta(maoDestino.cartas.get(k),true,tempo)    //e todas se movendo para o lugar correto
                );
            }
            
            //audio
            if (somCartaLigado){
                Timeline tl = new Timeline();
                tl.getKeyFrames().add(new KeyFrame(Duration.millis(0.0),playCarta));
                tl.getKeyFrames().add(new KeyFrame(Duration.millis(600),stopCarta));
                pt.getChildren().add(tl);
            }
            
            //move as mãos da direita para dar espaço
            if ((mao_mesa + 1) < mesa[ms].maos.size()){
                for (int mm=mao_mesa+1;mm<mesa[ms].maos.size();mm++){
                    pos = mesa[ms].posMao(mm);
                    mesa[ms].maos.get(mm).setPosInit(pos.getX(), pos.getY(), 10, 0);
                    for (int crt=0; crt < mesa[ms].maos.get(mm).cartas.size(); crt++) {
                        mesa[ms].maos.get(mm).cartas.get(crt).posX = mesa[ms].maos.get(mm).posInitX + mesa[ms].maos.get(mm).deltaX * crt;
                        mesa[ms].maos.get(mm).cartas.get(crt).posY = mesa[ms].maos.get(mm).posInitY + mesa[ms].maos.get(mm).deltaY * crt;
                        pt.getChildren().add(arrumaCarta(mesa[ms].maos.get(mm).cartas.get(crt),true));
                    }
                    pt.getChildren().add(mesa[ms].maos.get(mm).fxMoveTarja());
                }
            } 
            
            seqtransition = new SequentialTransition(seqtransition, pt);
            
        }
        
        else if ((destino==MORTO1)||(destino==MORTO2)||(destino==MONTE)){
            maoDestino.cartas.add(ctOrigem);
            mao[origem].cartas.remove(index);
            ParallelTransition pt = new ParallelTransition();
            
            //audio
            if (somCartaLigado){
                Timeline tl = new Timeline();
                tl.getKeyFrames().add(new KeyFrame(Duration.millis(0.0),playCarta));
                tl.getKeyFrames().add(new KeyFrame(Duration.millis(600),stopCarta));
                pt.getChildren().add(tl);
            }

            if (DEBUGMODE && (destino==MONTE)){
                pt.getChildren().add(
                    maoDestino.cartas.get(maoDestino.cartas.size()-1).fxViraPraCima(tempo)
                ); //faz a animação da carta virando pra baixo
            }
            
            maoDestino.cartas.get(maoDestino.cartas.size()-1).posX = maoDestino.posInitX;
            maoDestino.cartas.get(maoDestino.cartas.size()-1).posY = maoDestino.posInitY;
            maoDestino.posInitX += maoDestino.deltaX;
            pt.getChildren().add(
                arrumaCarta(maoDestino.cartas.get(maoDestino.cartas.size()-1),true,tempo,true)    //e todas se movendo para o lugar correto
            );
            
            seqtransition = new SequentialTransition(seqtransition,pt);
        }
        return seqtransition;
    }
    
    private void setZOrder(ClasseCarta ctOrigem, int zOrder){
        if (zOrder - ctOrigem.zOrder > 1){
            for (int k=0;k<104;k++){
                if ((baralho[k].zOrder > ctOrigem.zOrder) && (baralho[k].zOrder < zOrder)){
                    baralho[k].zOrder--;
                }
            }
            ctOrigem.zOrder = zOrder - 1;
        }
        else if (ctOrigem.zOrder - zOrder > 0){
            int temp = zOrder;
            for (int k=0;k<104;k++){
                if ((baralho[k].zOrder >= zOrder) && (baralho[k].zOrder < ctOrigem.zOrder)){
                    baralho[k].zOrder++;
                }
            }
            ctOrigem.zOrder = temp;
        }
    }
    private void setZOrder(ClasseCarta ctOrigem, ClasseCarta ctDestino){
        setZOrder(ctOrigem,ctDestino.zOrder);
    }
    
    private void embaralha() {
        //System.out.println("embaralha()");
        int j,k;
        for (k=0;k<MAXCARTAS*2;k++){
            j=(int)Math.floor(Math.random()*MAXCARTAS);
            if (j>MAXCARTAS-1) j=MAXCARTAS-1;
            mao[MONTE].enviaProFinal(j);
        }
    }
    
    private void corta() {
        //System.out.println("corta()");
        int j;
        j=(int)Math.floor(Math.random()*MAXCARTAS);
        if (j>MAXCARTAS-1) j=MAXCARTAS-1;
        for (int k=0;k<j;k++){
            mao[MONTE].enviaProFinal(k);
        }
    }

    private SequentialTransition distribui(Duration tempo) {
        //System.out.println("distribui()");
        double t;
        SequentialTransition seqtransition = new SequentialTransition(new PauseTransition(Duration.millis(1)));
        int k,j;
        for (k=0;k<CARTASMORTO;k++){
            for (j=MORTO1;j<=MORTO2;j++){
                seqtransition = new SequentialTransition(seqtransition,transfereCarta(MONTE,mao[MONTE].cartas.size()-1,j,tempo));
            }
        }
        for (k=0;k<CARTASJOG;k++){
            for (j=JOG1;j<=JOG4;j++){
                if (j==JOG1) t=20.0; else t=1.0;
                seqtransition = new SequentialTransition(seqtransition,transfereCarta(MONTE,mao[MONTE].cartas.size()-1,j,tempo.multiply(t)));
            }
        }
        return seqtransition;
    }
    
    private void descarta() {
        //System.out.println("descarta()");
        int index=0, s=0,k;
        for (k=0;k<mao[JOG1].cartas.size();k++){
            if (mao[JOG1].cartas.get(k).selecionada){
                s++;
                index=k;
            }
        }
        if (s==1){
            if (!((mao[JOG1].cartas.get(index).carta==cartaLixo)&&(mao[JOG1].cartas.get(index).naipe==naipeLixo))){
                cartaLixo = 0; //carta válida, pode esquecer a carta única que estava no lixo antes da jogada
                naipeLixo = 0;
                mao[JOG1].cartas.get(index).selecionada = false;
                ja_comprou = false; //permite compra na próxima jogada
                SequentialTransition t = new SequentialTransition(new PauseTransition(Duration.millis(1000)));
                t = new SequentialTransition(transfereCarta(JOG1, index, LIXO),t);
                t.setOnFinished(new EventHandler<ActionEvent>(){
                    @Override public void handle (ActionEvent e){
                        SequentialTransition tempseq = verificaSeAcabou(JOG1, MESA1);
                        if (tempseq!=null){
                            tempseq.setOnFinished(new EventHandler<ActionEvent>(){
                                @Override public void handle (ActionEvent e){
                                    if (jogo_acabou){
                                        oldvez = -1;
                                        vez = -1;
                                    } else {
                                        vez = 1.1;
                                    }
                                }
                            });
                            tempseq.play();
                        } else {
                            if (jogo_acabou){
                                oldvez = -1;
                                vez = -1;
                            } else { 
                                vez = 1.1;
                            }
                        }
                    }
                });
                t.play();
            } else {
                mao[JOG1].cartas.get(index).fxToggleSelect().play(); //se for a mesma carta que estava sozinha no lixo, não pode descartar
            }
        }
    }
    
    private SequentialTransition botCompra(int jog) {
        //System.out.println("Bot()");
        
        ////////////////////////////////////
        //Comprando...
        ////////////////////////////////////
        int ms;
        boolean serveProBot = false;
        if (jog==JOG3) ms=0; else ms=1;
        SequentialTransition botsplay = new SequentialTransition(new PauseTransition(Duration.millis(1)));
        botPegouDoLixo.carta = 0;
        botPegouDoLixo.naipe = 0;
        for (ClasseCarta ct:mao[LIXO].cartas) { //se serve na mesa ou se é coringa
            serveProBot = serveNaMesa(ct,ms);
            if (serveProBot) break;
        }
        if (!serveProBot) {
            for (ClasseCarta ct:mao[LIXO].cartas) { //se uma carta serve na mão (talvez redundante com o 'if' de baixo)
                serveProBot = serveNaMao(ct,jog);
                if (serveProBot) break;
            }
        }
        if (!serveProBot) {
            ClasseMao mao_temp = new ClasseMao(); //se o lixo combinado com a mão tem jogo válido
            for (ClasseCarta ct:mao[LIXO].cartas) {
                mao_temp.cartas.add(ct);
            }
            for (ClasseCarta ct:mao[jog].cartas) {
                mao_temp.cartas.add(ct);
            }
            serveProBot = mao_temp.temJogoValido();
        }
        if (!serveProBot) {
            if (mao[LIXO].temJogoValido()) serveProBot = true; //se tem jogo formado no lixo
        }
        int kLixo[] = {8, 8, 8, 7, 6, 5, 4, 4, 4}; 
        if ((int)Math.floor(Math.random() * kLixo[Math.min(mao[jog].cartas.size(),8)]) < mao[LIXO].cartas.size() || serveProBot) {
            if (mao[LIXO].cartas.size()==1){
                botPegouDoLixo.carta = mao[LIXO].cartas.get(0).carta;
                botPegouDoLixo.naipe = mao[LIXO].cartas.get(0).naipe;
            }
            ParallelTransition pt = new ParallelTransition();
            while (!mao[LIXO].cartas.isEmpty()){
                pt.getChildren().add(transfereCarta(LIXO, 0, jog));
            }
            botsplay = new SequentialTransition(botsplay,pt);
        }
        else {
            botsplay = new SequentialTransition(botsplay,transfereCarta(MONTE, mao[MONTE].cartas.size()-1, jog));
        }
        return botsplay;
    }
    
    private SequentialTransition botDesce(int jog) {
        //////////////////////////
        //Descendo...
        //////////////////////////
        
        //////////////////
        //a fazer:
        //priorizar em qual jogo descer
        //////////////////
        
        SequentialTransition descida;
        SequentialTransition botsplay = new SequentialTransition(new PauseTransition(Duration.millis(1)));
        ClasseMao mao_temp, mtmp;
        boolean serviu, fazdenovo, podedescer;
        int ms, np;
        
        if (jog==JOG3) ms=0;
        else ms=1;
        
        for(ClasseMao cMao:mesa[ms].maos){
            cMao.anim = new ParallelTransition();
        }
        
        fazdenovo = true;
        while (fazdenovo){ //sempre que desce um jogo ou uma carta, refaz o procedimento
            fazdenovo = false;
            //DESCE CARTAS QUE SERVEM
            serviu = true;
            while (serviu) { //sempre que servir uma carta, verifica a mão novamente pra encontrar outra carta que sirva
                serviu = false;
                if (!mesa[ms].maos.isEmpty()){ //se não tem carta na mesa, as cartas da mão não vão servir em lugar nenhum
                    if (mao[jog].cartas.size() == mao[jog].contaCarta("2")) { //só sobrou coringa na mão
                        System.out.println("[Jogador " + (jog+1) + "] Só sobrou coringa.");
                        for (int ct = mao[jog].cartas.size()-1; ct>=0; ct--) {
                            for (int t = 3; t < 15; t++) { //procura o menor jogo em que o coringa sirva
                                //procura o menor jogo em que o coringa sirva
                                for (ClasseMao iMao:mesa[ms].maos) {
                                    if (iMao.cartas.size() == t) {
                                        mao_temp = new ClasseMao();
                                        for (ClasseCarta crt:iMao.cartas){
                                            //mão temporária com o jogo da mesa
                                            mao_temp.cartas.add(crt);
                                        }
                                        mao_temp.cartas.add(mao[jog].cartas.get(ct));
                                        if (mao_temp.jogoValido().valido){
                                            mao[jog].cartas.get(ct).selecionada = true;
                                            descida = desceSelecionadas(jog,mesa[ms].maos.indexOf(iMao));
                                            if (descida!=null){
                                                System.out.println("[Jogador " + (jog+1) + "] Vou jogar esse coringa nesse joguinho que só tem " + t + " cartas.");
                                                iMao.anim.getChildren().add(descida);
                                                serviu = true;
                                                atualizaPontos();
                                                if (mao[jog].cartas.isEmpty()){
                                                    //se acabaram as cartas da mão
                                                    botsplay = new SequentialTransition(botsplay, animacao(ms));
                                                    SequentialTransition tempseq = verificaSeAcabou(jog,ms);
                                                    if (tempseq!=null) {
                                                        botsplay = new SequentialTransition(botsplay, tempseq);
                                                    }
                                                    if (jogo_acabou) return botsplay; //se acabou o jogo, retorna, senão faz mais um loop
                                                }
                                            }
                                            if (serviu) break;
                                        }
                                    }
                                }
                                if (serviu) break;
                            }
                            if (serviu) break;
                        }
                    }
                    
                    //procura cartas que servem (mesmo naipe de coringa)
                    for (int m = 0; m < mesa[ms].maos.size(); m++){ //de jogo em jogo da mesa... (falta definir prioridade)
                        for (int k = mao[jog].cartas.size()-1; k >= 0; k--){ //...verifica cada carta da mão pra ver se serve
                            if (mesa[ms].maos.get(m).naipe() == mao[jog].cartas.get(k).naipe) { //mas só olha as do mesmo naipe do jogo da mesa
                                podedescer = true;
                                mao_temp = new ClasseMao();
                                for (ClasseCarta crt:mesa[ms].maos.get(m).cartas){
                                    //mão temporária com o jogo da mesa
                                    mao_temp.cartas.add(crt);
                                }
                                mao_temp.cartas.add(mao[jog].cartas.get(k)); //adiciona a carta à mão temporária
                                if  (
                                        (
                                            (
                                                mao_temp.contaCarta("2") > 1
                                            ) //dois coringas no jogo
                                            &&
                                            (
                                                mao[jog].cartas.get(k).carta == 2
                                            )
                                        ) //e descendo coringa
                                        &&
                                        (
                                            (
                                                mao[jog].cartas.size() > 2
                                            ) //bastante carta na mão
                                            ||
                                            (
                                                mao[jog].cartas.size() - mao[jog].contaCarta("2") > 2
                                            )
                                        )
                                    ) { //ou está cheio de coringa
                                    //evita descer coringa em jogo que já tem coringa, exceto se for pra bater ou se está cheio de coringa na mão
                                    System.out.println("[Jogador " + (jog+1) + "] Não vou sujar esse jogo de " + this.naipes[mao[jog].cartas.get(k).naipe-1] + " agora.");
                                    podedescer = false;
                                }
                                
                                if (mao_temp.jogoValido().valido){ //verifica se o jogo vai ficar válido
                                    if ((mesa[ms].maos.get(m).limpa())
                                            //&&(mao[jog].cartas.size()>=2)
                                            &&(mao_temp.suja())){
                                        System.out.println("[Jogador " + (jog+1) + "] Não quero sujar uma canastra limpa.");
                                        podedescer=false;
                                    }
                                    if (mao_temp.limpa()
                                            || (podedescer 
                                                && !(mao[jog].cartas.size()<=2
                                                    && (mesa[ms].pegouMorto || (mao[MORTO1].cartas.isEmpty() && mao[MORTO2].cartas.isEmpty()))
                                                    && !mesa[ms].temLimpa()
                                                )
                                            )
                                        ) { 
                                        //não pode descer se for bater e não tiver limpa, a não ser que o jogo onde vai descer vai virar uma limpa
                                        mao[jog].cartas.get(k).selecionada = true;
                                        descida = desceSelecionadas(jog,m);
                                        if (descida!=null){
                                            System.out.println("[Jogador " + (jog+1) + "] Ess" + (this.cartas[mao_temp.cartas.get(mao_temp.cartas.size()-1).carta - 1]) + " de " +  (this.naipes[mao_temp.cartas.get(mao_temp.cartas.size()-1).naipe - 1]) + " serviu.");
                                            mesa[ms].maos.get(m).anim.getChildren().add(descida);
                                            serviu = true;
                                            atualizaPontos();
                                            if (mao[jog].cartas.isEmpty()){
                                                //se acabaram as cartas da mão
                                                botsplay = new SequentialTransition(botsplay, animacao(ms));
                                                SequentialTransition tempseq = verificaSeAcabou(jog,ms);
                                                if (tempseq!=null) {
                                                    botsplay = new SequentialTransition(botsplay, tempseq);
                                                }
                                                if (jogo_acabou) return botsplay; //se acabou o jogo, retorna, senão faz mais um loop
                                            }
                                        }
                                    }
                                }
                            }
                            if (serviu) break;
                        }//next carta da mão
                        if (serviu) break;
                    }//next jogo da mesa
                }//end if mesa[ms].maos.isEmpty()
            }//end while serviu
            
            //DESCE JOGOS
            
            //se só sobrou um jogo válido na mão, bate
            if (mao[jog].jogoValido().valido){
                for (ClasseCarta cc:mao[jog].cartas){
                    cc.selecionada=true;
                }
                descida = desceSelecionadas(jog,-1);
                if (descida!=null){
                    System.out.println("[Jogador " + (jog+1) + "] Olha, ficou só um jogo na minha mão, desci e bati.");
                    mesa[ms].maos.get(mesa[ms].maos.size()-1).anim.getChildren().add(descida);
                    if (mao[jog].cartas.isEmpty()){
                        //se acabaram as cartas da mão
                        botsplay = new SequentialTransition(botsplay, animacao(ms));
                        SequentialTransition tempseq = verificaSeAcabou(jog,ms);
                        if (tempseq!=null) {
                            botsplay = new SequentialTransition(botsplay, tempseq);
                        }
                        if (jogo_acabou) return botsplay; //se acabou o jogo, retorna, senão faz mais um loop
                    }
                    fazdenovo = true;
                }
            }
            
            //procura jogos de 3 cartas pra descer
            for (np=ESPADAS;np<=OUROS;np++){
                mtmp = new ClasseMao(); //sub-mão temporária com cartas do mesmo naipe
                for (ClasseCarta c:mao[jog].cartas) {
                    if (c.naipe==np) mtmp.cartas.add(c);
                }
                if (mtmp.cartas.size()>=3){ //se não tem nem 3 cartas desse naipe, não precisa tentar descer
                    //System.out.println("tem pelo menos 3 cartas do mesmo naipe");
                    for (int k1=0;k1<mtmp.cartas.size()-2;k1++){
                        for (int k2=k1+1;k2<mtmp.cartas.size()-1;k2++){
                            for (int k3=k2+1;k3<mtmp.cartas.size();k3++){
                                mao_temp = new ClasseMao(); //sub-mão de mtmp com combinação de 3 cartas
                                mao_temp.cartas.add(mtmp.cartas.get(k1));
                                mao_temp.cartas.add(mtmp.cartas.get(k2));
                                mao_temp.cartas.add(mtmp.cartas.get(k3));
                                //não pode descer se for bater e não tiver limpa
                                if ((!(mao_temp.contaCarta("2")==2 && mao[MONTE].cartas.size()>5))&&mao_temp.jogoValido().valido&&(!((mao[jog].cartas.size()-mao_temp.cartas.size())<=1&&(mesa[ms].pegouMorto||(mao[MORTO1].cartas.isEmpty()&&mao[MORTO2].cartas.isEmpty())&&!mesa[ms].temLimpa())))){
                                    //System.out.println("jogo valido e vai sobrar mais de 1 carta na mão");
                                    for (ClasseCarta c:mao_temp.cartas){
                                        for (ClasseCarta cc:mao[jog].cartas){
                                            if (c.equals(cc)){
                                                cc.selecionada=true;
                                                //System.out.print("selecionou pra descer");
                                            }
                                        }
                                    }
                                    descida = desceSelecionadas(jog,-1);
                                    if (descida!=null){
                                        System.out.println("[Jogador " + (jog+1) + "] Desci um jogo de " +  this.naipes[np - 1] + ".");
                                        mesa[ms].maos.get(mesa[ms].maos.size()-1).anim.getChildren().add(descida);
                                        if (mao[jog].cartas.isEmpty()){
                                            //se acabaram as cartas da mão
                                            botsplay = new SequentialTransition(botsplay, animacao(ms));
                                            SequentialTransition tempseq = verificaSeAcabou(jog,ms);
                                            if (tempseq!=null) {
                                                botsplay = new SequentialTransition(botsplay, tempseq);
                                            }
                                            if (jogo_acabou) return botsplay; //se acabou o jogo, retorna, senão faz mais um loop
                                        }
                                        fazdenovo = true;
                                    }
                                }
                                if (fazdenovo) break;
                            } //next k3
                            if (fazdenovo) break;
                        } //next k2
                        if (fazdenovo) break;
                    } //next k1
                }
                if (fazdenovo) break;
            } //next np
        } //end while fazdenovo
        for (ClasseMao cMao:mesa[ms].maos){
            if (cMao.anim!=null && !cMao.anim.getChildren().isEmpty()){
                botsplay = new SequentialTransition(botsplay, cMao.anim);
                cMao.anim = new ParallelTransition();
            }
        }
        return botsplay;
    }
    
    private SequentialTransition botDescarta(int jog) {
        //////////////////////////
        //Descartando...
        //////////////////////////
        //boolean descartou = false, serve_adv;
        int k = -1, ms = 1, tentativas = 0;
        SequentialTransition tempseq;
        SequentialTransition botsplay = new SequentialTransition(new PauseTransition(Duration.millis(1)));
        if (jog==JOG3) ms=0; //jogador 3 é seu parceiro, desce na mesma mesa que você
        mao[jog].zeraDescarte();
        int menor = 5000;
        for (k=0; k < mao[jog].cartas.size(); k++) {
            ClasseCarta ct = mao[jog].cartas.get(k);
            if (ct.carta == 2) ct.descarte += 150;
            if (serveProAdversario(jog,k)) ct.descarte += ct.valor * 2;
            ct.descarte += (daCanastraProAdversario(jog,k));
            if (ct.carta==botPegouDoLixo.carta && ct.naipe==botPegouDoLixo.naipe) ct.descarte +=10000;
            if (mao[jog].temRepetida(ct)) ct.descarte -= 20;
            if (serveNaMesa(ct,ms)) ct.descarte += ct.valor;
            if (serveNaMao(ct,jog)) ct.descarte += 5;
            if (ct.descarte < menor) menor = ct.descarte;
        }
        ClasseMao mao_temp = new ClasseMao();
        for (k=0; k < mao[jog].cartas.size(); k++) {
            if (mao[jog].cartas.get(k).descarte == menor) {
                mao_temp.cartas.add(mao[jog].cartas.get(k));
            }
        }
        /*while (!descartou&&tentativas<mao[jog].cartas.size()*3) {
            k = -1;
            //tenta escolher uma carta repetida da mão
            for (int ct1 = 0; ct1 < mao[jog].cartas.size(); ct1++){
                for (int ct2 = 0; ct2 < mao[jog].cartas.size(); ct2++){
                    if (ct1 != ct2){
                        if ((mao[jog].cartas.get(ct1).carta == mao[jog].cartas.get(ct2).carta)
                            && (mao[jog].cartas.get(ct1).naipe == mao[jog].cartas.get(ct2).naipe) //se a carta tem repetida e não é coringa
                            && (mao[jog].cartas.get(ct1).carta != 2)){
                            if (!mao[jog].cartas.get(ct1).selecionada){
                                k = ct1;
                                mao[jog].cartas.get(ct1).selecionada = true;
                                mao[jog].cartas.get(ct2).selecionada = true;
                            }
                        }
                    }
                }
            }
            if (k!=-1) {
                serve_adv = serveProAdversario(jog, k);
                if (!serve_adv){
                    if (!((mao[jog].cartas.get(k).carta==botPegouDoLixo.carta)&&(mao[jog].cartas.get(k).naipe==botPegouDoLixo.naipe))){
                        descartou = true;
                        System.out.println("[Jogador " + (jog+1) + "] Descartei essa carta repetida que não serve pra eles.");
                    }
                }
            }
            //tenta escolher uma carta que já tenha na mesa
            for (int ct1 = 0; ct1 < mao[jog].cartas.size(); ct1++){
                for (int m = 0; m < mesa[ms].maos.size(); m++){
                    for (int ct2 = 0; ct2 < mesa[ms].maos.get(m).cartas.size(); ct2++){
                        if ((mao[jog].cartas.get(ct1).carta == mesa[ms].maos.get(m).cartas.get(ct2).carta)
                            && (mao[jog].cartas.get(ct1).naipe == mesa[ms].maos.get(m).cartas.get(ct2).naipe) //se a carta já está na mesa e não é coringa
                            && (mao[jog].cartas.get(ct1).carta != 2)){
                            if (!mao[jog].cartas.get(ct1).selecionada){
                                k = ct1;
                                mao[jog].cartas.get(ct1).selecionada = true;
                            }
                        }
                    }
                }
            }
            if (k!=-1) {
                serve_adv = serveProAdversario(jog, k);
                if (!serve_adv){
                    if (!((mao[jog].cartas.get(k).carta==botPegouDoLixo.carta)&&(mao[jog].cartas.get(k).naipe==botPegouDoLixo.naipe))){
                        descartou = true;
                        System.out.println("[Jogador " + (jog+1) + "] Descartei ess" + cartas[mao[jog].cartas.get(k).carta - 1] + " de " + naipes[mao[jog].cartas.get(k).naipe - 1] + " que já temos na mesa e que não serve pra eles.");
                    }
                }
            }
            if (!descartou){
                k = (int)Math.floor(Math.random()*mao[jog].cartas.size());
                if ((mao[jog].cartas.get(k).carta==botPegouDoLixo.carta)&&(mao[jog].cartas.get(k).naipe==botPegouDoLixo.naipe)) {
                    //não pode descartar a mesma carta que estava sozinha no lixo, passa pra próxima carta
                    System.out.println("[Jogador " + (jog+1) + "] Não posso descartar a carta que peguei do lixo.");
                    k++;
                    if (k>=mao[jog].cartas.size()) k=0; //proteção de null pointer
                }
                tentativas++;
                //verifica se a carta escolhida não serve para o adversário
                if (mao[jog].cartas.get(k).carta==2){
                    serve_adv=true;
                    System.out.println("[Jogador " + (jog+1) + "] Opa! Não quero descartar coringa.");
                }
                else {
                    serve_adv = serveProAdversario(jog, k);
                    if (serve_adv) System.out.println("[Jogador " + (jog+1) + "] Opa! Ess" + cartas[mao[jog].cartas.get(k).carta - 1] + " de " + naipes[mao[jog].cartas.get(k).naipe - 1] + " serve pra eles.");
                }
                descartou = !serve_adv;
            }
        }
        */
        if (mao_temp.cartas.isEmpty()) {
            k = (int)Math.floor(Math.random()*mao[jog].cartas.size());
            if (k<0) k=0;
            if (k>mao[jog].cartas.size()-1) k = mao[jog].cartas.size()-1;
        } else {
            k = (int)Math.floor(Math.random()*mao_temp.cartas.size());
            if (k<0) k=0;
            if (k>mao_temp.cartas.size()-1) k = mao_temp.cartas.size()-1;
            k = mao[jog].cartas.indexOf(mao_temp.cartas.get(k));
        }
        
        System.out.println("[Jogador " + (jog+1) + "] Depois de " + (tentativas+1) + " tentativa(s), descartei ess" + cartas[mao[jog].cartas.get(k).carta - 1] + " de " + naipes[mao[jog].cartas.get(k).naipe - 1] + ".");
        mao[jog].deSeleciona();
        mao[jog].zeraDescarte();
        botsplay = new SequentialTransition (botsplay,transfereCarta(jog,k,LIXO));
        tempseq = verificaSeAcabou(jog,ms); //vê se acabou o jogo, senão pega o morto
        if (tempseq!=null){
            botsplay = new SequentialTransition(botsplay,tempseq); //faz a animação do morto
        }
        return botsplay;
    }
    
    private SequentialTransition desceSelecionadas(int j, int iMao) {
        //System.out.println("desceSelecionadas()"+j+" "+m+" "+juntajogo);
        boolean eraSuja = false;
        boolean eraLimpa = false;
        boolean descendoCoringa = false;
        boolean descendoAs = false;
        SequentialTransition tempseq;
        SequentialTransition jPlay = new SequentialTransition(new PauseTransition(Duration.millis(300)));
        ClasseMao mao_temp = new ClasseMao();
        int ms, k, limpas;
        if ((j==JOG1)||(j==JOG3)) ms=0; else ms=1;
        if (iMao!=-1){
            eraLimpa = mesa[ms].maos.get(iMao).limpa();
            eraSuja = mesa[ms].maos.get(iMao).suja();
        }
        boolean vaiDescerNaUnicaLimpa=false, desceu=false, podedescer=false;
        for (k=mao[j].cartas.size()-1;k>=0;k--){
            if (mao[j].cartas.get(k).selecionada){
                mao_temp.cartas.add(mao[j].cartas.get(k));
                //System.out.println("selecionou pra descer");
            }
        }
        if (mao_temp.cartas.isEmpty()){
            //System.out.println("mao vazia");
            return null;
        }
        if (mao_temp.cartas.size()==1) {
            if (mao_temp.cartas.get(0).carta == 2) descendoCoringa = true;
            if (mao_temp.cartas.get(0).carta == 1) descendoAs = true;
        }
        if (mao[j].cartas.size()-mao_temp.cartas.size()<=1) { //se vai sobrar 1 ou nenhuma carta na mão
            //se não pegou morto, mas ainda tem morto
            if (!mesa[ms].pegouMorto&&((!mao[MORTO1].cartas.isEmpty())||(!mao[MORTO2].cartas.isEmpty()))){
                podedescer=true; //pode descer
            }
            else { //se vai bater (morto: já pegou e não tem mais, já pegou e tem, ou ainda não pegou mas não tem => vai bater)
                if (mesa[ms].temLimpa()) { //tem que ter limpa
                    limpas=0;
                    for (ClasseMao mm:mesa[ms].maos){
                        if ((mesa[ms].maos.indexOf(mm)!=iMao)&&(mm.limpa())){
                            limpas++;
                        }
                    }
                    if (limpas==0) vaiDescerNaUnicaLimpa=true;
                    podedescer=true;
                }
            }
        }
        else {
            podedescer=true;
        }
        if (iMao>-1){
            for (ClasseCarta crk:mesa[ms].maos.get(iMao).cartas){
                mao_temp.cartas.add(crk);
            }
            if (j!=JOG1){
                if (vaiDescerNaUnicaLimpa && !mao_temp.limpa()) { //por favor não suje a única limpa
                    //System.out.println("vai descer na limpa");
                    System.out.println("[Jogador " + (j+1) + "] Não vou sujar a única limpa.");
                    podedescer=false;
                }
            }
        }
        if (!podedescer&&mao_temp.jogoValido().valido&&mao_temp.limpa()){
            podedescer=true;
        }
        if (podedescer && iMao==-1 && j!=JOG1 && !mao_temp.limpa()){ //lógica para não separar jogos
            for (int m=0; m<mesa[ms].maos.size(); m++){
                if (mao_temp.naipe()==mesa[ms].maos.get(m).naipe()){
                    ClasseMao mMao = mao_temp.jogoValido().ordem;
                    int ctMaoMin = mMao.cartas.get(0).carta;
                    if (ctMaoMin == 2) ctMaoMin = mMao.cartas.get(1).carta - 1;
                    int ctMesaMax = mesa[ms].maos.get(m).cartas.get(mesa[ms].maos.get(m).cartas.size()-1).carta;
                    if (ctMesaMax == 1) ctMesaMax = 14; //Ás no final
                    if (ctMesaMax == 2) ctMesaMax = mesa[ms].maos.get(m).cartas.get(mesa[ms].maos.get(m).cartas.size()-2).carta + 1; //carta que estaria no lugar do coringa
                    //menor da mão - maior da mesa >=1 e <=2
                    if ((ctMaoMin - ctMesaMax) >= 2 && (ctMaoMin - ctMesaMax) <= 3) {
                        System.out.println("[Jogador " + (j+1) + "] Não vou separar esse jogo de " + naipes[mMao.naipe()-1] + ".");
                        podedescer = false;
                    }
                    int ctMaoMax = mMao.cartas.get(mMao.cartas.size()-1).carta;
                    if (ctMaoMax == 1) ctMaoMax = 14;
                    if (ctMaoMax == 2) ctMaoMax = mMao.cartas.get(mMao.cartas.size()-2).carta + 1;
                    int ctMesaMin = mesa[ms].maos.get(m).cartas.get(0).carta;
                    if (ctMesaMin == 2) ctMesaMin = mesa[ms].maos.get(m).cartas.get(1).carta - 1; //carta que estaria no lugar do coringa
                    //menor da mesa - maior da mão >=1 e <=2
                    if ((ctMesaMin - ctMaoMax) >= 2 && (ctMesaMin - ctMaoMax) <= 3) {
                        System.out.println("[Jogador " + (j+1) + "] Não vou separar esse jogo de " + naipes[mMao.naipe()-1] + ".");
                        podedescer = false;
                    }
                }
            }
            if ((!podedescer) && (mao[MONTE].cartas.size() <= 6 || mao[j].cartas.size()<=4)) {
                System.out.println("[Jogador " + (j+1) + "] Se bem que está no final do jogo (ou da minha mão), vou separar sim.");
                podedescer = true;
            }
        }
        if (podedescer && iMao==-1 && j!=JOG1) { // não suja alto no começo do jogo
            if (mao_temp.contaCarta("2") == 1) {
                if ((mao_temp.cartas.get(0).carta > 8) || (mao_temp.cartas.get(1).carta > 8) || (mao_temp.cartas.get(2).carta > 8) || (mao_temp.jogoValido().ordem.converte().contains("A23"))) {
                    System.out.println("[Jogador " + (j+1) + "] Não vou sujar alto ou prender o ás nesse jogo de " + naipes[mao_temp.naipe()-1] + ".");
                    podedescer = false;
                }
            }
            if ((!podedescer) && (mao[MONTE].cartas.size() <= 6 || mao[j].cartas.size()<=4)) {
                System.out.println("[Jogador " + (j+1) + "] Se bem que está no final do jogo (ou da minha mão), vou sujar sim.");
                podedescer = true;
            }
        }

        if ((iMao > -1) && (descendoCoringa) && (j!=JOG1)) { //está descendo coringa
            int menor = 15;
            for (ClasseCarta ct:mesa[ms].maos.get(iMao).cartas) {
                if ((ct.carta >= 3) && (ct.carta < menor)) {
                    menor = ct.carta;
                }
            }
            if (menor >= 7) { //está sujando jogo alto
                podedescer = false;
                System.out.println("[Jogador " + (j+1) + "] Não vou sujar alto ou prender o ás nesse jogo de " + naipes[mao_temp.naipe()-1] + ".");
            }
            if ((!podedescer) && (mao[MONTE].cartas.size() <= 6 || mao[j].cartas.size()<=4)) {
                System.out.println("[Jogador " + (j+1) + "] Se bem que está no final do jogo (ou da minha mão), vou sujar sim.");
                podedescer = true;
            }
        }
        
        if ((iMao > -1) && (descendoAs) && (j!=JOG1)) { //está descendo Ás
            int menor = 15;
            for (ClasseCarta ct:mesa[ms].maos.get(iMao).cartas) {
                if ((ct.carta >= 3) && (ct.carta < menor)) { //procura a menor carta, sem contar AS e 2.
                    menor = ct.carta;
                }
            }
            if ((menor == 3) && mao_temp.cartas.size() < 6) { //está prendendo ás em jogo pequeno
                podedescer = false;
                System.out.println("[Jogador " + (j+1) + "] Não vou prender o ás nesse jogo de " + naipes[mao_temp.naipe()-1] + ".");
            }
            if ((!podedescer) && (mao[MONTE].cartas.size() <= 6 || mao[j].cartas.size()<=4)) {
                System.out.println("[Jogador " + (j+1) + "] Se bem que está no final do jogo (ou da minha mão), vou prender sim.");
                podedescer = true;
            }
        }
                
        if (podedescer){
            //System.out.println("pode descer");
            ClasseOrdem ord = mao_temp.jogoValido();
            if (ord.valido){
                //System.out.println("jogo valido");
                desceu=true;
                ParallelTransition pt = new ParallelTransition();
                if (iMao>-1) {
                    //ordena o jogo da mesa antes de receber as novas cartas - O PROBLEMA ESTÁ AQUI ???
                    pt.getChildren().add(ordenaJogo(ms,iMao,ord.ordem));
                } else {
                    mesa[ms].maos.add(new ClasseMao());
                    iMao = mesa[ms].maos.size()-1;
                    root.getChildren().add(mesa[ms].maos.get(iMao).tarja); //adiciona a tarja ao cenário, inicialmente invisível.
                    Point2D pos = mesa[ms].posMao(iMao);
                    mesa[ms].maos.get(iMao).setPosInit(pos.getX(), pos.getY(), 10, 0);
                }
                //transfere as novas cartas
                while (mao[j].contaSelecionadas()>0){
                    for (ClasseCarta crk:mao[j].cartas){
                        if (crk.selecionada){
                            pt.getChildren().add(transfereCarta(j,mao[j].cartas.indexOf(crk),ms+8,iMao,ord.ordem));
                            break;
                        }
                    }
                }
                if (!eraLimpa && mesa[ms].maos.get(iMao).limpa()){
                    //fez uma limpa!!!
                    Timeline tl = new Timeline();
                    tl.getKeyFrames().add(new KeyFrame(Duration.millis(0),playLimpa));
                    tl.getKeyFrames().add(new KeyFrame(Duration.millis(2500),stopLimpa)); //1854
                    pt.getChildren().add(tl);
                    pt.getChildren().add(mesa[ms].maos.get(iMao).fxMostraTarja("LIMPA"));
                }
                else if (!eraSuja && mesa[ms].maos.get(iMao).suja()){
                    //fez uma suja!!!
                    Timeline tl = new Timeline();
                    tl.getKeyFrames().add(new KeyFrame(Duration.millis(0.0),playSuja));
                    tl.getKeyFrames().add(new KeyFrame(Duration.millis(2000),stopSuja)); //1416
                    pt.getChildren().add(tl);
                    pt.getChildren().add(mesa[ms].maos.get(iMao).fxMostraTarja("SUJA"));
                }
                else if (eraLimpa && mesa[ms].maos.get(iMao).limpa()){
                    pt.getChildren().add(mesa[ms].maos.get(iMao).fxMoveTarja());
                }
                else if (eraSuja && mesa[ms].maos.get(iMao).suja()){
                    pt.getChildren().add(mesa[ms].maos.get(iMao).fxMoveTarja());
                }
                jPlay = new SequentialTransition (jPlay,pt);
                if (mao[j].cartas.isEmpty()){
                    tempseq = verificaSeAcabou(j,ms);
                    if (tempseq!=null){
                        jPlay = new SequentialTransition(jPlay,tempseq);
                    }
                }                
                atualizaPontos();
            } else {
                for (ClasseCarta crt:mao[j].cartas) {
                    if (crt.selecionada) {
                        if (j==JOG1) crt.fxToggleSelect().play();
                    } else {
                        crt.selecionada = false;
                    }
                }
            }
        } else {
            for (ClasseCarta crt:mao[j].cartas) {
                if (crt.selecionada) {
                    if (j==JOG1) crt.fxToggleSelect().play();
                    else crt.selecionada = false;
                }
            }
        }
        //System.out.println(desceu);
        if (desceu){
            return jPlay;
        }
        else {
            return null;
        }
    }
    
    private ParallelTransition ordenaJogo(int ms, int m, ClasseMao maoOrdem){

        ClasseMao maoDestino = mesa[ms].maos.get(m);
        ClasseMao ordem = new ClasseMao();
        ordem.cartas.addAll(maoOrdem.cartas);
        
        //remove tudo de 'ordem' que não está em 'maoDestino'
        for (int ctOrdem = ordem.cartas.size()-1;ctOrdem>=0;ctOrdem--){
            boolean remove=true;
            for (int ctMao = maoDestino.cartas.size()-1;ctMao>=0;ctMao--){
                if ((ordem.cartas.get(ctOrdem).carta==maoDestino.cartas.get(ctMao).carta)&&(ordem.cartas.get(ctOrdem).naipe==maoDestino.cartas.get(ctMao).naipe)&&(ordem.cartas.get(ctOrdem).cor==maoDestino.cartas.get(ctMao).cor)){
                    remove=false;
                }
            }
            if (remove) ordem.cartas.remove(ctOrdem);
        }
        
        //ordena 'maoDestino' como em ordem
        for (int ctOrdem = 0; ctOrdem < ordem.cartas.size(); ctOrdem++){
            for (int ctMao = 0; ctMao<maoDestino.cartas.size();ctMao++){ 
                if ((ordem.cartas.get(ctOrdem).carta==maoDestino.cartas.get(ctMao).carta)&&(ordem.cartas.get(ctOrdem).naipe==maoDestino.cartas.get(ctMao).naipe)&&(ordem.cartas.get(ctOrdem).cor==maoDestino.cartas.get(ctMao).cor)){
                    ClasseCarta ct = maoDestino.cartas.remove(ctMao);
                    maoDestino.cartas.add(ctOrdem,ct);
                }
            }   
        }
        
        //organiza o zOrder
        for (int ctMao = 0; ctMao<maoDestino.cartas.size();ctMao++){
            int menor = 1000;
            int iMenor = -1;
            for (int k = ctMao; k<maoDestino.cartas.size();k++){
                if (maoDestino.cartas.get(k).zOrder < menor){
                    menor = maoDestino.cartas.get(k).zOrder;
                    iMenor = k;
                }
            }
            maoDestino.cartas.get(iMenor).zOrder = maoDestino.cartas.get(ctMao).zOrder;
            maoDestino.cartas.get(ctMao).zOrder = menor;
        }
        
        ParallelTransition pt = new ParallelTransition();
        for (int k=0;k<maoDestino.cartas.size();k++){
            //Define a posição das cartas da mão para abrir espaço para a carta que vai entrar
            maoDestino.cartas.get(k).posX = maoDestino.posInitX+maoDestino.deltaX*k;
            maoDestino.cartas.get(k).posY = maoDestino.posInitY+maoDestino.deltaY*k;
            pt.getChildren().add(
                arrumaCarta(maoDestino.cartas.get(k),true)    //e todas se movendo para o lugar correto
            );
        }
        return pt;
    }

    private void terminaJogo() {
        //System.out.println("terminaJogo()");
        masterListener.stop();
        String resultado;
        int[] pt={mesa[0].somaPontos()-mao[JOG1].somaPontos()-mao[JOG3].somaPontos(),mesa[1].somaPontos()-mao[JOG2].somaPontos()-mao[JOG4].somaPontos()};
        if (pt[0]>=pt[1]){
            APLAUSO.play();
            resultado = "Ganhamos!!!";
        }
        else {
            CHORO.play();
            resultado = "Perdemos!";
        }
        Region mensagem = new Region();
        mensagem.setLayoutX(341);
        mensagem.setLayoutY(192);
        mensagem.setPrefSize(683,384);
        mensagem.setStyle("-fx-background-color: #333333;-fx-background-radius: 5.0;");
        mensagem.setOpacity(0.5);
        Label texto = new Label();
        resultado = resultado + "\nNosso jogo: " + pt[0] + "\nJogo deles: " + pt[1];
        texto.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 20pt; -fx-opacity:0.8;");
        texto.setText(resultado);
        texto.setLayoutX(500);
        texto.setLayoutY(300);
        texto.setPrefSize(400, 200);
        
        root.getChildren().add(mensagem);
        root.getChildren().add(texto);
        //System.out.println("Acabou: " + pt[0] + " pra nós e " + pt[1] + " pra eles.");
        //reiniciaJogo();
    }
    
    private SequentialTransition verificaSeAcabou(int j, int m) {
        //System.out.println("verificaSeAcabou()"+j+" "+m);
        SequentialTransition seq = null;
        SequentialTransition seq1 = null;
        SequentialTransition seq2 = null;
        if (m==MESA1) m=0;
        if (m==MESA2) m=1;
        if (mao[j].cartas.isEmpty()) {
            if (mesa[m].pegouMorto) {
                mesa[m].bateu = true;
                jogo_acabou = true;
                seq1 = null;
            }
            else {
                seq1 = pegaMorto(j);
            }
        }
        if ((!jogo_acabou)&&(mao[MONTE].cartas.isEmpty())) {
            seq2 = pegaMorto(MONTE);
        }
        if (seq1==null&&seq2==null) seq = null;
        if (seq1==null&&seq2!=null) seq = seq2;
        if (seq1!=null&&seq2==null) seq = seq1;
        if (seq1!=null&&seq2!=null) seq = new SequentialTransition(seq1,seq2);
        return seq;
    }
    
    private SequentialTransition pegaMorto(int j) {
        //System.out.println("pegaMorto()"+j);
        if (mao[MORTO1].cartas.isEmpty()&&mao[MORTO2].cartas.isEmpty()) {
            jogo_acabou = true;
            return null;
        }
        else {
            SequentialTransition botsplay = new SequentialTransition(new PauseTransition(Duration.millis(1)));
            int k,m;
            if (!mao[MORTO1].cartas.isEmpty()){
                m=MORTO1;
            }
            else {
                m=MORTO2;
            }
            //mao[m].ordenaNaipe();
            //botsplay = new SequentialTransition (botsplay,mao[m].arruma(false));
            ParallelTransition pt = new ParallelTransition();
            for (k=CARTASMORTO-1;k>=0;k--) {
                pt.getChildren().add(transfereCarta(m,k,j));
            }
            if ((j==JOG1)||(j==JOG3)) mesa[0].pegouMorto = true;
            if ((j==JOG2)||(j==JOG4)) mesa[1].pegouMorto = true;
            return new SequentialTransition(botsplay,pt);
        }
    }
    
    private void atualizaPontos(){
        //    Label1.Text = mesa(1).SomaPontos
        //    Label2.Text = mesa(2).SomaPontos
    }
    
    private double calculaAngulo(int k, int n){
        if (n==1){
            return 0.0;
        } else {
            double theta = 0.7699 * n * n - 16.402 * n + 135.18;
            theta = Math.min(theta, Math.min(n*5,30));
            return ((-theta/2)+k*(theta/(n-1)))*Math.PI/180.0;
        }
    }
    
    private double calculaX(int k, int n){
        double R = 1.7776 * n * n + 5.3827 * n + 32.687;
        R = Math.max(R, 100.0);
        return R * Math.sin(calculaAngulo(k,n));
    }
    
    private double calculaY(int k, int n){
        double R = 1.7776 * n * n + 5.3827 * n + 32.687;
        R = Math.max(R, 100.0);
        return R * (Math.cos(calculaAngulo(k,n)) - 1);
    }

    /*
    private double corrigeX(int k, int n){
        double theta0 = -(0.7714 * n * n - 16.409 * n + 135.21)/2.0;
        double deltatheta = calculaAngulo(k, n);
        double R = 1.7766 * n * n + 5.4018 * n + 32.649;
        return R * Math.sin(theta0 + deltatheta);
    }

    private double corrigeY(int k, int n){
        double theta0 = -(0.7714 * n * n - 16.409 * n + 135.21)/2.0;
        double deltatheta = calculaAngulo(k, n);
        double R = 1.7766 * n * n + 5.4018 * n + 32.649;
        return R * Math.cos(theta0 + deltatheta);

    }
    */
    
    /*
    private void reiniciaJogo() {
        int i;
        for (i=JOG1;i<=MORTO2;i++) {
            mao[i].esvazia();
        }
        for (i=MESA1;i<=MESA2;i++) {
            for (int m=0;m<mesa[i-8].maos.size();m++) {
                root.getChildren().removeAll(mesa[i-8].maos.get(m).tarja);
            }
            mesa[i-8].esvazia();
            mesa[i-8].bateu=false;
            mesa[i-8].pegouMorto=false;
        }
        atualizaPontos();
        vez = -1;
        oldvez = -1;
        inicializaCartas();
        jogo();
    }
    */
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //System.out.println("main()"+args);
        launch(args);
    }

    private boolean serveProAdversario(int jog, int k) {
        if (k==-1) return false;
        int ms;
        boolean serve_adv = false;
        if (jog == JOG3) ms = 0; else ms = 1;
        for (int m=0;m<mesa[1-ms].maos.size();m++){ //1-ms é a mesa do adversário
            if ((mesa[1-ms].maos.get(m).naipe() == mao[jog].cartas.get(k).naipe) || (mao[jog].cartas.get(k).carta == 2)){ //se é do mesmo naipe ou é coringa
                ClasseMao mao_temp = new ClasseMao();
                for (int ct=mesa[1-ms].maos.get(m).cartas.size()-1;ct>=0;ct--){
                    mao_temp.cartas.add(mesa[1-ms].maos.get(m).cartas.get(ct));
                }
                mao_temp.cartas.add(mao[jog].cartas.get(k));
                if (mao_temp.jogoValido().valido){ //...e serve para o adversário...
                    serve_adv=true; //...não descarta e refaz o loop.
                    break;
                }
            }
        } //next m
        return serve_adv;
    }
    
    private int daCanastraProAdversario(int jog, int k) {
        int pontos = 0, maxpontos = 0;
        if (k==-1) return 0;
        if (!serveProAdversario(jog,k)) return 0;
        int ms;
        if (jog == JOG3) ms = 0; else ms = 1;
        if (mesa[1-ms].maos.isEmpty()) return 0;
        for (int m=0;m<mesa[1-ms].maos.size();m++){ //1-ms é a mesa do adversário
            if ((mesa[1-ms].maos.get(m).naipe() == mao[jog].cartas.get(k).naipe) || (mao[jog].cartas.get(k).carta == 2)){ //se é do mesmo naipe ou é coringa
                ClasseMao mao_temp = new ClasseMao();
                for (int ct=mesa[1-ms].maos.get(m).cartas.size()-1;ct>=0;ct--){
                    mao_temp.cartas.add(mesa[1-ms].maos.get(m).cartas.get(ct));
                }
                if (mao_temp.real()) pontos -= 1000;
                else if (mao_temp.semireal()) pontos -= 500;
                else if (mao_temp.limpa()) pontos -= 200;
                else if (mao_temp.suja()) pontos -= 100;
                pontos = mao_temp.somaPontos();
                mao_temp.cartas.add(mao[jog].cartas.get(k));
                if (mao_temp.jogoValido().valido){ //...e serve para o adversário...
                    if (mao_temp.real()) pontos += 1000;
                    else if (mao_temp.semireal()) pontos += 500;
                    else if (mao_temp.limpa()) pontos += 200;
                    else if (mao_temp.suja()) pontos += 100;
                }
                if (pontos<0) pontos = 0;
            }
            if (pontos>maxpontos) maxpontos = pontos;
        } //next m
        return maxpontos;
    }

    private boolean serveNaMesa(ClasseCarta ct, int ms) {
        boolean serve = false;
        if (ct.carta==2){
            return true;
        }
        for (int m=0;m<mesa[ms].maos.size();m++){ //ms é a mesa do Bot
            if (mesa[ms].maos.get(m).naipe()==ct.naipe){ //se é do mesmo naipe...
                ClasseMao mao_temp = new ClasseMao();
                for (int crt=mesa[ms].maos.get(m).cartas.size()-1;crt>=0;crt--){
                    mao_temp.cartas.add(mesa[ms].maos.get(m).cartas.get(crt));
                }
                mao_temp.cartas.add(ct);
                if (mao_temp.jogoValido().valido){ //...e serve para o bot...
                    serve=true; //...não descarta e refaz o loop.
                }
            }
            if (serve) break;
        }
        return serve;
    }

    private boolean serveNaMao(ClasseCarta ct, int jog) {
        ClasseMao mao_temp = new ClasseMao();
        for (ClasseCarta crt:mao[jog].cartas) {
            mao_temp.cartas.add(crt);
        }
        if (!mao_temp.cartas.contains(ct)) mao_temp.cartas.add(ct);
        return mao_temp.temJogoValido();
    }

    private SequentialTransition animacao(int ms) {
        SequentialTransition botsplay = new SequentialTransition(new PauseTransition(Duration.millis(1)));
        for (ClasseMao cMao:mesa[ms].maos){
            if (cMao.anim!=null && !cMao.anim.getChildren().isEmpty()){
                botsplay = new SequentialTransition(botsplay,cMao.anim);
                cMao.anim = new ParallelTransition();
            }
        } // adiciona as animações na variável de retorno
        return botsplay; //se acabou o jogo, retorna, senão faz mais um loop
    }
}
