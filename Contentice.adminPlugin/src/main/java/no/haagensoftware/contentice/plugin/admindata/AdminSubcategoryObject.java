package no.haagensoftware.contentice.plugin.admindata;

import no.haagensoftware.contentice.data.SubCategoryData;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/23/13
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminSubcategoryObject {
    private SubCategoryData subcategory;

    public AdminSubcategoryObject() {
    }

    public SubCategoryData getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(SubCategoryData subcategory) {
        this.subcategory = subcategory;
    }
}
