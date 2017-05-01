package simpledb.buffer;

import simpledb.server.*;
import simpledb.buffer.*;
import simpledb.file.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class BufferMgrTest {

	@Before
	public void setUp() throws Exception {
		 SimpleDB.init("simpleDB");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_read_num() {
		 System.out.println("test number of buffer read start");
        
		 Block[] blk=new Block[8];
		 Buffer[] buf = new Buffer[8];
		 for(int i=0;i<8;i++){
			 blk[i] = new Block("filename", i);
		 }
		 BufferMgr basicBufferMgr = new SimpleDB().bufferMgr();
		 //initially, available buffers should be 8
		 assertEquals(8, basicBufferMgr.available());
		 Buffer.clear_read();
		 for(int i=0;i<8;i++){
			 
			//pin a block to buffer,if buffer is full, it will wait for some time then fail
			try {
			     buf[i] = basicBufferMgr.pin(blk[i]); //pin a block to buffer	
			     }
			catch (BufferAbortException e) {
				 System.out.println(i+" buffer pin fails!");//buffer pool is full
			} 
			
		 }
		 assertEquals(8, Buffer.re_read());
		 System.out.println("test read number of buffer success!");
	}
	
	@Test
	public void test_write_num() {
		 System.out.println("test number of buffer read start");
       
		 Block[] blk=new Block[8];
		 Buffer[] buf = new Buffer[8];
		 for(int i=0;i<8;i++){
			 blk[i] = new Block("filename", i);
		 }
		 BufferMgr basicBufferMgr = new SimpleDB().bufferMgr();
		 //initially, available buffers should be 8
		 assertEquals(8, basicBufferMgr.available());
		 Buffer.clear_write();
		 //basicBufferMgr.set_buffer_main();
		 //basicBufferMgr.flushAll(1);
		 for(int i=0;i<8;i++){
			 
			//pin a block to buffer,if buffer is full, it will wait for some time then fail
			try {
			     buf[i] = basicBufferMgr.pin(blk[i]); //pin a block to buffer	
			     }
			catch (BufferAbortException e) {
				 System.out.println(i+" buffer pin fails!");//buffer pool is full
			} 
			
		 }
		 basicBufferMgr.set_buffer_main();
		 basicBufferMgr.flushAll(1);
		 assertEquals(8, Buffer.re_write());
		 System.out.println("test write number of buffer success!");
	}
	@Test
	public void testMap_LSN() {
		 System.out.println("test Map&LSN start------------------------!");

		 Block[] blk=new Block[20];
		 Buffer[] buf = new Buffer[10];
		 for(int i=0;i<20;i++){
			 blk[i] = new Block("filename", i);
		 }
		 BufferMgr basicBufferMgr = new SimpleDB().bufferMgr();
		 //initially, available buffers should be 8
		 assertEquals(8, basicBufferMgr.available());

		 for(int i=0;i<8;i++){
			 
			//pin a block to buffer,if buffer is full, it will wait for some time then fail
			try {
			     buf[i] = basicBufferMgr.pin(blk[i]); //pin a block to buffer
			     buf[i].setLSN(8-i);
				 assertEquals(8-i-1, basicBufferMgr.available());	
			     }
			catch (BufferAbortException e) {
				 System.out.println(i+" buffer pin fails!");//buffer pool is full
			} 
			
		 }
	     
		 
		 for(int i=0;i<8;i++){
			 
			 assertEquals(i, basicBufferMgr.available());
				try {
				     basicBufferMgr.unpin(buf[i]); //unpin a buffer
				     }
				catch (BufferAbortException e) {
					 System.out.println(i+" buffer unpin fails!");
				}
			 //after pin, the available buffer is increased by 1
			 assertEquals(i+1, basicBufferMgr.available());
				
			 }
		 
		 
		 for(int i = 10; i < 15; i++){
			 try {
			     basicBufferMgr.pin(blk[i]); //pin a block to buffer
			     Buffer tmp = basicBufferMgr.getMapping(blk[i]);
			     tmp.setLSN(15-i);
				 assertEquals(18- i - 1, basicBufferMgr.available());	
			     }
			catch (BufferAbortException e) {
				 System.out.println(i+" buffer pin fails!");//buffer pool is full
			} 
		 }
		 
		 for(int i=0;i<15;i++){
			 
				//pin a block to buffer,if buffer is full, it will wait for some time then fail
			 if(i >=3 && i < 10)
			 	assertFalse(basicBufferMgr.containsMapping(blk[i]));
			 else
				assertTrue(basicBufferMgr.containsMapping(blk[i])); 
				//if(!basicBufferMgr.containsMapping(blk[i]))	 System.out.println(i+" buffer map fails!");//buffer pool is full
				
				
			 }
		 
		 for(int i = 15; i < 20; i++){
			 try {
			     basicBufferMgr.pin(blk[i]); //pin a block to buffer
			     Buffer tmp = basicBufferMgr.getMapping(blk[i]);
			     tmp.setLSN(20-i);
				 //assertEquals(18- i - 1, basicBufferMgr.available());	
			     }
			catch (BufferAbortException e) {
				 System.out.println(i+" buffer pin fails!");//buffer pool is full
			} 
		 }
		 System.out.println("test Map&LSN end!-------------------------");
		 blk=null;
	}

}