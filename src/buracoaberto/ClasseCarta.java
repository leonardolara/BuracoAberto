/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buracoaberto;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 *
 * @author Leonardo Lara Rodrigues
 */
public class ClasseCarta extends ImageView {
    public final static double TEMPO = 1.0;
    public final static int JOG1 = 0, JOG2 = 1, JOG3 = 2, JOG4 = 3;
    public int carta;
    public int naipe;
    public int cor;
    public int valor;
    //public int local;
    public boolean selecionada;
    public double angulo;
    public double posX;
    public double posY;
    public Image frente;
    public Image verso;
    public int zOrder;
    public int descarte;

    public int ordemNaipe(){
        return naipe*100+carta*3+cor;
    }
    
    public void zeraDescarte () {
        this.descarte = 0;
    }
    
    public void defineAngulo (int j){
        if (j==JOG2){
            if (this.angulo==0){
                this.angulo=-90;
            }
        }
        else if (j==JOG3){
            if (this.angulo==0){
                this.angulo=180;
            }
        }
        else if (j==JOG4){
            if (this.angulo==0){
                this.angulo=90;
            }
        }
        else {
            if (this.angulo!=0){
                this.angulo=0;
            }
        }
    }
    
    public int ordemCanastra (ClasseMao ordem){
        if (ordem!=null){
            for (int k=0;k<ordem.cartas.size();k++){
                if ((this.carta==ordem.cartas.get(k).carta)&&(this.naipe==ordem.cartas.get(k).naipe)&&(this.cor==ordem.cartas.get(k).cor)){
                    return k;
                }
            }
        }
        return -1;
    }
    
    public SequentialTransition fxToggleSelect() {
        this.selecionada = !this.selecionada;
        if (this.selecionada){
            posY = posY - Math.rint(10.0 * Math.cos(Math.toRadians(this.angulo)));
            posX = posX + Math.rint(10.0 * Math.sin(Math.toRadians(this.angulo)));
        } else {
            posY = posY + Math.rint(10.0 * Math.cos(Math.toRadians(this.angulo)));
            posX = posX - Math.rint(10.0 * Math.sin(Math.toRadians(this.angulo)));
        }
        /*
        if (this.angulo == 0){
            if (this.selecionada){
                posY = posY - 10.0;
            }
            else {
                posY = posY + 10.0;
            }
        }
        if (this.angulo == 90){
            if (this.selecionada){
                posX = posX + 10.0;
            }
            else {
                posX = posX - 10.0;
            }
        }
        if (this.angulo == 180){
            if (this.selecionada){
                posY = posY + 10.0;
            }
            else {
                posY = posY - 10.0;
            }
        }
        if (this.angulo == 270){
            if (this.selecionada){
                posX = posX - 10.0;
            }
            else {
                posX = posX + 10.0;
            }
        }
        */
        TranslateTransition translate = new TranslateTransition(Duration.millis(20));
        translate.setToY(posY);
        translate.setToX(posX);
        SequentialTransition seqtransition = new SequentialTransition(this,translate);
        return seqtransition;
    }
    public SequentialTransition fxViraPraBaixo () {
        return fxViraPraBaixo(Duration.ZERO);
    }
    public SequentialTransition fxViraPraBaixo (Duration t) {
        if (t==Duration.ZERO) t=Duration.seconds(TEMPO);
        if (this.getImage().equals(frente)) {
            KeyFrame k1b, k2a, k2b, k3b;
            k1b = new KeyFrame(t.multiply(0.2),new KeyValue(this.scaleXProperty(),1.0));
            k2a = new KeyFrame(t.multiply(0.35),new KeyValue(this.imageProperty(),verso));
            k2b = new KeyFrame(t.multiply(0.35),new KeyValue(this.scaleXProperty(),0.0));
            k3b = new KeyFrame(t.multiply(0.6),new KeyValue(this.scaleXProperty(),1.0));
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().addAll(k1b, k2a, k2b, k3b);
            return new SequentialTransition(new PauseTransition(Duration.millis(1)),timeline);
        }
        else {
            return new SequentialTransition(new PauseTransition(Duration.millis(1)));
        }
    }
    
    public SequentialTransition fxViraPraCima () {
        return fxViraPraCima(Duration.ZERO);
    }
    public SequentialTransition fxViraPraCima (Duration t) {
        if (t==Duration.ZERO) t=Duration.seconds(TEMPO);
        if (this.getImage().equals(verso)) {
            KeyFrame k1b, k2a, k2b, k3b;
            k1b = new KeyFrame(t.multiply(0.2),new KeyValue(this.scaleXProperty(),1.0));
            k2a = new KeyFrame(t.multiply(0.35),new KeyValue(this.imageProperty(),frente));
            k2b = new KeyFrame(t.multiply(0.35),new KeyValue(this.scaleXProperty(),0.0));
            k3b = new KeyFrame(t.multiply(0.6),new KeyValue(this.scaleXProperty(),1.0));
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().addAll(k1b, k2a, k2b, k3b);
            return new SequentialTransition(new PauseTransition(Duration.millis(1)),timeline);
        }
        else {
            return new SequentialTransition(new PauseTransition(Duration.millis(1)));
        }
    }
}
