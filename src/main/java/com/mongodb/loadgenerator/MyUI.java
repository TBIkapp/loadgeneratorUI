package com.mongodb.loadgenerator;

import javax.servlet.annotation.WebServlet;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import com.johnlpage.pocdriver.POCDriver;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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
	private ValueChangeListener valueChangeListenrTable;
	private Table tblOptions;
	private Label lblParameter = new Label("");
	private BeanContainer<String, BeanOptions> beans;
	private Options cliopt;
	private String lblvalue;
	private boolean isRunning = false;
	private Button btnstartdriver;

	Thread t = new Thread(new Runnable() {

		@Override
		public void run() {
//			ProcessBuilder pb = new ProcessBuilder("java", "-jar", "/WEB-INF/lib/POCDriver.jar");
//			Process p;
//			try {
//				System.out.println("start poc driver");
//				p = pb.start();
//				updateStartButton();
//				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
//				String s = "";
//				while ((s = in.readLine()) != null) {
//					System.out.println(s);
//				}
//				int status = p.waitFor();
//				System.out.println("Exited with status: " + status);
//				System.out.println("POC Driver DONE");
//				isRunning = false;
//				updateStartButton();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			
			POCDriver.main(new String[] { lblvalue });
		}
	});
	private TextArea txtArea;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		System.setOut(new PrintStreamCapturer(txtArea, System.out));
		System.setErr(new PrintStreamCapturer(txtArea, System.err, "[ERROR] "));
		
		setUpEventHandling();
		final VerticalLayout layout = new VerticalLayout();

		Options cliopt = defineOptions();

		beans = new BeanContainer<String, BeanOptions>(BeanOptions.class);
		beans.setBeanIdProperty("opt");

		for (Option opt : cliopt.getOptions()) {
			beans.addBean(new BeanOptions(opt.getOpt(), opt.getLongOpt(), opt.getDescription(), "", false));
		}

		tblOptions = new Table("Load Generator Options", beans);
		tblOptions.setHeight("100%");
		tblOptions.addValueChangeListener(valueChangeListenrTable);

		tblOptions.setVisibleColumns(new Object[] { "active", "opt", "textField", "longOpt", "description" });
		tblOptions.setColumnHeaders(new String[] { "active", "parameter", "value", "short desc", "desc" });
		tblOptions.setPageLength(tblOptions.size());

		Button btnSetInsertParameters = new Button("Set Insert Parameters");
		btnSetInsertParameters.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 3431741180099701268L;

			@Override
			public void buttonClick(ClickEvent event) {
				setFillDBParameters();
			}
		});

		Button btnSetQueryParameters = new Button("Set Query only Parameters");
		btnSetQueryParameters.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				setQueryOnlyParameters();
			}
		});

		Button btnSetMyParameters = new Button("Set My Parameters");
		btnSetMyParameters.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				setCurrentParameters();
			}
		});

		Button btnResetMyParameters = new Button("Reset Parameters");
		btnResetMyParameters.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				resetParameters();
				updateParameterLabel();
			}
		});

		btnstartdriver = new Button("Start Driver");
		btnstartdriver.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (!t.isAlive()) {
					t.start();
				} else {
					t.interrupt();
				}
				isRunning = !isRunning;
				updateStartButton();
			}
		});

		HorizontalLayout layoutbuttons = new HorizontalLayout();
		layoutbuttons.setMargin(true);
		layoutbuttons.setSpacing(true);
		layoutbuttons.addComponent(btnResetMyParameters);
		layoutbuttons.addComponent(btnSetInsertParameters);
		layoutbuttons.addComponent(btnSetQueryParameters);
		layoutbuttons.addComponent(btnSetMyParameters);
		layoutbuttons.addComponent(btnstartdriver);

		HorizontalLayout layoutlbl = new HorizontalLayout();
		layoutlbl.addComponent(lblParameter);

		txtArea = new TextArea();
		txtArea.setWidth("100%");
		
		Label h1 = new Label("Visual PoC Driver");
		h1.addStyleName("h1");
		layout.addComponent(h1);
		layout.addComponent(txtArea);
		layout.addComponent(layoutbuttons);
		layout.addComponent(layoutlbl);
		layout.addComponent(tblOptions);

		// POCDriver pocdriver = new POCDriver();
		// pocdriver.main(new String[]{"A", "B"});

		layout.setMargin(true);
		layout.setSpacing(true);

		setContent(layout);
	}

	private Options defineOptions() {
		cliopt = new Options();
		cliopt.addOption("a", "arrays", true, "Shape of any arrays in new sample records x:y so -a 12:60 adds an array of 12 length 60 arrays of integers");
		cliopt.addOption("b", "bulksize", true, "Bulk op size (default 512)");
		cliopt.addOption("c", "host", true, "Mongodb connection details (default 'mongodb://localhost:27017' )");
		cliopt.addOption("d", "duration", true, "Test duration in seconds, default 18,000");
		cliopt.addOption("e", "empty", false, "Remove data from collection on startup");
		cliopt.addOption("f", "numfields", true, "Number of top level fields in test records (default 10)");
		cliopt.addOption("g", "arrayupdates", true, "Ratio of array increment ops requires option 'a' (default 0)");
		cliopt.addOption("h", "help", false, "Show Help");
		cliopt.addOption("i", "inserts", true, "Ratio of insert operations (default 100)");
		cliopt.addOption("j", "workingset", true, "Percentage of database to be the working set (default 100)");
		cliopt.addOption("k", "keyqueries", true, "Ratio of key query operations (default 0)");
		cliopt.addOption("l", "textfieldsize", true, "Length of text fields in bytes (default 30)");
		cliopt.addOption("m", "findandmodify", false, "Use findandmodify instead of update and retireve record (with -u or -v only)");
		cliopt.addOption("n", "namespace", true, "Namespace to use , for example myDatabase.myCollection");
		cliopt.addOption("o", "logfile", true, "Output stats to  <file> ");
		cliopt.addOption("p", "print", false, "Print out a sample record according to the other parameters then quit");
		cliopt.addOption("q", "opsPerSecond", true, "Try to rate limit the total ops/s to the specified ammount");
		cliopt.addOption("r", "rangequeries", true, "Ratio of range query operations (default 0)");
		cliopt.addOption("s", "slowthreshold", true, "Slow operation threshold in ms(default 50)");
		cliopt.addOption("t", "threads", true, "Number of threads (default 4)");
		cliopt.addOption("u", "updates", true, "Ratio of update operations (default 0)");
		cliopt.addOption("v", "workflow", true, "Specify a set of ordered operations per thread from [iukp]");
		cliopt.addOption("w", "nosharding", false, "Do not shard the collection");
		cliopt.addOption("x", "indexes", true, "Number of secondary indexes - does not remove existing (default 0)");
		return cliopt;
	}

	private void setUpEventHandling() {
		valueChangeListenrTable = new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				for (String id : beans.getItemIds()) {
					BeanOptions b = beans.getItem(id).getBean();
					if (!b.getTextField().getValue().isEmpty())
						System.out.println(b.getTextField().getValue());
				}
			}
		};
	}

	private void setFillDBParameters() {
		// -r 20 -k 20 -g 30 -a 5:10 -f 7 -i 40 -u 30 -c mongodb://tim:test@127.0.0.1:27000/test
		unsetParameters();
		setBeanParameters("r", "20", true);
		setBeanParameters("k", "20", true);
		setBeanParameters("g", "30", true);
		setBeanParameters("a", "5:10", true);
		setBeanParameters("f", "7", true);
		setBeanParameters("i", "40", true);
		setBeanParameters("u", "30", true);
		setBeanParameters("c", "mongodb://tim:test@127.0.0.1:27000/test", true);

		updateParameterLabel();

		beans.sort(new Object[] { "opt", "active" }, new boolean[] { true });
	}

	private void updateParameterLabel() {
		String lblprefixvalue = "java -jar POCDriver.jar ";
		lblvalue = "";
		for (String id : beans.getItemIds()) {
			BeanOptions bean = beans.getItem(id).getBean();
			if (!bean.getTextField().getValue().isEmpty() && bean.getActive().getValue()) {
				lblvalue += " -" + bean.getOpt() + " " + bean.getTextField().getValue();
			}
		}
		lblParameter.setValue(lblprefixvalue + lblvalue);
	}

	private void setQueryOnlyParameters() {
		// -r 20 -k 20 -g 30 -a 5:10 -u 20 -i 0 -s 20 -c mongodb://tim:test@127.0.0.1:27000/test
		unsetParameters();
		setBeanParameters("r", "20", true);
		setBeanParameters("k", "20", true);
		setBeanParameters("g", "30", true);
		setBeanParameters("a", "5:10", true);
		setBeanParameters("u", "30", true);
		setBeanParameters("i", "40", true);
		setBeanParameters("s", "20", true);
		setBeanParameters("c", "mongodb://tim:test@127.0.0.1:27000/test", true);

		updateParameterLabel();
		beans.sort(new Object[] { "opt", "active" }, new boolean[] { true });
	}

	private void setBeanParameters(String id, String value, boolean active) {
		BeanOptions bean = beans.getItem(id).getBean();
		beans.removeItem(id);
		bean.setActive(active);
		bean.setTextFieldValue(value);
		beans.addItem(id, bean);
	}

	private void setCurrentParameters() {
		updateParameterLabel();
	}

	private void unsetParameters() {
		for (String id : beans.getItemIds()) {
			BeanOptions b = beans.getItem(id).getBean();
			b.setActive(false);
			b.setTextFieldValue("");
		}
	}

	private void resetParameters() {
		beans.removeAllItems();
		for (Option opt : cliopt.getOptions()) {
			beans.addBean(new BeanOptions(opt.getOpt(), opt.getLongOpt(), opt.getDescription(), "", false));
		}
	}

	private void updateStartButton() {
		System.out.println("is Running: " + isRunning);
		if (!isRunning) {
			btnstartdriver.setCaption("Stop Driver");
		} else {
			btnstartdriver.setCaption("Stopping Driver");
			btnstartdriver.setCaption("Start Driver");
		}
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
