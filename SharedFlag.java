package dispatchernode;

public class SharedFlag {
    private volatile boolean flag;
    public SharedFlag(){
        flag=false;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    
}
