package hha.aiml;

public class SyncQueue {

	 public SyncQueue(int _size) {
	       array = new Object[_size];
	       size = _size;
	       oldest = 0;
	       next = 0;
	    }

	 public synchronized void put(Object o) throws ExceptionAdapter {
	       while (full()) {
	           try {
	              wait();
	           } catch (InterruptedException ex) {
	              throw new ExceptionAdapter(ex);
	           }
	       }
	       array[next] = o;
	       next = (next + 1) % size;
	       notify();
	    }
	   
	    public synchronized Object get() throws ExceptionAdapter {
	       while (empty()) {
	           try {
	              wait();
	           } catch (InterruptedException ex) {
	              throw new ExceptionAdapter(ex);
	           }
	       }
	       Object ret = array[oldest];
	       oldest = (oldest + 1) % size;
	       notify();
	       return ret;
	    }
	   
	    protected boolean empty() {
	       return next == oldest;
	    }
	   
	    protected boolean full() {
	       return (next + 1) % size == oldest;
	    }
	
	 protected Object [] array;
	 protected int next;
	 protected int oldest;
	 protected int size;

}
