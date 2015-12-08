/**
 * 
 */
package com.github.fedy2.customtablestyle.client;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.builder.shared.DivBuilder;
import com.google.gwt.dom.builder.shared.TableCellBuilder;
import com.google.gwt.dom.builder.shared.TableRowBuilder;
import com.google.gwt.dom.client.Style.OutlineStyle;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.AbstractCellTableBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.AbstractCellTable.Style;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.view.client.SelectionModel;

/**
 * @author "Federico De Faveri defaveri@gmail.com"
 *
 */
public class CustomCellTableBuilder<T> extends AbstractCellTableBuilder<T> {
	
	
	/**
	 * Class copied from {@link CellTable} where it is private...
	 *
	 */
	public static class StyleAdapter implements AbstractCellTable.Style {
	    private final CellTable.Style style;

	    public StyleAdapter(CellTable.Style style) {
	      this.style = style;
	    }

	    @Override
	    public String cell() {
	      return style.cellTableCell();
	    }

	    @Override
	    public String evenRow() {
	      return style.cellTableEvenRow();
	    }

	    @Override
	    public String evenRowCell() {
	      return style.cellTableEvenRowCell();
	    }

	    @Override
	    public String firstColumn() {
	      return style.cellTableFirstColumn();
	    }

	    @Override
	    public String firstColumnFooter() {
	      return style.cellTableFirstColumnFooter();
	    }

	    @Override
	    public String firstColumnHeader() {
	      return style.cellTableFirstColumnHeader();
	    }

	    @Override
	    public String footer() {
	      return style.cellTableFooter();
	    }

	    @Override
	    public String header() {
	      return style.cellTableHeader();
	    }

	    @Override
	    public String hoveredRow() {
	      return style.cellTableHoveredRow();
	    }

	    @Override
	    public String hoveredRowCell() {
	      return style.cellTableHoveredRowCell();
	    }

	    @Override
	    public String keyboardSelectedCell() {
	      return style.cellTableKeyboardSelectedCell();
	    }

	    @Override
	    public String keyboardSelectedRow() {
	      return style.cellTableKeyboardSelectedRow();
	    }

	    @Override
	    public String keyboardSelectedRowCell() {
	      return style.cellTableKeyboardSelectedRowCell();
	    }

	    @Override
	    public String lastColumn() {
	      return style.cellTableLastColumn();
	    }

	    @Override
	    public String lastColumnFooter() {
	      return style.cellTableLastColumnFooter();
	    }

	    @Override
	    public String lastColumnHeader() {
	      return style.cellTableLastColumnHeader();
	    }

	    @Override
	    public String oddRow() {
	      return style.cellTableOddRow();
	    }

	    @Override
	    public String oddRowCell() {
	      return style.cellTableOddRowCell();
	    }

	    @Override
	    public String selectedRow() {
	      return style.cellTableSelectedRow();
	    }

	    @Override
	    public String selectedRowCell() {
	      return style.cellTableSelectedRowCell();
	    }

	    @Override
	    public String sortableHeader() {
	      return style.cellTableSortableHeader();
	    }

	    @Override
	    public String sortedHeaderAscending() {
	      return style.cellTableSortedHeaderAscending();
	    }

	    @Override
	    public String sortedHeaderDescending() {
	      return style.cellTableSortedHeaderDescending();
	    }

	    @Override
	    public String widget() {
	      return style.cellTableWidget();
	    }
	  }

	private String evenRowStyle;
	private String oddRowStyle;
	private String selectedRowStyle;
	private String cellStyle;
	private String evenCellStyle;
	private String oddCellStyle;
	private String firstColumnStyle;
	private String lastColumnStyle;
	private String selectedCellStyle;

	public CustomCellTableBuilder(AbstractCellTable<T> cellTable) {
		super(cellTable);

		// Cache styles for faster access.
		Style style = cellTable.getResources().style();
		setStyle(style);
	}
	
	public void setStyle(Style style) {
		evenRowStyle = style.evenRow();
		oddRowStyle = style.oddRow();
		selectedRowStyle = " " + style.selectedRow();
		cellStyle = style.cell();
		evenCellStyle = " " + style.evenRowCell();
		oddCellStyle = " " + style.oddRowCell();
		firstColumnStyle = " " + style.firstColumn();
		lastColumnStyle = " " + style.lastColumn();
		selectedCellStyle = " " + style.selectedRowCell();
	}

	@Override
	public void buildRowImpl(T rowValue, int absRowIndex) {

		// Calculate the row styles.
		SelectionModel<? super T> selectionModel = cellTable.getSelectionModel();
		boolean isSelected =
				(selectionModel == null || rowValue == null) ? false : selectionModel.isSelected(rowValue);
		boolean isEven = absRowIndex % 2 == 0;
		StringBuilder trClasses = new StringBuilder(isEven ? evenRowStyle : oddRowStyle);
		if (isSelected) {
			trClasses.append(selectedRowStyle);
		}

		// Add custom row styles.
		RowStyles<T> rowStyles = cellTable.getRowStyles();
		if (rowStyles != null) {
			String extraRowStyles = rowStyles.getStyleNames(rowValue, absRowIndex);
			if (extraRowStyles != null) {
				trClasses.append(" ").append(extraRowStyles);
			}
		}

		// Build the row.
		TableRowBuilder tr = startRow();
		tr.className(trClasses.toString());

		// Build the columns.
		int columnCount = cellTable.getColumnCount();
		for (int curColumn = 0; curColumn < columnCount; curColumn++) {
			Column<T, ?> column = cellTable.getColumn(curColumn);
			// Create the cell styles.
			StringBuilder tdClasses = new StringBuilder(cellStyle);
			tdClasses.append(isEven ? evenCellStyle : oddCellStyle);
			if (curColumn == 0) {
				tdClasses.append(firstColumnStyle);
			}
			if (isSelected) {
				tdClasses.append(selectedCellStyle);
			}
			// The first and last column could be the same column.
			if (curColumn == columnCount - 1) {
				tdClasses.append(lastColumnStyle);
			}

			// Add class names specific to the cell.
			Context context = new Context(absRowIndex, curColumn, cellTable.getValueKey(rowValue));
			String cellStyles = column.getCellStyleNames(context, rowValue);
			if (cellStyles != null) {
				tdClasses.append(" " + cellStyles);
			}

			// Build the cell.
			HorizontalAlignmentConstant hAlign = column.getHorizontalAlignment();
			VerticalAlignmentConstant vAlign = column.getVerticalAlignment();
			TableCellBuilder td = tr.startTD();
			td.className(tdClasses.toString());
			if (hAlign != null) {
				td.align(hAlign.getTextAlignString());
			}
			if (vAlign != null) {
				td.vAlign(vAlign.getVerticalAlignString());
			}

			// Add the inner div.
			DivBuilder div = td.startDiv();
			div.style().outlineStyle(OutlineStyle.NONE).endStyle();

			// Render the cell into the div.
			renderCell(div, context, column, rowValue);

			// End the cell.
			div.endDiv();
			td.endTD();
		}

		// End the row.
		tr.endTR();
	}


}
