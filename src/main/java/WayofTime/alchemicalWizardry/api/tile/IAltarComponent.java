package WayofTime.alchemicalWizardry.api.tile;

public interface IAltarComponent {

    ComponentType getType();

    enum ComponentType {
        GLOWSTONE,
        BLOODSTONE,
        BEACON,
        BLOODRUNE,
        CRYSTAL
    }
}
