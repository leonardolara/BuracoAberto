/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buracoaberto;
import static buracoaberto.ClasseCarta.TEMPO;
import java.util.List;
import java.util.ArrayList;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
/**
 *
 * @author Leonardo Lara Rodrigues
 */
public class ClasseMao {
    private final static boolean VERTICAL = true;
    private final static boolean HORIZONTAL = false;
    public List<ClasseCarta> cartas;
    public double posInitX, posInitY, deltaX, deltaY;
    public final Image limpa;
    public final Image suja;
    public ImageView tarja;
    private final String[] jogos;
    private boolean orientation;
    public ParallelTransition anim;
    public double kkk;
    
    public ClasseMao (){
        this.suja = new Image(getClass().getResourceAsStream("/buracoaberto/suja.png"),190.0,20.0,false,true);
        this.limpa = new Image(getClass().getResourceAsStream("/buracoaberto/limpa.png"),190.0,20.0,false,true);
        this.jogos = new String[]{"223456789DJQKA", "A23456789DJQKA", "A22456789DJQKA", "A23256789DJQKA", "A23426789DJQKA", "A23452789DJQKA", "A23456289DJQKA", "A23456729DJQKA", "A23456782DJQKA", "A234567892JQKA", "A23456789D2QKA", "A23456789DJ2KA", "A23456789DJQ2A", "A23456789DJQK2"};
        this.tarja = new ImageView();
        this.tarja.setVisible(false);
        this.tarja.setImage(limpa);
        this.tarja.setLayoutX(0.0);
        this.tarja.setLayoutY(0.0);
        this.tarja.setViewport(new Rectangle2D(0.0,0.0,0.1,20.0));
        this.tarja.mouseTransparentProperty();
        this.cartas = new ArrayList<>();
        this.orientation = HORIZONTAL;
        this.anim = new ParallelTransition();
        kkk = 0.0;
    }
    
    public void zeraDescarte () {
        if (this.cartas.isEmpty()) return;
        this.cartas.stream().forEach((ct) -> {
            ct.descarte = 0;
        });
    }
    
    public void deSeleciona() {
        this.cartas.stream().forEach((ct) -> {
            ct.selecionada = false;
        });
    }
    public void setPosInit(double pX, double pY, double dX, double dY) {
        this.posInitX = pX;
        this.posInitY = pY;
        this.deltaX = dX;
        this.deltaY = dY;
        if (this.deltaX==0.0) {
            this.orientation = VERTICAL;
        }
        else if (this.deltaY==0.0) {
            this.orientation = HORIZONTAL;
        }
    }
    
    public void enviaProFinal(int index) {
        ClasseCarta crt;
        crt = this.cartas.get(index);
        this.cartas.remove(index);
        this.cartas.add(crt);
    }

    public void ordenaNaipe(){
        //System.out.println("ordenaNaipe()");
        int itr, pontos, menosPontos, index=0;
        itr=this.cartas.size();
        while (itr>0) {
            menosPontos=500;
            for (int k=0;k<itr;k++) {
                pontos = this.cartas.get(k).naipe * 100 + this.cartas.get(k).carta * 3 + this.cartas.get(k).cor;
                if (pontos < menosPontos) {
                    menosPontos = pontos;
                    index=k;
                    //System.out.println(k);
                }
            }
            //System.out.println(index);
            enviaProFinal(index);
            itr--;
        }
    }
    
    public void ordenaJogo() {
        int itr, pontos, menosPontos, index=0;
        itr=cartas.size();
        while (itr>0) {
            menosPontos=20;
            for (int k=0;k<itr;k++) {
                pontos = cartas.get(k).carta;
                if (pontos < menosPontos) {
                    menosPontos = pontos;
                    index=k;
                }
            }
            enviaProFinal(index);
            itr--;
        }
        if (contaCarta("A")==2) enviaProFinal(0);
    }
    public int contaCarta(String ct) {
        String m = this.converte();
        int c=0, p;
        p=m.indexOf(ct,0);
        while (p>-1) {
            c++;
            p=m.indexOf(ct,p+1);
        }
        return c;
    }
    
    public String converte() {
        String ms = "";
        for (int k=0; k<cartas.size();k++) {
            ms=ms+jogos[1].substring(cartas.get(k).carta-1,cartas.get(k).carta);
        }
        //System.out.println(ms);
        return ms;
    }
    
    public int naipe(){
        int n=0;
        if (this.umSoNaipe(false)) {
            for (ClasseCarta ct:cartas) {
                if (ct.carta != 2) {
                    n=ct.naipe;
                    break;
                }
            }
        }
        return n;
    }
    
    private boolean umSoNaipe(boolean contacomcuringa) {
        boolean umsonaipe = true, primeiro = true;
        int naipeant=0;
        for (ClasseCarta ct:cartas) {
            if ((contacomcuringa) || (!contacomcuringa && ct.carta != 2)) {
                if (primeiro) {
                    naipeant = ct.naipe;
                    primeiro = false;
                }
                else {
                    if (naipeant != ct.naipe) {
                        umsonaipe = false;
                    }
                }
            }
        }
        return umsonaipe;
    }
    
    public ClasseOrdem jogoValido() {
        ClasseOrdem falso = new ClasseOrdem();
        falso.valido = false;
        falso.ordem = new ClasseMao();
        ClasseOrdem valido = new ClasseOrdem();
        valido.valido = true;
        valido.ordem = new ClasseMao();
        
        for (ClasseCarta k : this.cartas) {
            ClasseCarta ct = new ClasseCarta();
            ct.carta = k.carta;
            ct.naipe = k.naipe;
            ct.cor = k.cor;
            valido.ordem.cartas.add(ct);
        }
        int cf;
        String tmao;
        if (valido.ordem.cartas.size()<3) return falso;
        if (valido.ordem.cartas.size()>14) return falso;
        for (int k=1;k<=13;k++) { //de "A" a "K"
            if (k>=3) { //de "3" a "K"
                if (valido.ordem.contaCarta(this.jogos[1].substring(k-1, k))>=2) return falso; //no máximo uma carta de cada tipo
            }
            else if (k<=2) { //de "A" a "2"
                if (valido.ordem.contaCarta(this.jogos[1].substring(k-1, k))>=3) return falso; //exceto coringa(2) e ás (1) que pode ter até dois no mesmo jogo
            }
        }
        if (!valido.ordem.umSoNaipe(false)) return falso; //sem contar com coringa
        //if (valido.ordem.contaCarta("2")>=3) return falso; (redundante)
        valido.ordem.ordenaJogo(); //ordem numérica das cartas, com ás = 1
        cf = valido.ordem.cartaFaltante();
        tmao = valido.ordem.converte();
        // caso A26..., A224..., ADJ..., A2
        if ((tmao.contains("A2") && !tmao.contains("3")) //A224, A2JQ, A2QK, A2K
         || (tmao.contains("AA"))                        //AA...K, AA22...Q
         || (tmao.contains("A") && !tmao.contains("A2")) //A34...K, AQK
         || (tmao.contains("A2") && !tmao.contains("A22") && cf != 0)) { //A2... com carta faltante, tipo A2JK
            valido.ordem.enviaProFinal(0); //envia o ás pro final
            tmao = valido.ordem.converte();
        }
        if (tmao.contains("22") && !(tmao.contains("23") || tmao.contains("24"))) return falso; //se tem dois coringas, um deles precisa estar no lugar certo
        cf = valido.ordem.cartaFaltante();
        if (cf==14) return falso; //14 significa que falta mais de uma carta na sequencia
        if (cf!=0) { //se falta alguma carta
            if (tmao.contains("2")) {
                if (tmao.contains("A2") && !tmao.contains("22")) return falso; //um coringa preso pelo ás não consegue ocupar o lugar da carta que falta
                else {
                    valido.ordem.posicionaCuringa(cf);
                    return valido;
                }
            }
            else return falso;
        }
        else { //se não falta nenhuma carta
            if (tmao.contains("22")) { //A223456, 2234567, A223456789DJQK
                //joga o coringa de naipe diferente para o final, ou qualquer coringa se forem do mesmo naipe
                if ((valido.ordem.umSoNaipe(true)) || (valido.ordem.cartas.get(1).naipe != valido.ordem.naipe())){
                    valido.ordem.enviaProFinal(1); // --> A234562, 2345672, A23456789DJQK2
                } else { //se o coringa de naipe diferente não estava na posição 1
                    if (valido.ordem.cartas.get(0).carta == 2){ //deve estar na 0
                        valido.ordem.enviaProFinal(0);
                    }
                    else if (valido.ordem.cartas.get(2).carta == 2){ //ou na 2
                        valido.ordem.enviaProFinal(2);
                    }
                }
            }
            return valido;
        }
    }
    
    public int cartaFaltante() {
        int ct = 0;
        int ctinicial = 0;
        int start;
        if (cartas.get(0).carta == 2) start=1;
        else if (cartas.get(0).carta==1 && cartas.get(1).carta==2 && cartas.get(2).carta==2) start = 2;
        else start=0;
        for (int k=start;k<cartas.size();k++) {
            if (k==start) ctinicial=cartas.get(k).carta;
            else {
                if (((ctinicial + (k - start)) != cartas.get(k).carta) && !((ctinicial + (k - start)) == 14 && cartas.get(k).carta == 1)) {
                    if (ct!=0) return 14;
                    ct = ctinicial + (k - start);
                    k--;
                    ctinicial++;
                }
            }
        }
        return ct;
    }
    
    public void posicionaCuringa (int cf) {
        int ci;
        boolean asfinal = false;
        int cartaatual;
        String tmao = this.converte();
        if (cartas.get(cartas.size()-1).carta == 1) asfinal=true;
        if (tmao.contains("22") && !tmao.contains("A22")) {
            ci=cartas.get(2).carta;
            cartaatual=ci;
            if (cartaatual==1 && asfinal) cartaatual=14;
            while (cartaatual<cf) {
                this.enviaProFinal(2);
                cartaatual = cartas.get(2).carta;
                if (cartaatual == 1 && asfinal) cartaatual=14;
            }
            while (cartas.get(1).carta != ci) this.enviaProFinal(1);
        }
        else if (tmao.contains("A22") && cf != 3) {
            ci=cartas.get(3).carta;
            cartaatual=ci;
            if (cartaatual==1 && asfinal) cartaatual=14;
            while (cartaatual<cf) {
                this.enviaProFinal(3);
                cartaatual = cartas.get(3).carta;
                if (cartaatual == 1 && asfinal) cartaatual=14;
            }
            while (cartas.get(2).carta != ci) this.enviaProFinal(2);        
        }
        else {
            ci=cartas.get(1).carta;
            cartaatual=ci;
            if (cartaatual==1 && asfinal) cartaatual=14;
            while (cartaatual<cf) {
                this.enviaProFinal(1);
                cartaatual = cartas.get(1).carta;
                if (cartaatual == 1 && asfinal) cartaatual=14;
            }
            while (cartas.get(0).carta != ci) this.enviaProFinal(0);            
        }
    }
    
    public boolean limpa() {
        if (!this.jogoValido().valido) return false;
        String tmao = this.jogoValido().ordem.converte();
        if (this.umSoNaipe(true) && this.cartas.size()>=7) {
            if (this.contaCarta("2")==0) return true;
            else if (this.contaCarta("2")==1) return tmao.contains("23");
            else return false;
        }
        else return false;
    }
    
    public boolean suja(){
        if (!this.jogoValido().valido) return false;
        return cartas.size()>=7 && !this.limpa();
    }        
    
    public boolean real() {
        return this.limpa() && cartas.size()==14;
    }
    
    public boolean semireal() {
        return this.limpa() && cartas.size()==13;
    }
    
    public int somaPontos() {
        int pontos = 0;
        for (ClasseCarta ct:cartas) pontos+=ct.valor;
        return pontos;
    }
    
    public int contaNaoSelecionadas() {
        int k = 0;
        for (ClasseCarta ct:cartas) if (!ct.selecionada) k++;
        return k;
    }

    public int contaSelecionadas() {
        int k = 0;
        for (ClasseCarta ct:cartas) if (ct.selecionada) k++;
        return k;
    }
    
    public SequentialTransition fxMostraTarja(String tipo) {
        if (null != tipo)switch (tipo) {
            case "LIMPA":
                if (!this.tarja.getImage().equals(this.limpa)) this.tarja.setImage(this.limpa);
                break;
            case "SUJA":
                if (!this.tarja.getImage().equals(this.suja)) this.tarja.setImage(this.suja);
                break;
        }
        tarja.setTranslateX(posInitX);
        tarja.setTranslateY(posInitY+100.0*0.6);
        tarja.setViewport(new Rectangle2D(0.0,0.0,1.0,100.0*0.2));
        tarja.setVisible(true);
        tarja.toFront();
        tarja.setMouseTransparent(true);
        final double width = this.largura();
        TranslateTransition st = new TranslateTransition(); //dummy
        st.setNode(tarja);
        st.setDuration(Duration.seconds(TEMPO));
        st.setInterpolator(new Interpolator(){   
            @Override
            protected double curve (double t) {
                tarja.setViewport(new Rectangle2D(0.0,0.0,Math.max(width*t,1.0),100.0*0.2));
                return t;
            }
        });
        
        return new SequentialTransition(st);
    }
    
    public SequentialTransition fxMoveTarja() {
        if (this.tarja.isVisible()){
            TranslateTransition translate = new TranslateTransition(Duration.seconds(TEMPO));
            translate.setNode(this.tarja);
            translate.setToX(this.posInitX);
            translate.setToY(this.posInitY+100.0*0.6);
            TranslateTransition st = new TranslateTransition();
            st.setNode(tarja);
            st.setDuration(Duration.seconds(TEMPO));
            final double w2 = this.largura();
            final double w1 = this.tarja.getViewport().getWidth();
            st.setInterpolator(new Interpolator(){   
                @Override
                protected double curve (double t) {
                    tarja.setViewport(new Rectangle2D(0.0,0.0,Math.max((w2-w1)*t+w1,1.0),100.0*0.2));
                    return t;
                }
            });
            return new SequentialTransition(new ParallelTransition(translate,st));
        }
        else {
            return new SequentialTransition(new PauseTransition(Duration.millis(1)));
        }
    }
        
    public void tiraTarja() {
        tarja.setVisible(false);
    }
    /*
    private double posicaoX() {
        return cartas.get(0).getX();
    }
    private double posicaoY() {
        return cartas.get(0).getY();
    }
    */
    private double getNextX() {
        return this.posInitX + this.deltaX * this.cartas.size();
    }
    private double getNextY() {
        return this.posInitY + this.deltaY * this.cartas.size();
    }
    
    public double getNextPos(String axis) {
        double retorno;
        if (axis.equals("Y")){
            if (this.orientation==HORIZONTAL){
                retorno = posInitY;
            }
            else {
                retorno = this.getNextY();
            }
        }
        else {
            if (this.orientation==VERTICAL){
                retorno = posInitX;
            }
            else {
                retorno = this.getNextX();
            }
        }
        return retorno;
    }
    
    public double largura() {
        double s = this.cartas.size();
        return (s - 1) * this.deltaX + 70;
    }
    
    public void esvazia() {
        this.tiraTarja();
        for (int ct=cartas.size()-1;ct>=0;ct--) {
            this.cartas.get(ct).setVisible(false);
            this.cartas.remove(ct);           
        }
    }

    boolean temJogoValido() {
        for (int np=1;np<=4;np++){
            ClasseMao mtmp = new ClasseMao(); //sub-mão temporária com cartas do mesmo naipe
            for (ClasseCarta c:this.cartas) if (c.naipe==np) mtmp.cartas.add(c);
            if (mtmp.cartas.size()>=3){ //se não tem nem 3 cartas desse naipe, não precisa tentar descer
                //System.out.println("tem pelo menos 3 cartas do mesmo naipe");
                for (int k1=0;k1<mtmp.cartas.size()-2;k1++){
                    for (int k2=k1+1;k2<mtmp.cartas.size()-1;k2++){
                        for (int k3=k2+1;k3<mtmp.cartas.size();k3++){
                            ClasseMao mao_temp = new ClasseMao(); //sub-mão de mtmp com combinação de 3 cartas
                            mao_temp.cartas.add(mtmp.cartas.get(k1));
                            mao_temp.cartas.add(mtmp.cartas.get(k2));
                            mao_temp.cartas.add(mtmp.cartas.get(k3));
                            if (mao_temp.jogoValido().valido) return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    boolean temRepetida(ClasseCarta ct) {
        if (!this.cartas.contains(ct)) return false;
        for (ClasseCarta crt:this.cartas) {
            if (!crt.equals(ct)) {
                if (crt.carta==ct.carta && crt.naipe==ct.naipe) {
                    return true;
                }
            }
        }
        return false;
    }
}