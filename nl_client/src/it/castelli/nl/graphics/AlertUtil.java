package it.castelli.nl.graphics;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class AlertUtil
{
	public static Optional<String> showTextInputDialogue(String defaultValue, String title, String headerText,
			String contentText)
	{
		TextInputDialog dialog = new TextInputDialog(defaultValue);
		dialog.setTitle(title);
		dialog.setHeaderText(headerText);
		dialog.setContentText(contentText);
		return dialog.showAndWait();
	}

	public static Optional<ButtonType> showConfirmationAlert(String title, String headerText, String contentText)
	{
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		return alert.showAndWait();
	}
}
