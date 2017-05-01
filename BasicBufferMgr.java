package simpledb.buffer;

import simpledb.file.*;
import java.util.HashMap;
import java.util.Comparator;
import java.util.PriorityQueue;
/**
 * Manages the pinning and unpinning of buffers to blocks.
 * @author Edward Sciore
 *
 */
class BasicBufferMgr {
   private Buffer[] bufferpool;
   private int numAvailable;
   
   
   
   
   private HashMap<Block,Buffer> bufferPoolMap = new HashMap<Block,Buffer>();
   private PriorityQueue<Buffer> bufferQueue;
   //private int lsncount = 1;
   /**
    * Creates a buffer manager having the specified number 
    * of buffer slots.
    * This constructor depends on both the {@link FileMgr} and
    * {@link simpledb.log.LogMgr LogMgr} objects 
    * that it gets from the class
    * {@link simpledb.server.SimpleDB}.
    * Those objects are created during system initialization.
    * Thus this constructor cannot be called until 
    * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or
    * is called first.
    * @param numbuffs the number of buffer slots to allocate
    */
   BasicBufferMgr(int numbuffs) {
      bufferpool = new Buffer[numbuffs];
      numAvailable = numbuffs;
      for (int i=0; i<numbuffs; i++)
         bufferpool[i] = new Buffer();
      
      
      
      
      bufferQueue= new PriorityQueue<Buffer>(bufferpool.length,new Comparator<Buffer>(){
		   public int compare(Buffer b1, Buffer b2){
			   return b1.getLSN() - b2.getLSN();
		   }
  });
   }
   
   /**
    * Flushes the dirty buffers modified by the specified transaction.
    * @param txnum the transaction's id number
    */
   synchronized void flushAll(int txnum) {
      for (Buffer buff : bufferpool)
         if (buff.isModifiedBy(txnum))
         buff.flush();
   }
   public void set_buffer(){
	   for(Buffer buff: bufferpool){
             buff.setInt(1,1,1,1);		   
	   }
   }
   /**
    * Pins a buffer to the specified block. 
    * If there is already a buffer assigned to that block
    * then that buffer is used;  
    * otherwise, an unpinned buffer from the pool is chosen.
    * Returns a null value if there are no available buffers.
    * @param blk a reference to a disk block
    * @return the pinned buffer
    */
   synchronized Buffer pin(Block blk) {
      Buffer buff = findExistingBuffer(blk);
      if (buff == null) {
         buff = chooseUnpinnedBuffer();
         if (buff == null)
            return null;
         buff.assignToBlock(blk);
       //  buff.setLSN(lsncount++);
      // bufferQueue.add(buff);
         bufferPoolMap.put(blk, buff);
      }
      if (!buff.isPinned())
         numAvailable--;
      buff.pin();
      return buff;
   }
   
   /**
    * Allocates a new block in the specified file, and
    * pins a buffer to it. 
    * Returns null (without allocating the block) if 
    * there are no available buffers.
    * @param filename the name of the file
    * @param fmtr a pageformatter object, used to format the new block
    * @return the pinned buffer
    */
   synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
      Buffer buff = chooseUnpinnedBuffer();
      if (buff == null)
         return null;
      buff.assignToNew(filename, fmtr);
     // buff.setLSN(lsncount++);
      bufferPoolMap.put(buff.block(), buff);
     // bufferQueue.add(buff);
      numAvailable--;
      buff.pin();
      return buff;
   }
   
   /**
    * Unpins the specified buffer.
    * @param buff the buffer to be unpinned
    */
   synchronized void unpin(Buffer buff) {
      buff.unpin();
      if (!buff.isPinned())
      {
    	  //bufferQueue.offer(buff);
    	  numAvailable++;
      }
   }
   
   /**
    * Returns the number of available (i.e. unpinned) buffers.
    * @return the number of available buffers
    */
   int available() {
      return numAvailable;
   }
   
   private Buffer findExistingBuffer(Block blk) {
      for (Buffer buff : bufferpool) {
         Block b = buff.block();
         if (b != null && b.equals(blk))
            return buff;
      }
      return null;
   }
   
   private Buffer chooseUnpinnedBuffer() {
	   
	   
        for (Buffer buff : bufferpool)
         if (!buff.isPinned())
         {
        	 //bufferPoolMap.remove(buff.block());
        	 //System.out.println(buff.block());
        	 //return buff;
        	bufferQueue.offer(buff); 
         }
     
        
        while(bufferQueue.peek() != null){
	    	  Buffer tmp = bufferQueue.poll();
	    	  //System.out.println(tmp.block());
	    	  if(!tmp.isPinned()){
	    		  bufferPoolMap.remove(tmp.block());
	    		  bufferQueue.clear();
	    		  return tmp;
	    	  }
	      }
      
      
      return null;
   }
   
   public boolean containsMapping(Block blk){
	   return bufferPoolMap.containsKey(blk);
   }
   
   public Buffer getMapping(Block blk){
	   return bufferPoolMap.get(blk);
   }
}