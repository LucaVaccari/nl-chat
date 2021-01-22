package it.castelli.nl.graphics;

public class RGBColor
{
	private byte red, green, blue;

	public RGBColor(byte red, byte green, byte blue)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public byte getRed()
	{
		return red;
	}

	public byte getGreen()
	{
		return green;
	}

	public byte getBlue()
	{
		return blue;
	}
}
