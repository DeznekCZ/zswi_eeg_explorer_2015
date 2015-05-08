package cz.eeg.ui.dialog;

import cz.deznekcz.reflect.Out;
import cz.eeg.data.EegFile;
import cz.eeg.data.Marker;

/**
 * {@link DialogType} represents an type of dialog 
 * call able in {@link DialogManagement}
 *
 * @author IT Crowd
 */
public enum DialogType {
	/** Parameters:
	 * <br>- none */
	ABOUT,
	/** Parameters:
	 * <br>- {@link String} error message */
	ERROR,
	/** Parameters: 
	 * <br>- {@link EegFile} instance to save 
	 * <br>- {@link Out}&lt;{@link Boolean}&gt; return able value (true - save successful) */
	FILE_SAVE,
	/** Parameters:
	 * <br>- {@link File[]} list of files */
	FILE_DELETE,
	/** Parameters:
	 * <br>- {@link EegFile} instance with data to plot */
	GRAPH_PLOT,
	/** Parameters:
	 * <br>- {@link Marker} instance to edit
	 * <br>- {@link String} name of setter method
	 * <br>- {@link String} default value
	 * <br>- {@link EegFile} instance that owns marker */
	MARKER_EDIT,
	/** Parameters:
	 * <br>- {@link Out}&lt;{@link Boolean}&gt; return able value (new created name of scenario) */
	SCENARIO_ADD,
	/** Parameters:
	 * <br>- {@link String} name of scenario to remove */
	SCENARIO_REMOVE;
}
