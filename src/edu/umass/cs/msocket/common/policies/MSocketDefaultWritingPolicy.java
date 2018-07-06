package edu.umass.cs.msocket.common.policies;

import java.io.IOException;

import edu.umass.cs.msocket.ConnectionInfo;

public class MSocketDefaultWritingPolicy  extends MultipathWritingPolicy 
{
	public MSocketDefaultWritingPolicy(ConnectionInfo cinfo)
	{
		this.cinfo = cinfo;
	}
	
	@Override
	public void writeAccordingToPolicy(byte[] b, int offset, int length, int MesgType) throws IOException 
	{
		
	}
}