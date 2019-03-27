package moe.feo.NetConsole;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;

public class Printer {

	private static Printer printer = new Printer();

	public Terminal terminal;
	public LineReader linereader;
	public PrintWriter writer;

	private Printer() {
		this.terminal = CLI.getInstance().terminal;
		this.linereader = CLI.getInstance().linereader;
		this.writer = this.terminal.writer();
	}

	public static Printer getInstance() {
		return printer;
	}

	synchronized public void printInfo(String str) {
		SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		String message = new String("[" + ft.format(date) + " INFO" + "]: " + str);
		try {
			linereader.callWidget(LineReader.CLEAR);
			writer.println(message);
			linereader.callWidget(LineReader.REDRAW_LINE);
			linereader.callWidget(LineReader.REDISPLAY);
		} catch (IllegalStateException e) { // callWidget只能在linereader阻塞的时候用
			writer.println(message);
		}
	}

	synchronized public void printWarn(String str) {
		SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		String message = new String("[" + ft.format(date) + " WARN" + "]: " + str);
		try {
			linereader.callWidget(LineReader.CLEAR);
			writer.println(message);
			linereader.callWidget(LineReader.REDRAW_LINE);
			linereader.callWidget(LineReader.REDISPLAY);
		} catch (IllegalStateException e) {
			writer.println(message);
		}
	}

	synchronized public void printError(String str) {
		SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		String message = new String("[" + ft.format(date) + " ERROR" + "]: " + str);
		try {
			linereader.callWidget(LineReader.CLEAR);
			writer.println(message);
			linereader.callWidget(LineReader.REDRAW_LINE);
			linereader.callWidget(LineReader.REDISPLAY);
		} catch (IllegalStateException e) {
			writer.println(message);
		}
	}

}
