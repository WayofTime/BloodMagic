package WayofTime.alchemicalWizardry.common.items.potion;

public class WeakBindingAgent extends StandardBindingAgent
{
    public WeakBindingAgent()
    {
        super();
    }

    @Override
    public float getSuccessRateForPotionNumber(int potions)
    {
        return (float) Math.pow(0.4, potions);
    }
}
