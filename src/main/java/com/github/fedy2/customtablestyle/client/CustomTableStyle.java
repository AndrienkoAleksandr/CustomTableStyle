package com.github.fedy2.customtablestyle.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.CellTable.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.view.client.ListDataProvider;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CustomTableStyle implements EntryPoint {
	
	interface CustomResources extends Resources {
		
	    @Source("CellTableCustom.css")
	    CustomStyle cellTableStyle();
	}
	
	interface CustomStyle extends Style {
	}
	
	
	protected static Resources DEFAULT_RESOURCE = GWT.create(Resources.class);
	protected static CustomResources CUSTOM_RESOURCE = GWT.create(CustomResources.class);
	
	protected CustomCellTableBuilder<Person> builder;
	protected CellTable<Person> table;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		DEFAULT_RESOURCE.cellTableStyle().ensureInjected();
		CUSTOM_RESOURCE.cellTableStyle().ensureInjected();
		
		DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);
		
		FlowPanel toolBar = setupToolBar();
		mainPanel.addNorth(toolBar, 30);
		
		table = setupCellTable();
		mainPanel.add(table);
		
		RootLayoutPanel.get().add(mainPanel);
	}
	
	protected FlowPanel setupToolBar() {
		FlowPanel buttonBar = new FlowPanel();
		
		Button defaultStyle = new Button("Default");
		defaultStyle.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				setStyle(DEFAULT_RESOURCE.cellTableStyle());
			}
		});
		buttonBar.add(defaultStyle);
		
		Button customStyle = new Button("Custom");
		customStyle.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				setStyle(CUSTOM_RESOURCE.cellTableStyle());
			}
		});
		buttonBar.add(customStyle);
		
		return buttonBar;
	}
	
	protected void setStyle(Style style) {
		builder.setStyle(new CustomCellTableBuilder.StyleAdapter(style));
		table.redraw();
	}
	
	protected CellTable<Person> setupCellTable() {
		CellTable<Person> table = new CellTable<Person>();

		Column<Person, String> nameColumn = new Column<Person, String>(new TextCell()) {
			
			@Override
			public String getValue(Person person) {
				return person.name;
			}
		};
		table.addColumn(nameColumn, "Name");
		
		Column<Person, String> surnameColumn = new Column<Person, String>(new TextCell()) {
			
			@Override
			public String getValue(Person person) {
				return person.surname;
			}
		};
		table.addColumn(surnameColumn, "Surname");
		
		Column<Person, Number> ageColumn = new Column<Person, Number>(new NumberCell()) {
			
			@Override
			public Number getValue(Person person) {
				return person.age;
			}
		};
		table.addColumn(ageColumn, "Age");
		
		ListDataProvider<Person> dataProvider = getDataProvider();
		dataProvider.addDataDisplay(table);
		
		
		builder = new CustomCellTableBuilder<CustomTableStyle.Person>(table);
		table.setTableBuilder(builder);

		
		return table;
	}
	
	protected ListDataProvider<Person> getDataProvider() {
		List<Person> data = new ArrayList<Person>();
		data.add(new Person("John","Smith",32));
		data.add(new Person("Eddie","Punchclock",45));
		data.add(new Person("Joe","Botts",23));
		data.add(new Person("Vinnie","Boombotz",61));
		return new ListDataProvider<Person>(data);
	}
	
	protected class Person {
		String name;
		String surname;
		int age;
		
		/**
		 * @param name
		 * @param surname
		 * @param age
		 */
		public Person(String name, String surname, int age) {
			this.name = name;
			this.surname = surname;
			this.age = age;
		}
	}
}
