package com.mongodb.loadgenerator;

import java.io.PrintStream;

import com.vaadin.ui.TextArea;

public class PrintStreamCapturer extends PrintStream {

	private TextArea text;
	private boolean atLineStart;
	private String indent;

	public PrintStreamCapturer(TextArea textArea, PrintStream capturedStream, String indent) {
		super(capturedStream);
		this.text = textArea;
		this.indent = indent;
		this.atLineStart = true;
	}

	public PrintStreamCapturer(TextArea textArea, PrintStream capturedStream) {
		this(textArea, capturedStream, "");
	}

	private void writeToTextArea(String str) {
		if (text != null) {
			synchronized (text) {
				text.setCursorPosition(text.getValue().length());
				text.setValue(text.getValue() + str);
				text.requestRepaint();
			}
		}
	}

	private void write(String str) {
		String[] s = str.split("\n", -1);
		if (s.length == 0)
			return;
		for (int i = 0; i < s.length - 1; i++) {
			writeWithPotentialIndent(s[i]);
			writeWithPotentialIndent("\n");
			atLineStart = true;
		}
		String last = s[s.length - 1];
		if (!last.equals("")) {
			writeWithPotentialIndent(last);
		}
		text.requestRepaint();
	}

	private void writeWithPotentialIndent(String s) {
		if (atLineStart) {
			writeToTextArea(indent + s);
			atLineStart = false;
		} else {
			writeToTextArea(s);
		}
	}

	private void newLine() {
		write("\n");
		text.requestRepaint();
	}

	@Override
	public void print(boolean b) {
		synchronized (this) {
			super.print(b);
			write(String.valueOf(b));
		}
		text.requestRepaint();
	}

	@Override
	public void print(char c) {
		synchronized (this) {
			super.print(c);
			write(String.valueOf(c));
		}
		text.requestRepaint();
	}

	@Override
	public void print(char[] s) {
		synchronized (this) {
			super.print(s);
			write(String.valueOf(s));
		}
		text.requestRepaint();
	}

	@Override
	public void print(double d) {
		synchronized (this) {
			super.print(d);
			write(String.valueOf(d));
		}
		text.requestRepaint();
	}

	@Override
	public void print(float f) {
		synchronized (this) {
			super.print(f);
			write(String.valueOf(f));
		}
		text.requestRepaint();
	}

	@Override
	public void print(int i) {
		synchronized (this) {
			super.print(i);
			write(String.valueOf(i));
		}
		text.requestRepaint();
	}

	@Override
	public void print(long l) {
		synchronized (this) {
			super.print(l);
			write(String.valueOf(l));
		}
		text.requestRepaint();
	}

	@Override
	public void print(Object o) {
		synchronized (this) {
			super.print(o);
			write(String.valueOf(o));
		}
		text.requestRepaint();
	}

	@Override
	public void print(String s) {
		synchronized (this) {
			super.print(s);
			if (s == null) {
				write("null");
			} else {
				write(s);
			}
		}
		text.requestRepaint();
	}

	@Override
	public void println() {
		synchronized (this) {
			newLine();
			super.println();
		}
		text.requestRepaint();
	}

	@Override
	public void println(boolean x) {
		synchronized (this) {
			print(x);
			newLine();
			super.println();
		}
		text.requestRepaint();
	}

	@Override
	public void println(char x) {
		synchronized (this) {
			print(x);
			newLine();
			super.println();
		}
		text.requestRepaint();
	}

	@Override
	public void println(int x) {
		synchronized (this) {
			print(x);
			newLine();
			super.println();
		}
		text.requestRepaint();
	}

	@Override
	public void println(long x) {
		synchronized (this) {
			print(x);
			newLine();
			super.println();
		}
		text.requestRepaint();
	}

	@Override
	public void println(float x) {
		synchronized (this) {
			print(x);
			newLine();
			super.println();
		}
		text.requestRepaint();
	}

	@Override
	public void println(double x) {
		synchronized (this) {
			print(x);
			newLine();
			super.println();
		}
		text.requestRepaint();
	}

	@Override
	public void println(char x[]) {
		synchronized (this) {
			print(x);
			newLine();
			super.println();
		}
		text.requestRepaint();
	}

	@Override
	public void println(String x) {
		synchronized (this) {
			print(x);
			newLine();
			super.println();
		}
		text.requestRepaint();
	}

	@Override
	public void println(Object x) {
		String s = String.valueOf(x);
		synchronized (this) {
			print(s);
			newLine();
			super.println();
		}
		text.requestRepaint();
	}
}