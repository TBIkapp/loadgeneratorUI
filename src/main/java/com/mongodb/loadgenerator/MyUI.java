package com.mongodb.loadgenerator;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.annotation.WebServlet;

import org.apache.commons.cli.ParseException;

import com.johnlpage.pocdriver.LoadRunner;
import com.johnlpage.pocdriver.POCTestOptions;
import com.johnlpage.pocdriver.POCTestResults;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 * This UI is the application entry point. A UI may either represent a browser window (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be overridden to add component to the user interface and initialize non-component
 * functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7203412013399393954L;
	POCTestOptions testOpts = null;
	POCTestResults testResults = new POCTestResults();
	MyUIData layout;
	Thread t;

	void startThread() {
		t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					testOpts = new POCTestOptions(new String[] { layout.getLblvalue() });
					LoadRunner runner = new LoadRunner(testOpts);
					runner.RunLoad(testOpts, testResults);
				} catch (ParseException e) {
					e.printStackTrace();
				}

			}
		});
		t.start();
	}

	void stopThread() {
		testOpts.setDuration(0);
		System.out.println("=== Stopping Driver ===");
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		layout = new MyUIData(this);
		setUpEventHandling();
		setContent(layout);

//		TimerTask tt = new TimerTask() {
//
//			@Override
//			public void run() {
//				layout.getTxtArea().requestRepaint();
//			}
//		};
//		Timer t = new Timer(true);
//		t.scheduleAtFixedRate(tt, 0, 9000);
	}

	private void setUpEventHandling() {
		System.setOut(new PrintStreamCapturer(layout.getTxtArea(), System.out));
		System.setErr(new PrintStreamCapturer(layout.getTxtArea(), System.err, "[ERROR] "));
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5266887089687922749L;
	}
}
