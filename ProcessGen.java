package dispatchernode;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessGen extends Thread{
    int ID;
    volatile Process GenProcess;
    volatile SharedFlag newGenProcess;

    public ProcessGen(int ID, Process GenProcess, SharedFlag newGenProcess) {
        this.ID=ID;
        this.GenProcess = GenProcess;
        this.newGenProcess=newGenProcess;
    }
    
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    
    public void run(){
        Process temp;
       while(true){
           if(!newGenProcess.isFlag()){
               temp=new Process(ID,getRandomNumber(100,200),getRandomNumber(2,10));
               GenProcess.setONode(temp.getONode());
               GenProcess.setPID(temp.getPID());
               GenProcess.setTime(temp.getTime());
               //System.out.println("Proceso: "+GenProcess.getPID()+" generado "+GenProcess.getTime()+" Nodo de origen: "+GenProcess.getONode());
               newGenProcess.setFlag(true);
           }
           try {
               sleep(2000);
           } catch (InterruptedException ex) {
               Logger.getLogger(ProcessGen.class.getName()).log(Level.SEVERE, null, ex);
           }
           
       } 
    }
}
