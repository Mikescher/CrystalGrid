package gridShooter.game.menu;

import gridShooter.game.GameArt;
import gridShooter.game.GameScreen;

import java.awt.Font;

import de.abscanvas.input.MouseButtons;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.elements.GUIButton;
import de.abscanvas.ui.gui.elements.GUIEdit;
import de.abscanvas.ui.gui.elements.GUINativeLabel;
import de.abscanvas.ui.listener.ButtonListener;

public class SetNameMenu extends GUIMenu implements ButtonListener {
	private final static int BTN_BACK = -12;
	private final static int BTN_OK = 10;
	
	private GUIEdit edName;
	private GUIButton btnOK;
	private GUIButton btnBack;
	private GUINativeLabel lblMsg;
	private GUINativeLabel lblDesc;
	
	private GameScreen owner;
	private final boolean force;
	
	public SetNameMenu(GameScreen owner, boolean force) {
		super(owner);
		this.owner = owner;
		this.force = force;
		create();
	}

	public void create() {
		setImage(GameArt.cover_small);
		
		lblDesc = new GUINativeLabel(450, 300, "CREATE A NEW USER", this);
		addElement(lblDesc);
		
		if (! force) {
			btnBack = new GUIButton(32, getOwner().getScreenHeight() - 67, GameArt.btn_back, BTN_BACK, this);
			btnBack.addListener(this);
			addElement(btnBack);
			
			lblMsg = new GUINativeLabel(250, 650, "(THIS WILL RESET YOUR SCORE)", this);
			addElement(lblMsg);
		}
		
		edName = new GUIEdit(472, 350, 464, 64, owner.getKeys(), this);
		edName.setAcceptanceMode(GUIEdit.ACCEPT_NUMBERANDLETTER);
		edName.setFont(new Font("Arial", 0, 50));
		edName.setMaxLength(12);
		edName.doubleThickBorder(true);
		edName.doubleThickCursor(true);
		edName.setText("USERNAME");
		edName.setClearOnFirstClick();
		edName.setFocused(true);
		addElement(edName);
		
		btnOK = new GUIButton(472, 450, GameArt.btn_set, BTN_OK, this);
		btnOK.addListener(this);
		addElement(btnOK);
	}

	@Override
	public void buttonMouseDown(MouseButtons arg0, int arg1) {
		// nocode
	}

	@Override
	public void buttonMouseEnter(MouseButtons arg0, int arg1) {
		// nocode
	}

	@Override
	public void buttonMouseLeave(MouseButtons arg0, int arg1) {
		// nocode
	}

	@Override
	public void buttonPressed(MouseButtons arg0, int id) {
		switch (id) {
		case BTN_BACK:
			owner.popMenu();
			break;
		case BTN_OK:
			if (! edName.getText().isEmpty()) {
				owner.getPFile().reset();
				owner.getPFile().setNameAndID(edName.getText(), owner.getHighscore().getNewID());
				owner.getHighscore().updateHighscore(owner.getPFile().getUsername(), owner.getPFile().getScore(), owner.getPFile().getUserID());
				owner.popMenu();
			}
			break;
		}
	}

}
