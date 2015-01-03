package net.cloudhacking.androidgame2;

/**
 * Created by wcauchois on 12/31/14.
 */
public class GameServices {
    private GameObjectManager manager;
    private ResourceManager resourceManager;
    private DisplayMetricsService displayMetricsService;

    public GameServices(GameObjectManager manager, ResourceManager resourceManager,
                        DisplayMetricsService displayMetricsService) {
        this.manager = manager;
        this.resourceManager = resourceManager;
        this.displayMetricsService = displayMetricsService;
    }

    public GameObjectManager getManager() {
        return manager;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public DisplayMetricsService getDisplayMetricsService() {
        return displayMetricsService;
    }
}
