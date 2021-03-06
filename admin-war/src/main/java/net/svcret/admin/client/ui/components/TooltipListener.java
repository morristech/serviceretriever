package net.svcret.admin.client.ui.components;

import net.svcret.admin.client.MyResources;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/*
 Tooltip component for GWT
 Copyright (C) 2006 Alexei Sokolov http://gwt.components.googlepages.com/

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA

 */
public class TooltipListener implements MouseOverHandler, MouseOutHandler {
	private static final int DEFAULT_OFFSET_X = 10;
	private static final int DEFAULT_OFFSET_Y = 35;
	private static final String DEFAULT_TOOLTIP_STYLE = "tooltipPopup";

	private int offsetX = DEFAULT_OFFSET_X;
	private int offsetY = DEFAULT_OFFSET_Y;

	private String styleName;
	private String text;
	private Tooltip tooltip;

	public TooltipListener(String text) {
		this(text, DEFAULT_TOOLTIP_STYLE);
	}

	public TooltipListener(String text, String styleName) {
		this.text = text;
		this.styleName = styleName;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public String getStyleName() {
		return styleName;
	}

	@Override
	public void onMouseOut(MouseOutEvent theArg0) {
		if (tooltip != null) {
			tooltip.hide();
		}

	}

	@Override
	public void onMouseOver(MouseOverEvent theArg0) {
		if (tooltip != null) {
			tooltip.hide();
		}
		tooltip = new Tooltip((Widget) theArg0.getSource(), text);
		tooltip.show();
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public static class Tooltip extends PopupPanel {
		private int myDelay;
		private Element mySender;
		private Timer myTimer;
		private int offsetX = DEFAULT_OFFSET_X;
		private int offsetY = DEFAULT_OFFSET_Y;
		private com.google.gwt.dom.client.Element mySender2;

		public Tooltip(Widget sender, final String text) {
			this(sender, new HTML(text));
		}

		public Tooltip(Element sender, Widget theContents) {
			this(theContents);
			this.mySender = sender;
		}

		public Tooltip(Widget theContents) {
			super(true);
			this.myDelay = 60000;

			add(theContents);

			setStyleName(MyResources.CSS.tooltipPopup());
		}

		public Tooltip(Widget theSender, Widget theTooltip) {
			this(theTooltip);
			mySender = theSender.getElement();
		}

		public Tooltip(com.google.gwt.dom.client.Element theSender, Widget theTooltip) {
			this(theTooltip);
			mySender2 = theSender;
		}

		public void displayPopup() {

			setPopupPositionAndShow(new PositionCallback() {
				@Override
				public void setPosition(int theOffsetWidth, int theOffsetHeight) {
					int left;
					int top;
					if (mySender2 != null) {
						left = mySender2.getAbsoluteLeft()+offsetX;
						top = mySender2.getAbsoluteTop()+offsetY;
					} else {
						left = DOM.getAbsoluteLeft(mySender) + offsetX;
						top = DOM.getAbsoluteTop(mySender) + offsetY;
					}

					int windowClientWidth = Window.getClientWidth();
					int rightBorder = left + theOffsetWidth + 50;
					if (rightBorder > windowClientWidth) {
						left = left - theOffsetWidth;
					}

					int windowClientHeight = Window.getClientHeight();
					int bottomBorder = top + theOffsetHeight + 50;
					if (bottomBorder > windowClientHeight) {
						top = top - theOffsetHeight;
						top = top - 50;
					}

					setPopupPosition(left, top);
				}
			});

			myTimer = new Timer() {
				@Override
				public void run() {
					Tooltip.super.hide();
				}

			};
			myTimer.schedule(myDelay);
		}

		public void hideTooltip() {
			super.hide();

			if (myTimer != null) {
				myTimer.cancel();
			}
		}

	}
}