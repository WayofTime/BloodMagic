package wayoftime.bloodmagic.anointment;

public class AnointmentData
{
	private int level;
	private int damage;
	private int maxDamage;

	public AnointmentData(int level, int damage, int maxDamage)
	{
		this.level = level;
		this.damage = damage;
		this.maxDamage = maxDamage;
	}

	public int getLevel()
	{
		return this.level;
	}

	public int getDamage()
	{
		return this.damage;
	}

	public int getMaxDamage()
	{
		return this.maxDamage;
	}

	public void damage(int amount)
	{
		this.damage = Math.min(damage + amount, maxDamage);
	}

	public boolean isMaxDamage()
	{
		return damage >= maxDamage;
	}

	public String getDamageString()
	{
		return "" + (maxDamage - damage) + "/" + maxDamage;
	}
}
