package net.svcret.admin.client.ui.components;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;

public class HtmlPre extends Widget {

    public HtmlPre(String theText) {
        setElement(Document.get().createPreElement());
        setText(theText);
    }

	public void setText(String theText) {
		getElement().setInnerText(theText);
	}

	public String getText() {
		return getElement().getInnerText();
	}

}
