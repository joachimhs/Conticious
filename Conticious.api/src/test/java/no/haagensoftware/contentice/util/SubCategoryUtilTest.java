package no.haagensoftware.contentice.util;

import com.google.gson.Gson;
import no.haagensoftware.contentice.data.SubCategoryData;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhsmbp on 05/04/15.
 */
public class SubCategoryUtilTest {

    @Test
    public void testSubCategorySerializer() {
        TestSession session = new TestSession();
        session.setAuthenticated(true);
        session.setId("abcabvabv");
        session.setLastAccessed(System.currentTimeMillis());

        TestUser user = new TestUser();
        user.setRole("admin");
        user.setId("joachim@haagen-soft.no");
        user.setPassword("123abc124abd");

        session.setUser(user);

        TestUser user2 = new TestUser();
        user2.setRole("role2");
        user2.setId("test@haagen-soft.no");
        user2.setPassword("987987");

        TestUser user3 = new TestUser();
        user3.setRole("role4");
        user3.setId("test2@hs.no");
        user3.setPassword("777888222a");

        session.getUsers().add(user2);
        session.getUsers().add(user3);

        System.out.println(new Gson().toJson(session));

        SubCategoryData sd = SubCategoryUtil.convertObjectToSubCateogory(session);

        System.out.println(new Gson().toJson(sd));
    }
}
