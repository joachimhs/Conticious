package no.haagensoftware.contentice.plugin;

import io.netty.channel.ChannelHandler;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.plugin.handler.*;
import no.haagensoftware.contentice.spi.ConticiousPlugin;
import no.haagensoftware.contentice.spi.RouterPlugin;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

        routeMap.put("/json/admin/renameSubcategory/{oldSubcategory}/{newSubcategory}", AdminRenameSubcategoryHandler.class);

        routeMap.put("/json/admin/subcategoryFields", AdminSubcategoryFieldsHandler.class);
        routeMap.put("/json/admin/subcategoryFields/{subcategoryField}", AdminSubcategoryFieldsHandler.class);

        routeMap.put("/json/admin/addSubcategoryToRelation/{subcategoryToAdd}/{subcategoyFieldToAddTo}", AdminAddSubcategoryToFieldsHandler.class);

        routeMap.put("/json/admin/settings", SettingsHandler.class);
        routeMap.put("/json/admin/settings/{setting}", SettingsHandler.class);

        routeMap.put("/json/admin/users", AdminUserHandler.class);
        routeMap.put("/json/admin/users/{user}", AdminUserHandler.class);

        routeMap.put("/json/admin/auth", AdminAuthHandler.class);

        routeMap.put("/json/admin/spg/{domain}", SpgHandler.class);

        routeMap.put("/json/admin/postProcessors", AdminPostProcessorHandler.class);

        routeMap.put("/json/admin/fileUpload", AdminUploadHandler.class);

        routeMap.put("startsWith:/admin", AdminPagesHandler.class);
    }

    @Override
    public List<String> getPluginDependencies() {
        List<String> dependencies = new ArrayList<>();
        dependencies.add("AdminAuthenticationPlugin");
        return dependencies;
    }

    @Override
    public String getPluginName() {
        return "AdminRoutesPlugin";
    }

    @Override
    public LinkedHashMap<String,Class<? extends ChannelHandler>> getRoutes() {
        return routeMap;
    }

    @Override
    public ChannelHandler getHandlerForUrl(String url) {
        ChannelHandler handler = super.getHandlerForUrl(url);

        if (handler != null && handler instanceof ContenticeHandler) {
            for (ConticiousPlugin plugin : getDependantPluginsList()) {

            }

        }

        return handler;
    }

    @Override
    public Map<String, String> getPlurals() {
        Map<String, String> pluralMap = new LinkedHashMap<>();
        pluralMap.put("productCategory", "productCategories");
        pluralMap.put("prototypeCategory", "prototypeCategories");
        pluralMap.put("campaignSettingsCategory", "campaignSettingsCategories");
        pluralMap.put("figur", "figurer");

        return pluralMap;
    }
}
