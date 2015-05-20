/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buracoaberto;
import java.util.ArrayList;
import javafx.geometry.Point2D;
/**
 *
 * @author Leonardo Lara Rodrigues
 */
public class ClasseMesa {
    ArrayList<ClasseMao> maos;
    public double posInitX, posInitY, deltaX, deltaY;
    public double nextPosX, nextPosY, posMax;
    public boolean pegouMorto, bateu;
    public ClasseMesa (){
        this.posInitX=0.0;
        this.posInitY=0.0;
        this.deltaX=0.0;
        this.deltaY=0.0;
        this.nextPosX=0.0;
        this.nextPosY=0.0;
        this.posMax=0.0;
        this.pegouMorto=false;
        this.bateu=false;
        this.maos = new ArrayList<>();
    }
    
    public void setPosInit (double pX, double pY, double dX, double dY) {
        this.posInitX = pX;
        this.posInitY = pY;
        this.deltaX = dX;
        this.deltaY = dY;
    }
    
    public Point2D posMao (int mao_mesa, double lgCarta) {
        Point2D pos = new Point2D(this.posInitX, this.posInitY);
        if ((mao_mesa >= this.maos.size())
            || (mao_mesa == 0)
            || this.maos.isEmpty()) {
            return pos;
        }
        double espacoH = lgCarta * (2.0/7.0); //espaço horizontal entre os jogos da mesa
        double altura = lgCarta * (10.0/7.0);
        double espacoV = altura/10.0; //espaço vertical entre os jogos da mesa
        pos = new Point2D(
            this.maos.get(mao_mesa-1).posInitX
                + this.maos.get(mao_mesa-1).largura(lgCarta)
                + espacoH,
            this.maos.get(mao_mesa-1).posInitY); //posição da mão anterior + largura + espaço = posição da mão atual
        if (pos.getX() + this.maos.get(mao_mesa).largura(lgCarta) > this.posMax){ // se passar do limite, ajusta
            pos = new Point2D(this.posInitX, pos.getY() + altura + espacoV);
        }
        return pos;
    }
   
    public int somaPontos() {
        int pontos = 0;
        for (ClasseMao m:this.maos){
            if (m.real()) pontos += 1000;
            else if (m.semireal()) pontos += 500;
            else if (m.limpa()) pontos += 200;
            else if (m.suja()) pontos += 100;
            pontos += m.somaPontos();
        }
        if (!this.pegouMorto) pontos -= 100;
        if (this.bateu) pontos += 100;
        return pontos;
    }
    
    public boolean temLimpa() {
        boolean tem = false;
        for (ClasseMao m:this.maos){
            if (m.limpa()) return true;
        }
        return tem;
    }
    
    public void esvazia() {
        for (int m=0;m<this.maos.size();m++){
            this.maos.get(m).esvazia();
            this.maos.remove(m);
        }
    }
}
