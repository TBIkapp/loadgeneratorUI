package com.mongodb.loadgenerator;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class MyUIData extends VerticalLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = -643961441094143238L;
	private Table tblOptions;
	private Label lblParameter = new Label("");
	private BeanContainer<String, BeanOptions> beans;
	private Options cliopt;
	private Button btnstartdriver;
	private TextArea logArea;
	private String lblvalue = "";
	private boolean isRunning = false;
	private MyUI ui;
	private Button btnSetInsertParameters;
	private Button btnSetQueryParameters;
	private Button btnSetMyParameters;
	private Button btnResetMyParameters;

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

	public MyUIData(MyUI ui) {
		this.ui = ui;
		cliopt = defineOptions();

		beans = new BeanContainer<String, BeanOptions>(BeanOptions.class);
		beans.setBeanIdProperty("opt");

		for (Option opt : cliopt.getOptions()) {
			beans.addBean(new BeanOptions(opt.getOpt(), opt.getLongOpt(), opt.getDescription(), "", false));
		}

		tblOptions = new Table("Load Generator Options", beans);
		tblOptions.setHeight("100%");
		// tblOptions.addValueChangeListener(valueChangeListenrTable);

		tblOptions.setVisibleColumns(new Object[] { "active", "opt", "textField", "longOpt", "description" });
		tblOptions.setColumnHeaders(new String[] { "active", "parameter", "value", "short desc", "desc" });
		tblOptions.setPageLength(tblOptions.size());

		btnSetInsertParameters = new Button("Set Insert Parameters");
		btnSetInsertParameters.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 3431741180099701268L;

			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println("Insert Parameters");
				setFillDBParameters();
			}
		});

		btnSetQueryParameters = new Button("Set Query only Parameters");
		btnSetQueryParameters.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println("Query Only Parameters");
				setQueryOnlyParameters();
			}
		});

		btnSetMyParameters = new Button("Set My Parameters");
		btnSetMyParameters.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println("My selected Parameters");
				setCurrentParameters();
			}
		});

		btnResetMyParameters = new Button("Reset Parameters");
		btnResetMyParameters.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println("Reset Parameters");
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
				if (!isRunning) {
					btnstartdriver.setCaption("Stop Driver");
					log("++++ Start the driver ++++");
					activateConfigButtons(false);
					ui.startThread();
				} else {
					btnstartdriver.setCaption("Stopping Driver");
					log("==== Stop the driver ====");
					ui.stopThread();
					activateConfigButtons(true);
					btnstartdriver.setCaption("Start Driver");
				}

				isRunning = !isRunning;

			}

			private void log(String string) {
				logArea.setCursorPosition(logArea.getValue().length());
				logArea.setValue(logArea.getValue() + "\n" + string);
				logArea.setCursorPosition(logArea.getValue().length());
			}

			private void activateConfigButtons(boolean flag) {
				btnResetMyParameters.setEnabled(flag);
				btnSetInsertParameters.setEnabled(flag);
				btnSetMyParameters.setEnabled(flag);
				btnSetQueryParameters.setEnabled(flag);
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

		setTxtArea(new TextArea());
		getTxtArea().setWidth("100%");

		Label h1 = new Label("Visual PoC Driver");
		h1.addStyleName("h1");
		this.addComponent(h1);
		this.addComponent(getTxtArea());
		this.addComponent(layoutbuttons);
		this.addComponent(layoutlbl);
		this.addComponent(tblOptions);

		// POCDriver pocdriver = new POCDriver();
		// pocdriver.main(new String[]{"A", "B"});

		this.setMargin(true);
		this.setSpacing(true);

	}

	void setFillDBParameters() {
		// -r 20 -k 20 -g 30 -a 5:10 -f 7 -i 40 -u 30 -c mongodb://tim:test@127.0.0.1:27000/test
		unsetParameters();
		setBeanParameters("r", "20", true);
		setBeanParameters("k", "20", true);
		setBeanParameters("g", "30", true);
		setBeanParameters("a", "5:10", true);
		setBeanParameters("f", "7", true);
		setBeanParameters("i", "40", true);
		setBeanParameters("u", "30", true);
		setBeanParameters("c", "'mongodb://127.0.0.1:27000/test'", true);

		updateParameterLabel();

		beans.sort(new Object[] { "opt", "active" }, new boolean[] { true });
	}

	void updateParameterLabel() {
		String lblprefixvalue = "java -jar POCDriver.jar ";
		setLblvalue("");
		for (String id : beans.getItemIds()) {
			BeanOptions bean = beans.getItem(id).getBean();
			if (!bean.getTextField().getValue().isEmpty() && bean.getActive().getValue()) {
				setLblvalue(getLblvalue() + " -" + bean.getOpt() + " " + bean.getTextField().getValue());
			}
		}
		lblParameter.setValue(lblprefixvalue + getLblvalue());
	}

	void setQueryOnlyParameters() {
		// -r 20 -k 20 -g 30 -a 5:10 -u 20 -i 0 -s 20 -c mongodb://tim:test@127.0.0.1:27000/test
		unsetParameters();
		setBeanParameters("r", "20", true);
		setBeanParameters("k", "20", true);
		setBeanParameters("g", "30", true);
		setBeanParameters("a", "5:10", true);
		setBeanParameters("u", "30", true);
		setBeanParameters("i", "40", true);
		setBeanParameters("s", "20", true);
		setBeanParameters("c", "'mongodb://127.0.0.1:27000/test'", true);

		updateParameterLabel();
		beans.sort(new Object[] { "opt", "active" }, new boolean[] { true });
	}

	void setBeanParameters(String id, String value, boolean active) {
		BeanOptions bean = beans.getItem(id).getBean();
		beans.removeItem(id);
		bean.setActive(active);
		bean.setTextFieldValue(value);
		beans.addItem(id, bean);
	}

	void setCurrentParameters() {
		updateParameterLabel();
	}

	void unsetParameters() {
		for (String id : beans.getItemIds()) {
			BeanOptions b = beans.getItem(id).getBean();
			b.setActive(false);
			b.setTextFieldValue("");
		}
	}

	void resetParameters() {
		beans.removeAllItems();
		for (Option opt : cliopt.getOptions()) {
			beans.addBean(new BeanOptions(opt.getOpt(), opt.getLongOpt(), opt.getDescription(), "", false));
		}
	}

	public String getLblvalue() {
		return lblvalue;
	}

	public void setLblvalue(String lblvalue) {
		this.lblvalue = lblvalue;
	}

	public TextArea getTxtArea() {
		return logArea;
	}

	public void setTxtArea(TextArea txtArea) {
		this.logArea = txtArea;
	}
}
