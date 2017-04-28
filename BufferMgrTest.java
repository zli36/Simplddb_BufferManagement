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
	public void testPin() {
		 System.out.println("testPin start------------------------!");

		 Block[] blk1=new Block[10];
		 for(int i=0;i<10;i++){
			 blk1[i] = new Block("filename", i);
		 }
		 BufferMgr basicBufferMgr = new SimpleDB().bufferMgr();
		 //initially, available buffers should be 8
		 assertEquals(8, basicBufferMgr.available());

		 for(int i=0;i<8;i++){
			 
			//pin a block to buffer,if buffer is full, it will wait for some time then fail
			try {
			     basicBufferMgr.pin(blk1[i]); //pin a block to buffer
				 assertEquals(8-i-1, basicBufferMgr.available());	
			     }
			catch (BufferAbortException e) {
				 System.out.println(i+" buffer pin fails!");//buffer pool is full
			} 
			
		 }
	     
		 for(int i=7;i<9;i++){
			 
				//pin a block to buffer,if buffer is full, it will wait for some time then fail
				if(basicBufferMgr.containsMapping(blk1[i]))	 System.out.println(i+" buffer map fails!");//buffer pool is full
				
				
			 }
		 
		 
		 System.out.println("testPin end！-------------------------");
		 blk1=null;
	}
/*
	@Test
	public void testUnpin() {
		 System.out.println("testUnpin start------------------------!");
		 
		 //initiallize 10 blocks
		 Block[] blk=new Block[10];
		 Buffer[] buf=new Buffer[10];
		 for(int i=0;i<10;i++){
			 blk[i] = new Block("filename", i);
		 }
		 BufferMgr basicBufferMgr = new SimpleDB().bufferMgr();
		 //initially, available buffers should be 8
		 assertEquals(8, basicBufferMgr.available());
		 
		 //pin
		 for(int i=0;i<9;i++){
				try {
				     buf[i]=basicBufferMgr.pin(blk[i]); //pin a block to buffer
				     }
				catch (BufferAbortException e) {
					 System.out.println(i+" buffer pin fails!");//buffer pool is full
				}
		 }
		 assertEquals(0, basicBufferMgr.available());//since pool is full, available num is 0

		 //unpin
		 for(int i=0;i<2;i++){

			 //before unpin, available buffers should be i
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
		 
		 //after unpin, it can pin again!
			try {
			     basicBufferMgr.pin(blk[9]); //pin a block to buffer
			     }
			catch (BufferAbortException e) {
				 System.out.println("buffer pin fails after unpin!");
			}
			assertEquals(1, basicBufferMgr.available());
		 System.out.println("testUnpin end！-------------------------");
		 blk=null;
		 buf=null;
	}
	*/
	



}
