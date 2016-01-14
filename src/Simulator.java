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

			Thread.sleep(2000);
			
			System.out.println("program =\r\n" + Program);
			System.out.println("");
			for (String line : Program.split("\\n")) {
				Thread.sleep(100);
				String[] program = line.split(":");

				System.out.println("Send: " + program[0]);
				sp.writeBytes((program[0] + "\r\n").getBytes() );
				// 

				if (program.length >= 2 && !program[1].equals("*")) {
					System.out.println("Wait for: " + program[1]);
					while (true) {
						byte buffer[] = sp.readBytes();
						System.out.println(new String(buffer));

						
					}
				} else {
					boolean running = true;
					String globalBuffer = null;
					while (running) {
						byte buffer[] = sp.readBytes();

						if (buffer != null && buffer.length > 0) {
							System.out.println(new String(buffer));
							globalBuffer += new String(buffer);
							if (globalBuffer.contains("\r\n")) {
								running = false;
							}
						}

					}
				}

			}
		} catch (

		Exception e)

		{
			e.printStackTrace();
		} finally

		{
			try {
				System.out.println("Port Closed");
				sp.closePort();
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}
		}
	}

}
