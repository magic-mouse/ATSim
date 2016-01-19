import jssc.SerialPort;
import jssc.SerialPortException;

public class Simulator implements Runnable {

	protected String Com;
	protected String Program;
	private SerialPort sp;

	public Simulator() {

	}

	@Override
	public void run() {

		parseProgram();

	}

	private void parseProgram() {
		try {

			sp = new SerialPort(Com);
			sp.openPort();
			sp.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			Thread.sleep(500);

			System.out.println("program =\r\n" + Program);
			System.out.println("");
			for (String line : Program.split("\\n")) {

				String program = line.trim();

				if (program.isEmpty()){continue;}
				if (program.startsWith("//")){continue;}

				System.out.println("Send: " + program);
				sp.writeBytes((program + "\r\n").getBytes());
				
				
				boolean running = true;
				int loopCounter = 0;
				while (running) {
					if(sp == null)
					{
						throw new Exception("Serial port has exited prematurely");
					}
					
					
					byte[] byteBuffer = sp.readBytes(); 
					if(byteBuffer != null  && byteBuffer.length > 0 ){
						String bufferString = new String(byteBuffer);
						//if (!bufferString.isEmpty()) {
							if(loopCounter != 0){
									System.out.println("");
									loopCounter = 0;		
							}
							
							System.out.println(bufferString.replaceAll("\r\n\r\n", "\r\n"));
						/*} else {
							System.out.println("Internally Sleeping 100 ms");
							Thread.sleep(100);
							loopCounter++;
							if (loopCounter == 10) {
								running = false;
							}
						}*/
					}else{
						System.out.print(".");
						Thread.sleep(100);
						loopCounter++;
						if (loopCounter == 50) {
							System.out.println("");
							running = false;
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally

		{
			try {
				System.out.println("Port Closed");
				sp.closePort();
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}
	}

}
