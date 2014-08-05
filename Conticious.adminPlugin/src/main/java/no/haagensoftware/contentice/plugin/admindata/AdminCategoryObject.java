package no.haagensoftware.contentice.plugin.admindata;

import no.haagensoftware.contentice.data.CategoryData;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/23/13
 * Time: 1:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminCategoryObject {
    private CategoryData category;

    public AdminCategoryObject() {
    }

    public CategoryData getCategory() {
        return category;
    }

    public void setCategory(CategoryData category) {
        this.category = category;
    }
}
