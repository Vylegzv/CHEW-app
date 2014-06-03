package com.vanderbilt.isis.chew;

public class MainListRowItem {

	private int imageId;
	private String title;
	private String subtitle;

	public MainListRowItem(int imageId, String title, String subtitle) {
		this.imageId = imageId;
		this.title = title;
		this.subtitle = subtitle;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getDesc() {
		return subtitle;
	}

	public void setDesc(String desc) {
		this.subtitle = desc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return title + "\n" + subtitle;
	}
}
