package cz.eeg.ui.dialog.savedialog;

import static cz.deznekcz.tool.Lang.LANG;

public enum Gender {
	
	MALE("m", LANG("dialog_save_gender_m")),
	FEMALE("f", LANG("dialog_save_gender_f")),
	NONE("-", LANG("dialog_save_gender_select"));
	
	private String shortcut;
	private String name;

	Gender(String shortcut, String name) {
        this.shortcut = shortcut;
        this.name = name;
    }
	
	@Override
	public String toString() {
		return name;
	}
	
	public String value() {
		return shortcut;
	}
}
