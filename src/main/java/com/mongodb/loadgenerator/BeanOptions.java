package com.mongodb.loadgenerator;

import java.io.Serializable;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextField;

public class BeanOptions implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9018929944595965271L;
	private TextField textField;
	private CheckBox active;
	private String opt;
	private String longOpt;
	private String description;

	public BeanOptions(String opt, String longOpt, String description, String value, boolean active) {
		this.setTextFieldValue(value);
		this.setActive(active);
		this.setOpt(opt);
		this.setLongOpt(longOpt);
		this.setDescription(description);
	}

	public TextField getTextField() {
		return textField;
	}

	public void setTextFieldValue(String value) {
		this.textField = new TextField(value);
		this.textField.setValue(value);
	}

	public CheckBox getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = new CheckBox();
		this.active.setValue(active);
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	public String getLongOpt() {
		return longOpt;
	}

	public void setLongOpt(String longOpt) {
		this.longOpt = longOpt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return opt + " " + description + " " + textField.getValue();
	}
}
