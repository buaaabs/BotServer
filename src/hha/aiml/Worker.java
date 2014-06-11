package hha.aiml;

public class Worker implements Runnable {
	
	public Worker(SyncQueue _queue) {
	       queue = _queue;
	    }
	@Override
	public void run() {
       while (true) {
    	   EchoSession task = null;
			try {
				task = (EchoSession) queue.get();
			} catch (ExceptionAdapter e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (task!= null)
				try {
					task.run();
				} catch (ExceptionAdapter e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	       }
    }
   
    protected SyncQueue queue = null;

}
