package com.simpleir.wiki.model;

public class Article
{
	private String title;
	private long id;
	private String text;

	public Article()
	{
	}

	public Article(String title, long id, String text)
	{
		this.title = title;
		this.id = id;
		this.text = text;
	}

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public long getId() { return id; }
	public void setId(long id) { this.id = id; }
	
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }

	@Override
	public int hashCode()
	{
		return (int) id;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Article)
		{
			Article a = (Article) obj;
			return a.id == this.id && a.title.equals(this.title) && a.text.equals(this.text);
		}

		return false;
	}
}