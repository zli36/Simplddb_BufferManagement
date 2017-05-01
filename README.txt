GROUP MEMBERS:
Name                                        Unity ID
Haoyu Li                                     hli36
Zhuo Li                                      zli36
Lu Zhiren                                    nzlu
Chen Zhao                                    czhao13


TASK1:
Modification:
FileMgr.java: 1. Add a static variable numofBlockRead.
              2. Add a static variable numofBlockWrite.
              3. In method read(), numofBlockRead++.
              4. In method write(), numofBlockWrite++.
              5. Add a static method getFileStatistics() to print numofBlockRead and numofBlock Write.
RemoteConnectionlmpl.java: 1. In method commit(), call method getFileStatistics().
                           2. In method rollback(), call method getFileStatistics().

ADD File:
FileMgrTest.java:  Unit test for FileMgr.java


How to integrate with SimpleDB: Replace the FileMgr.java in simoledb.file package. Replace the RemoteConnectionlmpl.java in the simpledb.remote package with ours. Add FileMgrTest.java into the simpledb.file package

How to run Unit test:  Run FileMgrTest.java.



TASK2: Buffer Management
1.	Use a Map data structure to keep track of the buffer pool
Keep a Map of allocated buffers, keyed on the block they contain.  (A buffer is allocated when its contents is not null, and may be pinned or unpinned.  A buffer starts out unallocated; it becomes allocated when it is first assigned to a block, and stays allocated forever after.)  Use this map to determine if a block is currently in a buffer.  When a buffer is replaced, you must change the map -- The mapping for the old block must be removed, and the mapping for the new block must be added.  For our convenience, we will be using “bufferPoolMap” as the name of the Map. 

2.	Modify the buffer replacement policy to the Most Recently Modified (MRM)
This suggests a page replacement strategy that chooses the modified page with the lowest LSN. Implement this strategy.

For Task2.1 & 2.2 main changes are listed below.
In Buffer.java, we give two interfaces of logSequenceNumber.

Buffer.java
/**
    * Set a specific LSN to a buffer
    * @param lsn
    */
	public void setLSN(int lsn){
	   if(lsn >= 0)
		   logSequenceNumber = lsn;
   }
   
   
   /**
       * Get the logsequenceNumber for a specific buffer
       * @return the number of logSequenceNumber
       */
      public int getLSN(){
   	   return logSequenceNumber;
      }

In BasicBufferMgr.java, we use an hashmap to store the current block in bufferpool. Meanwhile, we implement a priorityQueue to choose the modified page with the lowest LSN.
BasicBufferMgr.java 


   /**
    * initial a HashMap and priorityQueue to store the buffer data
    */
   
   private HashMap<Block,Buffer> bufferPoolMap = new HashMap<Block,Buffer>();
   private PriorityQueue<Buffer> bufferQueue;



      bufferQueue= new PriorityQueue<Buffer>(bufferpool.length,new Comparator<Buffer>(){
		   public int compare(Buffer b1, Buffer b2){
			   return b1.getLSN() - b2.getLSN();
		   }
		   
		   
In task2, we can only chooseunpinned page as modified page, otherwise the init() function in database will throw BufferAbortException error. So there is a option. 

   private Buffer chooseUnpinnedBuffer() {
	   
	   
        for (Buffer buff : bufferpool)
         if (!buff.isPinned())
         {
        	 //bufferPoolMap.remove(buff.block());
        	 //System.out.println(buff.block());
        	 //return buff;
        	bufferQueue.offer(buff); 
         }
//        if(buff.CurModifiedBy()){
//        	bufferQueue.offer(buff);
//        }
        
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
		   
And also, we add some interfaces for junit test regard to the hashmap.
/**  
* Determines whether the map has a mapping from  
* the block to some buffer.  
* @paramblk the block to use as a key  
* @return true if there is a mapping; false otherwise  
*/  
boolean containsMapping(Block blk) {  
return bufferPoolMap.containsKey(blk);  
} 
/**  
* Returns the buffer that the map maps the specified block to.  
* @paramblk the block to use as a key  
* @return the buffer mapped to if there is a mapping; null otherwise  
*/  
Buffer getMapping(Block blk) {  
return bufferPoolMap.get(blk);  
} 

BufferMgr.java 
/**  
* Determines whether the map has a mapping from  
* the block to some buffer.  
* @paramblk the block to use as a key  
* @return true if there is a mapping; false otherwise  
*/  
public boolean  containsMapping(Block blk) {  
return bufferMgr.containsMapping(blk);  
} 
/**  
* Returns the buffer that the map maps the specified block to.  
* @paramblk the block to use as a key  
* @return the buffer mapped to if there is a mapping; null otherwise  
*/  
public Buffer getMapping(Block blk) {  
returnbufferMgr.getMapping(blk);  
}  

Modification:
Buffer.java: 1. Add methods setLSN(),getLSN(), CurModifiedBy().

BasicBufferMgr.java:1. Add a hashmap and a priorityQueue;		
                    2. Add methods containsMapping and getMapping.
					3. Modified funcution pin,pinNew,chooseUnpinnedBuff.
					
					
BufferMgr.java:1. Add methods containsMapping,getMapping.



ADD File:
BufferMgrTest.java:  Unit test for Task2

How to integrate with SimpleDB: Replace the Buffer.java,BasicBufferMgr.java and BufferMgr.java with ours. Add BufferMgrTest.java into the simpledb.file package

How to run Unit test:  Run BufferMgrTest.java.


   
3.	Buffer Manager Statistics: 
Extend the buffer manager to return useful statistics information like number of times buffers are read and written. Add a new method(s) getBufferStatistics() for this purpose. You may look at the description in Task 1 to see how to set up the testing of the code.

Modification: 

Buffer.java: 1.add 2 static variables: read_num(to record buffer read number) and write_num(to record buffer write number).
             2.add method re_read(), re_write() to return the value of read_num and write_num.  
			 3.add method clear_read(), clear_write() to set read_num and write_num to 0.
			 
BasicBufferMgr.java: add method set_buffer() to set ModifiedBy to 1 (just for test use).

BufferMgr.java: 1.add method set_buffer_main() to execute set_buffer().
                2.add method getBufferStatistics() to output read_num and write_num. 
				 
ADD file: 
BufferMgrTest.java: Unit test for Task3

How to integrate with SimpleDB: Replace the Buffer.java,BasicBufferMgr.java and BufferMgr.java with ours. Add BufferMgrTest.java into the simpledb.file package

How to run Unit test:  Run BufferMgrTest.java.



