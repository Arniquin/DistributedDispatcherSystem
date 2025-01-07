package dispatchernode;

public class Process {
    private volatile int ONode;
    private volatile int PID;
    private volatile int Time;

    public Process(){
        this.ONode=0;
        this.PID=0;
        this.Time=0;
    }
    
    public Process(int ONode, int PID, int Time) {
        this.ONode = ONode;
        this.PID = PID;
        this.Time = Time;
    }

    public int getONode() {
        return ONode;
    }

    public void setONode(int ONode) {
        this.ONode = ONode;
    }

    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public int getTime() {
        return Time;
    }

    public void setTime(int Time) {
        this.Time = Time;
    }
    
    
    
}
