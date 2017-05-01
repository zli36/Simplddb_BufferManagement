package simpledb.file;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import simpledb.server.*;
import simpledb.buffer.*;
import simpledb.file.*;


import org.junit.Test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileMgrTest {
	
	/*@Before
	public void setUp() throws Exception {
		 SimpleDB.init("simpleDB");
		 //SimpleDB.initFileMgr("MyFileMgr");
	}

	@After
	public void tearDown() throws Exception {
	}*/

	@Test
	public void testFilemgr() {
		 System.out.println("test FileMgr start------------------------!");

		/* Block[] blk=new Block[20];
		 for(int i=0;i<20;i++){
			 blk[i] = new Block("filename", i);
		 }*/
		 //When a write occurs, the numofBlockRead increment by 1
		 FileMgr f1 = new FileMgr("MyFileMgr");
		 Page p1 = new Page();
		 Block blk = new Block("junk", 6);
		 ByteBuffer contents = ByteBuffer.allocateDirect(400);
		 f1.write(blk,contents);
		 assertEquals(1, f1.numofBlockWrite);
		 
		 //When a Read occurs, the numofBlockWrite increment by 1
		 
		 f1.read(blk,contents);
		 assertEquals(1, f1.numofBlockRead);

	}

}
