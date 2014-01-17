package no.haagensoftware.contentice.plugin;

import io.netty.channel.ChannelHandler;
import no.haagensoftware.contentice.plugin.handler.*;
import no.haagensoftware.contentice.spi.RouterPlugin;
import org.apache.log4j.Logger;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/19/13
 * Time: 5:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminRoutesPlugin extends RouterPlugin {
    private static final Logger logger = Logger.getLogger(AdminRoutesPlugin.class.getName());

    LinkedHashMap<String, Class<? extends ChannelHandler>> routeMap;

    public AdminRoutesPlugin() {
        logger.info("Initializing AdminRoutesPlugin");
        routeMap = new LinkedHashMap<>();
        routeMap.put("/json/admin/categories", AdminCategoriesHandler.class);

        routeMap.put("/json/admin/categories/{category}", AdminCategoryHandler.class);

        routeMap.put("/json/admin/categories/{category}/subcategories", AdminSubcategoriesHandler.class);
        routeMap.put("/json/admin/subcategories", AdminSubcategoriesHandler.class);

        routeMap.put("/json/admin/categories/{category}/subcategories/{subcategory}", AdminSubcategoryHandler.class);
        routeMap.put("/json/admin/subcategories/{subcategory}", AdminSubcategoryHandler.class);

        routeMap.put("/json/admin/categoryFields", AdminCategoryFieldsHandler.class);
        routeMap.put("/json/admin/categoryFields/{categoryField}", AdminCategoryFieldsHandler.class);

        routeMap.put("/json/admin/subcategoryFields", AdminSubcategoryFieldsHandler.class);
        routeMap.put("/json/admin/subcategoryFields/{subcategoryField}", AdminSubcategoryFieldsHandler.class);

        routeMap.put("startsWith:/admin", AdminPagesHandler.class);
    }

    @Override
    public LinkedHashMap<String,Class<? extends ChannelHandler>> getRoutes() {
        return routeMap;
    }

    @Override
    public Class<? extends ChannelHandler> getHandlerForRoute(String route) {
        return routeMap.get(route);
    }
}
